package utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class NewsUtils {

    private static IdUtils idUtils;

    public static ArrayList<NewsBean> getAllNews(Context context, String cityName) throws IOException, JSONException, InterruptedException {

        idUtils = new IdUtils();
        ArrayList<NewsBean> arrayList = new ArrayList<NewsBean>();
        //连接新闻接口
        String url = "http://api.tianapi.com/travel/index?key=fb19680e94d72f6fe3875b3e47f4f556&num=5&word="
                + cityName;
        Log.d("url", url);
        SendGetThread sendGetThread = new SendGetThread(url,"");
        sendGetThread.start();
        sendGetThread.join();
        JSONObject JsonObject = new JSONObject(sendGetThread.getResult());


        //获得数据
        JSONArray introJsonArray = JsonObject.getJSONArray("newslist");
        Log.d("NEWS", introJsonArray.toString());
        if(introJsonArray.length() != 0) {
            for(int i = 0; i < introJsonArray.length(); i++) {
                NewsBean newsBean = new NewsBean();
                newsBean.title = introJsonArray.getJSONObject(i).getString("title");
                newsBean.des = introJsonArray.getJSONObject(i).getString("description");
                newsBean.news_url = introJsonArray.getJSONObject(i).getString("url");
                newsBean.icon_url = introJsonArray.getJSONObject(i).getString("picUrl");
                arrayList.add(newsBean);
            }
        }
        return arrayList;
    }
}
