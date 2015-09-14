package ua.com.dxlab.lesson_18_gmapexperience.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import ua.com.dxlab.lesson_18_gmapexperience.MapsActivity;
import ua.com.dxlab.lesson_18_gmapexperience.R;
import ua.com.dxlab.lesson_18_gmapexperience.model.MarkerItem;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

/**
 * Created by Dima on 14.09.2015.
 */
public class CustomMarkerSelectDialog  extends DialogFragment {
    public static final int PICK_IMAGE = 0;

    private LatLng mLatLng;
    private String mImageURI = "";
    private EditText mEditTxtMarkerText;
    private ImageView mImgViewMarkerImage;
    private ImageButton mImgBtnMarkerImage;
    private Button mBtnOk;
    private Button mBtnCancel;

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng _latLng) {
        mLatLng = _latLng;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_custom_marker_select, container);
        initUI(view);

        getDialog().setTitle(R.string.set_custom_marker_fileds);
        getDialog().getWindow().setLayout(100, 100);


        return view;
    }

    private void initUI(View _view) {
        mEditTxtMarkerText = (EditText) _view.findViewById(R.id.editTxtMarkerText_FDCMS);
        mImgViewMarkerImage = (ImageView) _view.findViewById(R.id.imgViewMarkerImage_FDCMS);
        mImgBtnMarkerImage = (ImageButton) _view.findViewById(R.id.imgBtnMarkerImage_FDCMS);
        mBtnOk = (Button) _view.findViewById(R.id.btnOk_FDCMS);
        mBtnCancel = (Button) _view.findViewById(R.id.btnCancel_FDCMS);

        setListeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Log.d("Path", data.getData().getPath());
                    mImageURI = data.getData().toString();
                    MapsActivity callingActivity = (MapsActivity) getActivity();
                    Picasso.with(callingActivity).load(mImageURI).into(mImgViewMarkerImage);
                }
            }
        }
        mBtnOk.setEnabled(mEditTxtMarkerText.getText().toString().length()>0 && mImageURI.length()>0 ? true : false);
    }

    private void setListeners() {
        mImgBtnMarkerImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MarkerItem markerItem = new MarkerItem();

                markerItem.setImageURI(mImageURI);
                markerItem.setCustomized(true);
                markerItem.setTitle(mEditTxtMarkerText.getText().toString());
                markerItem.setLatitude(mLatLng.latitude);
                markerItem.setLongitude(mLatLng.longitude);

                MapsActivity callingActivity = (MapsActivity) getActivity();
                callingActivity.onCustomMarkerSelectDialogValue(markerItem);
                dismiss();
            }
        });

        mEditTxtMarkerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnOk.setEnabled(mEditTxtMarkerText.getText().toString().length()>0 && mImageURI.length()>0 ? true : false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
