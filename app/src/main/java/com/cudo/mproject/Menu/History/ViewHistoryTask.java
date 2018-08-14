package com.cudo.mproject.Menu.History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;

import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.Menu.TaskHistory.TaskHistoryActivity;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by adsxg on 12/14/2017.
 */

public class ViewHistoryTask extends RecyclerView.ViewHolder {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.activity_name_history)
    TextView activity_name_history;
    @BindView(R.id.wp_name_history)
    TextView wp_name_history;
    @BindView(R.id.project_id_history)
    TextView project_id_history;
    @BindView(R.id.sitename_history)
    TextView sitename_history;
    //  @BindView(R.id.sow)
//  TextView sow;
    @BindView(R.id.date_history)
    TextView date_history;
    @BindView(R.id.history_num)
    TextView tvNumber_history;
    @BindView(R.id.status_history)
    TextView tv_status_history;
    Realm realm;
    private final Context context;
    public GpsCache gpsCache;
    private BaseActivity baseActivity;
    LinearLayout mainLayout;
    //
//    OfflineDataTransaction dataLocals;
//    List<OfflineDataTransaction> list;
//    static int i = 0;
//    static int count = 0;
//    CharSequence charMenu[] = new CharSequence[]{"View Update Site Profile", "View Progres Project", "Delete Activity Progress"};
    CharSequence charMenu[] = new CharSequence[]{"View Progress Project", "Delete Activity Progress"};

    public ViewHistoryTask(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        mainLayout = (LinearLayout) itemView.findViewById(R.id.history_layout);
    }

    public void setItem(final OfflineDataTransaction dataLocal) {
//    public void setItem() {
//    checkUser();
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
//        OfflineDataTransaction dataLocalUser = realm.where(OfflineDataTransaction.class)
//                .equalTo("userName", user.getUsername())
//                .findFirst();
//        RealmResults<OfflineDataTransaction> dataLocalUser = realm.where(OfflineDataTransaction.class)
//                .equalTo("userName", user.getUsername())
//                .findAll().sort("id_offline_transaction", Sort.DESCENDING);
//        list = realm.copyFromRealm(dataLocalUser);
//        count = list.size();
//        if (count > 0) {
//            for (int k = 0; k < count; k++) {
//                dataLocals = realm.where(OfflineDataTransaction.class)
//                .equalTo("id_offline_transaction", dataLocalUser.get(k).getId_offline_transaction())
//                .findFirst();
//            }
//        }else{
//          dataLocals = realm.where(OfflineDataTransaction.class)
//                    .equalTo("userName", user.getUsername())
//                    .equalTo("id_offline_transaction", dataLocal.getId_offline_transaction())
//                    .findFirst();
//        }
//        OfflineDataTransaction dataLocals;
//        List<OfflineDataTransaction> list = realm.where(OfflineDataTransaction.class)
//                .equalTo("userName", user.getUsername())
//                .findAll();
//        int count = realm.copyFromRealm(realm.where(OfflineDataTransaction.class)
//                .equalTo("userName", user.getUsername())
//                .findAll().sort("id_offline_transaction", Sort.DESCENDING)).size();
//
//        if (count > 0) {
//            List<Integer> list1 = new ArrayList<>();
//            for (int k = 0; k < count; k++) {
//                list1.add(list.get(k).getId_offline_transaction());
//            }
//            for (int k = 0; k < count; k++) {
//
//                dataLocals = realm.where(OfflineDataTransaction.class)
//                        .equalTo("id_offline_transaction", list1.get(k))
//                        .findFirst();
//            }
////                  //.findAllSorted("id_offline_transaction", Sort.DESCENDING);
//        } else {
//            dataLocals = realm.where(OfflineDataTransaction.class)
//                    .equalTo("id_offline_transaction", datalocal.getId_offline_transaction())
//                    .findFirst();
////                  //.findAllSorted("id_offline_transaction", Sort.DESCENDING);
//
//        }
        /*source dataLocals 1303018*/
//        final    OfflineDataTransaction dataLocals = realm.where(OfflineDataTransaction.class)
//                .equalTo("id_offline_transaction", datalocal.getId_offline_transaction())
//                .findFirst();
        final OfflineDataTransaction dataLocals;
        OfflineDataTransaction dataUsers = realm.where(OfflineDataTransaction.class)
                .equalTo("userName", user.getUsername())//
                .or()
                .equalTo("userId", user.getUser_id()).findFirst();
//
        if (dataUsers != null) {

            dataLocals = realm.where(OfflineDataTransaction.class)
                    .equalTo("userName", user.getUsername())
                    .equalTo("id_offline_transaction", dataLocal.getId_offline_transaction())
                    .findFirst();//
        } else {

            dataLocals = realm.where(OfflineDataTransaction.class)
                    .equalTo("id_offline_transaction", dataLocal.getId_offline_transaction())
                    .findFirst();//
        }


        if (dataLocals != null) {

            MActivity mActivity = realm.where(MActivity.class).equalTo("activity_id", dataLocals.getActivity_id()).findFirst();
            WorkPkg workPkg = realm.where(WorkPkg.class).equalTo("wp_id", dataLocals.getWp_id()).findFirst();
            Project mproject = realm.where(Project.class).equalTo("project_id", dataLocals.getProject_id()).findFirst();

            if (mproject != null) {

                if (dataLocals.getProject_id() == null) {
                    project_id_history.setText(mproject.getProject_id());

                } else {
                    project_id_history.setText(dataLocals.getProject_id());

                }
                if (dataLocals.getActivity_name() == null) {
                    activity_name_history.setText("Activity: "+mActivity.getActivity_name());
                } else {
                    activity_name_history.setText("Activity: "+dataLocals.getActivity_name());
                }
                if (dataLocals.getWp_name() == null) {
                    wp_name_history.setText("WP: "+workPkg.getWp_name());
                } else {
                    wp_name_history.setText("WP: "+dataLocals.getWp_name());
                }
                if(dataLocals.getSite_name() == null){
                    sitename_history.setText(mproject.getSite_name());
                }else{
                    sitename_history.setText(dataLocals.getSite_name());
                }
                date_history.setText(dataLocals.getDate_offline() + " " + dataLocals.getTime_offline());

                String number_history = "00";
                String status_p = "";
//                if (count < 9) {
//                    number_history = "0" + (count + 1);
//                } else if ((count + 1) >= 10 && count < 19) {
//                    number_history = String.valueOf(count + 1);
//                } else if ((count + 1) >= 20 && count < 29) {
//                    number_history = String.valueOf(count+ 1);
//                }
                if (getAdapterPosition() < 9) {
                    number_history = "0" + (getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 10 && getAdapterPosition() < 19) {
                    number_history = String.valueOf(getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 20 && getAdapterPosition() < 29) {
                    number_history = String.valueOf(getAdapterPosition() + 1);
                }else if ((getAdapterPosition() + 1) >= 30 && getAdapterPosition() < 39) {
                    number_history = String.valueOf(getAdapterPosition() + 1);
                }else if ((getAdapterPosition() + 1) >= 40 && getAdapterPosition() < 49) {
                    number_history = String.valueOf(getAdapterPosition() + 1);
                }
                else if ((getAdapterPosition() + 1) >=50 && getAdapterPosition() <59) {
                    number_history = String.valueOf(getAdapterPosition() + 1);
                }

                if (dataLocals.getStatus_project_offline().contains("1")) {
                    status_p = "Submited";
                    tv_status_history.setBackgroundColor(Color.parseColor("#0492FF"));
                } else if (dataLocals.getStatus_project_offline().contains("2")) {
                    status_p = "Approval";
                    tv_status_history.setBackgroundColor(Color.parseColor("#00ff55"));
                } else if (dataLocals.getStatus_project_offline().contains("0")) {
                    status_p = "Pending";
                    tv_status_history.setBackgroundColor(Color.parseColor("#e6e600"));
                } else if (dataLocals.getStatus_project_offline().contains("3")) {
                    status_p = "Reject";
                    tv_status_history.setBackgroundColor(Color.parseColor("#cc0000"));
                }
//
                tvNumber_history.setText(number_history);
                tv_status_history.setText(status_p);

            } else {
//
                mainLayout.setVisibility(LinearLayout.GONE);
//
                project_id_history.setVisibility(View.INVISIBLE);
                activity_name_history.setVisibility(View.INVISIBLE);
                wp_name_history.setVisibility(View.INVISIBLE);
                sitename_history.setVisibility(View.INVISIBLE);
                date_history.setVisibility(View.INVISIBLE);
                tvNumber_history.setVisibility(View.INVISIBLE);
                tv_status_history.setVisibility(View.INVISIBLE);
            }
        } else {
//
            mainLayout.setVisibility(LinearLayout.GONE);
//
            project_id_history.setVisibility(View.INVISIBLE);
            activity_name_history.setVisibility(View.INVISIBLE);
            wp_name_history.setVisibility(View.INVISIBLE);
            sitename_history.setVisibility(View.INVISIBLE);
            date_history.setVisibility(View.INVISIBLE);
            tvNumber_history.setVisibility(View.INVISIBLE);
            tv_status_history.setVisibility(View.INVISIBLE);
        }

        /*
         * Create Popup menu WITH  AlertDialog.Builder builder
         * - Menu Update Site Profile
         * - Menu Progress Project
         * - Menu remove project
         * create date 0901018
         * create time 17.00
         * https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
         */
        itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Menu");
                builder.setItems(charMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
//                        if (which == 0) {
                        switch (which) {
//                            case 0:
////                                Log.d(TAG, "Merge data foto off,");
//                                onClickSiteProject(itemView);
//                                break;
                            case 0:
//                                Toast.makeText(context, "PROGRESS PROJECT", Toast.LENGTH_SHORT).show();

//                                ((HistoryActivity) itemView.getContext()).onClickItem(dataLocals);//dataLocalUser.get(count)
                                onClickItem(dataLocals);
                                break;
                            case 1:
//                                Toast.makeText(context, "Delete Activity Progress", Toast.LENGTH_SHORT).show();
//                                ((HistoryActivity) itemView.getContext()).onClickItem(datalocal);
                                onClickRemoveProgress(itemView);
                                break;
                        }
                    }
                });
                builder.show();
            }

            public void onClickItem(OfflineDataTransaction m) {//OfflineDataTransaction m
                Intent myIntent = new Intent(context, TaskHistoryActivity.class);
                myIntent.putExtra("id_offline", String.valueOf(m.getId_offline_transaction()));
                myIntent.putExtra("is_offline", "0");
                context.startActivity(myIntent);
            }
