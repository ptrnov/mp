package com.cudo.mproject.Menu.Site.PLN;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cudo.mproject.Menu.Site.Person.PersonPhotoActivity;
import com.cudo.mproject.Model.PhotoPLN;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderPhotoPln extends RecyclerView.ViewHolder {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.desc)
    TextView desc;

    public ViewHolderPhotoPln(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setItem(final PhotoPLN photoPLN)
    {
        desc.setTag(getAdapterPosition());
        image.setVisibility(photoPLN.getPathPhotoPLN().equals("")?View.GONE:View.VISIBLE);
        if(!photoPLN.getPathPhotoPLN().equals(""))
        {
            //TODO CLEAR CACHE GLIDE
         /*   Glide.with(itemView.getContext()).setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .load(FileUtils.getUri(itemView.getContext(),m.getPath()))
                    .into(image);*/

            Picasso.with(itemView.getContext()).invalidate(FileUtils.getUri(itemView.getContext(),photoPLN.getPathPhotoPLN()));
            Picasso.with(itemView.getContext()).load(FileUtils.getUri(itemView.getContext(),photoPLN.getPathPhotoPLN())).placeholder(R.drawable.ic_photo_camera_black_24dp).into(image);

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PLNPhotoActivity)itemView.getContext()).ShowPhotoPlnDialog(photoPLN);

            }
        });



    }
}
