package net.uni_unity.commonadapter.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lemon on 13/12/2016.
 */

public interface ViewHolderFactory {
    RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding);

}
