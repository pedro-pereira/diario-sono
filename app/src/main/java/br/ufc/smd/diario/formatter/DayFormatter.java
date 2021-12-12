package br.ufc.smd.diario.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class DayFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        switch ((int) value){
            case 1: {
                return "D";
            }
            case 3: {
                return "T";
            }
            case 4:
            case 5: {
                return "Q";
            }
            default: return "S";
        }
    }
}
