
- FrameAnimation：一系列的图片顺序播放产生的动画效果，图片过多是可能造成OOM。
- TweenAnimation：对场景的view进行图像变化（平移，缩放，透明度，旋转）。
- AttributeAnimation：动态改变对象属性产生的动画属性。

TweenAnimation并未改变view的属性，只是更改了绘制位置。

对于属性动画：ValueAnimator（子类ObjectAnimator），TypeEvaluator（决定了运动轨迹），TimeInterpolator（决定了行进速率），ObjectAnimator通过不断的控制属性值的变化，并不断的自动赋值给属性，每次赋值后都会调用invalidate/postInvalidate方法刷新视图，通过反射获取到属性的set，get方法，从而通过动画控制view的属性值；

**ObjectAnimator的使用：**

两个前提条件：
1. 该属性必须有setXxxx的方法来修改该属性，如果没有传递动画属性的初始值，那么该属性必须有getXxxx的方法，获取初始值。
2. 该属性值的改变，必须要产生某种特定的影响。

如果无法满足上面的条件，可以通过

1. 手动添加get/set方法。
2. 用一个新的类包装它，从而添加get/set方法。
3. 使用ValueAnimator监听动画的改变，自己实现属性的变化过程。

StateListAnimator 视图状态动画
RevealAnimator 揭露动画：实现转场动画，或者某个view的扩展显示和隐藏
MotionLayout 关键帧动画：实现过渡动画
RippleEffect 水波纹动画

lottie动画