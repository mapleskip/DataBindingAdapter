package net.uni_unity.commonadapter.viewmodel;

import android.databinding.ViewDataBinding;

/**
 * 装饰器
 * ViewModel实现了这个接口，这样子每个ViewModel都可以在view被data binding初始化之后进行一些额外的操作
 * 也可以在数据绑定之后进行一些额外的处理
 * Created by lemon on 15/12/2016.
 */

public interface Decorator {

    /**
     * 视图被data binding创建之后回调这个方法
     * @param dataBinding 视图对应的ViewDataBinding实例
     */
    void onViewCreated(ViewDataBinding dataBinding);

    void onDataBinded(ViewDataBinding dataBinding);
}
