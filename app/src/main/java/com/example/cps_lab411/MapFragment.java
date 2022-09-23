package com.example.cps_lab411;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import com.example.cps_lab411.API.Thingsboard;
import com.example.cps_lab411.Communication.EncodeData;
import com.example.cps_lab411.EvenBus.ConntectHandleEvenbus;
import com.example.cps_lab411.EvenBus.HandleManualControl;
import com.example.cps_lab411.EvenBus.UDPMessage2EvenBus;
import com.example.cps_lab411.EvenBus.UDPMessageEvenBus;
import com.example.cps_lab411.EvenBus.UpdateGPS;
import com.example.cps_lab411.UavState.RobotArmParam;
import com.example.cps_lab411.UavState.UavMode;
import com.example.cps_lab411.UavState.UavParam;
import com.example.cps_lab411.UavState.WayPointParam;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.slider.Slider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    Marker UAVLocationMarker = null;
    private SwitchCompat mJoystickEnableSw, mCamEnableSw;
    public JoystickView joystickleft, joystickright;
    private TextView mtvLongitude, mtvLatitude, mtvAltitude, mtvVelocity, mtvBattery, mConnectStatus;
    int Vx, Vy, Vz, Yawrate;
    int servo1, servo2, servo3, servo4, servo5;
    FrameLayout mcamFrame;
    private Thread thread;
    private double lat = 0,lon = 0;
    private float altitude = 2;
    private float UavAltitude;
    private Button mLandbtn, mArmbtn, mNavigate;
    private ToggleButton mDrawmap;
    private boolean DrawEnable = false;
    private boolean EnableJoystick = false;
    private boolean readyToTakeoff = false;
    private LatLng preLatLng = null;
    private Polyline polylineDraw;
    private FrameLayout frameLayoutCam;
    RelativeLayout.LayoutParams layPra;
    private ImageView mUAVBattery, mImVSignal;
    private boolean checkConnected = false;
    private LinearLayout mSensorLinearLayout;
    private ScrollView mScollSensorView;
    private Button mButtonGrap;
    private Slider mAlitudeSlider;
    LinearLayout sensorNodeLayout;

    private Button mThingsboard;

    //Hide on VR layout
    TextView tvms, tvm, tvH, tvLat, tvLon;
    ConstraintLayout box;

    double BatteryPercentage;
    WifiManager wifi;
    WifiInfo wifiInfo;
    int levels = 3;

    // ThingsBoard REST API URL
    String url = "s";

    // Default Tenant Administrator credentials
    String username = "linh.nn280399@gmail.com";
    String password = "ktttlab411";

    Polyline DrawPolyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<WayPointParam> wayPointList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    List<Polyline> polylinesList = new ArrayList<>();
    List<Polyline> polylinesUAVList = new ArrayList<>();
    List<NodeSensor> nodeSensorList = new ArrayList<>();
    List<String> IDSensorList = new ArrayList<>();
    Map<Integer,Marker> sensorMarkerMap = new HashMap<Integer, Marker>();
    Map<Integer,NodeSensor> nodeSensorMap = new HashMap<Integer, NodeSensor>();
    Map<Integer,TextView> TextViewTempMap = new HashMap<Integer, TextView>();
    Map<Integer,TextView> TextViewHumMap = new HashMap<Integer, TextView>();
    Map<Integer,TextView> TextViewGasMap = new HashMap<Integer, TextView>();

    EncodeData encodeData = new EncodeData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Creating new rest client and auth with credentials for Thingsboard
