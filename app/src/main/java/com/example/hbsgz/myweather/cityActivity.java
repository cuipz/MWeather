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
import java.util.Map;
import java.util.logging.Filter;

public class cityActivity extends Activity implements View.OnClickListener{

    private String cityNow;
    private String codeNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_select);
//        接收从MainActivity传递的消息
        Intent res = getIntent();
        cityNow = res.getStringExtra("city");
        String cityNowShow = "当前城市："+cityNow;
        codeNow = res.getStringExtra("code");
//        显示当前城市
        TextView tv = (TextView)findViewById(R.id.title_name);
        tv.setText(cityNowShow);
//        初始化布局
        initViews();
//        获取ImageView对象并创建监听事件
        ImageView titleBack = (ImageView)findViewById(R.id.title_back);
        titleBack.setOnClickListener((View.OnClickListener) this);
    }

//    处理监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                Intent it = new Intent();
                it.putExtra("cityCode",codeNow);
                it.putExtra("cityName",cityNow);
                setResult(RESULT_OK,it);
                finish();
                break;
            default:
                break;
        }
    }

//    初始化函数
    private void initViews(){
        ListView mlist = (ListView)findViewById(R.id.title_list);
//。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
//        实现getApplication方法，调用getCityList()函数提取城市信息数据
        MyApplication myApplication = (MyApplication)getApplication();
        final List<City> cityList = myApplication.getCityList();
        final ArrayList<String> cityListName = new ArrayList<>();
        for(City city : cityList){
//            将cityList中城市数据中的省份和城市名提取到cityListName中
            String cityListNameStr = city.getProvince() + "-" + city.getCity();
            cityListName.add(cityListNameStr);
        }
//        实现适配器，将cityListName中的城市信息在listview中显示
        ArrayAdapter myAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,cityListName);
        mlist.setAdapter(myAdapter);
//        添加点击事件
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                获取position位置上的城市信息
                City city = cityList.get(position);
//                实现intent，添加附加消息
                Intent it = new Intent();
                it.putExtra("cityCode",city.getNumber());
                it.putExtra("cityName",city.getCity());
                setResult(RESULT_OK,it);
                finish();
            }
        });
    }
}
