package com.example.hbsgz.myweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbsgz.Bean.TodayWeather;
import com.example.hbsgz.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int UPDATE_TODAY_WEATHER = 1;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private String secret;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
       public void handleMessage(android.os.Message msg){
           switch (msg.what){
               case UPDATE_TODAY_WEATHER:
                   updateTodayWeather((TodayWeather) msg.obj);
                   break;
               default:
                   break;
           }
       }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);

        initView();
        ImageView updateBtn = (ImageView) findViewById(R.id.title_update_btn);
        ImageView updateCity = (ImageView)findViewById(R.id.title_city_manager);
        updateBtn.setOnClickListener(this);
        updateCity.setOnClickListener(this);
//        queryWeatherCode();
    }

    void initView(){
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm2_5);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pmIm);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }

    void updateTodayWeather(TodayWeather todayWeather){
        secret = todayWeather.getCity();
        String twCity = todayWeather.getCity()+"天气";
        String twUpdatetime = todayWeather.getUpdatetime()+ "发布";
        String twShidu = "湿度："+todayWeather.getShidu();
        String twTemperature = todayWeather.getHigh()+"~"+todayWeather.getLow();
        String twWind = "风力:"+todayWeather.getFengli();

        switch (todayWeather.getType().trim()){
            case "暴雪": weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);break;
            case "大雪": weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);break;
            case "中雪": weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);break;
            case "小雪": weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);break;
            case "阵雪": weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);break;
            case "雨夹雪": weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);break;
            case "特大暴雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);break;
            case "大暴雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);break;
            case "暴雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);break;
            case "大雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);break;
            case "中雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);break;
            case "小雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);break;
            case "阵雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);break;
            case "雷阵雨": weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);break;
            case "雷阵雨冰雹": weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);break;
            case "多云": weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);break;
            case "晴": weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);break;
            case "雾": weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);break;
            case "阴": weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);break;
            case "沙尘暴": weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);break;
            default:weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);break;
        }

        if(todayWeather.getPm25()<100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if (todayWeather.getPm25()>100&&todayWeather.getPm25()<200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if (todayWeather.getPm25()>200&&todayWeather.getPm25()<300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }else {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

        city_name_Tv.setText(twCity);
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(twUpdatetime);
        humidityTv.setText(twShidu);
        pmDataTv.setText(String.valueOf(todayWeather.getPm25()));
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(twTemperature);
        climateTv.setText(todayWeather.getType());
        windTv.setText(twWind);
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
//        final String address = "https://www.baidu.com";

        Log.d("weather123",address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                TodayWeather todayWeather = null;

                try{
                    URL url = new URL(address);
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str = null;
                    while ((str = reader.readLine()) != null){      //while (reader.readLine() != null){
                        response.append(str);                        //     str = reader.readLine();                                                                     //     response.append(str);
                        Log.d("mine",str);                     //      Log.d("mine",str);
                    }                                               //  }
                    String responseStr = response.toString();
                    Log.d("weatherSyr",responseStr);
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null){
                        Message message = new Message();
                        message.what = UPDATE_TODAY_WEATHER;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (conn != null){
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("man_city_code","101010100");
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("weather","link");
                queryWeatherCode(cityCode);
            }else {
                Toast.makeText(MainActivity.this,"network error",Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.title_city_manager){
            Intent it = new Intent(MainActivity.this,cityActivity.class);
            it.putExtra("city",secret);
            startActivityForResult(it,1);
        }
    }

    private TodayWeather parseXML(String xmldata){

        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;

        try{
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp" )){
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null){
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity((xmlPullParser.getText()));
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals ("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(Integer.valueOf(xmlPullParser.getText()));
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return todayWeather;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK){
            String newCityCode = data.getStringExtra("cityCode");
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                queryWeatherCode(newCityCode);
            }else {
                Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_LONG).show();
            }
        }
    }
}