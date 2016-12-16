package net.uni_unity.commonadapter.callback;

import android.databinding.ObservableList;

import net.uni_unity.commonadapter.Utils;
import net.uni_unity.commonadapter.adapter.BindingRecyclerViewAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by lemon on 13/12/2016.
 */

public class WeakRecyclerOnListChangeCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {

    final WeakReference<BindingRecyclerViewAdapter> adapterRef;

    public WeakRecyclerOnListChangeCallback(BindingRecyclerViewAdapter adapter) {
        this.adapterRef = new WeakReference<>(adapter);
    }

    @Override
    public void onChanged(ObservableList sender) {
        BindingRecyclerViewAdapter adapter = adapterRef.get();
        if (adapter == null) {
            return;
        }
        Utils.ensureChangeOnMainThread();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, final int positionStart, final int itemCount) {
        BindingRecyclerViewAdapter adapter = adapterRef.get();
        if (adapter == null) {
            return;
        }
        Utils.ensureChangeOnMainThread();
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, final int positionStart, final int itemCount) {
        BindingRecyclerViewAdapter adapter = adapterRef.get();
        if (adapter == null) {
            return;
        }
        Utils.ensureChangeOnMainThread();
        adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList sender, final int fromPosition, final int toPosition, final int itemCount) {
        BindingRecyclerViewAdapter adapter = adapterRef.get();
        if (adapter == null) {
            return;
        }
        Utils.ensureChangeOnMainThread();
        for (int i = 0; i < itemCount; i++) {
            adapter.notifyItemMoved(fromPosition + i, toPosition + i);
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, final int positionStart, final int itemCount) {
        BindingRecyclerViewAdapter adapter = adapterRef.get();
        if (adapter == null) {
            return;
        }
        Utils.ensureChangeOnMainThread();
        adapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

}
