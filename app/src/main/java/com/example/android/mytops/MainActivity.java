package com.example.android.mytops;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static String sharedprefes = "sharedprefes";
    SharedPreferences shrdpre;
    SharedPreferences.Editor editor;
    Gson gson;
    String json;
    ArrayList<String> topsList= new ArrayList<String>();
    ArrayAdapter topsAdapter ;
    public static String mName; //i made it static to re-use it in main activity2

               ////////// inflate the context menu///////////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.edit_menu,menu);
    }
             /////////////////////////////////////////////////

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete:

                topsList.remove(info.position);
                //topsList.toArray();
                initializeAdapter();
                topsAdapter.notifyDataSetChanged();
                saveTop();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn =  findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyName();
            }
        });

        //////////////register for context menu/////////
        ListView listView = (ListView)findViewById(R.id.tops);
        registerForContextMenu(listView);
                      ////////////////

     ///////////load the data when app restart///////
        loadTop();
               ///////////////
    }
         ////take the user input and show it on screen///
    public void applyName(){
        EditText edt = (EditText) findViewById(R.id.insertTopEdtxt); //edit text for the user input
        mName =  edt.getText().toString(); //variable that carry the user input
        topsList.add(mName);  //add the input to the array list
        initializeAdapter(); // must use adapter for the list view(xml file)
        saveTop(); //must save the data , unless saving, the data will disappear after restart the app.

    }

    public void initializeAdapter(){ //i put it in a method to save lines because i use it in another 2 methods
        topsAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,topsList);
        final ListView layout= (ListView) findViewById(R.id.tops);
        layout.setAdapter(topsAdapter);
           ///starting a new activity from each item in the main list view
        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                String topNamePosition = (String) layout.getItemAtPosition(i);
                intent.putExtra("TopName",topNamePosition);
                startActivity(intent);


            }
        });
    }


    public void saveTop() {
        //https://www.youtube.com/watch?v=jcliHGR3CHo
        shrdpre = getSharedPreferences(sharedprefes, MODE_PRIVATE);

        editor = shrdpre.edit();
        gson = new Gson();
        json = gson.toJson(topsList);
        editor.putString("name", json);

        editor.apply();

    }

    public void loadTop() {
        ListView layout = (ListView) findViewById(R.id.tops);
        shrdpre = getSharedPreferences(sharedprefes, MODE_PRIVATE);
        gson = new Gson();
        json = shrdpre.getString("name", null);
        TextView textView ;
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        topsList = gson.fromJson(json, type);
        if (topsList == null) {
            topsList = new ArrayList<String>();
        }
        else {

            //ListAdapter topsAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,topsList);
            //topsAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,topsList);
            //layout.setAdapter(topsAdapter);
            initializeAdapter();



        }
    }

}
