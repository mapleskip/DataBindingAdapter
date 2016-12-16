package net.uni_unity.commonadapter.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import net.uni_unity.commonadapter.Utils;
import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.callback.WeakRecyclerOnListChangeCallback;
import net.uni_unity.commonadapter.viewholder.SimpleBindingViewHolder;
import net.uni_unity.commonadapter.viewholder.ViewHolderFactory;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

import java.util.List;

/**
 * Created by lemon on 13/12/2016.
 */

public class BindingRecyclerViewAdapter<T extends ViewModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BaseAdapter<T> {

    private static final Object DATA_INVALIDATION = new Object();
    private ViewManager<T> viewManager;
    private List<T> items;
    private WeakRecyclerOnListChangeCallback<T> callback = new WeakRecyclerOnListChangeCallback<>(this);
    @Nullable
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private ViewHolderFactory viewHolderFactory;

    public BindingRecyclerViewAdapter(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewDataBinding binding = onCreateBinding(inflater, layoutId, parent);
        final RecyclerView.ViewHolder holder = onCreateViewHolder(binding);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                return recyclerView != null && recyclerView.isComputingLayout();
            }

            @Override
            public void onCanceled(ViewDataBinding binding) {
                if (recyclerView == null || recyclerView.isComputingLayout()) {
                    return;
                }
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION);
                }
            }
        });
        return holder;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewDataBinding binding) {
        if (viewHolderFactory != null) {
            return viewHolderFactory.createViewHolder(binding);
        } else {
            return new SimpleBindingViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T item = items.get(position);
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        onBindBinding(binding, viewManager.bindingVariable(), viewManager.layoutRes(), position, item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (isForDataBinding(payloads)) {
            ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
            binding.executePendingBindings();
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    private boolean isForDataBinding(List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            return false;
        }
        for (int i = 0; i < payloads.size(); i++) {
            Object obj = payloads.get(i);
            if (obj != DATA_INVALIDATION) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        viewManager.select(items.get(position));
        return viewManager.layoutRes();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.recyclerView == null && items != null && items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (this.recyclerView != null && items != null && items instanceof ObservableList) {
            ((ObservableList<T>) items).removeOnListChangedCallback(callback);
        }
        this.recyclerView = null;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        if (recyclerView != null) {
            if (this.items instanceof ObservableList) {
                ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
            }
            if (items instanceof ObservableList) {
                ((ObservableList<T>) items).addOnListChangedCallback(callback);
            }
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

    public void setViewHolderFactory(ViewHolderFactory viewHolderFactory) {
        this.viewHolderFactory = viewHolderFactory;
    }
}
