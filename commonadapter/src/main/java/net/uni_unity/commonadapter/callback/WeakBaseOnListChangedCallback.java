package net.uni_unity.commonadapter.callback;

import android.databinding.ObservableList;

import net.uni_unity.commonadapter.Utils;
import net.uni_unity.commonadapter.adapter.Notifier;

import java.lang.ref.WeakReference;

/**
 * Created by lemon on 06/12/2016.
 */

public class WeakBaseOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    final WeakReference<Notifier> notifierRef;

    public WeakBaseOnListChangedCallback(Notifier adapter) {
        this.notifierRef = new WeakReference<>(adapter);
    }

    @Override
    public void onChanged(ObservableList<T> ts) {
        Notifier notifier = notifierRef.get();
        if (notifier == null) {
            return;
        }
        Utils.ensureChangeOnMainThread();
        notifier.notifyDataChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
        onChanged(ts);
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
        onChanged(ts);
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
        onChanged(ts);
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
        onChanged(ts);
    }
}
