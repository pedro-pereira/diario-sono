package br.ufc.smd.diario.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import br.ufc.smd.diario.R;
import br.ufc.smd.diario.fragment.EventoBebidaFragment;
import br.ufc.smd.diario.fragment.EventoExercicioFragment;
import br.ufc.smd.diario.fragment.EventoRemedioFragment;
import br.ufc.smd.diario.fragment.EventoSonoFragment;

import androidx.viewpager.widget.ViewPager;
import android.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;

public class NovoEventoActivity extends AppCompatActivity {
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_evento);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.myviewpager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new EventoSonoFragment(),"Sono");
        viewPagerAdapter.addFragment(new EventoExercicioFragment(), "Exercicio");
        viewPagerAdapter.addFragment(new EventoRemedioFragment(), "Remédio");
        viewPagerAdapter.addFragment(new EventoBebidaFragment(), "Bebida");
        viewPager.setAdapter(viewPagerAdapter);
    }
}