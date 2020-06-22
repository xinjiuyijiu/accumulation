# ArrayList
**实际上使用数组实现进行存储和操作**
```
 transient Object[] elementData;
```
使用数组来实现数据存储，transient 表示elementData不可被序列化；重写了serializable序列化方式readObject和writeObject方法，使得不直接通过elementData序列化，因为elementData表示的capacity而不是size；
```
private static final int DEFAULT_CAPACITY = 10;
```
默认大小为10，每次添加元素时都会检查elementData是否已满，如果已满需要扩容：
```
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

```
每次扩容后，大小为之前的1.5倍，最大的capacity为Integer.MAX_VALUE;

**如何动态扩容**
```
System.arraycopy(a, 0, elementData, index, numNew);
```
通过 jni方法arraycopy，对数组进行拷贝，一般都是先尝试将数据搬移到更大的数组，然后将要添加的某个或者一些数据复制到尾端；

**可添加null元素**
```
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }
```
可以添加null元素，添加null的意义是啥？；

**快速失败机制**
```
  protected transient int modCount = 0;
```
在add，remove，改变elementData数组长度等改变结构的操作时，都会发生modCount++的操作,如果在迭代器中，读操作期间发生了这样的写操作，会抛出ConcurrentModificationException异常，这以一种fail-fast机制。如果是使用的迭代器的类似操作，比如Itr的remove()，ListItr(它是Itr的子类)的add()操作，都很好的解决了读写不同步的问题；

***实现了RandomAccess接口？***
***和Vector比较？***
Vector是个比较早的线程安全的List集合，也是使用数组实现，但是对读写等操作方法添加了synchronized关键字，实现了方法同步，因此在不需要线程同步的情况时，效率偏低，并且其中某些代码逻辑相比ArrayList不够优秀，比如序列化；
***CopyOnWriteArrayList和Collections.synchronizedList(list)线程同步？***
Collections.synchronizedList(list)，SynchronizeList其实就是一个包装类，对List的读写等方法添加了同步代码块锁。
CopyOnWriteArrayList是J.U.C下的一个工具类，逻辑类似ArrayList，只是对写操作添加了复制和锁的操作。

# LinkedList
链表结构，是一个双向链表；
实现了Deque接口，所以它同时也是一个双向队列；
```
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```
LinkedList的每个节点都包含了item数据，prev和next引用（指针），分别指向前后节点，从而一次形成双向链表的结构；

**获取某个位置的元素**
```
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
```
ArrayList在get指定元素时，可以通过数组下标直接获取，而LinkedList只能通过首尾节点，正向或反向遍历，当指定位置在靠近左边时，从首节点开始遍历，在靠近右边是，从尾节点反向遍历；

**断链操作**
```
    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        modCount++;
        return element;
    }
```
LinkedList在插入和删除元素时，只需要增加或删除节点，进行相应的断链操作；
#HashMap
数组+链表组成的散列表数据结构。
```
 transient Node<K,V>[] table;
```
这里的table表示数组，每个元素代表了一个bucket，它存储的是一个key-value节点，而链表就是hash冲突后每个桶的多个node链接形成的链表；
```
 static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
        ......
}
```
每个节点包含了key，value，key的hash值，以及下一个节点的引用；
```
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }
```
在构造方法中，传入初始化的capacity值，它表示table的长度(实际是threshold ，因为需要保证为2的n次幂)，loadFactor装载因子，它表示table的扩容阈，也就是当table数组放入了capacity*loadFactor个元素是，会触发扩容，增大一倍；

**为什么table长度是2的n次幂？**
```
index = (table.length - 1) & hash
```
一般来说，我们通过hash% (table.length - 1)取余操作，可以将任意数字放入数组的0~table.length - 1的位置，而如果table长度为2的n次幂， (table.length - 1) & hash的与操作，则可达到相同效果，且效率更高。同时也应该注意到如果table.length 是奇数，那么 与操作时，必然得到的是偶数，则所有数据都会落入数组的偶数位（当然前提是使用了&）。

