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

1、使用create( ),最基本的创建方式

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

上面定义的Observable对象仅仅发出一个Hello World字符串，然后就结束了。接着我们创建一个Subscriber来处理Observable对象发出的字符串：

Subscriber<String> mySubscriber = new Subscriber<String>() {  
    @Override  
    public void onNext(String s) {
		 System.out.println(s); //打印出"Hello, world!"
	}  
  
    @Override  
    public void onCompleted() { }  
  
    @Override  
    public void onError(Throwable e) { }  
};

除了 Observer 接口之外，RxJava 还内置了一个实现了 Observer 的抽象类：Subscriber。 Subscriber 对 Observer 接口进行了一些扩展，但他们的基本使用方式是完全一样的：

Observer<String> myObserver = new Observer<String>() {  
    @Override  
    public void onNext(String s) {
		 System.out.println(s); //打印出"Hello, world!"
	}  
  
    @Override  
    public void onCompleted() { }  
  
    @Override  
    public void onError(Throwable e) { }  
};

不仅基本使用方式一样，实质上，在 RxJava 的 subscribe 过程中，Observer 也总是会先被转换成一个 Subscriber 再使用。所以如果你只想使用基本功能，选择 Observer 和 Subscriber 是完全一样的。它们的区别对于使用者来说主要有两点：

onStart(): 这是 Subscriber 增加的方法。它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。需要注意的是，如果对准备工作的线程有要求（例如弹出一个显示进度的对话框，这必须在主线程执行），onStart() 就不适用了，因为它总是在 subscribe 所发生的线程被调用，而不能指定线程。要在指定的线程来做准备工作，可以使用 doOnSubscribe() 方法，具体可以在后面的文中看到。

unsubscribe(): 这是 Subscriber 所实现的另一个接口 Subscription 的方法，用于取消订阅。在这个方法被调用后，Subscriber 将不再接收事件。一般在这个方法调用前，可以使用 isUnsubscribed() 先判断一下状态。 unsubscribe() 这个方法很重要，因为在 subscribe() 之后， Observable 会持有 Subscriber 的引用，这个引用如果不能及时被释放，将有内存泄露的风险。所以最好保持一个原则：要在不再使用的时候尽快在合适的地方（例如 onPause() onStop() 等方法中）调用 unsubscribe() 来解除引用关系，以避免内存泄露的发生。

Observable与Subscriber的关联

这里subscriber仅仅就是打印observable发出的字符串。通过subscribe函数就可以将我们定义的myObservable对象和mySubscriber对象关联起来，这样就完成了subscriber对observable的订阅。

myObservable.subscribe(myObserver);

// 或者：
myObservable.subscribe(mySubscriber);

一旦mySubscriber订阅了myObservable，myObservable就是调用mySubscriber对象的onNext和onComplete方法，mySubscriber 就会打印出Hello World！

订阅（Subscriptions）

当调用Observable.subscribe()，会返回一个Subscription对象。这个对象代表了被观察者和订阅者之间的联系。

Subscription subscription = Observable.just("Hello, World!")

    .subscribe(s -> System.out.println(s));
	
你可以在后面使用这个Subscription对象来操作被观察者和订阅者之间的联系.

subscription.unsubscribe();//接触订阅关系

System.out.println("Unsubscribed=" + subscription.isUnsubscribed());

// Outputs "Unsubscribed=true"

RxJava的另外一个好处就是它处理unsubscribing的时候，会停止整个调用链。如果你使用了一串很复杂的操作符，调用unsubscribe将会在他当前执行的地方终止。不需要做任何额外的工作！

# 操作符

操作符(Operators)

操作符就是为了解决对Observable对象的 变换(关键词) 的问题，操作符用于在Observable和最终的Subscriber之间修改Observable发出的事件。RxJava提供了很多很有用的操作符。

比如map操作符，就是用来把把一个事件转换为另一个事件的。

map()操作符：

Observable.just("images/logo.png") // 输入类型 String
    .map(new Func1<String, Bitmap>() {
        @Override
        public Bitmap call(String filePath) { // 参数类型 String
            return getBitmapFromPath(filePath); // 返回类型 Bitmap
        }
    })
    .subscribe(new Action1<Bitmap>() {
        @Override
        public void call(Bitmap bitmap) { // 参数类型 Bitmap
            showBitmap(bitmap);
        }
    });
	
