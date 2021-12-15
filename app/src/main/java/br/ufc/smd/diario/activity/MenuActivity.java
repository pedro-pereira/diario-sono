package br.ufc.smd.diario.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufc.smd.diario.R;

public class MenuActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button1         = findViewById(R.id.button1);
        button2         = findViewById(R.id.button2);
        button3         = findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, NovoEventoActivity.class);
                // Intent intent = new Intent(MenuActivity.this, NovoEventoActivity.class);
                startActivity(intent);
                finish();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, GraficoActivity.class);
                startActivity(intent);
                finish();
            }
        });
/*
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, NovoEventoActivity.class);
                startActivity(intent);
                finish();
            }
        });
*/

    }
}