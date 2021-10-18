package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mytourguide.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import utils.NewsBean;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsBean> list;

    public NewsAdapter(Context mcontext, ArrayList<NewsBean> allNews) {
        this.context = mcontext;
        this.list = allNews;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView != null) {
            view = convertView;
        }
        else {
            view = View.inflate(context, R.layout.news_row,null);
        }
        TextView item_tv_des = (TextView) view.findViewById(R.id.item_tv_des);
        TextView item_tv_title = (TextView) view.findViewById(R.id.item_tv_title);
        ImageView item_img_icon = (ImageView) view.findViewById(R.id.item_img_icon);
        NewsBean bean = list.get(position);
        if (!bean.icon_url.isEmpty()) {
            Bitmap bitmap = getHttpBitmap(bean.icon_url);
            item_img_icon.setImageBitmap(bitmap);
        }
        item_tv_des.setText(bean.des);
        item_tv_title.setText(bean.title);
        return view;
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
