package com.velozion.myoitc.activities;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.bean.UserProfileBean;
import com.velozion.myoitc.databinding.ActivityProfile2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile2 extends BaseActivity {

    ActivityProfile2Binding binding;

    UserProfileBean userProfileBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarRequired(false);
        //setContentView(R.layout.activity_profile2);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile2);

        userProfileBean =new UserProfileBean(getApplicationContext());

        LoadProfile();



    }


    private void LoadProfile() {
        Utils.displayCustomDailog(Profile2.this);

        Map<String, String> headers = new HashMap<>();
        String credentials = PreferenceUtil.getData("username", getApplicationContext()) + ":" + PreferenceUtil.getData("password", getApplicationContext());
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("userid", PreferenceUtil.getData("userid", getApplicationContext()));


        Log.d("RespondedData", jsonParams.toString() + " headers: \n" + headers);


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Utils.ProfileAPI, jsonParams, headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResponseS", response.toString());
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")) {

                                Utils.dismissCustomDailog();

                                if (response.getJSONObject("data") != null)//sucess
                                {

                                    JSONObject object = response.getJSONObject("data");

                                    userProfileBean.setId(object.getString("id"));
                                    userProfileBean.setName(object.getString("name"));
                                    userProfileBean.setUsername(object.getString("username"));
                                    userProfileBean.setEmail(object.getString("email"));

                                    binding.setUser(userProfileBean);



                                } else {

                                    String msg = response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Utils.dismissCustomDailog();
                                Toast.makeText(getApplicationContext(), "" + response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Utils.dismissCustomDailog();
                            Toast.makeText(getApplicationContext(), "Json Error:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ResponseE", error.toString());

                        Utils.dismissCustomDailog();
                        Toast.makeText(getApplicationContext(), "Volley Error:\n" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(customRequest);
    }
}
