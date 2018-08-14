package com.cudo.mproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Site.IMB.IMBActivity;
import com.cudo.mproject.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogPickerDateBefore extends Dialog {
    String TAG = getClass().getSimpleName();
    BaseActivity baseActivity;
    @BindView(R.id.cancell_before_date)
    View cancell;
    @BindView(R.id.save_before_date)
    View save;

    private Calendar calendar;
    private int year, month, dayOfMonth;
    private DatePicker date_before;
    static String isiPickerDateBefore = "";
    public DialogPickerDateBefore(@NonNull Context context) {
        super(context);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            /*getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);*/

            getWindow().setWindowAnimations(android.R.style.Theme_Holo_Light_Dialog);
            getWindow().setTitle("TANGGAL AWAL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_date_picker_before_dialog);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {
        date_before = (DatePicker) findViewById(R.id.date_before);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String text = input.getText().toString();
//                Toast.makeText(getApplicationContext(), "startTimeET"+hour + ":" + minute + ":" + second, Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP||Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isiPickerDateBefore = date_before.getYear() + "-" + (date_before.getMonth()+1 )+ "-" + date_before.getDayOfMonth();
                    IMBActivity.setDateBeforespinnerDialog(isiPickerDateBefore);
                    Log.d(TAG, "onResponse" + isiPickerDateBefore);

                }
                dismiss();
            }
        });

        cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//                new DatePicker.OnDateChangedListener() {
//
//                    @Override
//                    public void onDateChanged(
//                            DatePicker view,
//                            int year,
//                            int monthOfYear,
//                            int dayOfMonth) {
//                        //Display the changed date to app interface
//                        isiPickerDateBefore = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
////                        Toast.makeText(getContext(), "isiPickerDateBefore" + isiPickerDateBefore, Toast.LENGTH_LONG).show();
////                        IMBActivity
//                    }
//                });

    }
}
