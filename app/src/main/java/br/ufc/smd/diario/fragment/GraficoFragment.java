package br.ufc.smd.diario.fragment;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.formatter.DayFormatter;
import br.ufc.smd.diario.formatter.RemoveZeroFormatter;

public class GraficoFragment extends Fragment implements OnCompleteListener<QuerySnapshot> {

    ImageView imgAndroid;

    private LineChart chart;

    FirebaseFirestore db;

    ArrayList<Entry> values;

    AnimationDrawable mAnimation;

    View view;

    public GraficoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        values = new ArrayList<>();

        // Inflate the layout for this fragment
        // View view = inflater.inflate(R.layout.fragment_diario, container, false);
        view = inflater.inflate(R.layout.fragment_grafico, container, false);

        // get fragment manager so we can launch from fragment
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        chart = view.findViewById(R.id.chart1);
        chart.setVisibility(View.GONE);
        getActivity().setTitle("Gráficos");

        setPropertiesChart();

        imgAndroid = (ImageView) view.findViewById(R.id.imgAndroid);
        imgAndroid.setBackgroundResource(R.drawable.loading);

        mAnimation = (AnimationDrawable) imgAndroid.getBackground();
        mAnimation.start();
        setData();

        return view;
    }

    public void setPropertiesChart() {
        chart.getDescription().setEnabled(false);

        final Typeface tipografia = ResourcesCompat.getFont(getActivity().getBaseContext(), R.font.roboto);
        // scaling can now only be done on x- and y-axis separately
        for (int i = 3; i < 12; i += 3) {
            LimitLine limitLine = new LimitLine(i);
            limitLine.setLineColor(getResources().getColor(R.color.primary));
            chart.getAxisLeft().addLimitLine(limitLine);
        }
        for (int i = 0; i < 7; i++) {
            LimitLine limitLine = new LimitLine(i);
            limitLine.setLineColor(getResources().getColor(R.color.primary));
            chart.getXAxis().addLimitLine(limitLine);
        }
        chart.setPinchZoom(false);
        chart.getXAxis().setLabelCount(7, true);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setTextColor(getResources().getColor(R.color.white));
        chart.getXAxis().setGridColor(getResources().getColor(R.color.primary));
        chart.getXAxis().setAxisMaximum(7);
        chart.getXAxis().setAxisMinimum(1);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTypeface(tipografia);
        chart.getXAxis().setTextSize(12);
        chart.getXAxis().setValueFormatter(new DayFormatter());
        chart.getAxisRight().setAxisMinimum(0);
        chart.getAxisRight().setAxisMaximum(12);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(12);
        chart.getAxisLeft().setLabelCount(5, true);
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
        chart.getAxisLeft().setTypeface(tipografia);
        chart.getAxisLeft().setTextSize(12);
        chart.getAxisLeft().setDrawZeroLine(false);
        chart.getAxisLeft().setValueFormatter(new RemoveZeroFormatter());
        chart.setDrawGridBackground(true);
        chart.setGridBackgroundColor(getResources().getColor(R.color.darkSecondary));
        chart.getAxisLeft().setDrawLimitLinesBehindData(true);
        chart.getXAxis().setDrawLimitLinesBehindData(true);
        chart.setDrawBorders(true);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.animateX(500);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData() {
        db.collection("usuarios")
                .document("ana")
                .collection("eventos")
                .orderBy("momento")
                .startAfter(getDateForFirstDayOfTheWeek().getTime())
                .get()
                .addOnCompleteListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Calendar getDateForFirstDayOfTheWeek(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return cal;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        imgAndroid.setVisibility(View.GONE);
        mAnimation.stop();
        chart.setVisibility(View.VISIBLE);
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.exists()) {
                    // Lógica para montar gréfico de linhas sobre o tipoEvento SONO - Início
                    // Date dataMomentoFinal = document.getDate("momentoFinal");
                    Date dataMomentoFinal = document.getDate("momento");
                    SimpleDateFormat sdf = new SimpleDateFormat("u");
                    String dayOfTheWeek = sdf.format(dataMomentoFinal).equals("7") ? "1" : String.valueOf(Integer.parseInt(sdf.format(dataMomentoFinal)) + 1);

                    if(document.get("tipoEvento").toString().equals("SONO") && document.get("subEvento").toString().equals("LEVANTAR")) {
                        Integer duracao = Integer.parseInt(document.get("duracao").toString());
                        values.add(new Entry(Float.parseFloat(dayOfTheWeek), duracao));
                    }
                    // Lógica para montar gréfico de linhas sobre o tipoEvento SONO - Fim

                    // Lógica para montar tabela de eventos: EXERCICIO, REMEDIO, BEBIDA - Início
                    String tipoEvento = document.getString("tipoEvento");
                    if(tipoEvento != null) {
                        switch (tipoEvento) {
                            case "EXERCICIO": {
                                View view = this.view.findViewById(R.id.halter);
                                String check = "checkBox"+dayOfTheWeek;
                                CheckBox checkBox = view.findViewById(getResources().getIdentifier(check, "id", getActivity().getPackageName()));
                                checkBox.setChecked(true);
                                break;
                            }
                            case "REMEDIO": {
                                View view = this.view.findViewById(R.id.pilula);

                                String check = "checkBox"+dayOfTheWeek;
                                CheckBox checkBox = view.findViewById(getResources().getIdentifier(check, "id", getActivity().getPackageName()));
                                checkBox.setChecked(true);
                                break;
                            }
                            case "BEBIDA": {
                                View view = this.view.findViewById(R.id.bebida);
                                String check = "checkBox"+dayOfTheWeek;

                                CheckBox checkBox = view.findViewById(getResources().getIdentifier(check, "id", getActivity().getPackageName()));
                                checkBox.setChecked(true);
                                break;
                            }
                        }
                    }
                    // Lógica para montar tabela de eventos: EXERCICIO, REMEDIO, BEBIDA - Fim
                }
            }

            LineDataSet set1;
            set1 = new LineDataSet(values, "");
            set1.setCircleColor(getResources().getColor(R.color.secondary));
            set1.setCircleHoleColor(getResources().getColor(R.color.secondary));
            set1.setCircleRadius(8);
            set1.setColor(getResources().getColor(R.color.secondary));
            set1.setLineWidth(3);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            chart.setData(data);
        } else {
            Log.d("TAG", "Error getting documents: ", task.getException());
        }

        Log.i("Teste values interno: ", String.valueOf(values.size()));
    }
}