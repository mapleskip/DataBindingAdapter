package net.uni_unity.commonadapter.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import net.uni_unity.commonadapter.Utils;
import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.callback.WeakBaseOnListChangedCallback;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

import java.util.List;

/**
 *
 * Created by lemon on 13/12/2016.
 */

public class BindingExpandAdapter<G extends ViewModel, C extends ViewModel> extends BaseExpandableListAdapter implements BaseExpandAdapter<G, C> {

    private List<G> groupItems;
    private List<ObservableList<C>> childItems;
    private ViewManager<G> groupViewManager;
    private ViewManager<C> childViewManager;
    private LayoutInflater inflater;
    private WeakBaseOnListChangedCallback<G> groupCallback = new WeakBaseOnListChangedCallback<>(this);
    private WeakBaseOnListChangedCallback<ObservableList<C>> callback1 = new WeakBaseOnListChangedCallback<>(this);
    private WeakBaseOnListChangedCallback<C> callback2 = new WeakBaseOnListChangedCallback<>(this);

    public BindingExpandAdapter(ViewManager groupViewManager, ViewManager childViewManager) {
        this.groupViewManager = groupViewManager;
        this.childViewManager = childViewManager;
    }

    @Override
    public int getGroupCount() {
        return groupItems == null ? 0 : groupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItems == null ? 0 : childItems.get(groupPosition).size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return groupItems == null ? null : groupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItems == null ? null : childItems.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        groupViewManager.select(groupItems.get(groupPosition));
        int layoutRes = groupViewManager.layoutRes();
        ViewDataBinding dataBinding;
        G item = groupItems.get(groupPosition);
        if (convertView == null) {
            dataBinding = onCreateBinding(inflater, layoutRes, viewGroup);
            if(item.getDecorator()!=null){
                item.getDecorator().onViewCreated(dataBinding);
            }
        } else {
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        onBindBinding(dataBinding, groupViewManager.bindingVariable(), layoutRes, groupPosition, item);
        return dataBinding.getRoot();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        childViewManager.select(childItems.get(groupPosition).get(childPosition));
        int layoutRes = childViewManager.layoutRes();
        ViewDataBinding dataBinding;
        C item = childItems.get(groupPosition).get(childPosition);
        if (convertView == null) {
            dataBinding = onCreateBinding(inflater, layoutRes, viewGroup);
            if(item.getDecorator()!=null){
                item.getDecorator().onViewCreated(dataBinding);
            }
        } else {
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        onBindChildBinding(dataBinding, childViewManager.bindingVariable(), layoutRes, groupPosition, childPosition, item);
        return dataBinding.getRoot();
    }

    @Override
    public int getGroupType(int groupPosition) {
        G item = groupItems.get(groupPosition);
        return groupViewManager.viewType(item);
    }

    @Override
    public int getGroupTypeCount() {
        return groupViewManager.viewTypeCount();
    }

    @Override
    public int getChildTypeCount() {
        return childViewManager.viewTypeCount();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        C item = childItems.get(groupPosition).get(childPosition);
        return childViewManager.viewType(item);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public ViewManager<C> getChildViewManager() {
        return childViewManager;
    }

    @Override
    public void onBindChildBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int groupPosition, int childPosition, C item) {
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
    public void setChildItems(List<ObservableList<C>> childItems) {
        if (this.childItems == childItems) {
            return;
        }
        if (this.childItems instanceof ObservableList) {
            ((ObservableList<ObservableList<C>>) this.childItems).removeOnListChangedCallback(callback1);
        }
        if (this.childItems != null) {
            for (List<C> temp : this.childItems) {
                if (temp instanceof ObservableList) {
                    ((ObservableList<C>) temp).removeOnListChangedCallback(callback2);
                }
            }
        }

        if (childItems instanceof ObservableList) {
            ((ObservableList<ObservableList<C>>) childItems).addOnListChangedCallback(callback1);
        }

        for (List<C> temp : childItems) {
            if (temp instanceof ObservableList) {
                ((ObservableList<C>) temp).addOnListChangedCallback(callback2);
            }
        }
        this.childItems = childItems;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void setItems(@Nullable List<G> groupItems) {
        if (this.groupItems == groupItems) {
            return;
        }
        if (this.groupItems instanceof ObservableList) {
            ((ObservableList<G>) this.groupItems).removeOnListChangedCallback(groupCallback);
        }
        if (groupItems instanceof ObservableList) {
            ((ObservableList<G>) groupItems).addOnListChangedCallback(groupCallback);
        }
        this.groupItems = groupItems;
        notifyDataSetChanged();
    }

    @Override
    public G getAdapterItem(int position) {
        return groupItems == null ? null : groupItems.get(position);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, G item) {
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
    public ViewManager<G> getViewManager() {
        return groupViewManager;
    }
}
