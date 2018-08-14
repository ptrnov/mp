package com.cudo.mproject.Menu.History;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.User;
import com.cudo.mproject.R;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

/**
 * Created by adsxg on 12/14/2017.
 */

public class AdapterHistoryActivityRealm extends RealmRecyclerViewAdapter<OfflineDataTransaction, ViewHistoryTask> {

//    Realm realm = Realm.getDefaultInstance();
//    User user = realm.where(User.class).findFirst();
//
//    OfflineDataTransaction dataLocals = realm.where(OfflineDataTransaction.class)
//            .equalTo("userName", user.getUsername())
//            .findFirst();

//    OfflineDataTransaction dataLocal = realm.where(OfflineDataTransaction.class)
//            .equalTo("id_offline_transaction", dataLocals.getId_offline_transaction()) .findFirst();

    public AdapterHistoryActivityRealm(@Nullable OrderedRealmCollection<OfflineDataTransaction> data) {
        super(data, true, true);

    }

    public void filterResults(String text, Realm realm) {
        text = text == null ? "" : text.toLowerCase().trim();
        if (text.length() < 3) {


//          updateData(realm.where(OfflineDataTransaction.class)
//                  .equalTo("userName",  user.getUsername()).findAll());
            updateData(realm.where(OfflineDataTransaction.class)
                    .equalTo("id_offline_transaction", text, Case.INSENSITIVE) // TODO: change field
                    .findAll());
        } else {
//            updateData(realm.where(OfflineDataTransaction.class)
//                    .beginGroup()
//                    .contains("project_id_history", text, Case.INSENSITIVE) // TODO: change field
//                    .or()
//                    .contains("wp_id_history", text, Case.INSENSITIVE) // TODO: change field
//                    .or()
//                    .contains("site_name_history", text, Case.INSENSITIVE) // TODO: change field
//                    .or()
//                   .contains("sow", text, Case.INSENSITIVE) // TODO: change field
//                   .endGroup()
//                    .findAll());
            updateData(realm.where(OfflineDataTransaction.class)
                    .beginGroup()
                    .contains("project_id_history", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("wp_id_history", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("site_name_history", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("id_offline_transaction", text, Case.INSENSITIVE) // TODO: change field
                    .endGroup()
                    .findAll());
//                    .findAllSorted("id_offline_transaction", Sort.DESCENDING));
        }
    }

    @Override
    public ViewHistoryTask onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHistoryTask vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
        view = inflater.inflate(R.layout.task_history_item, parent, false);
        vh = new ViewHistoryTask(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHistoryTask holder, int position) {
//
        OfflineDataTransaction datalocal = getItem(position);
        holder.setItem(datalocal);
//         dataLocals = getItem(position);
//         holder.setItem(dataLocals);
    }
}
