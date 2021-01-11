package com.baba.lajursurveyor.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyorsurveyor.R;
import com.squareup.picasso.Picasso;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class DetailFotoMobil extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView storiesProgressView;
    private ImageView image;
    Fungsi f;

    long pressTime = 0L;
    long limit = 500L;
    private String[] datalengkap;
    int posisi = 0;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    final long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_foto_mobil);

        f = new Fungsi(this, this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        f.setTransparantStatusBar();

        Bundle b = this.getIntent().getExtras();
        datalengkap = b.getStringArray("datalengkap");
        posisi = getIntent().getExtras().getInt("posisi");

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(datalengkap.length);
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(posisi);

        image = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(datalengkap[posisi]).into(image);

        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {
        Picasso.with(this).load(datalengkap[++posisi]).into(image);
    }

    @Override
    public void onPrev() {
        if ((posisi - 1) < 0) return;
        Picasso.with(this).load(datalengkap[--posisi]).into(image);
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }
}