package com.example.cps_lab411;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cps_lab411.MediapipeHand.HandsResultGlRenderer;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.hands.HandLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;


import java.time.LocalTime;
import java.util.List;

public class VirtualFragment extends Fragment {
    private static final String TAG = "VirtualFragment";

    private class DataControl{
        int x;
        int y;
        int z;
        int r;
    }

    private class RoboControl{
        int rot_angle;
        int lift_angle;
        int open_angle;
    }
    private DataControl dataControl;
    private RoboControl roboControl;

    private boolean isOneHand = false, isTwoHands = false;

    private class PositionImage {

        float[] lhposx = new float[21];
        float[] lhposy = new float[21];
        double[] lhposz = new double[21];

        float[] rhposx = new float[21];
        float[] rhposy = new float[21];
        double[] rhposz = new double[21];

        double angle;
        float[] middlex = new float[21];
        float[] middley = new float[21];
        double[] middlez = new double[21];
    }

    private TextView lvx1, lvy1, lvz1;
    private TextView lvx2, lvy2, lvz2;
    private TextView angle1, angle2;
    private TextView mode1, mode2;
    private TextView state1, state2;
    private TextView battery1, battery2;
    private TextView speed1, speed2;
    private TextView lat1, lat2, lon1, lon2;
    private TextView height1, height2;
    private TextView distance1, distance2;

    private ImageView imgButtonLanding1, imgButtonLanding2, imgButtonTakeoff1, imgButtonTakeoff2;

    //Draw on top Frame
    private class Circle extends View{
        Paint paint = new Paint();
        Paint paintleft = new Paint();
        Paint paintright = new Paint();
        public Circle(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            paintleft.setColor(Color.GREEN);
            paintright.setColor(Color.YELLOW);
//            canvas.drawCircle(widthx/2, heightx/2, 30, paint);
//            canvas.drawLine(widthx/2, 0, widthx/2, heightx, paint);
//            canvas.drawLine(0, heightx/2, widthx, heightx/2, paint);
            drawHandLeft(canvas, paintleft);
            drawHandRight(canvas, paintright);
        }

        public void drawHandLeft(Canvas canvas, Paint paint){
            Paint paintControl = new Paint();
            paintControl.setColor(Color.RED);
            canvas.drawLine(position.lhposy[0], position.lhposx[0], position.lhposy[5], position.lhposx[5], paint);
            canvas.drawLine(position.lhposy[0], position.lhposx[0], position.lhposy[17], position.lhposx[17], paint);
            for(int i = 0; i < 21; i++){
                canvas.drawCircle(position.lhposy[i], position.lhposx[i], 10, paint);
                if (i == 4){
                    canvas.drawCircle(position.lhposy[i], position.lhposx[i], 10, paintControl);
                }
            }
            for(int i = 0; i < 4; i ++){
                canvas.drawLine(position.lhposy[i], position.lhposx[i], position.lhposy[i+1], position.lhposx[i+1], paint);
            }
            for(int i = 5; i < 8; i ++){
                canvas.drawLine(position.lhposy[i], position.lhposx[i], position.lhposy[i+1], position.lhposx[i+1], paint);
            }
            for(int i = 9; i < 12; i ++){
                canvas.drawLine(position.lhposy[i], position.lhposx[i], position.lhposy[i+1], position.lhposx[i+1], paint);
            }
            for(int i = 13; i < 16; i ++){
                canvas.drawLine(position.lhposy[i], position.lhposx[i], position.lhposy[i+1], position.lhposx[i+1], paint);
            }
            for(int i = 17; i < 20; i ++){
                canvas.drawLine(position.lhposy[i], position.lhposx[i], position.lhposy[i+1], position.lhposx[i+1], paint);
            }
            for(int i = 5; i <= 13; i+=4){
                canvas.drawLine(position.lhposy[i], position.lhposx[i], position.lhposy[i+4], position.lhposx[i+4], paint);
            }
        }

        public void drawHandRight(Canvas canvas, Paint paint){
            Paint paintControl = new Paint();
            paintControl.setColor(Color.RED);
            canvas.drawLine(position.rhposy[0], position.rhposx[0], position.rhposy[5], position.rhposx[5], paint);
            canvas.drawLine(position.rhposy[0], position.rhposx[0], position.rhposy[17], position.rhposx[17], paint);
            for(int i = 0; i < 21; i++){
                canvas.drawCircle(position.rhposy[i], position.rhposx[i], 10, paint);
                if (i == 4){
                    canvas.drawCircle(position.rhposy[i], position.rhposx[i], 10, paintControl);
                }
            }
            for(int i = 0; i < 4; i ++){
                canvas.drawLine(position.rhposy[i], position.rhposx[i], position.rhposy[i+1], position.rhposx[i+1], paint);
            }
            for(int i = 5; i < 8; i ++){
                canvas.drawLine(position.rhposy[i], position.rhposx[i], position.rhposy[i+1], position.rhposx[i+1], paint);
            }
            for(int i = 9; i < 12; i ++){
                canvas.drawLine(position.rhposy[i], position.rhposx[i], position.rhposy[i+1], position.rhposx[i+1], paint);
            }
            for(int i = 13; i < 16; i ++){
                canvas.drawLine(position.rhposy[i], position.rhposx[i], position.rhposy[i+1], position.rhposx[i+1], paint);
            }
            for(int i = 17; i < 20; i ++){
                canvas.drawLine(position.rhposy[i], position.rhposx[i], position.rhposy[i+1], position.rhposx[i+1], paint);
            }
            for(int i = 5; i <= 13; i+=4){
                canvas.drawLine(position.rhposy[i], position.rhposx[i], position.rhposy[i+4], position.rhposx[i+4], paint);
            }
        }

    }