//            public void onClickSiteProject(View v) {
//
//                Intent myIntent = new Intent(context, AndroidTabLayoutActivity.class);
//                myIntent.putExtra("projectId", dataLocals.getProject_id());
//                myIntent.putExtra("is_offline", "1");
//                context.startActivity(myIntent);
//            }

            public void onClickRemoveProgress(View v) {
                if (dataLocals.getStatus_project_offline().contains("0") || dataLocals.getStatus_project_offline().contains("3")) {
//                  https://github.com/lararufflecoles/RecyclerView100/blob/master/app/src/main/java/es/rufflecol/lara/recyclerview100/MainActivity.java#L42
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Warning...");
                    alertDialogBuilder.setMessage("Anda Yakin Akan Menghapus Progress Activity ini?");
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                  Toast.makeText(context, "On Progress", Toast.LENGTH_SHORT).show();
                     deleteEvidence(dataLocals);
//                  OfflineDataTransaction.deleteOfflineData(realm, datalocal.getId_offline_transaction());
                        }
                    });
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {

                    String status = "";
                    if (dataLocals.getStatus_project_offline().contains("1")) {
                        status = "Submit to server";
                    }
                    if (dataLocals.getStatus_project_offline().contains("2")) {
                        status = "Project Approval";
                    }
                    Toast.makeText(context, "PID" + dataLocals.getProject_id() + " " + status, Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    void deleteEvidence(OfflineDataTransaction dataOffline) {
        realm = Realm.getDefaultInstance();
        List<Photo> list = realm.where(Photo.class).equalTo("activity_id", dataOffline.getActivity_id()).findAll();
        int count = realm.copyFromRealm(realm.where(Photo.class).equalTo("activity_id", dataOffline.getActivity_id())
                .equalTo("progress_photo", Integer.parseInt(dataOffline.getProgrees()))
                .findAll()).size();
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list1.add(list.get(i).getId());
        }
        for (int i = 0; i < count; i++) {
            Photo m = realm.where(Photo.class).equalTo("activity_id", dataOffline.getActivity_id())
                    .equalTo("progress_photo", Integer.parseInt(dataOffline.getProgrees()))
                    .equalTo(Photo.ID, list1.get(i)).findFirst();
            if (m != null) {
                try {
                    FileUtils.delete(baseActivity, m.getPath());
                    Photo.delete(realm, m);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        onSuccessSubmit(true, "Progress Activity Berhasil diHapus", dataOffline);
    }

    void onSuccessSubmit(boolean isSucces, String msg, OfflineDataTransaction datalocal) {
        if (isSucces == true) {
            Toast.makeText(context, msg + datalocal.getProject_id(), Toast.LENGTH_SHORT).show();
            OfflineDataTransaction.deleteOfflineData(realm, datalocal.getId_offline_transaction());
        }

    }
}


