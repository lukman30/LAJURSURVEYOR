package com.baba.lajursurveyor.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyorsurveyor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PostLogin extends AppCompatActivity implements View.OnClickListener {
    Fungsi f;
    TextInputLayout til_nama, til_nohp;
    TextInputEditText tie_nama, tie_nohp;
    MaterialButton lanjut;
    String namalengkap, nohp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_login);

        f = new Fungsi(this, this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById();

        lanjut.setOnClickListener(this);
    }

    private void findViewById() {
        til_nama = findViewById(R.id.til_nama);
        til_nohp = findViewById(R.id.til_nohp);
        tie_nama = findViewById(R.id.tie_nama);
        tie_nohp = findViewById(R.id.tie_nohp);
        lanjut = findViewById(R.id.lanjut);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    System.exit(0);
                }
            }, 250);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        if (v == lanjut){
            if (!f.cekText(til_nama) | !f.cekText(til_nohp)){
                return;
            }else{
                namalengkap = til_nama.getEditText().getText().toString();
                nohp = til_nohp.getEditText().getText().toString();
                SendOtp(namalengkap, nohp);
            }
        }
    }

    private void SendOtp(String namalengkap, String nohp) {
        Toast.makeText(this, namalengkap+" - "+nohp, Toast.LENGTH_SHORT).show();
    }
}