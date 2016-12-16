package net.uni_unity.databindingadapter.viewmodel;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.util.Log;

import net.uni_unity.commonadapter.viewmodel.BaseViewModel;
import net.uni_unity.commonadapter.viewmodel.Decorator;
import net.uni_unity.databindingadapter.databinding.SimpleItemBinding;
import net.uni_unity.databindingadapter.model.SimpleText;

/**
 * Created by lemon on 15/12/2016.
 */

public class SimpleTextModel extends BaseViewModel<SimpleText> {

    private Decorator decorator=new Decorator() {
        @Override
        public void onViewCreated(ViewDataBinding dataBinding) {
            ((SimpleItemBinding)dataBinding).tvName.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onDataBinded(ViewDataBinding dataBinding) {
            Log.d("bindTag","BIND:this is called");
        }
    };

    public SimpleTextModel(SimpleText dataModel) {
        super(dataModel);
    }

}
