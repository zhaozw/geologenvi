package com.sichuan.geologenvi.frg;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sichuan.geologenvi.R;
import com.sichuan.geologenvi.act.report.AreaInputAct;
import com.sichuan.geologenvi.bean.AreaInfo;
import com.sichuan.geologenvi.bean.AreaInfos;
import com.sichuan.geologenvi.bean.MapBean;
import com.sichuan.geologenvi.http.HttpHandler;
import com.sichuan.geologenvi.utils.DialogUtil;
import com.sichuan.geologenvi.utils.JsonUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 可爱的蘑菇 on 2016/7/30.
 */
public class CJ_GCZL_XCKP3 extends BaseFragment{

    DatePickerDialog datePickerDialog;
    TextView dataTxt1, districtTxt;
    private EditText nameEdt, idEdt;
    private HttpHandler handler;
    private Map<String, String> infoMap=new HashMap<>();
    private View.OnClickListener listener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.dateLayout1:
                    String[] dateStart=dataTxt1.getText().toString().split("-");
                    datePickerDialog=new DatePickerDialog(getActivity(), mDateSetListener, Integer.valueOf(dateStart[0]),
                            Integer.valueOf(dateStart[1])-1, Integer.valueOf(dateStart[2]));
                    datePickerDialog.show();
                    break;
                case R.id.gpwzsfhlLayout:
                    final String[] gpwzTxt={"合理","基本合理","不合理"};
                    DialogUtil.showSelectDialog(getActivity(), "工棚位置", gpwzTxt, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gpwzsfhl.setText(gpwzTxt[i]);
                        }
                    });
                    break;
                case R.id.aqbzcs_yes:
                    aqbzcs="有";
                    aqbzcs_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    aqbzcs_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.aqbzcs_no:
                    aqbzcs="无";
                    aqbzcs_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    aqbzcs_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sfdaqm_yes:
                    sfdaqm="有";
                    sfdaqm_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sfdaqm_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sfdaqm_no:
                    sfdaqm="无";
                    sfdaqm_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sfdaqm_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.zzaqy_yes:
                    zzaqy="有";
                    zzaqy_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    zzaqy_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.zzaqy_no:
                    zzaqy="无";
                    zzaqy_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    zzaqy_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sfywxdjcr_yes:
                    sfywxdjcr="有";
                    sfywxdjcr_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sfywxdjcr_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sfywxdjcr_no:
                    sfywxdjcr="无";
                    sfywxdjcr_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sfywxdjcr_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.ywjsp_yes:
                    ywjsp="有";
                    ywjsp_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    ywjsp_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.ywjsp_no:
                    ywjsp="无";
                    ywjsp_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    ywjsp_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sfyzhya_yes:
                    sfyzhya="有";
                    sfyzhya_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sfyzhya_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sfyzhya_no:
                    sfyzhya="无";
                    sfyzhya_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sfyzhya_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.jspwzsfhl_yes:
                    jspwzsfhl="是";
                    jspwzsfhl_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    jspwzsfhl_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.jspwzsfhl_no:
                    jspwzsfhl="否";
                    jspwzsfhl_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    jspwzsfhl_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.jjx_yes:
                    jjx="有";
                    jjx_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    jjx_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.jjx_no:
                    jjx="无";
                    jjx_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    jjx_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;

                case R.id.jlrydwqk_yes:
                    jlrydwqk="是";
                    jlrydwqk_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    jlrydwqk_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.jlrydwqk_no:
                    jlrydwqk="否";
                    jlrydwqk_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    jlrydwqk_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.jlrzsfjsyx_yes:
                    jlrzsfjsyx="是";
                    jlrzsfjsyx_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    jlrzsfjsyx_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.jlrzsfjsyx_no:
                    jlrzsfjsyx="否";
                    jlrzsfjsyx_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    jlrzsfjsyx_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.jljcsfjs_yes:
                    jljcsfjs="是";
                    jljcsfjs_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    jljcsfjs_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.jljcsfjs_no:
                    jljcsfjs="否";
                    jljcsfjs_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    jljcsfjs_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sjdbsfdc_yes:
                    sjdbsfdc="是";
                    sjdbsfdc_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sjdbsfdc_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sjdbsfdc_no:
                    sjdbsfdc="否";
                    sjdbsfdc_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sjdbsfdc_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sjdbjjwtsfjs_yes:
                    sjdbjjwtsfjs="是";
                    sjdbjjwtsfjs_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sjdbjjwtsfjs_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sjdbjjwtsfjs_no:
                    sjdbjjwtsfjs="否";
                    sjdbjjwtsfjs_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sjdbjjwtsfjs_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
            }
        }
    };

    private TextView gpwzsfhl;
    private EditText jcrxm, jcrsj, jcrzj;
    private ImageView aqbzcs_yes, aqbzcs_no, sfdaqm_yes, sfdaqm_no, zzaqy_yes, zzaqy_no, sfywxdjcr_yes, sfywxdjcr_no,
            sfyzhya_yes, sfyzhya_no, ywjsp_yes, ywjsp_no, jspwzsfhl_yes, jspwzsfhl_no, jjx_yes, jjx_no,
            jlrydwqk_yes, jlrydwqk_no, jlrzsfjsyx_yes, jlrzsfjsyx_no, jljcsfjs_yes, jljcsfjs_no, sjdbsfdc_yes, sjdbsfdc_no,
            sjdbjjwtsfjs_yes, sjdbjjwtsfjs_no;
    private String aqbzcs,  sfdaqm, zzaqy, sfywxdjcr,    sfyzhya, ywjsp, jspwzsfhl, jjx,   jlrydwqk, jlrzsfjsyx, jljcsfjs,    sjdbsfdc, sjdbjjwtsfjs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gczl_xckp3, container, false);


        initView(view);
        if(getActivity().getIntent().hasExtra("InfoMap")) {
            infoMap=((MapBean)(getActivity().getIntent().getSerializableExtra("InfoMap"))).getMap();
            initData();
        }
        return view;
    }

    public void getDataByJson(JSONObject jsonObj){

        JsonUtil.addJsonData(jsonObj, "aqbzcs", aqbzcs);
        JsonUtil.addJsonData(jsonObj, "sfdaqm", sfdaqm);
        JsonUtil.addJsonData(jsonObj, "zzaqy", zzaqy);
        JsonUtil.addJsonData(jsonObj, "sfywxdjcr", sfywxdjcr);

        JsonUtil.addJsonData(jsonObj, "sfyzhya", sfyzhya);
        JsonUtil.addJsonData(jsonObj, "ywjsp", ywjsp);
        JsonUtil.addJsonData(jsonObj, "jspwzsfhl", jspwzsfhl);
        JsonUtil.addJsonData(jsonObj, "jjx", jjx);

        JsonUtil.addJsonData(jsonObj, "jlrydwqk", jlrydwqk);
        JsonUtil.addJsonData(jsonObj, "jlrzsfjsyx", jlrzsfjsyx);
        JsonUtil.addJsonData(jsonObj, "jljcsfjs", jljcsfjs);
        JsonUtil.addJsonData(jsonObj, "sjdbsfdc", sjdbsfdc);
        JsonUtil.addJsonData(jsonObj, "sjdbjjwtsfjs", sjdbjjwtsfjs);

        if(gpwzsfhl!=null) {
            JsonUtil.addJsonData(jsonObj, "gpwzsfhl", gpwzsfhl.getText().toString());
            JsonUtil.addJsonData(jsonObj, "jcrxm", jcrxm.getText().toString());
            JsonUtil.addJsonData(jsonObj, "jcrsj", jcrsj.getText().toString());
            JsonUtil.addJsonData(jsonObj, "jcrzj", jcrzj.getText().toString());
        }

    }

    private void initData() {
        aqbzcs=infoMap.get("aqbzcs".toUpperCase());
        if(aqbzcs.equals("有")) {
            aqbzcs_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(aqbzcs.equals("无")) {
            aqbzcs_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        sfdaqm=infoMap.get("sfdaqm".toUpperCase());
        if(sfdaqm.equals("有")) {
            sfdaqm_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sfdaqm.equals("无")) {
            sfdaqm_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        zzaqy=infoMap.get("zzaqy".toUpperCase());
        if(zzaqy.equals("有")) {
            zzaqy_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(zzaqy.equals("无")) {
            zzaqy_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        sfywxdjcr=infoMap.get("sfywxdjcr".toUpperCase());
        if(sfywxdjcr.equals("有")) {
            sfywxdjcr_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sfywxdjcr.equals("无")) {
            sfywxdjcr_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        sfyzhya=infoMap.get("sfyzhya".toUpperCase());
        if(sfyzhya.equals("有")) {
            sfyzhya_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sfyzhya.equals("无")) {
            sfyzhya_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        ywjsp=infoMap.get("ywjsp".toUpperCase());
        if(ywjsp.equals("有")) {
            ywjsp_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(ywjsp.equals("无")) {
            ywjsp_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        jspwzsfhl=infoMap.get("jspwzsfhl".toUpperCase());
        if(jspwzsfhl.equals("是")) {
            jspwzsfhl_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(jspwzsfhl.equals("否")) {
            jspwzsfhl_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        jjx=infoMap.get("jjx".toUpperCase());
        if(jjx.equals("有")) {
            jjx_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(jjx.equals("无")) {
            jjx_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        jlrydwqk=infoMap.get("jlrydwqk".toUpperCase());
        if(jlrydwqk.equals("是")) {
            jlrydwqk_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(jlrydwqk.equals("否")) {
            jlrydwqk_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        jlrzsfjsyx=infoMap.get("jlrzsfjsyx".toUpperCase());
        if(jlrzsfjsyx.equals("是")) {
            jlrzsfjsyx_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(jlrzsfjsyx.equals("否")) {
            jlrzsfjsyx_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        jljcsfjs=infoMap.get("jljcsfjs".toUpperCase());
        if(jljcsfjs.equals("是")) {
            jljcsfjs_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(jljcsfjs.equals("否")) {
            jljcsfjs_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        sjdbsfdc=infoMap.get("sjdbsfdc".toUpperCase());
        if(sjdbsfdc.equals("是")) {
            sjdbsfdc_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sjdbsfdc.equals("否")) {
            sjdbsfdc_no.setImageResource(R.mipmap.app_login_remember_sel);
        }
        sjdbjjwtsfjs=infoMap.get("sjdbjjwtsfjs".toUpperCase());
        if(sjdbjjwtsfjs.equals("是")) {
            sjdbjjwtsfjs_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sjdbjjwtsfjs.equals("否")) {
            sjdbjjwtsfjs_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        gpwzsfhl.setText(infoMap.get("gpwzsfhl".toUpperCase()));
        jcrxm.setText(infoMap.get("jcrxm".toUpperCase()));
        jcrsj.setText(infoMap.get("jcrsj".toUpperCase()));
        jcrzj.setText(infoMap.get("jcrzj".toUpperCase()));
    }

    private void initView(View v) {
        v.findViewById(R.id.gpwzsfhlLayout).setOnClickListener(listener);

        gpwzsfhl=(TextView)v.findViewById(R.id.gpwzsfhl);
        jcrxm=(EditText) v.findViewById(R.id.jcrxm);
        jcrsj=(EditText) v.findViewById(R.id.jcrsj);
        jcrzj=(EditText) v.findViewById(R.id.jcrzj);

        aqbzcs_yes=(ImageView)v.findViewById(R.id.aqbzcs_yes);
        aqbzcs_yes.setOnClickListener(listener);
        aqbzcs_no=(ImageView)v.findViewById(R.id.aqbzcs_no);
        aqbzcs_no.setOnClickListener(listener);
        sfdaqm_yes=(ImageView)v.findViewById(R.id.sfdaqm_yes);
        sfdaqm_yes.setOnClickListener(listener);
        sfdaqm_no=(ImageView)v.findViewById(R.id.sfdaqm_no);
        sfdaqm_no.setOnClickListener(listener);
        zzaqy_yes=(ImageView)v.findViewById(R.id.zzaqy_yes);
        zzaqy_yes.setOnClickListener(listener);
        zzaqy_no=(ImageView)v.findViewById(R.id.zzaqy_no);
        zzaqy_no.setOnClickListener(listener);
        sfywxdjcr_yes=(ImageView)v.findViewById(R.id.sfywxdjcr_yes);
        sfywxdjcr_yes.setOnClickListener(listener);
        sfywxdjcr_no=(ImageView)v.findViewById(R.id.sfywxdjcr_no);
        sfywxdjcr_no.setOnClickListener(listener);

        sfyzhya_yes=(ImageView)v.findViewById(R.id.sfyzhya_yes);
        sfyzhya_yes.setOnClickListener(listener);
        sfyzhya_no=(ImageView)v.findViewById(R.id.sfyzhya_no);
        sfyzhya_no.setOnClickListener(listener);
        ywjsp_yes=(ImageView)v.findViewById(R.id.ywjsp_yes);
        ywjsp_yes.setOnClickListener(listener);
        ywjsp_no=(ImageView)v.findViewById(R.id.ywjsp_no);
        ywjsp_no.setOnClickListener(listener);
        jspwzsfhl_yes=(ImageView)v.findViewById(R.id.jspwzsfhl_yes);
        jspwzsfhl_yes.setOnClickListener(listener);
        jspwzsfhl_no=(ImageView)v.findViewById(R.id.jspwzsfhl_no);
        jspwzsfhl_no.setOnClickListener(listener);
        jjx_yes=(ImageView)v.findViewById(R.id.jjx_yes);
        jjx_yes.setOnClickListener(listener);
        jjx_no=(ImageView)v.findViewById(R.id.jjx_no);
        jjx_no.setOnClickListener(listener);

        jlrydwqk_yes=(ImageView)v.findViewById(R.id.jlrydwqk_yes);
        jlrydwqk_yes.setOnClickListener(listener);
        jlrydwqk_no=(ImageView)v.findViewById(R.id.jlrydwqk_no);
        jlrydwqk_no.setOnClickListener(listener);
        jlrzsfjsyx_yes=(ImageView)v.findViewById(R.id.jlrzsfjsyx_yes);
        jlrzsfjsyx_yes.setOnClickListener(listener);
        jlrzsfjsyx_no=(ImageView)v.findViewById(R.id.jlrzsfjsyx_no);
        jlrzsfjsyx_no.setOnClickListener(listener);
        jljcsfjs_yes=(ImageView)v.findViewById(R.id.jljcsfjs_yes);
        jljcsfjs_yes.setOnClickListener(listener);
        jljcsfjs_no=(ImageView)v.findViewById(R.id.jljcsfjs_no);
        jljcsfjs_no.setOnClickListener(listener);
        sjdbsfdc_yes=(ImageView)v.findViewById(R.id.sjdbsfdc_yes);
        sjdbsfdc_yes.setOnClickListener(listener);
        sjdbsfdc_no=(ImageView)v.findViewById(R.id.sjdbsfdc_no);
        sjdbsfdc_no.setOnClickListener(listener);
        sjdbjjwtsfjs_yes=(ImageView)v.findViewById(R.id.sjdbjjwtsfjs_yes);
        sjdbjjwtsfjs_yes.setOnClickListener(listener);
        sjdbjjwtsfjs_no=(ImageView)v.findViewById(R.id.sjdbjjwtsfjs_no);
        sjdbjjwtsfjs_no.setOnClickListener(listener);
    }

    DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            System.out.println("---> 设置后: year="+year+", month="+monthOfYear+",day="+dayOfMonth);
            dataTxt1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0x11&&resultCode==0x10){
            AreaInfos infos= (AreaInfos) data.getSerializableExtra(AreaInfos.Name);
            String selectTxt="";
            for(AreaInfo info:infos.getInfos()){
                selectTxt=selectTxt+info.getName()+"  ";
            }
            districtTxt.setText(selectTxt);
        }
    }
}
