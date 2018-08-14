package com.cudo.mproject.Menu.Photo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Utils.FileUtils;
import com.squareup.picasso.Picasso;
import com.cudo.mproject.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adsxg on 12/12/2017.
 */

public class ViewHolderPhoto extends RecyclerView.ViewHolder {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.desc)
    TextView desc;

    public ViewHolderPhoto(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setItem(final Photo m)
    {

        desc.setTag(getAdapterPosition());
        desc.setText(m.getDescription());
        image.setVisibility(m.getPath().equals("")?View.GONE:View.VISIBLE);
        if(!m.getPath().equals(""))
        {
            //TODO CLEAR CACHE GLIDE
         /*   Glide.with(itemView.getContext()).setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .load(FileUtils.getUri(itemView.getContext(),m.getPath()))
                    .into(image);*/

            Picasso.with(itemView.getContext()).invalidate(FileUtils.getUri(itemView.getContext(),m.getPath()));
            Picasso.with(itemView.getContext()).load(FileUtils.getUri(itemView.getContext(),m.getPath())).placeholder(R.drawable.ic_photo_camera_black_24dp).into(image);

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PhotoActivity)itemView.getContext()).ShowPhoto(m);

            }
        });



    }
}
