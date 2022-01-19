// https://stackoverflow.com/questions/33055129/how-to-show-a-notification-everyday-at-a-certain-time-even-when-the-app-is-close
// https://stackoverflow.com/questions/47916079/save-and-retrieve-intent-from-sharedpreferences

package br.ufc.smd.diario.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                usuario = (Usuario) params.getSerializable("usuario");
            }
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("configuracaoDiarioSono", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        swtLembreteSono      = findViewById(R.id.swtLembreteSono);
        swtLembreteExercicio = findViewById(R.id.swtLembreteExercicio);
        swtLembreteRemedio   = findViewById(R.id.swtLembreteRemedio);
        swtLembreteBebida    = findViewById(R.id.swtLembreteBebida);

        boolean statusAlarmeSono      = sharedPreferences.getBoolean("STATUS_ALARME_SONO_" + usuario.getUsuario()      , false);
        boolean statusAlarmeExercicio = sharedPreferences.getBoolean("STATUS_ALARME_EXERCICIO_" + usuario.getUsuario() , false);
        boolean statusAlarmeRemedio   = sharedPreferences.getBoolean("STATUS_ALARME_REMEDIO_" + usuario.getUsuario()   , false);
        boolean statusAlarmeBebida    = sharedPreferences.getBoolean("STATUS_ALARME_BEBIDA_" + usuario.getUsuario()    , false);

        swtLembreteSono.setChecked(statusAlarmeSono);
        swtLembreteExercicio.setChecked(statusAlarmeExercicio);
        swtLembreteRemedio.setChecked(statusAlarmeRemedio);
        swtLembreteBebida.setChecked(statusAlarmeBebida);

        swtLembreteSono.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int requestCode = 100;
                if(isChecked) {
                    habilitaAlarme(getString(R.string.tituloLembreteSono), getString(R.string.mensagemLembreteSono), requestCode, "SONO_" + usuario.getUsuario(), 1);
                } else {
                    desabilitaAlarme("SONO_" + usuario.getUsuario(), requestCode);
                }

                editor.putBoolean("STATUS_ALARME_SONO_" + usuario.getUsuario(), isChecked);
                editor.commit();
            }
        });

        swtLembreteExercicio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int requestCode = 200;
                if(isChecked) {
                    habilitaAlarme(getString(R.string.tituloLembreteExercicio), getString(R.string.mensagemLembreteExercicio), requestCode, "EXERCICIO_" + usuario.getUsuario(), 2);
                } else {
                    desabilitaAlarme("EXERCICIO_" + usuario.getUsuario(), requestCode);
                }

                editor.putBoolean("STATUS_ALARME_EXERCICIO_" + usuario.getUsuario(), isChecked);
                editor.commit();
            }
        });

        swtLembreteRemedio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int requestCode = 300;
                if(isChecked) {
                    habilitaAlarme(getString(R.string.tituloLembreteRemedio), getString(R.string.mensagemLembreteRemedio), requestCode, "REMEDIO_" + usuario.getUsuario(), 3);
                } else {
                    desabilitaAlarme("REMEDIO_" + usuario.getUsuario(), requestCode);
                }

                editor.putBoolean("STATUS_ALARME_REMEDIO_" + usuario.getUsuario(), isChecked);
                editor.commit();
            }
        });

        swtLembreteBebida.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int requestCode = 400;
                if(isChecked) {
                    habilitaAlarme(getString(R.string.tituloLembreteBebida), getString(R.string.mensagemLembreteBebida), requestCode, "BEBIDA_" + usuario.getUsuario(), 4);
                } else {
                    desabilitaAlarme("BEBIDA_" + usuario.getUsuario(), requestCode);
                }

                editor.putBoolean("STATUS_ALARME_BEBIDA_" + usuario.getUsuario(), isChecked);
                editor.commit();
            }
        });
    }

    public void desabilitaAlarme(String acao, int requestCode) {
        Intent intentAlarme = retrieveIntent(acao);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intentAlarme, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void habilitaAlarme(String titulo, String mensagem, int requestCode, String acao, int margemTempo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + margemTempo);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intentAlarme = new Intent(getApplicationContext(), NotificationReceiver.class);
        intentAlarme.setAction(acao);
        intentAlarme.putExtra("titulo", titulo);
        intentAlarme.putExtra("mensagem", mensagem);
        intentAlarme.putExtra("requestCode", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intentAlarme, 0);
        saveIntent(intentAlarme, acao);

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