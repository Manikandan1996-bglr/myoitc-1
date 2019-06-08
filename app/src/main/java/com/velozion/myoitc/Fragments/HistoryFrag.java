package com.velozion.myoitc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.velozion.myoitc.Adapter.HistoryBindingAdapter;
import com.velozion.myoitc.R;
import com.velozion.myoitc.ViewModel.MyViewModel;
import com.velozion.myoitc.db.HistoryData;

import java.util.ArrayList;
import java.util.HashMap;


public class HistoryFrag extends Fragment {
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
    ArrayList<HashMap<String, String>> Data = new ArrayList<>();
    // HistoryAdapter historyAdapter;

    Context context;
    MyViewModel myViewModel;

    HistoryBindingAdapter historyBindingAdapter;

    public HistoryFrag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryFrag newInstance(String param1, String param2) {
        HistoryFrag fragment = new HistoryFrag();
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

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_history_, container, false);

            myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

            recyclerView = view.findViewById(R.id.history_recylerview);
            progressBar = view.findViewById(R.id.history_progressbar);
            textView = view.findViewById(R.id.history_textview);
            nodata = view.findViewById(R.id.history_nodata_ll);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);


            LoadHistory();

        }
        return view;
    }

    private void LoadHistory() {


        myViewModel.getHistoryList(getActivity()).observe(this, new Observer<ArrayList<HistoryData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<HistoryData> historyData) {

                if (historyData.size() > 0) {
                    //historyAdapter=new HistoryAdapter(getActivity(),historyData);
                    historyBindingAdapter = new HistoryBindingAdapter(getActivity(), historyData);
                    recyclerView.setAdapter(historyBindingAdapter);

                    TriggerUI(true);


                } else {

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

    void TriggerUI(boolean showui) {
        if (showui)//show recyclerview
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);

        } else {//show error
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }

}
