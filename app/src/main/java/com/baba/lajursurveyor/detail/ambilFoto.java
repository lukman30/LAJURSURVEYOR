package com.baba.lajursurveyor.detail;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import com.asksira.bsimagepicker.BSImagePicker;
import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyorsurveyor.BuildConfig;
import com.baba.lajursurveyorsurveyor.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

public class ambilFoto extends AppCompatActivity implements
    BSImagePicker.OnSingleImageSelectedListener,
    BSImagePicker.ImageLoaderDelegate, View.OnClickListener{
    Fungsi f;
    Button Simpan;
    ImageView fotonya;
    String idproduk, idkategori, urutan;
    Uri urinya;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambil_foto);
        f = new Fungsi(this, this);
        idproduk = getIntent().getExtras().getString("idproduk");
        idkategori = getIntent().getExtras().getString("idkategori");
        urutan = getIntent().getExtras().getString("urutan");
        Simpan =findViewById(R.id.btn_simpan);
        fotonya =findViewById(R.id.foto);

        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.baba.surveyor.fileprovider")
                .hideCameraTile()
                .hideGalleryTile()
                .build();
        pickerDialog.show(getSupportFragmentManager(), "satu");

        Simpan.setOnClickListener(this);
        fotonya.setOnClickListener(this);
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Picasso.with(this).load(imageUri).into(ivImage);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        urinya = uri;
        Picasso.with(this).load(urinya).into(fotonya);
    }

    @Override
    public void onClick(View view) {
        if (view == Simpan){
            f.dr.child("tempFotoInspeksi").child(idproduk).child(idkategori).child(urutan).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ambilnamafoto) {
                    if(ambilnamafoto.exists()){
                        File file = new File(SiliCompressor.with(ambilFoto.this).compress(FileUtils.getPath(ambilFoto.this, urinya),new File(getCacheDir(), String.valueOf(urinya))));
                        Uri uri = Uri.fromFile(file);
                        String namaFolder = "dataInspeksi";
                        String namafile = idproduk +"_"+ idkategori +"_"+ urutan + ".jpg";
                        StorageReference filepath = f.sr.child(namaFolder).child(namafile);
                        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Uri downloadURL = taskSnapshot.getDownloadUrl();
                                Log.d("koceng", "onSuccess: "+downloadURL);
                                f.dr.child("tempInspeksi").child(idproduk).child(idkategori).child(urutan).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot tempInspeksi) {
                                        HashMap<String, Object> update = new HashMap();
                                        if(tempInspeksi.exists()){
                                            update.put("tempInspeksi/"+idproduk+"/"+idkategori+"/"+urutan+"/foto", downloadURL.toString());
                                        }else{
                                            update.put("tempInspeksi/"+idproduk+"/"+idkategori+"/"+urutan+"/foto", downloadURL.toString());
                                            update.put("tempInspeksi/"+idproduk+"/"+idkategori+"/"+urutan+"/keterangan", "");
                                        }
                                        f.dr.updateChildren(update, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else if(view == fotonya){
            BSImagePicker pickerDialog = new BSImagePicker.Builder("com.baba.surveyor.fileprovider")
                    .hideCameraTile()
                    .hideGalleryTile()
                    .build();
            pickerDialog.show(getSupportFragmentManager(), "dua");
        }
    }
}
