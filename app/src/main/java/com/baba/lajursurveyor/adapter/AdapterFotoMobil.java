package com.baba.lajursurveyor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baba.lajursurveyor.detail.DetailFotoMobil;
import com.baba.lajursurveyorsurveyor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFotoMobil extends RecyclerView.Adapter<AdapterFotoMobil.Vhnya> {

    Context c;
    Activity a;
    ArrayList<String> list;
    ArrayList<String> datalengkap;
    int sisa;

    public AdapterFotoMobil(Context c, Activity a, ArrayList<String> list) {
        this.c = c;
        this.a = a;
        this.list = list;
    }

    public void sisafoto(int sisa){
        this.sisa = sisa;
    }

    public void datalengkap(ArrayList<String> datalengkap){
        this.datalengkap = datalengkap;
    }

    @NonNull
    @Override
    public Vhnya onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.fotomobil, null);
        return new Vhnya(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vhnya h, final int position) {
        String m = list.get(position);
        Picasso.with(c).load(m).into(h.gambarmobil);
        if(position == 4 && sisa > 0){
            h.plus.setVisibility(View.VISIBLE);
            h.plus.setText("+"+sisa);
        }else{
            h.plus.setVisibility(View.GONE);
        }
        h.rl.setTag(h);
        h.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] listnya = new String[datalengkap.size()];
                for (int i = 0; i < datalengkap.size(); i++) {
                    listnya[i] = datalengkap.get(i);
                }
                Bundle b = new Bundle();
                b.putStringArray("datalengkap", listnya);
                Intent ke = new Intent(c, DetailFotoMobil.class);
                ke.putExtras(b);
                ke.putExtra("posisi", position);
                a.startActivity(ke);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Vhnya extends RecyclerView.ViewHolder {

        RelativeLayout rl;
        TextView plus;
        ImageView gambarmobil;

        public Vhnya(View v){
            super(v);
            rl = v.findViewById(R.id.rl);
            plus = v.findViewById(R.id.plus);
            gambarmobil = v.findViewById(R.id.gambarmobil);
        }
    }
}
