package com.example.GoogleMapsWeather;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class CurrentWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        Intent intent = getIntent();
        Location currentLocation = intent.getParcelableExtra("currentPlace");

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        getWeatherInfo(latLng);
    }

    private void getWeatherInfo(LatLng latLng) {
        String apiKey = "22c2add41cdafa08df22cf52d56fb772";
        String url = String.format(Locale.US, "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s", latLng.latitude, latLng.longitude, apiKey);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject mainObject = response.getJSONObject("main");
                double temperature = mainObject.getDouble("temp");
                double feelsLike = mainObject.getDouble("feels_like");
                int humidity = mainObject.getInt("humidity");

                JSONArray weatherArray = response.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String description = weatherObject.getString("description");

                String cityName = response.getString("name");

                String weatherInfo = String.format(Locale.US, "Температура: %.1f°C\nОщущается как: %.1f°C\nВлажность: %d%%\nПогода: %s", temperature - 273.15, feelsLike - 273.15, humidity, description);

                TextView textView_title = findViewById(R.id.textView_title);
                textView_title.setText(cityName);

                TextView textView_snippet = findViewById(R.id.textView_snippet);
                textView_snippet.setText(weatherInfo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        // Add the request to the request queue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}