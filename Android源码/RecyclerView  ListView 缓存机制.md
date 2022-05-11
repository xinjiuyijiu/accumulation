ListView和GridView RecycleBin： 
两级的缓存：
- mActiveViews
- mScrapViews（废弃的View，从mActiveView移除的）


初始化加载和滑动加载缓存逻辑


RecyclerView回收机制：
四级的缓存：
- 屏幕内缓存（mAttachedScrap）
- 屏幕外缓存（mCachedViews）
- 自定义缓存池（ViewCacheExtension）
- 缓存池（RecycleViewPool）多个RecyclerView可以复用该缓存池，如果该级缓存取出后，会调用Adapter的onBindViewHolder


RecyclerView局部刷新

ListView的缓存机制更为简单，在低数据量的情况下，更加简单高效
