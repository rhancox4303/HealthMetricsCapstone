package ca.mohawk.HealthMetrics.PhotoGallery;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.net.Uri;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditPhotoEntryFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final int CAMERA_INTENT_CODE = 2000;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_INTENT_CODE = 2001;
    private static final int GALLERY_PERMISSION_CODE = 101;

    private String time;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private ImageView imageView;
    private EditText dateOfEntryEditText;
    private int PhotoId;

    private Uri currentPhotoUri;
    private int isFromGallery;
    private String currentPhotoPath = null;
    private String previousPhotoPath = null;
    private int GalleryId;

    public EditPhotoEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_edit_photo_entry, container, false);

        imageView = rootView.findViewById(R.id.imageViewEditPhotoEntry);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryEditPhotoEntry);

        Button uploadImageButton = rootView.findViewById(R.id.buttonUploadImageEditPhotoEntry);
        Button editPhotoEntryButton = rootView.findViewById(R.id.buttonEditPhotoEntry);

        editPhotoEntryButton.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);
        dateOfEntryEditText.setOnClickListener(this);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            PhotoId = bundle.getInt("selected_photo_key", -1);
        }
        Toast.makeText(getActivity(), PhotoId + "", Toast.LENGTH_SHORT).show();
        PhotoEntry photoEntry = healthMetricsDbHelper.getPhotoEntryById(PhotoId);
        currentPhotoPath = photoEntry.PhotoEntryPath;

        GalleryId = photoEntry.PhotoGalleryId;
        dateOfEntryEditText.setText(photoEntry.DateOfEntry);
        isFromGallery = photoEntry.IsFromGallery;

        Glide.with(getActivity())
                .load(photoEntry.PhotoEntryPath)
                .fitCenter()
                .into(imageView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editTextDateOfEntryEditPhotoEntry:
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setOnTimeSetListener(this);
                timePickerFragment.show(getFragmentManager().beginTransaction(), "timePicker");
                break;
            case R.id.buttonEditPhotoEntry:
                editPhotoEntry();

                ViewPhotoEntryFragment viewPhotoEntryFragment = new ViewPhotoEntryFragment();

                Bundle metricBundle = new Bundle();
                metricBundle.putInt("selected_photo_key", PhotoId);
                viewPhotoEntryFragment.setArguments(metricBundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewPhotoEntryFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.buttonUploadImageEditPhotoEntry:
                previousPhotoPath = currentPhotoPath;
                Log.d("DELETEPATH",previousPhotoPath);
                showImageDialog();
                break;
        }
    }

    private void editPhotoEntry() {
        if (validateUserInput()) {
            String date = dateOfEntryEditText.getText().toString();

            PhotoEntry photoEntry = new PhotoEntry(PhotoId, GalleryId, currentPhotoPath, date,isFromGallery);
            boolean test = healthMetricsDbHelper.updatePhotoEntry(photoEntry);

            Toast.makeText(getContext(), String.valueOf(test), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUserInput() {
        if (dateOfEntryEditText.getText().toString().trim().equals("") || currentPhotoPath == null) {
            Toast.makeText(getActivity(), "Please enter all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showImageDialog() {
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

        dialog.show(getFragmentManager().beginTransaction(), "imagePicker");
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
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
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                startActivityForResult(takePictureIntent, CAMERA_INTENT_CODE);
            }
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

        currentPhotoPath = image.getAbsolutePath();
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void deleteImageFile(String path) {
        Log.d("PATH", path);
        File file = new File(path);
        file.delete();
    }

    private void dispatchPickFromGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY_INTENT_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (requestCode == CAMERA_INTENT_CODE && resultCode == RESULT_OK) {
            isFromGallery = 0;
            Glide.with(currentFragment)
                    .load(currentPhotoUri)
                    .into(imageView);
        }

        if (requestCode == GALLERY_INTENT_CODE && resultCode == RESULT_OK) {
            isFromGallery = 1;
            currentPhotoUri = data.getData();
            currentPhotoPath = currentPhotoUri.toString();
            Glide.with(currentFragment)
                    .load(currentPhotoUri)
                    .into(imageView);
        }

        if (previousPhotoPath != null) {
            Log.d("DELETEPATH", previousPhotoPath);
            deleteImageFile(previousPhotoPath);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (minute < 10) {
            time = hourOfDay + ":0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }
        dateOfEntryEditText.setText(time);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(getFragmentManager().beginTransaction(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfEntryEditText.setText(time + " " + (month + 1) + "-0" + dayOfMonth + "-" + year);
        } else {
            dateOfEntryEditText.setText(time + " " + (month + 1) + "-" + dayOfMonth + "-" + year);
        }
    }
}

