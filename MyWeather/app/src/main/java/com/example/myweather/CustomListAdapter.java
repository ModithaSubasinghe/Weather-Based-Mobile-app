package com.example.myweather;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.zip.Inflater;

public class CustomListAdapter extends ArrayAdapter{
    private final Activity context;
    private final String[]txtDay;
    private final String[]txtWeather;
    private final Integer[]imageView1;
    private final String[]txtTemp;
    public CustomListAdapter(Activity Context, String[] txtDay, String[] txtWeather, String[] txtTemp,Integer[] imageView1) {
        super(Context, R.layout.list_view,txtDay);

        this.context=Context;
        this.txtDay=txtDay;
        this.txtWeather=txtWeather;
        this.imageView1=imageView1;
        this.txtTemp=txtTemp;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();


        View rowView = inflater.inflate(R.layout.list_view,null, true);
        TextView Day = (TextView) rowView.findViewById(R.id.txtDay);
        TextView Weather = (TextView) rowView.findViewById(R.id.txtWeather);
        ImageView imgv1 = (ImageView) rowView.findViewById(R.id.imageView1);
        TextView Temp = (TextView) rowView.findViewById(R.id.txtTemp);

        Day.setText(txtDay[position]);
        Weather.setText(txtWeather[position]);
        imgv1.setImageResource(imageView1[position]);
        Temp.setText(txtTemp[position]);

        return rowView;

    }

}

