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

