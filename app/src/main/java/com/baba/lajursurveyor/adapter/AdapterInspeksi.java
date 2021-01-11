package com.baba.lajursurveyor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyor.model.ModelIsiKategori;
import com.baba.lajursurveyor.model.ModelKategori;
import com.baba.lajursurveyorsurveyor.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class AdapterInspeksi extends RecyclerView.Adapter<AdapterInspeksi.Vhnya> {

    Context c;
    Activity a;
    ArrayList<ModelKategori> list;
    ArrayList<ModelIsiKategori> listisikategori;
    String idproduk;
    Fungsi f;

    public AdapterInspeksi(Context c, Activity a, ArrayList<ModelKategori> list, String idproduk) {
        this.c = c;
        this.a = a;
        this.list = list;
        this.idproduk = idproduk;
        f = new Fungsi(c, a);
    }

    public void setData(ArrayList<ModelIsiKategori> listisikategori){
        this.listisikategori = listisikategori;
    }

    @NonNull
    @Override
    public Vhnya onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.kategoriinspeksi, null);
        return new Vhnya(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Vhnya h, final int position) {
        final ModelKategori m = list.get(position);
        h.judul.setText(m.getJudul());
        h.ljudul.setTag(h);
        f.dr.child("tempInspeksi").child(idproduk).child(m.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot cekinspeksi) {
                if(cekinspeksi.exists()){
                    h.panah.setImageResource(R.drawable.oke);
                    h.ljudul.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bsd(m.getListisikategori(), m.getJudul(), m.getId(), idproduk, cekinspeksi);
                        }
                    });
                }else{
                    h.panah.setImageResource(R.drawable.panahbawah);
                    h.ljudul.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DataSnapshot kosong = null;
                            bsd(m.getListisikategori(), m.getJudul(), m.getId(), idproduk, kosong);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void bsd(ArrayList<ModelIsiKategori> listisikategori, String jdl, final String idkategori, String idproduk, DataSnapshot ds){
        final BottomSheetDialog bsd = new BottomSheetDialog(c);
        View v = a.getLayoutInflater().inflate(R.layout.bsdisikategori, null);

        RecyclerView rvisikategori = v.findViewById(R.id.rvisikategori);
        rvisikategori.setLayoutManager(new GridLayoutManager(c, 1));
        rvisikategori.setHasFixedSize(true);
        Button simpan = v.findViewById(R.id.simpan);
        EditText rangkuman = v.findViewById(R.id.rangkuman);
        AdapterIsiKategori aik = new AdapterIsiKategori(c, a, listisikategori, jdl, simpan, rvisikategori, idkategori, idproduk, rangkuman, bsd, ds);
        rvisikategori.setAdapter(aik);
        aik.notifyDataSetChanged();

        bsd.setContentView(v);
        bsd.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Vhnya extends RecyclerView.ViewHolder {

        ImageView panah;
        CardView ljudul;
        TextView judul;


        public Vhnya(View v){
            super(v);
            panah = v.findViewById(R.id.panah);
            ljudul = v.findViewById(R.id.ljudul);
            judul = v.findViewById(R.id.kategori);
        }
    }
}
