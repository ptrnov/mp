package com.cudo.mproject.Menu.Site.Person;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cudo.mproject.Menu.Photo.PhotoActivity;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderPhotoPerson extends RecyclerView.ViewHolder {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.desc)
    TextView desc;

    public ViewHolderPhotoPerson(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setItem(final PhotoPerson photoPerson)
    {
        desc.setTag(getAdapterPosition());
        image.setVisibility(photoPerson.getPathPhotoPerson().equals("")?View.GONE:View.VISIBLE);
        if(!photoPerson.getPathPhotoPerson().equals(""))
        {
            //TODO CLEAR CACHE GLIDE
         /*   Glide.with(itemView.getContext()).setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .load(FileUtils.getUri(itemView.getContext(),m.getPath()))
                    .into(image);*/

            Picasso.with(itemView.getContext()).invalidate(FileUtils.getUri(itemView.getContext(),photoPerson.getPathPhotoPerson()));
            Picasso.with(itemView.getContext()).load(FileUtils.getUri(itemView.getContext(),photoPerson.getPathPhotoPerson())).placeholder(R.drawable.ic_photo_camera_black_24dp).into(image);

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PersonPhotoActivity)itemView.getContext()).ShowPhotoPersonDialog(photoPerson);

            }
        });



    }
}
