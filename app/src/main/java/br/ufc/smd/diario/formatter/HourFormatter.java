package br.ufc.smd.diario.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class HourFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return String.valueOf(Math.round(value));
    }
}
