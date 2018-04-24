package com.example.lecongan.qlnv_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    SQLiteDatabase database;
    final String DATABASE_NAME = "EmployeeDB.sqlite";
    AdapterNV adapter;
    ArrayList<NhanVien> list;
    ListView lv;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvent();
        readData();
    }

    private void addEvent() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien", null);
        list.clear();
        for(int i=0; i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            int id= cursor.getInt(0);
            String ten=cursor.getString(1);
            String sdt =cursor.getString(2);
            byte[] anh=cursor.getBlob(3);
            list.add(new NhanVien(id,ten,sdt,anh));
        }
        adapter.notifyDataSetChanged();
    }

    private void addControls() {
//        tv= (TextView) findViewById(R.id.tv);

        lv = (ListView) findViewById(R.id.lv_item);
        list = new ArrayList<>();
        adapter = new AdapterNV(this, list);
        lv.setAdapter(adapter);
        btnAdd = (Button) findViewById(R.id.btn_add);
    }
}
