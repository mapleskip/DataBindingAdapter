package net.uni_unity.commonadapter.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.uni_unity.commonadapter.Utils;
import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.callback.WeakBaseOnListChangedCallback;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

import java.util.List;

/**
 * ViewPager使用的Adapter
 * Created by lemon on 08/12/2016.
 */

public class BindingPagerAdapter<T extends ViewModel> extends PagerAdapter implements BaseAdapter<T> {

    private List<T> items;
    private LayoutInflater inflater;
    private ViewManager<T> viewManager;
    private WeakBaseOnListChangedCallback<T> callback = new WeakBaseOnListChangedCallback<>(this);
    private String[] pageTitles;


    public BindingPagerAdapter(ViewManager<T> viewManager) {
        this.viewManager = viewManager;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }
        T item = items.get(position);
        viewManager.select(item);
        ViewDataBinding binding = onCreateBinding(inflater, viewManager.layoutRes(), container);
        if(item.getDecorator()!=null){
            item.getDecorator().onViewCreated(binding);
        }
        onBindBinding(binding, viewManager.bindingVariable(), viewManager.layoutRes(), position, item);
        container.addView(binding.getRoot());
        binding.getRoot().setTag(item);
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles == null ? null : pageTitles[position];
    }

    public void setPageTitles(String[] pageTitles) {
        this.pageTitles = pageTitles;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
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
        if(item.getDecorator()!=null){
            item.getDecorator().onDataBinded(binding);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemPosition(Object object) {
        T item = (T) ((View) object).getTag();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (item == items.get(i)) {
                    return i;
                }
            }
        }
        return POSITION_NONE;
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {

        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public ViewManager<T> getViewManager() {
        return viewManager;
    }

    @Override
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

}