**如何保证table长度为2的n次幂（大于等于该值，且最小的2的n次幂）**
```
    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
```
```
    static final int MAXIMUM_CAPACITY = 1 << 30;
```
一个任意的正整数，二进制表示有k位，最高位的数是1，右移一位后异或，会使得高位前两位都为1，接下来一次右移两位（因为最高位连续两位1），高位变为4个1，最后右移16位，变成一个32位的二进制数，这样所有的位数都变为了1，该数必然是2^k-1。
因为最大的capacity为左移30位，所以只需要操作到16位，最大数2^32的范围足够；
如果传入的cap刚好是2的n次幂，通过 cap - 1的操作，能够避免类似传入16的长度，却变为32的情况。

**hash值的计算**
```
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```
获取每个key对象的hashCode值，将该值右移16位在和原值异或操作，会将hashCode值得低16位数据具有高16数据的信息，减少冲突概率，这是“扰动函数”的应用；

**put数据**
```
  final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        // 初始化散列表
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        // 如果需要放入的桶中没有数据，直接放入该数据
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
        // 如果hash值相同，并且key值相同，那么位置冲突（不仅仅是散列冲突），后面会替换该value
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
        // 如果是树节点，将数据插入树种
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
       // 如果是普通的链表，遍历该链表到尾部
                for (int binCount = 0; ; ++binCount) {
                    // 将数据插入到链表尾部
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        // 如果链表长度达到了变为树临界值，需要转换成红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                  // 如果在链表中找到了相同key值得节点（位置冲突），后面会替换该value
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            // e不为空，表示已经存在了相同的key，只需要处理value即可
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
               // 如果非缺失可放入，或者旧数据为null，则替换该数据
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        // 放入了数据后，需要判断capacity是否需要扩容，这里比较的是size而不是table的length!!!
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        // 也就是说，如果没有发生位置冲突，返回的value为空；
        return null;
    }
```
需要注意的是putVal() 返回的是上一次的数据，所以只有发生位置冲突时才会有值返回；另外，在判断是否需要扩容时，指的是当放入的键值对数量大于loadFactor*table.length是触发扩容；因为按照最好情况，每个K-V都放入在数组中，没有发生hash冲突，如果使用table.length和threshold比较，则发生hash冲突的概率非常大；

**putTreeVal插入树节点**
```
        final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
                                       int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            // 找到该节点所在树的根节点
            TreeNode<K,V> root = (parent != null) ? root() : this;
            // 从根节点开始，遍历查找节点的合适位置，然后放入
            for (TreeNode<K,V> p = root;;) {
                int dir, ph; K pk;
                // 根据比较哈希值结果dir方向值，确定遍历方向
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                // 找到了key相同的节点，直接返回该节点
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                // 如果没有实现comparable接口，或者比较值为0，则无法进一步确定遍历方向
                else if ((kc == null &&
                          (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0) {
                    // 仅仅遍历一次该节点的左右子树，查找是否存在key相同的节点，如果有直接返回
                    if (!searched) {
                        TreeNode<K,V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null &&
                             (q = ch.find(h, k, kc)) != null) ||
                            ((ch = p.right) != null &&
                             (q = ch.find(h, k, kc)) != null))
                            return q;
                    }
                  // 还是无法确定dir方向值，则使用决胜局方法，获取k，pk必定不相等的两个hash值，
                  // 使得dir可比较.
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K,V> xp = p;
                // 是否遍历到左或右子树为空的节点，如果没有则继续遍历左右子树，知道遍历到为止
                // 因为红黑树是二叉平衡树，所以插入的节点一定是某个叶节点
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K,V> xpn = xp.next;
                    // 创建新的树节点
                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
                    // 根据dir值决定插入到叶子结点的
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    // 处理节点的next prev引用
                    xp.next = x;
                    x.parent = x.prev = xp;
                    if (xpn != null)
                        ((TreeNode<K,V>)xpn).prev = x;
                   // 插入操作后，需要对红黑树进行平衡操作
                    moveRootToFront(tab, balanceInsertion(root, x));
                    return null;
                }
            }
        }
```

**动态扩容**

