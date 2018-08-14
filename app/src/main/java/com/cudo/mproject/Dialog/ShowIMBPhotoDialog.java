package com.cudo.mproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.cudo.mproject.Menu.Site.IMB.IMBPhotoActivity;
import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ShowIMBPhotoDialog extends Dialog {

    @BindView(R.id.dummy)
    View dummy;
    IMBPhotoActivity imbPhotoActivity;
    public ShowIMBPhotoDialog(@NonNull Context context, IMBPhotoActivity xxx, PhotoIMB photoIMB, Realm realm) {
        super(context, R.style.AppTheme);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            /*getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);*/
            getWindow().setWindowAnimations(R.style.DialogTheme);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.imbPhotoActivity = xxx;
        setContentView(R.layout.dialog_photo);
        ButterKnife.bind(this);
        dummy.requestFocus();
        initView(realm, photoIMB);
    }
    void initView(final Realm realm, final PhotoIMB dataPhotoIMB) {

    }
}
