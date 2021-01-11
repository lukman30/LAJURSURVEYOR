package com.baba.lajursurveyor.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.baba.lajursurveyorsurveyor.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.baba.lajursurveyor.Beranda;
import com.baba.lajursurveyor.helper.Fungsi;

import static com.baba.lajursurveyor.helper.Fungsi.kodenya;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener{
    Fungsi f;
    ProgressDialog pd;
    private SignInButton masuk;
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount x;
    FirebaseAuth auth;
    FirebaseUser fu;
    KLogin kLogin;



    private SharedPreferences permissionStatus;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private boolean sentToSettings = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        cepermission();
    }

    private void init() {
        f = new Fungsi(this, this);
        pd = new ProgressDialog(this);
        pd.setMessage("Memuat...");
        kLogin = new KLogin(this, this);
        auth = FirebaseAuth.getInstance();
        fu = auth.getCurrentUser();
        masuk =findViewById(R.id.masuk);
        kLogin.ubahTeksTombolGoogle(masuk, "Masuk Google Akun");
        masuk.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
    }

    @Override
    protected void onStart() {
        if (f.isConnectedToInternet()) {
            if (!f.sp.getString("id", "").equals("") && !f.sp.getString("id", "").equals(null)) {
//                TokenPengguna.child(f.sp.getString("id", "")).setValue(tokennya);
                startActivity(new Intent(Login.this, Beranda.class));
            }
        } else {
            Toast.makeText(this, "Tidak Ada Koneksi", Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.masuk:
                kLogin.Masuk(pd, googleApiClient);
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == kodenya){
            GoogleSignInResult hasil = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            kLogin.ambilData(hasil,x,auth, pd, f.dr,"",f.ed,googleApiClient);
        }

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void Keluar(FirebaseAuth fa, GoogleApiClient googleApiClient){
        fa.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }


    public void cepermission() {
        if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setCancelable(false);
                builder.setTitle("Informasi");
                builder.setMessage("Untuk menggunakan aplikasi ini silahkan aktifkan permissions anda...");
                builder.setPositiveButton("Setuju", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
//                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

//            txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[2])) {
//                txtPermissions.setText("Permissions Required");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Izinkan permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.show();
            } else {
//                Toast.makeText(getBaseContext(),"Permission Diaktifkan",Toast.LENGTH_LONG).show();
            }
        }
    }



    private void proceedAfterPermission() {
//        txtPermissions.setText("We've got all permissions");
//        Toast.makeText(getBaseContext(), "Permissions Diaktifkan", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}