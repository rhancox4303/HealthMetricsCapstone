package ca.mohawk.HealthMetrics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ca.mohawk.HealthMetrics.DataEntry.DataEntryListFragment;
import ca.mohawk.HealthMetrics.DataEntry.DeleteDataEntryDialog;
import ca.mohawk.HealthMetrics.MetricManagement.DeleteMetricDialog;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.MetricManagement.RemoveMetricDialog;
import ca.mohawk.HealthMetrics.Note.DeleteNoteDialog;
import ca.mohawk.HealthMetrics.Notification.DeleteNotificationDialog;
import ca.mohawk.HealthMetrics.Notification.NotificationListFragment;
import ca.mohawk.HealthMetrics.PhotoGallery.DeleteGalleryDialog;
import ca.mohawk.HealthMetrics.PhotoGallery.DeletePhotoEntryDialog;
import ca.mohawk.HealthMetrics.PhotoGallery.PhotoEntryList;
import ca.mohawk.HealthMetrics.Prescription.DeletePrescriptionDialog;
import ca.mohawk.HealthMetrics.Prescription.PrescriptionListFragment;
import ca.mohawk.HealthMetrics.UserProfile.CreateUserFragment;
import ca.mohawk.HealthMetrics.UserProfile.ViewUserFragment;

/**
 * The MainActivity extends the AppCompactActivity.
 * <p>
 * Functions as the main activity for the application.
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        DeleteMetricDialog.DeleteMetricDialogListener,
        DeleteDataEntryDialog.DeleteDataEntryDialogListener,
        DeletePrescriptionDialog.DeletePrescriptionDialogListener,
        RemoveMetricDialog.RemoveMetricDialogListener,
        DeleteNoteDialog.DeleteNoteDialogListener,
        DeletePhotoEntryDialog.DeletePhotoEntryDialogListener,
        DeleteGalleryDialog.DeleteGalleryDialogListener,
        DeleteNotificationDialog.DeleteNotificationDialogListener {

    // Instantiate the healthMetricsDbHelper.
    HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the fragment manager.
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the database handler.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(this);

        // Determine if the user has created a profile based on shared preferences.
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean loggedIn = userInfo.getBoolean("loggedIn", false);

        // Set up the navigation view.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set NavigationItemSelectedListener on the Navigation view.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Add the MetricsListFragment is tne user has created a profile.
        if (loggedIn) {
            MetricsListFragment metricsListFragment = new MetricsListFragment();
            switchFragment(metricsListFragment);
            // Add the CreateUserFragment is tne user has created a profile.
        } else {
            CreateUserFragment createUserFragment = new CreateUserFragment();
            switchFragment(createUserFragment);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Replaces the fragment based on the selected menu item.
     *
     * @param item Represents the selected menu item.
     * @return Return true.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Switch statement replaces the fragment based on the selected menu item.
        switch (id) {
            case R.id.nav_profile:
                ViewUserFragment viewUserFragment = new ViewUserFragment();
                switchFragment(viewUserFragment);
                break;
            case R.id.nav_metrics:
                MetricsListFragment metricsListFragment = new MetricsListFragment();
                switchFragment(metricsListFragment);

                break;
            case R.id.nav_notifications:
                NotificationListFragment notificationListFragment = new NotificationListFragment();
                switchFragment(notificationListFragment);

                break;
            case R.id.nav_prescriptions:
                PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();
                switchFragment(prescriptionListFragment);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Replaces the current fragment with the passed in fragment.
     *
     * @param fragment Represents the fragment to replace with.
     */
    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Responds to a positive click in the DeleteMetricDialog.
     *
     * @param dialog Represents the DeleteMetricDialog.
     */
    @Override
    public void onDeleteMetricDialogPositiveClick(DeleteMetricDialog dialog) {

        // Delete the metric and inform the user if it was successful
        if (healthMetricsDbHelper.deleteMetric(dialog.getMetricId())) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(new MetricsListFragment());
    }

    /**
     * Responds to a negative click in the DeleteMetricDialog.
     *
     * @param dialog Represents the DeleteMetricDialog.
     */
    @Override
    public void onDeleteMetricDialogNegativeClick(DeleteMetricDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the DeleteDataEntryDialog.
     *
     * @param dialog Represents the DeleteDataEntryDialog.
     */
    @Override
    public void onDeleteDataEntryDialogPositiveClick(DeleteDataEntryDialog dialog) {

        // Prepare the dataEntryListFragment.
        DataEntryListFragment dataEntryListFragment = new DataEntryListFragment();

        Bundle metricBundle = new Bundle();
        metricBundle.putInt("selected_item_key", dialog.getMetricId());
        dataEntryListFragment.setArguments(metricBundle);

        // Delete the data entry and inform the user if it was successful
        if (healthMetricsDbHelper.deleteDataEntry(dialog.getDataEntryId())) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(dataEntryListFragment);
    }

    /**
     * Responds to a negative click in the DeleteDataEntryDialog.
     *
     * @param dialog Represents the DeleteDataEntryDialog.
     */
    @Override
    public void onDeleteDataEntryDialogNegativeClick(DeleteDataEntryDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the DeletePrescriptionDialog.
     *
     * @param dialog Represents the DeletePrescriptionDialog.
     */
    @Override
    public void onDeletePrescriptionDialogPositiveClick(DeletePrescriptionDialog dialog) {

        // Delete the prescription and inform the user if it was successful
        if (healthMetricsDbHelper.deletePrescription(dialog.getPrescriptionId())) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(new PrescriptionListFragment());
    }

    /**
     * Responds to a negative click in the DeletePrescriptionDialog.
     *
     * @param dialog Represents the DeletePrescriptionDialog.
     */
    @Override
    public void onDeletePrescriptionDialogNegativeClick(DeletePrescriptionDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the RemoveMetricDialog.
     *
     * @param dialog Represents the RemoveMetricDialog.
     */
    @Override
    public void onRemoveMetricDialogPositiveClick(RemoveMetricDialog dialog) {

        // Delete the data entries and remove the metric and inform the user if it was successful.
        if (healthMetricsDbHelper.deleteDataEntriesByMetricId(dialog.getMetricId()) &&
                healthMetricsDbHelper.removeMetricFromProfile(dialog.getMetricId())) {
            Toast.makeText(this, "Remove was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Remove was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(new MetricsListFragment());
    }

    /**
     * Responds to a negative click in the RemoveMetricDialog.
     *
     * @param dialog Represents the RemoveMetricDialog.
     */
    @Override
    public void onRemoveMetricDialogNegativeClick(RemoveMetricDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the DeleteNoteDialog.
     *
     * @param dialog Represents the DeleteNoteDialog.
     */
    @Override
    public void onDeleteNoteDialogPositiveClick(DeleteNoteDialog dialog) {

        // Delete the note and inform the user if it was successful.
        if (healthMetricsDbHelper.deleteNoteById(dialog.getNoteId())) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(new MetricsListFragment());
    }

    /**
     * Responds to a negative click in the DeleteNoteDialog.
     *
     * @param dialog Represents the DeleteNoteDialog.
     */
    @Override
    public void onDeleteNoteDialogNegativeClick(DeleteNoteDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the DeletePhotoEntryDialog.
     *
     * @param dialog Represents the DeletePhotoEntryDialog.
     */
    @Override
    public void onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog dialog) {

        // Initialize the photoEntryList.
        PhotoEntryList photoEntryList = new PhotoEntryList();

        // Pass the gallery id as a bundle to photoEntryList.
        Bundle photoEntryBundle = new Bundle();
        photoEntryBundle.putInt("selected_item_key", dialog.getGalleryId());
        photoEntryList.setArguments(photoEntryBundle);

        boolean deleteSuccessful = healthMetricsDbHelper.deletePhotoEntryById(dialog.getPhotoEntryId());

        // If the deletion was successful abd the photo is not from the gallery then delete the file.
        if (deleteSuccessful && dialog.getIsFromGallery() == 0) {
            File photoEntryFile = new File(dialog.getPhotoEntryPath());
            photoEntryFile.delete();
        }
        // Inform the user if it was successful.
        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(photoEntryList);
    }

    /**
     * Responds to a negative click in the DeletePhotoEntryDialog.
     *
     * @param dialog Represents the DeletePhotoEntryDialog.
     */
    @Override
    public void onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the DeleteGalleryDialog.
     *
     * @param dialog Represents the DeleteGalleryDialog.
     */
    @Override
    public void onDeleteGalleryDialogPositiveClick(DeleteGalleryDialog dialog) {

        // Delete the gallery and inform the user if it was successful.
        if (healthMetricsDbHelper.deleteGallery(dialog.getGalleryId())) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(new MetricsListFragment());
    }

    /**
     * Responds to a negative click in the DeleteGalleryDialog.
     *
     * @param dialog Represents the DeleteGalleryDialog.
     */
    @Override
    public void onDeleteGalleryDialogNegativeClick(DeleteGalleryDialog dialog) {
        dialog.dismiss();
    }

    /**
     * Responds to a positive click in the DeleteNotificationDialog.
     *
     * @param dialog Represents the DeleteNotificationDialog.
     */
    @Override
    public void onDeleteNotificationDialogPositiveClick(DeleteNotificationDialog dialog) {

        // Get the notification id.
        int id = dialog.getNotificationId();

        // Replicate the pending intent and cancel the alarm.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.cancel(pendingIntent);

        // Delete the notification and inform the user if it was successful
        if (healthMetricsDbHelper.deleteNotification(dialog.getNotificationId())) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
        switchFragment(new NotificationListFragment());
    }

    /**
     * Responds to a negative click in the DeleteNotificationDialog.
     *
     * @param dialog Represents the DeleteNotificationDialog.
     */
    @Override
    public void onDeleteNotificationDialogNegativeClick(DeleteNotificationDialog dialog) {
        dialog.dismiss();
    }
}