    private class DrawHelpController extends View{
        Paint paint = new Paint();
        public DrawHelpController(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            paint.setColor(Color.WHITE);

            canvas.drawLine(widthx/2 - 110, 0, widthx/2 - 110, heightx, paint);
            canvas.drawLine(widthx/2 + 110, 0, widthx/2 + 110, heightx, paint);

            canvas.drawLine(0, heightx/2 - 110, widthx, heightx/2 - 110, paint);
            canvas.drawLine(0, heightx/2 + 110, widthx, heightx/2 + 110, paint);

        }
    }

    private PositionImage position;

    //Draw hands
    private double pi = Math.PI;
    private ImageView Img1, Img2;
    private ImageView Handleft1, Handright1, Handleft2, Handright2;

    private ImageView Steeringwheell1, Steeringwheell2, Steeringwheell;
    private int sum = 0;

    //Hand detect process
    public String detect_res="";
    public String detect_mode="";
    private Handler uiHandler = new Handler();
    private Hands hands;
    private static final boolean RUN_ON_GPU = true;
    private enum InputSource {
        UNKNOWN,
        CAMERA,
    }
    private InputSource inputSource = InputSource.UNKNOWN;
    private CameraInput cameraInput;
    private SolutionGlSurfaceView<HandsResult> glSurfaceView, glSurfaceView2;

    private boolean isRightHand = false;

    //Switch map and stream frame
    private DisplayMetrics metrics;
    private int heightx, widthx;

    private FrameLayout cam_frame1, cam_frame2, map_frame_back1, map_frame_back2, map_frame_front1, map_frame_front2;
    private RelativeLayout.LayoutParams params1, params2;
    private FrameLayout frameLayout1, frameLayout2;

    private ImageView cockpit1, cockpit2;

    private ImageButton streamexpand1, streamexpand2;
    private ImageButton mapexpand1, mapexpand2;

