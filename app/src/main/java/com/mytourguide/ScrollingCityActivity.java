package com.mytourguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mytourguide.databinding.ActivityScrollingCityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import adapter.NewsAdapter;
import model.CityInfo;
import model.ProvinceTotalInfo;
import utils.China;
import utils.IdUtils;
import utils.NewsBean;
import utils.NewsUtils;
import utils.SendGetThread;
import utils.listviewHeight;

public class ScrollingCityActivity extends AppCompatActivity {

    private ActivityScrollingCityBinding binding;
    private TextView search;

    String[] name;
    String[] engName;
    String[] address;
    String[] src;
    String[] type;
    String[] price;
    String baike;
    String cityName;
    String provinceName;
    String myCity;
    String provinceID;
    String cityID;
    private ArrayList places;
    private ArrayList tickets;
    private ArrayList hotels;
    private ArrayList news;
    private China chinaEpidemic;

    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private IdUtils idUtils;
    private listviewHeight listviewHeight;
    private NewsUtils newsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        binding = ActivityScrollingCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        cityName = intent.getStringExtra("title");
        if(intent.getStringExtra("title") != null)
        {
            this.setTitle(intent.getStringExtra("title"));
        }

        search = binding.searchText;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???????????????
                AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingCityActivity.this);
                View view2 = View.inflate(ScrollingCityActivity.this, R.layout.search_place, null);
                builder.setTitle("??????");
                final EditText placeName = (EditText) view2.findViewById(R.id.placeName);
                // ????????????
                builder.setTitle("??????????????????").setIcon(R.drawable.ic_baseline_search_24)
                        .setView(view2);
                // ???????????????
                final AlertDialog alertDialog = builder.create();
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //???????????????Intent???????????????????????????
                        Intent intent=new Intent();
                        intent = new Intent(ScrollingCityActivity.this, ScrollingPlaceActivity.class);
                        intent.putExtra("cityName", cityName);
                        intent.putExtra("placeName", placeName.getText().toString());
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "??????", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setView(view2);
                builder.show();
            }
        });



        idUtils = new IdUtils();

        /**
         * ????????????????????????
         */
        // ??????LocationClient???
        mLocationClient = new LocationClient(getApplicationContext());
        mBDLocationListener = new MyBDLocationListener();
        // ????????????
        mLocationClient.registerLocationListener(mBDLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);// ????????????????????????????????????
        // ??????????????????
        mLocationClient.setLocOption(option);
        // ????????????
        mLocationClient.start();

        /**
         * ?????????????????????
         */
        try {
            setIntroduction();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(ScrollingCityActivity.this,"??????????????????????????????",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


        /**
         * ???????????????????????????
         */
        try {
            setWeather();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(ScrollingCityActivity.this,"????????????????????????",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


        /**
         * ???????????????????????????
         */
        try {
            setProvince();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(ScrollingCityActivity.this,"??????????????????????????????",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        /**
         * ???????????????????????????
         */
        try {
            setChinaEpidemic();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(ScrollingCityActivity.this,"????????????????????????",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


        /**
         * ???????????????????????????????????????
         */
        try {
            setProvinceID();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(ScrollingCityActivity.this,"????????????????????????????????????",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        /**
         * ???????????????????????????????????????
         */
        if(!(cityName.equals("??????") || cityName.equals("??????") || cityName.equals("??????") || cityName.equals("??????"))) {
            try {
                setCityID();
            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(ScrollingCityActivity.this,"????????????????????????????????????",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        //????????????
        this.setTitle(cityName + '???' + provinceName);

        /**
         * ???????????????????????????
         */
        binding.buttonSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setPlacesList();
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(ScrollingCityActivity.this, android.R.layout.simple_list_item_1, places);
                    ListView listview= (ListView) findViewById(R.id.placeslist);
                    listview.setAdapter(adapter);
                    listviewHeight.setListViewHeightBasedOnChildren(listview);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object data_item = places.get(position);
                            Intent intent = new Intent();
                            intent = new Intent(view.getContext(), ScrollingPlaceActivity.class);
                            intent.putExtra("title", data_item.toString());
                            intent.putExtra("cityName",cityName);
                            view.getContext().startActivity(intent);
                        }
                    });
                    binding.buttonDelPlace.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.clear();
                            ViewGroup.LayoutParams params = binding.placeslist.getLayoutParams();
                            params.height = 0;
                            //listView.getDividerHeight()???????????????????????????????????????
                            //params.height??????????????????ListView???????????????????????????
                            binding.placeslist.setLayoutParams(params);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(ScrollingCityActivity.this,"??????????????????????????????",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        /**
         * ????????????????????????????????????
         */
        binding.buttonSearchTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????
                AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingCityActivity.this);
                View view2 = View.inflate(ScrollingCityActivity.this, R.layout.search_place, null);
                final TextView hint = (TextView) view2.findViewById(R.id.text1);
                hint.setText("?????????yyyyMMdd???");
                final EditText dateName = (EditText) view2.findViewById(R.id.placeName);
                // ????????????
                builder.setTitle("??????????????????").setIcon(R.drawable.ic_baseline_search_24)
                        .setView(view2);
                // ???????????????
                final AlertDialog alertDialog = builder.create();
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try {
                            if (!cityName.equals(myCity)) {
                                setTicketsList(dateName.getText().toString());
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ScrollingCityActivity.this, android.R.layout.simple_list_item_1, tickets);
                                ListView listview2 = (ListView) findViewById(R.id.ticketslist);
                                listview2.setAdapter(adapter2);
                                listviewHeight.setListViewHeightBasedOnChildren(listview2);
                                binding.buttonDelTrain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        adapter2.clear();
                                        ViewGroup.LayoutParams params = binding.ticketslist.getLayoutParams();
                                        params.height = 0;
                                        //listView.getDividerHeight()???????????????????????????????????????
                                        //params.height??????????????????ListView???????????????????????????
                                        binding.ticketslist.setLayoutParams(params);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(ScrollingCityActivity.this,"????????????????????????????????????",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "??????", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setView(view2);
                builder.show();
            }
        });



        /**
         * ??????????????????
         */
        binding.buttonSearchHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setHotelsList();
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(ScrollingCityActivity.this, android.R.layout.simple_list_item_1, hotels);
                    ListView listview3 = (ListView) findViewById(R.id.hotelslist);
                    listview3.setAdapter(adapter3);
                    listviewHeight.setListViewHeightBasedOnChildren(listview3);

                    listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object data_item = hotels.get(position);
                            Intent intent = new Intent();
                            intent = new Intent(view.getContext(), ScrollingHotelActivity.class);
                            intent.putExtra("name", name[position]);
                            intent.putExtra("engName", engName[position]);
                            intent.putExtra("address", address[position]);
                            intent.putExtra("type", type[position]);
                            intent.putExtra("price", price[position]);
                            intent.putExtra("url", src[position]);
                            view.getContext().startActivity(intent);
                        }
                    });

                    binding.buttonDelHotel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter3.clear();
                            ViewGroup.LayoutParams params = binding.hotelslist.getLayoutParams();
                            params.height = 0;
                            //listView.getDividerHeight()???????????????????????????????????????
                            //params.height??????????????????ListView???????????????????????????
                            binding.hotelslist.setLayoutParams(params);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(ScrollingCityActivity.this,"??????????????????????????????",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        /**
         * ???????????????????????????
         */
        binding.searchText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    news = newsUtils.getAllNews(ScrollingCityActivity.this, cityName);
                    BaseAdapter newsAdapter = new NewsAdapter(ScrollingCityActivity.this, news);
                    ListView listview4 = (ListView) findViewById(R.id.newsList);
                    listview4.setAdapter(newsAdapter);
                    listviewHeight.setListViewHeightBasedOnChildren(listview4);

                    listview4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NewsBean bean = (NewsBean) parent.getItemAtPosition(position);
                            String url = bean.news_url;
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    });

                    // TODO ??????????????????
                    binding.constrain1.removeView(binding.searchText2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(ScrollingCityActivity.this,"??????????????????????????????",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(ScrollingCityActivity.this,HomeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * ????????????????????????api
     */
    private class MyBDLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // ????????????
            if (location != null) {
                // ??????BDLocation ?????????????????????????????????????????????
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                myCity = location.getCity().substring(0,2);
                int a = location.getLocType();
                Log.d("tag", String.valueOf(a));
                if (myCity != null) {
                    // ??????????????????????????????
                    Log.d("TAG", "address:" + myCity + " latitude:" + latitude
                            + " longitude:" + longitude + "???");
                    binding.startCity.setText("???????????????" + myCity);
                    binding.destiCity.setText("???????????????" + cityName);
                    Log.d("startcity", binding.startCity.getText().toString());
                    Log.d("desticity", binding.destiCity.getText().toString());
                    mLocationClient.stop();
                }
            }
        }
    }


    /**
     * ??????????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setIntroduction() throws IOException, InterruptedException, JSONException {
        String url = "https://route.showapi.com/883-1?showapi_appid=795835&showapi_sign=d44cc2b0271d4c22869a1f9a377c89d8"
                + "&url=http://www.thinkcheng.com/" + cityName + ".html";
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url,"");
        sendGetThread.start();
        sendGetThread.join();
        JSONObject JsonObject = new JSONObject(sendGetThread.getResult());
        JSONArray introJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONArray("all_list");
        if(introJsonArray.length() != 0) {
            binding.textBaike.setText(introJsonArray.getString(0));
            binding.textCBD.setText(introJsonArray.getString(7));
            binding.textFood.setText(introJsonArray.getString(5));
        }
    }



    /**
     * ????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setWeather() throws IOException, InterruptedException, JSONException {
        String url = "http://wthrcdn.etouch.cn/WeatherApi?city=" + cityName;
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url,"");
        sendGetThread.start();
        sendGetThread.join();
        String weather = sendGetThread.getResult();
        Log.d("weather", weather);

        parseXML(weather);
    }

    /**
     * ??????????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setProvince() throws IOException, InterruptedException, JSONException {
        String url = "https://route.showapi.com/1149-1?showapi_appid=795835&showapi_sign=d44cc2b0271d4c22869a1f9a377c89d8"
                + "&areaName=" + cityName + "???";
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url,"");
        sendGetThread.start();
        sendGetThread.join();
        JSONObject JsonObject = new JSONObject(sendGetThread.getResult());
        JSONArray provinceJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONArray("data");
        if(provinceJsonArray.length() != 0) {
            String wholeName = provinceJsonArray.getJSONObject(0).getString("wholeName");
            String[] names = wholeName.split(",");
            provinceName = names[1];
            Log.d("province", provinceName);
        }
    }

    /**
     * ??????????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setProvinceID() throws IOException, InterruptedException, JSONException {
        String url = "http://ali-spot.showapi.com/level1";
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url,"AppCode " + "f6be2da2b4fb4b5ebebc0488ac732b8f");
        sendGetThread.start();
        sendGetThread.join();
        JSONObject JsonObject = new JSONObject(sendGetThread.getResult());
        JSONArray provinceListJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONArray("list");
        for(int i = 0; i < provinceListJsonArray.length(); i++) {
            if(provinceListJsonArray.getJSONObject(i).getString("name").equals(provinceName.substring(0,2))) {
                provinceID = provinceListJsonArray.getJSONObject(i).getString("id");
            }
        }
        Log.d("provinceID", provinceID);
    }

    /**
     * ????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setChinaEpidemic() throws IOException, InterruptedException, JSONException {
        String url = "https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";
        SendGetThread sendGetThread = new SendGetThread(url,"");
        sendGetThread.start();
        sendGetThread.join();
        chinaEpidemic = new China(sendGetThread.getResult());
        if ((cityName.equals("??????") || cityName.equals("??????") || cityName.equals("??????") || cityName.equals("??????"))) {
            ProvinceTotalInfo provinceTotalInfo = chinaEpidemic.getProvinceTotalInfoPojo(cityName);
            binding.textVirusNow.setText(String.valueOf(provinceTotalInfo.getNowConfirm()));
            binding.textVirusTotal.setText(String.valueOf(provinceTotalInfo.getConfirm()));
            binding.textVirusRecover.setText(String.valueOf(provinceTotalInfo.getHeal()));
            binding.textVirusDead.setText(String.valueOf(provinceTotalInfo.getDead()));
        }
        ArrayList<CityInfo> cityCovidInfo = chinaEpidemic.getCityInfoListByProvinceName(provinceName.substring(0,2));
        Log.d("cityinfo", cityCovidInfo.toString());
        for(int i = 0; i < cityCovidInfo.size(); i++) {
            if(cityCovidInfo.get(i).getName().equals(cityName)) {
                binding.textVirusNow.setText(String.valueOf(cityCovidInfo.get(i).getNowConfirm()));
                binding.textVirusTotal.setText(String.valueOf(cityCovidInfo.get(i).getConfirm()));
                binding.textVirusRecover.setText(String.valueOf(cityCovidInfo.get(i).getHeal()));
                binding.textVirusDead.setText(String.valueOf(cityCovidInfo.get(i).getDead()));
            }
        }
    }

    /**
     * ??????????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setCityID() throws IOException, InterruptedException, JSONException {
        String url = "http://ali-spot.showapi.com/level2?proId=" + provinceID;
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url,"AppCode " + "f6be2da2b4fb4b5ebebc0488ac732b8f");
        sendGetThread.start();
        sendGetThread.join();
        JSONObject JsonObject = new JSONObject(sendGetThread.getResult());
        JSONArray provinceListJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONArray("list");
        if(provinceListJsonArray.length() != 0) {
            for (int i = 0; i < provinceListJsonArray.length(); i++) {
                if (!provinceListJsonArray.getJSONObject(i).has("cityName")) {
                    if (provinceListJsonArray.getJSONObject(i).getString("name").equals(cityName)) {
                        cityID = provinceListJsonArray.getJSONObject(i).getString("id");
                    }
                }
            }
            Log.d("cityID", cityID);
        }
    }

    /**
     * ??????????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setPlacesList() throws IOException, InterruptedException, JSONException {
        String url;
        if(cityName.isEmpty()) {
            return;
        }
        if ((cityName.equals("??????") || cityName.equals("??????") || cityName.equals("??????") || cityName.equals("??????"))) {
            url = "http://ali-spot.showapi.com/spotList?proId=" + provinceID;
        }
        else {
            url = "http://ali-spot.showapi.com/spotList?cityId=" + cityID;
        }
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url, "AppCode " + "f6be2da2b4fb4b5ebebc0488ac732b8f");
        sendGetThread.start();
        sendGetThread.join();
        String data = sendGetThread.getResult();
        JSONObject JsonObject = new JSONObject(data);
        if(!JsonObject.getJSONObject("showapi_res_body").has("pagebean")) {
            return;
        }
        JSONArray placeListJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("contentlist");

        places = new ArrayList<>();
        for (int i = 0; i < placeListJsonArray.length(); i++) {
            String placeName = placeListJsonArray.getJSONObject(i).getString("name");
            Log.d("TAG", placeName);
            places.add(placeName);
        }
    }

    /**
     * ?????????????????????api
     * @param date
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTicketsList(String date) throws IOException, InterruptedException, JSONException {
        String url = "https://route.showapi.com/1651-1?showapi_appid=795835&showapi_sign=d44cc2b0271d4c22869a1f9a377c89d8"
                + "&departStation=" + myCity + "&arrivalStation=" + cityName + "&date=" + date;
        Log.d("url",url);
        SendGetThread sendGetThread = new SendGetThread(url, "");
        sendGetThread.start();
        sendGetThread.join();
        String data = sendGetThread.getResult();
        JSONObject JsonObject = new JSONObject(data);
        JSONArray ticketListJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONArray("trains");
        Log.d("T", ticketListJsonArray.toString());

        tickets = new ArrayList<>();
        if(ticketListJsonArray.length() == 0) {
            return;
        }
        for(int i = 0; i < ticketListJsonArray.length(); i++) {
            String ticketInfo = '\n' + "?????????" + ticketListJsonArray.getJSONObject(i).getString("departStation");
            ticketInfo = ticketInfo + "  ?????????" + ticketListJsonArray.getJSONObject(i).getString("arrivalStation");
            ticketInfo = ticketInfo + "  ?????????" +  ticketListJsonArray.getJSONObject(i).getString("trainNum") + '\n';
            JSONObject train = ticketListJsonArray.getJSONObject(i).getJSONObject("tickets");
            if (train.has("specialseat")) {
                ticketInfo = ticketInfo + "?????????" + "  ?????????" + train.getJSONObject("specialseat").getString("seats")
                        + "  ?????????" + train.getJSONObject("specialseat").getString("price") + '\n';
            }
            if (train.has("businessseat")) {
                ticketInfo = ticketInfo + "?????????" + "  ?????????" + train.getJSONObject("businessseat").getString("seats")
                        + "  ?????????" + train.getJSONObject("businessseat").getString("price") + '\n';
            }
            if (train.has("firstseat")) {
                ticketInfo = ticketInfo + "?????????" + "  ?????????" + train.getJSONObject("firstseat").getString("seats")
                        + "  ?????????" + train.getJSONObject("firstseat").getString("price") + '\n';
            }
            if (train.has("secondseat")) {
                ticketInfo = ticketInfo + "?????????" + "  ?????????" + train.getJSONObject("secondseat").getString("seats")
                        + "  ?????????" + train.getJSONObject("secondseat").getString("price") + '\n';
            }
            if (train.has("softsleeperdown")) {
                ticketInfo = ticketInfo + "??????" + "  ?????????" + train.getJSONObject("softsleeperdown").getString("seats")
                        + "  ?????????" + train.getJSONObject("softsleeperdown").getString("price") + '\n';
            }
            if (train.has("hardsleepermid")) {
                ticketInfo = ticketInfo + "??????" + "  ?????????" + train.getJSONObject("hardsleepermid").getString("seats")
                        + "  ?????????" + train.getJSONObject("hardsleepermid").getString("price") + '\n';
            }
            if (train.has("hardseat")) {
                ticketInfo = ticketInfo + "??????" + "  ?????????" + train.getJSONObject("hardseat").getString("seats")
                        + "  ?????????" + train.getJSONObject("hardseat").getString("price") + '\n';
            }
            if (train.has("noseat")) {
                ticketInfo = ticketInfo + "??????" + "  ?????????" + train.getJSONObject("noseat").getString("seats")
                        + "  ?????????" + train.getJSONObject("noseat").getString("price") + '\n';
            }
            Log.d("TAG", ticketInfo);
            tickets.add(ticketInfo);
        }
    }


    /**
     * ??????????????????api
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setHotelsList() throws IOException, InterruptedException, JSONException {
        String url = "https://route.showapi.com/1653-1?showapi_appid=795835&showapi_sign=d44cc2b0271d4c22869a1f9a377c89d8"
                + "&page=0" + "&cityName=" + cityName + "&sortKey=recommend" + "&limit=10";
        SendGetThread sendGetThread = new SendGetThread(url, "");
        sendGetThread.start();
        sendGetThread.join();
        String data = sendGetThread.getResult();
        JSONObject JsonObject = new JSONObject(data);
        JSONArray hotelListJsonArray = JsonObject.getJSONObject("showapi_res_body").getJSONObject("data").getJSONArray("hotelList");
        Log.d("T", hotelListJsonArray.toString());

        if(hotelListJsonArray.length() == 0) {
            return;
        }
        hotels = new ArrayList<>();
        name = new String[10];
        engName = new String[10];
        address = new String[10];
        type = new String[10];
        price = new String[10];
        src = new String[10];
        for(int i = 0; i < hotelListJsonArray.length(); i++) {
            name[i] = hotelListJsonArray.getJSONObject(i).getString("chineseName");
            engName[i] = hotelListJsonArray.getJSONObject(i).getString("englishName");
            address[i] = hotelListJsonArray.getJSONObject(i).getString("address");
            type[i] = hotelListJsonArray.getJSONObject(i).getString("starName");
            price[i] = hotelListJsonArray.getJSONObject(i).getString("price");
            src[i] = hotelListJsonArray.getJSONObject(i).getString("picture");
            String hotelInfo = '\n' + name[i] + '\n' + type[i] + ' ' + price[i] + "???/???";
            Log.d("TAG", hotelInfo);
            hotels.add(hotelInfo);
        }
    }


    /**
     * ??????XML??????
     */
    private void parseXML(String xmlData)
    {
        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        int zhishuCount = 0;
        String fengli = "";
        String day1 = "";
        String day2 = "";
        String day3 = "";
        String day4 = "";
        String day5 = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            Log.d("MWeather","start parse xml");

            while(eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //??????????????????
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse","start doc");
                        break;
                    //????????????????????????
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("wendu"))
                        {
                            eventType=xmlPullParser.next();
                            Log.d("wendu",xmlPullParser.getText());
                            binding.textCenti.setText(xmlPullParser.getText() + "???");
                        }
                        else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0)
                        {
                            eventType=xmlPullParser.next();
                            Log.d("fengli",xmlPullParser.getText());
                            fengliCount++;
                            fengli = xmlPullParser.getText();
                        }
                        else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0)
                        {
                            eventType=xmlPullParser.next();
                            Log.d("fengxiang",xmlPullParser.getText());
                            fengxiangCount++;
                            binding.textWind.setText(fengli + xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("date"))
                        {
                            eventType=xmlPullParser.next();
                            Log.d("date",xmlPullParser.getText());
                            dateCount++;
                            if(dateCount == 1) {
                                day1 = day1 + xmlPullParser.getText() + "  ";
                            }
                            else if (dateCount == 2) {
                                day2 = day2 + xmlPullParser.getText() + "  ";
                            }
                            else if (dateCount == 3) {
                                day3 = day3 + xmlPullParser.getText() + "  ";
                            }
                            else if (dateCount == 4) {
                                day4 = day4 + xmlPullParser.getText() + "  ";
                            }
                            else{
                                day5 = day5 + xmlPullParser.getText() + "  ";
                            }
                        }
                        else if (xmlPullParser.getName().equals("high"))
                        {
                            eventType=xmlPullParser.next();
                            Log.d("high",xmlPullParser.getText());
                            highCount++;
                            if(highCount == 1) {
                                day1 = day1 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else if (highCount == 2) {
                                day2 = day2 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else if (highCount == 3) {
                                day3 = day3 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else if (highCount == 4) {
                                day4 = day4 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else{
                                day5 = day5 + "???" + xmlPullParser.getText() + "  ";
                            }
                        }
                        else if (xmlPullParser.getName().equals("low"))
                        {
                            eventType=xmlPullParser.next();
                            Log.d("low",xmlPullParser.getText());
                            lowCount++;
                            if(lowCount == 1) {
                                day1 = day1 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else if (lowCount == 2) {
                                day2 = day2 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else if (lowCount == 3) {
                                day3 = day3 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else if (lowCount == 4) {
                                day4 = day4 + "???" + xmlPullParser.getText() + "  ";
                            }
                            else{
                                day5 = day5 + "???" + xmlPullParser.getText() + "  ";
                            }
                        }
                        else if (xmlPullParser.getName().equals("type"))
                        {
                            eventType=xmlPullParser.next();
                            Log.d("type",xmlPullParser.getText());
                            typeCount++;
                            if(typeCount == 1) {
                                day1 = day1 + xmlPullParser.getText() + "  ";
                                binding.textType.setText(xmlPullParser.getText());
                            }
                            else if (typeCount == 3) {
                                day2 = day2 + xmlPullParser.getText() + "  ";
                            }
                            else if (typeCount == 5) {
                                day3 = day3 + xmlPullParser.getText() + "  ";
                            }
                            else if (typeCount == 7) {
                                day4 = day4 + xmlPullParser.getText() + "  ";
                            }
                            else  if (typeCount == 9) {
                                day5 = day5 + xmlPullParser.getText() + "  ";
                            }
                        }
                        else if (xmlPullParser.getName().equals("value")) {
                            if (zhishuCount == 12) {
                                eventType=xmlPullParser.next();
                                Log.d("value",xmlPullParser.getText());
                                binding.textZhishu.setText(xmlPullParser.getText());
                            }
                            zhishuCount++;
                        }
                        else if (xmlPullParser.getName().equals("detail")) {
                            if (zhishuCount == 13) {
                                eventType=xmlPullParser.next();
                                Log.d("detail",xmlPullParser.getText());
                                binding.textDetail.setText(xmlPullParser.getText());
                            }
                            zhishuCount++;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType=xmlPullParser.next();
            }
            binding.textDay1.setText(day1);
            binding.textDay2.setText(day2);
            binding.textDay3.setText(day3);
            binding.textDay4.setText(day4);
            binding.textDay5.setText(day5);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}