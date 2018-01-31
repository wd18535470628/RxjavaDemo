package com.rxjava.rxjavademo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rxjava.rxjavademo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxjavaActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    @Bind(R.id.rxjava)
    Button rxjava;
    @Bind(R.id.rxjava2)
    Button rxjava2;
    @Bind(R.id.rxjava3)
    Button rxjava3;
    @Bind(R.id.rxjava4)
    Button rxjava4;
    @Bind(R.id.rxjava5)
    Button rxjava5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rxjava, R.id.rxjava2, R.id.rxjava3, R.id.rxjava4, R.id.rxjava5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rxjava:
                FirstRxjava();
                break;
            case R.id.rxjava2:
                SecondRxjava();
                break;
            case R.id.rxjava3:
                ThridRxjava();
                break;
            case R.id.rxjava4:
                SchedulerRxjava();
                break;
            case R.id.rxjava5:
                SubscriberRxjava();
                break;
        }
    }

    private void SubscriberRxjava() {

        //被观察者 相当于Button 第三种创建方式
        String[] words = new String[]{"Hello", "我是由Subscriber创建观察者", "Subscriber"};
        Observable observable = Observable.from(words);
        //观察者  相当于OnClickListener
        Subscriber<String> sub = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };
        //订阅 subscribe相当于setOnClickListener
        observable.subscribe(sub);
    }

    private void SchedulerRxjava() {

        //线程调度器Scheduler
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.e(TAG, "number:" + number);
                    }
                });
    }

    private void ThridRxjava() {
        //被观察者 相当于Button 第三种创建方式
        String[] words = new String[]{"Hello", "我是被观察者三", "Thrid"};
        Observable observable = Observable.from(words);
        //观察者  相当于OnClickListener
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };

        //订阅 subscribe相当于setOnClickListener
        observable.subscribe(observer);
        //sub.unsubscribe();
    }

    private void SecondRxjava() {
        //被观察者 相当于Button 第二种创建方式
        Observable observable = Observable.just("Hello", "我是被观察者二", "Second");
        //观察者  相当于OnClickListener
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };

        //订阅 subscribe相当于setOnClickListener
        observable.subscribe(observer);
    }

    private void FirstRxjava() {
        //被观察者 相当于Button  第一种创建方式
        Observable observable = Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("我是被观察者一");
                subscriber.onNext("First");
                subscriber.onCompleted();
            }
        });

        //观察者  相当于OnClickListener
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };

        //订阅 subscribe相当于setOnClickListener
        observable.subscribe(observer);
    }
}
