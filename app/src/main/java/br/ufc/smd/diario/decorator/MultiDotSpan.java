package br.ufc.smd.diario.decorator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufc.smd.diario.R;

public class MultiDotSpan implements LineBackgroundSpan {

    private final float radius;

    private final int color;

    public MultiDotSpan(float  radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();
        int leftMost = 0;
        switch (color) {
            case -10631681: {
                leftMost = 13;
                break;
            }
            case -65493: {
                leftMost = -13;
                break;
            }
            case -11085676: {
                leftMost= -13;
                bottom += 25;
                break;
            }
            case -561905: {
                leftMost= 13;
                bottom += 25;
                break;
            }
        }

        if (color != 0) {
            paint.setColor(color);
        }
        canvas.drawCircle((left + right) / 2 - leftMost, bottom + radius - 5, radius, paint);
        paint.setColor(oldColor);

    }

}
