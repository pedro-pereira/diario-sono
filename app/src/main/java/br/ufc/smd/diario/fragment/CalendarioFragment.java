package br.ufc.smd.diario.fragment;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.PrincipalActivity;
import br.ufc.smd.diario.decorator.EventDecorator;

public class CalendarioFragment extends Fragment {
    public MaterialCalendarView calendario;
    FirebaseFirestore db;
    public CalendarioFragment() {
        super(R.layout.fragment_calendario);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getActivity());

        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendario = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendario.setTopbarVisible(false);
        calendario.setClickable(false);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Fortaleza/Brasil"));
        cal.setTime(new Date());

        Toolbar myToolbarPrincipal = (Toolbar) view.findViewById(R.id.toolBar);
        myToolbarPrincipal.setBackgroundColor(getResources().getColor(R.color.barraSuperiorCadastro));
        myToolbarPrincipal.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbarPrincipal.setTitle(new SimpleDateFormat("MMMM").format(cal.getTime()).substring(0,1).toUpperCase().concat(new SimpleDateFormat("MMMM").format(cal.getTime()).substring(1)));

        db = FirebaseFirestore.getInstance();
        setData();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData() {
        db.collection("usuarios")
                .document("ana")
                .collection("eventos")
                .orderBy("momento")
                .get()
                .addOnCompleteListener(task -> onComplete(task));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onComplete(@NonNull Task<QuerySnapshot> task) {
        Log.i("TAG", "2: ");
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
            calendario.addDecorator(new EventDecorator(getResources().getColor(R.color.dotLaranja), datasExercicio));
            calendario.addDecorator(new EventDecorator(getResources().getColor(R.color.dotVerde), datasRemedio));
            calendario.addDecorator(new EventDecorator(getResources().getColor(R.color.dotVermelho), datasBebida));
            calendario.addDecorator(new EventDecorator(getResources().getColor(R.color.dotAzul), datasSono));
        } else {
            Log.d("TAG", "Error getting documents: ", task.getException());
        }
    }
}
