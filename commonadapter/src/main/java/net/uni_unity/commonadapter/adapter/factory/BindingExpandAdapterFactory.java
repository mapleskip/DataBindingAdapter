package net.uni_unity.commonadapter.adapter.factory;

import android.widget.ExpandableListView;

import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.adapter.BindingExpandAdapter;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

/**
 * ExpandableListView 的构建工厂
 * 需要构建BindingExpandAdapter子类的adapter的可以通过实现接口方法
 * 默认使用构建BindingExpandAdapter实例的工厂
 * Created by lemon on 13/12/2016.
 */

public interface BindingExpandAdapterFactory {

    <G extends ViewModel, C extends ViewModel> BindingExpandAdapter<G, C> create(ExpandableListView expandableListView, ViewManager<G> groupManager, ViewManager<C> childManager);

    BindingExpandAdapterFactory DEFAULT = new BindingExpandAdapterFactory() {
        @Override
        public <G extends ViewModel, C extends ViewModel> BindingExpandAdapter<G, C> create(ExpandableListView expandableListView, ViewManager<G> groupManager, ViewManager<C> childManager) {
            return new BindingExpandAdapter<>(groupManager, childManager);
        }
    };
}