```
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            // 如果达到了最大的capacity，将threshold设为int最大值，使得无法在扩容
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // 将capacity扩容1倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        // 创建新的数组
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            // 遍历旧的数组，将它上面的每个node迁移到新的tab上
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    // 如果没有链，将节点直接放入指定位置
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                   // 如果该bucket中放入的红黑树，可能需要树分裂
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                    // 如果该bucket中放入的单链表，处理它
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        // 遍历该链表
                        do {
                            next = e.next;
                            // e.hash & oldCap为0，表示在capacity扩容1倍后，节点仍在所在的bucket上，
                            // 将该链表上的所有位置不变的节点连接成新的链
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            //  e.hash & oldCap为0，表示在capacity扩容1倍后，节点在j+oldCap上，
                            // 将该链表上的所有位置变化的节点连接成新的链
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        // 位置不变的链放入原位置的bucket
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        // 位置变化的链放入j + oldCap的bucket
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

```

**链表和树的转换**
```
 static final int TREEIFY_THRESHOLD = 8;
```
散列表中，链表和树的转换临界值为8，当链表的长度达到8时，会转换成红黑树的结构。
```
static final int UNTREEIFY_THRESHOLD = 6;
```
当树的节点数不够6时，会退化成链表
```
static final int MIN_TREEIFY_CAPACITY = 64;
```
当某个bucket节点数达到树化**TREEIFY_THRESHOLD** 值时，如果数组长度不够**MIN_TREEIFY_CAPACITY **，则会调用resize扩容；

***链表变成树***
1. putVal
2. compute
3. merge
这些可能增加节点的操作，会导致树化产生
```
    final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        // 如果数组长度小于MIN_TREEIFY_CAPACITY，直接扩容而不是树化
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K,V> hd = null, tl = null;
            do {
                // 遍历链表节点，将普通节点转换成树节点，将这些节点连接成双向链表
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            // 开始树化操作
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }
```

***树退化成链表***
1. *resize时树分裂成小树*
2. *remove操作时，移除节点导致树节点太少*
在上面的resize扩容操作中，存放树节点的bucket在重新计算位置后，红黑树可能会分裂成链表或者子树（子树可能发生树的退化操作）。
```
        final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
            TreeNode<K,V> b = this;
            // Relink into lo and hi lists, preserving order
            TreeNode<K,V> loHead = null, loTail = null;
            TreeNode<K,V> hiHead = null, hiTail = null;
            int lc = 0, hc = 0;
          // 遍历树节点
            for (TreeNode<K,V> e = b, next; e != null; e = next) {
                next = (TreeNode<K,V>)e.next;
                e.next = null;
                // 将原位置的节点形成链
                if ((e.hash & bit) == 0) {
                    if ((e.prev = loTail) == null)
                        loHead = e;
                    else
                        loTail.next = e;
                    loTail = e;
                    ++lc;
                }
                // 将新位置的节点形成链
                else {
                    if ((e.prev = hiTail) == null)
                        hiHead = e;
                    else
                        hiTail.next = e;
                    hiTail = e;
                    ++hc;
                }
            }
          // 原位置的链
            if (loHead != null) {
                // 链长度不够，需要树退化
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);
                else {
                // 树化原位置的链
                    tab[index] = loHead;
                // 如果没有产生非原位值得链，也就是说该bucket的节点位置都没变，那么树继续保持，
                // 不需要重新树化.
                    if (hiHead != null) // (else is already treeified)
                        loHead.treeify(tab);
                }
            }
         // 新位置的链
            if (hiHead != null) {
                // 新位置的链，长度不够，也需要退化成普通链表
                if (hc <= UNTREEIFY_THRESHOLD)
                    tab[index + bit] = hiHead.untreeify(map);
                else {
                    tab[index + bit] = hiHead;
                    // 同样的，如果原位置的链为空，也就是说所有的节点都搬移到了新的位置，那么树继续保持，
                    // 只需要移动根节点到新的位置，不需要重新树化
                    if (loHead != null)
                        hiHead.treeify(tab);
                }
            }
        }
```
**remove节点**
前面的put数据到HashMap中，可能因为增加节点，导致复杂的变化：
1. 会发生resize扩容操作，从而导致每个bucket节点的移动，进一步导致每个bucket发生treeify和untreeify的操作；
2. 没有发生resize操作，直接放入bucket的链表中，可能会发生treeify的操作；
而remove操作，相对来说更简单，最多会触发untreeify的操作；
```
  final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
        Node<K,V>[] tab; Node<K,V> p; int n, index;
        // 找到节点所在的bucket位置
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (p = tab[index = (n - 1) & hash]) != null) {
            Node<K,V> node = null, e; K k; V v;
            // 如果被移除节点是bucket中的节点（链表首节点或者树的根节点）
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            // 如果该链表不仅仅只有一个节点，需要遍历查找它
            else if ((e = p.next) != null) {
                // 首节点是树节点，在树中查找它
                if (p instanceof TreeNode)
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
                // 如果是普通链表，遍历该链表
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key ||
                             (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            // 查找到key相同的node后，matchValue 表示是否需要value值相等，才能够移除该node
            // 如果为false，表示只要key相同则直接移除；如果为true，则要满足后面的==或equals条件
            // 这个逻辑写的真好！！！
            if (node != null && (!matchValue || (v = node.value) == value ||
                                 (value != null && value.equals(v)))) {
                // 移除树中的节点
                if (node instanceof TreeNode)
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                // 如果node==p表示该节点是bucket节点（首节点）
                else if (node == p)
                    tab[index] = node.next;
                // 否则表示该节点是链表中的某个节点
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }
```

