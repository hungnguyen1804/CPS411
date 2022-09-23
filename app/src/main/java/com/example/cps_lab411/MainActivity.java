package com.example.cps_lab411;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cps_lab411.Category.CategoryFlightModeAdapter;
import com.example.cps_lab411.Category.CategoryMode;
import com.example.cps_lab411.Communication.EncodeData;
import com.example.cps_lab411.EvenBus.ConntectHandleEvenbus;
import com.example.cps_lab411.EvenBus.UDPMessageEvenBus;
import com.example.cps_lab411.Communication.UDP.ManualControlThread;
import com.example.cps_lab411.UavState.UavMode;
import com.example.cps_lab411.UavState.UavParam;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private SwitchCompat mCamSwitch;
    private Toolbar toolbar;
    ImageView mSensorImV;
    private TextView mArmCheck, mConnectionStatus, mFlightMode, mTvUAVBattery;
    private ImageView mUAVBattery;
    private ImageView mImvConnection, mImvSwControlMode;
    private Spinner spnFlightMode;
    private CategoryFlightModeAdapter categoryFlightModeAdapter;
    private EncodeData encodeData = new EncodeData();
    private ManualControlThread manualControlThread = null;


    ImageView cockpit1, cockpit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorImV = findViewById(R.id.sensor_db);
        mArmCheck = findViewById(R.id.tv_armCheck);
        mUAVBattery = findViewById(R.id.batterry_ic);
        mFlightMode = findViewById(R.id.tv_flightmode);
        mImvConnection = findViewById(R.id.flightmode_ic);
        // Add menu
        mTvUAVBattery = findViewById(R.id.batteryState);
        mImvSwControlMode = findViewById(R.id.imageSwControlMode);

        //Init Connection Status = false
        DataHolder.getInstance().setConnectionStatus(false);

        // Add Custom Toolbar
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().hide();

        cockpit1 = findViewById(R.id.cockpit_layout1);
        cockpit2 = findViewById(R.id.cockpit_layout2);

        // Add Drawer Layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.lab411_logo4);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set Map Fragment as Default Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.conn_frame, new ConnFragment(), "con").commit();

        mSensorImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Sensor Clicked");
            }
        });

        mArmCheck.setOnClickListener(view -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Arming Vehicle?");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        DataHolder.getInstance().setSendArmCommand(true);
                        if (UavParam.getInstance().getAimMode() == 1) {
                            encodeData.SendCommandArmDisarm((byte) 0);
                        } else {
                            encodeData.SendCommandArmDisarm((byte) 1);
                        }
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
        });

        spnFlightMode = findViewById(R.id.spn_flightMode);
        categoryFlightModeAdapter = new CategoryFlightModeAdapter(this, R.layout.item_selected, getListCategory());
        spnFlightMode.setAdapter(categoryFlightModeAdapter);
        spnFlightMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (DataHolder.getInstance().getConnectionStatus()) {
                    int flightMode = categoryFlightModeAdapter.getItem(position).getFlightModeID();
                    encodeData.SendCommandSetMode(flightMode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mImvSwControlMode.setOnClickListener(view -> {
            int currentMode = DataHolder.getInstance().getControlMode();
            if(currentMode == 1) {
                //Set UAV control Mode
                DataHolder.getInstance().setControlMode(0);
                mImvSwControlMode.setImageResource(R.drawable.takeoff);
            } else {
                //Set Robot control Mode
                DataHolder.getInstance().setControlMode(1);
                mImvSwControlMode.setImageResource(R.drawable.robotic_arm);
            }
        });
    }

    private List<CategoryMode> getListCategory() {
        List<CategoryMode> list = new ArrayList<>();
        list.add(new CategoryMode("Manual", 0));
        list.add(new CategoryMode("Position", 1));
        list.add(new CategoryMode("OffBoard", 2));
        list.add(new CategoryMode("Land", 3));
        list.add(new CategoryMode("Hold", 4));

        return list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
        getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new MapFragment(), "map").commit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void hideLayoutVR() {
        if (getSupportFragmentManager().findFragmentByTag("minstream1") != null || getSupportFragmentManager().findFragmentByTag("maxstream1") != null) {
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("minmap1")).commit();
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("minmap2")).commit();
//            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("maxmap1")).commit();
//            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("maxmap2")).commit();
//            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("minstream1")).commit();
//            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("minstream2")).commit();
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("maxstream1")).commit();
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("maxstream2")).commit();
            cockpit1.setVisibility(View.INVISIBLE);
            cockpit2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_map) {
            hideLayoutVR();

            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("map")).commit();
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new MapFragment(), "map").commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("cam") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("cam")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("set")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("virtual") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("virtual")).commit();
            }

            //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        } else if (id == R.id.nav_cam) {
            hideLayoutVR();

            FrameLayout frameLayoutCam;
            frameLayoutCam = findViewById(R.id.cam_frame);
            RelativeLayout.LayoutParams layPra = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayoutCam.setLayoutParams(layPra);

            if (getSupportFragmentManager().findFragmentByTag("cam") != null) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("cam")).commit();
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.cam_frame, new CamFragment(), "cam").commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("virtual") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("virtual")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("set")).commit();
            }


        } else if (id == R.id.nav_con) {
            hideLayoutVR();

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("con")).commit();
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.conn_frame, new ConnFragment(), "con").commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("cam") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("cam")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("virtual") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("virtual")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("set")).commit();
            }

        } else if (id == R.id.nav_set) {
            hideLayoutVR();
            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("set")).commit();
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.set_frame, new SettingFragment(), "set").commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("cam") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("cam")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("virtual") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("virtual")).commit();
            }

        } else if (id == R.id.nav_virtualCabin) {
            if (getSupportFragmentManager().findFragmentByTag("virtual") != null) {
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("virtual")).commit();
                //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.virtual_frame, new VirtualFragment(), "virtual").commit();
                //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("cam") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("cam")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("set")).commit();
            }

        }

