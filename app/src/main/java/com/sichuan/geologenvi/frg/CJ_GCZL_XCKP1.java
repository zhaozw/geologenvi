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

import com.sichuan.geologenvi.DataBase.SqlHandler;
import com.sichuan.geologenvi.R;
import com.sichuan.geologenvi.act.geodisaster.SelectorAct;
import com.sichuan.geologenvi.act.geodisaster.TitleResultListAct;
import com.sichuan.geologenvi.act.report.AreaInputAct;
import com.sichuan.geologenvi.bean.AreaInfo;
import com.sichuan.geologenvi.bean.AreaInfos;
import com.sichuan.geologenvi.bean.MapBean;
import com.sichuan.geologenvi.http.HttpHandler;
import com.sichuan.geologenvi.utils.ActUtil;
import com.sichuan.geologenvi.utils.DialogUtil;
import com.sichuan.geologenvi.utils.JsonUtil;
import com.sichuan.geologenvi.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 可爱的蘑菇 on 2016/7/30.
 */
public class CJ_GCZL_XCKP1 extends BaseFragment{

    DatePickerDialog datePickerDialog;
    private EditText nameEdt, idEdt;
    private View arrowRight;
    private TextView xgrydgcsxcd, kgrq, jhwgrq, tbrq, projectName, sgdw, jldw, sjdw, dlwz,
            lon1,lon2,lon3,lat1,lat2,lat3;
    private ImageView sfczwffbzb_yes, sfczwffbzb_no,dwzz_sgdw_yes, dwzz_sgdw_no, dwzz_jldw_yes, dwzz_jldw_no, xmjlsfyxyjszc_yes, xmjlsfyxyjszc_no, jlryzgzs_yes, jlryzgzs_no,
            tsgzry_yes, tsgzry_no, sfszwpyt_yes, sfszwpyt_no, zjysfdw_yes, zjysfdw_no, sfyzlkzwj_yes, sfyzlkzwj_no;
    private EditText gcjzqk, qt, jcfzr, tbr, hbz, zbx, zby;
    private String guid="", lon="", lat="", sfczwffbzb="",dwzz_sgdw="",dwzz_jldw="", xmjlsfyxyjszc="",jlryzgzs="",tsgzry="",
            sfszwpyt="", zjysfdw="", sfyzlkzwj="";
    private int dialogtype=-1;
    private Map<String, String> infoMap=new HashMap<>();
    private SqlHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gczl_xckp1, container, false);


        initView(view);
        handler=new SqlHandler(getActivity());
        if(getActivity().getIntent().hasExtra("InfoMap")) {
            infoMap=((MapBean)(getActivity().getIntent().getSerializableExtra("InfoMap"))).getMap();
            arrowRight.setVisibility(View.INVISIBLE);
            initData();
        }else{
            if(getActivity().getIntent().hasExtra("Map")){
                MapBean mapBean = (MapBean) getActivity().getIntent().getSerializableExtra("Map");
                guid = mapBean.getMap().get("ZHCA01A010");
                projectName.setText(mapBean.getMap().get("ZHCA01A020"));
                sgdw.setText(mapBean.getMap().get("ZHCA01A230"));
                jldw.setText(mapBean.getMap().get("ZHCA01A210"));
                sjdw.setText(mapBean.getMap().get("ZHCA01A200"));

                gczl_quxian=mapBean.getMap().get("ZHCA01A040");
                if(gczl_quxian!=null)
                    gczl_quxian=handler.getDistrictName(gczl_quxian);
                gczl_xiangzhen=mapBean.getMap().get("ZHCA01A050");
                if(gczl_xiangzhen!=null)
                    gczl_xiangzhen=handler.getDistrictName(gczl_xiangzhen);
                gczl_cun=mapBean.getMap().get("ZHCA01A060");
                gczl_zu=mapBean.getMap().get("ZHCA01A070");
                dlwz.setText(gczl_quxian+"  "+gczl_xiangzhen+"  "+gczl_cun+"  "+gczl_zu);
//            hb.setText(mapBean.getMap().get("ZHCA01A210"));

                String lon = mapBean.getMap().get("ZHCA01A075");
                this.lon = lon;
                String lat = mapBean.getMap().get("ZHCA01A076");
                this.lat = lat;
                setLoaction(lon, lat);
            }

            view.findViewById(R.id.projectNameLayout).setOnClickListener(listener);
        }
        return view;
    }

    private void initData() {
        guid=infoMap.get("gczl_guid".toUpperCase());
        gcjzqk.setText(infoMap.get("gcjzqk".toUpperCase()));
        qt.setText(infoMap.get("qt".toUpperCase()));
        jcfzr.setText(infoMap.get("jcfzr".toUpperCase()));
        tbr.setText(infoMap.get("tbr".toUpperCase()));
        hbz.setText(infoMap.get("gczl_z".toUpperCase()));
        zbx.setText(infoMap.get("gczl_x".toUpperCase()));
        zby.setText(infoMap.get("gczl_y".toUpperCase()));

        xgrydgcsxcd.setText(infoMap.get("xgrydgcsxcd".toUpperCase()));
        kgrq.setText(ActUtil.getFormatDate(infoMap.get("kgrq".toUpperCase())));
        jhwgrq.setText(ActUtil.getFormatDate(infoMap.get("jhwgrq".toUpperCase())));
        tbrq.setText(ActUtil.getFormatDate(infoMap.get("tbrq".toUpperCase())));
        projectName.setText(infoMap.get("gczl_name".toUpperCase()));
        sgdw.setText(infoMap.get("gczl_sgdw".toUpperCase()));
        jldw.setText(infoMap.get("gczl_jldw".toUpperCase()));
        sjdw.setText(infoMap.get("gczl_sjdw".toUpperCase()));
        dlwz.setText(infoMap.get("gczl_quxian".toUpperCase())+"  "+infoMap.get("gczl_xiangzhen".toUpperCase())
                +"  "+infoMap.get("gczl_cun".toUpperCase())+"  "+infoMap.get("gczl_zu".toUpperCase()));

        setLoaction(infoMap.get("gczl_lon".toUpperCase()), infoMap.get("gczl_lat".toUpperCase()));

        sfczwffbzb=infoMap.get("sfczwffbzb".toUpperCase());
        if(sfczwffbzb.equals("是")) {
            sfczwffbzb_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sfczwffbzb.equals("否")) {
            sfczwffbzb_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        dwzz_sgdw=infoMap.get("dwzz_sgdw".toUpperCase());
        if(dwzz_sgdw.equals("有")) {
            dwzz_sgdw_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(dwzz_sgdw.equals("无")) {
            dwzz_sgdw_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        dwzz_jldw=infoMap.get("dwzz_jldw".toUpperCase());
        if(dwzz_jldw.equals("有")) {
            dwzz_jldw_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(dwzz_jldw.equals("无")) {
            dwzz_jldw_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        xmjlsfyxyjszc=infoMap.get("xmjlsfyxyjszc".toUpperCase());
        if(xmjlsfyxyjszc.equals("有")) {
            xmjlsfyxyjszc_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(xmjlsfyxyjszc.equals("无")) {
            xmjlsfyxyjszc_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        jlryzgzs=infoMap.get("jlryzgzs".toUpperCase());
        if(jlryzgzs.equals("有")) {
            jlryzgzs_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(jlryzgzs.equals("无")) {
            jlryzgzs_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        sfszwpyt=infoMap.get("sfszwpyt".toUpperCase());
        if(sfszwpyt.equals("是")) {
            sfszwpyt_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sfszwpyt.equals("否")) {
            sfszwpyt_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        zjysfdw=infoMap.get("zjysfdw".toUpperCase());
        if(zjysfdw.equals("是")) {
            zjysfdw_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(zjysfdw.equals("否")) {
            zjysfdw_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        tsgzry=infoMap.get("tsgzry".toUpperCase());
        if(tsgzry.equals("有")) {
            tsgzry_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(tsgzry.equals("无")) {
            tsgzry_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

        sfyzlkzwj=infoMap.get("sfyzlkzwj".toUpperCase());
        if(sfyzlkzwj.equals("是")) {
            sfyzlkzwj_yes.setImageResource(R.mipmap.app_login_remember_sel);
        }else if(sfyzlkzwj.equals("否")) {
            sfyzlkzwj_no.setImageResource(R.mipmap.app_login_remember_sel);
        }

    }

    public void getDataByJson(JSONObject jsonObj){

        JsonUtil.addJsonData(jsonObj, "gczl_guid", guid);
        JsonUtil.addJsonData(jsonObj, "gcjzqk", gcjzqk.getText().toString());
        JsonUtil.addJsonData(jsonObj, "qt", qt.getText().toString());
        JsonUtil.addJsonData(jsonObj, "jcfzr", jcfzr.getText().toString());
        JsonUtil.addJsonData(jsonObj, "tbr", tbr.getText().toString());

        JsonUtil.addJsonData(jsonObj, "xgrydgcsxcd", xgrydgcsxcd.getText().toString());
        JsonUtil.addJsonData(jsonObj, "kgrq", kgrq.getText().toString());
        JsonUtil.addJsonData(jsonObj, "jhwgrq", jhwgrq.getText().toString());
        JsonUtil.addJsonData(jsonObj, "tbrq", tbrq.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_name", projectName.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_sjdw", sjdw.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_jldw", jldw.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_sgdw", sgdw.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_lon", lon);
        JsonUtil.addJsonData(jsonObj, "gczl_lat", lat);
        JsonUtil.addJsonData(jsonObj, "gczl_quxian", gczl_quxian);
        JsonUtil.addJsonData(jsonObj, "gczl_xiangzhen", gczl_xiangzhen);
        JsonUtil.addJsonData(jsonObj, "gczl_cun", gczl_cun);
        JsonUtil.addJsonData(jsonObj, "gczl_zu", gczl_zu);
        JsonUtil.addJsonData(jsonObj, "gczl_x", zbx.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_y", zby.getText().toString());
        JsonUtil.addJsonData(jsonObj, "gczl_z", hbz.getText().toString());

        JsonUtil.addJsonData(jsonObj, "sfczwffbzb", sfczwffbzb);
        JsonUtil.addJsonData(jsonObj, "dwzz_sgdw", dwzz_sgdw);
        JsonUtil.addJsonData(jsonObj, "dwzz_jldw", dwzz_jldw);
        JsonUtil.addJsonData(jsonObj, "xmjlsfyxyjszc", xmjlsfyxyjszc);
        JsonUtil.addJsonData(jsonObj, "jlryzgzs", jlryzgzs);
        JsonUtil.addJsonData(jsonObj, "sfszwpyt", sfszwpyt);
        JsonUtil.addJsonData(jsonObj, "zjysfdw", zjysfdw);
        JsonUtil.addJsonData(jsonObj, "sfyzlkzwj", sfyzlkzwj);
        JsonUtil.addJsonData(jsonObj, "tsgzry", tsgzry);

    }

    private void initView(View v) {
        arrowRight=v.findViewById(R.id.arrowRight);

        sfczwffbzb_yes= (ImageView) v.findViewById(R.id.sfczwffbzb_yes);
        sfczwffbzb_yes.setOnClickListener(listener);
        sfczwffbzb_no= (ImageView) v.findViewById(R.id.sfczwffbzb_no);
        sfczwffbzb_no.setOnClickListener(listener);

        dwzz_sgdw_yes= (ImageView) v.findViewById(R.id.dwzz_sgdw_yes);
        dwzz_sgdw_yes.setOnClickListener(listener);
        dwzz_sgdw_no= (ImageView) v.findViewById(R.id.dwzz_sgdw_no);
        dwzz_sgdw_no.setOnClickListener(listener);

        dwzz_jldw_yes= (ImageView) v.findViewById(R.id.dwzz_jldw_yes);
        dwzz_jldw_yes.setOnClickListener(listener);
        dwzz_jldw_no= (ImageView) v.findViewById(R.id.dwzz_jldw_no);
        dwzz_jldw_no.setOnClickListener(listener);

        xmjlsfyxyjszc_yes= (ImageView) v.findViewById(R.id.xmjlsfyxyjszc_yes);
        xmjlsfyxyjszc_yes.setOnClickListener(listener);
        xmjlsfyxyjszc_no= (ImageView) v.findViewById(R.id.xmjlsfyxyjszc_no);
        xmjlsfyxyjszc_no.setOnClickListener(listener);

        jlryzgzs_yes= (ImageView) v.findViewById(R.id.jlryzgzs_yes);
        jlryzgzs_yes.setOnClickListener(listener);
        jlryzgzs_no= (ImageView) v.findViewById(R.id.jlryzgzs_no);
        jlryzgzs_no.setOnClickListener(listener);

        tsgzry_yes= (ImageView) v.findViewById(R.id.tsgzry_yes);
        tsgzry_yes.setOnClickListener(listener);
        tsgzry_no= (ImageView) v.findViewById(R.id.tsgzry_no);
        tsgzry_no.setOnClickListener(listener);

        sfszwpyt_yes= (ImageView) v.findViewById(R.id.sfszwpyt_yes);
        sfszwpyt_yes.setOnClickListener(listener);
        sfszwpyt_no= (ImageView) v.findViewById(R.id.sfszwpyt_no);
        sfszwpyt_no.setOnClickListener(listener);

        zjysfdw_yes= (ImageView) v.findViewById(R.id.zjysfdw_yes);
        zjysfdw_yes.setOnClickListener(listener);
        zjysfdw_no= (ImageView) v.findViewById(R.id.zjysfdw_no);
        zjysfdw_no.setOnClickListener(listener);

        sfyzlkzwj_yes= (ImageView) v.findViewById(R.id.sfyzlkzwj_yes);
        sfyzlkzwj_yes.setOnClickListener(listener);
        sfyzlkzwj_no= (ImageView) v.findViewById(R.id.sfyzlkzwj_no);
        sfyzlkzwj_no.setOnClickListener(listener);

        xgrydgcsxcd= (TextView) v.findViewById(R.id.xgrydgcsxcd);
        kgrq= (TextView) v.findViewById(R.id.kgrq);
        jhwgrq= (TextView) v.findViewById(R.id.jhwgrq);
        tbrq= (TextView) v.findViewById(R.id.tbrq);
        tbrq.setText(ActUtil.getDate());
        sgdw= (TextView) v.findViewById(R.id.sgdw);
        jldw= (TextView) v.findViewById(R.id.jldw);
        sjdw= (TextView) v.findViewById(R.id.sjdw);
        dlwz= (TextView) v.findViewById(R.id.dlwz);
        lon1= (TextView) v.findViewById(R.id.lon1);
        lon2= (TextView) v.findViewById(R.id.lon2);
        lon3= (TextView) v.findViewById(R.id.lon3);
        lat1= (TextView) v.findViewById(R.id.lat1);
        lat2= (TextView) v.findViewById(R.id.lat2);
        lat3= (TextView) v.findViewById(R.id.lat3);
        projectName= (TextView) v.findViewById(R.id.projectName);

        gcjzqk= (EditText) v.findViewById(R.id.gcjzqk);
        qt= (EditText) v.findViewById(R.id.qt);
        jcfzr= (EditText) v.findViewById(R.id.jcfzr);
        tbr= (EditText) v.findViewById(R.id.tbr);
        hbz= (EditText) v.findViewById(R.id.hbz);
        zbx= (EditText) v.findViewById(R.id.zbx);
        zby= (EditText) v.findViewById(R.id.zby);

        v.findViewById(R.id.xgrydgcsxcdLayout).setOnClickListener(listener);
        v.findViewById(R.id.kgrqLayout).setOnClickListener(listener);
        v.findViewById(R.id.jhwgrqLayout).setOnClickListener(listener);
        v.findViewById(R.id.tbrqLayout).setOnClickListener(listener);


    }

    private View.OnClickListener listener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.kgrqLayout:
                case R.id.jhwgrqLayout:
                case R.id.tbrqLayout:
                    String[] dateStart = null;
                    if(view.getId()==R.id.kgrqLayout) {
                        dialogtype=0;
                        if(kgrq.getText().length()>0)
                            dateStart = kgrq.getText().toString().split("-");
                        else
                            dateStart = ActUtil.getDate().split("-");
                    }else if(view.getId()==R.id.jhwgrqLayout) {
                        dialogtype=1;
                        if(jhwgrq.getText().length()>0)
                            dateStart = jhwgrq.getText().toString().split("-");
                        else
                            dateStart = ActUtil.getDate().split("-");
                    }else if(view.getId()==R.id.tbrqLayout) {
                        dialogtype=2;
                        dateStart = tbrq.getText().toString().split("-");
                    }
                    datePickerDialog=new DatePickerDialog(getActivity(), mDateSetListener, Integer.valueOf(dateStart[0]),
                            Integer.valueOf(dateStart[1])-1, Integer.valueOf(dateStart[2]));
                    datePickerDialog.show();
                    break;
                case R.id.districtLayout:
                    Intent selectIntent=new Intent(getActivity(), AreaInputAct.class);
                    startActivityForResult(selectIntent, 0x11);
                    break;
                case R.id.xgrydgcsxcdLayout:
                    final String[] ljcdTxt={"了解","基本了解","不了解"};
                    DialogUtil.showSelectDialog(getActivity(), "熟悉程度", ljcdTxt, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            xgrydgcsxcd.setText(ljcdTxt[i]);
                        }
                    });
                    break;
                case R.id.sfczwffbzb_yes:
                    sfczwffbzb="是";
                    sfczwffbzb_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sfczwffbzb_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sfczwffbzb_no:
                    sfczwffbzb="否";
                    sfczwffbzb_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sfczwffbzb_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.dwzz_sgdw_yes:
                    dwzz_sgdw="有";
                    dwzz_sgdw_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    dwzz_sgdw_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.dwzz_sgdw_no:
                    dwzz_sgdw="无";
                    dwzz_sgdw_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    dwzz_sgdw_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.dwzz_jldw_yes:
                    dwzz_jldw="有";
                    dwzz_jldw_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    dwzz_jldw_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.dwzz_jldw_no:
                    dwzz_jldw="无";
                    dwzz_jldw_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    dwzz_jldw_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.xmjlsfyxyjszc_yes:
                    xmjlsfyxyjszc="有";
                    xmjlsfyxyjszc_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    xmjlsfyxyjszc_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.xmjlsfyxyjszc_no:
                    xmjlsfyxyjszc="无";
                    xmjlsfyxyjszc_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    xmjlsfyxyjszc_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.jlryzgzs_yes:
                    jlryzgzs="有";
                    jlryzgzs_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    jlryzgzs_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.jlryzgzs_no:
                    jlryzgzs="无";
                    jlryzgzs_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    jlryzgzs_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.tsgzry_yes:
                    tsgzry="有";
                    tsgzry_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    tsgzry_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.tsgzry_no:
                    tsgzry="无";
                    tsgzry_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    tsgzry_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sfszwpyt_yes:
                    sfszwpyt="是";
                    sfszwpyt_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sfszwpyt_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sfszwpyt_no:
                    sfszwpyt="否";
                    sfszwpyt_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sfszwpyt_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.zjysfdw_yes:
                    zjysfdw="是";
                    zjysfdw_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    zjysfdw_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.zjysfdw_no:
                    zjysfdw="否";
                    zjysfdw_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    zjysfdw_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.sfyzlkzwj_yes:
                    sfyzlkzwj="是";
                    sfyzlkzwj_yes.setImageResource(R.mipmap.app_login_remember_sel);
                    sfyzlkzwj_no.setImageResource(R.mipmap.app_login_remember_unsel);
                    break;
                case R.id.sfyzlkzwj_no:
                    sfyzlkzwj="否";
                    sfyzlkzwj_yes.setImageResource(R.mipmap.app_login_remember_unsel);
                    sfyzlkzwj_no.setImageResource(R.mipmap.app_login_remember_sel);
                    break;
                case R.id.projectNameLayout:
                    Intent zdmcIntent=new Intent(getActivity(), SelectorAct.class);
                    zdmcIntent.putExtra("Report", true);
                    zdmcIntent.putExtra("Type", 3);
                    startActivityForResult(zdmcIntent, 0x10);
                    break;
            }
        }
    };

    public boolean canbeCreate(){
        if(guid==null||guid.length()==0){
            ToastUtils.displayTextShort(getActivity(), "请选择一个治理工程");
        }else if(jcfzr.getText().toString().length()==0){
            ToastUtils.displayTextShort(getActivity(), "请输入检查负责人");
        }else if(tbr.getText().toString().length()==0) {
            ToastUtils.displayTextShort(getActivity(), "请输入填表人");
        }else{
            return true;
        }
        return false;
    }

    DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            System.out.println("---> 设置后: year="+year+", month="+monthOfYear+",day="+dayOfMonth);
            String dateStr=year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            if(dialogtype==0)
                kgrq.setText(dateStr);
            else if(dialogtype==1)
                jhwgrq.setText(dateStr);
            else if(dialogtype==2)
                tbrq.setText(dateStr);
        }
    };

    private String gczl_quxian="", gczl_xiangzhen="", gczl_cun="", gczl_zu="";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x11 && resultCode == 0x10) {
            AreaInfos infos = (AreaInfos) data.getSerializableExtra(AreaInfos.Name);
            String selectTxt = "";
            for (AreaInfo info : infos.getInfos()) {
                selectTxt = selectTxt + info.getName() + "  ";
            }
//            districtTxt.setText(selectTxt);
        } else if (resultCode == 0x11) {
            data.setClass(getActivity(), TitleResultListAct.class); //查看线下数据库内容
            data.putExtra("TakeDataBack", true);
            data.putExtra("Type", 3);
            data.putExtra("Title", "工程治理");
            startActivityForResult(data, 0x20);
        } else if (resultCode == 0x21) {
            MapBean mapBean = (MapBean) data.getSerializableExtra("InfoMap");
            guid = mapBean.getMap().get("ZHCA01A010");
            projectName.setText(mapBean.getMap().get("ZHCA01A020"));
            sgdw.setText(mapBean.getMap().get("ZHCA01A230"));
            jldw.setText(mapBean.getMap().get("ZHCA01A210"));
            sjdw.setText(mapBean.getMap().get("ZHCA01A200"));

            gczl_quxian=mapBean.getMap().get("ZHCA01A040");
            if(gczl_quxian!=null)
                gczl_quxian=handler.getDistrictName(gczl_quxian);
            gczl_xiangzhen=mapBean.getMap().get("ZHCA01A050");
            if(gczl_xiangzhen!=null)
                gczl_xiangzhen=handler.getDistrictName(gczl_xiangzhen);
            gczl_cun=mapBean.getMap().get("ZHCA01A060");
            gczl_zu=mapBean.getMap().get("ZHCA01A070");
            dlwz.setText(gczl_quxian+"  "+gczl_xiangzhen+"  "+gczl_cun+"  "+gczl_zu);
//            hb.setText(mapBean.getMap().get("ZHCA01A210"));

            String lon = mapBean.getMap().get("ZHCA01A075");
            this.lon = lon;
            String lat = mapBean.getMap().get("ZHCA01A076");
            this.lat = lat;
            setLoaction(lon, lat);

        }
    }

    private void setLoaction(String lon, String lat){
        if(lon!=null&&lon.contains(".")) {
            String[] lons=lon.split("\\.");
            lon1.setText(lons[0]);
            if(lons[1]!=null){
                if(lons[1].length()>2){
                    lon2.setText(lons[1].substring(0,2));
                    lon3.setText(lons[1].substring(2,lons[1].length()));
                }else{
                    lon2.setText(lons[1]);
                    lon3.setText("00");
                }
            }else{
                lon2.setText("00");
                lon3.setText("00");
            }
        }
        if(lat!=null&&lat.contains(".")) {
            String[] lats=lat.split("\\.");
            lat1.setText(lats[0]);
            if(lats[1]!=null){
                if(lats[1].length()>2){
                    lat2.setText(lats[1].substring(0,2));
                    lat3.setText(lats[1].substring(2,lats[1].length()));
                }else{
                    lat2.setText(lats[1]);
                    lat3.setText("00");
                }
            }else{
                lat2.setText("00");
                lat3.setText("00");
            }
        }
    }
}
