package com.velozion.myoitc.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile_Activity extends AppCompatActivity {

    ImageView back,pic;
    TextView name,desc;
    Button edit;

    Animation animation;
    HashMap<String,String> Profile=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);


        back=(ImageView)findViewById(R.id.profile_back);
        pic=(ImageView)findViewById(R.id.profile_pic);
        name=(TextView) findViewById(R.id.profile_username);
        desc=(TextView) findViewById(R.id.profile_desc);
        edit=(Button) findViewById(R.id.profile_edit);

        pic.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        LoadProfile();





    }

    private void LoadProfile() {
        Utils.displayCustomDailog(Profile_Activity.this);

        Map<String,String> headers = new HashMap<>();
        String credentials = PreferenceUtil.getData("username",getApplicationContext())+":"+PreferenceUtil.getData("password",getApplicationContext());
        String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userid",PreferenceUtil.getData("userid",getApplicationContext()));


        Log.d( "RespondedData",jsonParams.toString()+" headers: \n"+headers);


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        CustomRequest customRequest = new CustomRequest( Request.Method.POST,Utils.ProfileApi, jsonParams,headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d( "ResponseS",response.toString() );
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")){

                                Utils.dismissCustomDailog();

                                if (response.getJSONObject("data")!=null)//sucess
                                {

                                    JSONObject object=response.getJSONObject("data");

                                    Profile.put("id",object.getString("id"));
                                    Profile.put("name",object.getString("name"));
                                    Profile.put("username",object.getString("username"));
                                    Profile.put("email",object.getString("email"));


                                    name.setText(""+Profile.get("name"));
                                    desc.setText(""+Profile.get("email"));

                                    pic.setVisibility(View.VISIBLE);


                                    AllowUx();


                                }else {

                                    String msg=response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
                                }




                            }
                            else{

                                Utils.dismissCustomDailog();
                                Toast.makeText(getApplicationContext(), ""+response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Utils.dismissCustomDailog();
                            Toast.makeText(getApplicationContext(), "Json Error:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "ResponseE",error.toString() );

                        Utils.dismissCustomDailog();
                        Toast.makeText(getApplicationContext(), "Volley Error:\n"+error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } );
        requestQueue.add(customRequest);
    }

    void AllowUx(){



        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_down_to_up);

        pic.setAnimation(animation);

        name.setVisibility(View.GONE);
        desc.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                name.setVisibility(View.VISIBLE);
                name.setAnimation(animation);


            }
        }, 1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                desc.setVisibility(View.VISIBLE);
                desc.setAnimation(animation);

            }
        }, 2000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edit.setVisibility(View.VISIBLE);
                edit.setAnimation(animation);
            }
        }, 3000);

    }


}
