package com.example.cc.mypiechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyPieChart.OnItemClickListener {

    private MyPieChart mMyPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyPieChart = findViewById(R.id.pieChart);

        mMyPieChart.setRadius(DensityUtils.dp2px(this,60));

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(1, R.color.chart_orange, true));
        pieEntries.add(new PieEntry(1, R.color.chart_green, false));
        pieEntries.add(new PieEntry(1, R.color.chart_blue, false));
        pieEntries.add(new PieEntry(1, R.color.chart_purple, false));
        pieEntries.add(new PieEntry(1, R.color.chart_mblue, false));
        pieEntries.add(new PieEntry(2, R.color.chart_turquoise, false));

        mMyPieChart.setPieEntries(pieEntries);

        mMyPieChart.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(int position) {
        //点击了哪一个区域的点击事件回调
    }
}
