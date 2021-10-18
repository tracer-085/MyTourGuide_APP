package com.mytourguide.ui.account;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mytourguide.CollectionActivity;
import com.mytourguide.MainActivity;
import com.mytourguide.R;
import com.mytourguide.databinding.FragmentAccountBinding;

import model.SqliteDB;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;

    private TextView logout;
    private TextView edit;
    private TextView tv;
    private TextView collect;
    private TextView trace;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        //与fragment_account.xml绑定
        View root = binding.getRoot();

        //获取用户昵称
        SharedPreferences userInfo = getActivity().getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String name = userInfo.getString("username", null);//读取username
        tv = binding.username;
        tv.setText("昵称: "+name);
        Log.d("TAG", "读取用户信息");


//        final TextView textView = binding.textAccount;
//        accountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //退出登录
        logout = (TextView) getView().findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //清空数据
                SharedPreferences userInfo = getActivity().getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();//获取Editor
                editor.clear();
                editor.commit();
                //回到登录界面
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //修改信息
        edit = (TextView) getView().findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view2 = View.inflate(getActivity(), R.layout.edit_info, null);
                builder.setTitle("提示");
                final EditText username = (EditText) view2.findViewById(R.id.username);
                final EditText password = (EditText) view2.findViewById(R.id.password);
                final EditText newpassword = (EditText) view2.findViewById(R.id.newpassword);
                // 设置参数
                builder.setTitle("修改个人信息").setIcon(android.R.drawable.ic_dialog_info)
                        .setView(view2);
                // 创建对话框
                final AlertDialog alertDialog = builder.create();
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //读取当前登录信息
                        SharedPreferences userInfo = getActivity().getSharedPreferences("CurrentUser", MODE_PRIVATE);
                        String name = userInfo.getString("username", null);
                        String pwd = userInfo.getString("userpwd", null);
                        String userName = username.getText().toString();
                        String userPwd = password.getText().toString();
                        //原个人信息输入正确
                        if (newpassword.getText().toString().length() == 0) {
                            Toast.makeText(getActivity(), "未输入新密码", Toast.LENGTH_SHORT).show();
                        }
                        else if (name.equals(userName) && pwd.equals(userPwd)) {
                            //修改当前个人信息
                            SharedPreferences.Editor editor = userInfo.edit();//获取Editor
                            //得到Editor后，写入需要保存的数据
                            editor.putString("userpwd", newpassword.getText().toString());
                            editor.commit();//提交修改
                            Log.d("TAG", "保存用户信息成功");
                            //修改数据库中数据
                            SqliteDB.getInstance(getContext()).Edit(userName, newpassword.getText().toString());
                            //打印修改成功提示
                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "原有昵称或密码输入错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setView(view2);
                builder.show();
            }
        });

        //查看收藏
        collect = (TextView) getView().findViewById(R.id.collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CollectionActivity.class);
                startActivity(intent);
            }
        });

        //查看行程
//        trace = (TextView) getView().findViewById(R.id.trace);
//        trace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), TraceActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
}