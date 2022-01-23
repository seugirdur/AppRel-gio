package com.example.apprelgio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class FullscreenActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean runnableStopped = false;
    private boolean cbBateriaChecked = true;



    private BroadcastReceiver bateriaReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            int nivel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mViewHolder.tv_nivelbateria.setText(String.valueOf(nivel) + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mViewHolder.tv_hor_e_min = findViewById(R.id.tv_hr_e_min);
        mViewHolder.tv_sec = findViewById(R.id.tv_sec);
        mViewHolder.tv_nivelbateria = findViewById(R.id.tv_nivelbateria);
        mViewHolder.cb_nivelBateria = findViewById(R.id.cb_nivelBateria);
        mViewHolder.iv_sair = findViewById(R.id.iv_sair);
        mViewHolder.iv_preferencias = findViewById(R.id.iv_preferencias);
        mViewHolder.ll_menu = findViewById(R.id.ll_menu);




        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        registerReceiver(bateriaReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        mViewHolder.ll_menu.animate().translationY(500);

        mViewHolder.cb_nivelBateria.setChecked(true);

        mViewHolder.cb_nivelBateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbBateriaChecked == true) {
                    cbBateriaChecked = false;
                    mViewHolder.tv_nivelbateria.setVisibility(View.GONE);

                } else {
                    cbBateriaChecked = true;
                    mViewHolder.tv_nivelbateria.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewHolder.iv_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewHolder.ll_menu.animate()
                        .translationY(mViewHolder.ll_menu.getMeasuredHeight())
                        .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));

            }
        });

        mViewHolder.iv_preferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewHolder.ll_menu.setVisibility(View.VISIBLE);
                mViewHolder.ll_menu.animate()
                        .translationY(0)
                        .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        runnableStopped=false;

        AtualizarHora();

    }

    @Override
    protected void onStop() {
        super.onStop();
        runnableStopped=true;
    }

    private void AtualizarHora() {

        runnable = new Runnable() {
            @Override
            public void run() {
                if(runnableStopped)
                    return;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                String hora_e_min = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));

                String sec = String.format("%02d", calendar.get(Calendar.SECOND));

                mViewHolder.tv_hor_e_min.setText(hora_e_min);
                mViewHolder.tv_sec.setText(sec);


                long agora = SystemClock.uptimeMillis();
                long proximo = agora+(1000-(agora%1000));

                handler.postAtTime(runnable, proximo);
            }
        };
        runnable.run();


    }

    private static class ViewHolder{
        TextView tv_hor_e_min;
        TextView tv_sec;
        CheckBox cb_nivelBateria;
        TextView tv_nivelbateria;
        ImageView iv_preferencias, iv_sair;
        LinearLayout ll_menu;
    }
}