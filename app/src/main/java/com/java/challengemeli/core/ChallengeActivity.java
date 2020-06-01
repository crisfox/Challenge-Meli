package com.java.challengemeli.core;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public abstract class ChallengeActivity extends AppCompatActivity {

    private ProgressDialog instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Metodo para ejecutar el loading, con temporizador en el caso de no recibir respuesta.
    public void startProgressDialog() {
        try {
            if (instance == null) {
                Timer mTimer = new Timer();
                instance = new ProgressDialog(this);
                MyTask task = new MyTask(instance);
                mTimer.schedule(task, 40000);
            }
            instance.setMessage("Cargando...");
            instance.setCancelable(false);
            instance.setOnCancelListener(null);
            if (!instance.isShowing()) {
                instance.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Metodo para frenar el loading
    public void stopProgressDialog() {
        try {
            if (instance != null) {
                instance.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Tarea asignada para el Timer
    static class MyTask extends TimerTask {
        ProgressDialog innerInstance;

        MyTask(ProgressDialog pinstance) {
            innerInstance = pinstance;
        }

        public void run() {
            if (innerInstance != null) {
                innerInstance.dismiss();
                this.cancel();
            }
        }
    }

}
