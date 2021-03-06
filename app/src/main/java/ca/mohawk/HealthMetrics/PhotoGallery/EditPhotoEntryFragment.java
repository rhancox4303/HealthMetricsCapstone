package ca.mohawk.HealthMetrics.PhotoGallery;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

import static android.app.Activity.RESULT_OK;

/* The code in the Google Dev guide https://developer.android.com/training/camera/photobasics
 *  was used and modified to create this class.
 * */

/**
 * EditPhotoEntryFragment extends Fragment.
 * Allows the user to edit a photo entry.
 */
public class EditPhotoEntryFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    // Instantiate the Camera and Gallery Permission and intent codes.
    private static final int CAMERA_INTENT_CODE = 2000;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_INTENT_CODE = 2001;
    private static final int GALLERY_PERMISSION_CODE = 101;

    // Instantiate the healthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the views.
    private ImageView imageView;
    private EditText dateOfEntryEditText;

    // Initialize the photo entry id variable.
    private int photoEntryId;

    // Initialize the time variable.
    private String time;

    // Instantiate the photo path variables.
    private Uri currentPhotoUri;
    private String currentPhotoPath = null;
    private String previousPhotoPath = null;

    // Initialize the galleryId.
    private int galleryId;

    // Initialize the isFromGallery variable.
    private int isFromGallery;

    public EditPhotoEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_photo_entry, container, false);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the photo entry id from the bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            photoEntryId = bundle.getInt("selected_photo_key", -1);
        }

        imageView = rootView.findViewById(R.id.imageViewEditPhotoEntry);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryEditPhotoEntry);

        Button uploadImageButton = rootView.findViewById(R.id.buttonUploadImageEditPhotoEntry);
        Button editPhotoEntryButton = rootView.findViewById(R.id.buttonEditPhotoEntry);

        editPhotoEntryButton.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);
        dateOfEntryEditText.setOnClickListener(this);

        // Get the photo entry from the database.
        PhotoEntry photoEntry = healthMetricsDbHelper.getPhotoEntryById(photoEntryId);

        if (photoEntry != null) {
            currentPhotoPath = photoEntry.photoEntryPath;

            galleryId = photoEntry.photoGalleryId;
            dateOfEntryEditText.setText(photoEntry.dateOfEntry);
            isFromGallery = photoEntry.isFromGallery;

            // Load the photo with the Glide Library.
            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(photoEntry.photoEntryPath)
                    .fitCenter()
                    .into(imageView);
        } else {
            Toast.makeText(getActivity(), "Cannot get photo entry from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        }

        return rootView;
    }


    /**
     * Replaces the current fragment with a MetricsListFragment.
     */
    private void navigateToMetricsListFragment() {

        MetricsListFragment destinationFragment = new MetricsListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Runs when the choose image, date of entry edit text and edit photo entry buttons are pressed.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextDateOfEntryEditPhotoEntry:
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setOnTimeSetListener(this);
                timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "timePicker");
                break;
            case R.id.buttonEditPhotoEntry:
                updatePhotoEntry();
                break;
            case R.id.buttonUploadImageEditPhotoEntry:
                previousPhotoPath = currentPhotoPath;
                showImageDialog();
                break;
        }
    }

    /**
     * Create and show a InsertPhotoDialog dialog.
     */
    private void showImageDialog() {

        InsertPhotoDialog insertPhotoDialog = InsertPhotoDialog.newInstance();

        // Set the listeners to handle button clicks.
        insertPhotoDialog.setListener(new InsertPhotoDialog.InsertPhotoDialogListener() {
            @Override
            public void onInsertPhotoDialogPositiveClick(InsertPhotoDialog dialog) {
                // Check Camera Permissions.
                checkPermissions(CAMERA_PERMISSION_CODE);
            }

            @Override
            public void onInsertPhotoDialogNeutralClick(InsertPhotoDialog dialog) {
                // Check Gallery Permissions.
                checkPermissions(GALLERY_PERMISSION_CODE);
            }

            @Override
            public void onInsertPhotoDialogNegativeClick(InsertPhotoDialog dialog) {
                // Dismiss the dialog.
                dialog.dismiss();
            }
        });

        insertPhotoDialog.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(),
                "insertPhotoDialog");
    }

    /**
     * Checks the Camera and Gallery permissions based on the permission code.
     * <p>
     * Dispatches the intent if the requested permission has already been granted.
     *
     * @param permissionCode Represents the code of permission that will be checked.
     */
    private void checkPermissions(int permissionCode) {
        switch (permissionCode) {
            case CAMERA_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE);
                } else {
                    dispatchTakePictureIntent();
                }

                break;
            case GALLERY_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALLERY_PERMISSION_CODE);
                } else {
                    dispatchPickFromGalleryIntent();
                }
                break;
        }
    }

    /**
     * Runs after permissions are requested and dispatches a intent based on the results.
     *
     * @param requestCode  Represents the request code.
     * @param permissions  Represents the array of permissions.
     * @param grantResults Represents the results array.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // Validate if the permissions were granted.
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(getActivity(), "Camera permission is required to add photos using the camera.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case GALLERY_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchPickFromGalleryIntent();
                } else {
                    Toast.makeText(getActivity(), "Storage permissions is required to add photos using storage.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Creates and launches an intent to use the camera to capture an image.
     */
    private void dispatchTakePictureIntent() {

        // Create ACTION_IMAGE_CAPTURE intent.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {

            // Create a file in storage to the image in.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("IOException: ", ex.toString());
            }

            // Set the currentPhotoUri
            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(getActivity(),
                        "ca.mohawk.HealthMetrics.fileprovider",
                        photoFile);

                // Store the currentPhotoUri as an extra and launch the takePictureIntent.
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                startActivityForResult(takePictureIntent, CAMERA_INTENT_CODE);
            }
        }
    }

    /**
     * Creates and launches an intent to use the gallery to select an image.
     */
    private void dispatchPickFromGalleryIntent() {

        // Create intent.
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Launch intent.
        if (galleryIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY_INTENT_CODE);
        }
    }

    /**
     * Runs after the Gallery and Camera intents are finished.
     *
     * @param requestCode Represents the request code.
     * @param resultCode  Represents the result code.
     * @param data        Represents the returned data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Get the current fragment.
        Fragment currentFragment = Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);

        // If a image was received from the camera.
        if (requestCode == CAMERA_INTENT_CODE && resultCode == RESULT_OK) {

            isFromGallery = 0;

            // Use the Glide library to the load the image.
            Glide.with(Objects.requireNonNull(currentFragment))
                    .load(currentPhotoUri)
                    .into(imageView);
        }

        // If a image was received from the gallery.
        if (requestCode == GALLERY_INTENT_CODE && resultCode == RESULT_OK) {

            isFromGallery = 1;

            // Get the data from the intent.
            currentPhotoUri = data.getData();
            currentPhotoPath = Objects.requireNonNull(currentPhotoUri).toString();

            // Use the Glide library to the load the image.
            Glide.with(Objects.requireNonNull(currentFragment))
                    .load(currentPhotoUri)
                    .into(imageView);
        }

        // Delete the previously stored image.
        if (previousPhotoPath != null) {
            deleteImageFile(previousPhotoPath);
        }
    }

    /**
     * Create am image file to store images received from the camera in.
     *
     * @return Returns the created file.
     * @throws IOException Throws an IO exception if the file cannot be created.
     */
    private File createImageFile() throws IOException {

        // Create an image file name using the date.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Deletes the file at the given path.
     *
     * @param path Represents the path of the file to delete.
     */
    private void deleteImageFile(String path) {
        File file = new File(path);
        file.delete();
    }

    /**
     * Updates the photo entry in the database.s
     */
    private void updatePhotoEntry() {

        // Validate the user input.
        if (validateUserInput()) {

            String date = dateOfEntryEditText.getText().toString();

            PhotoEntry photoEntry = new PhotoEntry(photoEntryId, galleryId, currentPhotoPath, date, isFromGallery);
            if (healthMetricsDbHelper.updatePhotoEntry(photoEntry)) {
                ViewPhotoEntryFragment viewPhotoEntryFragment = new ViewPhotoEntryFragment();

                Bundle metricBundle = new Bundle();
                metricBundle.putInt("selected_photo_key", photoEntryId);
                viewPhotoEntryFragment.setArguments(metricBundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewPhotoEntryFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Unable to update the photo latestDataEntry.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateUserInput() {

        //  If date of entry is empty then inform the user and return false.
        if (dateOfEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date of entry cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // If date of entry is does not contain a date and time then inform the user and return false.
        if (!dateOfEntryEditText.getText().toString().matches("^(\\d+:\\d\\d)\\s(\\d+-\\d\\d-\\d+)$")) {
            Toast.makeText(getActivity(), "Both a date and time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // The a photo has not been entered then inform the user and return false.
        if (currentPhotoPath == null) {
            Toast.makeText(getActivity(), "Please enter a photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Runs when the TimePickerFragment onTimeSet listener is called.
     *
     * @param view      Represents the TimePicker view.
     * @param hourOfDay Represents the selected hour.
     * @param minute    Represents the selected minute.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (minute < 10) {
            time = hourOfDay + ":0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "datePicker");
    }

    /**
     * Runs when the DatePickerFragment onDateSet listener is called.
     *
     * @param view       Represents the DatePicker view.
     * @param year       Represents the selected year.
     * @param month      Represents the selected month.
     * @param dayOfMonth Represents the selected dayOfMonth.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ")
                    .append(month + 1).append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}