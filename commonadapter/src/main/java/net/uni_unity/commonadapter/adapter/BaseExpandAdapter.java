package net.uni_unity.commonadapter.adapter;

import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

import java.util.List;

/**
 * 继承于BaseAdapter
 * 因为expandlistview负责绘制group和child的view，所以增加了相应部分的内容
 * Created by lemon on 13/12/2016.
 */

public interface BaseExpandAdapter<G extends ViewModel, C extends ViewModel> extends BaseAdapter<G>{

    ViewManager<C> getChildViewManager();

    void onBindChildBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int groupPosition, int childPosition, C item);

    void setChildItems(List<ObservableList<C>> childItems);


}
