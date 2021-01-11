package com.baba.lajursurveyor.scan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.baba.lajursurveyor.helper.Fungsi;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanLogin extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    Fungsi f;
    DatabaseReference loginweb;
    private static final int PERMISION_CODE = 8962;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mScannerView = new ZXingScannerView(this);
        f = new Fungsi(this);
        loginweb = f.dr.child("loginweb");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                setContentView(mScannerView);
            } else {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISION_CODE);
                    setContentView(mScannerView);
                }else {
                    setContentView(mScannerView);
                }
            }
        } else {
            setContentView(mScannerView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        String hasil[] = result.getText().split(Pattern.quote("|"));
        String key = hasil[1];

        HashMap map = new HashMap();
        map.put("emailweb",f.sp.getString("emailweb",""));
        map.put("id", f.sp.getString("id",""));
        map.put("foto", f.sp.getString("foto",""));
        loginweb.child(key).setValue(map);
    }
}