//Taken from https://developer.android.com/training/camera/photobasics
package ca.mohawk.HealthMetrics.PhotoGallery;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.mohawk.HealthMetrics.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPhotoEntryFragment extends Fragment implements View.OnClickListener {


    private ImageView imageView;

    private static final int CAMERA_INTENT_CODE = 2000;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private String currentPhotoPath = null;
    private Uri currentPhotoUri = null;
    private static final int GALLERY_INTENT_CODE = 2001;
    private static final int GALLERY_PERMISSION_CODE = 101;
    Bitmap currentImage;

    public AddPhotoEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_photo_entry, container, false);
        imageView = rootView.findViewById(R.id.imageViewAddPhotoEntry);
        Button button = rootView.findViewById(R.id.buttonUploadImageAddPhotoEntry);
        button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (currentPhotoPath != null) {
            File file = new File(currentPhotoPath);
            boolean deleted = file.delete();
        }

        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(getActivity(), "Camera permission is required to add photos using the camera.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case GALLERY_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchPickFromGalleryIntent();
                } else {
                    Toast.makeText(getActivity(), "Storage permissions is required to add photos using storage.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_INTENT_CODE && resultCode == RESULT_OK) {
            currentPhotoUri = data.getData();
            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            Glide.with(currentFragment)
                    .load(currentPhotoUri)
                    .into(imageView);


            if (requestCode == GALLERY_INTENT_CODE && resultCode == RESULT_OK) {
                try {
                    currentPhotoUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), currentPhotoUri);
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkPermissions(int permissionCode) {
        switch (permissionCode) {
            case CAMERA_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE);
                } else {
                    dispatchTakePictureIntent();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALLERY_PERMISSION_CODE);
                } else {
                    dispatchPickFromGalleryIntent();
                }
                break;
        }
    }

    private void showDialog() {
        InsertPhotoDialog dialog = InsertPhotoDialog.newInstance();
        dialog.setListener(new InsertPhotoDialog.insertPhotoDialogListener() {
            @Override
            public void onInsertPhotoDialogPositiveClick(InsertPhotoDialog dialog) {
                checkPermissions(CAMERA_PERMISSION_CODE);
            }

            @Override
            public void onInsertPhotoDialogNeutralClick(InsertPhotoDialog dialog) {
                checkPermissions(GALLERY_PERMISSION_CODE);
            }

            @Override
            public void onInsertPhotoDialogNegativeClick(InsertPhotoDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager().beginTransaction(), "timePicker");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
           /* // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("ERROR", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(getActivity(),
                        "ca.mohawk.HealthMetrics.fileprovider",
                        photoFile);*/
            startActivityForResult(takePictureIntent, CAMERA_INTENT_CODE);
        }
    }

    private void dispatchPickFromGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
           /* // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("ERROR", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(getActivity(),
                        "ca.mohawk.HealthMetrics.fileprovider",
                        photoFile);*/

            startActivityForResult(galleryIntent, GALLERY_INTENT_CODE);
        }
    }

    //google
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("PATH", currentPhotoPath);
        return image;
    }
}



