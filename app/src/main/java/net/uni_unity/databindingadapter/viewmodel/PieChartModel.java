package net.uni_unity.databindingadapter.viewmodel;


import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import net.uni_unity.commonadapter.viewmodel.BaseViewModel;
import net.uni_unity.commonadapter.viewmodel.Decorator;
import net.uni_unity.databindingadapter.databinding.ItemChartViewBinding;
import net.uni_unity.databindingadapter.model.PieData;

import java.util.ArrayList;

/**
 * Created by lemon on 15/12/2016.
 */

public class PieChartModel extends BaseViewModel<PieData> {

    private Decorator decorator=new Decorator() {
        @Override
        public void onViewCreated(ViewDataBinding dataBinding) {
            PieChart mChart = ((ItemChartViewBinding) dataBinding).chart;
            mChart.setUsePercentValues(true);
            mChart.setExtraOffsets(5, 10, 5, 5);

            mChart.setDragDecelerationFrictionCoef(0.95f);

            mChart.setDrawHoleEnabled(true);
            mChart.setHoleColor(Color.WHITE);
            mChart.setTransparentCircleColor(Color.WHITE);
            mChart.setTransparentCircleAlpha(110);

            mChart.setHoleRadius(58f);
            mChart.setTransparentCircleRadius(61f);

            mChart.setDrawCenterText(true);

            mChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            mChart.setRotationEnabled(true);
            mChart.setHighlightPerTapEnabled(true);

            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener

//        setData(4, 100);

            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            // mChart.spin(2000, 0, 360);

//        mSeekBarX.setOnSeekBarChangeListener(this);
//        mSeekBarY.setOnSeekBarChangeListener(this);

            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
            mChart.setEntryLabelTextSize(12f);
        }

        @Override
        public void onDataBinded(ViewDataBinding dataBinding) {
            PieChart mChart = ((ItemChartViewBinding) dataBinding).chart;
            setData(mChart,dataModel);
        }
    };

    protected String[] mParties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };


    public PieChartModel(PieData dataModel) {
        super(dataModel);
    }

    @Override
    public Decorator getDecorator() {
        return super.getDecorator();
    }


    public void setData(PieChart mChart, PieData pieData) {
        float mult = pieData.range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < pieData.count; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        com.github.mikephil.charting.data.PieData data = new com.github.mikephil.charting.data.PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

}
