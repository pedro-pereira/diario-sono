package br.ufc.smd.diario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.formatter.EventoAdapter;
import br.ufc.smd.diario.model.Evento;
import br.ufc.smd.diario.model.Usuario;

public class ListaEventosActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Usuario usuario;
    Calendar calendario;

    private RecyclerView eventoRV;

    private ArrayList<Evento> eventoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        db = FirebaseFirestore.getInstance();
        eventoArrayList = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                usuario = (Usuario) params.getSerializable("usuario");
                calendario = (Calendar) params.getSerializable("dataSelecionada");
            }
        }

        calendario.set(calendario.get(Calendar.YEAR),
                       calendario.get(Calendar.MONTH),
                       calendario.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date d0 = calendario.getTime();

        calendario.set(calendario.get(Calendar.YEAR),
                       calendario.get(Calendar.MONTH),
                       calendario.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        Date d1 = calendario.getTime();

        db.collection("usuarios")
                .document(usuario.getUsuario())
                .collection("eventos")
                .whereGreaterThan("momento", d0)
                .whereLessThan("momento", d1)
                .orderBy("momento", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.i("RESUMO", document.getId() + " => " + document.getData());

                                String idEvento = "", tipoEvento = "", subEvento = "", duracao = "", observacao = "", situacao = "";
                                Date momento = null;

                                if(document.getData().get("tipoEvento") != null) { tipoEvento = document.getData().get("tipoEvento").toString(); }
                                if(document.getData().get("subEvento")  != null) { subEvento  = document.getData().get("subEvento").toString(); }
                                if(document.getData().get("momento")    != null) { momento    = ((com.google.firebase.Timestamp) document.getData().get("momento")).toDate(); }
                                if(document.getData().get("duracao")    != null) { duracao    = document.getData().get("duracao").toString(); }
                                if(document.getData().get("observacao") != null) { observacao = document.getData().get("observacao").toString(); }
                                if(document.getData().get("situacao")   != null) { situacao   = document.getData().get("situacao").toString(); }

                                eventoArrayList.add(new Evento(document.getId(), tipoEvento, subEvento, momento, duracao, observacao, situacao));
                            }

                            eventoRV = findViewById(R.id.idRVListaEventos);
                            EventoAdapter eventoAdapter = new EventoAdapter(ListaEventosActivity.this, eventoArrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            eventoRV.setLayoutManager(linearLayoutManager);
                            eventoRV.setAdapter(eventoAdapter);

                        } else {
                            Log.d("RESUMO", "Erro ao recuperar eventos do dia...", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ListaEventosActivity.this, PrincipalActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}