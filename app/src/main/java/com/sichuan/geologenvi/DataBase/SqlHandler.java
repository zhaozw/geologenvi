package com.sichuan.geologenvi.DataBase;

import android.app.Activity;
import android.database.Cursor;

import com.sichuan.geologenvi.bean.AreaInfo;
import com.sichuan.geologenvi.bean.Contact;
import com.sichuan.geologenvi.bean.PopupInfoItem;
import com.sichuan.geologenvi.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/24.
 */
public class SqlHandler {

    private DBManager dbManager;
    private Activity act;

    public SqlHandler(final Activity con){
        act=con;
        dbManager=new DBManager(act);
    }

    public ArrayList<Contact> getPersonInfo(String sqlStr){
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});

        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
//        Cursor c = dbManager.querySQL("SELECT * FROM SL_JCBA05A left join SL_TATTR_DZZH_XZQH on " +
//                "(SL_JCBA05A.JCBA05A040=SL_TATTR_DZZH_XZQH.CODE or SL_JCBA05A.JCBA05A030=SL_TATTR_DZZH_XZQH.CODE)"+str, new String[]{});
        ArrayList<Contact> contacts=new ArrayList<>();
        if(c!=null) {
            boolean hasAddr=false;
            boolean hasOtherInfo=false;
            String[] columnNames=c.getColumnNames();
            for (int i=0;i<columnNames.length;i++){
                String name=columnNames[i];
                if(name.equals("otherinfo"))
                    hasOtherInfo=true;
                if(name.equals("addr"))
                    hasAddr=true;
            }
            while (c.moveToNext()) {
                Contact contact=new Contact();
                String phone = c.getString(c.getColumnIndex("phone"));
                String name = c.getString(c.getColumnIndex("name"));
                String otherinfo="";
                if(hasOtherInfo)
                    otherinfo = c.getString(c.getColumnIndex("otherinfo"));
                String addr="";
                if(hasAddr)
                    addr = c.getString(c.getColumnIndex("addr"));
                if(phone==null) {
                    contact.setName(name);
                    contact.setPhone("");
                }else if(name==null){
                    contact.setName(phone);
                    contact.setPhone(phone);
                }else{
                    contact.setName(name);
                    contact.setPhone(phone);
                }
                contact.setAddress(addr);
                contact.setPosition(otherinfo);
                contacts.add(contact);
            }
            c.close();
        }
        return contacts;
    }

    public ArrayList<Contact> getPersonInfo2(String sqlStr){
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});

        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        ArrayList<Contact> contacts=new ArrayList<>();
        if(c!=null) {
            while (c.moveToNext()) {
                Contact contact=new Contact();
                String QX = c.getString(c.getColumnIndex("QX"));
                contact.setName(QX);
                Map<String, String> infoMaps=new HashMap<>();
                String FZRNAME = c.getString(c.getColumnIndex("FZRNAME"));
                infoMaps.put("FZRNAME", FZRNAME);
                String FZRPHONE = c.getString(c.getColumnIndex("FZRPHONE"));
                infoMaps.put("FZRPHONE", FZRPHONE);

                String CYRNAME1 = c.getString(c.getColumnIndex("CYNAME1"));
                infoMaps.put("CYNAME1", CYRNAME1);
                String CYPHONE1 = c.getString(c.getColumnIndex("CYPHONE1"));
                infoMaps.put("CYPHONE1", CYPHONE1);

                String CYRNAME2 = c.getString(c.getColumnIndex("CYNAME2"));
                infoMaps.put("CYNAME2", CYRNAME2);
                String CYPHONE2 = c.getString(c.getColumnIndex("CYPHONE2"));
                infoMaps.put("CYPHONE2", CYPHONE2);
                contact.setMaps(infoMaps);
                contacts.add(contact);
            }
            c.close();
        }
        return contacts;
    }

    public ArrayList<Contact> getPersonInfo3(String sqlStr){
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});

        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        ArrayList<Contact> contacts=new ArrayList<>();
        if(c!=null) {
            while (c.moveToNext()) {
                Contact contact=new Contact();
                String QX = c.getString(c.getColumnIndex("QX"));
                contact.setName(QX);
                Map<String, String> infoMaps=new HashMap<>();
                String DHZZZNAME = c.getString(c.getColumnIndex("DHZZZNAME"));
                infoMaps.put("DHZZZNAME", DHZZZNAME);


                infoMaps.put("DHZZZPHONE", c.getString(c.getColumnIndex("DHZZZPHONE")));
                infoMaps.put("DHZKZPHONE", c.getString(c.getColumnIndex("DHZKZPHONE")));
                infoMaps.put("DHZZBSPHONE", c.getString(c.getColumnIndex("DHZZBSPHONE")));
                infoMaps.put("CHUANZHEN", c.getString(c.getColumnIndex("CHUANZHEN")));

                contact.setMaps(infoMaps);
                contacts.add(contact);
            }
            c.close();
        }
        return contacts;
    }

    public ArrayList<AreaInfo> getAreaInfo(String parent){
        Cursor c;
        if(parent.length()==0)
            c = dbManager.querySQL("select * from SL_TATTR_DZZH_XZQH where length(code) = 6", new String[]{});
        else
            c = dbManager.querySQL("select * from SL_TATTR_DZZH_XZQH where parentcodeid = ?", new String[]{parent});
        ArrayList<AreaInfo> contacts=new ArrayList<>();
        if(c!=null) {
            while (c.moveToNext()) {
                AreaInfo contact=new AreaInfo();
                String NAME = c.getString(c.getColumnIndex("NAME"));
                String ID = c.getString(c.getColumnIndex("ID"));
                String CODE = c.getString(c.getColumnIndex("CODE"));
                if(NAME!=null) {
                    contact.setName(NAME);
                }
                if(ID!=null){
                    contact.setId(ID);
                }
                if(CODE!=null){
                    contact.setCode(CODE);
                }
                contacts.add(contact);
            }
            c.close();
        }
        return contacts;
    }

    public ArrayList<Map<String, String>> getGeohazardInfo(String queryStr, int type, String name, String disasterName, String disasterTypeCode,String disasterSizeCode,
                                                           String areaCode, String avoidCode, String yearCode){
        String typeStr="";
        String formName="";
        switch (type){
            case 1:
                typeStr=" where ZHAA01A810 = 2";
                formName="SL_ZHAA01A";
                break;
            case 4:
                typeStr=" where ZHDD02A020 is not null";
                formName="SL_ZHDD02A";
                break;
            case 5:
                typeStr=" where ZHDD02A020 is not null";
                formName="SL_ZHDD02A as a left join SL_ZHAA01A as c on a.ZHDD02A300=c.ZHAA01A010";
                break;
            case 2:
                typeStr=" where METERTYPE is not null";
                formName="SL_STATIONMETERS";
                break;
            case 3:
                typeStr=" where ZHCA01A020 is not null";
                formName="SL_ZHCA01A";
                break;
            case 6:
                typeStr=" where ZHDD04B020 is not null";
                formName="SL_ZHDD04B";
                break;
            case 7://国土负责点位
//                typeStr=" where ZHAA01A382 = 0 and ZHAA01A875 = 0";
                typeStr= " where (ZHAA01A382=0 or ZHAA01A382 is null) and (ZHAA01A875 = 0 or ZHAA01A875 is null) ";
                formName="SL_ZHAA01A";
                break;
            case 8://其他行业点位
//                typeStr=" where ZHAA01A382 = 1  and ZHAA01A875 = 0";
                typeStr= " where ZHAA01A382 = 1 and (ZHAA01A875 = 0 or ZHAA01A875 is null) ";
                formName="SL_ZHAA01A";
                break;
            case 9://销号点位
                typeStr=" where ZHAA01A875 = 1";
                formName="SL_ZHAA01A";
                break;
            case 10://全部点位
//                typeStr=" where ZHAA01A875 = 1";
                typeStr=" where ZHAA01A875 = 0 or ZHAA01A875 is null";
                formName="SL_ZHAA01A";
                break;
            case 20://数据采集（地质灾害防治工作检查）
                typeStr=" where ZHAA01A382 = 0 and ZHAA01A875 = 0";
                formName="SL_ZHAA01A";
                break;
            case 21://数据采集（重大地质灾害防治工程项目现场检查）
                typeStr=" where ZHCA01A020 is not null";
                formName="SL_ZHCA01A";
                break;
            case 22://数据采集（应急避险场所检查）
                typeStr=" where ZHDD02A020 is not null";
                formName="SL_ZHDD02A as a left join SL_ZHAA01A as c on a.ZHDD02A300=c.ZHAA01A010";
                break;
            case 30:
                typeStr="";
                formName="SL_TXL_SHENG";
                break;
            case 31:
                typeStr="";
                formName="SL_TXL_SHI";
                break;
            case 32:
                typeStr="";
                formName="SL_TXL_SHIGUOTUJU";
                break;
            case 33:
                typeStr="";
                formName="SL_TXL_SHIDHZ";
                break;
            case 34:
                typeStr="";
                formName="SL_TXL_SHILD";
                break;
        }
        if(type==1||type==7||type==8||type==9||type==20) {
            if (name.length() > 0)
                typeStr = typeStr + " and ZHAA01A020 like '%" + name + "%'";
            if (disasterTypeCode.length() > 0)
                typeStr = typeStr + " and ZHAA01A210 = '" + disasterTypeCode + "'";
            if (disasterSizeCode.length() > 0)
                typeStr = typeStr + " and ZHAA01A370 = '" + disasterSizeCode + "'";
            if (areaCode.length() == 6)
                typeStr = typeStr + " and ZHAA01A110 = '" + areaCode + "'";
            else if (areaCode.length() == 9)
                typeStr = typeStr + " and ZHAA01A120 = '" + areaCode + "'";
        }else if(type==3||type==21){
            if (areaCode.length() == 6)
                typeStr = typeStr + " and ZHCA01A040 = '" + areaCode + "'";
            else if (areaCode.length() == 9)
                typeStr = typeStr + " and ZHCA01A050 = '" + areaCode + "'";
        }else if(type==4||type==5||type==22){
            if (areaCode.length() == 6)
                typeStr = typeStr + " and ZHDD02A040 = '" + areaCode + "'";
            else if (areaCode.length() == 9)
                typeStr = typeStr + " and ZHDD02A050 = '" + areaCode + "'";
            if(avoidCode.length()>0){
                typeStr = typeStr + " and ZHDD02A150 = '" + avoidCode + "'";
            }
            if(disasterName.length()>0){
                typeStr = typeStr + " and ZHDD02A310 like '%" + disasterName + "%'";
            }
            if(name.length()>0){
                typeStr = typeStr + " and ZHDD02A020 like '%" + name + "%'";
            }
            if(type==5)
                typeStr = typeStr + " and c.ZHAA01A810 = 2";
        }else if(type==2){
            if (areaCode.length()>0)
                typeStr = typeStr + " and CITY = '" + areaCode + "'";
            if (disasterTypeCode.length()>0)
                typeStr = typeStr + " and METERTYPE = '" + disasterTypeCode + "'";
        }else if(type==6){
            if (name.length() > 0)
                typeStr = typeStr + " and ZHDD04B020 like '%" + name + "%'";
            if (yearCode.length() > 0)
                typeStr = typeStr + " and ZHDD04B013 = '" + yearCode + "'";
            if (areaCode.length() == 6)
                typeStr = typeStr + " and ZHDD04B040 = '" + areaCode + "'";
            else if (areaCode.length() == 9)
                typeStr = typeStr + " and ZHDD04B060 = '" + areaCode + "'";
        }

        ArrayList<Map<String, String>> mapsInfo;
        if(queryStr!=null&&queryStr.length()>0)
            mapsInfo = getQueryResult(queryStr, formName, typeStr);
        else
            mapsInfo = getQueryResult(formName, typeStr);
        return mapsInfo;
    }

    public ArrayList<Map<String, String>> getQueryResult(String tableName, String typeStr){

        ArrayList<Map<String, String>> datas=new ArrayList<>();
        String sqlStr="select * from "+tableName+typeStr;
        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});
        if(c!=null) {
            LogUtil.i("SQL", "result num---->:  "+c.getCount());
            String columnNames[]=c.getColumnNames();
            while (c.moveToNext()) {
                LinkedHashMap<String, String> maps=new LinkedHashMap<>();
                for (int i=0;i<columnNames.length;i++) {
                    String key = columnNames[i];
                    String value = c.getString(c.getColumnIndex(key));
                    maps.put(key, value);

                }
                datas.add(maps);
            }
            c.close();

        }
        return datas;
    }

    public ArrayList<Map<String, String>> getQueryResult(String[] columnNames,String tableName, String typeStr){

        ArrayList<Map<String, String>> datas=new ArrayList<>();
        String sqlStr="select * from "+tableName+typeStr;
        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});
        if(c!=null) {
            LogUtil.i("SQL", "result num---->:  "+c.getCount());
            while (c.moveToNext()) {

                LinkedHashMap<String, String> maps=new LinkedHashMap<>();
                for (int i=0;i<columnNames.length;i++) {
                    String key = columnNames[i];
                    String value = c.getString(c.getColumnIndex(key));
                    maps.put(key, value);
                }
                datas.add(maps);

            }
            c.close();
        }

        return datas;
    }

    public ArrayList<Map<String, String>> getQueryResult(String queryStry, String tableName, String typeStr){

        ArrayList<Map<String, String>> datas=new ArrayList<>();
        String sqlStr="select "+queryStry+" from "+tableName+typeStr;
        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});
        if(c!=null) {
            LogUtil.i("SQL", "result num---->:  "+c.getCount());
            String columnNames[]=c.getColumnNames();
            while (c.moveToNext()) {
                LinkedHashMap<String, String> maps=new LinkedHashMap<>();
                for (int i=0;i<columnNames.length;i++) {
                    String key = columnNames[i];
                    String value = c.getString(c.getColumnIndex(key));
                    maps.put(key, value);
                }
                datas.add(maps);
            }
            c.close();
        }
        return datas;
    }

    public ArrayList<Map<String, String>> getQueryResult2(String queryStry, String tableName, String typeStr){

        ArrayList<Map<String, String>> datas=new ArrayList<>();
        String sqlStr="select "+queryStry+" from "+tableName+typeStr;
        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});
        if(c!=null) {
            LogUtil.i("SQL", "result num---->:  "+c.getCount());
            String columnNames[]=c.getColumnNames();
            while (c.moveToNext()) {
                LinkedHashMap<String, String> maps=new LinkedHashMap<>();
                for (int i=0;i<columnNames.length;i++) {
                    String key = columnNames[i];
                    String value = c.getString(c.getColumnIndex(key));
                    maps.put(key, value);
                }
                datas.add(maps);
            }
            LinkedHashMap<String, String> maps=new LinkedHashMap<>();
            for (int i=0;i<columnNames.length;i++) {
                String key = columnNames[i];
                String value;
                Double v=0d;
                if(i==0)
                    value="总计";
                else {
                    for (int j = 0; j < datas.size(); j++) {
                        Map<String, String> m=datas.get(j);
                        v+=Double.valueOf(m.get(key));
                    }
                    long lv=v.longValue();
                    if(lv==v)
                        value=String.valueOf(lv);
                    else
                        value=String.valueOf(v);
                }
                maps.put(key,value);
            }
            datas.add(maps);
            c.close();
        }
        return datas;
    }

    public String[] getTypesQuery(String tableName, String typeString){

        String[] datas=null;
        String sqlStr="SELECT DISTINCT "+typeString+" FROM "+tableName;
        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});
        if(c!=null) {
            LogUtil.i("SQL", "result num---->:  "+c.getCount());
            datas=new String[c.getCount()+1];
            int i=0;
            while (c.moveToNext()) {
                String value = c.getString(0);
                datas[i]=value;
                i++;
            }
            datas[datas.length-1]="取消";
            c.close();
        }
        return datas;
    }

    public LinkedList<PopupInfoItem> getTypesQueryWithCode(String tableName, String typeString, String codeName, String where){

        LinkedList<PopupInfoItem> datas=new LinkedList<>();
        String sqlStr="SELECT DISTINCT "+typeString+","+codeName+" FROM "+tableName;
        if(where!=null&&where.length()>0)
            sqlStr=sqlStr+" WHERE "+where;
        LogUtil.i("SQL", "reques sql---->:  "+sqlStr);
        Cursor c = dbManager.querySQL(sqlStr, new String[]{});
        if(c!=null) {
            LogUtil.i("SQL", "result num---->:  "+c.getCount());
            while (c.moveToNext()) {
                datas.add(new PopupInfoItem(c.getString(c.getColumnIndex(codeName)), c.getString(c.getColumnIndex(typeString))));
            }
            c.close();
        }
        return datas;
    }


    public String getDistrictName(String code) {
        if(code!=null&&code.length()>0) {
            Cursor c = dbManager.querySQL("select * from SL_TATTR_DZZH_XZQH where CODE = " + code, new String[]{});
            if (c != null) {
                while (c.moveToNext()) {
                    return c.getString(c.getColumnIndex("NAME"));
                }
            }
        }
        return code;
    }
}
