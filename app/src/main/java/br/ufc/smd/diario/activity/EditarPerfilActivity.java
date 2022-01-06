package br.ufc.smd.diario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.model.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {

    FirebaseFirestore db;

    Usuario usuario;

    private EditText edtNovoNome;
    private EditText edtNovoDataNascimento;
    private EditText edtNovoGenero;
    private EditText edtNovoCpf;
    private EditText edtNovoTelefone;
    private EditText edtNovoLogin;
    private EditText edtNovoSenha;
    private EditText edtNovoConfirmarSenha;
    private Button btCriar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                usuario = (Usuario) params.getSerializable("usuario");
            }
        }

        db = FirebaseFirestore.getInstance();

        edtNovoNome            = findViewById(R.id.edtNovoNome);
        edtNovoDataNascimento  = findViewById(R.id.edtNovoDataNascimento);
        edtNovoGenero          = findViewById(R.id.edtNovoGenero);
        edtNovoCpf             = findViewById(R.id.edtNovoCpf);
        edtNovoTelefone        = findViewById(R.id.edtNovoTelefone);
        edtNovoLogin           = findViewById(R.id.edtNovoLogin);
        edtNovoSenha           = findViewById(R.id.edtNovoSenha);
        edtNovoConfirmarSenha  = findViewById(R.id.edtNovoConfirmarSenha);
        btCriar                = findViewById(R.id.btCriar);

        edtNovoNome            .setText(usuario.getNome());
        // edtNovoDataNascimento  .setText(usuario.);
        // edtNovoGenero          .setText(usuario.);
        // edtNovoCpf             .setText(usuario.);
        edtNovoTelefone        .setText(usuario.getTelefone());
        edtNovoLogin           .setText(usuario.getUsuario());
        edtNovoSenha           .setText(usuario.getSenha());
        edtNovoConfirmarSenha  .setText(usuario.getSenha());

        edtNovoLogin.setEnabled(false);
        edtNovoLogin.setBackgroundColor(getResources().getColor(R.color.cinzaBorda));

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
                        Toast.makeText(EditarPerfilActivity.this, "Perfil alterado...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarPerfilActivity.this, PrincipalActivity.class);
                        intent.putExtra("usuario", u);
                        startActivity(intent);
                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Erro ao editar perfil...", e);
                            }
                        });
            }
        });
    }

    // this event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EditarPerfilActivity.this, PrincipalActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
