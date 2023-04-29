package com.travel.journal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.travel.journal.notifications.NotificationHelper;
import com.travel.journal.sign_in.LoginActivity;
import com.travel.journal.trip.NewTripActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * The application home activity.
 *
 * @author Alex_Smarandache
 *
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * The floating action button of the application.
     */
    public static FloatingActionButton floatingActionButton;

    /**
     * The bar configurations.
     */
    private AppBarConfiguration appBarConfiguration;

    /**
     * The intent.
     */
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationHelper.initializeNotificationChannel(getSystemService(NotificationManager.class));
        }

        TextView navName;
        TextView navEmail;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), NewTripActivity.class);
            startActivity(i);
        });

        DrawerLayout drawer           = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView               = navigationView.getHeaderView(0);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,
                R.id.nav_about, R.id.nav_contact, R.id.nav_share).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navName  = headerView.findViewById(R.id.nav_name);
        navEmail = headerView.findViewById(R.id.nav_email);

        navName.setText(ApplicationController.getLoggedInUser().getName());
        navEmail.setText(ApplicationController.getLoggedInUser().getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    /**
     * The logout function.
     *
     * @param item The menu item.
     */
    public void logout(MenuItem item) {
        ApplicationController.setLoggedInUser(null, HomeActivity.this);
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Action to open the GitHubLink.
     *
     * @param view The view.
     */
    public void openGithubLink(View view) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.contact_github)));
        startActivity(intent);
    }

    /**
     * The action to open the email link.
     *
     * @param view The view.
     */
    public void openEmailLink(View view) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.contact_email)));
        startActivity(intent);
    }

    /**
     * The action to open the instagram link.
     *
     * @param view The view.
     */
    public void openInstagramLink(View view) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.contact_instagram)));
        startActivity(intent);
    }

    /**
     * Action to open the LinkedIn link.
     *
     * @param view The view.
     */
    public void openLinkedInLink(View view) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.contact_linkedin)));
        startActivity(intent);
    }

    /**
     * The share function.
     *
     * @param item The menu item.
     */
    public void share(MenuItem item) {
        intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        intent.setType("text/plain");

        startActivity(Intent.createChooser(intent, null));
    }

}