RV的三步曲：
- onMeasure
AutoMeasure ：dispatchLayoutStep1，dispatchLayoutStep2，setMeasuredDimensionFromChildren
- onLayout
dispatchLayoutStep1（layoutinfo），dispatchLayoutStep2（layoutchildren），dispatchLayoutStep3（animation）
- draw
drawItemDecor，drawItem，drawItemDecorOver，drawClipChildPadding

RV的滑动：
onTouchEvent：涉及到嵌套滑动的逻辑
   - ACTION DOWN
   startNestedScroll，通知父控件开始滚动；初始化一些滚动初始值；
   - ACTION MOVE
   dispatchNestedPreScroll，父控件是否预先消耗部分值，处理滚动；剩下的
   - ACTION UP