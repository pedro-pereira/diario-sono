package br.ufc.smd.diario.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import br.ufc.smd.diario.R;

public class RegistroEventoActivity extends AppCompatActivity {

    ImageButton btnEventoSono;
    MaterialButton btnEventoExercicio;
    MaterialButton btnEventoRemedio;
    MaterialButton btnEventoBebida;

    Button btnSonoDeitar;
    Button btnSonoLevantar;
    Button btnBebidaCafe;
    Button btnBebidaCha;
    Button btnBebidaRefrigerante;
    Button btnBebidaAlcool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_evento);

        btnEventoSono = findViewById(R.id.btnEventoSono);
        btnEventoExercicio = findViewById(R.id.btnEventoExercicio);
        btnEventoRemedio = findViewById(R.id.btnEventoRemedio);
        btnEventoBebida = findViewById(R.id.btnEventoBebida);

        btnSonoDeitar = findViewById(R.id.btnSonoDeitar);
        btnSonoLevantar = findViewById(R.id.btnSonoLevantar);
        btnBebidaCafe = findViewById(R.id.btnBebidaCafe);
        btnBebidaCha = findViewById(R.id.btnBebidaCha);
        btnBebidaRefrigerante = findViewById(R.id.btnBebidaRefrigerante);
        btnBebidaAlcool = findViewById(R.id.btnBebidaAlcool);

        btnBebidaCafe.setVisibility(View.INVISIBLE);
        btnBebidaCha.setVisibility(View.INVISIBLE);
        btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
        btnBebidaAlcool.setVisibility(View.INVISIBLE);

        btnEventoSono.getBackground().setAlpha( 255 );
        btnEventoExercicio.getBackground().setAlpha( 128 );
        btnEventoRemedio.getBackground().setAlpha( 128 );
        btnEventoBebida.getBackground().setAlpha( 128 );

        btnEventoSono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.VISIBLE);
                btnSonoLevantar.setVisibility(View.VISIBLE);

                btnBebidaCafe.setVisibility(View.INVISIBLE);
                btnBebidaCha.setVisibility(View.INVISIBLE);
                btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
                btnBebidaAlcool.setVisibility(View.INVISIBLE);

                btnEventoSono.getBackground().setAlpha( 255 );
                btnEventoExercicio.getBackground().setAlpha( 128 );
                btnEventoRemedio.getBackground().setAlpha( 128 );
                btnEventoBebida.getBackground().setAlpha( 128 );
            }
        });

        btnEventoExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.INVISIBLE);
                btnSonoLevantar.setVisibility(View.INVISIBLE);

                btnBebidaCafe.setVisibility(View.INVISIBLE);
                btnBebidaCha.setVisibility(View.INVISIBLE);
                btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
                btnBebidaAlcool.setVisibility(View.INVISIBLE);

                btnEventoSono.getBackground().setAlpha( 128 );
                btnEventoExercicio.getBackground().setAlpha( 255 );
                btnEventoRemedio.getBackground().setAlpha( 128 );
                btnEventoBebida.getBackground().setAlpha( 128 );
            }
        });

        btnEventoRemedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.INVISIBLE);
                btnSonoLevantar.setVisibility(View.INVISIBLE);

                btnBebidaCafe.setVisibility(View.INVISIBLE);
                btnBebidaCha.setVisibility(View.INVISIBLE);
                btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
                btnBebidaAlcool.setVisibility(View.INVISIBLE);

                btnEventoSono.getBackground().setAlpha( 128 );
                btnEventoExercicio.getBackground().setAlpha( 128 );
                btnEventoRemedio.getBackground().setAlpha( 255 );
                btnEventoBebida.getBackground().setAlpha( 128 );
            }
        });

        btnEventoBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.INVISIBLE);
                btnSonoLevantar.setVisibility(View.INVISIBLE);

                btnBebidaCafe.setVisibility(View.VISIBLE);
                btnBebidaCha.setVisibility(View.VISIBLE);
                btnBebidaRefrigerante.setVisibility(View.VISIBLE);
                btnBebidaAlcool.setVisibility(View.VISIBLE);

                btnEventoSono.getBackground().setAlpha( 128 );
                btnEventoExercicio.getBackground().setAlpha( 128 );
                btnEventoRemedio.getBackground().setAlpha( 128 );
                btnEventoBebida.getBackground().setAlpha( 255 );
            }
        });
    }
}
