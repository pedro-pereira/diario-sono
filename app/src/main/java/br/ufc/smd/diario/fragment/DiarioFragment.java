package br.ufc.smd.diario.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.PrincipalActivity;
import br.ufc.smd.diario.model.Evento;
import br.ufc.smd.diario.model.Usuario;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class DiarioFragment extends Fragment {

    FirebaseFirestore db;
    Button btnSalvar;

    Button btnEventoSono;
    Button btnEventoExercicio;
    Button btnEventoRemedio;
    Button btnEventoBebida;

    Button btnSonoDeitar;
    Button btnSonoLevantar;
    Button btnBebidaCafe;
    Button btnBebidaCha;
    Button btnBebidaRefrigerante;
    Button btnBebidaAlcool;

    EditText edtObservacao;

    TimePicker spnMomentoHora;

    String tipoEvento, subEvento;
    Calendar momentoData;
    int momentoHora;
    int momentoMinuto;

    public Usuario usuario;
    Evento evento;

    Drawable drawableEventoSonoHabilitado;
    Drawable drawableEventoSonoDesabilitado;
    Drawable drawableEventoExercicioHabilitado;
    Drawable drawableEventoExercicioDesabilitado;
    Drawable drawableEventoRemedioHabilitado;
    Drawable drawableEventoRemedioDesabilitado;
    Drawable drawableEventoBebidaHabilitado;
    Drawable drawableEventoBebidaDesabilitado;

    public DiarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diario, container, false);

        // get fragment manager so we can launch from fragment
        // final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        drawableEventoSonoHabilitado        = getResources().getDrawable(R.drawable.ic_dormir_habilitado);
        drawableEventoSonoDesabilitado      = getResources().getDrawable(R.drawable.ic_dormir_desabilitado);
        drawableEventoExercicioHabilitado   = getResources().getDrawable(R.drawable.ic_exercicio_habilitado);
        drawableEventoExercicioDesabilitado = getResources().getDrawable(R.drawable.ic_exercicio_desabilitado);
        drawableEventoRemedioHabilitado     = getResources().getDrawable(R.drawable.ic_remedio_habilitado);
        drawableEventoRemedioDesabilitado   = getResources().getDrawable(R.drawable.ic_remedio_desabilitado);
        drawableEventoBebidaHabilitado      = getResources().getDrawable(R.drawable.ic_bebida_habilitado);
        drawableEventoBebidaDesabilitado    = getResources().getDrawable(R.drawable.ic_bebida_desabilitado);

        btnEventoSono         = view.findViewById(R.id.btnEventoSono);
        btnEventoExercicio    = view.findViewById(R.id.btnEventoExercicio);
        btnEventoRemedio      = view.findViewById(R.id.btnEventoRemedio);
        btnEventoBebida       = view.findViewById(R.id.btnEventoBebida);

        btnSonoDeitar         = view.findViewById(R.id.btnSonoDeitar);
        btnSonoLevantar       = view.findViewById(R.id.btnSonoLevantar);
        btnBebidaCafe         = view.findViewById(R.id.btnBebidaCafe);
        btnBebidaCha          = view.findViewById(R.id.btnBebidaCha);
        btnBebidaRefrigerante = view.findViewById(R.id.btnBebidaRefrigerante);
        btnBebidaAlcool       = view.findViewById(R.id.btnBebidaAlcool);

        edtObservacao         = view.findViewById(R.id.edtObservacao);

        spnMomentoHora        = view.findViewById(R.id.spnMomentoHora);

        tratarTelaPorEvento("SONO", "DEITAR");

        momentoData = Calendar.getInstance();
        momentoHora = momentoData.getTime().getHours();
        momentoMinuto = momentoData.getTime().getMinutes();

        btnSalvar       = view.findViewById(R.id.btnSalvar);
        usuario = ((PrincipalActivity) getActivity()).usuario;

        btnEventoSono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("SONO", "DEITAR");
            }
        });

        btnEventoExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("EXERCICIO", "NaN");
            }
        });

        btnEventoRemedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("REMEDIO", "NaN");
            }
        });

        btnEventoBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("BEBIDA", "CAFE");
            }
        });

        // "Aba" de registro de sono - Início
        btnSonoDeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("SONO", "DEITAR");
            }
        });

        btnSonoLevantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("SONO", "LEVANTAR");
            }
        });
        // "Aba" de registro de sono - Fim

        // "Aba" de registro de bebida - Início
        btnBebidaCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("BEBIDA", "CAFE");
            }
        });

        btnBebidaCha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("BEBIDA", "CHA");
            }
        });

        btnBebidaRefrigerante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("BEBIDA", "REFRIGERANTE");
            }
        });

        btnBebidaAlcool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarTelaPorEvento("BEBIDA", "ALCOOL");
            }
        });
        // "Aba" de registro de bebida - Fim

        // Componente de Data - Início
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                    // on below line we are adding a range as start date and end date to our calendar.
                    .range(startDate, endDate)
                    // on below line we are providing a number of dates which will be visible on the screen at a time.
                    .datesNumberOnScreen(5)
                    // at last we are calling a build method to build our horizontal recycler view.
                    .build();
        // Componente de Data - Fim

        // Componente de Hora - Início
        spnMomentoHora.setIs24HourView(false); // used to display AM/PM mode
        spnMomentoHora.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                momentoHora = hourOfDay;
                momentoMinuto = minute;
            }
        });
        // Componente de Hora - Fim

        // Trata cenário de edição de evento - Início
        Intent quemChamou = getActivity().getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                evento = (Evento) params.getSerializable("eventoSelecionado");
            }
            if(evento != null) {
                tratarTelaPorEvento(evento.getTipoEvento(), evento.getSubEvento());

                spnMomentoHora.setHour(evento.getMomento().getHours());
                spnMomentoHora.setMinute(evento.getMomento().getMinutes());
                edtObservacao.setText(evento.getObservacao());

                Calendar todayDateEdit = Calendar.getInstance();
                todayDateEdit.set(evento.getMomento().getYear(), evento.getMomento().getMonth(), evento.getMomento().getDate());

                Log.i("RESUMO TODAY ", todayDateEdit.toString());

                horizontalCalendar.getSelectedDate().set(evento.getMomento().getYear(), evento.getMomento().getMonth(), evento.getMomento().getDate());
                horizontalCalendar.refresh();
            }
        }
        // Trata cenário de edição de evento - Fim

        // on below line we are setting calendar listener to our calendar view.
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                momentoData = date;
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> valoresEvento = new HashMap<>();
                valoresEvento.put("observacao", edtObservacao.getText().toString());
                valoresEvento.put("tipoEvento", tipoEvento);
                valoresEvento.put("subEvento", subEvento);
                valoresEvento.put("duracao", Long.toString(0L));

                Date data = momentoData.getTime();
                data.setHours(momentoHora);
                data.setMinutes(momentoMinuto);
                valoresEvento.put("momento", data);

                if(tipoEvento.equals("SONO") && subEvento.equals("LEVANTAR")) {
                    db.collection("usuarios")
                            .document(usuario.getUsuario())
                            .collection("eventos")
                            .whereEqualTo("tipoEvento", "SONO")
                            .orderBy("momento")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int tamanhoResultado = task.getResult().size();
                                        DocumentSnapshot document = task.getResult().getDocuments().get(tamanhoResultado - 1);

                                        String temp = document.getData().get("subEvento").toString();
                                        if(temp.equals("DEITAR")) {
                                            Date d0 = ((com.google.firebase.Timestamp) document.getData().get("momento")).toDate();
                                            long diferenca = (data.getTime() - d0.getTime()) / 3600000;
                                            valoresEvento.put("duracao", Long.toString(diferenca));

                                            db.collection("usuarios")
                                                    .document(usuario.getUsuario())
                                                    .collection("eventos")
                                                    .add(valoresEvento)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getActivity(), "Novo evento cadastrado...", Toast.LENGTH_LONG).show();
                                                            edtObservacao.setText("");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "Erro ao cadastrar evento de sono...", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getActivity(), "Você precisa primeiro cadastrar um evento 'DEITAR', antes de cadastrar um evento 'LEVANTAR'", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    } else {
                                        Log.i("RESUMO", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    db.collection("usuarios")
                            .document(usuario.getUsuario())
                            .collection("eventos")
                            .add(valoresEvento)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getActivity(), "Novo evento cadastrado...", Toast.LENGTH_LONG).show();
                                    edtObservacao.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("RESUMO", "Erro ao cadastrar evento de sono...", e);
                                }
                            });
                }
            }
        });
        return view;
    }

    // Método para exibir/ocultar botões baseado no tipo de evento + sub-evento - Início
    public void tratarTelaPorEvento(String tipoEvento, String subEvento) {

        this.tipoEvento = tipoEvento;
        this.subEvento = subEvento;

        if(tipoEvento.equals("SONO")) {
            btnSonoDeitar.setVisibility(View.VISIBLE);
            btnSonoLevantar.setVisibility(View.VISIBLE);

            btnBebidaCafe.setVisibility(View.INVISIBLE);
            btnBebidaCha.setVisibility(View.INVISIBLE);
            btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
            btnBebidaAlcool.setVisibility(View.INVISIBLE);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoHabilitado , null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado , null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado , null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado , null, null);

            if(subEvento.equals("DEITAR")) {
                btnSonoDeitar.setBackgroundColor(getResources().getColor(R.color.botaoSelecionado));
                btnSonoDeitar.setTextColor(getResources().getColor(R.color.textoBotaoSelecionado));

                btnSonoLevantar.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnSonoLevantar.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));
            }

            if(subEvento.equals("LEVANTAR")) {
                btnSonoDeitar.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnSonoDeitar.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnSonoLevantar.setBackgroundColor(getResources().getColor(R.color.botaoSelecionado));
                btnSonoLevantar.setTextColor(getResources().getColor(R.color.textoBotaoSelecionado));
            }
        }

        if(tipoEvento.equals("EXERCICIO")) {
            btnSonoDeitar.setVisibility(View.INVISIBLE);
            btnSonoLevantar.setVisibility(View.INVISIBLE);

            btnBebidaCafe.setVisibility(View.INVISIBLE);
            btnBebidaCha.setVisibility(View.INVISIBLE);
            btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
            btnBebidaAlcool.setVisibility(View.INVISIBLE);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado, null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioHabilitado, null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado, null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado, null, null);
        }

        if(tipoEvento.equals("REMEDIO")) {
            btnSonoDeitar.setVisibility(View.INVISIBLE);
            btnSonoLevantar.setVisibility(View.INVISIBLE);

            btnBebidaCafe.setVisibility(View.INVISIBLE);
            btnBebidaCha.setVisibility(View.INVISIBLE);
            btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
            btnBebidaAlcool.setVisibility(View.INVISIBLE);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado, null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado, null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioHabilitado, null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado, null, null);
        }

        if(tipoEvento.equals("BEBIDA")) {
            btnSonoDeitar.setVisibility(View.INVISIBLE);
            btnSonoLevantar.setVisibility(View.INVISIBLE);

            btnBebidaCafe.setVisibility(View.VISIBLE);
            btnBebidaCha.setVisibility(View.VISIBLE);
            btnBebidaRefrigerante.setVisibility(View.VISIBLE);
            btnBebidaAlcool.setVisibility(View.VISIBLE);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado, null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado, null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado, null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaHabilitado, null, null);

            if(subEvento.equals("CAFE")) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.botaoSelecionado));
                btnBebidaCafe.setTextColor(getResources().getColor(R.color.textoBotaoSelecionado));

                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaCha.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaRefrigerante.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaAlcool.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));
            }

            if(subEvento.equals("CHA")) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaCafe.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.botaoSelecionado));
                btnBebidaCha.setTextColor(getResources().getColor(R.color.textoBotaoSelecionado));

                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaRefrigerante.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaAlcool.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));
            }

            if(subEvento.equals("REFRIGERANTE")) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaCafe.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaCha.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.botaoSelecionado));
                btnBebidaRefrigerante.setTextColor(getResources().getColor(R.color.textoBotaoSelecionado));

                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaAlcool.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));
            }

            if(subEvento.equals("ALCOOL")) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaCafe.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaCha.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.botaoNaoSelecionado));
                btnBebidaRefrigerante.setTextColor(getResources().getColor(R.color.textoBotaoNaoSelecionado));

                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.botaoSelecionado));
                btnBebidaAlcool.setTextColor(getResources().getColor(R.color.textoBotaoSelecionado));
            }
        }
    }
    // Método para exibir/ocultar botões baseado no tipo de evento + sub-evento - Fim
}