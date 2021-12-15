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

    private EditText edtNovoNome;
    private EditText edtNovoDataNascimento;
    private EditText edtNovoGenero;
    private EditText edtNovoCpf;
    private EditText edtNovoTelefone;
    private EditText edtNovoLogin;
    private EditText edtNovoSenha;
    private EditText edtNovoConfirmarSenha;
    private Button   btCriar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        db = FirebaseFirestore.getInstance();

        edtNovoNome    = findViewById(R.id.edtNovoNome);
        edtNovoDataNascimento    = findViewById(R.id.edtNovoDataNascimento);
        edtNovoGenero    = findViewById(R.id.edtNovoGenero);
        edtNovoCpf    = findViewById(R.id.edtNovoCpf);
        edtNovoTelefone = findViewById(R.id.edtNovoTelefone);
        edtNovoLogin    = findViewById(R.id.edtNovoLogin);
        edtNovoSenha    = findViewById(R.id.edtNovoSenha);
        edtNovoConfirmarSenha    = findViewById(R.id.edtNovoConfirmarSenha);
        btCriar         = findViewById(R.id.btCriar);

        btCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = new Usuario();
                u.setNome(edtNovoNome.getText().toString());
                u.setNome(edtNovoDataNascimento.getText().toString());
                u.setNome(edtNovoGenero.getText().toString());
                u.setNome(edtNovoCpf.getText().toString());
                u.setTelefone(edtNovoTelefone.getText().toString());
                u.setUsuario(edtNovoLogin.getText().toString());
                u.setSenha(edtNovoSenha.getText().toString());
                u.setEmail(edtNovoConfirmarSenha.getText().toString());

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