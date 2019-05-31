package com.velozion.myoitc.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.velozion.myoitc.Adapter.HistoryAdapter;
import com.velozion.myoitc.CustomRequest;
import com.velozion.myoitc.PreferenceUtil;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;
import com.velozion.myoitc.ViewModel.MyViewModel;
import com.velozion.myoitc.db.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link History_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History_Frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout nodata;
    TextView textView;
    ArrayList<HashMap<String,String>> Data=new ArrayList<>();
    HistoryAdapter historyAdapter;

    Context context;
    MyViewModel myViewModel;

    public History_Frag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static History_Frag newInstance(String param1, String param2) {
        History_Frag fragment = new History_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view==null)
        {
            view=inflater.inflate(R.layout.fragment_history_, container, false);

            myViewModel=ViewModelProviders.of(getActivity()).get(MyViewModel.class);

            recyclerView=(RecyclerView)view.findViewById(R.id.history_recylerview);
            progressBar=(ProgressBar)view.findViewById(R.id.history_progressbar);
            textView=(TextView)view.findViewById(R.id.history_textview);
            nodata=(LinearLayout)view.findViewById(R.id.history_nodata_ll);

            LinearLayoutManager manager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);



            LoadHistory();

        }
        return view;
    }

    private void LoadHistory() {



        myViewModel.getHistoryList(getActivity()).observe(this, new Observer<ArrayList<HistoryData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<HistoryData> historyData) {

                if (historyData.size()>0)
                {
                    historyAdapter=new HistoryAdapter(getActivity(),historyData);
                    recyclerView.setAdapter(historyAdapter);

                    TriggerUI(true);


                }else {

                    textView.setText("No Data Found");
                    TriggerUI(false);
                }
            }
        });

        myViewModel.Failuremessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                TriggerUI(false);

            }
        });

        myViewModel.jsonError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                TriggerUI(false);

            }
        });

        myViewModel.volleyError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                TriggerUI(false);

            }
        });

      /*  Map<String,String> headers = new HashMap<>();
        String credentials = PreferenceUtil.getData("username",getActivity())+":"+PreferenceUtil.getData("password",getActivity());
        String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        Map<String, String> jsonParams = new HashMap<String, String>();


        Log.d( "RespondedData",jsonParams.toString());


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        CustomRequest customRequest = new CustomRequest( Request.Method.POST, Utils.HistoryApi, jsonParams,headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d( "ResponseS",response.toString() );
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")){


                                if (response.getJSONArray("data")!=null)//sucess
                                {



                                    JSONArray jsonArray=response.getJSONArray("data");

                                    for (int i=0;i<jsonArray.length();i++)
                                    {

                                        JSONObject object=jsonArray.getJSONObject(i);

                                        HashMap<String,String> hashMap=new HashMap<>();
                                        hashMap.put("ci_place",object.getString("in_location"));
                                        hashMap.put("ci_time",object.getString("check_in"));
                                        hashMap.put("co_place",object.getString("out_location"));
                                        hashMap.put("co_time",object.getString("check_out"));

                                        hashMap.put("ci_lat",object.getString("in_lat"));
                                        hashMap.put("ci_lang",object.getString("in_lang"));
                                        hashMap.put("co_lat",object.getString("out_lat"));
                                        hashMap.put("co_lang",object.getString("out_lang"));
                                        Data.add(hashMap);

                                    }

                                    if (Data.size()>0)
                                    {
                                        historyAdapter=new HistoryAdapter(getActivity(),Data);
                                        recyclerView.setAdapter(historyAdapter);

                                        TriggerUI(true);


                                    }else {

                                        textView.setText("No Data Found");
                                        TriggerUI(false);
                                    }

                                }else {

                                    String msg=response.getJSONObject("messages").getJSONArray("error").get(0).toString();
                                    Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
                                    textView.setText(""+msg);
                                    TriggerUI(false);
                                }




                            }
                            else{

                              TriggerUI(false);
                                Toast.makeText(getActivity(), ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                textView.setText(" "+response.getString("message"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                          TriggerUI(false);
                            Toast.makeText(getActivity(), "Json Error:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "ResponseE",error.toString() );

                        TriggerUI(false);
                        Toast.makeText(getActivity(), "Volley Error:\n"+error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } );
        requestQueue.add(customRequest);*/

     /* for (int i=0;i<5;i++)
      {

          HashMap<String,String> hashMap=new HashMap<>();
          hashMap.put("ci_loc","bangalore");
          hashMap.put("ci_time","02:25");
          hashMap.put("co_loc","bangalore");
          hashMap.put("co_time","4:20");
          Data.add(hashMap);

      }

      if (Data.size()>0)
      {
          historyAdapter=new HistoryAdapter(getActivity(),Data);
          recyclerView.setAdapter(historyAdapter);

          TriggerUI(true);


      }else {

            textView.setText("No Data Found");
          TriggerUI(false);
      }*/




    }

    void TriggerUI(boolean showui)
    {
        if (showui)//show recyclerview
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);

        }else {//show error
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context=ctx;
    }

}
