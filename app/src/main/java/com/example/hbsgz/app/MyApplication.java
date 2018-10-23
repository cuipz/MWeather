package com.example.hbsgz.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.hbsgz.Bean.City;
import com.example.hbsgz.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private static final String TAG = "MyAPP";
    private static MyApplication mApplication;
    private CityDB mCityDB;
    private List<City> mCityList;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }

//    初始化CityList
    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                开辟子线程，准备数据
                prepareCityList();
            }
        }).start();
    }

//    准备数据,从数据库中得到数据
    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        int i = 0;
//        遍历mCityList
        for(City city : mCityList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
//            Log.d(TAG,cityCode+":"+cityName);
        }
        return true;
    }

//获取城市信息
    public List<City> getCityList() {
//        mCityList = mCityDB.getAllCity();
//        int i = 0;
//        for(City city : mCityList) {
//            i++;
//            Log.d("111111111111111111", city.getCity()+"111111111111111111111111111111111111111111111111111111111111");
//        }
        return mCityList;
    }

    public static MyApplication getInstane(){
        return mApplication;
    }

//    打开数据库接口
    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath ()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator + CityDB.CITY_DB_NAME;
        File db = new File(path);
//        判断文件是否存在,不存在则创建文件
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1" + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();                //建立多层文件夹；
                Log.i("MyApp","mkdirs");
            }
//                Log.i("MyApp","db is not exists");
            try {
//                打开数据库,输入流读数据
                InputStream is = getAssets().open("city.db");
//                文件输出流将数据输出到创建的文件中
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }
}
