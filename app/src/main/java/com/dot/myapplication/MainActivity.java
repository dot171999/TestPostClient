package com.dot.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

import static com.dot.myapplication.Client.webSocketClient;

public class MainActivity extends AppCompatActivity {

    Button button;
    String ipAddress ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread=new Thread(new Client(MainActivity.this));
        thread.start();

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonBody = new JSONObject();
                Log.e("event","sending event on click");
                try {
                    jsonBody.put("event", "BlinkDetection-Sebastian");
                    jsonBody.put("value", "hello");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonBody.toString();
                webSocketClient.send(requestBody);
            }
        });
    }

    private void sendWorkPostRequest() {

        try {
            String URL = "http://"+ipAddress+":3000";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("event", "StartBlink");
            jsonBody.put("value", "hello");


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Resp",response+"");
                    Toast.makeText(getApplicationContext(), "Response:  " + response+"", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Err",error+"");
                    Toast.makeText(getApplicationContext(), "Error:  " + error+"", Toast.LENGTH_SHORT).show();
                }
            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    final Map<String, String> headers = new HashMap<>();
//                    headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
//                    return headers;
//                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendWorkPostRequest1() {
        //String URL = "http://localhost:3000";
        String URL = "http://"+ipAddress+":3000";
        StringRequest jsonArrayRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("InspectionImg Response", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private void sendWorkPostRequest2(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://"+ipAddress+":3000";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("event", "StartBlink");
            jsonBody.put("value", "hello");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