使用lambda可以简化为:

Observable.just("images/logo.png") // 输入类型 String
    .map(
       	 	filePath -> getBitmapFromPath(filePath); // 返回类型 Bitmap
    	)
    .subscribe(
           		bitmap -> showBitmap(bitmap);
    		  );
可以看到，map() 方法将参数中的 String 对象转换成一个 Bitmap 对象后返回，而在经过 map() 方法后，事件的参数类型也由 String 转为了 Bitmap。这种直接变换对象并返回的，是最常见的也最容易理解的变换。不过 RxJava 的变换远不止这样，它不仅可以针对事件对象，还可以针对整个事件队列，这使得 RxJava 变得非常灵活。

map()操作符进阶：

Observable.just("Hello, world!")  
    .map(s -> s.hashCode())  
    .map(i -> Integer.toString(i))  
    .subscribe(s -> System.out.println(s));
	
是不是很酷？map()操作符就是用于变换Observable对象的，map操作符返回一个Observable对象，这样就可以实现链式调用，在一个Observable对象上多次使用map操作符，最终将最简洁的数据传递给Subscriber对象。

flatMap()操作符：

假设我有这样一个方法：

这个方法根据输入的字符串返回一个网站的url列表

Observable<List<String>> query(String text);   

Observable.flatMap()接收一个Observable的输出作为输入，同时输出另外一个Observable。直接看代码：

query("Hello, world!")  
    .flatMap(new Func1<List<String>, Observable<String>>() {  
        @Override  
        public Observable<String> call(List<String> urls) {  
            return Observable.from(urls);  
        }  
    })  
    .subscribe(url -> System.out.println(url));
	
query("Hello, world!")  
    .flatMap(urls -> Observable.from(urls))  
    .subscribe(url -> System.out.println(url));
	
flatMap()是不是看起来很奇怪？为什么它要返回另外一个Observable呢？理解flatMap的关键点在于，flatMap输出的新的Observable正是我们在Subscriber想要接收的。现在Subscriber不再收到List<String>，而是收到一些列单个的字符串，就像Observable.from()的输出一样。

flatMap() 和map()有一个相同点：它也是把传入的参数转化之后返回另一个对象。但需要注意，和 map() 不同的是， flatMap() 中返回的是个 Observable 对象，并且这个 Observable 对象并不是被直接发送到了 Subscriber 的回调方法中。flatMap() 的原理是这样的：

使用传入的事件对象创建一个 Observable 对象；

并不发送这个 Observable, 而是将它激活，于是它开始发送事件；

每一个创建出来的 Observable 发送的事件，都被汇入同一个 Observable ，而这个 Observable 负责将这些事件统一交给 Subscriber 的回调方法。这三个步骤，把事件拆成了两级，通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去。而这个『铺平』就是 flatMap() 所谓的 flat。

值得注意的是.from()是Observable创建时候用的，.flatMap()才是操作符；

其他操作符：

目前为止，我们已经接触了两个操作符，RxJava中还有更多的操作符，那么我们如何使用其他的操作符来改进我们的代码呢？

更多RxJava的操作符请查看：RxJava操作符大全

getTitle()返回null如果url不存在。我们不想输出”null”，那么我们可以从返回的title列表中过滤掉null值！

query("Hello, world!")  
    .flatMap(urls -> Observable.from(urls))  
    .flatMap(url -> getTitle(url))  
    .filter(title -> title != null)  
    .subscribe(title -> System.out.println(title));
	
filter()输出和输入相同的元素，并且会过滤掉那些不满足检查条件的。

如果我们只想要最多5个结果：

query("Hello, world!")  
    .flatMap(urls -> Observable.from(urls))  
    .flatMap(url -> getTitle(url))  
    .filter(title -> title != null)  
    .take(5)  
    .subscribe(title -> System.out.println(title));
	
take()输出最多指定数量的结果。

如果我们想在打印之前，把每个标题保存到磁盘：

query("Hello, world!")  
    .flatMap(urls -> Observable.from(urls))  
    .flatMap(url -> getTitle(url))  
    .filter(title -> title != null)  
    .take(5)  
    .doOnNext(title -> saveTitle(title))  
    .subscribe(title -> System.out.println(title));
	
