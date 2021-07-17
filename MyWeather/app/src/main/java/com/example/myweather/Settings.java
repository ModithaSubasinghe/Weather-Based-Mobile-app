package com.example.myweather;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new MypreferenceFragement()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Settings.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static class MypreferenceFragement extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("CityName","Colombo");
            editor.commit();
        }

    }
}