**get数据**
```
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            // 首节点为查找的节点
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                // 树节点
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                // 普通链表，直接遍历查找
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
```
在查找某个key-value时，如果是树节点，则需要在红黑树中查找
```
        final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
            TreeNode<K,V> p = this;
            // 遍历红黑树
            do {
                int ph, dir; K pk;
                TreeNode<K,V> pl = p.left, pr = p.right, q;
                // 当前节点hash值大于目标hash值，则继续遍历左子树
                if ((ph = p.hash) > h)
                    p = pl;
               // 当前节点hash值小于目标hash值，则继续遍历右子树
                else if (ph < h)
                    p = pr;
              // 当前节点hash值等于目标hash值，且key equal相等则查找成功
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
              // 当前节点左子树null，则继续遍历右子树
                else if (pl == null)
                    p = pr;
              // 当前节点右子树null，则继续遍历左子树
                else if (pr == null)
                    p = pl;
              // 如果key对象实现了comparable接口，且comparable返回值不为0，表示key可比较，
              //通过comparable返回值，决定继续遍历左子树或者右子树
                else if ((kc != null ||
                          (kc = comparableClassFor(k)) != null) &&
                         (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
               // 递归遍历右子树
                else if ((q = pr.find(h, k, kc)) != null)
                    return q;
              // 如果右子树没找到，则继续遍历左子树
                else
                    p = pl;
            } while (p != null);
            return null;
        }
```
在上述查找树节点的过程中，遇到hash冲突，但key不相等的node，尝试使用comparable接口比较，如果没有实现该接口，则无法直接判断继续遍历左子树或者右子树，只能都遍历一次，使得效率大大降低；

**put的数据key为null，value为null？**
```
   static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

```
key为空时，其hash值为0，则必定放入第0个bucket，并且整个HashMap中仅有一个key为null的节点；

# LinkedHashMap
在HashMap的基础上，通过双向链表链接所有的key-value节点，从而能够按照一定顺序遍历节点；
```
    static class LinkedHashMapEntry<K,V> extends HashMap.Node<K,V> {
        LinkedHashMapEntry<K,V> before, after;
        LinkedHashMapEntry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }
    /**
     * The head (eldest) of the doubly linked list.
     */
    transient LinkedHashMapEntry<K,V> head;

    /**
     * The tail (youngest) of the doubly linked list.
     */
    transient LinkedHashMapEntry<K,V> tail;

```
LinkedHashMap的节点在HashMap的节点基础上，添加了before，after引用，以及head和tail成员变量用来形成双向链表；
```
    void afterNodeAccess(Node<K,V> p) { }
    void afterNodeInsertion(boolean evict) { }
    void afterNodeRemoval(Node<K,V> p) { }
```
HashMap中定义了三个空实现的方法，分别表示访问检点后，插入节点后，移除节点后的操作，在LinkedHashMap中，会具体实现这些方法，用于控制双向链表的顺序；

