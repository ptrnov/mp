package com.cudo.mproject.Menu.Photo;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.R;
import com.cudo.mproject.Model.Photo;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by adsxg on 12/12/2017.
 */

public class AdapterPhotoRealm extends RealmRecyclerViewAdapter<Photo,ViewHolderPhoto> {

    public AdapterPhotoRealm(@Nullable OrderedRealmCollection<Photo> data) {
        super(data, true, true);

    }


    @Override
    public ViewHolderPhoto onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderPhoto vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        view = inflater.inflate(R.layout.image_item, parent, false);
        vh = new ViewHolderPhoto(view);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolderPhoto holder, int position) {
        Photo m = getItem(position);
        holder.setItem(m);

    }


}