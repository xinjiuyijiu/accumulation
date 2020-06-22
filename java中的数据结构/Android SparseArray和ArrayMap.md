# SparseArray
针对HashMap存储key-value时，key为整型数据，使用SparseArray能够提高内存效率，避免了整形key的Auto-box以及key-value需要额外使用entry存储的问题，SparseArray在存储数据时，避免了散列表数组使用效率过低的问题。但是SparseArray在增删查操作的效率要低于HashMap，因为需要处理key的二分查找和数组移动,但是当数据量在几百时，差异不太大；
另外SparseArray针对key类型的扩展，包括了LongSparseArray（key为long类型）和SparseArray（key为int类型）；针对value类型的扩展，包括了SparseLongArray，SparseIntArray，SparseBooleanArray，和SparseArray（value为Object类型）；
```
    private int[] mKeys;
    private Object[] mValues;
```
通过mKeys数组升序存储整形的key值，这样使得key值可以通过二分查找的到索引值.而key对应的value值会存储在mValues相同索引值的位置，从而形成key-value的映射；
```
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;
```
mGarbage变量用来标记是否需要进行数组空间回收，在成功删除key-value后，mGarbage设为true；在gc()方法调用后，mGarbage设为false。DELETED 对象使用来标记被删除的key-value，在gc()方法被调用之前，所有的删除操作，都只是将对应key位置的value值设为DELETED 
```
    private int mSize;
```
mSize表示存在的key-value数量（包括DELETE）
```
    public void put(int key, E value) {
        // 通过二分查找得到key值在mKeys中的位置
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
        // i大于等于0，表示已经存在这样的key-value映射，直接替换value值即可
        if (i >= 0) {
            mValues[i] = value;
        } else {
          // i<0，没有找到这样的key，i值取反后得到的就是key需要插入的位置
            i = ~i;
           // 如果i所在的位置是一个被删除的无效数据，那么直接使用该位置；
           // 替换mKeys，mValues所在位置i的值  
            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }
           // 如果存在可回收位置，并且此时key-value数量已达到目前最大容量，那么可以尝试整理数组，将
           // DELETE的key-value真正移除
            if (mGarbage && mSize >= mKeys.length) {
                gc();
                
                // Search again because indices may have changed.
                // gc后重新得到新的位置（此时一定小于0，因为不存在这样的key）
                i = ~ContainerHelpers.binarySearch(mKeys, mSize, key);
            }
            // 将key-value插入到对应的数组中
            mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, key);
            mValues = GrowingArrayUtils.insert(mValues, mSize, i, value);
            mSize++;
        }
    }
```
```
static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;

        while (lo <= hi) {
            final int mid = (lo + hi) >>> 1;
            final int midVal = array[mid];

            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;  // value found
            }
        }
        // 此时的lo取值为[0,array.length]
        return ~lo;  // value not present
    }
```
进行二分查找，返回对应的位置；如果存在这样的value，返回大于等于0的位置；如果不存在这样的value，返回lo的取反，lo值表示的是value需要插入的对应位置，将它取反表示没有找到这样的值；
```
    private void gc() {
        // Log.e("SparseArray", "gc start with " + mSize);

        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        Object[] values = mValues;

        // 遍历数组，将所有值为DELETED的key-value删除，将数组左移对应的位置
        for (int i = 0; i < n; i++) {
            Object val = values[i];
      
            if (val != DELETED) {
                // i和o不相等，表示因为删除DELETED，使得i和o之间的数据需要左移，补充位置；
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                // o记录了数组中当前移除DELETED后的实际位置
                o++;
            }
        }

        mGarbage = false;
        mSize = o;

        // Log.e("SparseArray", "gc end with " + mSize);
    }
```
gc操作，将数组中的无效key-value删除，并通过左移数据，处理数组之间的空隙，提高存储效率；
```
    public static int[] insert(int[] array, int currentSize, int index, int element) {
        assert currentSize <= array.length;
        // 如果当前key-value数量小于数组长度，不需要扩容数组，直接插入到对应位置即可
        if (currentSize + 1 <= array.length) {
            // 将数组中index以及它后面的所有数据，向右移动一位
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }
        // 数组容量不够，需要扩容
        int[] newArray = new int[growSize(currentSize)];
        // 搬移旧数据到新数组
        System.arraycopy(array, 0, newArray, 0, index);
        // 修改index位置的值
        newArray[index] = element;
        // 将旧数组index以及它后面的所有数据，复制到新数组（index + 1）处
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    public static int growSize(int currentSize) {
        // 基本是2倍扩容了
        return currentSize <= 4 ? 8 : currentSize * 2;
    }
```

# ArrayMap
和SparseArray的核心思想类似，针对HashMap存储k-v，使用
```
    int[] mHashes;
    Object[] mArray;
```
mHashes升序地存储key的hash值；mArray根据key在mHashes中的位置，对应的存储key和value；

