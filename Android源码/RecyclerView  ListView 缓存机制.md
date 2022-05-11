ListView和GridView RecycleBin： 
两级的缓存：
- mActiveViews
- mScrapViews（废弃的View，从mActiveView移除的）


初始化加载和滑动加载缓存逻辑


RecyclerView回收机制：
四级的缓存：
- 屏幕内缓存（mAttachedScrap）
- 屏幕外缓存（mCachedViews）默认为2
- 自定义缓存池（ViewCacheExtension）默认不实现
- 缓存池（RecycleViewPool）多个RecyclerView可以复用该缓存池，如果该级缓存取出后，会调用Adapter的onBindViewHolder


ListView的缓存机制更为简单，在低数据量的情况下，更加简单高效；
RV缓存的View+ViewHolder，ListView缓存的View需要自己实现ViewHolder的逻辑；
Rv的离屏缓存不需要重新onBindViewHolder，而ListView需要此操作，更加消耗资源；
RV的缓存是对ListView缓存的补充和优化；

ListView：简单的列表，使用简单
RecyclerView： 复杂的列表，需要频繁更新，局部刷新，item加载动画
RecyclerView嵌套滑动的优势

++RecyclerView局部刷新++


