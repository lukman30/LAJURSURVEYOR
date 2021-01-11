package com.baba.lajursurveyor.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Fungsi {

    Context c;
    Activity a;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public DatabaseReference dr;
    public StorageReference sr;
    public static final String TAG = "com.baba.lajursurveyor.helper";
    public static final int kodenya = 8910;
    public FirebaseAuth fa;
    public FirebaseUser fu;
    public static final String xyz = "https://fcm.googleapis.com/fcm/send";

    public Fungsi(){}

    public Fungsi(Context c) {
        this.c = c;
        sp = c.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        ed = sp.edit();
        dr = FirebaseDatabase.getInstance().getReference();
        sr = FirebaseStorage.getInstance().getReference();
        fa = FirebaseAuth.getInstance();
        fu = fa.getCurrentUser();
    }

    public Fungsi(Context c, Activity a) {
        this.a = a;
        this.c = c;
        sp = c.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        ed = sp.edit();
        dr = FirebaseDatabase.getInstance().getReference();
        sr = FirebaseStorage.getInstance().getReference();
        fa = FirebaseAuth.getInstance();
        fu = fa.getCurrentUser();
    }

    public void panggilTelepon(String telpon) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telpon));
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(a, new String[]{android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE},
                        kodenya);
            }
            return;
        }
        c.startActivity(callIntent);

    }

    public static void KirimNotif(final String DeviceIdKey, final String title, final String body, final String xx) throws Exception {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BufferedReader in = null;

                String authKey = "AAAAUpFhJ6g:APA91bGxsXzt3tn2tWJ7zvwzLgDzUSabRy6Svr_j6jOoFGez0j7QCJw3b9KEZr3OBgnCYMLN1vfBonmOPXOBOZWKz8BnCrDJA6fhc_rsXK7j826SqLuSeCSPpX-cW0m4paCGbBDMGdlU";

                URL url = null;
                try {
                    url = new URL(xx);
                } catch (MalformedURLException e) {
                    Log.i("yoyoyo", "error: 1");
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 2");
                    e.printStackTrace();
                }

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    Log.i("yoyoyo", "error: 3");
                    e.printStackTrace();
                }
                conn.setRequestProperty("Authorization", "key=" + authKey);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();
                try {
                    json.put("to", DeviceIdKey.trim());
                } catch (JSONException e) {
                    Log.i("yoyoyo", "error: 4");
                    e.printStackTrace();
                }
                JSONObject info = new JSONObject();
                try {
                    info.put("title", title);
                } catch (JSONException e) {
                    Log.i("yoyoyo", "error: 5");
                    e.printStackTrace();
                }
                try {
                    info.put("body", body);
                } catch (JSONException e) {
                    Log.i("yoyoyo", "error: 6");
                    e.printStackTrace();
                }
                try {
                    json.put("data", info);
                } catch (JSONException e) {
                    Log.i("yoyoyo", "error: 7");
                    e.printStackTrace();
                }

                OutputStreamWriter wr = null;
                try {
                    wr = new OutputStreamWriter(conn.getOutputStream());
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 8");
                    e.printStackTrace();
                }
                try {
                    wr.write(json.toString());
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 9");
                    e.printStackTrace();
                }
                try {
                    wr.flush();
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 10");
                    e.printStackTrace();
                }
                try {
                    conn.getInputStream();
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 11");
                    e.printStackTrace();
                }

                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void kirimnotif(final String title, final String body) {
        dr.child("token_op").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token = dataSnapshot.getValue(String.class);
                try {
                    KirimNotif(token, title, body, xyz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setTransparantStatusBar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(a, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            a.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(a, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            a.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = a.getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setWindowFlag(a, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            a.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public int statusBarHeight(){
        Rect rectangle = new Rect();
        Window window = a.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    public static String tglKata(String tgl){
        String[] index_bln = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        int bln = Integer.parseInt(tgl.substring(5,7)) * 1;
        int thn = Integer.parseInt(tgl.substring(0,4));
        return  index_bln[bln]+" "+thn;
    }

    public String ambilTanggal(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String Rp(String nominal){
        String hasil = NumberFormat.getInstance().format(Integer.parseInt(nominal));
        String replaceString = "Rp. "+hasil .replace(',','.');
        return replaceString;
    }

    public String notrans(String kode){
        DateFormat kodeunik = new SimpleDateFormat("MMyyyyddmmssHH");
        Date date = new Date();
        return kode+""+kodeunik.format(date);
    }

    public String jam(){
        DateFormat kodeunik = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return kodeunik.format(date);
    }

    public String tglkadaluarsa(){
        DateFormat tglformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis()+30*60*1000);
        return tglformat.format(date);
    }

    public String jamkadaluarsa(){
        DateFormat tglformat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis()+30*60*1000);
        return tglformat.format(date);
    }

    public int randomdigit(int nominal) {
        String sub = String.valueOf(nominal);
        int getLenght = sub.length();
        int sisaLenght = getLenght - 3;
        String depan = sub.substring(0, sisaLenght);
        String belakang = sub.substring(sub.length() - 3);
        Random random = new Random();
        int min = Integer.parseInt(belakang);
        if(min == 0){
            min = 100;
        }
        int x = min + random.nextInt(900 - min);
        String hasil = depan + x;
        return Integer.parseInt(hasil);
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)a.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public static String randomEmail(String email) {
        String[] parts = email.split("@");
        String hasil = shuffle(parts[0])+"23";
        return hasil;
    }

    private static String shuffle(String word) {
        String shuffledWord = word;
        int wordSize = word.length();
        int shuffleCount = 10;
        for(int i = 0;i < shuffleCount; i++) {
            int position1 = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                position1 = ThreadLocalRandom.current().nextInt(0, wordSize);
                int position2 = ThreadLocalRandom.current().nextInt(0, wordSize);
                shuffledWord = swapCharacters(shuffledWord,position1,position2);
            }
        }
        return shuffledWord;
    }

    private static String swapCharacters(String shuffledWord, int position1, int position2) {
        char[] charArray = shuffledWord.toCharArray();
        char temp = charArray[position1];
        charArray[position1] = charArray[position2];
        charArray[position2] = temp;
        return new String(charArray);
    }

    public void createUser(String email, String password){
        fa.createUserWithEmailAndPassword(email, password).addOnCompleteListener(a, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("createUser", "success");
                        } else {
                            Log.w("createUser", "failure", task.getException());
                        }
                    }
                });
    }

    public boolean cekText(TextInputLayout v){
        if (v.getEditText().getText().toString().trim().isEmpty()){
            v.setErrorEnabled(true);
            v.setError("Harus di isi.");
            return false;
        }else {
            v.setErrorEnabled(false);
            v.setError(null);
            return true;
        }
    }

    public String cekIsi(TextInputLayout v){
        String hasil = "";
        if (v.getEditText().getText().toString().trim().isEmpty()){
            hasil = "";
            return hasil;
        }else {
            hasil = v.getEditText().getText().toString().trim();
            return hasil;
        }
    }
}