doOnNext()允许我们在每次输出一个元素之前做一些额外的事情，比如这里的保存标题。

看到这里操作数据流是多么简单了么。你可以添加任意多的操作，并且不会搞乱你的代码。

RxJava包含了大量的操作符。操作符的数量是有点吓人，但是很值得你去挨个看一下，这样你可以知道有哪些操作符可以使用。弄懂这些操作符可能会花一些时间，但是一旦弄懂了，你就完全掌握了RxJava的威力。

感觉如何？

好吧，你是一个怀疑主义者，并且还很难被说服，那为什么你要关心这些操作符呢？

因为操作符可以让你对数据流做任何操作。

将一系列的操作符链接起来就可以完成复杂的逻辑。代码被分解成一系列可以组合的片段。这就是响应式函数编程的魅力。用的越多，就会越多的改变你的编程思维。

# 线程控制

线程控制(Scheduler)

假设你编写的Android app需要从网络请求数据。网络请求需要花费较长的时间，因此你打算在另外一个线程中加载数据。那么问题来了！

编写多线程的Android应用程序是很难的，因为你必须确保代码在正确的线程中运行，否则的话可能会导致app崩溃。最常见的就是在非主线程更新UI。

在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （调度器）。

使用RxJava，你可以使用subscribeOn()指定观察者代码运行的线程，使用observerOn()指定订阅者运行的线程

Scheduler 的 API

在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：

Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。

Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。

Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。

Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。

另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。

有了以上这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。

subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。

observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。

注意：observeOn() 指定的是 Subscriber 的线程，而这个 Subscriber 并不一定是 subscribe() 参数中的 Subscriber（这块参考RxJava变换部分），而是 observeOn() 执行时的当前 Observable 所对应的 Subscriber ，即它的直接下级 Subscriber 。

换句话说，observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可。

代码示例：

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
	
上面这段代码中，由于 subscribeOn(Schedulers.io()) 的指定，被创建的事件的内容 1、2、3、4 将会在 IO 线程发出；

而由于 observeOn(AndroidScheculers.mainThread()) 的指定，因此 subscriber 数字的打印将发生在主线程 。

事实上，这种在 subscribe() 之前写上两句subscribeOn(Scheduler.io()) 和 observeOn(AndroidSchedulers.mainThread()) 的使用方式非常常见，它适用于多数的 『后台线程取数据，主线程显示』的程序策略。

下面的实例，在Observable.OnSubscribe的call()中模拟了长时间获取数据过程，在Subscriber的noNext()中显示数据到UI。

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
				
至此，我们可以看到call()将会发生在 IO 线程，而showInfo(s)则被设定在了主线程。这就意味着，即使加载call()耗费了几十甚至几百毫秒的时间，也不会造成丝毫界面的卡顿。

值得注意：subscribeOn () 与 observeOn()都会返回了一个新的Observable，因此若不是采用上面这种直接流方式，而是分步调用方式，需要将新返回的Observable赋给原来的Observable，否则线程调度将不会起作用。



使用下面方式，最后发现“OnSubscribe”还是在默认线程中运行；原因是subscribeOn这类操作后，返回的是一个新的Observable。

observable.subscribeOn(Schedulers.io());
observable.observeOn(AndroidSchedulers.mainThread());
observable .subscribe(subscribe);

可以修改为下面两种方式：

observable = observable.subscribeOn(Schedulers.io());
observable = observable.observeOn(AndroidSchedulers.mainThread());
observable .subscribe(subscribe);

//OR
observable.subscribeOn(Schedulers.io())
.observeOn(AndroidSchedulers.mainThread())
.subscribe(subscribe);

前面讲到了，可以利用 subscribeOn() 结合 observeOn() 来实现线程控制，让事件的产生和消费发生在不同的线程。可是在了解了 map() flatMap() 等变换方法后，有些好事的（其实就是当初刚接触 RxJava 时的我）就问了：能不能多切换几次线程？

答案是：能。

因为 observeOn() 指定的是 Subscriber 的线程，而这个 Subscriber 并不是（严格说应该为『不一定是』，但这里不妨理解为『不是』）subscribe() 参数中的 Subscriber ，而是 observeOn() 执行时的当前 Observable 所对应的 Subscriber ，即它的直接下级 Subscriber 。换句话说，observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可。上代码：

