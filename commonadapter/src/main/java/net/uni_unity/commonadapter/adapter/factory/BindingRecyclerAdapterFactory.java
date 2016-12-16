package net.uni_unity.commonadapter.adapter.factory;

import android.support.v7.widget.RecyclerView;

import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.adapter.BindingRecyclerViewAdapter;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

/**
 * RecyclerView 的adapter的构建工厂
 * Created by lemon on 13/12/2016.
 */

public interface BindingRecyclerAdapterFactory {
    <T extends ViewModel> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ViewManager<T> viewManager);

    BindingRecyclerAdapterFactory DEFAULT = new BindingRecyclerAdapterFactory() {
        @Override
        public <T extends ViewModel> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ViewManager<T> viewManager) {
            return new BindingRecyclerViewAdapter<>(viewManager);
        }
    };
}
