package utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import model.CityInfo;
import model.ProvinceTotalInfo;

/**
 * 解析接口返回的数据，进行封装
 */
public class China {
    private JSONObject netDataJsonObject;
    private String lastUpdateTime;
    private JSONObject chinaTotalJsonObject;
    private int localConfirm;//本土现有确诊
    private int nowConfirm;//现有确诊
    private int confirm;//累计确诊
    private int noInfect;//无症状感染者
    private int importedCase;//境外输入
    private int dead;//累计死亡

    private JSONObject chinaAddJsonObject;
    private int addLocalConfirm;
    private int addNowConfirm;
    private int addConfirm;
    private int addNoInfect;
    private int addImportedCase;
    private int addDead;

    private JSONArray allAreasJsonArray;
    private HashMap<String, JSONObject> provinceTotalMap;


    private HashMap<String, JSONArray> eachCityInProvinceMap;
    private ArrayList<ProvinceTotalInfo> provinceTotalInfos;


    private ArrayList<String> provinceNameList;

    public ArrayList<String> getProvinceNameList() {
        return provinceNameList;
    }

    public ArrayList<ProvinceTotalInfo> getProvinceTotalInfos() {
        return provinceTotalInfos;
    }

    public HashMap<String, JSONArray> getEachCityInProvinceMap() {
        return eachCityInProvinceMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public China(String netDataString) throws JSONException {
        JSONObject JsonObject = new JSONObject(netDataString);
        String dataString = JsonObject.getString("data");
        Log.d("TAG", dataString);
        netDataJsonObject = new JSONObject(dataString.replaceAll("\\\\", ""));
        lastUpdateTime = getLastUpdateTimeFromNetDataJsonObject(netDataJsonObject);

        chinaTotalJsonObject = netDataJsonObject.getJSONObject("chinaTotal");
        localConfirm = chinaTotalJsonObject.getInt("localConfirm");
        nowConfirm = chinaTotalJsonObject.getInt("nowConfirm");
        confirm = chinaTotalJsonObject.getInt("confirm");
        noInfect = chinaTotalJsonObject.getInt("noInfect");
        importedCase = chinaTotalJsonObject.getInt("importedCase");
        dead = chinaTotalJsonObject.getInt("dead");
        chinaAddJsonObject = netDataJsonObject.getJSONObject("chinaAdd");
        addLocalConfirm = chinaAddJsonObject.getInt("localConfirm");
        addNowConfirm = chinaAddJsonObject.getInt("nowConfirm");
        addConfirm = chinaAddJsonObject.getInt("confirm");
        addNoInfect = chinaAddJsonObject.getInt("noInfect");
        addImportedCase = chinaAddJsonObject.getInt("importedCase");
        addDead = chinaAddJsonObject.getInt("dead");
        allAreasJsonArray = new JSONArray(netDataJsonObject.getJSONArray("areaTree").getJSONObject(0).getString("children"));
        provinceTotalInfos = new ArrayList<>();
        provinceTotalMap = new HashMap<>();
        eachCityInProvinceMap = new HashMap<>();
        provinceNameList = new ArrayList<>();
        for (int i = 0; i < allAreasJsonArray.length(); i++) {
            String provinceName = allAreasJsonArray.getJSONObject(i).getString("name");
            provinceNameList.add(provinceName);
            JSONObject provinceTotalJsonObject =
                    allAreasJsonArray.getJSONObject(i).getJSONObject("total");
            JSONArray eachCityInProvinceJsonArray =
                    allAreasJsonArray.getJSONObject(i).getJSONArray("children");
            provinceTotalMap.put(provinceName, provinceTotalJsonObject);


            eachCityInProvinceMap.put(provinceName, eachCityInProvinceJsonArray);
            ProvinceTotalInfo provinceTotalInfo = getProvinceTotalInfoPojo(provinceName);
            provinceTotalInfo.setName(provinceName);
            provinceTotalInfo.setCitiesCount(eachCityInProvinceJsonArray.length());


            provinceTotalInfos.add(provinceTotalInfo);
        }


    }

    public void setEachCityInProvinceMap(HashMap<String, JSONArray> eachCityInProvinceMap) {


        this.eachCityInProvinceMap = eachCityInProvinceMap;
    }

    public int getLocalConfirm() {
        return localConfirm;
    }

    public void setLocalConfirm(int localConfirm) {


        this.localConfirm = localConfirm;
    }

    public int getNowConfirm() {
        return nowConfirm;
    }

    public void setNowConfirm(int nowConfirm) {


        this.nowConfirm = nowConfirm;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {


        this.confirm = confirm;
    }

    public int getNoInfect() {
        return noInfect;
    }

    public void setNoInfect(int noInfect) {


        this.noInfect = noInfect;
    }

    public int getAddLocalConfirm() {
        return addLocalConfirm;
    }

    public void setAddLocalConfirm(int addLocalConfirm) {


        this.addLocalConfirm = addLocalConfirm;
    }

    public int getAddNowConfirm() {
        return addNowConfirm;
    }

    public void setAddNowConfirm(int addNowConfirm) {


        this.addNowConfirm = addNowConfirm;
    }

    public int getAddConfirm() {
        return addConfirm;
    }

    public void setAddConfirm(int addConfirm) {


        this.addConfirm = addConfirm;
    }

    public int getAddNoInfect() {
        return addNoInfect;
    }

    public void setAddNoInfect(int addNoInfect) {


        this.addNoInfect = addNoInfect;
    }

    public int getAddImportedCase() {
        return addImportedCase;
    }

    public void setAddImportedCase(int addImportedCase) {


        this.addImportedCase = addImportedCase;
    }

    public int getAddDead() {
        return addDead;
    }

    public void setAddDead(int addDead) {


        this.addDead = addDead;
    }

    public int getImportedCase() {
        return importedCase;
    }

    public void setImportedCase(int importedCase) {


        this.importedCase = importedCase;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {


        this.dead = dead;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }




    public JSONObject getProvinceTotalInfoByName(String provinceName) {
        return provinceTotalMap.get(provinceName);
    }

    public String getLastUpdateTimeFromNetDataJsonObject(JSONObject netDataJsonObject) throws JSONException {
        return netDataJsonObject.getString("lastUpdateTime");
    }

    public ProvinceTotalInfo getProvinceTotalInfoByNameIncludeCount(String name) {
        for (ProvinceTotalInfo p : provinceTotalInfos) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public ProvinceTotalInfo getProvinceTotalInfoPojo(String name) throws JSONException {
        ProvinceTotalInfo provinceTotalInfo = new ProvinceTotalInfo();
        JSONObject jsonObject = getProvinceTotalInfoByName(name);
        if (jsonObject == null) {
            return null;
        }
        provinceTotalInfo.setConfirm(jsonObject.getInt("confirm"));
        provinceTotalInfo.setDead(jsonObject.getInt("dead"));
        provinceTotalInfo.setHeal(jsonObject.getInt("heal"));
        provinceTotalInfo.setNowConfirm(jsonObject.getInt("nowConfirm"));

        return provinceTotalInfo;
    }


    public ArrayList<CityInfo> getCityInfoListByProvinceName(String provinceName) throws JSONException {
        ArrayList<CityInfo> cityInfoArrayList = new ArrayList<>();
        JSONArray jsonArray = eachCityInProvinceMap.get(provinceName);
        if (jsonArray == null) {
            return cityInfoArrayList;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            CityInfo cityInfo = new CityInfo();
            cityInfo.setName(jsonArray.getJSONObject(i).getString("name"));
            cityInfo.setConfirm(jsonArray.getJSONObject(i).getJSONObject("total").getInt("confirm"));
            cityInfo.setNowConfirm(jsonArray.getJSONObject(i).getJSONObject("total").getInt("nowConfirm"));
            cityInfo.setDead(jsonArray.getJSONObject(i).getJSONObject("total").getInt("dead"));
            cityInfo.setHeal(jsonArray.getJSONObject(i).getJSONObject("total").getInt("heal"));
            cityInfoArrayList.add(cityInfo);
        }
        return cityInfoArrayList;
    }

}

