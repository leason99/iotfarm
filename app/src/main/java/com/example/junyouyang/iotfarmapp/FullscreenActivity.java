package com.example.junyouyang.iotfarmapp;
import com.example.junyouyang.iotfarmapp.API.API;
import com.example.junyouyang.iotfarmapp.API.APIServices.WeatherServices;
import com.example.junyouyang.iotfarmapp.intItemChart;

import android.annotation.SuppressLint;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        //mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        /*mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
*/
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


        mQueue = Volley.newRequestQueue(this);

        //picChart = findViewById(R.id.chart1);
        //picChart2 = findViewById(R.id.chart2);
        //picChart3 = findViewById(R.id.chart3);
        picChart = new intItemChart(this,(PieChart)findViewById(R.id.chart1));
        picChart2 = new intItemChart(this,(PieChart)findViewById(R.id.chart2));
        picChart3 = new intItemChart(this,(PieChart)findViewById(R.id.chart3));
        picChart4 = new intItemChart(this,(PieChart)findViewById(R.id.chart4));

/*

        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(30f,""));
        strings.add(new PieEntry(70f,""));
        PieDataSet dataSet = new PieDataSet(strings,"Label");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.Transparent));
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);

        picChart.setRotationEnabled(false);
        picChart.setCenterText("LUX");
        picChart.setMaxAngle(180f);
        picChart.setRotationAngle(180f);
        picChart.setData(pieData);
        picChart.setDrawEntryLabels(false);
        picChart.getLegend().setEnabled(false);
        picChart.getDescription().setEnabled(false);
        picChart.invalidate();

*/

        //setChart(picChart,"lux",50,100);
        //setChart(picChart2,"temp",30,50);
        //setChart(picChart3,"rain",50,100);

        //setChart(picChart4,"moisture",50,100);
        picChart.initChart("Lux",50,100);
        picChart2.initChart("Temp",50,100);
        picChart3.initChart("Rain",50,100);
        picChart4.initChart("Moisture",50,100);

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
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                handler.postDelayed(this, 1000);

            }
        };

// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);







        /*

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference luxRef = database.getReference("lux");
        DatabaseReference tempRef = database.getReference("temp");
        DatabaseReference rainValueRef = database.getReference("RainValue");
        DatabaseReference moistureValueRef = database.getReference("moistureValue");

        luxRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class);
                setChart(picChart,"lux",value,100);

                Log.d("firebase", "Value is: " + String.valueOf(value));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class);
                setChart(picChart2,"temp",value,50);

                Log.d("firebase", "Value is: " + String.valueOf(value));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
        rainValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class);
                setChart(picChart3,"rain",value,100);

                Log.d("firebase", "Value is: " + String.valueOf(value));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
      */
        /*
        moistureValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class);
                setChart(picChart4,"moisture",value,100);

                Log.d("firebase", "Value is: " + String.valueOf(value));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
*/

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



                    //cities.add(cityWeather);
                    //adapter.notifyItemInserted(cities.size()-1);
                    //recyclerView.scrollToPosition(cities.size()-1);

                }else{
                    Toast.makeText(FullscreenActivity.this,"Sorry, city not found",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(FullscreenActivity.this,"Sorry, weather services are currently unavailable",Toast.LENGTH_LONG).show();
            }
        });






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



}
