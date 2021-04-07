package com.didichuxing.doraemondemo.amap

import android.os.Bundle
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.enums.PathPlanningStrategy
import com.amap.api.navi.model.NaviLatLng
import com.didichuxing.doraemondemo.R
import com.didichuxing.doraemondemo.comm.CommBaseFragment

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：3/11/21-14:51
 * 描    述：
 * 修订历史：
 * ================================================
 */
class AMapRouterFragment : CommBaseFragment() {
    private lateinit var mAmap: AMap
    private lateinit var mapView: MapView
    private lateinit var mAMapNavi: AMapNavi
    private val mStartPoint = NaviLatLng(30.29659, 120.081127)
    private val mEndPoint = NaviLatLng(30.296793, 120.07527)
    override fun initActivityTitle(): String {
        return "高德路径规划"
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_amap
    }


    override fun initView(savedInstanceState: Bundle?) {
        mapView = findViewById(R.id.amap_view)
        mapView.onCreate(savedInstanceState)
        mAmap = mapView.map
        initAMapLocation()
    }

    /**
     * 初始化高德地图的定位
     */
    private fun initAMapLocation() {
        mAmap.minZoomLevel = 6.0f

        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        val myLocationStyle = MyLocationStyle()
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒
        myLocationStyle.interval(1000L)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.myLocationIcon(
            com.amap.api.maps.model.BitmapDescriptorFactory.fromResource(
                R.mipmap.ic_navi_map_gps_locked
            )
        )
        ////设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(true)
        mAmap.myLocationStyle = myLocationStyle //设置定位蓝点的Style

        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAmap.isMyLocationEnabled = true
        //规划路径
        mAMapNavi = AMapNavi.getInstance(activity?.application)
        val startList = mutableListOf<NaviLatLng>()
        startList.add(mStartPoint)
        val endList = mutableListOf<NaviLatLng>()
        endList.add(mEndPoint)
        mAMapNavi.calculateDriveRoute(
            startList,
            endList,
            null,
            PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT
        )
        mAMapNavi.addAMapNaviListener(activity?.application?.let {
            DefaultNaviListener(
                mAmap, mAMapNavi,
                it
            )
        })
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}