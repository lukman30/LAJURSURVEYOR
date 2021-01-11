package com.baba.lajursurveyor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import com.baba.lajursurveyor.adapter.AdapterListHome;
import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyor.helper.InternetDialog;
import com.baba.lajursurveyor.login.Login;
import com.baba.lajursurveyor.model.ModelProduk;
import com.baba.lajursurveyorsurveyor.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.mobiwise.materialintro.shape.Focus.MINIMUM;

public class Beranda extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener
        ,GoogleApiClient.OnConnectionFailedListener, MaterialIntroListener,GoogleApiClient.ConnectionCallbacks{
    // tes komenhjsgdsjd
    Fungsi f;
    CircleImageView foto;
    TextView nama;

    RecyclerView rv;
    AdapterListHome ac;
    ArrayList<ModelProduk> listnya = new ArrayList<>();

    RecyclerView rv2;
    AdapterListHome ac2;
    ArrayList<ModelProduk> listnya2 = new ArrayList<>();
    String tokennya;
    private DrawerLayout drawer;
    ImageView menubar;

    ImageView empty,empty2;


    private GoogleApiClient googleApiClient;
    FirebaseAuth auth;

    TextView tvproses,tvsukses,b1,b2;
    LinearLayout btnproses,btnselesai;

    int sukses=0;
    int proses=0;

    TextView alamat;


    private SharedPreferences permissionStatus;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private boolean sentToSettings = false;

    private static final String INTRO_FOCUS_1 = "intro_focus_1";
    private static final String INTRO_FOCUS_2 = "intro_focus_2";
    private static final String INTRO_FOCUS_3 = "intro_focus_3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        f = new Fungsi(this, this);
        super.onCreate(savedInstanceState);
        f.setTransparantStatusBar();
        setContentView(R.layout.beranda);

        if (new InternetDialog(this).getInternetStatus()) {

        }

        tokennya = FirebaseInstanceId.getInstance().getToken();
        f.dr.child("token_surveyor").child(f.sp.getString("id", "")).setValue(tokennya);
        foto=findViewById(R.id.profil);
        nama=findViewById(R.id.nama);
        tvproses=findViewById(R.id.proses);
        tvsukses=findViewById(R.id.sukses);
        empty=findViewById(R.id.empty);
        empty2=findViewById(R.id.empty2);

        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
////      btnproses=findViewById(R.id.btnproses);
//        btnselesai=findViewById(R.id.btnsukses);
//
//        tvproses.setOnClickListener(this);
//        tvsukses.setOnClickListener(this);
        Picasso.with(this).load(f.sp.getString("foto","")).into(foto);
        nama.setText(f.sp.getString("nama",""));

        rv=findViewById(R.id.data);
        rv.setLayoutManager(new GridLayoutManager(this, 1));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SpacesItemDecoration(10));
        ac = new AdapterListHome(Beranda.this, this, listnya);
        rv.setAdapter(ac);

        rv2=findViewById(R.id.datasukses);
        rv2.setLayoutManager(new GridLayoutManager(this, 1));
        rv2.setHasFixedSize(true);
        rv2.addItemDecoration(new SpacesItemDecoration(10));
        ac2 = new AdapterListHome(Beranda.this, this, listnya2);
        rv2.setAdapter(ac2);

        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();



        menubar = (ImageView) findViewById(R.id.menubar);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        cepermission();
        drawer = findViewById(R.id.drawer_layout);
        showIntro(menubar, INTRO_FOCUS_1, "Sidebar", Focus.NORMAL);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);

        alamat=v.findViewById(R.id.alamat);
        alamat.setText(f.sp.getString("lokasi",""));
        navigationView.setNavigationItemSelectedListener(this);
        menubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });



        f.dr.child("daftarinspeksi").orderByChild("surveyor").equalTo(f.sp.getString("id","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot daftarinspeksi) {
                listnya.clear();
                listnya2.clear();
                sukses=0;
                proses=0;
                if(daftarinspeksi.getChildrenCount() > 0){



                    for(final DataSnapshot di: daftarinspeksi.getChildren()){
                        if (di.child("status").getValue(String.class).equals("Sukses")){


                            sukses++;
                            f.dr.child("produk").orderByChild("idnya").equalTo(di.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ModelProduk m = ds.getValue(ModelProduk.class);
                                        m.setTgl(di.child("tgl").getValue(String.class));
                                        m.setFoto(ds.child("foto").getValue(String.class));
                                        m.setNama(di.child("namapemohon").getValue(String.class));


                                        listnya2.add(m);
                                    }
                                    ac2.notifyDataSetChanged();


                                    if (listnya2.size()<1){
                                        empty.setVisibility(View.VISIBLE);
                                        b1.setVisibility(View.VISIBLE);
//                                                empty2.setVisibility(View.GONE);
                                    }else{
                                        empty.setVisibility(View.GONE);
                                        b1.setVisibility(View.GONE);
                                    }

                                    tvsukses.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            Toast.makeText(getApplicationContext(),"SUKSES",Toast.LENGTH_LONG).show();
                                            rv.setVisibility(View.GONE);
                                            rv2.setVisibility(View.VISIBLE);
                                            if (listnya2.size()<1){
                                                empty.setVisibility(View.VISIBLE);
                                                b1.setVisibility(View.VISIBLE);
//                                                empty2.setVisibility(View.GONE);
                                            }else{
                                                empty.setVisibility(View.GONE);
                                                b1.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    tvproses.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            Toast.makeText(getApplicationContext(),"PROSES",Toast.LENGTH_LONG).show();
                                            rv2.setVisibility(View.GONE);
                                            rv.setVisibility(View.VISIBLE);
                                            if (listnya.size()<1){

                                                empty.setVisibility(View.VISIBLE);
                                                b1.setVisibility(View.VISIBLE);
//                                                empty2.setVisibility(View.GONE);
                                            }else{
                                                empty.setVisibility(View.GONE);
                                                b1.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }





                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else{
                            proses++;

                        f.dr.child("produk").orderByChild("idnya").equalTo(di.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds2: dataSnapshot.getChildren()){


                                    ModelProduk m = ds2.getValue(ModelProduk.class);
                                    m.setTgl(di.child("tgl").getValue(String.class));
                                    m.setNama(di.child("namapemohon").getValue(String.class));
                                    m.setFoto(ds2.child("foto").getValue(String.class));
                                    listnya.add(m);
                                }
                                ac.notifyDataSetChanged();

                                if (listnya2.size()<1){
                                    empty.setVisibility(View.VISIBLE);
                                    b1.setVisibility(View.VISIBLE);
//                                                empty2.setVisibility(View.GONE);
                                }else{
                                    empty.setVisibility(View.GONE);
                                    b1.setVisibility(View.GONE);
                                }

                                tvsukses.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            Toast.makeText(getApplicationContext(),"SUKSES",Toast.LENGTH_LONG).show();
                                            rv.setVisibility(View.GONE);
                                            rv2.setVisibility(View.VISIBLE);
                                            if (listnya2.size()<1){
                                                empty.setVisibility(View.VISIBLE);
                                                b1.setVisibility(View.VISIBLE);
//                                                empty2.setVisibility(View.GONE);
                                            }else{
                                                empty.setVisibility(View.GONE);
                                                b1.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                tvproses.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Toast.makeText(getApplicationContext(),"PROSES",Toast.LENGTH_LONG).show();
                                        rv2.setVisibility(View.GONE);
                                        rv.setVisibility(View.VISIBLE);
                                        if (listnya.size()<1){

                                            empty.setVisibility(View.VISIBLE);
                                            b1.setVisibility(View.VISIBLE);
//                                                empty2.setVisibility(View.GONE);
                                        }else{
                                            empty.setVisibility(View.GONE);
                                            b1.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        }

                    }
                    ///sukses
                    tvproses.setText(proses+" PROSES");
                    tvsukses.setText(sukses+" SUKSES");



//                    if (proses==0){
//                        empty.setVisibility(View.VISIBLE);
//                        empty2.setVisibility(View.GONE);
//                        b1.setVisibility(View.VISIBLE);
//                        b2.setVisibility(View.GONE);
//                    }else if (sukses==0){
//                        empty2.setVisibility(View.VISIBLE);
//                        empty.setVisibility(View.GONE);
//                        b1.setVisibility(View.GONE);
//                        b2.setVisibility(View.VISIBLE);
//                    }


                }
                else{
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showIntro(View view, String id, String text, Focus focusType) {
        new MaterialIntroView.Builder(this)
                .setTextColor(R.color.colortext)
                .dismissOnTouch(true)
                .enableDotAnimation(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(focusType)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .setListener(this)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(id) //THIS SHOULD BE UNIQUE ID
                .show();
    }
    @Override
    public void onUserClicked(String materialIntroViewId) {
        if (materialIntroViewId == INTRO_FOCUS_1)
            showIntro(tvproses, INTRO_FOCUS_2, "Data Proses Inspeksi Mobil", MINIMUM);
        else if (materialIntroViewId == INTRO_FOCUS_2)
            showIntro(tvsukses, INTRO_FOCUS_3, "Data Sukses Inspeksi Mobil", Focus.NORMAL);


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

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.proses:
//                rv2.setVisibility(View.GONE);
//                rv.setVisibility(View.VISIBLE);
//
////                if (proses==0){
////                    empty.setVisibility(View.VISIBLE);
////                    b1.setVisibility(View.VISIBLE);
////
////                }else{
////                    empty2.setVisibility(View.GONE);
////                    b2.setVisibility(View.GONE);
////                }
//                break;
//
//
//            case R.id.sukses:
//                rv2.setVisibility(View.VISIBLE);
//                rv.setVisibility(View.GONE);
//
////                if (sukses==0){
////                    empty2.setVisibility(View.VISIBLE);
////                    b2.setVisibility(View.VISIBLE);
////
////                }else{
////                    empty.setVisibility(View.GONE);
////                    b1.setVisibility(View.GONE);
////
////                }
//                break;
//        }
//    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int halfSpace;

        public SpacesItemDecoration(int space) {
            this.halfSpace = space / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getPaddingLeft() != halfSpace) {
                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
                parent.setClipToPadding(false);
            }

            outRect.top = halfSpace;
            outRect.bottom = halfSpace;
            outRect.left = halfSpace;
            outRect.right = halfSpace;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_keluar) {
            Keluar(auth, googleApiClient);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void keluar() {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//        builder.setMessage("Anda yakin ingin keluar?")
//                .setCancelable(false)
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//
//                    }
//                })
//                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                }).show();
//    }



    public void Keluar(FirebaseAuth auth, GoogleApiClient googleApiClient){
        auth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                f.ed.clear();
                f.ed.commit();
                startActivity(new Intent(Beranda.this, Login.class));
            }
        });
    }


    public void cepermission() {
        if (ActivityCompat.checkSelfPermission(Beranda.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Beranda.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Beranda.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Beranda.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Beranda.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Beranda.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Beranda.this);
                builder.setCancelable(false);
                builder.setTitle("Informasi");
                builder.setMessage("Untuk menggunakan aplikasi ini silahkan aktifkan permissions anda...");
                builder.setPositiveButton("Setuju", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Beranda.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                ActivityCompat.requestPermissions(Beranda.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Beranda.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Beranda.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Beranda.this, permissionsRequired[2])) {
//                txtPermissions.setText("Permissions Required");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Beranda.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Izinkan permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Beranda.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Beranda.this, permissionsRequired[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
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
            if (ActivityCompat.checkSelfPermission(Beranda.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}