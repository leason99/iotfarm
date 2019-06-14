package com.example.junyouyang.iotfarmapp;
import com.example.junyouyang.iotfarmapp.API.API;
import com.example.junyouyang.iotfarmapp.API.APIServices.WeatherServices;
import com.example.junyouyang.iotfarmapp.intItemChart;

import android.annotation.SuppressLint;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.junyouyang.iotfarmapp.models.CityWeather;
import com.example.junyouyang.iotfarmapp.utils.IconProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private LineChart chart;
    private View mContentView;
    private View mControlsView;
    //private PieChart picChart,picChart2,picChart3;
    private intItemChart picChart,picChart2,picChart3,picChart4;
    private boolean mVisible;
    RequestQueue mQueue ;
    private WeatherServices weatherServices;
    @BindView(R.id.textViewCardCityName) TextView textViewCityName;
    @BindView(R.id.textViewCardWeatherDescription) TextView textViewWeatherDescription;
    @BindView(R.id.textViewCardCurrentTemp) TextView textViewCurrentTemp;
    @BindView(R.id.textViewCardMaxTemp) TextView textViewMaxTemp;
    @BindView(R.id.textViewCardMinTemp) TextView  textViewMinTemp;
    @BindView(R.id.imageViewCardWeatherIcon) ImageView imageViewWeatherIcon;
    //@BindView(R.id.cardViewWeatherCard) CardView cardViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);

        mVisible = true;
        toggle();

        mQueue = Volley.newRequestQueue(this);
        picChart = new intItemChart(this,(PieChart)findViewById(R.id.chart1));
        picChart2 = new intItemChart(this,(PieChart)findViewById(R.id.chart2));
        picChart3 = new intItemChart(this,(PieChart)findViewById(R.id.chart3));
        picChart4 = new intItemChart(this,(PieChart)findViewById(R.id.chart4));


        picChart.initChart("Lux",50,100);
        picChart2.initChart("Temp",50,100);
        picChart3.initChart("Rain",50,100);
        picChart4.initChart("Moisture",50,100);

        PieChart pichart =(PieChart)findViewById(R.id.chart1);
        pichart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

      final Handler handler = new Handler();
// Define the code block to be executed
         Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                picChart.getData("http://140.118.25.64:23000/resources/qiot/things/107pblteam4/Tingname/3");
                picChart2.getData("http://140.118.25.64:23000/resources/qiot/things/107pblteam4/Tingname/Temp");
                picChart3.getData("http://140.118.25.64:23000/resources/qiot/things/107pblteam4/Tingname/4");
                picChart4.getData("http://140.118.25.64:23000/resources/qiot/things/107pblteam4/Tingname/2");

                handler.postDelayed(this, 1000);

            }
        };

// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);




        weatherServices = API.getApi().create(WeatherServices.class);
        Call<CityWeather> cityWeather = weatherServices.getWeatherCity("taipei", API.KEY, "metric",6);
        cityWeather.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, retrofit2.Response<CityWeather> response) {
                if(response.code()==200){
                    CityWeather cityWeather = response.body();


                        textViewCityName.setText(cityWeather.getCity().getName()+", "+cityWeather.getCity().getCountry());
                        textViewWeatherDescription.setText(cityWeather.getWeeklyWeather().get(0).getWeatherDetails().get(0).getLongDescription());
                        textViewCurrentTemp.setText((int) cityWeather.getWeeklyWeather().get(0).getTemp().getDay()+"°");
                        textViewMaxTemp.setText((int) cityWeather.getWeeklyWeather().get(0).getTemp().getMax()+"°");
                        textViewMinTemp.setText((int) cityWeather.getWeeklyWeather().get(0).getTemp().getMin()+"°");
                        String weatherDescription = cityWeather.getWeeklyWeather().get(0).getWeatherDetails().get(0).getShotDescription();
                        Picasso.with(FullscreenActivity.this).load(IconProvider.getImageIcon(weatherDescription)).into(imageViewWeatherIcon);



                }else{
                    Toast.makeText(FullscreenActivity.this,"Sorry, city not found",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(FullscreenActivity.this,"Sorry, weather services are currently unavailable",Toast.LENGTH_LONG).show();
            }
        });


        chart = findViewById(R.id.lineChart);

        // background color
        chart.setBackgroundColor(Color.WHITE);

        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        //chart.setTouchEnabled(true);

        // set listeners
        //chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);



        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true);
        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(200f);
            yAxis.setAxisMinimum(-50f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
            //llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            //ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
            //ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }

        // add data

        // draw points over time
        chart.animateX(1500);

        new Thread(new Runnable() {
            public void run() {

            }
        }).start();

        new AsyncTask<Void, Void, ArrayList<Entry>>() {

            @Override
            protected ArrayList<Entry>   doInBackground( Void... voids ) {
                //Do things...
                return       testmongo();

            }
            @Override
            protected void onPostExecute(ArrayList<Entry> values) {

                System.out.println("onPostExecute onPostExecute onPostExecute");

                Log.i("Task", "onPostExecute");

                setData(values);
            }
        }.execute();

    }
    void setChart(PieChart chart ,String  text ,float value ,float max){

        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(value/max,""));
        strings.add(new PieEntry(1-value/max,""));
        PieDataSet dataSet = new PieDataSet(strings,"Label");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.google_yellow));
        colors.add(getResources().getColor(R.color.Transparent));
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);

        //chart = findViewById(R.id.chart1);
        chart.setRotationEnabled(false);
        chart.setCenterText(text+"\n"+String.valueOf(value));
        chart.setMaxAngle(180f);
        chart.setRotationAngle(180f);
        chart.setData(pieData);
        chart.setDrawEntryLabels(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.invalidate();

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

        } catch (JSONException e) {
            e.printStackTrace();
        }









                    }


                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(FullscreenActivity.this,
                                volleyError.toString(), Toast.LENGTH_LONG).show();
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
                ;
        mQueue.add(getRequest);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
       // mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
        //    hide();
        }
    };

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    ArrayList<Entry>  testmongo(){

        ArrayList<Entry> values = new ArrayList<>();



        MongoClient mongoClient = new MongoClient( "140.118.172.200" , 27017 );

        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("iot");
        System.out.println("Connect to database successfully");

        MongoCollection<Document> collection = mongoDatabase.getCollection("temp");

        FindIterable<Document> findIterable = collection.find().limit(50);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        System.out.println(mongoCursor.next());
        float i=1;
        while(mongoCursor.hasNext()){

            Document doc =mongoCursor.next();
            Document payload =(Document) doc.get("payload");


            values.add(new Entry(i,  Float.valueOf(String.valueOf( payload.get("value")))));
            System.out.println("mongod"+String.valueOf( payload.get("value")));

            i++;

        }


        System.out.println("集合 test 选择成功");
        return values ;
    }


    private void setData(ArrayList<Entry>  values) {




        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });



            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart.setData(data);
        }
    }



}