//        RestClient client = new RestClient(url);
//        client.login(username,password);

        // Initialization Google map
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        wifi = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Reference to UI
        mThingsboard = view.findViewById(R.id.mThingsboard);

        mcamFrame = view.findViewById(R.id.cam_frame);
        mtvLongitude = view.findViewById(R.id.tvLongtitude);
        mtvLatitude = view.findViewById(R.id.tvLatitude);
        mArmbtn = view.findViewById(R.id.armbtn);
        mLandbtn = view.findViewById(R.id.landbtn);
        mDrawmap =  view.findViewById(R.id.drawmapbtn2);
        mNavigate = view.findViewById(R.id.drawmapbtn);
        mtvAltitude = view.findViewById(R.id.tvHeight);
        mtvBattery = getActivity().findViewById(R.id.batteryState);
        mtvVelocity = view.findViewById(R.id.tvVelocity);
        frameLayoutCam = getActivity().findViewById(R.id.cam_frame);
        mUAVBattery = getActivity().findViewById(R.id.batterry_ic);
        mConnectStatus = getActivity().findViewById(R.id.tv_flightmode);
        mImVSignal = getActivity().findViewById(R.id.wifi_ic);
        mSensorLinearLayout = view.findViewById(R.id.Sensor_layout);
        mScollSensorView = view.findViewById(R.id.Sensor_scollview);
        mButtonGrap = view.findViewById(R.id.button_grap);
        mAlitudeSlider = view.findViewById(R.id.slider);


        //Hide on VR layout
        tvms = view.findViewById(R.id.tvms);
        tvm = view.findViewById(R.id.tvm);
        tvH = view.findViewById(R.id.tvH);
        tvLat = view.findViewById(R.id.tvLat);
        tvLon = view.findViewById(R.id.tvLon);
        box = view.findViewById(R.id.box);

        // Enable small camera
        mCamEnableSw = view.findViewById(R.id.camEnableSw);
        EnableCam();
        // Create virtual joystick and their function
        mJoystickEnableSw = view.findViewById(R.id.enableJoystick);
        // Joystick left
        joystickleft = view.findViewById(R.id.joystickViewLeft);
        // Joystick right
        joystickright = view.findViewById(R.id.joystickViewRight);
        // Disable Joystick when start the app
        DataHolder.getInstance().setJoyStickEnable(false);
        // Enable Joystick Switch
        EnableJoystick();
        // The function of joystick
        JoyStickControl();

        mAlitudeSlider.setVisibility(View.GONE);

        //Thingsboard
        mThingsboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thingsboard dialog = new Thingsboard(getContext());
                dialog.show();
            }
        });

        // Land Button
        mLandbtn.setOnClickListener(view13 -> {
            encodeData.SendCommandLand();
        });

        // TakeOff Button
        mArmbtn.setOnClickListener(view12 -> {
            if(!readyToTakeoff) {
                mAlitudeSlider.setVisibility(View.VISIBLE);
                readyToTakeoff = true;
            } else {
                encodeData.SendCommandTakeoff(altitude);
                mAlitudeSlider.setVisibility(View.GONE);
                readyToTakeoff = false;
            }
        });

        mButtonGrap.setOnClickListener(view1 -> {
            if (servo5 == 1) {
                servo5 = 0;
            } else {
                servo5 = 1;
            }
            RobotArmParam.getInstance().setServo5(servo5);
        });



        // Set Drawmap function to false when start the app
        mDrawmap.setChecked(false);
        DrawFlightPath();

        // Send GPS list to communication Fragment
        mNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(WayPointParam wayPoint: wayPointList) {
                    encodeData.SendMissionMessage(wayPoint);
                }
                encodeData.SendFlyToCommand( 1, 1);
            }
        });

        mAlitudeSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                altitude = slider.getValue();
            }
        });

        hideButtonOnVRLayout();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Registered EventBus
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    /*==================================================================================
     *                             Handle Map Function
     *==================================================================================
     */
    // Initialize when Map ready
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        int height = 120;
        int width = 120;
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitMapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.compassinstrumentarrow);
        Bitmap b = bitMapDraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        // Initialize first coordinate
        LatLng HUST = new LatLng(21.006418, 105.843118);

        if (zoomViewOnVRLayout()){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HUST, 18));
        }else{
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HUST, 20));
        }

        if (zoomViewOnVRLayout()){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HUST, 18));
        }else{
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HUST, 25));
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(this);
    }

    // Move marker when GPS change and draw Polyline
    private void setUAVLocationMarker(double lat, double lon, float rotation) {
        LatLng latLng = new LatLng(lat, lon);

        int height = 120;
        int width = 120;
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitMapIcon = (BitmapDrawable)getResources().getDrawable(R.drawable.compassinstrumentarrow);
        Bitmap b = bitMapIcon.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        // Move marker
        if (UAVLocationMarker == null) {
            // Create new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng).title("UAV").snippet("Latitude: " + lat + "\n" + "Longitude: " + lon);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            UAVLocationMarker = mMap.addMarker(markerOptions.flat(true).anchor(0.5f, 0.5f));
            UAVLocationMarker.setRotation(rotation);
            if (zoomViewOnVRLayout()){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }else{
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25));
            }
        } else {
                // Move marker
                UAVLocationMarker.setPosition(latLng);
                UAVLocationMarker.setTitle("UAV");
                UAVLocationMarker.setSnippet("Latitude: " + lat + "\n" + "Longitude: " + lon);
                UAVLocationMarker.setRotation(rotation);
        }
    }

    private void UpdateUavMarkerLocation(double _lat, double _lon, float _rotation) {
        if((lat == 0) & (lon == 0)) {
            lat = _lat;
            lon = _lon;
        }
        setUAVLocationMarker(_lat, _lon, _rotation);

        if(UavParam.getInstance().getAimMode() == 1) {
            Polyline polylinegps = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(lat, lon), new LatLng(_lat, _lon))
                    .width(5)
                    .color(Color.RED));
            lat = _lat;
            lon = _lon;
            polylinesUAVList.add(polylinegps);
        }
    }


    public boolean zoomViewOnVRLayout(){
        if(getParentFragmentManager().findFragmentByTag("minmap1") != null || getParentFragmentManager().findFragmentByTag("maxmap1") != null){
            return true;
        }
        return false;
    }

    public void hideButtonOnVRLayout(){
        if(getParentFragmentManager().findFragmentByTag("minmap1") != null || getParentFragmentManager().findFragmentByTag("maxmap1") != null){
            setVisibilityForButton();
        }
    }

    public void setVisibilityForButton() {
        mCamEnableSw.setVisibility(View.INVISIBLE);
        mJoystickEnableSw.setVisibility(View.INVISIBLE);
        mDrawmap.setVisibility(View.INVISIBLE);
        mArmbtn.setVisibility(View.INVISIBLE);
        mLandbtn.setVisibility(View.INVISIBLE);
        mNavigate.setVisibility(View.INVISIBLE);
        mtvAltitude.setVisibility(View.INVISIBLE);
        mtvLatitude.setVisibility(View.INVISIBLE);
        mtvLongitude.setVisibility(View.INVISIBLE);
        mtvVelocity.setVisibility(View.INVISIBLE);
        tvms.setVisibility(View.INVISIBLE);
        tvm.setVisibility(View.INVISIBLE);
        tvH.setVisibility(View.INVISIBLE);
        tvLat.setVisibility(View.INVISIBLE);
        tvLon.setVisibility(View.INVISIBLE);
        box.setVisibility(View.INVISIBLE);
    }

    /*==================================================================================
     *                             Handle Map Function
     *==================================================================================
     */

    private void setSensorLocationMarker(double lat, double lon, int ID) {
        LatLng latLng = new LatLng(lat, lon);

        int height = 100;
        int width = 100;
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitMapIcon = (BitmapDrawable) getResources().getDrawable(R.drawable.sensor);
        Bitmap b = bitMapIcon.getBitmap();
        Bitmap smallSensorMarker = Bitmap.createScaledBitmap(b, width, height, false);

        MarkerOptions drawSensorMarkerOption = new MarkerOptions().position(latLng).title("Sensor: " + ID);
        drawSensorMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(smallSensorMarker));

        Marker sensorMarker = mMap.addMarker(drawSensorMarkerOption.flat(true));

        sensorMarkerMap.put(ID, sensorMarker);
    }

    private void setSensorAlertLocationMarker(NodeSensor sensor, boolean alert) {
        if(sensor.getAlert() != alert) {
            if(alert) {
                Objects.requireNonNull(sensorMarkerMap.get(sensor.getID())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sensor_warning_36));

                Runnable flyToAlertSensorThread =
                        new Runnable(){
                            public void run(){
                                encodeData.SendCommandTakeoff(3);
                                while (UavAltitude <= 2.7) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                WayPointParam WayPoint = new WayPointParam();
                                WayPoint.setAltitude(3);
                                WayPoint.setLatitude((float) sensor.getSensorLatitude());
                                WayPoint.setLongitude((float) sensor.getSensorLongitude());
                                WayPoint.setSpeed(0.8f);
                                encodeData.SendMissionMessage(WayPoint);
                                encodeData.SendFlyToCommand(1,1);
                            }
                        };

                Thread flyToAlertSensor = new Thread(flyToAlertSensorThread);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setMessage("Sensor " + sensor.getID() + " alert?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Navigate",
                        (dialog, id) -> {
                            flyToAlertSensor.start();
                            dialog.cancel();
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            } else {
                Objects.requireNonNull(sensorMarkerMap.get(sensor.getID())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sensor_36));
            }
            sensor.setAlert(alert);
        }
    }

    private void setSensorMarkerParameter(int ID, double Temp, double Hum, double Gas) {
        Objects.requireNonNull(sensorMarkerMap.get(ID))
                .setSnippet("Temperature: " + Temp + " °C\n" + "Humidity: " + Hum + " %\n" + "Gas: " + Gas + " %");
    }

    private void setAlertSensorMarkerParameter(int ID, double Temp, double Hum, double Gas) {
        Objects.requireNonNull(sensorMarkerMap.get(ID))
                .setSnippet("Temperature: " + Temp + " °C\n" + "Humidity: " + Hum + " %\n" + "Gas: " + Gas + " %");

    }

    public void setLayoutSensor(int ID, double Temp, double Hum, double Gas) {
        sensorNodeLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpView.setMargins(10,10,10,10);
        sensorNodeLayout.setLayoutParams(lpView);
        sensorNodeLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tvID = new TextView(getContext());
        TextView tvTemp = new TextView(getContext());
        TextView tvHum = new TextView(getContext());
        TextView tvGas= new TextView(getContext());

        TextViewTempMap.put(ID, tvTemp);
        TextViewHumMap.put(ID, tvHum);
        TextViewGasMap.put(ID, tvGas);

        tvID.setTextColor(Color.WHITE);
        tvTemp.setTextColor(Color.WHITE);
        tvHum.setTextColor(Color.WHITE);
        tvGas.setTextColor(Color.WHITE);

        String SensorID = "Sensor: " + ID;
        String TemperatureSensor = "Temp: " + Temp;
        String HumiditySensor = "Hum: " + Hum;
        String GasSensor = "Gas: " + Gas;

        tvID.setText(SensorID);
        tvTemp.setText(TemperatureSensor);
        tvHum.setText(HumiditySensor);
        tvGas.setText(GasSensor);

        tvID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nodeSensorMap != null) {
                    LatLng latLng = new LatLng(Objects.requireNonNull(nodeSensorMap.get(ID)).Latitude, Objects.requireNonNull(nodeSensorMap.get(ID)).Longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25));
                }
            }
        });

        sensorNodeLayout.addView(tvID);
        sensorNodeLayout.addView(tvTemp);
        sensorNodeLayout.addView(tvHum);
        sensorNodeLayout.addView(tvGas);

        mSensorLinearLayout.setVisibility(View.VISIBLE);
        mSensorLinearLayout.addView(sensorNodeLayout);
    }

    void ClearSensorLayout() {
        sensorNodeLayout.removeAllViews();
        mSensorLinearLayout.setVisibility(View.GONE);
    }

    void onSensorLayoutChange(int ID, double Temp, double Hum, double Gas) {
        Objects.requireNonNull(TextViewTempMap.get(ID)).setText(String.valueOf("Temp: " + Temp + " °C"));
        Objects.requireNonNull(TextViewHumMap.get(ID)).setText(String.valueOf("Hum: " + Hum + " %"));
        Objects.requireNonNull(TextViewGasMap.get(ID)).setText(String.valueOf("Gas: " + Gas + " %"));
    }

    /*==================================================================================
     *                             Enable / Disable Camera
     *==================================================================================
     */
    private void EnableCam() {
        // Switch function
        mCamEnableSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    layPra = new RelativeLayout.LayoutParams(466, 350);
                    layPra.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layPra.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layPra.topMargin = 80;

                    frameLayoutCam.setLayoutParams(layPra);

                    if (getParentFragmentManager().findFragmentByTag("cam") != null) {
                        //if the fragment exists, show it.
                        getParentFragmentManager().beginTransaction().show(Objects.requireNonNull(getParentFragmentManager().findFragmentByTag("cam"))).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        getParentFragmentManager().beginTransaction().add(R.id.cam_frame, new CamFragment(), "cam").commit();
                    }
                } else {
                    if (getParentFragmentManager().findFragmentByTag("cam") != null) {
                        getParentFragmentManager().beginTransaction().hide(Objects.requireNonNull(getParentFragmentManager().findFragmentByTag("cam"))).commit();
                    }
                }
            }
        });
    }

    /*==================================================================================
     *                             Handle Draw Mission
     *==================================================================================
     */

    // Draw mission path
    private void DrawFlightPath() {
        mDrawmap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean myisChecked) {
                if (myisChecked) {
                    // Create waypoint by click on map.
                    DrawEnable = true;
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            if (DrawEnable && !EnableJoystick) {
                                //Open a waypoint dialog window
                                openSetPointDialog(Gravity.CENTER, latLng);
                            }
                        }
                    });
                } else {
                    // Clear marker, waypoint and polyline on map
                    if (markerList != null) {
                        for (Polyline polyline : polylinesList) {
                            polyline.remove();
                        }
                        for (Marker marker : markerList) {
                            marker.remove();
                        }
                        // Clear coordinate and marker list
                        latLngList.clear();
                        markerList.clear();
                        //WayPointParam.idCount = 0;
                        wayPointList.clear();
                        polylinesList.clear();
                        //Disable draw mission
                        DrawEnable =false;
                        preLatLng = null;
                        mDrawmap.setChecked(false);
                    }
                }
            }
        });
    }

    private void openSetPointDialog(int gravity, LatLng latLng) {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_setwp);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes  = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(Gravity.CENTER == gravity);

        WayPointParam wayPoint = new WayPointParam();
        EditText editAltitude = dialog.findViewById(R.id.edt_wp_altitude);
        EditText editSpeed = dialog.findViewById(R.id.edt_wp_speed);
        Button buttonCancel = dialog.findViewById(R.id.btn_wp_cancel);
        Button buttonApply = dialog.findViewById(R.id.btn_wp_apply);

        editAltitude.setText("1");
        editSpeed.setText("1");

        // Cancel button - do nothing
        buttonCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Apply button, create waypoint on the map.
        // Multi waypoint create a navigate mission.
        buttonApply.setOnClickListener(v -> {
            String altitude = editAltitude.getText().toString();
            String speed = editSpeed.getText().toString();
            // Check if altitude and speed input is valid!!
            if(!altitude.isEmpty() & !speed.isEmpty()) {
                float altitudeValue = Float.parseFloat(altitude);
                float speedValue = Float.parseFloat(speed);
                if(altitudeValue > 0 & altitudeValue < 16) {
                    if(speedValue > 0 && speedValue < 11) {

                        //If input is valid, create a waypoint
                        wayPoint.setAltitude(Float.parseFloat(editAltitude.getText().toString()));
                        wayPoint.setSpeed(Float.parseFloat(editSpeed.getText().toString()));
                        wayPoint.setLatitude((float) latLng.latitude);
                        wayPoint.setLongitude((float) latLng.longitude);
                        wayPointList.add(wayPoint);

                        // Set size of marker
                        int height = 80;
                        int width = 60;
                        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker2);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap circleMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        // Create new marker when torch to screen
                        MarkerOptions drawMarkerOption = new MarkerOptions().position(latLng);
                        drawMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(circleMarker));
                        Marker marker = mMap.addMarker(drawMarkerOption);

                        // Set window information of the marker when click on it.
                        if(marker != null) {
                            marker.setTitle("WayPoint " + wayPoint.get_wpID());
                            marker.setSnippet("Latitude: " + latLng.latitude + "\n" + "Longitude: " + latLng.longitude
                                    + "\n" + "Altitude: " + wayPoint.getAltitude() + " m");
                        }

                        // Add new coordinate and marker to list
                        latLngList.add(latLng);
                        markerList.add(marker);

                        // Draw new polyline between two marker
                        if (preLatLng != null) {
                            Polyline polylineDraw = mMap.addPolyline(new PolylineOptions()
                                    .add(preLatLng, latLng)
                                    .width(5)
                                    .color(Color.BLUE));
                            polylinesList.add(polylineDraw);

                        }
                        // Save previous coordinate
                        preLatLng = latLng;
                        // Dismiss dialog window
                        dialog.dismiss();

                    } else {
                        Toast.makeText(requireContext(), "Invalid Speed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid Altitude!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Altitude and Speed can not be empty!!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    /*==================================================================================
     *                             Handle Virtual Joystick
     *==================================================================================
     */

    private void EnableJoystick() {
        // Enable Joystick
        mJoystickEnableSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Enable Joystick
                    joystickleft.setEnabled(true);
                    joystickright.setEnabled(true);
                    joystickleft.setVisibility(View.VISIBLE);
                    joystickright.setVisibility(View.VISIBLE);
                    EnableJoystick = true;
                    DataHolder.getInstance().setJoyStickEnable(true);

                } else {
                    // Disable Joystick
                    joystickleft.setEnabled(false);
                    joystickright.setEnabled(false);
                    joystickleft.setVisibility(View.INVISIBLE);
                    joystickright.setVisibility(View.INVISIBLE);
                    EnableJoystick = false;
                    DataHolder.getInstance().setJoyStickEnable(false);
                }
            }
        });
    }

    // Handle Virtual Joystick control Mode
    private void JoyStickControl() {
        //Joystick Left
        joystickleft.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                switch (DataHolder.getInstance().getControlMode()) {
                    case 0: {
                        JoystickLeftManualControl(angle, strength);
                        break;
                    }
                    case 1: {
                        JoystickLeftRobotControl(angle, strength);
                        break;
                    }
                    default: {

                    }
                }
            }
        }, 10);

        // Joystick Right
        joystickright.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                switch (DataHolder.getInstance().getControlMode()) {
                    case 0: {
                        JoystickRightManualControl(angle, strength);
                        break;
                    }
                    case 1: {
                        JoystickRightRobotControl(angle, strength);
                    }
                    default: {
                        break;
                    }
                }
            }
        }, 100);
        //Set Joystick invisible and disable joystick
        joystickleft.setVisibility(View.INVISIBLE);
        joystickright.setVisibility(View.INVISIBLE);
        joystickleft.setEnabled(false);
        joystickright.setEnabled(false);
    }

    private void JoystickLeftManualControl(int angle, int strength) {
        if (45 <= angle & angle <= 135) {
            Yawrate = 0;
            Vz = 500 + strength*5;
        } else if (135 < angle & angle < 225) {
            Vz = 500;
            Yawrate = -strength*5;
        } else if (225 <= angle & angle <= 315) {
            Yawrate = 0;
            Vz = 500 - strength*5;
        } else if ((315 < angle & angle < 360) | (angle < 45 & angle >= 0)) {
            Vz = 500;
            Yawrate = strength*5;
        } else {
            Vz = 500;
            Yawrate = 0;
        }
        UavParam.getInstance().setVz(Vz);
        UavParam.getInstance().setYawRate(Yawrate);
    }

    private void JoystickRightManualControl(int angle, int strength) {

        if (45 <= angle & angle <= 135) {
            Vx = strength * 5;
            Vy = 0;
        } else if (135 < angle & angle < 225) {
            Vx = 0;
            Vy = -strength*5;
        } else if (225 <= angle & angle <= 315) {
            Vx = -strength*5;
            Vy = 0;
        } else if ((315 < angle & angle < 360) | (angle < 45 & angle >= 0)) {
            Vx = 0;
            Vy = strength*5;
        } else {
            Vx = 0;
            Vy = 0;
        }
        UavParam.getInstance().setVx(Vx);
        UavParam.getInstance().setVy(Vy);
    }

    private void JoystickLeftRobotControl(int angle, int strength) {
        if (45 <= angle & angle <= 135) {
            servo2++;
        } else if (135 < angle & angle < 225) {
            servo1++;
        } else if (225 <= angle & angle <= 315) {
            servo2--;
        } else if ((315 < angle & angle < 360) | (angle < 45 & angle >= 0)) {
            servo1++;
        }

        if(servo2 > 180) {
            servo2 = 180;
        } else if(servo3 < 0) {
            servo2 = 0;
        }

        if(servo1 > 180) {
            servo1 = 180;
        } else if (servo1 < 0) {
            servo1 = 0;
        }
        RobotArmParam.getInstance().setRobotAimServo12(servo1, servo2);
    }

    private void JoystickRightRobotControl(int angle, int strength) {
        if (45 <= angle & angle <= 135) {
            servo3 = servo3 + 5;
        } else if (135 < angle & angle < 225) {
            servo4 = servo4 + 5;
        } else if (225 <= angle & angle <= 315) {
            servo3 = servo3 - 5;
        } else if ((315 < angle & angle < 360) | (angle < 45 & angle >= 0)) {
            servo4 = servo4 - 5;
        }

        if(servo3 > 180) {
            servo3 = 180;
        } else if(servo3 < 0) {
            servo3 = 0;
        }

        if(servo4 > 180) {
            servo4 = 180;
        } else if (servo4 < 0) {
            servo4 = 0;
        }

        RobotArmParam.getInstance().setRobotAimServo34(servo3, servo4);
    }

    public int checkConnectionSignal() {
        wifiInfo = wifi.getConnectionInfo();
        return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), levels);
    }

    public void setImageSignalStrong(int level) {
        switch (level) {
            case 0:
                mImVSignal.setImageResource(R.drawable.low_signal);
            case 1:
                mImVSignal.setImageResource(R.drawable.medium_signal);
            case 2:
                mImVSignal.setImageResource(R.drawable.full_signal);
            default:
                mImVSignal.setImageResource(R.drawable.no_connection);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorChange(UpdateSensor USS) {
        int ID = USS.getID();
        if(!nodeSensorMap.containsKey(ID)) {
            // If node sensor does not exits, create new node
            nodeSensorMap.put(ID, new NodeSensor(USS.getSSID()));
            Objects.requireNonNull(nodeSensorMap.get(ID)).setSensorCoordinate(USS.getSSLatitude(), USS.getSSLongitude());
            Objects.requireNonNull(nodeSensorMap.get(ID)).setTemperature(USS.getTemp());
            Objects.requireNonNull(nodeSensorMap.get(ID)).setHumidity(USS.getHum());
            Objects.requireNonNull(nodeSensorMap.get(ID)).setGas(USS.getGas());
            setSensorLocationMarker(nodeSensorMap.get(ID).getSensorLatitude(), nodeSensorMap.get(ID).getSensorLongitude(), ID);
            setLayoutSensor(ID, USS.getTemp(), USS.getHum(), USS.getGas());
        } else {
            // If node sensor already exits, update sensor parameter
            Objects.requireNonNull(nodeSensorMap.get(ID)).setSensorCoordinate(USS.getSSLatitude(), USS.getSSLongitude());
            Objects.requireNonNull(nodeSensorMap.get(ID)).setTemperature(USS.getTemp());
            Objects.requireNonNull(nodeSensorMap.get(ID)).setHumidity(USS.getHum());
            Objects.requireNonNull(nodeSensorMap.get(ID)).setGas(USS.getGas());

            NodeSensor nodeSensor = nodeSensorMap.get(ID);
            if(nodeSensor != null) {
                // Check if sensor has alert.
                setSensorAlertLocationMarker(nodeSensor, nodeSensor.checkSensorParameter());
                setSensorMarkerParameter(nodeSensor.getID(), nodeSensor.getTemperature(), nodeSensor.getHumidity(), nodeSensor.getGas());
                onSensorLayoutChange(nodeSensor.getID(),nodeSensor.getTemperature(), nodeSensor.getHumidity(), nodeSensor.getGas());
            }
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if(DrawEnable) {
            WayPointParam WayPoint = new WayPointParam();
            WayPoint.setAltitude(3);
            WayPoint.setLatitude((float) marker.getPosition().latitude);
            WayPoint.setLongitude((float) marker.getPosition().longitude);
            WayPoint.setSpeed(0.8f);
            wayPointList.add(WayPoint);

            LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            // Draw new polyline between two marker
            if (preLatLng != null) {
                Polyline polylineDraw = mMap.addPolyline(new PolylineOptions()
                        .add(preLatLng, latLng)
                        .width(5)
                        .color(Color.BLUE));
                polylinesList.add(polylineDraw);

            }
            // Save previous coordinate
            preLatLng = latLng;
        } else {
            if(marker != UAVLocationMarker) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setMessage("Navigate to this waypoint?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Navigate",
                        (dialog, id) -> {
                            WayPointParam WayPoint = new WayPointParam();
                            WayPoint.setAltitude(3);
                            WayPoint.setLatitude((float) marker.getPosition().latitude);
                            WayPoint.setLongitude((float) marker.getPosition().longitude);
                            WayPoint.setSpeed(0.8f);
                            encodeData.SendMissionMessage(WayPoint);
                            encodeData.SendFlyToCommand(1,1);
                            dialog.cancel();
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {

        LinearLayout info = new LinearLayout(getContext());
        info.setBackground(getResources().getDrawable(R.drawable.custom_bg));
        info.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(getContext());
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(marker.getTitle());

        TextView snippet = new TextView(getContext());
        snippet.setTextColor(Color.WHITE);
        snippet.setPadding(15, 15,15,15);
        snippet.setTextSize(10);
        snippet.setText(marker.getSnippet());

        info.addView(title);
        info.addView(snippet);

        return info;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    public void onDisconnected() {
        mtvLatitude.setText(String.valueOf("0.00000000"));
        mtvLongitude.setText(String.valueOf("0.00000000"));
        mtvAltitude.setText(String.valueOf("0.00"));
        mtvVelocity.setText(String.valueOf("0.00"));
        UAVLocationMarker.remove();
        UAVLocationMarker = null;
        for (Polyline polyline : polylinesUAVList) {
            polyline.remove();
        }


        if(nodeSensorList != null) {
            for(Map.Entry<Integer, Marker> maker : sensorMarkerMap.entrySet()) {
                maker.getValue().remove();
            }

            for(Map.Entry<Integer, TextView> textViewTemp : TextViewTempMap.entrySet()) {
                textViewTemp.getValue().setText("0.0 C");
            }

            for(Map.Entry<Integer, TextView> textViewHum : TextViewHumMap.entrySet()) {
                textViewHum.getValue().setText("0.0 %");
            }

            for(Map.Entry<Integer, TextView> textViewGas : TextViewGasMap.entrySet()) {
                textViewGas.getValue().setText("0.0 %");
            }

            nodeSensorList.clear();
            nodeSensorMap.clear();
            sensorMarkerMap.clear();
            TextViewHumMap.clear();
            TextViewGasMap.clear();
            TextViewTempMap.clear();
            ClearSensorLayout();
        }
    }

    public void onConnected() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUDPMessage2Change(UDPMessage2EvenBus UDP) {
        mtvLatitude.setText(String.valueOf(UDP.getLatitude()));
        mtvLongitude.setText(String.valueOf(UDP.getLongitude()));
        mtvAltitude.setText(String.valueOf(UDP.getAltitude()));
        UavAltitude = UDP.getAltitude();
        mtvVelocity.setText(String.valueOf(UDP.getVx()));
        UpdateUavMarkerLocation(UDP.getLatitude(), UDP.getLongitude(), UDP.getRotation());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectStatus(ConntectHandleEvenbus connect) {
        if(!connect.getConntectionState()) {
            onDisconnected();
        } else {
            onConnected();
        }
    }

}