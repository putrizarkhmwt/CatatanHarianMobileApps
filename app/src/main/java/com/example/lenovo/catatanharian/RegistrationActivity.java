package com.example.lenovo.catatanharian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class RegistrationActivity extends AppCompatActivity {

    EditText edtUsername, edtPasswod, edtNama, edtEmail, edtSekolah, edtAlamat;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Resgister");

        edtUsername = findViewById(R.id.edt_username);
        edtPasswod = findViewById(R.id.edt_password);
        edtNama = findViewById(R.id.edt_nama);
        edtEmail = findViewById(R.id.edt_email);
        edtSekolah = findViewById(R.id.edt_sekolah);
        edtAlamat = findViewById(R.id.edt_alamat);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidation()){
                    simpanFileData();
                }else{
                    Toast.makeText(RegistrationActivity.this, "Mohon Lengkapi Selutuh Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void simpanFileData() {
        String isiFile = edtUsername.getText().toString()+";"+edtPasswod.getText().toString()+";"+edtNama.getText().toString()+";"+edtEmail.getText().toString()+";"+edtSekolah.getText().toString()+";"+edtAlamat.getText().toString();
        File file = new File(getFilesDir(), edtUsername.getText().toString());

        FileOutputStream outputStream = null;
        try{
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private boolean isValidation() {
        if(edtUsername.getText().toString().equals("") || edtPasswod.getText().toString().equals("") || edtNama.getText().toString().equals("") || edtEmail.getText().toString().equals("") || edtSekolah.getText().toString().equals("") || edtAlamat.getText().toString().equals("")){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
