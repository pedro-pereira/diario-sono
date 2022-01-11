package br.ufc.smd.diario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        edtNovoCpf             = findViewById(R.id.edtNovoCpf);
        edtNovoTelefone        = findViewById(R.id.edtNovoTelefone);
        edtNovoLogin           = findViewById(R.id.edtNovoLogin);
        edtNovoSenha           = findViewById(R.id.edtNovoSenha);
        edtNovoConfirmarSenha  = findViewById(R.id.edtNovoConfirmarSenha);
        btCriar                = findViewById(R.id.btCriar);

        edtNovoNome            .setText(usuario.getNome());
        edtNovoDataNascimento  .setText(new SimpleDateFormat("dd/MM/yyyy").format(usuario.getDataNascimento()));
        edtNovoCpf             .setText(usuario.getCpf());
        edtNovoTelefone        .setText(usuario.getTelefone());
        edtNovoLogin           .setText(usuario.getUsuario());
        edtNovoSenha           .setText(usuario.getSenha());
        edtNovoConfirmarSenha  .setText(usuario.getSenha());

        edtNovoLogin.setEnabled(false);
        edtNovoLogin.setBackgroundColor(getResources().getColor(R.color.cinzaBorda));

        edtNovoDataNascimento.addTextChangedListener(tw);

        btCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(edtNovoNome.getText()) ||
                    TextUtils.isEmpty(edtNovoDataNascimento.getText()) ||
                    TextUtils.isEmpty(edtNovoCpf.getText()) ||
                    TextUtils.isEmpty(edtNovoTelefone.getText()) ||
                    TextUtils.isEmpty(edtNovoLogin.getText()) ||
                    TextUtils.isEmpty(edtNovoSenha.getText()) ||
                    TextUtils.isEmpty(edtNovoConfirmarSenha.getText())
                ) {
                    Toast.makeText(EditarPerfilActivity.this, "Todos os campos são obrigatórios.", Toast.LENGTH_LONG).show();
                } else {
                    Usuario u = new Usuario();
                    u.setNome(edtNovoNome.getText().toString());
                    u.setDataNascimento(new Date(edtNovoDataNascimento.getText().toString()));
                    u.setCpf(edtNovoCpf.getText().toString());
                    u.setTelefone(edtNovoTelefone.getText().toString());
                    u.setUsuario(edtNovoLogin.getText().toString());
                    u.setSenha(edtNovoSenha.getText().toString());

                    db.collection("usuarios")
                        .document(u.getUsuario())
                        .set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditarPerfilActivity.this, "Perfil alterado...", Toast.LENGTH_LONG).show();
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

    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                edtNovoDataNascimento.setText(current);
                edtNovoDataNascimento.setSelection(sel < current.length() ? sel : current.length());
            }
        }
    };

}
