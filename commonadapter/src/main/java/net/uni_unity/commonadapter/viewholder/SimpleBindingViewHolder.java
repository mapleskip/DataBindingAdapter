package net.uni_unity.commonadapter.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * 通过ViewDataBinding构造ViewHolder对象
 * Created by lemon on 13/12/2016.
 */

public class SimpleBindingViewHolder extends RecyclerView.ViewHolder {

    public SimpleBindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
    }
}
