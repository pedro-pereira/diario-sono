package br.ufc.smd.diario.formatter;

import br.ufc.smd.diario.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.ufc.smd.diario.model.Notificacao;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificacaoAdapter.Viewholder> {

    private Context context;
    private ArrayList<Notificacao> notificacaoArrayList;

    public NotificacaoAdapter(Context context, ArrayList<Notificacao> notificacaoArrayList) {
        this.context = context;
        this.notificacaoArrayList = notificacaoArrayList;
    }

    @NonNull
    @Override
    public NotificacaoAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacaoAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Notificacao notificacao = notificacaoArrayList.get(position);
        holder.txtDescricao.setText(notificacao.getDescricao());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataNotificacaoFormatada = sdf.format(notificacao.getDataCadastro());

        holder.txtDataCadastro.setText("Data da notificação: " + dataNotificacaoFormatada);
        // holder.txtHabilitado.setText(String.valueOf(notificacao.isHabilitado()));
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view.
        return notificacaoArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        // private ImageView courseIV;
        private TextView txtDescricao, txtDataCadastro, txtHabilitado;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtDataCadastro = itemView.findViewById(R.id.txtDataCadastro);
            // txtHabilitado = itemView.findViewById(R.id.txtHabilitado);
        }
    }
}
