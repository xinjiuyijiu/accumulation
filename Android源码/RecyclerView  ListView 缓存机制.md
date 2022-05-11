RecycleBin： mScapView  mActiveView
初始化加载和滑动加载缓存逻辑
RecyclerView回收机制：
四级的缓存：
- 屏幕内缓存（mAttachedScrap）
- 屏幕外缓存（mCachedViews）
- 自定义缓存池（ViewCacheExtension）
- 缓存池（RecycleViewPool）多个RecyclerView可以复用该缓存池，如果上面的三个缓存中都没有去除


RecyclerView局部刷新
