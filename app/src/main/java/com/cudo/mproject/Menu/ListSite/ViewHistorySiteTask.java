package com.cudo.mproject.Menu.ListSite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.History.HistoryActivity;
import com.cudo.mproject.Menu.Site.SiteProfileActivity;
import com.cudo.mproject.Menu.Tab.AndroidTabHistoryActivity;
import com.cudo.mproject.Menu.Tab.AndroidTabLayoutActivity;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ViewHistorySiteTask extends RecyclerView.ViewHolder {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.project_id_history_site)
    TextView project_id_history_site;
    @BindView(R.id.site_history)
    TextView site_history;
    //    @BindView(R.id.wp_name_history_site)
//    TextView wp_name_history_site;
//    @BindView(R.id.activity_name_history_site)
//    TextView activity_name_history_site;
    @BindView(R.id.tvNumber_history_site)
    TextView tvNumber_history_site;
    @BindView(R.id.sitename_history_site)
    TextView sitename_history_site;

    private Context context;
    public BaseActivity baseActivity;
    public Realm realm;
    ;
    LinearLayout mainLayout;

    CharSequence charMenu[] = new CharSequence[]{"View Update Site Profile", "Delete Site Profile"};

    public ViewHistorySiteTask(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        realm = Realm.getDefaultInstance();
        mainLayout = (LinearLayout) itemView.findViewById(R.id.history_site_layout);
    }

    //    public void setItem(final Project dataProject) {
//        realm = Realm.getDefaultInstance();
//        User user = realm.where(User.class).findFirst();
//    }
    public void setItem(final DataAlamatActualOffline dataProject) {

        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();

        final DataAlamatActualOffline dataProjects;
        DataAlamatActualOffline dataUsers = realm.where(DataAlamatActualOffline.class)
                .equalTo("userName", user.getUsername())//
                .or()
                .equalTo("userId", user.getUser_id()).findFirst();

        if (dataUsers != null) {

            dataProjects = realm.where(DataAlamatActualOffline.class)
                    .equalTo("userName", user.getUsername())
                    .equalTo("idAlamatActualOffline", dataProject.getIdAlamatActualOffline())
                    .findFirst();//
        } else {

            dataProjects = realm.where(DataAlamatActualOffline.class)
                    .equalTo("idAlamatActualOffline", dataProject.getIdAlamatActualOffline())
                    .findFirst();//
        }
        if (dataProjects != null) {
            Project mproject = realm.where(Project.class).equalTo("project_id", dataProject.getProject_id()).findFirst();
            if (mproject != null) {
                if (dataProjects.getProject_id() != null) {
                    site_history.setText("PROJECT ID");
                    project_id_history_site.setText(dataProjects.getProject_id());
                } else {
                    site_history.setText("PROJECT ID");
                    project_id_history_site.setText(mproject.getProject_id());
                }
                if(dataProject.getSiteName() == null){
                    sitename_history_site.setText("Site Name : "+dataProjects.getSiteName());
                }else{
                    sitename_history_site.setText("Site Name : "+ mproject.getSite_name());
                }
                String number_history_site = "00";
                String status_p = "";
                if (getAdapterPosition() < 9) {
                    number_history_site = "0" + (getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 10 && getAdapterPosition() < 19) {
                    number_history_site = String.valueOf(getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 20 && getAdapterPosition() < 29) {
                    number_history_site = String.valueOf(getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 30 && getAdapterPosition() < 39) {
                    number_history_site = String.valueOf(getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 40 && getAdapterPosition() < 49) {
                    number_history_site = String.valueOf(getAdapterPosition() + 1);
                } else if ((getAdapterPosition() + 1) >= 50 && getAdapterPosition() < 59) {
                    number_history_site = String.valueOf(getAdapterPosition() + 1);
                }
                tvNumber_history_site.setText(number_history_site);
            } else {
                mainLayout.setVisibility(LinearLayout.GONE);
                project_id_history_site.setVisibility(View.INVISIBLE);
            }
        } else {
            mainLayout.setVisibility(LinearLayout.GONE);
            project_id_history_site.setVisibility(View.INVISIBLE);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Menu");
                builder.setItems(charMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
//                        if (which == 0) {
                        switch (which) {
                            case 0:
//                                Log.d(TAG, "Merge data foto off,");
                                onClickSiteProject(itemView);
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

            public void onClickSiteProject(View v) {
                Intent myIntent = new Intent(context, AndroidTabHistoryActivity.class);//AndroidTabLayoutActivity
                myIntent.putExtra("projectId", dataProjects.getProject_id());
                myIntent.putExtra("is_offline", "1");
                context.startActivity(myIntent);
            }

            public void onClickRemoveProgress(View v) {
                if (dataProjects.getStatus_alamat_actual_offline().contains("0") || dataProjects.getStatus_alamat_actual_offline().contains("3")) {
//                  https://github.com/lararufflecoles/RecyclerView100/blob/master/app/src/main/java/es/rufflecol/lara/recyclerview100/MainActivity.java#L42
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Warning...");
                    alertDialogBuilder.setMessage("Anda Yakin Akan Menghapus Progress Activity ini?");
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                  Toast.makeText(context, "On Progress", Toast.LENGTH_SHORT).show();
                            deleteEvidence(dataProjects);
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
                    if (dataProjects.getStatus_alamat_actual_offline().contains("1")) {
                        status = "Submit to server";
                    }
                    if (dataProjects.getStatus_alamat_actual_offline().contains("2")) {
                        status = "Project Approval";
                    }
                    Toast.makeText(context, "PID" + dataProjects.getProject_id() + " " + status, Toast.LENGTH_SHORT).show();
                }
            }
            void deleteEvidence(DataAlamatActualOffline dataOffline) {

            }

        });
    }
}