```
    public V put(K key, V value) {
        final int osize = mSize;
        final int hash;
        int index;
        // key为null，hash值默认为0，
        if (key == null) {
            hash = 0;
            // 查找key为null的k-v所在的位置
            index = indexOfNull();
        } else {
	// 获取key的hash值
            hash = mIdentityHashCode ? System.identityHashCode(key) : key.hashCode();
            index = indexOf(key, hash);
        }
	// index大于等于0，表示已经存在了对应的k-v，直接替换value的值
        if (index >= 0) {
            // index所对应的mArray中value的位置
            index = (index<<1) + 1;
            final V old = (V)mArray[index];
            mArray[index] = value;
            return old;
        }
	// index小于0，表示添加新的k-v
        index = ~index;
 	// k-v数量大于等于当前容量，需要扩容操作
        if (osize >= mHashes.length) {
	    // 指定扩容后的大小，4，8，或者1.5倍
            final int n = osize >= (BASE_SIZE*2) ? (osize+(osize>>1))
                    : (osize >= BASE_SIZE ? (BASE_SIZE*2) : BASE_SIZE);

            if (DEBUG) Log.d(TAG, "put: grow from " + mHashes.length + " to " + n);
	   
            final int[] ohashes = mHashes;
            final Object[] oarray = mArray;
	    // 重新初始化mHashes和mArray数组
            allocArrays(n);
	    // 在这期间，k-v数量发生变化
            if (CONCURRENT_MODIFICATION_EXCEPTIONS && osize != mSize) {
                throw new ConcurrentModificationException();
            }
	    // 将数据迁移到新的数组中
            if (mHashes.length > 0) {
                if (DEBUG) Log.d(TAG, "put: copy 0-" + osize + " to 0");
                System.arraycopy(ohashes, 0, mHashes, 0, ohashes.length);
                System.arraycopy(oarray, 0, mArray, 0, oarray.length);
            }
	    // 释放就数组数据
            freeArrays(ohashes, oarray, osize);
        }
	
	// 如果插入位置在数据之间，需要移动数据，将index位置留出来
        if (index < osize) {
            if (DEBUG) Log.d(TAG, "put: move " + index + "-" + (osize-index)
                    + " to " + (index+1));
            System.arraycopy(mHashes, index, mHashes, index + 1, osize - index);
            System.arraycopy(mArray, index << 1, mArray, (index + 1) << 1, (mSize - index) << 1);
        }

        if (CONCURRENT_MODIFICATION_EXCEPTIONS) {
            if (osize != mSize || index >= mHashes.length) {
                throw new ConcurrentModificationException();
            }
        }
	
	// 更新index位置处的数据
        mHashes[index] = hash;
        mArray[index<<1] = key;
        mArray[(index<<1)+1] = value;
        mSize++;
        return null;
    }
```
ArrayMap可插入[null,null]的数据，key的hash值可通过Object.hashCode获取或者key对象的hashCode方法获取，ArrayMap的扩容大小根据mHashes的长度，设为4，8，或者1.5倍大小；

