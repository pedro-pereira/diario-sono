package br.ufc.smd.diario.formatter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.PrincipalActivity;
import br.ufc.smd.diario.model.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.Viewholder> {

    private Activity eventoActivity;
    private ArrayList<Evento> eventoArrayList;
    private Evento evento;

    public EventoAdapter(Activity eventoActivity, ArrayList<Evento> eventoArrayList) {
        this.eventoActivity = eventoActivity;
        this.eventoArrayList = eventoArrayList;
    }

    @NonNull
    @Override
    public EventoAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_evento, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAdapter.Viewholder holder, int position) {
        evento = eventoArrayList.get(position);

        String tituloEvento;

        if(!evento.getSubEvento().equals("NaN")) {
            tituloEvento = evento.getTipoEvento() + " - " + evento.getSubEvento();
        } else {
            tituloEvento = evento.getTipoEvento();
        }

        holder.txtTipoEvento.setText(tituloEvento);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataEventoFormatada = sdf.format(evento.getMomento());
        holder.txtMomento.setText("Data do evento: " + dataEventoFormatada);

        if(evento.getTipoEvento().equals("SONO") && evento.getSubEvento().equals("LEVANTAR")) {
            holder.txtDuracao.setText("Duração: " + evento.getDuracao());
        }

        holder.txtObservacao.setText("Observação: " + evento.getObservacao());

        /*
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RESUMO CARD", eventoArrayList.get(position).toString());

                Intent intent = new Intent(eventoActivity, PrincipalActivity.class);
                intent.putExtra("eventoSelecionado", eventoArrayList.get(position));
                eventoActivity.startActivity(intent);
            }
        });
         */
    }

    @Override
    public int getItemCount() {
        return eventoArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {

        public CardView cardView;
        private TextView txtTipoEvento, txtMomento, txtDuracao, txtObservacao;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtTipoEvento = itemView.findViewById(R.id.txtTipoEvento);
            txtMomento    = itemView.findViewById(R.id.txtMomento);
            txtDuracao    = itemView.findViewById(R.id.txtDuracao);
            txtObservacao = itemView.findViewById(R.id.txtObservacao);

            cardView      = itemView.findViewById(R.id.cardEvento);
        }
    }
}
