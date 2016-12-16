package net.uni_unity.commonadapter;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import net.uni_unity.commonadapter.adapter.BindingExpandAdapter;
import net.uni_unity.commonadapter.adapter.BindingListAdapter;
import net.uni_unity.commonadapter.adapter.BindingPagerAdapter;
import net.uni_unity.commonadapter.adapter.BindingRecyclerViewAdapter;
import net.uni_unity.commonadapter.adapter.factory.BindingAdapterViewFactory;
import net.uni_unity.commonadapter.adapter.factory.BindingExpandAdapterFactory;
import net.uni_unity.commonadapter.adapter.factory.BindingPagerAdapterFactory;
import net.uni_unity.commonadapter.adapter.factory.BindingRecyclerAdapterFactory;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

import java.util.List;

/**
 * data binding注解的集中管理器
 * 实现了利用 @BindingAdapter 注解完成adapter的绑定与数据装载
 * Created by lemon on 13/12/2016.
 */

public class AnnotationController {

    /**
     * 通过 @BindingAdapter 为AdapterView绑定数据
     * @param adapterView
     * @param items AdapterView绑定的数据
     * @param factory adapter的构造工厂，默认会构建一个BindingListAdapter的实例
     * @param viewManager 每个BindingListAdapter的实例一定需要一个
     * @param <T>
     */
    @BindingAdapter(value = {"items","factory","viewManager"},requireAll = false)
    public static <T extends ViewModel> void setAdapter(AdapterView adapterView, List<T> items, BindingAdapterViewFactory factory, ViewManager<T> viewManager){
        if(viewManager==null){
            throw new IllegalArgumentException("the instance of ViewManager can not be null");
        }
        if (factory == null) {
            factory = BindingAdapterViewFactory.DEFAULT;
        }
        BindingListAdapter<T> adapter = (BindingListAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(adapterView, viewManager);
            adapter.setItems(items);
            adapterView.setAdapter(adapter);
        } else {
            adapter.setItems(items);
        }
    }

    /**
     * 通过 @BindingAdapter 为ViewPager绑定数据
     * @param viewPager
     * @param items
     * @param factory
     * @param viewManager
     * @param <T>
     */
    @BindingAdapter(value = {"items","factory","viewManager"},requireAll = false)
    public static <T extends ViewModel> void setAdapter(ViewPager viewPager, List<T> items, BindingPagerAdapterFactory factory,ViewManager<T> viewManager){
        if(viewManager==null){
            throw new IllegalArgumentException("the instance of ViewManager can not be null");
        }
        if (factory == null) {
            factory = BindingPagerAdapterFactory.DEFAULT;
        }
        BindingPagerAdapter<T> adapter = (BindingPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = factory.create(viewPager, viewManager);
            adapter.setItems(items);
            viewPager.setAdapter(adapter);
        } else {
            adapter.setItems(items);
        }
    }

    /**
     * 通过 @BindingAdapter 为RecyclerView绑定数据
     * @param recyclerView
     * @param items
     * @param factory
     * @param viewManager
     * @param <T>
     */
    @BindingAdapter(value = {"items","factory","viewManager"},requireAll = false)
    public static <T extends ViewModel> void setAdapter(RecyclerView recyclerView, List<T> items, BindingRecyclerAdapterFactory factory,ViewManager<T> viewManager){
        if(viewManager==null){
            throw new IllegalArgumentException("the instance of ViewManager can not be null");
        }
        if (factory == null) {
            factory = BindingRecyclerAdapterFactory.DEFAULT;
        }
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(recyclerView, viewManager);
            adapter.setItems(items);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setItems(items);
        }
    }

    /**
     * 通过 @BindingAdapter 为ExpandableListView绑定数据
     * @param expandableListView
     * @param groupItems
     * @param childItems
     * @param factory
     * @param groupViewManager
     * @param childViewManager
     * @param <G>
     * @param <C>
     */
    @BindingAdapter(value = {"groupItems","childItems","factory","groupViewManager","childViewManager"},requireAll = false)
    public static <G extends ViewModel,C extends ViewModel> void setAdapter(ExpandableListView expandableListView, List<G> groupItems, List<ObservableList<C>> childItems, BindingExpandAdapterFactory factory, ViewManager<G> groupViewManager, ViewManager<C> childViewManager){
        if(groupViewManager==null||childViewManager==null){
            throw new IllegalArgumentException("the instance of ViewManager can not be null");
        }
        if(factory==null){
            factory=BindingExpandAdapterFactory.DEFAULT;
        }
        BindingExpandAdapter<G,C> adapter= (BindingExpandAdapter<G, C>) expandableListView.getExpandableListAdapter();
        if(adapter==null){
            adapter=factory.create(expandableListView,groupViewManager,childViewManager);
            adapter.setItems(groupItems);
            adapter.setChildItems(childItems);
            expandableListView.setAdapter(adapter);
        }else{
            adapter.setItems(groupItems);
            adapter.setChildItems(childItems);
        }
    }

    /**
     * 设定RecyclerView使用的布局管理器LayoutManager
     * @param recyclerView
     * @param layoutManagerFactory
     */
    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }


}
