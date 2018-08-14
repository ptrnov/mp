package com.cudo.mproject.Menu.PhotoDocument;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.Menu.Photo.ViewHolderPhoto;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AdapterPhotoDocumentRealm  extends RealmRecyclerViewAdapter<Photo,ViewHolderPhotoDocument> {

    public AdapterPhotoDocumentRealm(@Nullable OrderedRealmCollection<Photo> data) {
        super(data, true, true);

    }


    @Override
    public ViewHolderPhotoDocument onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderPhotoDocument vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        view = inflater.inflate(R.layout.image_item, parent, false);
        vh = new ViewHolderPhotoDocument(view);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolderPhotoDocument holder, int position) {
        Photo m = getItem(position);
        holder.setItem(m);

    }


}