```
    int indexOf(Object key, int hash) {
        final int N = mSize;
	// 没有数据，直接插入第0个位置
        // Important fast case: if nothing is in here, nothing to look for.
        if (N == 0) {
            return ~0;
        }
	
	// 通过二分查找获取hash位置
        int index = binarySearchHashes(mHashes, N, hash);

        // If the hash code wasn't found, then we have no entry for this key.
        if (index < 0) {
            return index;
        }

        // If the key at the returned index matches, that's what we want.
	// 如果存在这个key，表示查找到k-v
        if (key.equals(mArray[index<<1])) {
            return index;
        }

        // Search for a matching key after the index.
        int end;
	// 如果hash相同，key不相等，可能存在hash冲突的k-v，通过左右遍历，找到该值
	// 从index处向右遍历，尝试找到k-v
        for (end = index + 1; end < N && mHashes[end] == hash; end++) {
            if (key.equals(mArray[end << 1])) return end;
        }
	
        // Search for a matching key before the index.
	// 从index处向左遍历，尝试找到k-v
        for (int i = index - 1; i >= 0 && mHashes[i] == hash; i--) {
            if (key.equals(mArray[i << 1])) return i;
        }

        // Key not found -- return negative value indicating where a
        // new entry for this key should go.  We use the end of the
        // hash chain to reduce the number of array entries that will
        // need to be copied when inserting.
	// 如果左右遍历后仍没找到k-v，将新的数据插入到hash值相同的最后一个位置
        return ~end;
    }

```
ArrayMap遇到hash冲突时，会依次将新的数据插入到hash相同的最后一个位置；
```
    private void allocArrays(final int size) {
        if (mHashes == EMPTY_IMMUTABLE_INTS) {
            throw new UnsupportedOperationException("ArrayMap is immutable");
        }
	// 如果当前容量大小是8，尝试从缓存中拿到旧的mHashes和mArray
        if (size == (BASE_SIZE*2)) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCache != null) {
                    final Object[] array = mTwiceBaseCache;
                    mArray = array;
                    mTwiceBaseCache = (Object[])array[0];
                    mHashes = (int[])array[1];
                    array[0] = array[1] = null;
                    mTwiceBaseCacheSize--;
                    if (DEBUG) Log.d(TAG, "Retrieving 2x cache " + mHashes
                            + " now have " + mTwiceBaseCacheSize + " entries");
                    return;
                }
            }
        } else if (size == BASE_SIZE) {
            synchronized (ArrayMap.class) {
                if (mBaseCache != null) {
                    final Object[] array = mBaseCache;
                    mArray = array;
                    mBaseCache = (Object[])array[0];
                    mHashes = (int[])array[1];
                    array[0] = array[1] = null;
                    mBaseCacheSize--;
                    if (DEBUG) Log.d(TAG, "Retrieving 1x cache " + mHashes
                            + " now have " + mBaseCacheSize + " entries");
                    return;
                }
            }
        }
	// 如果不符合缓存条件，则直接创建新的mHashes和mArray数组
        mHashes = new int[size];
        mArray = new Object[size<<1];
    }

```
容积为4,8时使用缓存数据，指的是使用容积为4的存储空间，和容积为8的存储空间，目的是不用重新分配内存空间，和具体的k-v数据无关；
```
 private static void freeArrays(final int[] hashes, final Object[] array, final int size) {
	// 容积为8时，将mHashes和mArray数组缓存，至于mArray中的数据则清空
        if (hashes.length == (BASE_SIZE*2)) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCacheSize < CACHE_SIZE) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    for (int i=(size<<1)-1; i>=2; i--) {
                        array[i] = null;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                    if (DEBUG) Log.d(TAG, "Storing 2x cache " + array
                            + " now have " + mTwiceBaseCacheSize + " entries");
                }
            }
        } else if (hashes.length == BASE_SIZE) {
            synchronized (ArrayMap.class) {
                if (mBaseCacheSize < CACHE_SIZE) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    for (int i=(size<<1)-1; i>=2; i--) {
                        array[i] = null;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                    if (DEBUG) Log.d(TAG, "Storing 1x cache " + array
                            + " now have " + mBaseCacheSize + " entries");
                }
            }
        }
    }
```

```
    public V removeAt(int index) {
        final Object old = mArray[(index << 1) + 1];
        final int osize = mSize;
        final int nsize;
        if (osize <= 1) {
            // Now empty.
            if (DEBUG) Log.d(TAG, "remove: shrink from " + mHashes.length + " to 0");
            final int[] ohashes = mHashes;
            final Object[] oarray = mArray;
            mHashes = EmptyArray.INT;
            mArray = EmptyArray.OBJECT;
            freeArrays(ohashes, oarray, osize);
            nsize = 0;
        } else {
            nsize = osize - 1;
            if (mHashes.length > (BASE_SIZE*2) && mSize < mHashes.length/3) {
                // Shrunk enough to reduce size of arrays.  We don't allow it to
                // shrink smaller than (BASE_SIZE*2) to avoid flapping between
                // that and BASE_SIZE.
                final int n = osize > (BASE_SIZE*2) ? (osize + (osize>>1)) : (BASE_SIZE*2);

                if (DEBUG) Log.d(TAG, "remove: shrink from " + mHashes.length + " to " + n);

                final int[] ohashes = mHashes;
                final Object[] oarray = mArray;
                allocArrays(n);

                if (CONCURRENT_MODIFICATION_EXCEPTIONS && osize != mSize) {
                    throw new ConcurrentModificationException();
                }

                if (index > 0) {
                    if (DEBUG) Log.d(TAG, "remove: copy from 0-" + index + " to 0");
                    System.arraycopy(ohashes, 0, mHashes, 0, index);
                    System.arraycopy(oarray, 0, mArray, 0, index << 1);
                }
                if (index < nsize) {
                    if (DEBUG) Log.d(TAG, "remove: copy from " + (index+1) + "-" + nsize
                            + " to " + index);
                    System.arraycopy(ohashes, index + 1, mHashes, index, nsize - index);
                    System.arraycopy(oarray, (index + 1) << 1, mArray, index << 1,
                            (nsize - index) << 1);
                }
            } else {
                if (index < nsize) {
                    if (DEBUG) Log.d(TAG, "remove: move " + (index+1) + "-" + nsize
                            + " to " + index);
                    System.arraycopy(mHashes, index + 1, mHashes, index, nsize - index);
                    System.arraycopy(mArray, (index + 1) << 1, mArray, index << 1,
                            (nsize - index) << 1);
                }
                mArray[nsize << 1] = null;
                mArray[(nsize << 1) + 1] = null;
            }
        }
        if (CONCURRENT_MODIFICATION_EXCEPTIONS && osize != mSize) {
            throw new ConcurrentModificationException();
        }
        mSize = nsize;
        return (V)old;
    }

```