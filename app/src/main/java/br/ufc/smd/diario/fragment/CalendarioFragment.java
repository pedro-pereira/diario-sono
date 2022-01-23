package br.ufc.smd.diario.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.ListaEventosActivity;
import br.ufc.smd.diario.activity.PrincipalActivity;
import br.ufc.smd.diario.decorator.EventDecorator;
import br.ufc.smd.diario.model.Usuario;

public class CalendarioFragment extends Fragment {

    public MaterialCalendarView calendario;
    FirebaseFirestore db;
    Usuario usuario;

    View view;

    public CalendarioFragment() {
        super(R.layout.fragment_calendario);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // FirebaseApp.initializeApp(getActivity());

        view = inflater.inflate(R.layout.fragment_calendario, container, false);

        usuario = ((PrincipalActivity) getActivity()).usuario;

        calendario = view.findViewById(R.id.calendarView);
        calendario.setTopbarVisible(false);
        calendario.setClickable(true);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Fortaleza/Brasil"));
        cal.setTime(new Date());

        Toolbar myToolbarPrincipal = view.findViewById(R.id.toolBar);
        myToolbarPrincipal.setBackgroundColor(getResources().getColor(R.color.barraSuperiorCadastro));
        myToolbarPrincipal.setTitleTextColor(getResources().getColor(R.color.white));

        String[] months = new DateFormatSymbols().getMonths();
        String titulo = months[cal.get(Calendar.MONTH)] + " / " + cal.get(Calendar.YEAR);
        myToolbarPrincipal.setTitle(titulo);

        // Implementação de click na data escolhida - Início
        calendario.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                Calendar c = Calendar.getInstance();
                c.set(date.getYear(), date.getMonth() - 1, date.getDay());

                Intent intentListaEventos = new Intent(getActivity(), ListaEventosActivity.class);
                intentListaEventos.putExtra("usuario", usuario);
                intentListaEventos.putExtra("dataSelecionada", c);
                startActivity(intentListaEventos);
            }
        });
        // Implementação de click na data escolhida - fim

        calendario.setOnMonthChangedListener((widget, date) -> {
            try {
                Field currentMonthField = MaterialCalendarView.class.getDeclaredField("currentMonth");
                currentMonthField.setAccessible(true);
                int currentMonth = ((CalendarDay) currentMonthField.get(widget)).getMonth();

                // Do something, currentMonth is between 1 and 12.
                String titulo2 = months[currentMonth - 1] + " / " + String.valueOf(((CalendarDay) currentMonthField.get(widget)).getYear());
                myToolbarPrincipal.setTitle(titulo2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Failed to get field value, maybe library was changed.
            }
        });

        db = FirebaseFirestore.getInstance();
        setData();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData() {
        db.collection("usuarios")
                .document(usuario.getUsuario())
                .collection("eventos")
                .orderBy("momento")
                .get()
                .addOnCompleteListener(task -> onComplete(task));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {

            Collection<CalendarDay> datasExercicio = new ArrayList<>();
            Collection<CalendarDay> datasRemedio = new ArrayList<>();
            Collection<CalendarDay> datasBebida = new ArrayList<>();
            Collection<CalendarDay> datasSono = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.exists()) {

                    Timestamp momento = document.getTimestamp("momento");
                    String tipoEvento = document.getString("tipoEvento");
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Fortaleza/Brasil"));
                    cal.setTime(momento.toDate());

                    assert momento != null;
                    if(momento != null) {
                        switch (tipoEvento) {
                            case "EXERCICIO": {
                                datasExercicio.add(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
                                break;
                            }
                            case "REMEDIO": {
                                datasRemedio.add(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
                                break;
                            }
                            case "BEBIDA": {
                                datasBebida.add(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
                                break;
                            }
                            case "SONO": {
                                datasSono.add(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
                                break;
                            }
                        }
                    }
                }
            }

            calendario.addDecorator(new EventDecorator(view.getResources().getColor(R.color.dotLaranja) , datasExercicio));
            calendario.addDecorator(new EventDecorator(view.getResources().getColor(R.color.dotVerde)   , datasRemedio));
            calendario.addDecorator(new EventDecorator(view.getResources().getColor(R.color.dotVermelho), datasBebida));
            calendario.addDecorator(new EventDecorator(view.getResources().getColor(R.color.dotAzul)    , datasSono));
        } else {
            Log.d("TAG", "Error getting documents: ", task.getException());
        }
    }
}
