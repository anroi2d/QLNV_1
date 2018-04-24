package com.example.lecongan.qlnv_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNV extends BaseAdapter {
    Activity context;
    ArrayList<NhanVien> list;

    public AdapterNV(Activity context, ArrayList<NhanVien> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_lv, null);
        ImageView img = (ImageView) row.findViewById(R.id.imv_hinh);
        TextView tv_id = (TextView) row.findViewById(R.id.tv_id);
        TextView tv_ten = (TextView) row.findViewById(R.id.tv_ten);
        TextView tv_sdt = (TextView) row.findViewById(R.id.tv_sdt);
        Button btn_sua = (Button) row.findViewById(R.id.btn_sua);
        Button btn_xoa = (Button) row.findViewById(R.id.btn_xoa);

        final NhanVien nhanVien = list.get(position);
        tv_id.setText(nhanVien.id+ "");
        tv_ten.setText(nhanVien.ten);
        tv_sdt.setText(nhanVien.sdt);

        Bitmap bitHinh = BitmapFactory.decodeByteArray(nhanVien.anh, 0,nhanVien.anh.length);
        img.setImageBitmap(bitHinh);

        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, UpdateActivity.class);
                intent.putExtra("id",nhanVien.id);
                context.startActivity(intent);
            }
        });
        btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xac nhan Xoa");
                builder.setMessage("Ban Chac chan muon xoa nhan vien nay?");
                builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(nhanVien.id);
                    }
                });
                builder.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void delete(int idNV) {
        SQLiteDatabase database = Database.initDatabase(context, "EmployeeDB.sqlite");
        database.delete("NhanVien", "ID= ? ",new String[]{idNV+ ""});
        list.clear();
        //cap nhat listview
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien",null);
        //load du lieu
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            list.add(new NhanVien(id,ten,sdt,anh));
        }
        notifyDataSetChanged();
    }
}
