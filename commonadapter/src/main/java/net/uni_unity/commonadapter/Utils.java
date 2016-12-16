package net.uni_unity.commonadapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.os.Looper;
import android.support.annotation.LayoutRes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 这个文件直接使用了开源库的文件：https://github.com/evant/binding-collection-adapter
 * Created by lemon on 06/12/2016.
 */
public class Utils {

    public static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        // Yeah reflection is slow, but this only happens when there is a programmer error.
        String bindingVariableName;
        try {
            bindingVariableName = getBindingVariableName(context, bindingVariable);
        } catch (Resources.NotFoundException e) {
            // Fall back to int
            bindingVariableName = "" + bindingVariable;
        }
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }

    public static String getBindingVariableName(Context context, int bindingVariable) throws Resources.NotFoundException {
        try {
            return getBindingVariableByDataBinderMapper(bindingVariable);
        } catch (Exception e1) {
            try {
                return getBindingVariableByBR(context, bindingVariable);
            } catch (Exception e2) {
                throw new Resources.NotFoundException("" + bindingVariable);
            }
        }
    }

    private static String getBindingVariableByDataBinderMapper(int bindingVariable) throws Exception {
        Class<?> dataBinderMapper = Class.forName("android.databinding.DataBinderMapper");
        Method convertIdMethod = dataBinderMapper.getDeclaredMethod("convertBrIdToString", int.class);
        convertIdMethod.setAccessible(true);
        Constructor constructor = dataBinderMapper.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        Object result = convertIdMethod.invoke(instance, bindingVariable);
        return (String) result;
    }

    private static String getBindingVariableByBR(Context context, int bindingVariable) throws Exception {
        String packageName = context.getPackageName();
        Class BRClass = Class.forName(packageName + ".BR");
        Field[] fields = BRClass.getFields();
        for (Field field : fields) {
            int value = field.getInt(null);
            if (value == bindingVariable) {
                return field.getName();
            }
        }
        throw new Exception("not found");
    }

    public static void ensureChangeOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
        }
    }

}
