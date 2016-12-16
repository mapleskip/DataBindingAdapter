package net.uni_unity.commonadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

/**
 * adapter当中的item视图管理对象
 * Created by lemon on 06/12/2016.
 */

public class ViewManager<T> {

    // 标记当前需要构建的item对象，ItemView是一个item的抽象
    private final ItemView itemView = new ItemView();
    // 下面两个map缓存adapter中装载的视图类型
    private final SimpleArrayMap<Integer, ItemView> itemViewMap;
    private final SimpleArrayMap<Class<? extends T>, Integer> viewTypeMap;
    // itemview不需要绑定对象
    public static final int NO_VARIABLE_BINDING = 0;

    private ViewManager(SimpleArrayMap<Integer, ItemView> itemViewMap, SimpleArrayMap<Class<? extends T>, Integer> viewTypeMap) {
        this.itemViewMap = itemViewMap;
        this.viewTypeMap = viewTypeMap;
    }

    /**
     * 通过select方法调整获取当前listview装载的item对象，动态切换布局的使用的文件
     * 例如，在listview的getview方法中，都应该调用这个方法，确保加载正确的布局文件
     * 这个方法会被频繁调用
     * @param item
     */
    public void select(T item) {
        int viewType = viewType(item);
        ItemView temp = itemViewMap.get(viewType);
        if (temp != null) {
            itemView.setBindingVariable(temp.bindingVariable()).setLayoutRes(temp.layoutRes());
        } else {
            throw new IllegalArgumentException("Missing class for item " + item);
        }
    }

    /**
     * 获取当前加载的item的数据对象索引
     * @return
     */
    public int bindingVariable() {
        return itemView.bindingVariable();
    }

    /**
     * 获取当前加载的布局文件
     * @return
     */
    public int layoutRes() {
        return itemView.layoutRes();
    }

    /**
     * 获取当前的item的视图类型
     * @param item
     * @return
     */
    public int viewType(T item) {
        return viewTypeMap.get(item.getClass());
    }

    public int viewTypeCount() {
        return itemViewMap.size();
    }

    public static class Builder<T> {
        private final SimpleArrayMap<Integer, ItemView> itemViewMap = new SimpleArrayMap<>();
        private final SimpleArrayMap<Class<? extends T>, Integer> viewTypeMap = new SimpleArrayMap<>();

        /**
         * adpter中支持的布局类型，都应该调用put方法添加缓存
         * @param itemClass
         * @param bindingVariable
         * @param layoutRes
         * @return
         */
        public ViewManager.Builder<T> put(@NonNull Class<? extends T> itemClass, int bindingVariable, @LayoutRes int layoutRes) {
            ItemView itemView=ItemView.create(bindingVariable, layoutRes);
            return put(itemClass,itemView);
        }

        public ViewManager.Builder<T> put(@NonNull Class<? extends T> itemClass, @NonNull ItemView itemView) {
            int viewType = viewTypeMap.size();
            viewTypeMap.put(itemClass, viewType);
            itemViewMap.put(viewType, itemView);
            return this;
        }

        public ViewManager<T> build() {
            return new ViewManager<>(itemViewMap, viewTypeMap);
        }
    }
}
