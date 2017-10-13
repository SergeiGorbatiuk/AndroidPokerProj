package com.example.user.pokerheh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.TextView;

public class getBorder extends TextView {
    public getBorder(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        paint.setColor(android.graphics.Color.RED);

        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                this.getHeight() - 1, paint);
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                this.getHeight() - 1, paint);
    }
}
