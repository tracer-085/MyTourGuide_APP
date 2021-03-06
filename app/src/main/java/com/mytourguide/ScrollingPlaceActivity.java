package com.mytourguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mytourguide.databinding.ActivityScrollingPlaceBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import model.SqliteDB;
import utils.SendGetThread;

public class ScrollingPlaceActivity extends AppCompatActivity {

    private ActivityScrollingPlaceBinding binding;
    private String cityName;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        binding = ActivityScrollingPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;

        Intent intent = getIntent();
        if(intent.getStringExtra("title") != null)
        {
            placeName = intent.getStringExtra("title");
            this.setTitle(intent.getStringExtra("title"));
        }
        if(intent.getStringExtra("placeName") != null)
        {
            placeName = intent.getStringExtra("placeName");
            this.setTitle(intent.getStringExtra("placeName"));
        }

        FloatingActionButton fab = binding.fab; //????????????
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //??????????????????
                SharedPreferences userInfo = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                String name = userInfo.getString("username", null);
                //?????????????????????
                SqliteDB.getInstance(getApplicationContext()).Collect(name, placeName);
                //????????????
                Snackbar.make(view, "????????????", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /**
         * ????????????????????????
         */
        try {
            setPlaceInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(ScrollingPlaceActivity.this,"????????????????????????",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(ScrollingPlaceActivity.this,HomeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * ????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setPlaceInfo() throws IOException, InterruptedException, JSONException {
        String url = "http://ali-spot.showapi.com/spotList?keyword=" + placeName;
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url, "AppCode " + "f6be2da2b4fb4b5ebebc0488ac732b8f");
        sendGetThread.start();
        sendGetThread.join();
        JSONObject placeInfo = new JSONObject(sendGetThread.getResult()).getJSONObject("showapi_res_body").getJSONObject("pagebean")
                .getJSONArray("contentlist").getJSONObject(0);
        if (placeInfo.has("content")) {
            binding.textInfoPlace.setText(placeInfo.getString("content"));
        }
        else if (placeInfo.has("summary")) {
            binding.textInfoPlace.setText(placeInfo.getString("summary"));
        }
        else if (placeInfo.has("address")){
            binding.textInfoPlace.setText(placeInfo.getString("address"));
        }
        else {
            binding.textInfoPlace.setText("???????????????");
        }
        if (placeInfo.has("picList") && placeInfo.getJSONArray("picList").length() != 0) {
            JSONArray pic = placeInfo.getJSONArray("picList");
            Log.d("pic", pic.toString());
            for(int i = 0; i < pic.length(); i++) {
                Log.d("pic", pic.getJSONObject(i).toString());
            }
            String src = placeInfo.getJSONArray("picList").getJSONObject(0).getString("picUrl");
            Bitmap bitmap = getHttpBitmap(src);
            binding.placeImage.setImageBitmap(bitmap);
        }
        if (placeInfo.has("attention")) {
            binding.textPlaceAttention.setText(placeInfo.getString("attention"));
        }
        else {
            binding.textPlaceAttention.setText("???????????????");
        }
        if (placeInfo.has("coupon")) {
            binding.textPlaceCoupon.setText(placeInfo.getString("coupon"));
        }
        else {
            binding.textPlaceCoupon.setText("???????????????");
        }
    }


    /**
     * ????????????????????????
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //????????????
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //?????????????????????6000?????????conn.setConnectionTiem(0);????????????????????????
            conn.setConnectTimeout(6000);
            //???????????????????????????
            conn.setDoInput(true);
            //???????????????
            conn.setUseCaches(false);
            //?????????????????????????????????
            //conn.connect();
            //???????????????
            InputStream is = conn.getInputStream();
            //??????????????????
            bitmap = BitmapFactory.decodeStream(is);
            //???????????????
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
