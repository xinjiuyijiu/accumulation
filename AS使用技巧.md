# AS使用技巧 #

----------

## 1.项目分组
在项目欢迎页面，可对左侧的项目列表进行group分组，方便项目管理。

## 2.自定义类模板
创建自定义的java文件，再创件文件时，自动生成该代码，提高编写效率。

## 3.TODO表示
暂时无法实现的方法添加该标识，标识将会完成。

## 4.收藏列表和书签
可以将文件，方法添加到收藏列表中，也可以给任意行添加书签，在AS左下角的favorite中查看，这样可以提高代码阅读效率。

## 5.窗口模式
可以在view选项中选择enter xx，切换三种窗口模式：演讲模式，免打扰模式和全屏模式，方便开发。

## 6.快速文件定位
ctrl+Shift+N 查找所有的file

ctrl+Shift+Alt+N 关键字查找，除了文件，还能找到方法，变量

***点击瞄准器，能快速打开当前Tab的文件在左侧文件目录中的位置***


## 7.Live Templates，自定义代码片段
按自己的习惯编写类似***sout(System.out.println();)***的代码片段，提高编写效率。

## extract 快速提取method，style，layout
在布局文件中使用extract，将光标所在的控件抽取可用style，或者将控件出去成布局文件include进来。
在类文件中，选中某段代码使用extract将它抽离成一个方法。

## 8.lint使用
代码检查，资源检查

## 9.rearrange code 代码重新排序
使用code下的rearrange code选项，将类文件和布局文件中的代码按和是的循序排列：

1.在类文件中，可以将排序混乱的成员变量，方法按合理的方式重新排列；

2.在布局文件中，可以将不合理的属性顺序重新排列，比如：
>     <LinearLayout
>     android:layout_width="match_parent"
>     android:orientation="vertical"
>     android:layout_height="match_parent">
  经过从新排列会变成：

>     <LinearLayout
>     android:layout_width="match_parent"
>     android:layout_height="match_parent"
>     android:orientation="vertical">