    private RelativeLayout relativeLayout1, relativeLayout2;
    private RelativeLayout drawHelpController1, drawHelpController2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View virtualView = inflater.inflate(R.layout.fragment_virtual, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //Display coordinates
        lvx1 = virtualView.findViewById(R.id.lvx1);
        lvy1 = virtualView.findViewById(R.id.lvy1);
        lvz1 = virtualView.findViewById(R.id.lvz1);

        lvx2 = virtualView.findViewById(R.id.lvx2);
        lvy2 = virtualView.findViewById(R.id.lvy2);
        lvz2 = virtualView.findViewById(R.id.lvz2);

        angle1 = virtualView.findViewById(R.id.angle1);
        angle2 = virtualView.findViewById(R.id.angle2);

        mode1 = virtualView.findViewById(R.id.tv_mode1);
        mode2 = virtualView.findViewById(R.id.tv_mode2);
        state1 = virtualView.findViewById(R.id.tv_state1);
        state2 = virtualView.findViewById(R.id.tv_state2);
        battery1 = virtualView.findViewById(R.id.tv_battery1);
        battery2 = virtualView.findViewById(R.id.tv_battery2);
        lat1 = virtualView.findViewById(R.id.tv_lat1);
        lat2 = virtualView.findViewById(R.id.tv_lat2);
        lon1 = virtualView.findViewById(R.id.tv_lon1);
        lon2 = virtualView.findViewById(R.id.tv_lon2);
        speed1 = virtualView.findViewById(R.id.tv_speed1);
        speed2 = virtualView.findViewById(R.id.tv_speed2);
        height1 = virtualView.findViewById(R.id.tv_height1);
        height2 = virtualView.findViewById(R.id.tv_height2);

        //Map view
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthx = metrics.widthPixels/2;
        heightx = metrics.heightPixels;

        streamexpand1 = virtualView.findViewById(R.id.button_streamexpand1);
        streamexpand2 = virtualView.findViewById(R.id.button_streamexpand2);
        mapexpand1 = virtualView.findViewById(R.id.button_mapexpand1);
        mapexpand2 = virtualView.findViewById(R.id.button_mapexpand2);

        streamexpand1.setX(dpToPx(110));
        streamexpand1.setY(heightx - dpToPx(100));
        mapexpand1.setX(dpToPx(110));
        mapexpand1.setY(heightx - dpToPx(100));

        streamexpand2.setX(dpToPx(110));
        streamexpand2.setY(heightx - dpToPx(100));
        mapexpand2.setX(dpToPx(110));
        mapexpand2.setY(heightx - dpToPx(100));


        frameLayout1 = virtualView.findViewById(R.id.preview_hands_layout1);
        frameLayout2 = virtualView.findViewById(R.id.preview_hands_layout2);
        cam_frame1 = getActivity().findViewById(R.id.cam_frame1);
        cam_frame2 = getActivity().findViewById(R.id.cam_frame2);
        map_frame_back1 = getActivity().findViewById(R.id.map_frame_back1);
        map_frame_back2 = getActivity().findViewById(R.id.map_frame_back2);
        map_frame_front1 = getActivity().findViewById(R.id.map_frame_front1);
        map_frame_front2 = getActivity().findViewById(R.id.map_frame_front2);


        cockpit1 = getActivity().findViewById(R.id.cockpit_layout1);
        cockpit2 = getActivity().findViewById(R.id.cockpit_layout2);
        cockpit1.setVisibility(View.VISIBLE);
        cockpit2.setVisibility(View.VISIBLE);

        Button touch1 = virtualView.findViewById(R.id.button_touch1);
        Button touch2 = virtualView.findViewById(R.id.button_touch2);
        touch1.setOnClickListener(
                v -> {
                    if (inputSource == InputSource.CAMERA) {
                        return;
                    }
                    stopCurrentPipeline();
                    setupStreamingModePipeline(InputSource.CAMERA);
                    touch1.setVisibility(View.INVISIBLE);
                    touch2.setVisibility(View.INVISIBLE);
                });

        // Draw Hands
        position = new PositionImage();
        Img1 = virtualView.findViewById(R.id.img1);
        Img2 = virtualView.findViewById(R.id.img2);

        Steeringwheell1 = virtualView.findViewById(R.id.steer1);
        Steeringwheell1.setScaleType(ImageView.ScaleType.MATRIX);
        Steeringwheell2 = virtualView.findViewById(R.id.steer2);
        Steeringwheell = virtualView.findViewById(R.id.steer);

        imgButtonLanding1 = getActivity().findViewById(R.id.imgbtn_landing1);
        imgButtonLanding2 = getActivity().findViewById(R.id.imgbtn_landing2);
        imgButtonTakeoff1 = getActivity().findViewById(R.id.imgbtn_takeoff1);
        imgButtonTakeoff2 = getActivity().findViewById(R.id.imgbtn_takeoff2);

        imgButtonLanding1.setVisibility(View.VISIBLE);
        imgButtonLanding2.setVisibility(View.VISIBLE);
        imgButtonTakeoff1.setVisibility(View.VISIBLE);
        imgButtonTakeoff2.setVisibility(View.VISIBLE);

        imgButtonLanding1.setX(200);
        imgButtonLanding1.setY((float) (heightx*0.7));

        imgButtonLanding2.setX(200);
        imgButtonLanding2.setY((float) (heightx*0.7));

        imgButtonTakeoff1.setX(widthx - 350);
        imgButtonTakeoff1.setY((float) (heightx*0.7));

        imgButtonTakeoff2.setX(widthx - 350);
        imgButtonTakeoff2.setY((float) (heightx*0.7));

        //Draw on top Frame
        relativeLayout1 = (RelativeLayout) virtualView.findViewById(R.id.container1);
        relativeLayout2 = (RelativeLayout) virtualView.findViewById(R.id.container2);
        drawHelpController1 = (RelativeLayout) virtualView.findViewById(R.id.draw_help_controller1);
        drawHelpController2 = (RelativeLayout) virtualView.findViewById(R.id.draw_help_controller2);

        mapexpand1.setOnClickListener(
                v -> {
                    setMaxMapLayout();
                    setMinStreamLayout();
                    mapexpand1.setVisibility(View.INVISIBLE);
                    mapexpand2.setVisibility(View.INVISIBLE);
                    streamexpand1.setVisibility(View.VISIBLE);
                    streamexpand2.setVisibility(View.VISIBLE);
                });

        mapexpand2.setOnClickListener(
                v -> {
                    setMaxMapLayout();
                    setMinStreamLayout();
                    mapexpand1.setVisibility(View.INVISIBLE);
                    mapexpand2.setVisibility(View.INVISIBLE);
                    streamexpand1.setVisibility(View.VISIBLE);
                    streamexpand2.setVisibility(View.VISIBLE);
                });

        streamexpand1.setOnClickListener(
                v -> {
                    setMaxStreamLayout();
                    setMinMapLayout();
                    mapexpand1.setVisibility(View.VISIBLE);
                    mapexpand2.setVisibility(View.VISIBLE);
                    streamexpand1.setVisibility(View.INVISIBLE);
                    streamexpand2.setVisibility(View.INVISIBLE);

                });

        streamexpand2.setOnClickListener(
                v -> {
                    setMaxStreamLayout();
                    setMinMapLayout();
                    mapexpand1.setVisibility(View.VISIBLE);
                    mapexpand2.setVisibility(View.VISIBLE);
                    streamexpand1.setVisibility(View.INVISIBLE);
                    streamexpand2.setVisibility(View.INVISIBLE);
                });

        setMaxStreamLayout();
        setMinMapLayout();
        drawHelpController1.addView(new DrawHelpController(getActivity()));
        drawHelpController2.addView(new DrawHelpController(getActivity()));

        //Data control
        dataControl = new DataControl();
        roboControl = new RoboControl();
        return virtualView;
    }

    /*
    ---------------------------- Perspective Steer Design -------------------------------------------
     */

    private void setPerspective(double deep){
        double soDeep = deep/5000;
        double zero2toZero4 = soDeep + 0.2;
        double zero8toZero6 = 0.8 - soDeep;

        double zero2toZero = 0.2 + soDeep;
        double zero8to1 = soDeep + 1;

        Bitmap transformed = null;

        // Perspective Steer control
        Steeringwheell.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) Steeringwheell.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        int w = bitmap.getWidth(), h = bitmap.getHeight();

        float[] src = {
                0 , 0, // Coordinate of top left point
                0 , h, // Coordinate of bottom left point
                w , h, // Coordinate of bottom right point
                w , 0  // Coordinate of top right point
        };

        float[] dst_testMax = {
                0, 0,      // Desired coordinate of top left point
                w * 0.2f, h * 0.8f,      // Desired coordinate of bottom left point
                w * 0.8f, h * 0.8f,      // Desired coordinate of bottom right point
                w, 0       // Desired coordinate of top right point
        };

