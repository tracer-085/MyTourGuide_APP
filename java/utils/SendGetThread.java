package utils;
import java.io.IOException;

public class SendGetThread extends Thread implements Runnable {
    String result;
    String url;
    String key;
    String query;
    String name;

    public SendGetThread(String url, String key) throws IOException {
        this.url = url;
        this.key = key;
    }
    @Override
    public void run(){
        String str;
        str = RequestUtils.sendGet(url, key);
        result = str;
        System.out.println("这是得到的请求  " + str);
    }
    public String getResult() {
        return result;
    }
}