```
    // 节点移除后，双向链表断链重连
    void afterNodeRemoval(Node<K,V> e) { // unlink
        LinkedHashMapEntry<K,V> p =
            (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
        p.before = p.after = null;
        if (b == null)
            head = a;
        else
            b.after = a;
        if (a == null)
            tail = b;
        else
            a.before = b;
    }
 
    void afterNodeInsertion(boolean evict) { // possibly remove eldest
        LinkedHashMapEntry<K,V> first;
         // 节点插入后，根据需要移除老生代的节点，一边在缓存和有大小限制时使用
        if (evict && (first = head) != null && removeEldestEntry(first)) {
            K key = first.key;
            removeNode(hash(key), key, null, false, true);
        }
    }
    // 某个节点被访问后，如果是按访问数据设置的双向链表，那么该节点会被移动到尾端
    void afterNodeAccess(Node<K,V> e) { // move node to last
        LinkedHashMapEntry<K,V> last;
        if (accessOrder && (last = tail) != e) {
            LinkedHashMapEntry<K,V> p =
                (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
            p.after = null;
            if (b == null)
                head = a;
            else
                b.after = a;
            if (a != null)
                a.before = b;
            else
                last = b;
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }

```
```
   // 在新增节点时，会将节点插入到双向链表尾端
    private void linkNodeLast(LinkedHashMapEntry<K,V> p) {
        LinkedHashMapEntry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }
```
LinkedHashMap正是通过重写HashMap的一些方法，使得在节点操作后，控制双向链表中节点位置，从而实现有序遍历的功能；

```
  final boolean accessOrder;
```
accessOrder控制双向链表的顺序，true表示根据访问顺序控制双向链表，被访问的节点会被移动到双向链表尾部；false表示根据插入顺序控制双向链表，数据插入后，方向不变.正是因为这样的特性，可以使用LinkedHashMap实现类似LRUCache的功能.

# TreeMap

通过红黑树实现存储key-value数据，每个树节点通过key进行比较排序；是一个二叉查找树，也是一个黑平衡二叉树；通过中序遍历能将所有节点有序遍历出；

```
    static final class TreeMapEntry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        TreeMapEntry<K,V> left;
        TreeMapEntry<K,V> right;
        TreeMapEntry<K,V> parent;
        boolean color = BLACK;
        ....
```
可以看出TreeMap的每个节点包含了左右子节点，父节点的引用，key，value，以及红黑树颜色.

