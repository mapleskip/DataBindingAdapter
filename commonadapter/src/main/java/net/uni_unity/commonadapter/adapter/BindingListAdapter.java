package net.uni_unity.commonadapter.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.uni_unity.commonadapter.Utils;
import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.callback.WeakBaseOnListChangedCallback;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

import java.util.List;

/**
 * ListView使用的adapter
 * Created by lemon on 06/12/2016.
 */

public class BindingListAdapter<T extends ViewModel> extends android.widget.BaseAdapter implements BaseAdapter<T> {

    private List<T> items;
    private LayoutInflater inflater;
    private final ViewManager<T> viewManager;
    private final WeakBaseOnListChangedCallback<T> callback = new WeakBaseOnListChangedCallback<>(this);


    public BindingListAdapter(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (inflater == null) {
            this.inflater = LayoutInflater.from(viewGroup.getContext());
        }
        viewManager.select(items.get(position));
        int layoutRes = viewManager.layoutRes();
        ViewDataBinding binding;
        T item = items.get(position);
        if (convertView == null) {
            binding = onCreateBinding(inflater, layoutRes, viewGroup);
            item.getDecorator().onViewCreated(binding);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        onBindBinding(binding, viewManager.bindingVariable(), layoutRes, position, item);
        return binding.getRoot();
    }

    @Override
    public int getItemViewType(int position) {
        // ListView与RecyclerView这两个方法的要求是不同的
        // ListView之类的AdapterList要求viewType所属的int类型值是连续，但是RecyclerView并没有这样的要求
        // 因此我们在ViewManager中管理了一张viewType的map,就是用于这里返回
        // 而在RecyclerView中使用的是对应的布局文件id作为唯一值返回
        T item = items.get(position);
        return viewManager.viewType(item);
    }

    @Override
    public int getViewTypeCount() {
        return viewManager.viewTypeCount();
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        if (this.items instanceof ObservableList) {
            ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
        }
        if (items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return items.get(position);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item) {
        if (bindingVariable != ViewManager.NO_VARIABLE_BINDING) {
            boolean result = binding.setVariable(bindingVariable, item);
            if (!result) {
                Utils.throwMissingVariable(binding, bindingVariable, layoutRes);
            }
            binding.executePendingBindings();
        }
        item.getDecorator().onDataBinded(binding);
    }


    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public ViewManager<T> getViewManager() {
        return this.viewManager;
    }

    @Override
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

}
