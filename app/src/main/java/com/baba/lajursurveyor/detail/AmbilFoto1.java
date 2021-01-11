package com.baba.lajursurveyor.detail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyorsurveyor.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

public class AmbilFoto1 extends AppCompatActivity {

    int kodepermission = 10;
    int kodekamera = 8910;
    Fungsi f;
    Uri picUri;
    String idproduk, idkategori, urutan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambil_foto1);
        f = new Fungsi(this, this);
        idproduk = getIntent().getExtras().getString("idproduk");
        idkategori = getIntent().getExtras().getString("idkategori");
        urutan = getIntent().getExtras().getString("urutan");
        cekPermission();
    }

    private void cekPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AmbilFoto1.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                ambilFoto(kodekamera);
            } else {
                if (ContextCompat.checkSelfPermission(AmbilFoto1.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AmbilFoto1.this,
                            android.Manifest.permission.CAMERA)) {
                        new AlertDialog.Builder(AmbilFoto1.this)
                                .setTitle("Lajur")
                                .setMessage("Izinkan aplikasi mengakses kamera.")
                                .setCancelable(false)
                                .setPositiveButton("Izinkan", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(AmbilFoto1.this,
                                                new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                                                kodepermission);
                                        dialogInterface.cancel();
                                        ambilFoto(kodekamera);
                                    }
                                })
                                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        dialog.cancel();
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(AmbilFoto1.this,
                                new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                                kodepermission);
                    }
                }
            }
        }else {
            ambilFoto(kodekamera);
        }
    }

    private void ambilFoto(int kode) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = getOutputMediaFile(1);
            if (photoFile != null) {
                Uri photoURI = anu(photoFile);
                picUri = photoURI;
                f.dr.child("tempFotoInspeksi").child(idproduk).child(idkategori).child(urutan).setValue(Uri.fromFile(photoFile).getPath());
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(pictureIntent, kode);
            }
        }
    }

    public Uri anu(File file){
        Uri uri;
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
//            uri = Uri.fromFile(file);
//        }else{
////            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Movie_pict" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//            uri = FileProvider.getUriForFile(AmbilFoto1.this, "com.baba.surveyor" + ".GenericFileProvider", file);
//        }
        uri = FileProvider.getUriForFile(AmbilFoto1.this, "com.baba.surveyor" + ".fileprovider", file);
        return uri;
    }

    private Bitmap getBitmap(String path) {
        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 100000;
            in = getContentResolver().openInputStream(uri);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;

                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private File getOutputMediaFile(int type){
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "datainspeksi");
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"datainspeksi_lajur");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + f.dr.push().getKey() + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ambilFoto(8910);
                } else {

                    Toast.makeText(AmbilFoto1.this, "Anda belum mengizinkan aplikasi untuk mengambil foto / gambar pada ponsel.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 8910:
                    upload(picUri);
                    break;
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            finish();
        }
    }

    private void upload(final Uri uri){
        f.dr.child("tempFotoInspeksi").child(idproduk).child(idkategori).child(urutan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ambilnamafoto) {
                if(ambilnamafoto.exists()){
                    String imagePath = ambilnamafoto.getValue(String.class);
                    try{
                        getContentResolver().notifyChange(uri, null);
                    }catch(NullPointerException e){

                    }

                    String namaFolder = "dataInspeksi";
                    String namafile = idproduk +"_"+ idkategori +"_"+ urutan + ".jpg";

                    Bitmap reducedSizeBitmap = getBitmap(imagePath);
                    Uri uri2 = getImageUri(getApplicationContext(), reducedSizeBitmap);
                    StorageReference filepath = f.sr.child(namaFolder).child(namafile);

                    filepath.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadURL = taskSnapshot.getDownloadUrl();
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
    }
}