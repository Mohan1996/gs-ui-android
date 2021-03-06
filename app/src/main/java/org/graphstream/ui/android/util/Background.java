package org.graphstream.ui.android.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;

public class Background {
    private int color = -1;
    private int oldColor = -1 ;

    private Shader gradient = null;

    private Bitmap img = null;
    private float x = 0;
    private float y = 0;

    public Background(int color) {
        this.color = color ;
    }

    public Background(Shader gradient) {
        this.gradient = gradient ;
    }

    public Background(Bitmap img, double xFrom, double yFrom, int width, int height) {
        this.x = (float)xFrom ;
        this.y = (float)yFrom ;
        this.img = getResizedBitmap(img, width, height) ;
    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        // Resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void applyPaint(Canvas c, Paint p) {
        if( color != -1 ) {
            oldColor = p.getColor();
            p.setColor(color);
        }
        else if (gradient != null) {
            p.setShader(gradient);
        }
        else if ( img != null ) {
            c.drawBitmap(img, x, y, p);
        }
    }

    public void removePaint(Paint p) {
        if( color != -1 ) {
            p.setColor(oldColor);
        }
        else if (gradient != null) {
            p.setShader(null);
        }
    }

    @Override
    public String toString() {
        return "color = "+color+" shader = "+gradient+" img = "+img+" x = "+x+" y = "+y;
    }

    public boolean isImage() {
        return (img!=null);
    }
}
