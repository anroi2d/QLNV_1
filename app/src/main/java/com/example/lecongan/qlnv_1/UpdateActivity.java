package com.example.lecongan.qlnv_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.os.Build.ID;

public class UpdateActivity extends AppCompatActivity {
    final String DATABASE_NAME = "EmployeeDB.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUESE_CHOOSE_PHOTO = 321;
    int id= -1;

    EditText edt_name,edt_sdt;
    Button btnChup,btnChon,btnLuu,btnReturn;
    ImageView imgHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addControls();
        addEvent();
        initUI();
    }

    private void addEvent() {
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        btnChup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void initUI() {
        Intent intent = getIntent();
        id= intent.getIntExtra("id", -1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien WHERE ID = ? ",new String[]{id + ""});
        cursor.moveToFirst();//thieu dong nay sinh ra loi
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        byte[] anh = cursor.getBlob(3);

        Bitmap bitHinh = BitmapFactory.decodeByteArray(anh,0,anh.length);
        imgHinh.setImageBitmap(bitHinh);
        edt_name.setText(ten);
        edt_sdt.setText(sdt);
    }

    private void addControls() {
        edt_name = (EditText) findViewById(R.id.edt_ten);
        edt_sdt = (EditText) findViewById(R.id.edt_sdt);
        btnChon= (Button) findViewById(R.id.btn_chon_hinh);
        btnChup= (Button) findViewById(R.id.btn_chup_hinh);
        btnLuu= (Button) findViewById(R.id.btn_save);
        btnReturn= (Button) findViewById(R.id.btn_return);
        imgHinh= (ImageView) findViewById(R.id.imv);
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUESE_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUESE_CHOOSE_PHOTO){
                try {
                    Uri imvUri = data.getData();
                    InputStream in = getContentResolver().openInputStream(imvUri);
                    Bitmap bitHinh = BitmapFactory.decodeStream(in);
                    imgHinh.setImageBitmap(bitHinh);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == REQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgHinh.setImageBitmap(bitmap);
            }
        }
    }

    private void update(){
        String ten = edt_name.getText().toString();
        String sdt = edt_sdt.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgHinh);

        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",ten);
        contentValues.put("SDT", sdt);
        contentValues.put("IMAGE", anh);

        SQLiteDatabase database = Database.initDatabase(this, "EmployeeDB.sqlite");
        database.update("NhanVien", contentValues, "ID = ? ", new String[] {id + ""});//update theo id
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