```
   public V put(K key, V value) {
        TreeMapEntry<K,V> t = root;
        // 如果根节点为空，创建根节点
        if (t == null) {
            compare(key, key); // type (and possibly null) check

            root = new TreeMapEntry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        TreeMapEntry<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        // 如果存在比较器，使用比较器比较key
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                // 继续遍历左子树
                if (cmp < 0)
                    t = t.left;
                // 继续遍历右子树
                else if (cmp > 0)
                    t = t.right;
                else
                // 如果key比较结果相等，替换成新的value，并返回
                    return t.setValue(value);
            // 一直遍历到叶子结点
            } while (t != null);
        }
        else {
            // 常规的key比较，key不能为空
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
                // key必须实现Comparable接口
                Comparable<? super K> k = (Comparable<? super K>) key;
             // 通过key的comparable接口比较值判断大小，遍历并找到叶子结点位置
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        // 创建树节点，传入parent节点
        TreeMapEntry<K,V> e = new TreeMapEntry<>(key, value, parent);
        // 设置节点在父节点的哪个分支
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        // 红黑树插入后的自平衡操作
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }
```
从上面的节点插入操作可知，当TreeMap没有传入Comparable比较器时，需要key实现自己的比较器，使得节点可以比较，从而形成分叉结构；
```
    // 这里的CLR指的是Introduction_to_Algorithms这本书的几名作者的缩写，这里的红黑树自平衡逻辑，
    // 基本上来自该书籍
    /** From CLR */
    private void fixAfterInsertion(TreeMapEntry<K,V> x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                TreeMapEntry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                TreeMapEntry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
```
***插入新的节点后，如何保持自平衡？？？***
```
    private void deleteEntry(TreeMapEntry<K,V> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
       // 如果p节点的左右节点都存在，那么需要将p节点的后继节点代替p节点的位置（因为一个节点最多只能
       // 有两个子节点）;除此之外，p节点只有1个或0个子节点，那么直接将子节点代替p节点；
        if (p.left != null && p.right != null) {
            // 找到p的后继节点
            TreeMapEntry<K,V> s = successor(p);
            // 将后继节点的key-value赋值给p节点，相当于p节点被删除了，但树结构不变
            p.key = s.key;
            p.value = s.value;
            // 将后继节点设为p节点，那么将要被删除的节点变成了p节点的后继节点；
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        TreeMapEntry<K,V> replacement = (p.left != null ? p.left : p.right);
        // replacement不为空，表示p节点被删除后，需要处理它的子树和p节点parent的关系；
        if (replacement != null) {
            // Link replacement to parent
            // 将p节点的父节点指向p节点子节点
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            // 如果replacement节点在p节点parent左子树
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
            // 如果replacement节点在p节点parent右子树
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            // 将被删除节点所有引用清空
            p.left = p.right = p.parent = null;

            // Fix replacement
            // 被删除节点是黑节点，那么子树的黑平衡被破坏，需要自平衡操作
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
           // p节点是叶子节点（非nil）
           // 如果p是黑节点，那么子树的黑平衡被破坏，需要自平衡操作
            if (p.color == BLACK)
                fixAfterDeletion(p);
            
            if (p.parent != null) {
               //  p节点是左节点，parent的左节点引用设为null
                if (p == p.parent.left)
                    p.parent.left = null;
               // p节点是右节点，parent的右节点引用设为null
                else if (p == p.parent.right)
                    p.parent.right = null;
                // 最后将p节点的parent引用设为null
                p.parent = null;
            }
        }
    }
```
删除一个节点，如果该节点有左右子节点，那么需要将后继节点替换该节点，然后变成了删除后继节点的问题；如果该节点右0个或1个子节点，直接删除该节点即可；在删除节点后，还应该考虑黑节点平衡问题，如果被删除节点是红色节点，则不需要自平衡操作；
```
    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K,V> TreeMapEntry<K,V> successor(TreeMapEntry<K,V> t) {
        if (t == null)
            return null;
        // 如果t节点的右节点，不为空，那么遍历找到该右子树的最左节点，它就是t节点的后继节点
        else if (t.right != null) {
            TreeMapEntry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
        // 如果t节点的右子树不存在，如果t节点是左子树节点，那么它的parent节点就是后继节点；如果t节点是
        // 右子树节点，那么它的parent的parent节点就是后继节点
            TreeMapEntry<K,V> p = t.parent;
            TreeMapEntry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Returns the predecessor of the specified Entry, or null if no such.
     */
    static <K,V> TreeMapEntry<K,V> predecessor(TreeMapEntry<K,V> t) {
        if (t == null)
            return null;
        // 左子树的最右节点，就是t节点的前任节点
        else if (t.left != null) {
            TreeMapEntry<K,V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
         // 如果t节点左子树为空，如果它是右子树节点，它的前任节点是它的parent节点；如果他是左子树节
        // 点，它的前任节点是它的parent的parent节点；
            TreeMapEntry<K,V> p = t.parent;
            TreeMapEntry<K,V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
```
successor和predecessor分别获取某个节点的后继节点和前任节点，红黑树使用中序遍历；

```
    /** From CLR */
    private void fixAfterDeletion(TreeMapEntry<K,V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                TreeMapEntry<K,V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == BLACK &&
                    colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                TreeMapEntry<K,V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                    colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

```
***删除节点后，如何自平衡，后面再研究？？？***
```
    final TreeMapEntry<K,V> getEntry(Object key) {
        // Offload comparator-based version for sake of performance
       // 使用TreeMap传入的比较器进行比较，从而获取对应节点，该逻辑和后面的key的比较类似
        if (comparator != null)
            return getEntryUsingComparator(key);
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
        TreeMapEntry<K,V> p = root;
        // 从根节点开始遍历比较
        while (p != null) {
            // 通过key的Comparable接口比较
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
              // 找到相等的key返回
                return p;
        }
        return null;
    }
```
TreeMap是一个已排序的数据结构，获取最大值，最小值，大于某个值得数据，小于某个值得数据等等这些和排序相关的逻辑;对于TreeMap查找，插入，删除数据的时间复杂度都是O（logn）.