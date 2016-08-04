package com.sichuan.geologenvi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 可爱的蘑菇 on 2016/3/13.
 */
public class ActUtil {

    public static void closeSoftPan(Activity act) {
        View view = act.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager)act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /** 保留两位小数点 */
    public static String twoDecimal(String money) {
        if (money != null && money.length() > 0) {
            Double decimal = Double.valueOf(money);
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String twoDecimal = fnum.format(decimal);
            return twoDecimal;
        } else
            return "暂未获取";
    }
    public static String twoDecimal(Double money) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String twoDecimal = fnum.format(money);
        return twoDecimal;
    }

    public static String addStringContent(String columnName, EditText edt, String content){
        JSONObject json= new JSONObject();
        try {
            if(content.length()>0)
                json = new JSONObject(content);
            String edtTxt=edt.getText().toString().trim();
            if(edtTxt.length()>0)
                json.put(columnName, edtTxt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static String addStringContent(String[] columnName, Object[] values){
        JSONObject json= new JSONObject();
        try {
            for (int i=0;i<columnName.length;i++) {
                Object item=values[i];
                if(item!=null)
                    json.put(columnName[i], item);
//                if(item instanceof String) {
//                    String value=String.valueOf(item) ;
//                    json.put(columnName[i], value);
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static String addStringContent(ArrayList<String> columnName, ArrayList<Object> values){
        JSONObject json= new JSONObject();
        try {
            for (int i=0;i<columnName.size();i++) {
                Object item=values.get(i);
                if(item!=null)
                    json.put(columnName.get(i), item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static void playVideo(Context mContext, String fileAbsolutePath) {
        boolean hasSetting=false;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + fileAbsolutePath);
            LogUtil.i("totp", "play uri: " + uri);
            intent.setDataAndType(uri, "video/mp4");
            hasSetting = hasPreferredApplication(mContext, intent);
            mContext.startActivity(intent);
        }catch (Exception e) {
            if(hasSetting){
                clearDefaultOpenSetting(mContext);
                playVideo(mContext, fileAbsolutePath);
            }else{
                ToastUtils.displayTextShort(mContext, "无法播放此视频");
            }
        }
    }

    public static void playVideoByUrl(Context mContext, String url) {
        boolean hasSetting=false;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "video/*");
            hasSetting = hasPreferredApplication(mContext, intent);
            mContext.startActivity(intent);
        }catch (Exception e) {
            if(hasSetting){
                clearDefaultOpenSetting(mContext);
                playVideo(mContext, url);
            }else{
                ToastUtils.displayTextShort(mContext, "无法播放此视频");
            }
        }
    }

    public static void clearDefaultOpenSetting(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        pm.clearPackagePreferredActivities(mContext.getPackageName());
    }

    public static boolean hasPreferredApplication(final Context context, final Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }

    public static String getDate() {
        Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
        long todayL=mCalendar.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(todayL);//获取当
        String dateTxt = formatter.format(curDate);
        return dateTxt;
    }

    public static String getFormatDate(String dateStr) {
        if(dateStr!=null&&dateStr.length()>0) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr2 = "";
            if (dateStr.contains("T")) {
                dateStr2 = dateStr.replace("T", " ");
                try {
                    Date date = formatter.parse(dateStr2);
                    return formatter2.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else
                return dateStr;
        }
        return "";
    }

    private String getDisasterType(String typeStr){
        if(typeStr.equals("00"))
            return "斜坡";
        else if(typeStr.equals("01"))
            return "滑坡";
        else if(typeStr.equals("02"))
            return "崩塌";
        else if(typeStr.equals("03"))
            return "泥石流";
        else if(typeStr.equals("04"))
            return "地面塌陷";
        else if(typeStr.equals("05"))
            return "地裂缝";
        else if(typeStr.equals("06"))
            return "地面沉降";
        else if(typeStr.equals("07"))
            return "其它";
        else
            return "未知";
    }

}
