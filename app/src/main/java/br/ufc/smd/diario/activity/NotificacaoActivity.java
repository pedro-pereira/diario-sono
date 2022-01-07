package br.ufc.smd.diario.activity;

import br.ufc.smd.diario.R;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.ufc.smd.diario.formatter.NotificacaoAdapter;
import br.ufc.smd.diario.model.Notificacao;
import br.ufc.smd.diario.model.Usuario;

public class NotificacaoActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Usuario usuario;

    private RecyclerView notificacaoRV;

    // Arraylist for storing data
    private ArrayList<Notificacao> notificacaoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                usuario = (Usuario) params.getSerializable("usuario");
            }
        }

        notificacaoArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("alertas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("RESUMO", document.getId() + " => " + document.getData());
                                notificacaoArrayList.add(new Notificacao(document.getData().get("descricao").toString(),
                                                                        ((com.google.firebase.Timestamp) document.getData().get("dataCadastro")).toDate(),
                                                                        (boolean) document.getData().get("habilitado")));
                            }

                            notificacaoRV = findViewById(R.id.idRVCourse);
                            NotificacaoAdapter notificacaoAdapter = new NotificacaoAdapter(getApplicationContext(), notificacaoArrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            notificacaoRV.setLayoutManager(linearLayoutManager);
                            notificacaoRV.setAdapter(notificacaoAdapter);

                        } else {
                            Log.d("RESUMO", "Erro ao recuperar alertas...", task.getException());
                        }
                    }
                });
    }

    // this event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NotificacaoActivity.this, PrincipalActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
