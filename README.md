# CommonAdapter
一个基于android data binding实现的adapter库，支持多种viewType类型的item.目前已经实现包括ListView、ViewPager、ExpandableListView RecyclerView的adater实现

---

android data binding library为我们提供了一个页面与数据绑定的技术，尤其是android gradle plugin在2.2的版本之后，更是开始支持了双向绑定以及lambada表达式，使得data binding的技术更加成熟，而将data bingding技术结合的mvvm设计结构也越来越得到更多的关注。在前面我的文章里面，已经从源码的角度，讲述了data binding库的一些实现细节。现在结合data binding的技术实现一个adapter库。

> 声明：这个库是在看了GitHub上面的一个开源项目[binding-collection-adapter](https://github.com/evant/binding-collection-adapter)，然后结合自己的理解以及一些实际的经验进行了优化和代码整体结构的调整。对于源码中优秀的工具类，我都没有修改，按照原来的进行保留，并在文件头标明了文件的出处。

## 整体功能描述

目前已经完成的功能：

1. 支持在列表中装载多种viewType的item。每个item对应一种布局文件以及一种ViewModle；
2. 基于data binding库设计，因此支持通过data binding技术（在XML文件中配置）完成adapter的构建、数据加载以及其他data binding相应的技术实现；
3. 支持在item的view视图被data binding构建之后，对view视图执行更多的配置操作。比如当item是一个图标的时候，可能需要对图标进行更多自定义的属性配置。这时候可以通过data binding的注解技术实现（如果你很熟悉data binding的话），也可以通过decorator对象的接口回调进行设置。
4. 支持在data bingding的视图和数据绑定之后，接收回调，执行额外的操作；
5. 实现了ListView、ViewPager、RecyclerView、ExpandableListView对应的adapter

目前还不完善的功能；

1. 当你使用的是RecyclerView的时候，只能通过ViewHolder的factory构建自己的ViewHolder实例，并在ViewHolder中对view进行自定义配置，不能使用decorator对象开放的接口进行前置的视图配置
2. adapter当中的ViewDataBinding对象还不支持自定义的DataBindingComponent
3. 其他可能需要但还没被支持的属性。。。


## 代码整体类图

![](/assets/in-post/common-adapter-uml.png)

## 代码设计概述

上面的类图基本上描述了代码中的结构，用两个表达式阐述整个库的功能实现就是：

__adapter = ViewManager + ViewModle + Factory__
__ViewModle = Decorator + model(data)__

### ViewManager

ViewManager的设计初衷其实就是为了管理adapter需要装载的各种item的样式。每个adapter在初始化的时候都必须构建一个ViewManager的实例，这个ViewManager的实例标识了这个adapter需要加载的item的全部类型。

在库当中，我将每个item抽象成了一个ItemView的对象， 主要包含了两个属性，一个是对应的布局文件ID，一个是item所绑定的data的索引值，也就是data binding中BR.java当中的值。而ViewManager则封装了对ItemView的操作逻辑，使得item视图选择对外部是透明的。

ViewManager当中核心的逻辑就只有两个部分：

* 通过ViewManager的内部类Builder构建ViewManager实例的时候的put()方法。所有adapter需要支持的item类型都应该使用这个方法进行添加；
* ViewManager当中的select()方法，主要是根据当前需要布局的item类型，选择加载合适的布局文件以及数据绑定；

另外ViewManager当中还包含了一个 __NO_VARIABLE_BINDING__ 的常量值，当item不需要进行数据绑定的时候，可以使用这个标志位进行标识。

### Decorator

当我们使用data binding技术之后，data binding库会自动的为我们初始化视图，并且将数据与视图进行绑定。但是很多时候，当我们使用的是一些复杂的item视图类型的时候，我们需要对item里面的视图进行很多的配置操作。比如说我们在item当中加载的是一个图标的时候，我们需要对图标进行很多的属性配置。这种情况下，如果纯粹的依赖data binding的话，可能需要我们通过注解@BindAdapter完成相应的设置。如果不习惯这种做法的话，我们这个时候就可以使用decorator对象了。

Decorator是一个接口，包含两个接口方法：

* void onViewCreated(ViewDataBinding dataBinding);

这个方法会在视图被data binding库加载之后回调

* void onDataBinded(ViewDataBinding dataBinding);

这个方法会在视图与数据绑定之后被回调。

目前的实现中，ViewModle可以通过依赖注入增加这个接口的实现，而ViewModle对象是我们list装载的item数据内容。也就是说每个item其实都具备一个Decorator的实现。当我们需要对item的视图进行更多的配置的时候，就可以实现这个接口，并注入到我们的ViewModle里面。

### ViewModel

我们库当中的list装载的内容要求都是ViewModel的子类。也就是说我们需要将实际应用的model再进行封装一层。
在库当中的实现，我还是通过依赖注入将model（data）数据注入到ViewModel当中，而且我们可以往ViewModel注入更多的数据，也可以将一些交互的事件的实现在ViewModel当中。

### Factory

库当中的所有的adapter都实现了对应的工厂接口方法，我们可以通过实现工厂方法返回我们需要的自定义的adapter类型。默认都会提供一个DEFAULT的工厂，返回对应的adpter的基类。

### AnnotationController

这个类是库当中处理data binding相关注解的实现中枢。库提供了通过在XML当中配置属性，完成adpter的构建以及数据绑定的操作。如果你很熟悉data binding的使用方法的话，你可以通过添加对应的XML属性设置就可以完成adapter的绑定了，非常的简单快捷。如果你不熟悉这样的方式，也可以使用传统的在java代码中进行实例化和设置操作。

## 愿景

目前这个库的设计还比较初期，功能也只是完成了几本覆盖，并且因为是基于data binding技术设计的，因此项目当中必须在gradle当中打开data binding的开关才可以。接下来会针对实际应用中的需求不断完善这个库的功能。

另外data binding在项目中只是负责了数据与视图绑定的操作，对于库当中其他的核心功能以及整个库的架构是没有强耦合的，因此我们完全可以通过开放数据绑定的逻辑接口方法，就能够实现一个不依赖data binding的adapter库。
