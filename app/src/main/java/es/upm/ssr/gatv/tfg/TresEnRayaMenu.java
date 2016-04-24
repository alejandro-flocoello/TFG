package es.upm.ssr.gatv.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class TresEnRayaMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView texto = (TextView) findViewById(R.id.titulo);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.girozoom);
        texto.startAnimation(animacion);

        Button jugar = (Button) findViewById(R.id.jugar);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.aparecer);
        jugar.startAnimation(animacion2);

        Button config = (Button) findViewById(R.id.acercaDe);
        Animation animacion3 = AnimationUtils.loadAnimation(this, R.anim.desplazarderecha);
        config.startAnimation(animacion3);

        Button acercade = (Button) findViewById(R.id.salir);
        Animation animacion4 = AnimationUtils.loadAnimation(this,R.anim.aparecer);
        acercade.startAnimation(animacion4);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void pulsaSalir(View view){
        finish();
    }

    public void pulsaJugar(View view){
        Intent i = new Intent(this, TresEnRayaActivity.class);
        startActivity(i);
    }

    public void pulsaJugarCpu(View view){

        Intent i = new Intent(this, JuegoCpu.class);
        startActivity(i);
    }

    public void pulsaAcercaDe(View view){

        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

}
