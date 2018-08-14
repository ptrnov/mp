package com.cudo.mproject.Menu.Site.IMB;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.Menu.Photo.ViewHolderPhoto;
import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by adsxg on 3/21/2018.
 */

public class AdapterPhotoIMBRealm extends RealmRecyclerViewAdapter<PhotoIMB, ViewHolderPhotoIMB> {


    public AdapterPhotoIMBRealm(@Nullable OrderedRealmCollection<PhotoIMB> data) {
        super(data, true, true);
    }

    @Override
    public ViewHolderPhotoIMB onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderPhotoIMB vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        view = inflater.inflate(R.layout.image_item, parent, false);
        vh = new ViewHolderPhotoIMB(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderPhotoIMB holder, int position) {
        PhotoIMB pm = getItem(position);
        holder.setItem(pm);
    }
}
