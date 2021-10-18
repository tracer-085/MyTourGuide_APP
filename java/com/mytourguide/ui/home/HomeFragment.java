package com.mytourguide.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mytourguide.R;
import com.mytourguide.ScrollingCityActivity;
import com.mytourguide.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.HotCityAdapter;
import adapter.HotPlaceAdapter;
import model.HotCity;
import model.HotPlace;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView hotCityRecycler, hotPlaceRecycler;
    HotCityAdapter hotCityAdapter;
    HotPlaceAdapter hotPlaceAdapter;

    private EditText searchCity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        List<HotCity> hotCityList = new ArrayList<>();
        hotCityList.add(new HotCity("上海","上海市","中国", R.drawable.shanghai));
        hotCityList.add(new HotCity("北京","北京市","中国", R.drawable.beijing));
        hotCityList.add(new HotCity("重庆","重庆市","中国", R.drawable.chongqing));
        hotCityList.add(new HotCity("长沙","湖南省","中国", R.drawable.changsha));
        hotCityList.add(new HotCity("青岛","山东省","中国", R.drawable.qingdao));

        setHotCityRecycler(hotCityList);

        List<HotPlace> hotPlaceList = new ArrayList<>();
        hotPlaceList.add(new HotPlace("东方明珠","上海","上海市","中国",R.drawable.dongfangmingzhu));
        hotPlaceList.add(new HotPlace("上海欢乐谷","上海","上海市","中国",R.drawable.disney));
        hotPlaceList.add(new HotPlace("天安门广场","北京","北京市","中国",R.drawable.tiananm));
        hotPlaceList.add(new HotPlace("北京故宫","北京","北京市","中国",R.drawable.gugong));
        hotPlaceList.add(new HotPlace("玉龙雪山滑雪场","丽江","云南省","中国",R.drawable.yulong));
        hotPlaceList.add(new HotPlace("杭州西湖","杭州","浙江省","中国",R.drawable.xihu));
        hotPlaceList.add(new HotPlace("泰山","九江","江西省","中国",R.drawable.lushan));
        hotPlaceList.add(new HotPlace("苏州园林博物馆","苏州","江苏省","中国",R.drawable.suzhou));
        hotPlaceList.add(new HotPlace("世界之窗","深圳","广东省","中国",R.drawable.shijie));
        hotPlaceList.add(new HotPlace("岳阳楼","岳阳","湖南省","中国",R.drawable.yueyang));

        setHotPlaceRecycler(hotPlaceList);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchCity = binding.editTextTextPersonName4;
        searchCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String cityName = searchCity.getText().toString();
                Intent intent = new Intent();
                intent = new Intent(getContext(), ScrollingCityActivity.class);
                intent.putExtra("title", cityName);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setHotCityRecycler(List<HotCity> hotCityList) {

        hotCityRecycler = binding.hotcityRecycler;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        hotCityRecycler.setLayoutManager(layoutManager);
        hotCityAdapter = new HotCityAdapter(getContext(), hotCityList);
        hotCityRecycler.setAdapter(hotCityAdapter);
    }
    private void setHotPlaceRecycler(List<HotPlace> hotPlaceList) {

        hotPlaceRecycler = binding.hotplaceRecycler;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        hotPlaceRecycler.setLayoutManager(layoutManager);
        hotPlaceAdapter = new HotPlaceAdapter(getContext(), hotPlaceList);
        hotPlaceRecycler.setAdapter(hotPlaceAdapter);
    }
}