        float[] dst_testMin = {
                w * 0.4f, h * 0.4f,      // Desired coordinate of top left point
                w * 0.2f, h * 0.8f,      // Desired coordinate of bottom left point
                w * 0.8f, h * 0.8f,      // Desired coordinate of bottom right point
                w * 0.6f, h * 0.4f       // Desired coordinate of top right point
        };

        float[] dst_default = {
                w * 0.2f, h * 0.2f,      // Desired coordinate of top left point
                w * 0.2f, h * 0.8f,      // Desired coordinate of bottom left point
                w * 0.8f, h * 0.8f,      // Desired coordinate of bottom right point
                w * 0.8f, h * 0.2f       // Desired coordinate of top right point
        };

        float[] dst_zoom = {
                (float) (w * zero2toZero), (float) (h * zero2toZero),        // Desired coordinate of top left point
                (float) (w * zero2toZero), (float) (h * zero8to1),        // Desired coordinate of bottom left point
                (float) (w * zero8to1), (float) (h * zero8to1),      // Desired coordinate of bottom right point
                (float) (w * zero8to1), (float) (h * zero2toZero)  // Desired coordinate of top right point
        };

        float[] dst_out = {
                (float) (w * zero2toZero4), (float) (h * zero2toZero4),        // Desired coordinate of top left point
                (float) (w * zero2toZero4), (float) (h * zero8toZero6),        // Desired coordinate of bottom left point
                (float) (w * zero8toZero6), (float) (h * zero8toZero6),      // Desired coordinate of bottom right point
                (float) (w * zero8toZero6), (float) (h * zero2toZero4)  // Desired coordinate of top right point
        };

        if(dataControl.z == 250){
            transformed = cornerPin(bitmap, src, dst_testMin);
        }else if(dataControl.z == -250){
            transformed = cornerPin(bitmap, src, dst_testMax);
        }else {
            transformed = cornerPin(bitmap, src, dst_default);
            Steeringwheell1.setRotation((float)position.angle);
            Steeringwheell2.setRotation((float)position.angle);
        }

        Steeringwheell2.setImageBitmap(transformed);
        Steeringwheell1.setImageBitmap(transformed);
        Steeringwheell1.setVisibility(View.VISIBLE);
        Steeringwheell2.setVisibility(View.VISIBLE);

//        if(dataControl.z == 0){
//            if(dataControl.x < -150){
//                Steeringwheell1.setX((float) (widthx*0.1));
//                Steeringwheell2.setX((float) (widthx*1.1));
//            }else if (dataControl.x > 150){
//                Steeringwheell1.setX((float) (widthx*0.8));
//                Steeringwheell2.setX((float) (widthx*1.8));
//            }else{
//                Steeringwheell1.setX((float) (widthx*0.5));
//                Steeringwheell2.setX((float) (widthx*1.5));
//            }
//        }

