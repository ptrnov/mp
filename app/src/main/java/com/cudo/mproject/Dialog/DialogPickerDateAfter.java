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

public class DialogPickerDateAfter extends Dialog {
    String TAG = getClass().getSimpleName();
    BaseActivity baseActivity;
    @BindView(R.id.cancell_after_date)
    View cancell;
    @BindView(R.id.save_after_date)
    View save;

    private Calendar calendar;
    private int year, month, dayOfMonth;
    private DatePicker date_after;
    static String isiPickerDateAfter = "";
    public DialogPickerDateAfter(@NonNull Context context) {
        super(context);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            /*getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);*/

            getWindow().setWindowAnimations(android.R.style.Theme_Holo_Light_Dialog);
            getWindow().setTitle("TANGGAL AKHIR");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_date_picker_after_dialog);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {
        date_after = (DatePicker) findViewById(R.id.date_after);
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
                    isiPickerDateAfter = date_after.getYear() + "-" + (date_after.getMonth() + 1)+ "-" + date_after.getDayOfMonth();
                    IMBActivity.setDateAfterspinnerDialog(isiPickerDateAfter);
                    Log.d(TAG, "onResponse" + isiPickerDateAfter);
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
//        date_after.init(
//                year,
//                month,
//                dayOfMonth,
//                new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(
//                            DatePicker view,
//                            int year,
//                            int monthOfYear,
//                            int dayOfMonth) {
//                        //Display the changed date to app interface
////                        isiPickerDateAfter = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
////                        Toast.makeText(baseActivity.getApplicationContext(), "isiPickerDateAfter" + isiPickerDateAfter, Toast.LENGTH_LONG).show();
//                    }
//                });

    }
}
