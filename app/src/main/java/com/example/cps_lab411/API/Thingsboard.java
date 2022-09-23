package com.example.cps_lab411.API;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cps_lab411.MainActivity;
import com.example.cps_lab411.MapFragment;
import com.example.cps_lab411.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Thingsboard extends Dialog implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String baseURL = "https://demo.thingsboard.io:443/api";
    private static final String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    private String authToken;

    private ListView listView;
    private Button btnClose;
    private Context context;

    public Thingsboard(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_thingsboard);
        listView = findViewById(R.id.lv_displaySensor);
        //btnClose = findViewById(R.id.btn_closeThingsboard);

        authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ3V5ZW5kaW5oaHVuZzE4MDRAZ21haWwuY29tIiwidXNlcklkIjoiZjlhNjk3ZjAtMzMyMy0xMWVkLWJhYTYtMzE2NzQ1ZGVlZjM1Iiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTY2MzYwMTY0NCwiZXhwIjoxNjY1NDAxNjQ0LCJmaXJzdE5hbWUiOiJIw7luZyIsImxhc3ROYW1lIjoiTmd1eeG7hW4gxJDDrG5oIiwiZW5hYmxlZCI6dHJ1ZSwicHJpdmFjeVBvbGljeUFjY2VwdGVkIjp0cnVlLCJpc1B1YmxpYyI6ZmFsc2UsInRlbmFudElkIjoiZjc5MTRjODAtMzMyMy0xMWVkLWJhYTYtMzE2NzQ1ZGVlZjM1IiwiY3VzdG9tZXJJZCI6IjEzODE0MDAwLTFkZDItMTFiMi04MDgwLTgwODA4MDgwODA4MCJ9.4f_1HgDHoAvehQhrFFAow8mU6mssfwoYzNNLh97zX2w2Vsj97QXlF9zp6GW-74TAMDhD3WGWR7f2RQXwBh8MyA";

        Runnable helloRunnable = new Runnable() {
            public void run() {
                addDataToScreen();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
    }

    private void addDataToScreen() {
        getTelemetryValuesOfADevice("DEVICE", "c96fd450-34ce-11ed-89c2-7b8e9c33fd73");
    }

    private void getTelemetryValuesOfADevice(String deviceType, String deviceId) {
        String URL = baseURL + "/plugins/telemetry/" + deviceType + "/" + deviceId + "/values/attributes/CLIENT_SCOPE";

        final List<DataSensorModel> dataSensorModels = new ArrayList<>();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++)
                        {
                            DataSensorModel dataSensorModel = new DataSensorModel();
                            try {
                                JSONObject data = response.getJSONObject(i);
                                dataSensorModel.setSensorName(data.getString("key"));
                                dataSensorModel.setSensorValue(data.getString("value"));
//                                dataSensorModel.setSensorName(data.getString("key"));
//                                dataSensorModel.setSensorValue(data.getString("value"));
                                dataSensorModels.add(dataSensorModel);


//                                strJSON += "\n\nName: " + sensorName + "\n";
//                                strJSON += "Value: " + sensorValue + "\n";

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, dataSensorModels);
                        listView.setAdapter(arrayAdapter);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + authToken);
                return headers;
            }
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.btn_closeThingsboard)
//        {
//            dismiss();
//        }
    }
}
