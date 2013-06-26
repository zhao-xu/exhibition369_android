package com.threeH.MyExhibition.tools;

import android.content.Context;
import android.graphics.Typeface;

public class MSYH {
    static MSYH instance = null;
    Typeface normal;
    Typeface bold;

    public static MSYH getInstance(Context context) {
        if (instance == null) {
            synchronized (MSYH.class) {
                instance = new MSYH();
                instance.normal = Typeface.createFromAsset(context.getAssets(),"fonts/msyh.ttf");
                instance.bold = Typeface.createFromAsset(context.getAssets(),"fonts/msyhbd.ttf");
            }
        }
        return instance;
    }

    public Typeface getNormal() {
        return normal;
    }

    public Typeface getBold() {
        return bold;
    }
}