Observable.just(1, 2, 3, 4) // IO 线程，由 subscribeOn() 指定
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.newThread())
    .map(mapOperator) // 新线程，由 observeOn() 指定
    .observeOn(Schedulers.io())
    .map(mapOperator2) // IO 线程，由 observeOn() 指定
    .observeOn(AndroidSchedulers.mainThread) 
    .subscribe(subscriber);  // Android 主线程，由 observeOn() 指定

如上，通过 observeOn() 的多次调用，程序实现了线程的多次切换。

不过，不同于 observeOn() ， subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。

又有好事的（其实还是当初的我）问了：如果我非要调用多次 subscribeOn() 呢？会有什么效果？

这个问题先放着，我们还是从 RxJava 线程控制的原理说起吧。

Scheduler 的原理

其实， subscribeOn() 和 observeOn() 的内部实现，也是用的 lift()。具体看图（不同颜色的箭头表示不同的线程）：

subscribeOn()原理图：

![image](https://github.com/wd18535470628/RxjavaDemo/blob/master/subscribeOn.jpg)

observeOn() 原理图：

![image](https://github.com/wd18535470628/RxjavaDemo/blob/master/observeOn.jpg)

从图中可以看出，subscribeOn() 和 observeOn() 都做了线程切换的工作（图中的 “schedule…” 部位）。不同的是， subscribeOn() 的线程切换发生在 OnSubscribe 中，即在它通知上一级 OnSubscribe 时，这时事件还没有开始发送，因此 subscribeOn() 的线程控制可以从事件发出的开端就造成影响；而 observeOn() 的线程切换则发生在它内建的 Subscriber 中，即发生在它即将给下一级 Subscriber 发送事件时，因此 observeOn() 控制的是它后面的线程。

最后，我用一张图来解释当多个 subscribeOn() 和 observeOn() 混合使用时，线程调度是怎么发生的（由于图中对象较多，相对于上面的图对结构做了一些简化调整）：



图中共有 5 处含有对事件的操作。由图中可以看出，①和②两处受第一个 subscribeOn() 影响，运行在红色线程；③和④处受第一个 observeOn() 的影响，运行在绿色线程；⑤处受第二个 onserveOn() 影响，运行在紫色线程；而第二个 subscribeOn() ，由于在通知过程中线程就被第一个 subscribeOn() 截断，因此对整个流程并没有任何影响。这里也就回答了前面的问题：当使用了多个 subscribeOn() 的时候，只有第一个 subscribeOn() 起作用。

延伸：doOnSubscribe()

doOnSubscribe()一般用于执行一些初始化操作.

然而，虽然超过一个的 subscribeOn() 对事件处理的流程没有影响，但在流程之前却是可以利用的。

在前面讲 Subscriber 的时候，提到过 Subscriber 的 onStart() 可以用作流程开始前的初始化。然而 onStart() 由于在 subscribe() 发生时就被调用了，因此不能指定线程，而是只能执行在 subscribe() 被调用时的线程。这就导致如果 onStart() 中含有对线程有要求的代码（例如在界面上显示一个 ProgressBar，这必须在主线程执行），将会有线程非法的风险，因为有时你无法预测 subscribe() 将会在什么线程执行。

而与 Subscriber.onStart() 相对应的，有一个方法 Observable.doOnSubscribe() 。它和 Subscriber.onStart() 同样是在 subscribe() 调用后而且在事件发送前执行，但区别在于它可以指定线程。默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；而如果在 doOnSubscribe() 之后有 subscribeOn() 的话，它将执行在离它最近的 subscribeOn() 所指定的线程。

示例：

Observable.create(onSubscribe)
    .subscribeOn(Schedulers.io())
    .doOnSubscribe(new Action0() {
        @Override
        public void call() {
            progressBar.setVisibility(View.VISIBLE); // 需要在主线程执行
        }
    })
    .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(subscriber);
	
如上，在 doOnSubscribe() 的后面跟一个 subscribeOn() ，就能指定准备工作的线程了。

# 这是一些基本的用法
![image](https://github.com/wd18535470628/RxjavaDemo/blob/master/1.jpg)


