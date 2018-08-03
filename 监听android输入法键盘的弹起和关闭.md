# android监听输入法键盘的弹起和关闭 #

----------

        View rootView = activity.findViewById(android.R.id.content);
        mDisposable = RxView.globalLayouts(rootView)
                .flatMap(new Function<Object, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        Rect rect = new Rect();
                        //获取root所在窗体的可视区域
                        rootView.getWindowVisibleDisplayFrame(rect);
                        //获取root所在在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = rootView.getRootView().getHeight() - rect.bottom;
                        int dispayHeight = (int) DeviceUtils.getScreenHeight(mContext);
                        // 比较1/5的高度，之前用的1/3，因为如果用手写键盘高度不够1/3
                        return Observable.just(rootInvisibleHeight > (dispayHeight * (1f / 5)));
                    }
                })
                // 监听键盘弹起隐藏状态时，会多次调用globalLayouts方法，为了避免多个数据导致状态判断出错，只取200ms内最后一次数据
                .debounce(mContext.getResources().getInteger(android.R.integer.config_mediumAnimTime), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        //若不可视区域高度大于1/5屏幕高度，则键盘显示
                        isInputWindowShow = aBoolean;
                        if (aBoolean) {
                            Timber.i(TAG + "键盘显示");
                        } else {
                            //键盘隐藏,清除焦点
                            Timber.i(TAG + "键盘隐藏");
                        }
                    }
                });

获取根布局（它在activity所在的window中）的可见高度，它的不可见位置就是被输入法所在的window挡住的地方。通过判断这个不可见的高度的大小，可以在某些场景下进行判断。

使用了1/5的屏幕高度进行判断，其实这个值只要合理就行了，刚开始使用了1/3的高度，在很多手机下都能够判断，但是有些手机，在使用手写输入法，键盘高度没有达到1/3。

在输入法键盘弹起时，globalLayouts返回了一次或者几次不可见高度为0的结果，所以这里使用了debounce，只选择了200ms内的最后一次结果。

在监听根布局ViewTreeObserver视图树，window被键盘顶起（所以需要将activity的inputmethod设置成adjustpan或者adjustresize），导致视图位置变化。







