// https://stackoverflow.com/questions/33055129/how-to-show-a-notification-everyday-at-a-certain-time-even-when-the-app-is-close
// https://stackoverflow.com/questions/47916079/save-and-retrieve-intent-from-sharedpreferences

package br.ufc.smd.diario.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.model.Usuario;

public class ConfiguracaoActivity  extends AppCompatActivity {

    Switch swtLembreteSono;
    Switch swtLembreteExercicio;
    Switch swtLembreteRemedio;
    Switch swtLembreteBebida;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                usuario = (Usuario) params.getSerializable("usuario");
            }
        }

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        swtLembreteSono      = findViewById(R.id.swtLembreteSono);
        swtLembreteExercicio = findViewById(R.id.swtLembreteExercicio);
        swtLembreteRemedio   = findViewById(R.id.swtLembreteRemedio);
        swtLembreteBebida    = findViewById(R.id.swtLembreteBebida);

        swtLembreteSono.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    habilitaAlarme("Lembrete de sono", "Registre as horas de sono...", "SONO", 60);
                } else {
                    desabilitaAlarme("SONO");
                }
            }
        });

        swtLembreteExercicio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    habilitaAlarme("Lembrete de exercício", "Quando vc malhou?", "EXERCICIO", 70);
                } else {
                    desabilitaAlarme("EXERCICIO");
                }
            }
        });

        swtLembreteRemedio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    habilitaAlarme("Lembrete de remédio", "Quando vc se drogou?", "REMEDIO", 80);
                } else {
                    desabilitaAlarme("REMEDIO");
                }
            }
        });

        swtLembreteBebida.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    habilitaAlarme("Lembrete de bebida", "Quando vc bebeu?", "BEBIDA", 90);
                } else {
                    desabilitaAlarme("BEBIDA");
                }
            }
        });
    }

    public void desabilitaAlarme(String acao) {
        Intent intentAlarme = retrieveIntent(acao);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intentAlarme, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void habilitaAlarme(String titulo, String mensagem, String acao, int margemTempo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + margemTempo);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        NotificationReceiver notificationReceiver = new NotificationReceiver();

        notificationReceiver.setTitulo(titulo);
        notificationReceiver.setMensagem(mensagem);

        Intent intentAlarme = new Intent(getApplicationContext(), notificationReceiver.getClass());
        intentAlarme.setAction(acao);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intentAlarme, PendingIntent.FLAG_UPDATE_CURRENT);
        saveIntent(intentAlarme, acao);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES/15, pendingIntent);
        }
    }

    private void saveIntent(Intent intent, String nomeIntent){
        String uri = intent.toUri(0); //intent.toURI() deprecated
        editor.putString(nomeIntent, uri);
        editor.commit();
    }

    private Intent retrieveIntent(String nomeIntent) {
        String intentString = sharedPreferences.getString(nomeIntent, null);
        try {
            Intent intentAlarme = Intent.parseUri(intentString, 0);
            return intentAlarme;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    // this event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ConfiguracaoActivity.this, PrincipalActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}