        Steeringwheell1.setY(heightx - dpToPx(120));
        Steeringwheell2.setY(heightx - dpToPx(120));

    }

    /* ------------------------- Design Switch Stream and Map Screen on VR ------------------------- */

    private void setMinMapLayout(){
        params2 = new RelativeLayout.LayoutParams(widthx/4, heightx/5);
//        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        map_frame_front1.setLayoutParams(params2);
        map_frame_front1.setX((float) (widthx/3 + 45));
        map_frame_front1.setY(heightx - 220);

        map_frame_front2.setLayoutParams(params2);
        map_frame_front2.setX((float) (widthx*4/3 + 45));
        map_frame_front2.setY(heightx - 220);

        if (getParentFragmentManager().findFragmentByTag("minmap1") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("minmap1")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.map_frame_front1, new MapFragment(), "minmap1").commit();
        }

        if (getParentFragmentManager().findFragmentByTag("minmap2") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("minmap2")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.map_frame_front2, new MapFragment(), "minmap2").commit();
        }
    }

    private void setMaxMapLayout(){

        params2 = new RelativeLayout.LayoutParams(widthx, heightx);
        map_frame_back1.setLayoutParams(params2);
        map_frame_back2.setLayoutParams(params2);
        map_frame_back2.setX(widthx);

        if (getParentFragmentManager().findFragmentByTag("maxmap1") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("maxmap1")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.map_frame_back1, new MapFragment(), "maxmap1").commit();
        }

        if (getParentFragmentManager().findFragmentByTag("maxmap2") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("maxmap2")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.map_frame_back2, new MapFragment(), "maxmap2").commit();
        }

    }

    private void setMinStreamLayout(){

        params1 = new RelativeLayout.LayoutParams(dpToPx(150), dpToPx(100));
        params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        cam_frame1.setLayoutParams(params1);
        cam_frame2.setLayoutParams(params1);
        cam_frame2.setX(widthx);

        if (getParentFragmentManager().findFragmentByTag("minmap1") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().hide(getParentFragmentManager().findFragmentByTag("minmap1")).commit();
        }
        if (getParentFragmentManager().findFragmentByTag("minmap2") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().hide(getParentFragmentManager().findFragmentByTag("minmap2")).commit();
        }

        if (getParentFragmentManager().findFragmentByTag("minstream1") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("minstream1")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.cam_frame1, new CamFragment(), "minstream1").commit();
        }

        if (getParentFragmentManager().findFragmentByTag("minstream2") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("minstream2")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.cam_frame2, new CamFragment(), "minstream2").commit();
        }
    }

    private void setMaxStreamLayout(){
        params1 = new RelativeLayout.LayoutParams(widthx, heightx);

        cam_frame1.setLayoutParams(params1);
        cam_frame2.setLayoutParams(params1);
        cam_frame2.setX(widthx);

        if (getParentFragmentManager().findFragmentByTag("maxstream1") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("maxstream1")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.cam_frame1, new CamFragment(), "maxstream1").commit();
        }

        if (getParentFragmentManager().findFragmentByTag("maxstream2") != null) {
            //if the fragment exists, show it.
            getParentFragmentManager().beginTransaction().show(getParentFragmentManager().findFragmentByTag("maxstream2")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            getParentFragmentManager().beginTransaction().add(R.id.cam_frame2, new CamFragment(), "maxstream2").commit();
        }

    }

    private void changeLayout(){
        //getParentFragmentManager().findFragmentByTag("minmap1") != null
        if(cam_frame1.getWidth() < widthx){
            setMinMapLayout();
            setMaxStreamLayout();

        }else{
            setMaxMapLayout();
            setMinStreamLayout();
        }
    }

    // dp to pixel for design
    public int dpToPx(int dp) {
        return Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /* ------------------------- Detect Hands and Draw ------------------------- */
    private void setupStreamingModePipeline(InputSource inputSource) {
        this.inputSource = inputSource;
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands =
                new Hands(
                        getContext(),
                        HandsOptions.builder()
                                .setStaticImageMode(false)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build());

        hands.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Hands error:" + message));

        if (inputSource == InputSource.CAMERA) {
            cameraInput = new CameraInput(getActivity());
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));

        }

        glSurfaceView = new SolutionGlSurfaceView<>(getContext(), hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView2 = new SolutionGlSurfaceView<>(getContext(), hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new HandsResultGlRenderer());
        glSurfaceView2.setSolutionResultRenderer(new HandsResultGlRenderer());


        //Background view on app - default = false
        glSurfaceView.setRenderInputImage(false);
        glSurfaceView2.setRenderInputImage(false);

        hands.setResultListener(
                handsResult -> {
                    logWristLandmark(handsResult,false);
                    glSurfaceView.setRenderData(handsResult);
                    glSurfaceView.requestRender();
                    glSurfaceView2.setRenderData(handsResult);
                    glSurfaceView2.requestRender();


                    int numHands = handsResult.multiHandLandmarks().size();

                    if(numHands>1){
                        isRightHand = handsResult.multiHandedness().get(0).getLabel().equals("Left");
                        if(isRightHand){
                            detect_res=twoHandDetect(handsResult.multiHandLandmarks().get(1).getLandmarkList()
                                    ,handsResult.multiHandLandmarks().get(0).getLandmarkList());
                        }else{
                            detect_res=twoHandDetect(handsResult.multiHandLandmarks().get(0).getLandmarkList()
                                    ,handsResult.multiHandLandmarks().get(1).getLandmarkList());
                        }
                        convertDataControlUav();
                        sendDataControl();
                        String code = setDataControl();
                        detect_res = code;
                        detect_mode = "UAV Control";

                        isTwoHands = true;
                        isOneHand = false;
                    }
                    else if(numHands == 1){
                        sendDataControlDefault();
                        for (int i = 0; i < numHands; ++i) {
                            isRightHand = handsResult.multiHandedness().get(i).getLabel().equals("Left");
                            String code=baseHandCount(handsResult.multiHandLandmarks().get(i).getLandmarkList());

                            detect_res = code;
                            detect_mode = "Robo Control";

                            setDataOneHandDetected(isRightHand);
                            convertDataControlRobo();
                            sendDataRoboControl();
                            isTwoHands = false;
                            isOneHand = true;

                        }
                    }else{
                        detect_mode = "UAV Hold";
                        sendDataControlDefault();
                        setDataNoHandDetected();
                    }

//                    DateTimeFormatter dtf = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                        LocalDateTime now = LocalDateTime.now();
//                        System.out.println("Hand Detected at: " + dtf.format(now));
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        System.out.println("Hand Detected With GPU at: " + LocalTime.now());
                    }


                    new MessageShow().start();
                });

        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.post(this::startCamera);
            glSurfaceView2.post(this::startCamera);
        }

        // Updates the preview layout.
        frameLayout1.addView(glSurfaceView);
        frameLayout2.addView(glSurfaceView2);

        //show hands detect
        glSurfaceView.setVisibility(View.INVISIBLE);
        glSurfaceView2.setVisibility(View.INVISIBLE);
        frameLayout1.requestLayout();
        frameLayout2.requestLayout();

    }
    private void startCamera() {
        cameraInput.start(
                getActivity(),
                hands.getGlContext(),
                CameraInput.CameraFacing.BACK,
                glSurfaceView.getWidth(),
                glSurfaceView.getHeight());
    }

    //----------------------------Hands Detection Process-----------------------------------------//
    private void stopCurrentPipeline() {
        if (cameraInput != null) {
            cameraInput.setNewFrameListener(null);
            cameraInput.close();
        }
        if (glSurfaceView != null) {
            glSurfaceView.setVisibility(View.INVISIBLE);
        }
        if (hands != null) {
            hands.close();
        }
    }

    private void logWristLandmark(HandsResult result, boolean showPixelValues) {
        if (result.multiHandLandmarks().isEmpty()) {
            return;
        }
        LandmarkProto.NormalizedLandmark wristLandmark =
                result.multiHandLandmarks().get(0).getLandmarkList().get(HandLandmark.INDEX_FINGER_TIP);
        // For Bitmaps, show the pixel values. For texture inputs, show the normalized coordinates.
        if (showPixelValues) {
            int widthz = result.inputBitmap().getWidth();
            int heightz = result.inputBitmap().getHeight();

//            Log.i(
//                    TAG,
//                    String.format(
//                            "MediaPipe Hand wrist coordinates (pixel values): x=%f, y=%f",
//                            wristLandmark.getX(), wristLandmark.getY()));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                System.out.println("Draw hand and pipline With GPU at: " + LocalTime.now());
            }
//            Log.i(
//                    TAG,
//                    String.format(
//                            "Icon coordinates: iconx=%f, icony=%f",
//                            mapexpand1.getX(), mapexpand1.getY()));
//            Log.i(
//                    TAG,
//                    String.format(
//                            "Hands coordinates: Handx=%f, Handy=%f, Handz=%f",
//                            position.lhposx[2], position.lhposy[2], position.lhposz[2]));
//            Log.i(
//                    TAG,
//                    String.format(
//                            "Hands array: Handx=%f, Handy=%f",
//                            position.lhposx[0], position.lhposy[0]));

        }
        if (result.multiHandWorldLandmarks().isEmpty()) {
            return;
        }

        LandmarkProto.Landmark wristWorldLandmark =
                result.multiHandWorldLandmarks().get(0).getLandmarkList().get(HandLandmark.THUMB_CMC);
