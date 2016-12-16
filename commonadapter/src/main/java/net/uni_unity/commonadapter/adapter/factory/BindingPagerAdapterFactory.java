package net.uni_unity.commonadapter.adapter.factory;

import android.support.v4.view.ViewPager;

import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.adapter.BindingPagerAdapter;
import net.uni_unity.commonadapter.viewmodel.ViewModel;

/**
 * ViewPager 的adapter的构建工厂
 * Created by lemon on 13/12/2016.
 */

public interface BindingPagerAdapterFactory {

    <T extends ViewModel> BindingPagerAdapter<T> create(ViewPager viewPager, ViewManager<T> viewManager);

    BindingPagerAdapterFactory DEFAULT=new BindingPagerAdapterFactory() {
        @Override
        public <T extends ViewModel> BindingPagerAdapter<T> create(ViewPager viewPager, ViewManager<T> viewManager) {
            return new BindingPagerAdapter<>(viewManager);
        }
    };
}
