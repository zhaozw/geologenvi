package com.sichuan.geologenvi;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sichuan.geologenvi.act.AppFrameAct;
import com.sichuan.geologenvi.act.MapAct;
import com.sichuan.geologenvi.act.MineListAct;
import com.sichuan.geologenvi.act.RainAct;
import com.sichuan.geologenvi.act.SearchAct;
import com.sichuan.geologenvi.act.TitleListAct;
import com.sichuan.geologenvi.act.contact.ActivityAddFriends;
import com.sichuan.geologenvi.adapter.RainAdapter;
import com.sichuan.geologenvi.adapter.TopImgAdapter;
import com.sichuan.geologenvi.bean.JsonMessage;
import com.sichuan.geologenvi.http.CallBack;
import com.sichuan.geologenvi.http.GlbsNet;
import com.sichuan.geologenvi.http.HttpHandler;
import com.sichuan.geologenvi.utils.ConstantUtil;
import com.sichuan.geologenvi.utils.DialogUtil;
import com.sichuan.geologenvi.utils.ImageUtil;
import com.sichuan.geologenvi.utils.JsonUtil;
import com.sichuan.geologenvi.utils.ScreenUtil;
import com.sichuan.geologenvi.utils.SharedPreferencesUtil;
import com.sichuan.geologenvi.utils.ToastUtils;
import com.sichuan.geologenvi.views.AsycnDialog;
import com.sichuan.geologenvi.views.AutoScrollViewPager;

public class MainActivity extends AppFrameAct {

    private String[] names={"法律法规","通讯录","地图","数据同步","地质灾害","统计分析","数据采集","矿山地质","地质遗迹",
            "地下水","水土地质","雨量监测"};
    private int[] ress={R.mipmap.icon_menu_1,R.mipmap.icon_menu_2,R.mipmap.icon_menu_3,R.mipmap.icon_menu_4,
            R.mipmap.icon_menu_5,R.mipmap.icon_menu_6,R.mipmap.icon_menu_7,R.mipmap.icon_menu_8,
            R.mipmap.icon_menu_9,R.mipmap.icon_menu_10,R.mipmap.icon_menu_11,R.mipmap.icon_menu_12};
    private AutoScrollViewPager viewPager;
    private AsycnDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _setHeaderGone();
        _setHeaderTitle(getResources().getString(R.string.app_name));
        initView();

//        FileUtil.verifyStoragePermissions(this);
    }

    private void initView() {
        viewPager= (AutoScrollViewPager) findViewById(R.id.galleryImgs);
        TopImgAdapter adapter=new TopImgAdapter(this, new int[]{R.mipmap.test_pic1, R.mipmap.test_pic2});
        viewPager.setAdapter(adapter);
//        viewPager.startAutoScroll(2000);
        LinearLayout menuLayout= (LinearLayout) findViewById(R.id.menuLayout);
        int paddtop = ImageUtil.dip2px(this,20);
        int itemWidth = (ScreenUtil.getScreenWidth(this)-3)/4;
        LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(itemWidth, itemWidth);
        llp.topMargin=1;
        llp.rightMargin=1;
        LayoutInflater inflater=LayoutInflater.from(this);
        LinearLayout layout=new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0;i<12;i++){
            View v=inflater.inflate(R.layout.item_main_menu, null);
            ImageView menuIcon= (ImageView) v.findViewById(R.id.menuIcon);
            TextView menuName= (TextView) v.findViewById(R.id.menuName);
            menuName.setText(names[i]);
            menuIcon.setImageResource(ress[i]);
            v.setLayoutParams(llp);
            layout.addView(v);
            v.setTag(i);
            v.setOnClickListener(listener);
            if(i%4==3){
                menuLayout.addView(layout);
                layout=new LinearLayout(this);
//                layout.setPadding(0,paddtop,0,0);
                layout.setOrientation(LinearLayout.HORIZONTAL);
            }
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent();
            switch ((int)view.getTag()){
                case 0:
                    i.setClass(MainActivity.this, SearchAct.class);
                    i.putExtra("Title", "法律法规");
                    startActivity(i);
                    break;
                case 1:
                    i.setClass(MainActivity.this, TitleListAct.class);
                    i.putExtra("Title", "通讯录");
                    i.putExtra("Type", "Contact");
                    startActivity(i);
                    break;
                case 2:
                    i.setClass(MainActivity.this, MapAct.class);
                    startActivity(i);
                    break;
                case 3:
                    dialog=new AsycnDialog(MainActivity.this);
                    dialog.show();
                    break;
                case 4:
                    i.setClass(MainActivity.this, TitleListAct.class);
                    i.putExtra("Title", "地质灾害");
                    i.putExtra("Type", "Disaster");
                    startActivity(i);
                    break;
                case 5:
                    i.setClass(MainActivity.this, TitleListAct.class);
                    i.putExtra("Title", "统计分析");
                    i.putExtra("Type", "Statistics");
                    startActivity(i);
                    break;
                case 6:
                    i.setClass(MainActivity.this, TitleListAct.class);
                    i.putExtra("Title", "数据采集");
                    i.putExtra("Type", "Report");
                    startActivity(i);
                    break;
                case 7:
                    i.setClass(MainActivity.this, MineListAct.class);
                    i.putExtra("Title", "矿山地质");
                    i.putExtra("TableName", "SL_KS_DZHJ_XX");
                    startActivity(i);
                    break;
                case 8:
                    i.setClass(MainActivity.this, MineListAct.class);
                    i.putExtra("Title", "地质遗迹");
                    i.putExtra("TableName", "SL_DZYJBH");
                    startActivity(i);
                    break;
                case 9:
                    i.setClass(MainActivity.this, MineListAct.class);
                    i.putExtra("Title", "地下水");
                    i.putExtra("TableName", "SL_TBLJING");
                    startActivity(i);
                    break;
                case 11:
                    i.setClass(MainActivity.this, RainAct.class);
                    i.putExtra("Title", "雨量监测");
                    startActivity(i);
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                exithandler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                ImageUtil.initImageLoader(this);
                ImageLoader imageloader=ImageLoader.getInstance();
                imageloader.clearMemoryCache();
                imageloader.destroy();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private static boolean isExit = false;

    Handler exithandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
