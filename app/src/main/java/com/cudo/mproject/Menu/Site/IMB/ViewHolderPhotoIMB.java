package com.cudo.mproject.Menu.Site.IMB;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cudo.mproject.Model.PhotoIMB;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adsxg on 3/21/2018.
 */

public class ViewHolderPhotoIMB extends RecyclerView.ViewHolder {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.desc)
    TextView desc;

    public ViewHolderPhotoIMB(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setItem(final PhotoIMB photoimb)
    {
        desc.setTag(getAdapterPosition());
        image.setVisibility(photoimb.getPathPhotoIMB().equals("")?View.GONE:View.VISIBLE);
        if(!photoimb.getPathPhotoIMB().equals(""))
        {
            //TODO CLEAR CACHE GLIDE
         /*   Glide.with(itemView.getContext()).setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .load(FileUtils.getUri(itemView.getContext(),m.getPath()))
                    .into(image);*/

            Picasso.with(itemView.getContext()).invalidate(FileUtils.getUri(itemView.getContext(),photoimb.getPathPhotoIMB()));
            Picasso.with(itemView.getContext()).load(FileUtils.getUri(itemView.getContext(),photoimb.getPathPhotoIMB())).placeholder(R.drawable.ic_photo_camera_black_24dp).into(image);

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IMBPhotoActivity)itemView.getContext()).ShowPhotoIMBDialog(photoimb);

            }
        });



    }
}
