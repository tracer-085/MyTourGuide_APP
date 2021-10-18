//package com.mytourguide;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.Gravity;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//import com.mytourguide.databinding.ActivityTraceBinding;
//
//import java.util.LinkedList;
//
//import model.SqliteDB;
//
//public class TraceActivity extends AppCompatActivity {
//
//    private ActivityTraceBinding binding;
//
//
//    private LinearLayout linearLayout;
//    private TextView tv_day1;
//    private EditText et_day1;
//    private TextView now;
//    private ImageButton btn_create;
//    private Button btn_save;
//    private Button btn_get;
//    // “+”按钮控件List
//    private LinkedList<ImageButton> listIBTNAdd;
//    //textView控件List
//    private LinkedList<TextView> listTextView;
//    // “+”按钮ID索引
//    private int btnIDIndex = 1000;
//    //edittext控件List
//    private LinkedList<EditText> listEditText;
//    // “-”按钮控件List
//    private LinkedList<ImageButton> listIBTNDel;
//    private int iETContentWidth;   // EditText控件宽度
//    private int iETContentHeight;  // EditText控件高度
//    private int iTVContentWidth;   //textView控件高度
//    private int iTVContentHeight;   //textView控件高度
//    private int iIBContentWidth;   //imageButton控件边长
//    private float fDimRatio = 1.0f; // 尺寸比例（实际尺寸/xml文件里尺寸）
//    private Message msg;
//    private String[] myTrace;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityTraceBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        Toolbar toolbar = binding.toolbar;
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle("我的行程");
//
//        findAllViews();
//        listIBTNAdd = new LinkedList<ImageButton>();
//        listIBTNDel = new LinkedList<ImageButton>();
//        listTextView = new LinkedList<TextView>();
//        listEditText = new LinkedList<EditText>();
//
//        //加载数据库中已有行程
//        btn_get.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myTrace = new String[]{};
//                SharedPreferences userInfo = getSharedPreferences("CurrentUser", MODE_PRIVATE);
//                String name = userInfo.getString("username", null);
//                myTrace = SqliteDB.getInstance(getApplicationContext()).QuerTrace(name);
//                String mytrace = "";
//                for (int i = 0; i < myTrace.length; i++) {
//                    mytrace = mytrace + "DAY" + (i + 1) + ": " + myTrace[i] + "\n";
//                }
//                now.setText(mytrace);
//            }
//        });
//
//
//        btn_create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                iETContentWidth = et_day1.getWidth();   // EditText控件宽度
//                iETContentHeight = et_day1.getHeight();  // EditText控件高度
//                iTVContentWidth = tv_day1.getWidth();   //textView控件高度
//                iTVContentHeight = tv_day1.getHeight();   //textView控件高度
//                iIBContentWidth = btn_create.getWidth();
//                fDimRatio = iETContentWidth / 150;
//
//                addContent(v);
//            }
//        });
//        listIBTNAdd.add(btn_create);
//        listIBTNDel.add(null);
//        listEditText.add(et_day1);
//
//        final Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if(msg.what == 1) {
//                    //获取当前行程
//                    String newTrace = "";
//                    for (int i = 0; i < listEditText.size(); i++) {
//                        newTrace = newTrace + listEditText.get(i).getText().toString() + ',';
//                    }
//                    //读取登录信息
//                    SharedPreferences userInfo = getSharedPreferences("CurrentUser", MODE_PRIVATE);
//                    String name = userInfo.getString("username", null);
//                    // 将当前行程保存到数据库中
//                    SqliteDB.getInstance(getApplicationContext()).EditTrace(name, newTrace);
//                }
//                return true;
//            }
//        });
//
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.sendEmptyMessage(1);
//                    }
//                });
//                thread.start();
//            }
//        });
//
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if(id == android.R.id.home){
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//
//
//
//    @SuppressWarnings("ResourceType")
//    private void addContent(View v) {
//        if (v == null)
//            return;
//
//        int index = -1;
//        for (int i = 0; i < listIBTNAdd.size(); i++) {
//            if (listIBTNAdd.get(i) == v) {
//                index = i;
//                break;
//            }
//        }
//
//        if (index >= 0) {
//            //判断点击按钮的下方是否已经存在按钮，是则将按钮从最底部插入
//            try {
//                if (listIBTNAdd.get(index + 1) != null) {
//                    index = listIBTNAdd.size();
//                }
//            } catch (Exception e) {
//                index += 1;
//            }
//
//
//            //开始添加控件
//
//            //创建外围linearlayout控件
//            LinearLayout layout = new LinearLayout(this);
//            LinearLayout.LayoutParams ILayoutlayoutParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            ILayoutlayoutParams.setMargins(0, (int) (fDimRatio * 15), 0, 0);
//            layout.setGravity(Gravity.CENTER);
//            layout.setLayoutParams(ILayoutlayoutParams);
//
//            //创建内部textView控件
//            TextView tvContent = new TextView(this);
//            LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(
//                    iTVContentWidth, iTVContentHeight
//            );
//            tvContent.setText("Day " + (index + 1));
//            layout.addView(tvContent);
//            listTextView.add(tvContent);
//
//            //创建内部edittext控件
//            EditText etContent = new EditText(this);
//            LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
//                    iETContentWidth, iETContentHeight
//            );
//            try {
//
//            } catch (Exception e) {
//
//            }
//            etParam.setMargins((int) (fDimRatio * 20), 0, 0, 0);
//            etContent.setBackgroundResource(R.drawable.edit_register_bg);
//            etContent.setLayoutParams(etParam);
//            layout.addView(etContent);
//            listEditText.add(etContent);
//
//            //创建“+”按钮
//            ImageButton btnAdd = new ImageButton(this);
//            LinearLayout.LayoutParams btnAddParam = new LinearLayout.LayoutParams(
//                    iIBContentWidth,
//                    iIBContentWidth
//            );
//            btnAddParam.setMargins((int) (fDimRatio * 10), 0, 0, 0);
//            btnAdd.setLayoutParams(btnAddParam);
//            btnAdd.setBackgroundResource(R.drawable.ibcreatestyle);
//            btnAdd.setId(btnIDIndex);
//            btnAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    addContent(v);
//                }
//            });
//            layout.addView(btnAdd);
//            listIBTNAdd.add(index, btnAdd);
//
//            //创建“-”按钮
//            ImageButton btnDelete = new ImageButton(this);
//            LinearLayout.LayoutParams btnDeleteParam = new LinearLayout.LayoutParams(
//                    iIBContentWidth,
//                    iIBContentWidth
//            );
//            btnDeleteParam.setMargins((int) (fDimRatio * 10), 0, 0, 0);
//            btnDelete.setLayoutParams(btnDeleteParam);
//            btnDelete.setBackgroundResource(R.drawable.ibdeletestyle);
//            btnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteContent(v);
//                }
//            });
//            layout.addView(btnDelete);
//            listIBTNDel.add(index, btnDelete);
//
//            //将layout同它内部的所有控件都放在最外围的linearLayout容器里
//            linearLayout.addView(layout);
//
//            btnIDIndex++;
//        }
//    }
//
//    private void deleteContent(View v) {
//        if (v == null)
//            return;
//
//        //判断第几个“-”按钮触发了事件
//        int index = -1;
//        for (int i = 0; i < listIBTNDel.size(); i++) {
//            if (listIBTNDel.get(i) == v) {
//                index = i;
//                break;
//            }
//        }
//
//        if (index >= 0) {
//            listIBTNAdd.remove(index);
//            listIBTNDel.remove(index);
//            listTextView.remove(index - 1);
//            listEditText.remove(index);
//
//            linearLayout.removeViewAt(index);
//        }
//
//        for (int i = 0; i < listTextView.size(); i++) {
//            int num = 2 + i;
//            listTextView.get(i).setText("Day " + num);
//        }
//    }
//
//    private void findAllViews() {
//        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
//        tv_day1 = (TextView) findViewById(R.id.tv_day1);
//        et_day1 = (EditText) findViewById(R.id.et_day1);
//        btn_create = (ImageButton) findViewById(R.id.btn_create);
//        btn_save = (Button) findViewById(R.id.btn_saveRoute);
//        btn_get = (Button) findViewById(R.id.btn_getRoute);
//        now = (TextView) findViewById(R.id.textNow);
//    }
//}