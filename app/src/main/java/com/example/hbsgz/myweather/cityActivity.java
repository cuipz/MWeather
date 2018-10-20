package com.example.hbsgz.myweather;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hbsgz.Bean.City;
import com.example.hbsgz.app.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

public class cityActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_select);

        initViews();

        ImageView titleBack = (ImageView)findViewById(R.id.title_back);
        titleBack.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                Intent it = new Intent();
                it.putExtra("cityCode","101010100");
                setResult(RESULT_OK,it);
                finish();
                break;
            default:
                break;
        }
    }

    private void initViews(){
        ListView mlist = (ListView)findViewById(R.id.title_list);

        MyApplication myApplication = (MyApplication)getApplication();
        final List<City> cityList = myApplication.getCityList();
        for(City city : cityList){
            //过滤器；
        }
        ArrayAdapter myAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,cityList);
        mlist.setAdapter(myAdapter);
        Intent res = getIntent();
        String cityNow = "当前城市："+res.getStringExtra("city");
        TextView tv = (TextView)findViewById(R.id.title_name);
        tv.setText(cityNow);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cityList.get(position);
                Intent it = new Intent();
                it.putExtra("cityCode",city.getNumber());
                setResult(RESULT_OK,it);
                finish();
            }
        });
    }
}
