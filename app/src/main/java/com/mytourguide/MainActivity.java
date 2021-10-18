package com.mytourguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import model.SqliteDB;
import model.User;

public class MainActivity extends AppCompatActivity {

    private Button Login;
    private Button Regis;
    private EditText count;
    private EditText pwd;
    private List<User> userList;
    private List<User> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login = (Button) findViewById(R.id.login);
        Regis = (Button) findViewById(R.id.regin);
        count = (EditText) findViewById(R.id.count);
        pwd = (EditText) findViewById(R.id.pwd);

        //注册
        Regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=count.getText().toString().trim();
                String pass=pwd.getText().toString().trim();

                User user=new User();
                user.setUsername(name);
                user.setUserpwd(pass);
                user.setCollection("");
                user.setTrace("");

                int result = SqliteDB.getInstance(getApplicationContext()).saveUser(user);
                if (result == 1)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Succeed!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if (result == 0)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Empty Password!!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if (result == -1)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Already Registered!!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Unknown Error",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }

        });

        //登录
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = count.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                int result = SqliteDB.getInstance(getApplicationContext()).Quer(pass, name);
                if (result == 1)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Succeed!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //登录成功，保存账户昵称信息
                    SharedPreferences userInfo = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userInfo.edit();//获取Editor
                    //得到Editor后，写入需要保存的数据
                    editor.putString("username", name);
                    editor.putString("userpwd", pass);
                    editor.commit();//提交修改
                    Log.d("TAG", "保存用户信息成功");
                    //登录成功，页面跳转
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                else if (result == 0)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Unknown Username!!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if (result == -1)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"Wrong Password!!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

    }
}