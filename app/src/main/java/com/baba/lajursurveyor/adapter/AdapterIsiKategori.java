package com.baba.lajursurveyor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baba.lajursurveyor.detail.AmbilFoto1;
import com.baba.lajursurveyor.detail.ambilFoto;
import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyor.model.ModelIsiKategori;
import com.baba.lajursurveyorsurveyor.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterIsiKategori extends RecyclerView.Adapter<AdapterIsiKategori.Vhnya> {

    Context c;
    Activity a;
    String kategori;
    ArrayList<ModelIsiKategori> list;
    Button simpan;
    RecyclerView rv;
    String idkategori, idproduk;
    EditText rangkuman;
    BottomSheetDialog bsd;
    DataSnapshot ds;
    Fungsi f;

    public AdapterIsiKategori(Context c, Activity a, ArrayList<ModelIsiKategori> list, String kategori, Button simpan, RecyclerView rv, String idkategori, String idproduk, EditText rangkuman, BottomSheetDialog bsd, DataSnapshot ds) {
        this.c = c;
        this.a = a;
        this.list = list;
        this.kategori = kategori;
        this.simpan = simpan;
        this.rv = rv;
        this.idkategori = idkategori;
        this.idproduk = idproduk;
        this.ds = ds;
        this.rangkuman = rangkuman;
        if (ds != null) {
            this.rangkuman.setText(ds.child("rangkuman").getValue(String.class));
        }
        this.bsd = bsd;
        f = new Fungsi(c, a);
    }

    @NonNull
    @Override
    public Vhnya onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.itemisikategori, null);
        return new Vhnya(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vhnya h, final int position) {
        final ModelIsiKategori m = list.get(position);
        h.judul.setText(kategori + " - " + m.getJudul());
        f.dr.child("tempInspeksi").child(idproduk).child(idkategori).child(m.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                View ve = rv.getChildAt(position);
                TextView textView = ve.findViewById(R.id.keterangan);
                final ImageView foto = ve.findViewById(R.id.gambar);

                if (ve != null) {
                    foto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ke = new Intent(c, AmbilFoto1.class);
                            ke.putExtra("idproduk", idproduk);
                            ke.putExtra("idkategori", idkategori);
                            ke.putExtra("urutan", m.getId());
                            a.startActivity(ke);
                        }
                    });
                }

                if (dataSnapshot.exists() && !dataSnapshot.getKey().equals("rangkuman")) {
                    textView.setText(dataSnapshot.child("keterangan").getValue(String.class));
                    Picasso.with(c).load(dataSnapshot.child("foto").getValue(String.class)).into(foto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pathnya = "/tempInspeksi/" + idproduk + "/" + idkategori + "/";
                HashMap<String, Object> map = new HashMap();
                boolean cekketerangan = true, cekfoto = true;
                for (int i = 0; i < list.size(); i++) {
                    View ve = rv.getChildAt(i);
                    if (ve != null) {
                        TextView textView = ve.findViewById(R.id.keterangan);
                        ImageView foto = ve.findViewById(R.id.gambar);

                        String keterangan = textView.getText().toString();
                        if (TextUtils.isEmpty(keterangan)) {
                            cekketerangan = false;
                        }
                        if (foto.getDrawable().getConstantState() == a.getResources().getDrawable(R.drawable.noimage).getConstantState()) {
                            cekfoto = false;
                        }
                        map.put(pathnya + "" + list.get(i).getId() + "/keterangan", keterangan);
                    }
                }
                map.put(pathnya + "/rangkuman", rangkuman.getText().toString());

                if(cekketerangan && cekfoto){
                    f.dr.updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {

                            } else {
                                bsd.cancel();
                            }
                        }
                    });
                } else {
                    Toast.makeText(c, "Pastikan Anda telah mengisi seluruh form dengan benar..!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Vhnya extends RecyclerView.ViewHolder {

        TextView judul;
        ImageView gambar;
        EditText keterangan;

        public Vhnya(View v) {
            super(v);

            judul = v.findViewById(R.id.judul);
            gambar = v.findViewById(R.id.gambar);
            keterangan = v.findViewById(R.id.keterangan);
        }
    }
}
