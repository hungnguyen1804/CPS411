package com.example.cps_lab411;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cps_lab411.API.DataSensorModel;
import com.example.cps_lab411.API.VolleyController;

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


public class ThingsboardFragment extends Fragment {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String baseURL = "https://demo.thingsboard.io:443/api";
    private static final String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    private String authToken;

    private ListView listView;


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_thingsboard);
//        listView = findViewById(R.id.lv_displaySensor);
//
//        sharedPreferences = this.getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
//
//        authToken = sharedPreferences.getString(AUTH_TOKEN_KEY, "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ3V5ZW5kaW5oaHVuZzE4MDRAZ21haWwuY29tIiwidXNlcklkIjoiZjlhNjk3ZjAtMzMyMy0xMWVkLWJhYTYtMzE2NzQ1ZGVlZjM1Iiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTY2MzYwMTY0NCwiZXhwIjoxNjY1NDAxNjQ0LCJmaXJzdE5hbWUiOiJIw7luZyIsImxhc3ROYW1lIjoiTmd1eeG7hW4gxJDDrG5oIiwiZW5hYmxlZCI6dHJ1ZSwicHJpdmFjeVBvbGljeUFjY2VwdGVkIjp0cnVlLCJpc1B1YmxpYyI6ZmFsc2UsInRlbmFudElkIjoiZjc5MTRjODAtMzMyMy0xMWVkLWJhYTYtMzE2NzQ1ZGVlZjM1IiwiY3VzdG9tZXJJZCI6IjEzODE0MDAwLTFkZDItMTFiMi04MDgwLTgwODA4MDgwODA4MCJ9.4f_1HgDHoAvehQhrFFAow8mU6mssfwoYzNNLh97zX2w2Vsj97QXlF9zp6GW-74TAMDhD3WGWR7f2RQXwBh8MyA");
//
//        Runnable helloRunnable = new Runnable() {
//            public void run() {
//                addDataToScreen();
//            }
//        };
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thingsboard = inflater.inflate(R.layout.fragment_thingsboard, container, false);

        listView = thingsboard.findViewById(R.id.lv_displaySensor);
        authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ3V5ZW5kaW5oaHVuZzE4MDRAZ21haWwuY29tIiwidXNlcklkIjoiZjlhNjk3ZjAtMzMyMy0xMWVkLWJhYTYtMzE2NzQ1ZGVlZjM1Iiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTY2MzYwMTY0NCwiZXhwIjoxNjY1NDAxNjQ0LCJmaXJzdE5hbWUiOiJIw7luZyIsImxhc3ROYW1lIjoiTmd1eeG7hW4gxJDDrG5oIiwiZW5hYmxlZCI6dHJ1ZSwicHJpdmFjeVBvbGljeUFjY2VwdGVkIjp0cnVlLCJpc1B1YmxpYyI6ZmFsc2UsInRlbmFudElkIjoiZjc5MTRjODAtMzMyMy0xMWVkLWJhYTYtMzE2NzQ1ZGVlZjM1IiwiY3VzdG9tZXJJZCI6IjEzODE0MDAwLTFkZDItMTFiMi04MDgwLTgwODA4MDgwODA4MCJ9.4f_1HgDHoAvehQhrFFAow8mU6mssfwoYzNNLh97zX2w2Vsj97QXlF9zp6GW-74TAMDhD3WGWR7f2RQXwBh8MyA";

        Runnable helloRunnable = new Runnable() {
            public void run() {
                addDataToScreen();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
        // Inflate the layout for this fragment
        return thingsboard;
    }

    private void addDataToScreen() {
        getTelemetryValuesOfADevice("DEVICE", "c96fd450-34ce-11ed-89c2-7b8e9c33fd73");
    }

    private void getTelemetryValuesOfADevice(String deviceType, String deviceId) {
        String URL = baseURL + "/plugins/telemetry/" + deviceType + "/" + deviceId + "/values/attributes/CLIENT_SCOPE";

        final List<DataSensorModel> dataSensorModels = new ArrayList<>();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Method.GET, URL, null,
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
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, dataSensorModels);
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
        VolleyController.getInstance(getActivity().getApplicationContext()).addToQueue(jsonObjReq);
    }
}