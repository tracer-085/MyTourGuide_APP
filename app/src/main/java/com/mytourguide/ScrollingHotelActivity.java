package com.mytourguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mytourguide.databinding.ActivityScrollingHotelBinding;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScrollingHotelActivity extends AppCompatActivity {

    private ActivityScrollingHotelBinding binding;

    String name;
    String engName;
    String address;
    String url;
    String type;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        binding = ActivityScrollingHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.getStringExtra("name") != null)
        {
            name = intent.getStringExtra("name");
            this.setTitle(intent.getStringExtra("name"));
            binding.hotelName.setText(name);
        }
        else
        {
            binding.hotelName.setText("暂无数据");
        }
        if(intent.getStringExtra("engName") != null)
        {
            engName = intent.getStringExtra("engName");
            binding.hotelEngName.setText(engName);
        }
        else
        {
            binding.hotelEngName.setText("暂无数据");
        }
        if(intent.getStringExtra("address") != null)
        {
            address = intent.getStringExtra("address");
            binding.hotelAdd.setText(address);
        }
        else
        {
            binding.hotelAdd.setText("暂无数据");
        }
        if(intent.getStringExtra("type") != null)
        {
            type = intent.getStringExtra("type");
            binding.hotelType.setText(type);
        }
        else
        {
            binding.hotelType.setText("暂无数据");
        }
        if(intent.getStringExtra("price") != null)
        {
            price = intent.getStringExtra("price");
            binding.hotelPrice.setText(price + "元/晚");
        }
        else
        {
            binding.hotelPrice.setText("暂无数据");
        }
        if(intent.getStringExtra("url") != null)
        {
            url = intent.getStringExtra("url");
            Bitmap bitmap = getHttpBitmap(url);
            binding.hotelImage.setImageBitmap(bitmap);
        }
    }

    /**
     * 获取网络图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}