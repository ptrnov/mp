package com.cudo.mproject.Menu.Video;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.cudo.mproject.Menu.Photo.ViewHolderPhoto;
import com.cudo.mproject.Model.Photo;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AdapterVideoRealm  extends RealmRecyclerViewAdapter<Photo,ViewHolderVideo> {
    public AdapterVideoRealm(@Nullable OrderedRealmCollection<Photo> data) {
        super(data, true, true);

    }


    @Override
    public ViewHolderVideo onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolderVideo holder, int position) {

    }
}
