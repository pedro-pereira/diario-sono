package br.ufc.smd.diario.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.model.Usuario;

public class NovoUsuarioActivity extends AppCompatActivity {

    FirebaseFirestore db;

    private EditText edtNovoLogin;
    private EditText edtNovoSenha;
    private EditText edtNovoNome;
    private EditText edtNovoEmail;
    private EditText edtNovoTelefone;
    private Button   btCriar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        db = FirebaseFirestore.getInstance();

        edtNovoLogin    = findViewById(R.id.edtNovoLogin);
        edtNovoSenha    = findViewById(R.id.edtNovoSenha);
        edtNovoNome     = findViewById(R.id.edtNovoNome);
        edtNovoEmail    = findViewById(R.id.edtNovoEmail);
        edtNovoTelefone = findViewById(R.id.edtNovoTelefone);
        btCriar         = findViewById(R.id.btCriar);

        btCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = new Usuario();
                u.setUsuario(edtNovoLogin.getText().toString());
                u.setSenha(edtNovoSenha.getText().toString());
                u.setNome(edtNovoNome.getText().toString());
                u.setEmail(edtNovoEmail.getText().toString());
                u.setTelefone(edtNovoTelefone.getText().toString());

                db.collection("usuarios")
                        .document(u.getUsuario())
                        .set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NovoUsuarioActivity.this, "Novo usuário cadastrado...", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Novo usuário cadastrado...");

                        Intent intent = new Intent(NovoUsuarioActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Erro ao cadastrar usuário...", e);
                            }
                        });
            }
        });
    }
}