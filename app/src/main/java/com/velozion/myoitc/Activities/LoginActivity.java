package com.velozion.myoitc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    EditText email, password;
    Button login;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_login);

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_down_to_up);
        //login.setAnimation(animation);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty()) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Email is Empty", Snackbar.LENGTH_LONG).show();
                } else if (password.getText().toString().isEmpty()) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Password is Empty", Snackbar.LENGTH_LONG).show();

                } else {

                    SignIn(email.getText().toString(), password.getText().toString());
                    //hit server
                }

            }
        });

    }

    private void SignIn(String username, String pass) {

        Utils.displayCustomDailog(LoginActivity.this);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("username", username);
        jsonParams.put("password", pass);

        Log.d("RespondedData", jsonParams.toString());


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Utils.LoginApi, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResponseS", response.toString());
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")) {


                                /*
                                 "data": {
        "status": 1,
        "username": "employer",
        "email": "employer@yahoo.com",
        "fullname": "employer",
        "user_id": "379"
    }
                                 */


                                Utils.dismissCustomDailog();

                                if (response.getJSONObject("data") != null)//sucess
                                {


                                    String msg = response.getJSONObject("messages").getJSONArray("success").get(0).toString();
                                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();


                                    JSONObject object = response.getJSONObject("data");
                                    PreferenceUtil.saveData("userid", object.getString("user_id"), getApplicationContext());
                                    PreferenceUtil.saveData("username", object.getString("username"), getApplicationContext());
                                    PreferenceUtil.saveData("password", password.getText().toString(), getApplicationContext());
                                    PreferenceUtil.saveData("email", object.getString("email"), getApplicationContext());
                                    PreferenceUtil.saveData("fullname", object.getString("fullname"), getApplicationContext());


                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    finish();


                                } else {

                                    String msg = response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Utils.dismissCustomDailog();
                                Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();

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