//        Log.i(
//                TAG,
//                String.format(
//                        "MediaPipe Hand wrist world coordinates (in meters with the origin at the hand's"
//                                + " approximate geometric center): x=%f m, y=%f m, z=%f m",
//                        wristWorldLandmark.getX(), wristWorldLandmark.getY(), wristWorldLandmark.getZ()));


    }

    private String baseHandCount(List<LandmarkProto.NormalizedLandmark> handLandmarkList){

        String code="";

        if(isRightHand){
            for (int i = 0; i < 21; i++){
                position.rhposx[i] =  (1 - handLandmarkList.get(i).getX())*1200;
                position.rhposy[i] =  handLandmarkList.get(i).getY()*1200;
                position.rhposz[i] =  handLandmarkList.get(i).getZ()*1200 + 500;
            }
//            if(checkClick()){
//                code = "click";
//            }
        }
        else {
            for (int i = 0; i < 21; i++){
                position.lhposx[i] =  (1 - handLandmarkList.get(i).getX())*1200;
                position.lhposy[i] =  handLandmarkList.get(i).getY()*1200;
                position.lhposz[i] =  handLandmarkList.get(i).getZ()*1200 + 500;
            }
//            if(checkClick()){
//                code = "click";
//            }
        }

        return code;
    }

    private boolean checkClick(){
        float defx = mapexpand1.getY();
        float defy = mapexpand1.getX();
        double defz = 0.08;

        if(position.lhposx[8] < defx + 50 && position.lhposx[8] > defx - 50 && position.lhposy[8] > defy - 50 && position.lhposy[8] < defy + 50 && position.lhposz[8] > defz){
            return true;
        }
        return false;
    }

    // Perspective Steer
    public static Bitmap cornerPin(Bitmap b, float[] srcPoints, float[] dstPoints) {
        int w = b.getWidth(), h = b.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas c = new Canvas(result);
        Matrix m = new Matrix();
        m.setPolyToPoly(srcPoints, 0, dstPoints, 0, 4);
        c.drawBitmap(b, m, p);
        return result;
    }

    private String twoHandDetect(List<LandmarkProto.NormalizedLandmark> landmark1, List<LandmarkProto.NormalizedLandmark> landmark2){

        //function get data landmark hands detect
        for (int i = 0; i < 21; i++){
            position.lhposx[i] =  (1 - landmark1.get(i).getX())*1200;
            position.lhposy[i] =  landmark1.get(i).getY()*1200;
            position.lhposz[i] =  landmark1.get(i).getZ()*1200;

            position.rhposx[i] =  (1 - landmark2.get(i).getX())*1200;
            position.rhposy[i] =  landmark2.get(i).getY()*1200;
            position.rhposz[i] =  landmark2.get(i).getZ()*1200;

            position.middlex[i] = (2 - landmark1.get(i).getX() - landmark2.get(i).getX())*600;
            position.middley[i] = (landmark1.get(i).getY() + landmark2.get(i).getY())*600;
            position.middlez[i] = (landmark1.get(i).getZ() + landmark2.get(i).getZ())*2400;
        }

        float hand1x0 = 1 - landmark1.get(4).getX();
        float hand1y0 = landmark1.get(4).getY();


        float hand2x0 = 1 - landmark2.get(4).getX();
        float hand2y0 = landmark2.get(4).getY();

        float pixHand1x0 = hand1x0*1200;
        float pixHand1y0 = hand1y0*1200;

        float pixHand2x0 = hand2x0*1200;
        float pixHand2y0 = hand2y0*1200;


        float offset_hand12x = Math.abs(pixHand2x0 - pixHand1x0);
        float offset_hand12y = Math.abs(pixHand2y0 - pixHand1y0);

        double tanAngle_h1h2 = offset_hand12y/offset_hand12x;

        double angle = ((Math.atan(tanAngle_h1h2))/pi)*180 - 90;

        if (pixHand1x0 > pixHand2x0){
            position.angle = angle;
        }else{
            position.angle = -angle;
        }

        return "none";
    }

    private void showHands(){
        if(isTwoHands){
            displayDataControl(detect_mode);
        }else{
            displayModeInfor(detect_mode, detect_res);
        }
        relativeLayout1.addView(new Circle(getActivity()));
        relativeLayout2.addView(new Circle(getActivity()));
        restartDrawLayout();


//        setPerspective(dataControl.z);
    }

    private void setDataNoHandDetected(){
        for (int i = 0; i < 21; i++){
            position.lhposx[i] = 0;
            position.lhposy[i] = 0;
            position.lhposz[i] = 0;

            position.rhposx[i] = 0;
            position.rhposy[i] = 0;
            position.rhposz[i] = 0;

            position.angle = 0;
            position.middlex[i] = 0;
            position.middley[i] = 0;
            position.middlez[i] = 0;
        }
    }

    private void setDataOneHandDetected(boolean checkRight){
        for (int i = 0; i < 21; i++){
            position.angle = 0;
            position.middlex[i] = 0;
            position.middley[i] = 0;
            position.middlez[i] = 0;
        }
        if(checkRight){
            for (int i = 0; i < 21; i++){
                position.lhposx[i] = 0;
                position.lhposy[i] = 0;
                position.lhposz[i] = 0;
            }
        }else{
            for (int i = 0; i < 21; i++){
                position.rhposx[i] = 0;
                position.rhposy[i] = 0;
                position.rhposz[i] = 0;
            }
        }

    }

    private void restartDrawLayout(){
        relativeLayout1.removeAllViewsInLayout();
        relativeLayout2.removeAllViewsInLayout();
        relativeLayout1.addView(new Circle(getActivity()));
        relativeLayout2.addView(new Circle(getActivity()));
    }

    private void displayModeInfor(String mode, String res){
        lvx1.setText("Open: " + roboControl.open_angle);
        lvy1.setText("Lift: " + roboControl.lift_angle);
        lvz1.setText("Rot: " + roboControl.rot_angle);

        lvx2.setText("Open: " + roboControl.open_angle);
        lvy2.setText("Lift: " + roboControl.lift_angle);
        lvz2.setText("Rot: " + roboControl.rot_angle);

//        angle1.setText("rot: " + dataControl.r);
//        angle2.setText("rot: " + dataControl.r);

        mode1.setText("Mode: " + mode);
        mode2.setText("Mode: " + mode);

        state1.setText("State: ");
        state2.setText("State: ");
    }

    class MessageShow extends Thread{

        @Override
        public void run() {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                     if("click".equals(detect_res)){
                        sum+=1;
                        Img1.setImageResource(R.drawable.radar);
                        Img1.setVisibility(View.VISIBLE);
                        Img2.setImageResource(R.drawable.radar);
                        Img2.setVisibility(View.VISIBLE);
                        showHands();
                        if (sum == 5){
                            changeLayout();
                            sum = 0;
                        }
                    }
                    else{
                        Img1.setVisibility(View.INVISIBLE);
                        Img2.setVisibility(View.INVISIBLE);
                        showHands();
                    }
                }
            };
            uiHandler.post(runnable);
        }
    }



    //---------------------------- Data Control Process-----------------------------------------//

    private void displayDataControl(String mode){
        lvx1.setText("r/l: " + dataControl.x);
        lvy1.setText("u/d: " + dataControl.y);
        lvz1.setText("f/b: " + dataControl.z);

        lvx2.setText("r/l: " + dataControl.x);
        lvy2.setText("u/d: " + dataControl.y);
        lvz2.setText("f/b: " + dataControl.z);

        angle1.setText("rot: " + dataControl.r);
        angle2.setText("rot: " + dataControl.r);

        mode1.setText("Mode: " + mode);
        mode2.setText("Mode: " + mode);
    }

    //Change Mode: Steer/Robo Arm
    private void convertDataControlRobo(){
        int convert_rot;
        float convert_move;
        int convert_lift;
        int convert_open;
        boolean check_open = false, check_lift = false, check_rot = false, check_move = false;

        float root0y = (float) heightx/2;
        float root0x = (float) widthx/2;
        float offset_hand12x;
        float offset_hand12y;
        double tanAngle_h1h2;
        double angleX;
        float handX4 = 0, handY4 = 0, handX8 = 0, handY8 = 0;
        double distance_4_to_8;
        float middle_point_y;

        if(isRightHand){
            handX4 = position.rhposy[8];
            handX8 = position.rhposy[4];
            handY4 = position.rhposx[8];
            handY8 = position.rhposx[4];
        }
        else {
            handX4 = position.lhposy[4];
            handX8 = position.lhposy[8];
            handY4 = position.lhposx[4];
            handY8 = position.lhposx[8];
        }

        distance_4_to_8 = Math.sqrt((handX4 - handX8)*(handX4 - handX8) + (handY4 - handY8)*(handY4 - handY8));
        middle_point_y = (handY4 + handY8)/2;
        convert_open = (int)(distance_4_to_8*0.45 - 135);
        convert_lift = (int)(middle_point_y/4);
        convert_move = (handX4 + handX8)/2;

        offset_hand12x = Math.abs(handY4 - handY8);
        offset_hand12y = Math.abs(handX4 - handX8);
        tanAngle_h1h2 = offset_hand12y/offset_hand12x;
        angleX = ((Math.atan(tanAngle_h1h2))/pi)*180 - 90;

        if (handY4 > handY8){
            convert_rot = (int) angleX;
        }else{
            convert_rot = (int) -angleX;
        }

        if(handY4 > root0y - 100 && handY4 < root0y + 100 && handY8 > root0y - 100 && handY8 < root0y + 100 &&
                handX4 > root0x && handX8 < root0x){
            check_open = true;
        }

        if(handY4 <= 360 && handY4 > 0 && handY8 <= 360 && handY8 > 0 && handX8 > root0x - 100 && handX4 < root0x + 100 || handY4 < heightx && handY4 >= 720 && handY8 < heightx && handY8 >= 720 &&
                handX8 > root0x - 100 && handX4 < root0x + 100){
            check_lift = true;
        }

        if((handY4 > root0y - 100) && (handY4 < root0y + 100) && (handY8 > root0y - 100) && (handY8 < root0y + 100)){
            if(handX4 < root0x - 100 && handX8 < root0x - 100 || handX4 > root0x + 100 && handX8 > root0x + 100){
                check_move = true;
            }
        }


        //Open Angle: 1 - open, 0 - close, -1 - null
        if(check_open){
            if(convert_open > 90){
                roboControl.open_angle = 1;
            }else if(convert_open < -90){
                roboControl.open_angle = 0;
            }else if (convert_open >= 70 && convert_open <= 90){
                roboControl.open_angle = 1;
            }else if (convert_open >= -90 && convert_open <= -70){
                roboControl.open_angle = 0;
            }else {
                roboControl.open_angle = 0;
            }
        }else{
            roboControl.open_angle = 0;
        }

        if(check_lift){
            if(middle_point_y > 720){
                roboControl.lift_angle = (int) Math.abs((-(convert_lift - 270))/2 );
                //roboControl.lift_angle = -(convert_lift - 180);
            }else if(middle_point_y < 320){
                //roboControl.lift_angle = Math.abs(convert_lift - 90) + 80;
                roboControl.lift_angle = (int)Math.abs((Math.abs(convert_lift - 90) + 80)/2 );
                //roboControl.lift_angle = Math.abs(convert_lift - 90);
            }
        }

        if(check_move){
            if(convert_move < root0x){
                roboControl.rot_angle = (int) (((convert_move - 150)*0.36) + 10)/2;
            }else if(convert_move >= root0x){
                roboControl.rot_angle = (int) ((convert_move)/14);
            }
        }
    }
    private String setDataControl(){
        String code = "";
//        if (dataControl.x >= 500 )
        return code;
    }

    private void convertDataControlUav(){
        double step = 5/3;
        int convertX = (int)((position.middley[4] - 50)*step) - 500;
        int convertY = Math.abs((int)((position.middlex[4] - 50)*step) - 1000);
        double convertZ =(int) position.middlez[4] + 700;
        int convertR = (int) position.angle * 10;

        boolean checkX = false;
        boolean checkY = false;
        boolean checkZ = false;
        boolean checkR = true;

        if(convertX < 150 && convertX > -150 && convertY > 350 && convertY < 650 && convertR < 200 && convertR > -200){
            checkZ = true;
        }

        if (convertX >= -150 && convertX <= 150 && convertR < 200 && convertR > -200){
            checkY = true;
        }

        if (convertY >= 350 && convertY <= 650 && convertR < 200 && convertR > -200){
            checkX = true;
        }

        if(checkY){
            if(convertY > 900){
                dataControl.y = 750;
            }else if(convertY >= 650){
                dataControl.y = (convertY - 150);
            }else if(convertY <= 350){
                dataControl.y = (convertY) + 150;
                //sendDataRoboControlDefault();
            }else {
                dataControl.y = 500;
            }
        }else{
            dataControl.y = 500;
        }

        if(checkX){
            if(convertX > 400){
                dataControl.x = 500;
            }else if(convertX >= 150 && convertX <= 400){
                dataControl.x = (convertX - 150)*2;
            }else if(convertX <= -150 && convertX >= -400){
                dataControl.x = (convertX + 150)*2;
            }else if(convertX < -400){
                dataControl.x = -500;
            }else {
                dataControl.x = 0;
            }
        }else {
            dataControl.x = 0;
        }

        if(checkZ){
            if (convertZ < -500){
                dataControl.z = -300;
            }else if(convertZ > 500){
                dataControl.z = 300;
            }else if(convertZ >= -500 && convertZ <= -200){
                dataControl.z = (int) convertZ + 200;
            }else if(convertZ >= 200 && convertZ <= 500){
                dataControl.z = (int) convertZ - 200;
            }else {
                dataControl.z = 0;
            }
        }else {
            dataControl.z = 0;
        }

        if(checkR){
            if(convertR < -600){
                dataControl.r = -300;
            }else if(convertR > 600 ){
                dataControl.r = 300;
            }else if(convertR <= -300){
                dataControl.r = convertR + 300;
            }else if(convertR >= 300){
                dataControl.r = convertR - 300;
            }else{
                dataControl.r = 0;
            }

        }else {
            dataControl.r = 0;
        }
    }

    private void sendDataControl(){
        DataHolder.getInstance().setControlMode(1);
        DataHolder.getInstance().setVy(dataControl.x);
        DataHolder.getInstance().setVx(dataControl.z);
        DataHolder.getInstance().setVz(dataControl.y);
        DataHolder.getInstance().setYawRate(dataControl.r);

//        Uavlink_msg_manual_control_t manualControl = new Uavlink_msg_manual_control_t();
//        manualControl.Encode(dataControl.z, dataControl.x, dataControl.y, dataControl.r);
//        DataHolder.getInstance().putSendQueue(manualControl.getData());
    }

    private void sendDataControlDefault(){
        dataControl.x = 0;
        dataControl.y = 500;
        dataControl.z = 0;
        dataControl.r = 0;
        sendDataControl();
    }

    private void sendDataRoboControl() {
        DataHolder.getInstance().setControlMode(2);
        DataHolder.getInstance().setRobotAimServo(
                roboControl.open_angle,
                roboControl.lift_angle,
                roboControl.rot_angle);
    }

    private void sendDataRoboControlDefault() {
        roboControl.rot_angle = 0;
        roboControl.lift_angle = 0;
        roboControl.open_angle = 0;
        sendDataRoboControl();
    }
}