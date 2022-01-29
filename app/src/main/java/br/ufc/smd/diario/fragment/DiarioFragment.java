package br.ufc.smd.diario.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.PrincipalActivity;
import br.ufc.smd.diario.model.Evento;
import br.ufc.smd.diario.model.Usuario;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class DiarioFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static final int DIALOG_REQUEST_CODE = 100;
    private static final String DIALOG = "200";

    FirebaseFirestore db;
    Button btnSalvar;

    Button btnEventoSono;
    Button btnEventoExercicio;
    Button btnEventoRemedio;
    Button btnEventoBebida;

    CheckBox chkSonoDeitar;
    CheckBox chkSonoLevantar;
    CheckBox chkBebidaCafe;
    CheckBox chkBebidaCha;
    CheckBox chkBebidaRefrigerante;
    CheckBox chkBebidaAlcool;

    EditText edtObservacao;

    TimePicker spnMomentoHora;

    String tipoEvento, subEvento, situacao;

    int posEventoDeitarSelecionado = -1;
    List<Evento> listaEventoDormir;

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

    public DiarioFragment() { }

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

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        drawableEventoSonoHabilitado        = getResources().getDrawable(R.drawable.ic_sono_ativo);
        drawableEventoSonoDesabilitado      = getResources().getDrawable(R.drawable.ic_sono_inativo);
        drawableEventoExercicioHabilitado   = getResources().getDrawable(R.drawable.ic_exercicio_ativo);
        drawableEventoExercicioDesabilitado = getResources().getDrawable(R.drawable.ic_exercicio_inativo);
        drawableEventoRemedioHabilitado     = getResources().getDrawable(R.drawable.ic_remedio_ativo);
        drawableEventoRemedioDesabilitado   = getResources().getDrawable(R.drawable.ic_remedio_inativo);
        drawableEventoBebidaHabilitado      = getResources().getDrawable(R.drawable.ic_bebida_ativo);
        drawableEventoBebidaDesabilitado    = getResources().getDrawable(R.drawable.ic_bebida_inativo);

        btnEventoSono         = view.findViewById(R.id.btnEventoSono);
        btnEventoExercicio    = view.findViewById(R.id.btnEventoExercicio);
        btnEventoRemedio      = view.findViewById(R.id.btnEventoRemedio);
        btnEventoBebida       = view.findViewById(R.id.btnEventoBebida);

        chkSonoDeitar         = view.findViewById(R.id.chkSonoDeitar);
        chkSonoLevantar       = view.findViewById(R.id.chkSonoLevantar);
        chkBebidaCafe         = view.findViewById(R.id.chkBebidaCafe);
        chkBebidaCha          = view.findViewById(R.id.chkBebidaCha);
        chkBebidaRefrigerante = view.findViewById(R.id.chkBebidaRefrigerante);
        chkBebidaAlcool       = view.findViewById(R.id.chkBebidaAlcool);

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

        chkSonoDeitar         .setOnCheckedChangeListener(this);
        chkSonoLevantar       .setOnCheckedChangeListener(this);
        chkBebidaCafe         .setOnCheckedChangeListener(this);
        chkBebidaCha          .setOnCheckedChangeListener(this);
        chkBebidaRefrigerante .setOnCheckedChangeListener(this);
        chkBebidaAlcool       .setOnCheckedChangeListener(this);

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
                valoresEvento.put("situacao", situacao);

                if(tipoEvento.equals("SONO") && subEvento.equals("LEVANTAR")) {
                    if(posEventoDeitarSelecionado != -1) {

                        Evento eventoSelecionado  = listaEventoDormir.get(posEventoDeitarSelecionado);
                        Date d0 = eventoSelecionado.getMomento();

                        if(d0.before(data)) {
                            long diferenca = (data.getTime() - d0.getTime()) / 3600000;
                            valoresEvento.put("duracao", Long.toString(diferenca));

                            db.collection("usuarios")
                                    .document(usuario.getUsuario())
                                    .collection("eventos")
                                    .document(eventoSelecionado.getIdEvento())
                                    .update("situacao", "FECHADO");

                            db.collection("usuarios")
                                    .document(usuario.getUsuario())
                                    .collection("eventos")
                                    .add(valoresEvento)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), "Novo evento cadastrado...", Toast.LENGTH_LONG).show();
                                            edtObservacao.setText("");
                                            return;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Erro ao cadastrar evento de sono...", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    });
                            posEventoDeitarSelecionado = -1;
                            listaEventoDormir = null;
                        } else {
                            Toast.makeText(getActivity(), "O evento 'LEVANTAR' deve ser POSTERIOR ao evento 'DEITAR' selecionado...", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getActivity(), "Você precisa escolher um evento 'DEITAR', antes de cadastrar um evento 'LEVANTAR'", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                                    return;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("RESUMO", "Erro ao cadastrar evento de sono...", e);
                                    return;
                                }
                            });
                    posEventoDeitarSelecionado = -1;
                    listaEventoDormir = null;
                }
            }
        });
        return view;
    }

    // Método para exibir/ocultar botões baseado no tipo de evento + sub-evento - Início
    public void tratarTelaPorEvento(String tipoEvento, String subEvento) {

        this.tipoEvento = tipoEvento;
        this.subEvento  = subEvento;
        this.situacao   = "FECHADO";

        posEventoDeitarSelecionado = -1;
        listaEventoDormir = null;

        if(tipoEvento.equals("SONO")) {
            chkBebidaCafe.setChecked(false);
            chkBebidaCha.setChecked(false);
            chkBebidaRefrigerante.setChecked(false);
            chkBebidaAlcool.setChecked(false);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoHabilitado , null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado , null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado , null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado , null, null);

            if(subEvento.equals("DEITAR")) {
                chkSonoDeitar.setChecked(true);
                chkSonoLevantar.setChecked(false);
                this.situacao = "ABERTO";
            }

            if(subEvento.equals("LEVANTAR")) {
                chkSonoDeitar.setChecked(false);
                chkSonoLevantar.setChecked(true);
            }

            chkSonoDeitar.setVisibility(View.VISIBLE);
            chkSonoLevantar.setVisibility(View.VISIBLE);

            chkBebidaCafe.setVisibility(View.INVISIBLE);
            chkBebidaCha.setVisibility(View.INVISIBLE);
            chkBebidaRefrigerante.setVisibility(View.INVISIBLE);
            chkBebidaAlcool.setVisibility(View.INVISIBLE);
        }

        if(tipoEvento.equals("EXERCICIO")) {
            chkSonoDeitar.setChecked(false);
            chkSonoLevantar.setChecked(false);

            chkBebidaCafe.setChecked(false);
            chkBebidaCha.setChecked(false);
            chkBebidaRefrigerante.setChecked(false);
            chkBebidaAlcool.setChecked(false);

            chkSonoDeitar.setVisibility(View.INVISIBLE);
            chkSonoLevantar.setVisibility(View.INVISIBLE);

            chkBebidaCafe.setVisibility(View.INVISIBLE);
            chkBebidaCha.setVisibility(View.INVISIBLE);
            chkBebidaRefrigerante.setVisibility(View.INVISIBLE);
            chkBebidaAlcool.setVisibility(View.INVISIBLE);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado, null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioHabilitado, null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado, null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado, null, null);
        }

        if(tipoEvento.equals("REMEDIO")) {
            chkSonoDeitar.setChecked(false);
            chkSonoLevantar.setChecked(false);

            chkBebidaCafe.setChecked(false);
            chkBebidaCha.setChecked(false);
            chkBebidaRefrigerante.setChecked(false);
            chkBebidaAlcool.setChecked(false);

            chkSonoDeitar.setVisibility(View.INVISIBLE);
            chkSonoLevantar.setVisibility(View.INVISIBLE);

            chkBebidaCafe.setVisibility(View.INVISIBLE);
            chkBebidaCha.setVisibility(View.INVISIBLE);
            chkBebidaRefrigerante.setVisibility(View.INVISIBLE);
            chkBebidaAlcool.setVisibility(View.INVISIBLE);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado, null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado, null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioHabilitado, null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado, null, null);
        }

        if(tipoEvento.equals("BEBIDA")) {
            chkSonoDeitar.setChecked(false);
            chkSonoLevantar.setChecked(false);

            btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado, null, null);
            btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado, null, null);
            btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado, null, null);
            btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaHabilitado, null, null);

            if(subEvento.equals("CAFE")) {
                chkBebidaCafe.setChecked(true);
                chkBebidaCha.setChecked(false);
                chkBebidaRefrigerante.setChecked(false);
                chkBebidaAlcool.setChecked(false);
            }

            if(subEvento.equals("CHA")) {
                chkBebidaCafe.setChecked(false);
                chkBebidaCha.setChecked(true);
                chkBebidaRefrigerante.setChecked(false);
                chkBebidaAlcool.setChecked(false);
            }

            if(subEvento.equals("REFRIGERANTE")) {
                chkBebidaCafe.setChecked(false);
                chkBebidaCha.setChecked(false);
                chkBebidaRefrigerante.setChecked(true);
                chkBebidaAlcool.setChecked(false);
            }

            if(subEvento.equals("ALCOOL")) {
                chkBebidaCafe.setChecked(false);
                chkBebidaCha.setChecked(false);
                chkBebidaRefrigerante.setChecked(false);
                chkBebidaAlcool.setChecked(true);
            }

            chkSonoDeitar.setVisibility(View.INVISIBLE);
            chkSonoLevantar.setVisibility(View.INVISIBLE);

            chkBebidaCafe.setVisibility(View.VISIBLE);
            chkBebidaCha.setVisibility(View.VISIBLE);
            chkBebidaRefrigerante.setVisibility(View.VISIBLE);
            chkBebidaAlcool.setVisibility(View.VISIBLE);
        }
    }
    // Método para exibir/ocultar botões baseado no tipo de evento + sub-evento - Fim

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

        switch(compoundButton.getId()) {
            case R.id.chkSonoDeitar:
                if (checked)
                    tratarTelaPorEvento("SONO", "DEITAR");
                break;

            case R.id.chkSonoLevantar:
                if (checked) {
                    tratarTelaPorEvento("SONO", "LEVANTAR");

                    // Popup para eventos de deitar - Início
                    db.collection("usuarios")
                            .document(usuario.getUsuario())
                            .collection("eventos")
                            .whereEqualTo("tipoEvento", "SONO")
                            .whereEqualTo("subEvento", "DEITAR")
                            .whereEqualTo("situacao", "ABERTO")
                            .orderBy("momento", Query.Direction.DESCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        listaEventoDormir = new ArrayList<>();
                                        listaEventoDormir.clear();
                                        String[] arr;

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String idEvento = "", tipoEvento = "", subEvento = "", duracao = "", observacao = "", situacao = "";
                                            Date momento = null;

                                            if (document.getData().get("tipoEvento") != null) {
                                                tipoEvento = document.getData().get("tipoEvento").toString();
                                            }
                                            if (document.getData().get("subEvento") != null) {
                                                subEvento = document.getData().get("subEvento").toString();
                                            }
                                            if (document.getData().get("momento") != null) {
                                                momento = ((Timestamp) document.getData().get("momento")).toDate();
                                            }
                                            if (document.getData().get("duracao") != null) {
                                                duracao = document.getData().get("duracao").toString();
                                            }
                                            if (document.getData().get("observacao") != null) {
                                                observacao = document.getData().get("observacao").toString();
                                            }
                                            if (document.getData().get("situacao") != null) {
                                                situacao = document.getData().get("situacao").toString();
                                            }

                                            listaEventoDormir.add(new Evento(document.getId(), tipoEvento, subEvento, momento, duracao, observacao, situacao));
                                        }

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                        arr = new String[listaEventoDormir.size()];
                                        for (int i = 0; i < listaEventoDormir.size(); i++) {
                                            arr[i] = sdf.format(listaEventoDormir.get(i).getMomento());
                                        }

                                        ListaEventoDeitarFragment dialog = new ListaEventoDeitarFragment();

                                        Bundle bundle = new Bundle();
                                        bundle.putStringArray("eventosDeitar", arr);
                                        dialog.setArguments(bundle);

                                        dialog.setTargetFragment(DiarioFragment.this, DIALOG_REQUEST_CODE);
                                        dialog.show(getFragmentManager(), DIALOG);

                                    } else {
                                        Log.d("RESUMO", "Erro ao recuperar eventos do dia...", task.getException());
                                    }
                                }
                            });
                    } else {
                        tratarTelaPorEvento("SONO", "DEITAR");
                    }
                    // Popup para eventos de deitar - Fim
                    break;

            case R.id.chkBebidaCafe:
                if (checked)
                    tratarTelaPorEvento("BEBIDA", "CAFE");
                break;

            case R.id.chkBebidaCha:
                if (checked)
                    tratarTelaPorEvento("BEBIDA", "CHA");
                break;

            case R.id.chkBebidaRefrigerante:
                if (checked)
                    tratarTelaPorEvento("BEBIDA", "REFRIGERANTE");
                break;

            case R.id.chkBebidaAlcool:
                if (checked)
                    tratarTelaPorEvento("BEBIDA", "ALCOOL");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getExtras().containsKey("valorSelecionado")) {
                    posEventoDeitarSelecionado = data.getExtras().getInt("valorSelecionado");
                }
            }
        }
    }
}