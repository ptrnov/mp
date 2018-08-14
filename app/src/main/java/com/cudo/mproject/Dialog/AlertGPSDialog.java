package com.cudo.mproject.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.cudo.mproject.Menu.History.HistoryActivity;
import com.cudo.mproject.Menu.Project.ProjectListActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.R;
import com.cudo.mproject.Service.ConnectivityReceiverService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.cudo.mproject.BaseApp.getContext;

public class AlertGPSDialog extends Activity {
//    @BindView(R.id.btn_yes)
//    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

                        if(!provider.contains("gps")){ //if gps is disabled
                            final Intent poke = new Intent();
                            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                            poke.setData(Uri.parse("3"));
                            sendBroadcast(poke);
                        }
//                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        finish();
//                        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//                        intent.putExtra("enabled", true);
//                        sendBroadcast(intent);
//                        Intent intent = new Intent(AlertGPSDialog.this, ProjectListActivity.class);
//                        startActivity(intent);

                    }
                })
                .setNegativeButton("CANCELL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
//        alert.setTitle("Location Services Not Active");
        alert.setIcon(R.drawable.ic_warning_black_24dp);
        alert.show();
        ButterKnife.bind(this);
//        setContentView(R.layout.dialog_gps);
//        initView();
    }


//    void initView() {
//        btnOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AlertGPSDialog.this, ProjectListActivity.class);
//                startActivity(intent);
//            }
//
//        });
//    }
}

//    public class AlertGPSDialog extends Dialog {
//    String TAG = getClass().getSimpleName();
//
//    @BindView(R.id.btn_yes)
//    View btnOK;
//    @BindView(R.id.btn_no)
//    View btnCancell;
//    @BindView(R.id.diag)
//    View dalogGps;
//
//    public AlertGPSDialog(@NonNull Context context) {
//        super(context);
//        try {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            /*getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.MATCH_PARENT);*/
//            getWindow().setWindowAnimations(R.style.DialogTheme);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        setContentView(R.layout.dialog_gps);
//        ButterKnife.bind(this);
//        dalogGps.requestFocus();
//        initView();
//    }
//
//    void initView() {
//
//        btnOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                        startActivity(intent);
//            }
//
//        });
//
//        btnCancell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//
//        });
//
//    }
////    public void showAlertDialogButtonClicked() {
////
////        // setup the alert builder
////        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
////        // set title
////        alertDialogBuilder.setTitle("Location Services Not Active");
////        // set dialog message
////        alertDialogBuilder
////                .setMessage("Please enable Location Services and GPS")
////                .setCancelable(false)
////                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog,int id) {
////                        // if this button is clicked, close
////                        // current activity
////                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                        startActivity(intent);
////                    }
////                })
////                .setNegativeButton("No",new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog,int id) {
////                        // if this button is clicked, just close
////                        // the dialog box and do nothing
////                        dialog.cancel();
////                    }
////                });
////        // create alert dialog
////        AlertDialog alertDialog = alertDialogBuilder.create();
////        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
////        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////        // show it
////        alertDialog.show();
////    }
//
//
//}
