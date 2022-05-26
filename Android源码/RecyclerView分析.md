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
      - dispatchNestedPreScroll，父控件是否预先消耗部分值，处理滚动；
          剩下的滑动值scrollByInternal处理rv的滑动：
	     - scrollStep()处理rv的实际滑动：
	            layoutManager的scrollHorizontallyBy以及scrollVerticallyBy方法调用滚动，实际是调用每个child的offsetLeftAndRight，改变位置，形成滑动的效果；
	     - dispatchNestedScroll：父控件处理剩余的滚动值
	     - dispatchOnScrolled：滚动listener时间分发
      - GapWorker预取ViewHolder，走的是recycler.tryGetViewHolderForPositionByDeadline获取或创建实际的view
   - ACTION UP
   fling滑动的相关处理；stopNestedScroll嵌套滑动结束；
   - ACTION_POINTER_DOWN，ACTION_POINTER_UP
   多指滑动，指的是一根指头还没有up，另一根指头down了，不是指的是多指头操作；