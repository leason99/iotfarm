package com.example.junyouyang.iotfarmapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.junyouyang.iotfarmapp.FullscreenActivity;
import com.example.junyouyang.iotfarmapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class intItemChart {

    RequestQueue mQueue ;
    float max;
    String  text;
    Context context;
    PieChart piechart;
    public intItemChart(Context context,PieChart piechart) {

        this.piechart=piechart;
        this.context=context;
    }

        void setChart(float value ){

            initChart(text,value,max);
        }


        void initChart(String  text ,float value ,float max){


            this.max=max;
            this.text=text;
            //this.mcontext=context;
            mQueue = Volley.newRequestQueue(context);



        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(value/max,""));
        strings.add(new PieEntry(1-value/max,""));
        PieDataSet dataSet = new PieDataSet(strings,"Label");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(context.getResources().getColor(R.color.google_yellow));
        colors.add(context.getResources().getColor(R.color.Transparent));
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);

        //chart = findViewById(R.id.chart1);
            piechart.setRotationEnabled(false);
            piechart.setCenterText(text+"\n"+String.valueOf(value));
            piechart.setMaxAngle(180f);
            piechart.setRotationAngle(180f);
            piechart.setData(pieData);
            piechart.setDrawEntryLabels(false);
            piechart.getLegend().setEnabled(false);
            piechart.getDescription().setEnabled(false);
            piechart.invalidate();

    }

    public void getData(String url){
        StringRequest getRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("get", s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int value = jsonObject.getInt("value");
                            setChart((float)value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }


                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context,
                                volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }
                }



        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Access-Token", "r:ba26adf22dcd5fd9650e3fccd71145b5");
                params.put("RequesterId", "9e5885af-7eb9-4cc2-be62-7308daf99581");
                params.put("Content-Type", "application/json");


                return params;
            }
        };

        mQueue.add(getRequest);
    }

}
