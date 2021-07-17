package com.example.myweather;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    private Activity thisActivity=MainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FetchData fd= new FetchData();
        fd.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSettings:startActivity(new Intent(this,Settings.class));break;
            case R.id.menuAbout:startActivity(new Intent(this,about.class));break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchData extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = "";
        String units="metric";
        String city;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            final String[] daysList=new String[7];
            final String[] tempList=new String[7];
            final String[] statusList=new String[7];
            final Integer[] iconList=new Integer[7];
            final String[] humidityList=new String[7];
            final String[] dateList=new String[7];
            final String[] descriptionList=new String[7];
            try {
                JSONObject weatherData = new JSONObject(forecastJsonStr);
                JSONArray dailyArray = weatherData.getJSONArray("daily");
                for(int i = 0;i<7 ;i++)
                {
                    JSONObject mainObject = dailyArray.getJSONObject(i);
                    String dt=mainObject.getString("dt");
                    long unixTime=Long.parseLong(dt);
                    Date df=new Date(unixTime*1000);
                    SimpleDateFormat sdf= new SimpleDateFormat("EEEE");
                    String day = sdf.format(df);

                    if(i==0){day=day+"(Today)";}
                    daysList[i]=day;

                    SimpleDateFormat s_d_f = new SimpleDateFormat("yyyy.MM.dd");
                    String date = s_d_f.format(df);
                    dateList[i]=date;

                    JSONObject tempObject = mainObject.getJSONObject("temp");
                    String temp=tempObject.getString("day");
                    if (units.equals("metric")){
                        tempList[i]=temp+"\u2103";
                    }else if(units.equals("imperial")){
                        tempList[i]=temp+"\u2109";
                    }

                    JSONArray weatherArray = mainObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String weatherType=weatherObject.getString("main");
                    String weatherTypeDes=weatherObject.getString("description");
                    statusList[i]=weatherType;
                    descriptionList[i]=weatherTypeDes;

                    String humidity=mainObject.getString("humidity");
                    humidityList[i]=humidity;

                    String weatherImage=weatherObject.getString("icon");
                    if (weatherImage.equals("01d")) {
                        iconList[i] = R.drawable.clear_sky;
                    }else if (weatherImage.equals("02d")) {
                        iconList[i]=R.drawable.few_clouds;
                    }else if (weatherImage.equals("03d")) {
                        iconList[i]=R.drawable.scatterd_clouds;
                    }else if (weatherImage.equals("04d")) {
                        iconList[i]=R.drawable.broken_clouds;
                    }else if (weatherImage.equals("09d")) {
                        iconList[i]=R.drawable.clear_sky;
                    }else if (weatherImage.equals("10d")) {
                        iconList[i]=R.drawable.rain;
                    }else if (weatherImage.equals("11d")) {
                        iconList[i]=R.drawable.thunderstorm;
                    }else if (weatherImage.equals("13d")) {
                        iconList[i]=R.drawable.snow;
                    }else{
                        iconList[i] = R.drawable.mist;
                    }
                }

                CustomListAdapter adapter=new CustomListAdapter(thisActivity,daysList,statusList,tempList,iconList);
                ListView list=(ListView)findViewById(R.id.List1);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String SelectedDate = dateList[+position];
                        String SelectedWeatherType = statusList[+position];
                        Integer SelectedImage = iconList[+position];
                        String SelectedTemp = tempList[+position];
                        String SelectedHumidity = humidityList[+position];


                        Toast.makeText(getApplicationContext(), SelectedDate, Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this,MainActivity2.class);

                        intent.putExtra("ClickImage",SelectedImage);
                        intent.putExtra("ClickDate",SelectedDate);
                        intent.putExtra("ClickCity",city);
                        intent.putExtra("ClickWeatherType",SelectedWeatherType);
                        intent.putExtra("ClickTemperature",SelectedTemp);
                        intent.putExtra("ClickHumidity",SelectedHumidity);
                        startActivity(intent);

                        }
                    });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            double lon;
            double lat;

            try {
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                city= preferences.getString("city_name","Colombo");

                Geocoder gc=new Geocoder(getApplicationContext());
                List<Address> address=gc.getFromLocationName(city,5);
                List<Double> addressList=new ArrayList<>(address.size());
                for(Address ad:address){
                    if (ad.hasLatitude()&&ad.hasLongitude()){
                        addressList.add(ad.getLatitude());
                        addressList.add(ad.getLongitude());
                    }
                }
                lat=addressList.get(0);
                lon=addressList.get(1);

                units=preferences.getString("temperature_unit","metric");

                final String BASE_URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=current,hourly,minutely,alerts&units="+units+"&appid=546e141f2b1830c5050112408cb5ad4c";
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line1;

                while ((line1 = reader.readLine()) != null) {
                    buffer.append(line1 + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Hi", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Hi", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }





}