package com.example.escuela;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


public class Principal extends AppCompatActivity {

    private Button start;
    private TextView output;
    private Switch suich;

    private static final String TAG = "Principal";

    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        suich = (Switch) findViewById(R.id.chuich);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EjemploThread ejemploThread = new EjemploThread(10);
                //ejemploThread.start();

                ExampleRunnable exampleRunnable = new ExampleRunnable(10);
                new Thread(exampleRunnable).start();

            }
        });

        suich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    class EjemploThread extends Thread{

        int seconds;

        EjemploThread(int seconds){
            this.seconds=seconds;
        }

        @Override
        public void run() {
            for (int i=0;i <seconds; i++){

                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "onClick: "+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ExampleRunnable implements Runnable{

        int seconds;

        ExampleRunnable(int seconds){
            this.seconds=seconds;
        }

        @Override
        public void run() {
            for (int i=0;i <seconds; i++){



                if(i==5){
                    /*mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            start.setText("50%");
                        }
                    });*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            start.setText("50%");
                        }
                    });

                }


                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "onClick: "+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
