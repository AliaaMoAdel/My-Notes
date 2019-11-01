package com.example.android.mytops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    final static String sharedprefes = "sharedprefes";
    SharedPreferences shrdpre;
    SharedPreferences.Editor editor;
    Gson gson;
    String json;
    ArrayList<String> mSubtopsList= new ArrayList<String>();
    ArrayAdapter mtopsAdapter ;
    EditText mEditText;
    String mSubTopName;
    ListView layout2 ;
    String mTopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        layout2 = findViewById(R.id.subTopsXml);
        Button btn= findViewById(R.id.button2);
        mEditText = (EditText) findViewById(R.id.edtTxt); //edit text for the user input
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyName();
            }
        });

        loadTop();

    }

    public String getIntentData(){
        Intent intent = getIntent();
        mTopName= intent.getStringExtra("TopName");
        return mTopName;

    }

    public void applyName(){

        mSubTopName=  mEditText.getText().toString(); //variable that carry the user input
        mSubtopsList.add(mSubTopName);  //add the input to the array list
        initializeAdapter(); // must use adapter for the list view(xml file)
        saveTop();

    }

    public void initializeAdapter(){ //i put it in a method to save lines because i use it in another 2 methods
        mtopsAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mSubtopsList);
        //layout2= (ListView) findViewById(R.id.subTopsXml);
        layout2.setAdapter(mtopsAdapter);
    }

    public void saveTop() {
        getIntentData();
        //https://www.youtube.com/watch?v=jcliHGR3CHo
        shrdpre = getSharedPreferences(sharedprefes, MODE_PRIVATE);

        editor = shrdpre.edit();
        gson = new Gson();
        json = gson.toJson(mSubtopsList);
        editor.putString(mTopName, json);

        editor.apply();

    }

    public void loadTop() {
        getIntentData();
        ListView layout = (ListView) findViewById(R.id.subTopsXml);
        shrdpre = getSharedPreferences(sharedprefes, MODE_PRIVATE);
        gson = new Gson();
        json = shrdpre.getString(mTopName, null);
        TextView textView ;
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        mSubtopsList = gson.fromJson(json, type);
        if (mSubtopsList == null) {
            mSubtopsList = new ArrayList<String>();
        }
        else {


            initializeAdapter();



        }
    }
}
