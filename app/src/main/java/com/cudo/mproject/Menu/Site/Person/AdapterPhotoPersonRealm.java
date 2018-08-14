package com.cudo.mproject.Menu.Site.Person;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cudo.mproject.Menu.Photo.ViewHolderPhoto;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AdapterPhotoPersonRealm  extends RealmRecyclerViewAdapter<PhotoPerson, ViewHolderPhotoPerson>  {


    public AdapterPhotoPersonRealm(@Nullable OrderedRealmCollection<PhotoPerson> data) {
        super(data, true, true);
    }

//    public AdapterPhotoPersonRealm(RealmResults<PhotoPerson> all) {
//        super();
//    }

    @Override
    public ViewHolderPhotoPerson onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderPhotoPerson vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
        view = inflater.inflate(R.layout.image_item, parent, false);
        vh = new ViewHolderPhotoPerson(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderPhotoPerson holder, int position) {
        PhotoPerson pm = getItem(position);
        holder.setItem(pm);
    }
}