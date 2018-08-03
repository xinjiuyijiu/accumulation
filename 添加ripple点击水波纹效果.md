# android原生水波纹点击效果 #

--------

使用android原生水波纹效果，需要在在android5.0+上

在drawable-v21文件夹中创建xml资源文件：

    <ripple xmlns:android="http://schemas.android.com/apk/res/android"
            android:color="@color/ripple_color">
            <item android:drawable="@color/white"></item>
    </ripple>

这样就创建了白色背景的指定水波颜色的水波纹效果，然后再将它指定到view的backgroud就ok了。

如果要给水波纹所在控件添加背景：

		<ripple xmlns:android="http://schemas.android.com/apk/res/android"
        android:color="@color/ripple_color">
  			 <item>
       			  <shape>
          		  		<solid android:color="@color/white"></solid>
            	  		<corners android:radius="@dimen/padding_2dp"></corners>
        	 	  </shape>
    		</item>
		</ripple>+

直接在item中添加shape就行了。

另外为了兼容<5.0的android，需要在drawble中添加同名资源文件：

		<selector xmlns:android="http://schemas.android.com/apk/res/android">
    		 <item android:drawable="@color/ripple_color" android:state_pressed="true"></item>
   			 <item android:drawable="@color/white" android:state_pressed="false"></item>
		</selector>

以及：

		<selector xmlns:android="http://schemas.android.com/apk/res/android">
    		<item android:state_pressed="true">
        		<shape>
            		<corners android:radius="@dimen/padding_2dp"></corners>
            		<solid android:color="@color/ripple_color"></solid>
        		</shape>
    		</item>
    		<item android:state_pressed="false">
        		<shape>
            		<corners android:radius="@dimen/padding_2dp"></corners>
            		<solid android:color="@color/white"></solid>
        		</shape>
    		</item>
		</selector>

这样通过selector设置点击效果
