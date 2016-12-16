package net.uni_unity.commonadapter.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.uni_unity.commonadapter.ViewManager;

import java.util.List;

/**
 * 对于adapter的接口封装
 * Created by lemon on 06/12/2016.
 */

public interface BaseAdapter<T> extends Notifier {

    /**
     * 绑定adapter装载的数据
     * @param items
     */
    void setItems(@Nullable List<T> items);

    /**
     * 获取对应位置的数据对象
     * @param position
     * @return
     */
    T getAdapterItem(int position);

    /**
     * 布局文件与数据绑定
     * @param binding 布局文件对应的ViewDataBinding对象
     * @param bindingVariable 布局文件绑定的data的索引
     * @param layoutRes 布局文件id
     * @param position 对应的list中的位置
     * @param item 数据对象
     */
    void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item);

    /**
     * 构建布局文件对应的ViewDataBinding对象
     * @param inflater
     * @param layoutRes
     * @param viewGroup
     * @return
     */
    ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup);

    /**
     * 每个adapter都会持有一个ViewManager对象
     * 用于管理各种类型的item
     * @return
     */
    ViewManager<T> getViewManager();

}
