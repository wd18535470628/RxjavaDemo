package com.rxjava.rxjavademo.mvp.other;

import android.os.Bundle;

import com.rxjava.rxjavademo.ui.BaseActivity;


/**
 * Created by WuXiaolong on 2016/3/30.
 * github:https://github.com/WuXiaolong/
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

    public void showLoading() {
        showProgressDialog();
    }

    public void hideLoading() {
        dismissProgressDialog();
    }
}
