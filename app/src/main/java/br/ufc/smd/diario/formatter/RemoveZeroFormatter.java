package br.ufc.smd.diario.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class RemoveZeroFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        switch ((int) value) {
            case 0: {
                return "";
            }
            default:
                return String.valueOf((int)value);
        }
    }
}