package com.cudo.mproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.cudo.mproject.R;

/**
 * Created by adsxg on 12/11/2017.
 */

public class ProgressDialogGanz  extends Dialog {
    public ProgressDialogGanz(Context context) {
        super(context);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.dialog_progress);
        setCancelable(false);
    }
}
