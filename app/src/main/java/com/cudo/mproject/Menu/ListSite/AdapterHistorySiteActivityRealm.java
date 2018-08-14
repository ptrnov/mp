package com.cudo.mproject.Menu.ListSite;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.Menu.History.ViewHistoryTask;
import com.cudo.mproject.Model.DataAlamatActualOffline;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.R;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class AdapterHistorySiteActivityRealm extends RealmRecyclerViewAdapter<DataAlamatActualOffline, ViewHistorySiteTask> {



    public AdapterHistorySiteActivityRealm(@Nullable OrderedRealmCollection<DataAlamatActualOffline> data) {
        super(data, true, true);

    }

    public void filterResults(String text, Realm realm) {
        text = text == null ? "" : text.toLowerCase().trim();
        if(text.length()<3) {
            updateData(realm.where(DataAlamatActualOffline.class).findAll());
        } else {
            updateData(realm.where(DataAlamatActualOffline.class)
                    .beginGroup()
                    .contains("project_id", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("siteName", text, Case.INSENSITIVE) // TODO: change field
                    .endGroup()
                    .findAll());
        }
    }
    @Override
    public ViewHistorySiteTask onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHistorySiteTask vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
        view = inflater.inflate(R.layout.task_history_site_item, parent, false);
        vh = new ViewHistorySiteTask(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHistorySiteTask holder, int position) {
        DataAlamatActualOffline dataProject = getItem(position);
        holder.setItem(dataProject);
    }
}
