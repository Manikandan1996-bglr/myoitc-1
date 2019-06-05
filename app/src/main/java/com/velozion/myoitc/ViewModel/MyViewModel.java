package com.velozion.myoitc.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.db.DoctorProfileData;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.db.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyViewModel extends ViewModel {

    public MutableLiveData<String> Succesmessage=new MutableLiveData<>();
   public MutableLiveData<String> Failuremessage=new MutableLiveData<>();
   public MutableLiveData<String> jsonError=new MutableLiveData<>();
   public MutableLiveData<String> volleyError=new MutableLiveData<>();

    public MutableLiveData<ArrayList<HistoryData>> HistoryList;
    public MutableLiveData<ArrayList<DoctorProfileData>> DoctersData;
    public MutableLiveData<HashMap<String,String>> Profile;




   public LiveData<ArrayList<HistoryData>> getHistoryList(final Context context){

        if (HistoryList==null)
        {
            HistoryList=new MutableLiveData<>();

            final ArrayList<HistoryData> Data=new ArrayList<>();


            Map<String,String> headers = new HashMap<>();
            String credentials = PreferenceUtil.getData("username",context)+":"+PreferenceUtil.getData("password",context);
            String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);

            Map<String, String> jsonParams = new HashMap<String, String>();


            RequestQueue requestQueue = Volley.newRequestQueue(context);

            CustomRequest customRequest = new CustomRequest( Request.Method.POST, Utils.HistoryApi, jsonParams,headers,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d( "ResponseS",response.toString() );
                            try {
                                if (response.getString("success").equalsIgnoreCase("true")){


                                    if (response.getJSONArray("data")!=null)//sucess
                                    {

                                    /*
                                    {
            "in_location": "bangalore",
            "out_location": "bangalore",
            "in_lat": "10.000000",
            "in_lang": "20.000000",
            "out_lat": "10.000000",
            "out_lang": "20.000000",
            "check_in": "2012-12-01 15:20:00",
            "check_out": "2012-12-01 16:20:00"
        }
                                     */


                                        JSONArray jsonArray=response.getJSONArray("data");

                                        for (int i=0;i<jsonArray.length();i++)
                                        {





                                            JSONObject object=jsonArray.getJSONObject(i);

                                            HistoryData historyData=new HistoryData(context);
                                            historyData.setCheckinloc(object.getString("in_location"));
                                            historyData.setCheckintime(object.getString("check_in"));
                                            historyData.setCheckinLat(object.getString("in_lat"));
                                            historyData.setCheckinLang(object.getString("in_lang"));

                                            historyData.setCheckoutloc(object.getString("out_location"));
                                            historyData.setCheckouttime(object.getString("check_out"));
                                            historyData.setCheckoutLan(object.getString("out_lat"));
                                            historyData.setCheckoutLang(object.getString("out_lang"));

                                            Data.add(historyData);

                                        }


                                        HistoryList.setValue(Data);

                                    }else {

                                        String msg=response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                       Failuremessage.setValue(msg);

                                    }




                                }
                                else{


                                    Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                   Failuremessage.setValue(response.getString("mesaage"));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                jsonError.postValue(e.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            volleyError.postValue(error.getMessage());
                        }
                    } );
            requestQueue.add(customRequest);

        }

        return HistoryList;
    }

    public LiveData<ArrayList<DoctorProfileData>> getDoctersList(final Context context){

        if (DoctersData==null)
        {
            DoctersData=new MutableLiveData<>();

            final ArrayList<DoctorProfileData> Data=new ArrayList<>();

            Map<String, String> jsonParams = new HashMap<String, String>();

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            CustomRequest customRequest = new CustomRequest( Request.Method.GET, Utils.DocterListApi, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d( "ResponseS",response.toString() );
                            try {
                              //  if (response.getString("success").equalsIgnoreCase("true")){

                                    if (response.getJSONArray("Sheet1")!=null)//sucess
                                    {
                                    /*
                                    "name":"Kumaranlakkur",
"pic":"https://imagevars.gulfnews.com/2019/05/21/Indian-captain-Virat-Kohli_16ada7d3e5d_large.jpg",
"qualification":"MBBS MS Mphil",
"specialist":"Cardiography",
"experience":"19 years",
"about":"Iam Kumaranwith an 14 years of expirence in cardiology",
"hospital_name":"Soukya Hospital",
"area":"Channasandra",
"fees":"400",
"mobile":"9738913885",
"email":"jagadishlakkur@gmail.com",
"rating":"4.0",
"lat":"12.756",
"lang":"72.756",
"avaliable_timings":"9:00 AM - 7:00 PM"
                                     */
                                        JSONArray jsonArray=response.getJSONArray("Sheet1");

                                        for (int i=0;i<jsonArray.length();i++)
                                        {

                                            JSONObject object=jsonArray.getJSONObject(i);


                                            DoctorProfileData doctorProfileData=new DoctorProfileData(context);
                                            doctorProfileData.setName(object.getString("name"));
                                            doctorProfileData.setPic(object.getString("pic"));
                                            doctorProfileData.setQualification(object.getString("qualification"));
                                            doctorProfileData.setSpecialist(object.getString("specialist"));
                                            doctorProfileData.setExperience(object.getString("experience"));
                                            doctorProfileData.setAbout(object.getString("about"));
                                            doctorProfileData.setHospital_name(object.getString("hospital_name"));
                                            doctorProfileData.setArea(object.getString("area"));
                                            doctorProfileData.setFees(object.getString("fees"));
                                            doctorProfileData.setMobile(object.getString("mobile"));
                                            doctorProfileData.setEmail(object.getString("email"));
                                            doctorProfileData.setRating(object.getString("rating"));
                                            doctorProfileData.setLat(object.getString("lat"));
                                            doctorProfileData.setLang(object.getString("lang"));
                                            doctorProfileData.setAvaliable_timings(object.getString("avaliable_timings"));
                                            Data.add(doctorProfileData);

                                        }


                                        DoctersData.setValue(Data);

                                    }else {


                                        String msg=response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                        Failuremessage.setValue(msg);

                                    }




                                //}
                               /* else{


                                    Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                    Failuremessage.setValue(response.getString("mesaage"));

                                }*/

                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(context, "JsonException"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                jsonError.postValue(e.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ResponseE",volleyError.toString());
                            Toast.makeText(context, "VolleyExce"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            volleyError.postValue(error.getMessage());
                        }
                    } );
            requestQueue.add(customRequest);

        }

        return DoctersData;
    }

    public LiveData<HashMap<String,String>> getProfile(final Context context){

        if (Profile==null)
        {
            Profile=new MutableLiveData<>();

            Map<String,String> headers = new HashMap<>();
            String credentials = PreferenceUtil.getData("username",context)+":"+PreferenceUtil.getData("password",context);
            String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);

            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("userid",PreferenceUtil.getData("userid",context));


            Log.d( "RespondedData",jsonParams.toString()+" headers: \n"+headers);

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            CustomRequest customRequest = new CustomRequest( Request.Method.POST,Utils.ProfileApi, jsonParams,headers,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d( "ResponseS",response.toString() );
                            try {
                                if (response.getString("success").equalsIgnoreCase("true")){



                                    if (response.getJSONObject("data")!=null)//sucess
                                    {

                                        JSONObject object=response.getJSONObject("data");

                                        HashMap<String,String> data=new HashMap<>();
                                        data.put("id",object.getString("id"));
                                        data.put("name",object.getString("name"));
                                        data.put("username",object.getString("username"));
                                        data.put("email",object.getString("email"));


                                        Profile.setValue(data);

                                    }else {

                                        String msg=response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                        Failuremessage.setValue(msg);
                                    }




                                }
                                else{

                                    String msg=response.getString("message");
                                   Failuremessage.setValue(msg);
                                    Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(context, "Json Error:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                                jsonError.setValue(e.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d( "ResponseE",error.toString() );


                            Toast.makeText(context, "Volley Error:\n"+error.getMessage(), Toast.LENGTH_LONG).show();
                            volleyError.setValue(error.getMessage());

                        }
                    } );
            requestQueue.add(customRequest);

        }

        return Profile;
    }



}
