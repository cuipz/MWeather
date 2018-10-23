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

    private String cityNameNow = "北京";
    private String cityCodeNow = "101010100";

//    创建一个handler，在接收到子线程发送的消息后调用更新函数updateTodayWeather()更新天气信息
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
       public void handleMessage(android.os.Message msg){
           switch (msg.what){
//               判断接收到的消息
               case UPDATE_TODAY_WEATHER:
//                   更新数据
                   updateTodayWeather((TodayWeather) msg.obj);
                   break;
               default:
                   break;
           }
       }
    };

//    复写onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);

        initView();
        ImageView updateBtn = (ImageView) findViewById(R.id.title_update_btn);
        ImageView updateCity = (ImageView)findViewById(R.id.title_city_manager);
//        设置点击事件
        updateBtn.setOnClickListener(this);
        updateCity.setOnClickListener(this);
//        默认初始显示城市的天气信息
        queryWeatherCode(cityCodeNow);
    }

//    初始化天气显示界面
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

//    更新天气
    void updateTodayWeather(TodayWeather todayWeather){
//        cityNameNow = todayWeather.getCity();
        String twCity = todayWeather.getCity()+"天气";
        String twUpdatetime = todayWeather.getUpdatetime()+ "发布";
        String twShidu = "湿度："+todayWeather.getShidu();
        String twTemperature = todayWeather.getHigh()+"~"+todayWeather.getLow();
        String twWind = "风力:"+todayWeather.getFengli();

//        更新今日天气图片
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
//        更新pm2.5图片
        if(todayWeather.getPm25()<100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if (todayWeather.getPm25()>100&&todayWeather.getPm25()<200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if (todayWeather.getPm25()>200&&todayWeather.getPm25()<300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }else {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

//        更新数据
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
//        消息提示框提示更新成功
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

//    从服务器获取天气数据
    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
//        final String address = "https://www.baidu.com";

//        Log.d("weather123",address);
//        创建一个新的线程，用来进行网络访问
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                TodayWeather todayWeather = null;
                try{
//                    建立并打开一个http协议的URL连接，设置传输的一些参数
                    URL url = new URL(address);
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
//                    使用输入流接受服务器返回的数据
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str = null;
                    while ((str = reader.readLine()) != null){      //while (reader.readLine() != null){
                        response.append(str);                        //     str = reader.readLine();                                                                     //     response.append(str);
//                        Log.d("mine",str);                     //      Log.d("mine",str);
                    }                                               //  }
                    String responseStr = response.toString();
//                    Log.d("weatherSyr",responseStr);
//                    对服务器返回的数据进行解析
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null){
//                        使用handler将解析后的数据传递给主线程
                        Message message = new Message();
                        message.what = UPDATE_TODAY_WEATHER;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }
                }catch (Exception e){
//                    异常处理
                    e.printStackTrace();
                }finally {
                    if (conn != null){
//                        关闭http URL连接
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

//    对点击事件的处理
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_update_btn){
//            SharedPreferences存储数据
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("man_city_code",cityCodeNow);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
//                Log.d("weather","link");
//                更新数据
                queryWeatherCode(cityCode);
            }else {
                Toast.makeText(MainActivity.this,"network error",Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.title_city_manager){
//            创建一个intent携带消息跳转至cityActivity，并要求cityActivity返回时携带消息
            Intent it = new Intent(MainActivity.this,cityActivity.class);
            it.putExtra("city",cityNameNow);
            it.putExtra("code",cityCodeNow);
            startActivityForResult(it,1);
        }
    }

//    XML解析
    private TodayWeather parseXML(String xmldata){

        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;

        try{
//            调用API XmlPullParser进行XML解析
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
//                    判断是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
//                        判断是否为标签开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp" )){
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null){
//                            将解析后的数据读出并存放在todayWeather中作为返回值
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
//                        判断是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
//                进入下一个元素
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
//            异常处理
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return todayWeather;
    }

//    对cityActivity返回的消息的处理
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
//        判断标记是否符合条件
        if (requestCode == 1 && resultCode == RESULT_OK){
            String newCityCode = data.getStringExtra("cityCode");
            cityNameNow = data.getStringExtra("cityName");
            cityCodeNow = newCityCode;
//            判断当前网络是否可用
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
//                按新的城市代码更新天气信息
                queryWeatherCode(newCityCode);
            }else {
                Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_LONG).show();
            }
        }
    }
}