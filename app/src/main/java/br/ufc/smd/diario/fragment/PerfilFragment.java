package br.ufc.smd.diario.fragment;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.ConfiguracaoActivity;
import br.ufc.smd.diario.activity.EditarPerfilActivity;
import br.ufc.smd.diario.activity.LoginActivity;
import br.ufc.smd.diario.activity.NotificacaoActivity;
import br.ufc.smd.diario.activity.PrincipalActivity;
import br.ufc.smd.diario.model.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    private TextView txtBoasVindas;

    private Button btnEditarPerfil;
    private Button btnNotificacoes;
    private Button btnSair;

    private Button btnConfiguracao;

    Usuario usuario;

    public PerfilFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        usuario = ((PrincipalActivity) getActivity()).usuario;

        txtBoasVindas    = view.findViewById(R.id.txtBoasVindas);

        btnEditarPerfil  = view.findViewById(R.id.btnEditarPerfil);
        btnNotificacoes  = view.findViewById(R.id.btnNotificacoes);
        btnSair          = view.findViewById(R.id.btnSair);

        btnConfiguracao          = view.findViewById(R.id.btnConfiguracao);

        txtBoasVindas.setText(txtBoasVindas.getText() + usuario.getUsuario());

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = ((PrincipalActivity) getActivity()).usuario;
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                intent.putExtra("usuario", u);
                startActivity(intent);
            }
        });

        btnNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = ((PrincipalActivity) getActivity()).usuario;
                Intent intent = new Intent(getActivity(), NotificacaoActivity.class);
                intent.putExtra("usuario", u);
                startActivity(intent);
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PrincipalActivity) getActivity()).usuario = null;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnConfiguracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = ((PrincipalActivity) getActivity()).usuario;
                Intent intent = new Intent(getActivity(), ConfiguracaoActivity.class);
                intent.putExtra("usuario", u);
                startActivity(intent);
            }
        });

        return view;
    }
}
