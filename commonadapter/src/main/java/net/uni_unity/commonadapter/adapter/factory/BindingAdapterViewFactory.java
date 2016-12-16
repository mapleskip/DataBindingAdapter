package net.uni_unity.commonadapter.adapter.factory;

import android.widget.AdapterView;

import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.adapter.BindingListAdapter;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

/**
 * AdapterView 的adapter的构建工厂
 * 用户可以通过实现这个接口，构建自己的BindingListAdapter的子类
 * 默认的情况下提供一个构建BindingListAdapter的工厂
 * Created by lemon on 13/12/2016.
 */

public interface BindingAdapterViewFactory {

    <T extends ViewModel> BindingListAdapter<T> create(AdapterView adapterView, ViewManager<T> viewManager);

    BindingAdapterViewFactory DEFAULT = new BindingAdapterViewFactory() {
        @Override
        public <T extends ViewModel> BindingListAdapter<T> create(AdapterView adapterView, ViewManager<T> viewManager) {
            return new BindingListAdapter<>(viewManager);
        }
    };
}
