package com.example.favarit_item;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DataBaseHalper dataBaseHalper;
    ArrayList <HashMap <String,String>> arrayList = new ArrayList<>();
    HashMap <String,String> hashMap;
    Adapter myAdapter;
    FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dataBaseHalper = new DataBaseHalper(this);
        floatingButton = findViewById(R.id.floatingButton);

        listView = findViewById(R.id.listview);

       floatingButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showDialogBox ();
           }
       });

      myAdapter = new MyAdapter();
      listView.setAdapter((ListAdapter) myAdapter);

      

    }
    public class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.itemlist,parent,false) ;
            CheckBox tvName;
            CardView cardView;

            tvName=view.findViewById(R.id.tvName);
            cardView=view.findViewById(R.id.cardView);

            HashMap <String,String> myhashmap =arrayList.get(position);

              String id = myhashmap.get("id");
              String name = myhashmap.get("name");
              String isFavorite = myhashmap.get("isFavorite");


              tvName.setText(name);

              if (isFavorite.equals("0")) {

                 tvName.setChecked(false);

              }else {
                  //favoriteImage.setImageResource(R.drawable.fill_favorite);
                  tvName.setChecked(true);
              }

            tvName.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if (isFavorite.equals("0")){
                      dataBaseHalper.updateAddFavorite(Integer.parseInt(id));
                    //  favoriteImage.setImageResource(R.drawable.fill_favorite);
                      tvName.setChecked(true);

                      notifyDataSetChanged();

                  }else {
                      dataBaseHalper.updateRemoveFavorite(Integer.parseInt(id));
                    //  favoriteImage.setImageResource(R.drawable.unfill_favorite);
                      tvName.setChecked(false);

                      notifyDataSetChanged();
                  }
              }

            });

             // Delete button এর code lekha hoice


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dataBaseHalper.deleteItem(id);
                    getData();
                    notifyDataSetChanged();

                }
            });


            registerForContextMenu(cardView);



            return view;
        }

    } //end adapter


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_context, menu);

//aita dile crash kore
//        TextView tv1 = findViewById(R.id.delete);
//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Wait Bro!", Toast.LENGTH_SHORT).show();
//            }
//        });


    }



    //Aita akhono kori nai
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//
//        }
//
//        return super.onContextItemSelected(item);
//    }



    private void showDialogBox () {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View myview =getLayoutInflater().inflate(R.layout.input_layout,null) ;
        alert.setView(myview);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        EditText edName;
        Button button;
        button = myview.findViewById(R.id.button);
        edName = myview.findViewById(R.id.edName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Plz enter your name", Toast.LENGTH_SHORT).show();
                }else {
                    boolean isDataInserted = dataBaseHalper.InsertData(name);
                    if (isDataInserted){
                        Toast.makeText(MainActivity.this, "data inserted", Toast.LENGTH_SHORT).show();

                        getData();
                        //myAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();

                    }else {
                        Toast.makeText(MainActivity.this, "data is not inserted", Toast.LENGTH_SHORT).show();
                    }

                }
            }


        }); //button tag end here
        alertDialog.show();
    }//showDialogBox end tag


    private void getData (){

        Cursor cursor = dataBaseHalper.getUserData();
        arrayList.clear();
        if (cursor!=null){

            while (cursor.moveToNext()){

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int isFavorite = cursor.getInt(2);


                    hashMap = new HashMap<>();
                    hashMap.put("id",""+id);
                    hashMap.put("name",""+name);
                    hashMap.put("isFavorite",""+isFavorite);
                    arrayList.add(hashMap);

            }
        }

    }//getData end here


    @Override
    protected void onPause() {

        getData();
        super.onPause();
    }

    @Override
    protected void onResume() {

        getData();
        super.onResume();
    }
}


