package net.uni_unity.commonadapter.viewmodel;


/**
 * list装载的基础类型
 * 里面包含一个元数据的引用，而且实现了装饰器decorator的接口方法，可以在view创建后和数据绑定后做一些额外的逻辑
 * Created by lemon on 15/12/2016.
 */

public class BaseViewModel<T> implements ViewModel<T> {

    protected T dataModel;

    public BaseViewModel(T dataModel){
        this.dataModel=dataModel;
    }

    public T dataModel(){
        return dataModel;
    }


    public T getDataModel() {
        return dataModel;
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }
}
