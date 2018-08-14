package com.cudo.mproject.Menu.Project;

import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cudo.mproject.Model.Project;
import com.cudo.mproject.R;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by adsxg on 12/12/2017.
 */

public class AdapterActivityRealm extends RealmRecyclerViewAdapter<Project,ViewHolderTask> {


    public AdapterActivityRealm(@Nullable OrderedRealmCollection<Project> data) {
        super(data, true, true);

    }

    public void filterResults(String text, Realm realm) {
        text = text == null ? "" : text.toLowerCase().trim();
        if(text.length()<3) {
            updateData(realm.where(Project.class).findAll());
        } else {
            updateData(realm.where(Project.class)
                    .beginGroup()
                    .contains("project_id", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("nama_tenant", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("site_name", text, Case.INSENSITIVE) // TODO: change field
                    .or()
                    .contains("sow", text, Case.INSENSITIVE) // TODO: change field
                    .endGroup()
                    .findAll());
        }
    }
    @Override
    public ViewHolderTask onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderTask vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        view = inflater.inflate(R.layout.task_item, parent, false);
        vh = new ViewHolderTask(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderTask holder, int position) {
        Project m = getItem(position);
        holder.setItem(m);

    }




}