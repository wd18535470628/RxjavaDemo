# RxjavaDemo
Rxjava最基本的用法，创建被观察者的几种方式、创建观察者以及如何线程的调度

# 以下为Rxjava的一些基本概念，常用的知识点

1、Observable：发射源，英文释义“可观察的”，在观察者模式中称为“被观察者”或“可观察对象”；

2、Observer：接收源，英文释义“观察者”，没错！就是观察者模式中的“观察者”，可接收Observable、Subject发射的数据；

3、Subject：Subject是一个比较特殊的对象，既可充当发射源，也可充当接收源，为避免初学者被混淆，本章将不对Subject做过多的解释和使用，重点放在Observable和Observer上，先把最基本方法的使用学会，后面再学其他的都不是什么问题；

4、Subscriber：“订阅者”，也是接收源，那它跟Observer有什么区别呢？Subscriber实现了Observer接口，比Observer多了一个最重要的方法unsubscribe( )，用来取消订阅，当你不再想接收数据了，可以调用unsubscribe( )方法停止接收，Observer 在 subscribe() 过程中,最终也会被转换成 Subscriber 对象，一般情况下，建议使用Subscriber作为接收源；

5、Subscription ：Observable调用subscribe( )方法返回的对象，同样有unsubscribe( )方法，可以用来取消订阅事件；

6、Action0：RxJava中的一个接口，它只有一个无参call（）方法，且无返回值，同样还有Action1，Action2…Action9等，Action1封装了含有 1 个参的call（）方法，即call（T t），Action2封装了含有 2 个参数的call方法，即call（T1 t1，T2 t2），以此类推；

7、Func0：与Action0非常相似，也有call（）方法，但是它是有返回值的，同样也有Func0、Func1…Func9;

RxJava最核心的两个东西是Observables（被观察者，事件源）和Subscribers（观察者）。Observables发出一系列事件，Subscribers处理这些事件。这里的事件可以是任何你感兴趣的东西（触摸事件，web接口调用返回的数据…）

一个Observable可以发出零个或者多个事件，知道结束或者出错。每发出一个事件，就会调用它的Subscriber的onNext方法，最后调用Subscriber.onNext()或者Subscriber.onError()结束。

Rxjava的看起来很想设计模式中的观察者模式，但是有一点明显不同，那就是如果一个Observerble没有任何的的Subscriber，那么这个Observable是不会发出任何事件的。

# 基本用法 

一、Observable的创建
**1、使用create( ),最基本的创建方式：**
**//被观察者 相当于Button  第一种创建方式**
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
		
2、使用just创建，将为你创建一个Observable并自动为你调用onNext( )发射数据，使用just创建也可以发射一个集合List

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
		
3、使用from创建，同样可以发射集合，但是它与just的区别是：from发射的是每一个Item。换句话说是遍历的发射，而just是一次性的发射

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
		
		
发射集合：
List<String> list = new ArrayList<>();
list.add("from1");
list.add("from2");
list.add("from3");
fromObservable = Observable.from(list);  //遍历list 每次发送一个
/** 注意，just()方法也可以传list，但是发送的是整个list对象，而from（）发送的是list的一个item** /

4、使用interval( ),创建一个按固定时间间隔发射整数序列的Observable，可用作定时器：

intervalObservable = Observable.interval(1, TimeUnit.SECONDS);//每隔一秒发送一次
5、使用range( ),创建一个发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，如果为0则不发送，负数则抛异常：

rangeObservable = Observable.range(10, 5);//将发送整数10，11，12，13，14
6、使用timer( ),创建一个Observable，它在一个给定的延迟后发射一个特殊的值，等同于Android中Handler的postDelay( )方法：

timeObservable = Observable.timer(3, TimeUnit.SECONDS);  //3秒后发射一个值
7、使用repeat( ),创建一个重复发射特定数据的Observable:

repeatObservable = Observable.just("repeatObservable").repeat(3);//重复发射3次

以上是常用的一些创建方式，当然还有其他的方式，我只是列举几种。

# Subscriber的创建


# 这是一些基本的用法
![image](https://github.com/wd18535470628/RxjavaDemo/blob/master/1.jpg)


