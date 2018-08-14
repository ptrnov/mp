package com.cudo.mproject.Menu.Project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Menu.Tab.AndroidTabLayoutActivity;
import com.cudo.mproject.Menu.TaskActivity.TaskActivity;
import com.cudo.mproject.Model.GpsCache;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.ActivityUtils;
import com.cudo.mproject.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by adsxg on 12/12/2017.
 */

public class ViewHolderTask extends RecyclerView.ViewHolder {


    @BindView(R.id.project_id)
    TextView project_id;
    @BindView(R.id.sitename)
    TextView sitename;
    @BindView(R.id.tenant)
    TextView tenant;
    @BindView(R.id.sow)
    TextView sow;
    @BindView(R.id.number)
    TextView tvNumber;
    //    @BindView(R.id.textViewOptions)
//    TextView buttonViewOption;
    //    public TextView buttonViewOption;
    private Context context;
    public GpsCache gpsCache;
    public BaseActivity baseActivity;
    public Realm realm;

    //    CharSequence charMenu[] = new CharSequence[]{"Update Site Profile", "Progres Project"};
    CharSequence charMenu[] = new CharSequence[]{"Update Site Profile", "Progress Project"};
    final Integer[] icons = new Integer[]{android.R.drawable.ic_media_play, android.R.drawable.ic_input_add};
//    ListAdapter adapter = new ArrayAdapterWithIcon(this, charMenu, icons);

    public ViewHolderTask(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        realm = Realm.getDefaultInstance();
    }

