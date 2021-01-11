package com.baba.lajursurveyor.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baba.lajursurveyor.model.ModelInfoProduk;
import com.baba.lajursurveyorsurveyor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterInfoProduk extends RecyclerView.Adapter<AdapterInfoProduk.Vhnya> {

    Context c;
    Activity a;
    ArrayList<ModelInfoProduk> list;

    public AdapterInfoProduk(Context c, Activity a, ArrayList<ModelInfoProduk> list) {
        this.c = c;
        this.a = a;
        this.list = list;
    }

    @NonNull
    @Override
    public Vhnya onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.komponenmobil, null);
        return new Vhnya(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vhnya h, int posisi) {
        ModelInfoProduk m = list.get(posisi);
//        Picasso.with(c).load(m.getIkon()).into(h.ikon);


        if (m.getKey().equals("merk")){
            Picasso.with(c).load(R.drawable.no_boundary_merk).into(h.ikon);
        }else if (m.getKey().equals("bb")){
            Picasso.with(c).load(R.drawable.no_boundary_bahanbakar).into(h.ikon);
        }else if (m.getKey().equals("jaraktempuh")){
            Picasso.with(c).load(R.drawable.no_boundary_jarak_tempuh).into(h.ikon);
        }else if (m.getKey().equals("kapasitasmesin")){
            Picasso.with(c).load(R.drawable.no_boundary_kapasitas_mesin).into(h.ikon);
        }else if (m.getKey().equals("model")){
            Picasso.with(c).load(R.drawable.no_boundary_merk).into(h.ikon);
        }else if (m.getKey().equals("tahunmobil")){
            Picasso.with(c).load(R.drawable.no_boundary_tahun).into(h.ikon);
        }else if (m.getKey().equals("tipebody")){
            Picasso.with(c).load(R.drawable.no_boundary_tipe).into(h.ikon);
        }else if (m.getKey().equals("transmisi")){
            Picasso.with(c).load(R.drawable.no_boundary_transmisi).into(h.ikon);
        }else if (m.getKey().equals("warna")){
            Picasso.with(c).load(R.drawable.no_boundary_warna).into(h.ikon);
        }




        h.namakomponen.setText(m.getKomponen());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Vhnya extends RecyclerView.ViewHolder {

        ImageView ikon;
        TextView namakomponen;

        Vhnya(View v){
            super(v);
            ikon = v.findViewById(R.id.ikon);
            namakomponen = v.findViewById(R.id.namakomponen);
        }
    }
}
