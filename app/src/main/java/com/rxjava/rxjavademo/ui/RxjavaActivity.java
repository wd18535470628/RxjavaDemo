package com.rxjava.rxjavademo.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rxjava.rxjavademo.R;

import java.util.concurrent.TimeUnit;

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
    @Bind(R.id.rxjava6)
    Button rxjava6;
    @Bind(R.id.rxjava7)
    Button rxjava7;
    @Bind(R.id.rxjava8)
    Button rxjava8;
    @Bind(R.id.rxjava9)
    Button rxjava9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rxjava, R.id.rxjava2, R.id.rxjava3, R.id.rxjava4, R.id.rxjava5, R.id.rxjava6, R.id.rxjava7, R.id.rxjava8, R.id.rxjava9})
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
            case R.id.rxjava6:
                LongTimegetDataRxjava();
                break;
            case R.id.rxjava7:
                FoursRxjava();
                break;
            case R.id.rxjava8:
                FiveRxjava();
                break;
            case R.id.rxjava9:
                SixRxjava();
                break;
        }
    }

    private void SixRxjava() {

        //3秒后发射一个值 重复反射三次
        Observable.timer(3, TimeUnit.SECONDS)
                .repeat(3)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, aLong + " ");
                    }
                });
    }

    private void FiveRxjava() {
        //将发送整数10，11，12，13，14
        Observable.range(10, 3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, integer + " ");
                    }
                });
    }

    private void FoursRxjava() {

        //每隔一秒发送一次
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, aLong + " ");
                    }
                });

    }

    private void LongTimegetDataRxjava() {

        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("我要开始睡觉了");

                SystemClock.sleep(5000);
                subscriber.onNext("我睡了5秒钟");

                SystemClock.sleep(4000);
                subscriber.onNext("我又睡了4秒钟");

                SystemClock.sleep(3000);
                subscriber.onNext("在睡3秒钟就起床");

                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "好了，起床了");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError() e=" + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(RxjavaActivity.this, s, Toast.LENGTH_SHORT).show(); //UI view显示数据
                    }
                });
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
