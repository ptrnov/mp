package com.cudo.mproject.Menu.Site.PLN;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.Model.PhotoPLN;
import com.cudo.mproject.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AdapterPhotoPlnRealm extends RealmRecyclerViewAdapter<PhotoPLN, ViewHolderPhotoPln> {


    public AdapterPhotoPlnRealm(@Nullable OrderedRealmCollection<PhotoPLN> data) {
        super(data, true, true);
    }

//    public AdapterPhotoPersonRealm(RealmResults<PhotoPerson> all) {
//        super();
//    }

    @Override
    public ViewHolderPhotoPln onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderPhotoPln vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
        view = inflater.inflate(R.layout.image_item, parent, false);
        vh = new ViewHolderPhotoPln(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderPhotoPln holder, int position) {
        PhotoPLN pm = getItem(position);
        holder.setItem(pm);
    }
}