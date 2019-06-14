package com.example.junyouyang.iotfarmapp.API.APIServices;

import com.example.junyouyang.iotfarmapp.models.CityWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by MartinRuiz on 8/19/2017.
 */

public interface WeatherServices {
    @GET("forecast/daily")
    Call<CityWeather> getWeatherCity (@Query("q") String city, @Query("APPID") String key, @Query("units") String units, @Query("cnt") int days);

}
