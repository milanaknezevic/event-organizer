package com.example.eventorganizer;

import static com.example.eventorganizer.model.util.NotificationHelper.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.eventorganizer.databinding.ActivityMainBinding;
import com.example.eventorganizer.enums.Notification;
import com.example.eventorganizer.model.entities.Event;
import com.example.eventorganizer.model.util.DBHelper;
import com.example.eventorganizer.model.util.NotificationHelper;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static DBHelper dbHelper;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
       /* dbHelper.deleteEvents();
        dbHelper.deleteCategories();
*/
        //dbHelper.deleteItems();
        setLocale(this);


        notificationHelper = new NotificationHelper(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Notification option = null;
        String notificationPreference = sharedPreferences.getString("notification", "2");
        switch (notificationPreference) {
            case "0":
                option = Notification.OFF;
                break;
            case "1":
                option = Notification.HOUR_BEFORE;
                break;
            case "2":
                option = Notification.DAY_BEFORE;
                break;
            case "3":
                option = Notification.WEEK_BEFORE;
                break;
            default:
                option = Notification.OFF;
        }

        if (option != Notification.OFF) {
            if (hasNotificationPermission()) {
                sendNotification(option);
            } else {
                requestNotificationPermission();
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_calendar, R.id.nav_settings, R.id.nav_events)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean hasNotificationPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        );
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    private void sendNotification(Notification option) {
        List<Event> upcomingActivities = getUpcomingActivities(option);
        for (Event event : upcomingActivities) {
            String name = event.getName();
            String time = String.valueOf(event.getTime());
            notificationHelper.showNotification(name, time);
        }
    }

    private List<Event> getUpcomingActivities(Notification option) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        Date startDate = Calendar.getInstance().getTime();

        switch (option) {
            case HOUR_BEFORE:
                calendar.add(Calendar.HOUR, 1);
                break;
            case DAY_BEFORE:
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case WEEK_BEFORE:
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                break;
            default:
                break;
        }

        Date endDate = calendar.getTime();
        List<Event> upcomingActivities = MainActivity.dbHelper.getUpcomingEvents(
                format.format(startDate),
                format.format(endDate)
        );

        return upcomingActivities;
    }

    private void setLocale(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPreferences.getString(
                "language",
                context.getResources().getString(R.string.default_language)
        );
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private void requestNotificationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                PERMISSION_REQUEST_CODE
        );
    }

}