package com.example.myweather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        ImageView img_V2 = (ImageView) findViewById(R.id.imageView2);
        TextView txt_date = (TextView) findViewById(R.id.txtDate);
        TextView txt_city = (TextView) findViewById(R.id.txtCity);
        TextView txt_weather_des = (TextView) findViewById(R.id.txtWeatherDes);
        TextView txt_temp2 = (TextView) findViewById(R.id.txtTemp2);
        TextView txt_humidity=(TextView) findViewById(R.id.txtHumidity);

        Bundle bundle = getIntent().getExtras();
        Integer Img = bundle.getInt("ClickImage");
        String txt_1 = getIntent().getStringExtra("ClickDate");
        String txt_2 = getIntent().getStringExtra("ClickWeatherType");
        String txt_3 = getIntent().getStringExtra("ClickTemperature");
        String txt_4 = getIntent().getStringExtra("ClickHumidity");
        String txt_5 = getIntent().getStringExtra("ClickCity");

        img_V2.setImageResource(Img);
        txt_date.setText(txt_1);
        txt_weather_des.setText(txt_2);
        txt_temp2.setText(txt_3);
        txt_humidity.setText("Humidity:"+txt_4+"%");
        txt_city.setText(txt_5);

    }
}

