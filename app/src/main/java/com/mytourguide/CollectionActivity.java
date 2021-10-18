package com.mytourguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mytourguide.databinding.ActivityCollectionBinding;

import java.util.ArrayList;

import model.SqliteDB;

public class CollectionActivity extends AppCompatActivity {

    private ActivityCollectionBinding binding;
    private ArrayList collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle("我的收藏夹");


        collection = new ArrayList<>();
        SharedPreferences userInfo = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String name = userInfo.getString("username", null);
        collection = SqliteDB.getInstance(getApplicationContext()).QuerCol(name);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(CollectionActivity.this, android.R.layout.simple_list_item_1, collection);
        ListView listview= (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object data_item = collection.get(position);
                Intent intent = new Intent();
                intent = new Intent(view.getContext(), ScrollingPlaceActivity.class);
                intent.putExtra("title", data_item.toString());
                view.getContext().startActivity(intent);
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
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}