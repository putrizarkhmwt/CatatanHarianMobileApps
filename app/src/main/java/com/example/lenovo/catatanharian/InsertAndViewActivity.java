package com.example.lenovo.catatanharian;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class InsertAndViewActivity extends AppCompatActivity {

    public  static final int REQUEST_CODE_STORAGE = 100;
    int eventID = 0;
    EditText edtFileName, edtCatatan;
    Button btnSave;
    boolean isEditable = false;
    String fileName = "";
    String tempCatatan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtFileName = findViewById(R.id.edt_filename);
        edtCatatan = findViewById(R.id.edt_catatan);
        btnSave = findViewById(R.id.btn_simpan);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_simpan:
                        eventID = 2;
                        if(!tempCatatan.equals(edtCatatan.getText().toString())){
                            if(Build.VERSION.SDK_INT >= 23){
                                if(periksaIzinPenyimpaman()){
                                    tampilkanDialogKonfirmasiPenyimpanan();
                                }
                            }else{
                                tampilkanDialogKonfirmasiPenyimpanan();
                            }
                        }
                        break;
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            fileName = extras.getString("filename");
            edtFileName.setText(fileName);
            getSupportActionBar().setTitle("Ubah Catatan");
        }else{
            getSupportActionBar().setTitle("Tambah Catatan");
        }
        eventID = 1;
        if(Build.VERSION.SDK_INT >=23){
            if(periksaIzinPenyimpaman()){
                bacaFile();
            }
        }else{
            bacaFile();
        }
    }

    private void bacaFile() {
        String path = Environment.getExternalStorageDirectory().toString()+"/kominfo.proyek1";
        File file = new File(path, edtFileName.getText().toString());
        if(file.exists()){
            StringBuilder text = new StringBuilder();
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch(Exception e){
                System.out.println("Error "+e.getMessage());
            }
            tempCatatan = text.toString();
            edtCatatan.setText(text.toString());
        }
    }

    private boolean periksaIzinPenyimpaman() {
        if(Build.VERSION.SDK_INT >=23) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE);
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(eventID == 1){
                        bacaFile();
                    }else{
                        tampilkanDialogKonfirmasiPenyimpanan();
                    }
                }
                break;
        }
    }

    public void buatDanUbah(){
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)){
            return;
        }
        String path = Environment.getExternalStorageDirectory().toString()+"/kominfo.proyek1";
        File parent = new File(path);
        if (parent.exists()){
            File file = new File(path, edtFileName.getText().toString());
            FileOutputStream outputStream = null;

            try{
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                streamWriter.append(edtCatatan.getText());
                streamWriter.flush();
                streamWriter.close();
                outputStream.flush();
                outputStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            parent.mkdir();
            File file = new File(path, edtFileName.getText().toString());
            FileOutputStream outputStream = null;

            try{
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(edtCatatan.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        onBackPressed();
    }
    public  void tampilkanDialogKonfirmasiPenyimpanan(){
        new AlertDialog.Builder(this).setTitle("Simpan Catatan")
                .setMessage("Apakah anda yakin ingin menyimpan catatan ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int wichButton){
                        buatDanUbah();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public  void onBackPressed(){
        if(!tempCatatan.equals((edtCatatan.getText().toString()))){
            tampilkanDialogKonfirmasiPenyimpanan();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return  super.onContextItemSelected(item);
    }
}