//        else if (id == R.id.nav_thingsboard)
//        {
//            hideLayoutVR();
//            if (getSupportFragmentManager().findFragmentByTag("thingsboard") != null) {
//                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                //if the fragment exists, show it.
//                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("thingsboard")).commit();
//            } else {
//                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                //if the fragment does not exist, add it to fragment manager.
//                getSupportFragmentManager().beginTransaction().add(R.id.set_frame, new ThingsboardFragment(), "thingsboard").commit();
//            }
//
//            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
//            }
//
//            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
//            }
//
//            if (getSupportFragmentManager().findFragmentByTag("cam") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("cam")).commit();
//            }
//
//            if (getSupportFragmentManager().findFragmentByTag("virtual") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("virtual")).commit();
//            }
//        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        // Close DrawerBar when touched outside
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setArmDisarmStatus(int armStatus) {
        if (armStatus == 1) {
            mArmCheck.setText(R.string.arming);
        } else {
            mArmCheck.setText(R.string.disarm);
        }
    }

    public void setBatteryStatus(int batteryStatus, int connectionStatus) {
        if (connectionStatus == 1 && batteryStatus < 101 && batteryStatus > 0) {
            mTvUAVBattery.setText(String.valueOf(batteryStatus));
            if (batteryStatus > 90 && batteryStatus < 101) {
                mUAVBattery.setImageResource(R.drawable.battery2_full);
            } else if (batteryStatus > 70) {
                mUAVBattery.setImageResource(R.drawable.battery2_level);
            } else if (batteryStatus > 40) {
                mUAVBattery.setImageResource(R.drawable.battery2_medium);
            } else if (batteryStatus > 20) {
                mUAVBattery.setImageResource(R.drawable.battery2_low);
            } else {
                mUAVBattery.setImageResource(R.drawable.battery2_empty);
            }
        }
    }

    public void setModeStatus(int mode, int connectionStatus) {
        UavMode uavMode = UavMode.getUavMode(mode);
        if (connectionStatus == 1 & DataHolder.getInstance().getConnectionStatus()) {
            mImvConnection.setImageResource(R.drawable.ic_baseline_check_24);
            switch (uavMode) {
                case Manual: {
                    mFlightMode.setText(R.string.manual);
                    break;
                }
                case Position: {
                    mFlightMode.setText(R.string.position);
                    break;
                }
                case OffBoard: {
                    mFlightMode.setText(R.string.offboard);
                    break;
                }
                case Hold: {
                    mFlightMode.setText(R.string.hold);
                    break;
                }
                case TakeOff: {
                    mFlightMode.setText(R.string.takeoff);
                    break;
                }
                case Land: {
                    mFlightMode.setText(R.string.land);
                }
                default: {
                    mFlightMode.setText(R.string.connected);
                    break;
                }
            }
        } else {
            onDisconnected();
        }
    }

    public void onDisconnected() {
        mImvConnection.setImageResource(R.drawable.ic_baseline_uncheck_24);
        mFlightMode.setText(R.string.disconnect);
        mUAVBattery.setImageResource(R.drawable.battery2_empty);
        mArmCheck.setText(R.string.disarm);
    }

    public void onConnected() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUDPMessage1Change(UDPMessageEvenBus UDP) {
        setArmDisarmStatus(UDP.getArm());
        setBatteryStatus(UDP.getBatteryRemaining(), UDP.getConnected());
        setModeStatus(UDP.getMode(), UDP.getConnected());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConntectEvenBus(ConntectHandleEvenbus connected) {
        if(!connected.getConntectionState()) {
            onDisconnected();
        } else {
            onConnected();
        }
    }
}