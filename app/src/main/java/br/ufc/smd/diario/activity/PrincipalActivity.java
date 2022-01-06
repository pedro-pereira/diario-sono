package br.ufc.smd.diario.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.fragment.CalendarioFragment;
import br.ufc.smd.diario.fragment.PerfilFragment;
import br.ufc.smd.diario.fragment.GraficoFragment;
import br.ufc.smd.diario.fragment.DiarioFragment;
import br.ufc.smd.diario.model.Usuario;

public class PrincipalActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    DiarioFragment diarioFragment;
    CalendarioFragment calendarioFragment;
    GraficoFragment graficoFragment;
    PerfilFragment perfilFragment;

    public Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        diarioFragment = new DiarioFragment();
        calendarioFragment = new CalendarioFragment();
        graficoFragment = new GraficoFragment();
        perfilFragment = new PerfilFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menuDiario);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                usuario = (Usuario) params.getSerializable("usuario");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDiario:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, diarioFragment).commit();
                return true;

            case R.id.menuCalendario:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, calendarioFragment).commit();
                return true;

            case R.id.menuGrafico:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, graficoFragment).commit();
                return true;
            case R.id.menuPerfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, perfilFragment).commit();
                return true;
        }
        return false;
    }
}