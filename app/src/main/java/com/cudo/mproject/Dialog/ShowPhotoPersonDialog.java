package com.cudo.mproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cudo.mproject.Menu.Site.Person.PersonPhotoActivity;
import com.cudo.mproject.Model.PhotoPerson;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ShowPhotoPersonDialog extends Dialog {

    @BindView(R.id.xaxa)
    ImageView img;
    @BindView(R.id.delete)
    View delete;
    @BindView(R.id.save)
    View save;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.dummy)
    View dummy;
//    @BindView(R.id.extention)
//    Spinner spinner_extFile;
//    String extFile = "";
//    @BindView(R.id.input_layout)

    PersonPhotoActivity context;

    public ShowPhotoPersonDialog(@NonNull Context context, PersonPhotoActivity personPhotoActivity, PhotoPerson photoPerson, Realm realm) {
        super(context, R.style.AppTheme);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            /*getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);*/
            getWindow().setWindowAnimations(R.style.DialogTheme);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = personPhotoActivity;
        setContentView(R.layout.dialog_photo);
        ButterKnife.bind(this);
        dummy.requestFocus();
        initView(realm, photoPerson);
    }

    void initView(final Realm realm, final PhotoPerson photoPerson) {

        if (!photoPerson.getPathPhotoPerson().equals(""))
    /*    Glide.with(getContext()).setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(FileUtils.getUri(getContext(),m.getPath()))
                .into(img);*/
         Picasso.with(getContext()).invalidate(FileUtils.getUri(getContext(), photoPerson.getPathPhotoPerson()));
        Picasso.with(getContext()).load(FileUtils.getUri(getContext(),photoPerson.getPathPhotoPerson())).placeholder(R.drawable.ic_photo_camera_black_24dp).into(img);

        if (photoPerson.getStatusPhoto().contentEquals("0")) {
            Button saveButton = (Button) findViewById(R.id.save);
            saveButton.setEnabled(true);
            saveButton.setWidth(50);
            saveButton.setBackgroundColor(Color.parseColor("#D50000"));
//            saveButton.setVisibility(View.INVISIBLE);
            Button deleteButton = (Button) findViewById(R.id.delete);
            deleteButton.setEnabled(true);
            deleteButton.setWidth(50);
            deleteButton.setBackgroundColor(Color.parseColor("#00C853"));
//            initSpinnerExtFile();
//            String[] items = new String[]{"type", "jpg", "png"};
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
////         set the spinners adapter to the previously created one.
//            spinner_extFile.setAdapter(adapter);
////         mengeset listener untuk mengetahui saat item dipilih
//            spinner_extFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        extFile = spinner_extFile.getSelectedItem().toString();
//                        // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
//                        if (!adapter.getItem(i).contentEquals("type")) {
//                            Toast.makeText(context, "Selected " + adapter.getItem(i), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//

//            extFile = (String) spinner_extFile.getSelectedItem();

            desc.setText(photoPerson.getDescription());
//

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    extFile = String.valueOf(spinner_extFile.getSelectedItem());
//                    if (!extFile.contentEquals("type")) {
                    PhotoPerson.updateType(realm, photoPerson, desc.getText().toString(), "");
                    dismiss();
//                    }
                }
//                //
            });
//            else{

//            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.deletePic(photoPerson);
                    dismiss();
                }
            });
        } else {
//            spinner_extFile.setVisibility(View.GONE);
//
            Button saveButton = (Button) findViewById(R.id.save);
            saveButton.setEnabled(false);
            saveButton.setWidth(50);
            saveButton.setBackgroundColor(Color.parseColor("#ccffdd"));
//            saveButton.setVisibility(View.INVISIBLE);
            Button deleteButton = (Button) findViewById(R.id.delete);
            deleteButton.setEnabled(false);
            deleteButton.setWidth(50);
            deleteButton.setBackgroundColor(Color.parseColor("#80F44336"));
//            Button extButton = (Button) findViewById(R.id.extention);
//            extButton.setEnabled(false);
//            extButton.setBackgroundColor(Color.parseColor("#80F44336"));
//            deleteButton.setVisibility(View.INVISIBLE);
//            desc.setText(m.getDescription());
//            desc.setEnabled(false);
//
            TextView textEditFoto = (TextView) findViewById(R.id.edit_photo);
            textEditFoto.setText("View Photo");
//            TextView textAddFoto = (TextView) findViewById(R.id.add_photo);
//            textAddFoto.setText("Galery Photo");
        }

//
    }

//    void initSpinnerExtFile() {
//        String[] items = new String[]{"type", "jpg", "png"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
////         set the spinners adapter to the previously created one.
//        spinner_extFile.setAdapter(adapter);
////         mengeset listener untuk mengetahui saat item dipilih
//        spinner_extFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                extFile = spinner_extFile.getSelectedItem().toString();
//                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
//                if (!adapter.getItem(i).contentEquals("type")) {
//                    Toast.makeText(context, "Selected " + adapter.getItem(i), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
}
