package com.example.favarit_item;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DataBaseHalper dataBaseHalper;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    BaseAdapter myAdapter;
    FloatingActionButton floatingButton;
    HashMap<String, CountDownTimer> timerHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataBaseHalper = new DataBaseHalper(this);
        floatingButton = findViewById(R.id.floatingButton);
        listView = findViewById(R.id.listview);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });

        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        getData();
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
            View view = layoutInflater.inflate(R.layout.itemlist, parent, false);
            CheckBox tvName;
            CardView cardView;

            tvName = view.findViewById(R.id.tvName);
            cardView = view.findViewById(R.id.cardView);

            HashMap<String, String> myhashmap = arrayList.get(position);

            String id = myhashmap.get("id");
            String name = myhashmap.get("name");
            String isFavorite = myhashmap.get("isFavorite");

            tvName.setText(name);

            if (isFavorite.equals("0")) {
                tvName.setChecked(false);
            } else {
                tvName.setChecked(true);
            }

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavorite.equals("0")) {
                        // Cancel existing countdown timer if checkbox is unchecked again
                        if (timerHashMap.containsKey(id)) {
                            CountDownTimer timer = timerHashMap.get(id);
                            timer.cancel();
                            timerHashMap.remove(id);
                            tvName.setText(name); // Reset checkbox text
                        } else {
                            // Start countdown timer for 10 seconds
                            CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    // Update UI to display remaining time
                                    tvName.setText("Deleting in " + millisUntilFinished / 1000 + " seconds");
                                }

                                public void onFinish() {
                                    // Delete item after countdown finishes
                                    dataBaseHalper.deleteItem(id);
                                    getData();
                                    notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, "Task Finished", Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                            // Store the timer associated with this checkbox
                            timerHashMap.put(id, countDownTimer);
                        }
                    } else {
                        // Cancel the countdown timer if checkbox is unchecked before deletion
                        if (timerHashMap.containsKey(id)) {
                            CountDownTimer timer = timerHashMap.get(id);
                            timer.cancel();
                            timerHashMap.remove(id);
                            tvName.setText(name); // Reset checkbox text
                        }
                        tvName.setChecked(false);
                        getData();
                        notifyDataSetChanged();
                    }
                }
            });

            return view;
        }
    }

    private void showDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View myview = getLayoutInflater().inflate(R.layout.input_layout, null);
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
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Plz enter your name", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isDataInserted = dataBaseHalper.InsertData(name);
                    if (isDataInserted) {
                        Toast.makeText(MainActivity.this, "data inserted", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        getData();
                    } else {
                        Toast.makeText(MainActivity.this, "data is not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        alertDialog.show();
    }

    public void getData() {
        Cursor cursor = dataBaseHalper.getUserData();
        arrayList.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int isFavorite = cursor.getInt(2);

                hashMap = new HashMap<>();
                hashMap.put("id", "" + id);
                hashMap.put("name", "" + name);
                hashMap.put("isFavorite", "" + isFavorite);
                arrayList.add(hashMap);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData();
    }
}
