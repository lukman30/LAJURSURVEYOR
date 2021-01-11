package com.baba.lajursurveyor.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.baba.lajursurveyor.Beranda;
import com.baba.lajursurveyor.helper.Fungsi;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class KLogin {
    Context c;
    Activity a;
    Fungsi f;

    public KLogin() {
    }

    public KLogin(Context c, Activity a) {
        this.c = c;
        this.a = a;
        f = new Fungsi(c, a);
    }

    public void ambilData(final GoogleSignInResult result, GoogleSignInAccount x, final FirebaseAuth auth, final ProgressDialog pd, final DatabaseReference dr, final String imeinya, final SharedPreferences.Editor ed, final GoogleApiClient googleApiClient){
        if(result.isSuccess()){
            Log.e("surveyor", "weka");
            x = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(x.getIdToken(), null);
            auth.signInWithCredential(credential).addOnCompleteListener(a, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
//                    Log.e("surveyor", "wow");
                    if (!task.isSuccessful()) {
                        Log.e("surveyor", "gagal");
//                        Toast.makeText(c, "anu", Toast.LENGTH_SHORT).show();
//                        Keluar(auth, googleApiClient);
//                        pd.dismiss();
                    } else {
//                        Log.e("surveyor", "baca");
                        f.dr.child("surveyor").orderByChild("email").equalTo(auth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot admin) {
//                                Log.e("surveyor", auth.getCurrentUser().getEmail());
                                if(admin.getChildrenCount() > 0){
                                    for(DataSnapshot adm: admin.getChildren()){
                                        String idnya = adm.getKey();
                                        String nama =adm.child("nama").getValue(String.class);
                                        String foto =adm.child("foto").getValue(String.class);
                                        String alamat =adm.child("lokasi").getValue(String.class);
                                        ed.putString("id", idnya);
                                        ed.putString("nama",nama);

                                        ed.putString("lokasi",alamat);
                                        if (foto.equals("kosong")){
                                            f.dr.child("surveyor").child(idnya).child("foto").setValue(auth.getCurrentUser().getPhotoUrl().toString());
                                            ed.putString("foto",auth.getCurrentUser().getPhotoUrl().toString());
                                        }else{
                                            ed.putString("foto",foto);
                                        }

                                        ed.commit();
                                        pd.dismiss();
                                        Intent ke = new Intent(c, Beranda.class);
                                        a.startActivity(ke);
                                    }
                                }else{
                                    Toast.makeText(c, "Maaf, Login gagal. pastikan email anda terdaftar pada lajur", Toast.LENGTH_SHORT).show();
                                    Keluar(auth, googleApiClient);
                                    pd.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }
    }

    public void ubahTeksTombolGoogle(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    public void Masuk(ProgressDialog pd, GoogleApiClient googleApiClient){
        pd.show();
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        a.startActivityForResult(intent, Fungsi.kodenya);
    }

    public void Keluar(FirebaseAuth auth, GoogleApiClient googleApiClient){
        auth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }
}

