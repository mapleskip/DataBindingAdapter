package net.uni_unity.commonadapter;

import android.support.annotation.LayoutRes;

/**
 * 对adapter中每个item的封装
 * Created by lemon on 06/12/2016.
 */

final class ItemView {
    @LayoutRes
    private int layoutRes;
    private int bindingVariable;

    public ItemView() {

    }

    public ItemView(int bindingVariable, @LayoutRes int resLayout) {
        this.bindingVariable = bindingVariable;
        this.layoutRes = resLayout;
    }

    public static ItemView create(int bindingVariable, @LayoutRes int resLayout) {
        return new ItemView(bindingVariable, resLayout);
    }

    public int layoutRes() {
        return layoutRes;
    }

    public int bindingVariable() {
        return bindingVariable;
    }

    public ItemView setBindingVariable(int bindingVariable) {
        this.bindingVariable = bindingVariable;
        return this;
    }

    public ItemView setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemView itemView = (ItemView) o;

        if (bindingVariable != itemView.bindingVariable) return false;
        return layoutRes == itemView.layoutRes;
    }

    @Override
    public int hashCode() {
        int result = bindingVariable;
        result = 31 * result + layoutRes;
        return result;
    }


}
