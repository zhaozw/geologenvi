package com.sichuan.geologenvi.act;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.scgis.mmap.helper.TileCacheDBManager;
import com.scgis.mmap.map.SCGISTiledMapServiceLayer;
import com.sichuan.geologenvi.R;
import com.sichuan.geologenvi.bean.MapPoint;
import com.sichuan.geologenvi.bean.PopupInfoItem;
import com.sichuan.geologenvi.utils.ActUtil;
import com.sichuan.geologenvi.utils.ConstantUtil;
import com.sichuan.geologenvi.utils.ImageUtil;
import com.sichuan.geologenvi.utils.JsonUtil;
import com.sichuan.geologenvi.utils.LogUtil;
import com.sichuan.geologenvi.utils.SharedPreferencesUtil;
import com.sichuan.geologenvi.utils.ToastUtils;
import com.sichuan.geologenvi.views.MarkerSupportView;
import com.sichuan.geologenvi.views.MenuPopup;
import com.sichuan.geologenvi.views.PSDdialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MapAct  extends AppFrameAct {

    private final String dlgUrl="http://www.scgis.net.cn/iMap/iMapServer/DefaultRest/services/scmobile_dlg";
    private MarkerSupportView  mPopView=null;
    private String addr="";
    private boolean firstMove=false;
    private MapView mMapView;
    private LocationDisplayManager lDisplayManager;
    private LocationManager lm;
    private String bestProvider;
    private double lat=0, lon=0;
    private Point pressPoint;
    private GraphicsLayer mGraphicsLayer;
    private Map<Long, MapPoint> points=new LinkedHashMap<>();
    private MenuPopup popup;
    private String[] typeNames=new String[]{"地质灾害点","地下水","矿山","地质遗迹"};
    private int toLocation=0;
    private int showType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        _setHeaderTitle("地图");
        initView();
        _setRightHomeText("灾害点", listener);
        addMarker();
        showUserMarker();

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        bestProvider = lm.getBestProvider(getCriteria(), true);
        lm.requestLocationUpdates(bestProvider, 1000, 8, locationListener);
    }

    private Criteria getCriteria() {
        // TODO Auto-generated method stub
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setSpeedRequired(false);
        c.setCostAllowed(false);
        c.setBearingRequired(false);
        c.setAltitudeRequired(false);
        c.setPowerRequirement(Criteria.POWER_LOW);
        return c;
    }

    private void showUserMarker() {
        String json=SharedPreferencesUtil.getString(this, ConstantUtil.UserPoint);
        points= JsonUtil.getUserPointByJson(json);
        for(Long key:points.keySet()){
            MapPoint point=points.get(key);
            Map<String, Object> map = new HashMap<>();
            map.put("desc", point.getDesc());
            map.put("timeid", key);
            Graphic gp1 = CreateGraphic(point.getP(), map, R.mipmap.of_location_icon, 16);
            getGraphicLayer().addGraphic(gp1);
        }
    }

    private void addMarker() {
        mPopView = new MarkerSupportView(this, "题目", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopView.setVisibility(View.GONE);
            }
        });
        mMapView.addView(mPopView.getPopView());
        mPopView.setVisibility(View.GONE);
    }

    private void initView() {
        ArcGISRuntime.setClientId("zzzz4r7FQDyeNu12");

        mMapView=(MapView)findViewById(R.id.tdtMap);
        // mMapView.setResolution(0.010986328125);
//        mMapView.centerAt(new Point(105.444402, 28.871906), true);

        String mToken="YgA6oKQf_zMiFPwkc3GerqZq9dvmjc0smYCnhwEZPngytdYrPjwSa22d90lgVZ4q";//token ʹ�����ͼ�Ĵ����û�������

        long dlgdbsize = 100;//  100MB
        int dlgTileCompress=75;  //ͼƬѹ��75%

        TileCacheDBManager mDLGTileDBManager=new TileCacheDBManager(this,"iDLGTile1.db");  //�������߻����
        final SCGISTiledMapServiceLayer mDLGTileMapServiceLayer=new SCGISTiledMapServiceLayer(this,dlgUrl,mToken,true,mDLGTileDBManager);

        mDLGTileMapServiceLayer.setCacheSize(dlgdbsize); //�����ļ���С����
        mDLGTileMapServiceLayer.setTileCompressAndQuality(true, dlgTileCompress);  //����Ƭ����ѹ��
        mMapView.addLayer(mDLGTileMapServiceLayer);
        mMapView.setExtent(new Envelope(95,24,110,35));
        mMapView.centerAt(28.871906, 105.444402, true);
        mDLGTileMapServiceLayer.setVisible(true);
        mMapView.setMinScale(mDLGTileMapServiceLayer.getMinScale());
        mMapView.setMaxScale(mDLGTileMapServiceLayer.getMaxScale());
        mMapView.setOnSingleTapListener(singleTapListener);
        mMapView.setOnTouchListener(new View.OnTouchListener() {

            float startX=0;
            float startY=0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getRawX();
                        startY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //第一种方法，获取两点间距离
                        float endX = motionEvent.getRawX() - startX;
                        float endY = motionEvent.getRawY() - startY;
                        double distance = Math.sqrt(endX * endX + endY * endY);//
                        if (distance > 100) {
                            getDataOnScreen();
                        }
                        break;
                }
                return false;
            }
        });
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float x, float y) {
                pressPoint = mMapView.toMapPoint(new Point(x, y));
                if(points.size()<5) {
                    new PSDdialog(MapAct.this, cb, "请输入该点信息", false).show();
                }else
                    ToastUtils.displayTextShort(MapAct.this, "表计数已达上限，请删除部分");
                return false;
            }
        });

        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.setLocationListener(locationListener);
        lDisplayManager.setAllowNetworkLocation(true);   //是否允许网络定位
        lDisplayManager.setAccuracyCircleOn(false);       //是否要圈
        lDisplayManager.setShowLocation(true);
        try {
            lDisplayManager.setAccuracySymbol(new SimpleFillSymbol(Color.GREEN).setAlpha(20));
            lDisplayManager.setDefaultSymbol(new PictureMarkerSymbol(getResources().getDrawable(R.mipmap.icon_oval0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

        findViewById(R.id.myLoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(toLocation>=points.size()){

                Location l = lm.getLastKnownLocation(bestProvider);
                    if(l!=null)
                        mMapView.centerAt(l.getLatitude(), l.getLongitude(), true);
                    toLocation=0;
//                }else{
//                    mMapView.centerAt(points, true);
//                    toLocation++;
//                }
            }
        });
    }

    private void getDataOnScreen() {
        Envelope rExtent=new Envelope();
        mMapView.getExtent().queryEnvelope(rExtent);
        double leftB_x=rExtent.getXMin();
        double leftB_y=rExtent.getYMin();
        double topR_x=rExtent.getXMax();
        double topR_y=rExtent.getYMax();
    }


    @Override
    protected void onDestroy() {

        SharedPreferencesUtil.setString(this, ConstantUtil.UserPoint, JsonUtil.getJsonStrUserPoint(points));
        mMapView.destroyDrawingCache();
        lDisplayManager.stop();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }
    @Override 	protected void onResume() {
        super.onResume();
        mMapView.unpause();
        lDisplayManager.start();
    }

    private Graphic CreateGraphic(Point geometry, Map<String, Object> map, int imgRes, int yoffset) {
        Drawable image = getResources().getDrawable(imgRes);
        image.setBounds(5, 5, 5, 5);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(image);
        symbol.setOffsetY(yoffset);
        Graphic g = new Graphic(geometry, symbol, map);
        return g;
    }

    private GraphicsLayer getGraphicLayer() {
        if (mGraphicsLayer == null) {
            mGraphicsLayer = new GraphicsLayer();
            mMapView.addLayer(mGraphicsLayer);
        }
        return mGraphicsLayer;
    }

    PSDdialog.CallBack cb=new PSDdialog.CallBack() {
        @Override
        public void cancel() {

        }

        @Override
        public void editfinish(String psw) {
            Map<String, Object> map = new HashMap<>();
            map.put("desc", psw);
            long timeid=System.currentTimeMillis();
            map.put("timeid", timeid);
            MapPoint mapPoint=new MapPoint();
            mapPoint.setP(new Point(pressPoint.getX(), pressPoint.getY()));
            mapPoint.setDesc(psw);
            mapPoint.setId(timeid);
            points.put(timeid, mapPoint);
            Graphic gp1 = CreateGraphic(pressPoint, map, R.mipmap.of_location_icon, 16);
            getGraphicLayer().addGraphic(gp1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActUtil.closeSoftPan(MapAct.this);
                }
            },300);
        }
    };

    LocationListener locationListener=new LocationListener() {
        //boolean locationChanged = false;

        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
//                currentLocation=location;
            LogUtil.i("Location", "getLocation: "+location.getLongitude()+"    "+location.getLatitude());
            if(location!=null){
                lon=location.getLongitude();
                lat=location.getLatitude();
                mMapView.centerAt(lat, lon, true);
            }

        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    OnSingleTapListener singleTapListener=new OnSingleTapListener() {
        public void onSingleTap(float x, float y) {
            // TODO Auto-generated method stub
            int[] graphicIDs = getGraphicLayer().getGraphicIDs(x, y, 25);
            if (graphicIDs != null && graphicIDs.length > 0) {
                LayoutInflater inflater = LayoutInflater.from(MapAct.this);
                View view = inflater.inflate(R.layout.user_marker_pop, null);
//                view.setLayoutParams(new ViewGroup.LayoutParams(ImageUtil.dip2px(MapAct.this, 280), -2));
                Graphic gr = getGraphicLayer().getGraphic(graphicIDs[0]);
                view.findViewById(R.id.okBtn).setOnClickListener(listener);
                View del=view.findViewById(R.id.delBtn);
                Object id= gr.getAttributes().get("timeid");
                del.setTag(R.id.tag_1, id);
                del.setTag(R.id.tag_2, graphicIDs[0]);
                del.setOnClickListener(listener);
                TextView contentTxt= (TextView) view.findViewById(R.id.contentTxt);
                contentTxt.setText((String)(gr.getAttributes().get("desc")));
                Point popPositon = points.get(id).getP();
                Callout callout = mMapView.getCallout();
//                callout.setStyle(R.xml.calloutstyle);
                callout.setMaxWidthDp(340);
                callout.setOffset(0, 50);
                callout.show(popPositon, view);
            }
        }
    };

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.okBtn:
                    mMapView.getCallout().animatedHide();
                    break;
                case R.id.delBtn:
                    points.remove(view.getTag(R.id.tag_1));
                    getGraphicLayer().removeGraphic((int) view.getTag(R.id.tag_2));
                    mMapView.getCallout().animatedHide();
                    break;
                case R.id.right_txt:
                    popup = new MenuPopup(MapAct.this, typeNames, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            rightTxtBtn.setText(typeNames[i]);
                            popup.dismiss();
                        }
                    });
                    popup.showPopupWindow(rightTxtBtn);
                    break;
            }
        }
    };
}
