package com.example.cps_lab411;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cps_lab411.Category.Category;
import com.example.cps_lab411.Category.CategoryAdapter;
import com.example.cps_lab411.Communication.UDP.UDPConnection;
import com.example.cps_lab411.EvenBus.ConntectHandleEvenbus;
import com.example.cps_lab411.EvenBus.UDPMessage2EvenBus;
import com.example.cps_lab411.EvenBus.UpdateGPS;
import com.example.cps_lab411.EvenBus.UpdateNavigationPath;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressLint("SetTextI18n")
public class ConnFragment  extends Fragment {

    Thread Thread1 = null;
    private EditText etIP, etPort;
    private TextView tvMessages;
    String SERVER_IP;
    int SERVER_PORT;
    String SERVER_PORT_CHECK;
    ImageView ConnectToUAV;
    private boolean DataCheck;
    private Spinner spnConnectionType;
    private CategoryAdapter categoryAdapter;
    private ImageView mUAVBattery;
    private ImageView mImvConnection, mImvSwControlMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View connView = inflater.inflate(R.layout.fragment_conn, container, false);

        // Set UDP as default connection type
        DataHolder.getInstance().setConnectionType(0);

        spnConnectionType = connView.findViewById(R.id.spn_connection);
        categoryAdapter = new CategoryAdapter(requireActivity(), R.layout.item_selected, getListCategory());
        spnConnectionType.setAdapter(categoryAdapter);
        spnConnectionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataHolder.getInstance().setConnectionType(categoryAdapter.getItem(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etIP = connView.findViewById(R.id.etIP);
        etIP.setText("192.168.0.122");
        etPort = connView.findViewById(R.id.etPort);
        etPort.setText("12345");
        tvMessages = connView.findViewById(R.id.tvMessages);
        Button btnConnect = connView.findViewById(R.id.btnConnect);
        Button btnDisconnect = connView.findViewById(R.id.btnDisconnect);


        ConnectToUAV = requireActivity().findViewById(R.id.flightmode_ic);


        ConnectToUAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        // Connection Button
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT_CHECK = etPort.getText().toString().trim();
                if(!SERVER_IP.isEmpty() && !SERVER_PORT_CHECK.isEmpty()) {
                    SERVER_PORT = Integer.parseInt(SERVER_PORT_CHECK);
                    if(DataHolder.getInstance().getConnectionStatus()) {
                        System.out.println("Device already connected!!");
                    } else {
                        switch (DataHolder.getInstance().getConnectionType()) {
                            case 0:
                                //UDP
                                UDPConnection UDP = new UDPConnection(SERVER_IP, SERVER_PORT);
                                UDP.initConnection();
                                break;
                            case 1:
                                /*  TCP Connection */
                                break;
                            case 2:
                                //Serial
                                break;
                        }
                    }
                }
            }
        });

        btnDisconnect.setOnClickListener(v -> {
            if(DataHolder.getInstance().getConnectionStatus()) {
                DataHolder.getInstance().setConnectionStatus(false);
                ConntectHandleEvenbus conntectHandleEvenbus = new ConntectHandleEvenbus();
                conntectHandleEvenbus.setConnectionState(false);
                EventBus.getDefault().post(conntectHandleEvenbus);
            }
        });
        return connView;
    }

    private List<Category> getListCategory() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("UDP Connection", 0));
        list.add(new Category("TCP Connection", 1));
        list.add(new Category("Serial Connection", 2));

        return list;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Registered EventBus
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //EventBus.getDefault().unregister(this);
    }

}