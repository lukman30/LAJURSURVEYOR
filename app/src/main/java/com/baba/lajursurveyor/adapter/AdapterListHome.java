package com.baba.lajursurveyor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baba.lajursurveyor.detail.Detail;
import com.baba.lajursurveyor.model.ModelProduk;
import com.baba.lajursurveyorsurveyor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListHome extends RecyclerView.Adapter<AdapterListHome.Vhnya>{
    Context c;
    Activity a;
    List<ModelProduk> list;

    public AdapterListHome(Context c, Activity a, List<ModelProduk> list) {
        this.c = c;
        this.a = a;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterListHome.Vhnya onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.listberanda,null);

        return new AdapterListHome.Vhnya(v);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListHome.Vhnya h, int position) {
        final ModelProduk  m = list.get(position);
        h.produk.setText(m.getMerk());
        h.model.setText(" - "+m.getModel());
        h.tanggal.setText("Tanggal : "+m.getTgl());
        Picasso.with(c).load(m.getFoto()).into(h.gambar);
        h.penjual.setText(m.getNama());

        h.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update = new Intent(c, Detail.class);
                update.putExtra("update", 1);
                update.putExtra("merk", m.getMerk());
                update.putExtra("id", m.getIdnya());
                c.startActivity(update);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Vhnya extends RecyclerView.ViewHolder {

        CardView l1;
        TextView produk, model,tanggal,penjual;
        CircleImageView gambar;

        public Vhnya (View v){
            super(v);
            penjual=v.findViewById(R.id.penjual);
            produk = v.findViewById(R.id.produk);
            gambar = v.findViewById(R.id.gambar);
            model = v.findViewById(R.id.model);
            tanggal = v.findViewById(R.id.tanggal);
            l1=v.findViewById(R.id.list);

        }
    }
}
