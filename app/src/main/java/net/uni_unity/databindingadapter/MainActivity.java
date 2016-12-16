package net.uni_unity.databindingadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import net.uni_unity.commonadapter.ViewManager;
import net.uni_unity.commonadapter.adapter.BindingListAdapter;
import net.uni_unity.commonadapter.viewmodel.ViewModel;
import net.uni_unity.databindingadapter.model.PieData;
import net.uni_unity.databindingadapter.model.SimpleText;
import net.uni_unity.databindingadapter.viewmodel.PieChartModel;
import net.uni_unity.databindingadapter.viewmodel.SimpleTextModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvContent;
    private Button addButton;
    private Button removeButton;
    private ViewManager<ViewModel> viewManager;
    private BindingListAdapter<ViewModel> adapter;
    private List<ViewModel> items=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        lvContent = (ListView) findViewById(R.id.lvContent);
        addButton = (Button) findViewById(R.id.btnAdd);
        removeButton = (Button) findViewById(R.id.btnRemove);
    }

    private void initData(){
        viewManager=new ViewManager.Builder<ViewModel>()
                .put(SimpleTextModel.class,BR.simpleModel,R.layout.simple_item)
                .put(PieChartModel.class,ViewManager.NO_VARIABLE_BINDING,R.layout.item_chart_view)
                .build();
//        for(int i=0;i<10;i++){
//            items.add(new SimpleTextModel(new SimpleText("name"+i,"tag"+i)));
//        }
        items.add(new SimpleTextModel(new SimpleText("name1","tag1")));
        items.add(new PieChartModel(new PieData(4,100)));

        adapter=new BindingListAdapter<>(viewManager);
        adapter.setItems(items);
        lvContent.setAdapter(adapter);
    }

}