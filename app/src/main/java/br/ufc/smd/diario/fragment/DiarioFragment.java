package br.ufc.smd.diario.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.FirebaseFirestore;

import br.ufc.smd.diario.R;

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

    String dataDeitarSelecionada, dataLevantarSelecionada;
    String horaDeitarSelecionada, horaLevantarSelecionada;

    public DiarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        // View view = inflater.inflate(R.layout.fragment_diario, container, false);
        View view = inflater.inflate(R.layout.activity_registro_evento, container, false);

        // get fragment manager so we can launch from fragment
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        final Drawable drawableEventoSonoHabilitado        = getResources().getDrawable(R.drawable.ic_dormir_habilitado);
        final Drawable drawableEventoSonoDesabilitado      = getResources().getDrawable(R.drawable.ic_dormir_desabilitado);
        final Drawable drawableEventoExercicioHabilitado   = getResources().getDrawable(R.drawable.ic_exercicio_habilitado);
        final Drawable drawableEventoExercicioDesabilitado = getResources().getDrawable(R.drawable.ic_exercicio_desabilitado);
        final Drawable drawableEventoRemedioHabilitado     = getResources().getDrawable(R.drawable.ic_remedio_habilitado);
        final Drawable drawableEventoRemedioDesabilitado   = getResources().getDrawable(R.drawable.ic_remedio_desabilitado);
        final Drawable drawableEventoBebidaHabilitado      = getResources().getDrawable(R.drawable.ic_bebida_habilitado);
        final Drawable drawableEventoBebidaDesabilitado    = getResources().getDrawable(R.drawable.ic_bebida_desabilitado);

        /*
        edtDataDeitar   = view.findViewById(R.id.edtDataDeitar);
        edtHoraDeitar   = view.findViewById(R.id.edtHoraDeitar);
        edtDataLevantar = view.findViewById(R.id.edtDataLevantar);
        edtHoraLevantar = view.findViewById(R.id.edtHoraLevantar);
        edtObservacao   = view.findViewById(R.id.edtObservacao);
         */

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

        btnBebidaCafe.setVisibility(View.INVISIBLE);
        btnBebidaCha.setVisibility(View.INVISIBLE);
        btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
        btnBebidaAlcool.setVisibility(View.INVISIBLE);

        btnSonoDeitar.setBackgroundColor(getResources().getColor(R.color.purple_500));
        btnSonoLevantar.setBackgroundColor(getResources().getColor(R.color.secondary));
        btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.purple_500));
        btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.secondary));
        btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.secondary));
        btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.secondary));

        btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoHabilitado , null, null);
        btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado , null, null);
        btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado , null, null);
        btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado , null, null);

        btnSalvar       = view.findViewById(R.id.btnSalvar);

        btnEventoSono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        btnEventoExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.INVISIBLE);
                btnSonoLevantar.setVisibility(View.INVISIBLE);

                btnBebidaCafe.setVisibility(View.INVISIBLE);
                btnBebidaCha.setVisibility(View.INVISIBLE);
                btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
                btnBebidaAlcool.setVisibility(View.INVISIBLE);

                btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado , null, null);
                btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioHabilitado , null, null);
                btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado , null, null);
                btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado , null, null);
            }
        });

        btnEventoRemedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.INVISIBLE);
                btnSonoLevantar.setVisibility(View.INVISIBLE);

                btnBebidaCafe.setVisibility(View.INVISIBLE);
                btnBebidaCha.setVisibility(View.INVISIBLE);
                btnBebidaRefrigerante.setVisibility(View.INVISIBLE);
                btnBebidaAlcool.setVisibility(View.INVISIBLE);

                btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado , null, null);
                btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado , null, null);
                btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioHabilitado , null, null);
                btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaDesabilitado , null, null);
            }
        });

        btnEventoBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setVisibility(View.INVISIBLE);
                btnSonoLevantar.setVisibility(View.INVISIBLE);

                btnBebidaCafe.setVisibility(View.VISIBLE);
                btnBebidaCha.setVisibility(View.VISIBLE);
                btnBebidaRefrigerante.setVisibility(View.VISIBLE);
                btnBebidaAlcool.setVisibility(View.VISIBLE);

                btnEventoSono.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoSonoDesabilitado , null, null);
                btnEventoExercicio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoExercicioDesabilitado , null, null);
                btnEventoRemedio.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoRemedioDesabilitado , null, null);
                btnEventoBebida.setCompoundDrawablesWithIntrinsicBounds(null, drawableEventoBebidaHabilitado , null, null);
            }
        });

        // "Aba" de registro de sono - Início
        btnSonoDeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setBackgroundColor(getResources().getColor(R.color.purple_500));
                btnSonoLevantar.setBackgroundColor(getResources().getColor(R.color.secondary));
            }
        });

        btnSonoLevantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSonoDeitar.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnSonoLevantar.setBackgroundColor(getResources().getColor(R.color.purple_500));
            }
        });
        // "Aba" de registro de sono - Fim

        // "Aba" de registro de bebida - Início
        btnBebidaCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.purple_500));
                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.secondary));
            }
        });

        btnBebidaCha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.purple_500));
                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.secondary));
            }
        });

        btnBebidaRefrigerante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.purple_500));
                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.secondary));
            }
        });

        btnBebidaAlcool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBebidaCafe.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaCha.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaRefrigerante.setBackgroundColor(getResources().getColor(R.color.secondary));
                btnBebidaAlcool.setBackgroundColor(getResources().getColor(R.color.purple_500));
            }
        });
        // "Aba" de registro de bebida - Início

        /*
        edtDataDeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(DiarioFragment.this, REQUEST_CODE_DATA_DEITAR);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        edtDataLevantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(DiarioFragment.this, REQUEST_CODE_DATA_LEVANTAR);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        edtHoraDeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new TimePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(DiarioFragment.this, REQUEST_CODE_HORA_DEITAR);
                // show the datePicker
                newFragment.show(fm, "timePicker");
            }
        });

        edtHoraLevantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new TimePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(DiarioFragment.this, REQUEST_CODE_HORA_LEVANTAR);
                // show the datePicker
                newFragment.show(fm, "timePicker");
            }
        });
         */

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Map<String, Object> valoresEvento = new HashMap<>();
                valoresEvento.put("detalhe", edtObservacao.getText().toString());
                valoresEvento.put("tipoEvento", "sono");

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                    String momentoDeitar = edtDataDeitar.getText().toString() + " " + edtHoraDeitar.getText().toString();
                    Date dataDeitar = formatter.parse(momentoDeitar);
                    valoresEvento.put("momentoInicial", dataDeitar);

                    String momentoLevantar = edtDataLevantar.getText().toString() + " " + edtHoraLevantar.getText().toString();
                    Date dataLevantar = formatter.parse(momentoLevantar);
                    valoresEvento.put("momentoFinal", dataLevantar);

                    long diferenca = (dataLevantar.getTime() - dataDeitar.getTime()) / 3600000;
                    valoresEvento.put("duracao", Long.toString(diferenca));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                db.collection("usuarios")
                        .document("ana")
                        .collection("eventos")
                        .add(valoresEvento)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Novo evento com ID: ..." + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Novo evento de sono cadastrado com ID: " + documentReference.getId());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Erro ao cadastrar evento de sono...", e);
                            }
                        });
                 */
            }

        });

        return view;
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE_DATA_DEITAR && resultCode == Activity.RESULT_OK) {
            // get date from string
            dataDeitarSelecionada = data.getStringExtra("selectedDate");
            // set the value of the editText
            edtDataDeitar.setText(dataDeitarSelecionada);
        }

        if (requestCode == REQUEST_CODE_DATA_LEVANTAR && resultCode == Activity.RESULT_OK) {
            // get date from string
            dataLevantarSelecionada = data.getStringExtra("selectedDate");
            // set the value of the editText
            edtDataLevantar.setText(dataLevantarSelecionada);
        }

        if (requestCode == REQUEST_CODE_HORA_DEITAR && resultCode == Activity.RESULT_OK) {
            // get date from string
            horaDeitarSelecionada = data.getStringExtra("selectedTime");
            // set the value of the editText
            edtHoraDeitar.setText(horaDeitarSelecionada);
        }

        if (requestCode == REQUEST_CODE_HORA_LEVANTAR && resultCode == Activity.RESULT_OK) {
            // get date from string
            horaLevantarSelecionada = data.getStringExtra("selectedTime");
            // set the value of the editText
            edtHoraLevantar.setText(horaLevantarSelecionada);
        }
    }
     */

}
