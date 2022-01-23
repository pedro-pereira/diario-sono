// https://www.viralandroid.com/2016/03/android-material-design-profile-screen-xml-ui-design.html

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
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    private TextView txtBoasVindas;

    private TextView txtEditarPerfil;
    private TextView txtNotificacoes;
    private TextView txtConfiguracoes;
    private TextView txtSair;

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
        txtEditarPerfil  = view.findViewById(R.id.txtEditarPerfil);
        txtNotificacoes  = view.findViewById(R.id.txtNotificacoes);
        txtConfiguracoes = view.findViewById(R.id.txtConfiguracoes);
        txtSair          = view.findViewById(R.id.txtSair);

        txtBoasVindas.setText("Olá, \n" + usuario.getUsuario());

        txtEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = ((PrincipalActivity) getActivity()).usuario;
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                intent.putExtra("usuario", u);
                startActivity(intent);
            }
        });

        txtNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = ((PrincipalActivity) getActivity()).usuario;
                Intent intent = new Intent(getActivity(), NotificacaoActivity.class);
                intent.putExtra("usuario", u);
                startActivity(intent);
            }
        });

        txtConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = ((PrincipalActivity) getActivity()).usuario;
                Intent intent = new Intent(getActivity(), ConfiguracaoActivity.class);
                intent.putExtra("usuario", u);
                startActivity(intent);
            }
        });

        txtSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PrincipalActivity) getActivity()).usuario = null;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