    public void setItem(final Project m) {
        project_id.setText(m.getProject_id());
        sitename.setText(m.getSite_name());
        tenant.setText(m.getNama_tenant());
        sow.setText(m.getSow());
        String number = "00";
//        if (getAdapterPosition() < 9) {
//            number = "0" + (getAdapterPosition() + 1);
//        } else if (getAdapterPosition() > 9 && getAdapterPosition() < 19) {
//            number = "1" + (getAdapterPosition() + 1);
//        } else if (getAdapterPosition() > 19 && getAdapterPosition() < 29) {
//            number = "2" + (getAdapterPosition() + 1);
//        }else if (getAdapterPosition() > 29 && getAdapterPosition() < 39) {
//            number = "3" + (getAdapterPosition() + 1);
//        }
        if (getAdapterPosition() < 9) {
            number = "0" + (getAdapterPosition() + 1);
        } else if ((getAdapterPosition() + 1) >= 10 && getAdapterPosition() < 19) {
            number = String.valueOf(getAdapterPosition() + 1);
        } else if ((getAdapterPosition() + 1) >= 20 && getAdapterPosition() < 29) {
            number = String.valueOf(getAdapterPosition() + 1);
        }
        else if ((getAdapterPosition() + 1) >= 30 && getAdapterPosition() < 39) {
            number = String.valueOf(getAdapterPosition() + 1);
        }
        else if ((getAdapterPosition() + 1) >=50 && getAdapterPosition() <59) {
            number = String.valueOf(getAdapterPosition() + 1);
        }

        tvNumber.setText(number);
        /*
         * Create context menu
         * - Menu Update Site Profile
         * - Menu Progress Project
         * create date 0601018
         * create time 15.00
         * https://stackoverflow.com/questions/10321649/start-an-activity-with-an-intent-from-another-activitys-contextmenu
         * https://gist.github.com/gauravat16/e8e03496a4056829e65dede3c236da28
         * https://developer.android.com/reference/android/view/MenuItem.OnMenuItemClickListener.html
         */
//        itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                v.setLongClickable(false);
//                menu.setHeaderTitle("Select The Action");
////                menu.add(0, v.getId(), 0, "Site Profile");//groupId, itemId, order, title
////                menu.add(0, v.getId(), 0, "Progress Project");
//                MenuItem siteProfile = menu.add(0, 1000, 0, "Site Profile");//groupId, itemId, order, title
//                MenuItem progressProject = menu.add(1, 1001, 0, "Progress Project");
//                siteProfile.setOnMenuItemClickListener(onMenu);
//                progressProject.setOnMenuItemClickListener(onMenu);
//
//            }
//
//            //ADD AN ONMENUITEM LISTENER TO EXECUTE COMMANDS ONCLICK OF CONTEXT MENU TASK
//            private final MenuItem.OnMenuItemClickListener onMenu;
//
//            {
//                onMenu = new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        final Intent intent;
//                        switch (item.getItemId()) {
//                            case 1000:
//                                Toast.makeText(context, "UPDATE SITE PROFILE", Toast.LENGTH_LONG).show();
////                                ActivityUtils.goToActivity(this, SiteProfileActivity.class, 101, m.getProject_id(), PROJECT_ID);
////                                ((SiteProfileActivity) itemView.getContext()).onClickItem(m);
////                                ActivityUtils.goToActivity(context, SiteProfileActivity.class, 101, m.getProject_id(), PROJECT_ID);
//                                onClick(itemView);
//                                break;
//                            case 1001:
//                                Toast.makeText(context, "PROGRESS PROJECT", Toast.LENGTH_LONG).show();
////                                ((ProjectListActivity) itemView.getContext()).onClickItem(m);
//                                ((ProjectListActivity) context).onClickItem(m);
//                                break;
//                        }
//                        return true;
//                    }
//
//                };
//            }
//
//            public void onClick(View v) {
////                ActivityUtils.goToActivity(context, SiteProfileActivity.class, 101, m.getProject_id(), PROJECT_ID);
//                // Start NewActivity.class
//                Intent myIntent = new Intent(context, SiteProfileActivity.class);
//                myIntent.putExtra("Id", m.getProject_id());
//                myIntent.putExtra("is_offline", "0");
//                context.startActivity(myIntent);
//            }
//        });

/* not use
//        itemView.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            ((ProjectListActivity)itemView.getContext()).onClickItem(m);
//        }
//    });
*/
        /*
         * Create Popup menu WITH three dot
         * - Menu Update Site Profile
         * - Menu Progress Project
         * create date 0801018
         * create time 17.00
         * https://gist.github.com/brettwold/45039b7f02ce752ae0d32522a8e2ad9c
         */

//        itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//                v.setLongClickable(true);
//                menu.setHeaderTitle("Select The Action");
//                PopupMenu popup = new PopupMenu(v.getContext(), buttonViewOption);
//                popup.getMenuInflater().inflate(R.menu.contex_menu, popup.getMenu());
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        switch (item.getItemId()) {
//                            case R.id.siteProfile:
//                                Toast.makeText(context, "UPDATE SITE PROFILE", Toast.LENGTH_LONG).show();
//                                onClick(itemView);
//                                break;
//                            case R.id.progress:
//                                Toast.makeText(context, "PROGRESS PROJECT", Toast.LENGTH_LONG).show();
//                                ((ProjectListActivity) context).onClickItem(m);
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popup.show();
//            }
         /*
         * Create Popup menu WITH  AlertDialog.Builder builder
         * - Menu Update Site Profile
         * - Menu Progress Project
         * create date 0901018
         * create time 17.00
         * https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
         */

        itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
//                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Menu");
                builder.setItems(charMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Drawable drawable;
                        switch (which) {
                            case 0:
//                                Toast.makeText(context, "UPDATE SITE PROFILE", Toast.LENGTH_SHORT).show();
//                                drawable = builder.getContext().getResources().getDrawable(android.R.drawable.ic_media_play);
//                                drawable.setBounds((int) (drawable.getIntrinsicWidth() * 0.5),
//                                        0, (int) (drawable.getIntrinsicWidth() * 1.5),
//                                        drawable.getIntrinsicHeight());
//
                                onClickSiteProject(m);//itemView
                                break;
                            case 1:
//                                Toast.makeText(context, "PROGRESS PROJECT", Toast.LENGTH_SHORT).show();
//                                builder.setIcon(context.getDrawable(android.R.drawable.ic_input_add));
//                                drawable = builder.getContext().getResources().getDrawable(android.R.drawable.ic_input_add);
//                                drawable.setBounds((int) (drawable.getIntrinsicWidth() * 0.5),
//                                        0, (int) (drawable.getIntrinsicWidth() * 1.5),
//                                        drawable.getIntrinsicHeight());
//
//                                ((ProjectListActivity) itemView.getContext()).onClickItem(m);
                                onClickItem(m);
                                break;
                        }
                    }
                });
                builder.show();
            }

            public void onClickSiteProject(Project m) {//View v
                Intent myIntent = new Intent(context, AndroidTabLayoutActivity.class);
                myIntent.putExtra("projectId",  m.getProject_id());
                myIntent.putExtra("is_offline", "0");
                context.startActivity(myIntent);

            }
            public void onClickItem(Project m) {
                Intent myIntent = new Intent(context, TaskActivity.class);
                myIntent.putExtra("projectId", m.getProject_id());
                myIntent.putExtra("is_offline", "0");
                context.startActivity(myIntent);
            }

        });
         /*
         * Create list(option) menu
         * - Menu Update Site Profile
         * - Menu Progress Project
         * create date 0801018
         * create time 17.00
         * https://www.simplifiedcoding.net/create-options-menu-recyclerview-item-tutorial/
         * https://stackoverflow.com/questions/30847096/android-getmenuinflater-in-a-fragment-subclass-cannot-resolve-method
         */
//        itemView.setOnClickListener(new View.OnClickListener() {
//            public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//                MenuInflater inflater = getMenuInflater();
//                inflater.inflate(R.menu.contex_menu, menu);
//                inflater.inflate(R.menu.contex_menu, menu) ;
//                return true;
//            }
//
//
//            public boolean onOptionsItemSelected(MenuItem item) {
//                if (item.getItemId() == R.id.siteProfile) {
//                    Toast.makeText(context, "UPDATE SITE PROFILE", Toast.LENGTH_LONG).show();
//                    onClick(itemView);
//                }
//
//                if (item.getItemId() == R.id.progress) {
//                    Toast.makeText(context, "PROGRESS PROJECT", Toast.LENGTH_LONG).show();
//                              ((ProjectListActivity) itemView.getContext()).onClickItem(m);
//                    ((ProjectListActivity) context).onClickItem(m);
//                }
//                return onOptionsItemSelected(item);
//            }
//
//            //
//            public void onClick(View v) {
//           ActivityUtils.goToActivity(context, SiteProfileActivity.class, 101, m.getProject_id(), PROJECT_ID);
//                // Start NewActivity.class
//                Intent myIntent = new Intent(context, SiteProfileActivity.class);
//                myIntent.putExtra("Id", m.getProject_id());
//                myIntent.putExtra("is_offline", "0");
//                context.startActivity(myIntent);
//            }
//        });


    }


}