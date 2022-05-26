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
   startNestedScroll，通知父控件开始
   - ACTION MOVE
   - ACTION UP