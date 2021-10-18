package com.mytourguide.ui.covid;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mytourguide.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import model.CityInfo;
import model.ProvinceTotalInfo;
import utils.China;
import utils.IdUtils;
import utils.SendGetThread;

public class CovidFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static TextView lastUpdateTimeTextView;
    private China chinaEpidemic;
    private IdUtils idUtils;
    private TextView numView1;
    private TextView numView2;
    private TextView numView3;
    private TextView numView4;
    private TextView numView5;
    private TextView numView6;
    private TextView addView1;
    private TextView addView2;
    private TextView addView3;
    private TextView addView4;
    private TextView addView5;
    private TextView addView6;
    private TableLayout tableLayout;
    private ArrayList<String> provinceList;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_covid19, container, false);

        idUtils=new IdUtils();

        try {
            setComponent(root);
            setChinaEpidemic();
            iniUI(root);
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(requireContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("提示")
                    .setMessage("当前无网络\n请确认有网络连接后再试")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).create().show();
        }


        //  chinaViewModel.getChinaEpidemic().observe(getViewLifecycleOwner(), new Observer<ChinaEpidemic>() {
//            @Override
//            public void onChanged(ChinaEpidemic chinaEpidemicPara) {
//                //chinaEpidemic =chinaEpidemicPara;
//            }
//        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void iniUI(View root) throws JSONException {

        iniLastUpdateTime();
        iniNumView();
        iniAddView();
        iniTable();
        iniListener(root);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    public void iniTable() throws JSONException {
        ArrayList<ProvinceTotalInfo> arrayList = chinaEpidemic.getProvinceTotalInfos();

        for (ProvinceTotalInfo provinceTotalInfo : arrayList) {

            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(getContext());
            textView.setText(provinceTotalInfo.getName());
            textView.setTextColor(Color.argb(255, 65, 126, 241));
            textView.setTextSize(22);
            tableRow.addView(textView);

            textView = new TextView(getContext());
            textView.setGravity(Gravity.LEFT);
            textView.setText(provinceTotalInfo.getNowConfirm() + "");
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(22);
            tableRow.addView(textView);

            textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(provinceTotalInfo.getConfirm() + "");
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(22);
            tableRow.addView(textView);

            textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(provinceTotalInfo.getHeal() + "");
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(22);
            tableRow.addView(textView);

            textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(provinceTotalInfo.getDead() + "");
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(22);
            tableRow.addView(textView);
            tableRow.setId(idUtils.generateViewId());
            Log.v("zlx",tableRow.getId()+"");

            tableLayout.addView(tableRow);


            final View line = new View(getContext());
            line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 5));
            line.setBackgroundColor(Color.argb(255, 245, 245, 245));

            tableLayout.addView(line);

            ArrayList<CityInfo> cityInfoListByProvinceName = chinaEpidemic.getCityInfoListByProvinceName(provinceTotalInfo.getName());

            for (CityInfo cityInfo : cityInfoListByProvinceName) {
                TableRow tbr = new TableRow(getContext());
                tbr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                textView = new TextView(getContext());
                textView.setText(cityInfo.getName());
                textView.setTextColor(Color.argb(255, 115, 115, 160));
                textView.setTextSize(16);
                tbr.addView(textView);

                textView = new TextView(getContext());
                textView.setGravity(Gravity.LEFT);
                textView.setText(cityInfo.getNowConfirm() + "");
                textView.setTextColor(Color.argb(255, 115, 115, 160));
                textView.setTextSize(16);
                tbr.addView(textView);

                textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setText(cityInfo.getConfirm() + "");
                textView.setTextColor(Color.argb(255, 115, 115, 160));
                textView.setTextSize(16);
                tbr.addView(textView);

                textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setText(cityInfo.getHeal() + "");
                textView.setTextColor(Color.argb(255, 115, 115, 160));
                textView.setTextSize(16);
                tbr.addView(textView);

                textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setText(cityInfo.getDead() + "");

                textView.setTextColor(Color.argb(255, 115, 115, 160));
                textView.setTextSize(16);
                tbr.addView(textView);

                tableLayout.addView(tbr);
                tbr.setVisibility(View.GONE);
                tbr.setId(idUtils.generateViewId());
                Log.v("zlx",tbr.getId()+"");


            }
        }
    }

    public void iniListener(View root) {

        ArrayList<ProvinceTotalInfo> arrayList = chinaEpidemic.getProvinceTotalInfos();
        int id = 1;
        for (ProvinceTotalInfo provinceTotalInfo : arrayList) {
            TableRow t = root.findViewById(id);
            int finalId = id;
            t.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    if (provinceTotalInfo.isShowCities()) {
                        for (int j = 1; j <= provinceTotalInfo.getCitiesCount(); j++) {
                            TableRow tableRow1 = root.findViewById(finalId + j);
                            tableRow1.setVisibility(View.GONE);
                            provinceTotalInfo.setShowCities(false);
                        }
                    } else {
                        for (int j = 1; j <= provinceTotalInfo.getCitiesCount(); j++) {
                            TableRow tableRow1 = root.findViewById(finalId + j);
                            tableRow1.setVisibility(View.VISIBLE);
                            provinceTotalInfo.setShowCities(true);
                        }
                    }
                }
            });
            id = id + provinceTotalInfo.getCitiesCount() + 1;
        }
    }

    @SuppressLint("SetTextI18n")
    public void iniNumView() {
        numView1.setText(chinaEpidemic.getLocalConfirm() + "");
        numView2.setText(chinaEpidemic.getNowConfirm() + "");
        numView3.setText(chinaEpidemic.getConfirm() + "");
        numView4.setText(chinaEpidemic.getNoInfect() + "");
        numView5.setText(chinaEpidemic.getImportedCase() + "");
        numView6.setText(chinaEpidemic.getDead() + "");
    }

    @SuppressLint("SetTextI18n")
    public void iniAddView() {
        addView1.setText("较上日" + ((chinaEpidemic.getAddLocalConfirm()) >= 0 ? ("+" + chinaEpidemic.getAddLocalConfirm()) : chinaEpidemic.getAddLocalConfirm()));
        addView2.setText("较上日" + ((chinaEpidemic.getAddNowConfirm()) >= 0 ? ("+" + chinaEpidemic.getAddNowConfirm()) : chinaEpidemic.getAddNowConfirm()));
        addView3.setText("较上日" + ((chinaEpidemic.getAddConfirm()) >= 0 ? ("+" + chinaEpidemic.getAddConfirm()) : chinaEpidemic.getAddConfirm()));
        addView4.setText("较上日" + ((chinaEpidemic.getAddNoInfect()) >= 0 ? ("+" + chinaEpidemic.getAddNoInfect()) : chinaEpidemic.getAddNoInfect()));
        addView5.setText("较上日" + ((chinaEpidemic.getAddImportedCase()) >= 0 ? ("+" + chinaEpidemic.getAddImportedCase()) : chinaEpidemic.getAddImportedCase()));
        addView6.setText("较上日" + ((chinaEpidemic.getAddDead()) >= 0 ? ("+" + chinaEpidemic.getAddDead()) : chinaEpidemic.getAddDead()));
    }

    public void setComponent(View root) {
        lastUpdateTimeTextView = root.findViewById(R.id.lastUpdateTimeText);
        numView1 = root.findViewById(R.id.numberView1);
        numView2 = root.findViewById(R.id.numberView2);
        numView3 = root.findViewById(R.id.numberView3);
        numView4 = root.findViewById(R.id.numberView4);
        numView5 = root.findViewById(R.id.numberView5);
        numView6 = root.findViewById(R.id.numberView6);
        addView1 = root.findViewById(R.id.compareView1);
        addView2 = root.findViewById(R.id.compareView2);
        addView3 = root.findViewById(R.id.compareView3);
        addView4 = root.findViewById(R.id.compareView4);
        addView5 = root.findViewById(R.id.compareView5);
        addView6 = root.findViewById(R.id.compareView6);
        tableLayout = root.findViewById(R.id.tableView);
    }

    @SuppressLint("SetTextI18n")
    public void iniLastUpdateTime() {
        lastUpdateTimeTextView.setText("统计截至" + chinaEpidemic.getLastUpdateTime());
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setChinaEpidemic() throws IOException, InterruptedException, JSONException {
        String url = "https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";
        SendGetThread sendGetThread = new SendGetThread(url,"");
        sendGetThread.start();
        sendGetThread.join();
        chinaEpidemic = new China(sendGetThread.getResult());
    }
}