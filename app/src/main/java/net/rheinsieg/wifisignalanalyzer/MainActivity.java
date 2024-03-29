/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package net.rheinsieg.wifisignalanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.rheinsieg.util.ConfigurationUtils;
import net.rheinsieg.util.EnumUtils;

import net.rheinsieg.wifisignalanalyzer.R;

import net.rheinsieg.wifisignalanalyzer.navigation.NavigationMenu;
import net.rheinsieg.wifisignalanalyzer.navigation.NavigationMenuControl;
import net.rheinsieg.wifisignalanalyzer.navigation.NavigationMenuView;
import net.rheinsieg.wifisignalanalyzer.navigation.options.OptionMenu;
import net.rheinsieg.wifisignalanalyzer.settings.Repository;
import net.rheinsieg.wifisignalanalyzer.settings.Settings;
import net.rheinsieg.wifisignalanalyzer.wifi.accesspoint.ConnectionView;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiBand;
import net.rheinsieg.wifisignalanalyzer.wifi.band.WiFiChannel;

import java.util.Locale;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity
    implements OnSharedPreferenceChangeListener, OnNavigationItemSelectedListener, NavigationMenuControl {

    private MainReload mainReload;
    private DrawerNavigation drawerNavigation;
    private NavigationMenuView navigationMenuView;
    private NavigationMenu startNavigationMenu;
    private OptionMenu optionMenu;
    private String currentCountryCode;
    private PermissionChecker permissionChecker;

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale = new Settings(new Repository(newBase)).getLanguageLocale();
        Context context = ConfigurationUtils.createContext(newBase, newLocale);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainContext mainContext = MainContext.INSTANCE;
        mainContext.initialize(this, isLargeScreen());

        Settings settings = mainContext.getSettings();
        settings.initializeDefaultValues();

        setTheme(settings.getThemeStyle().getThemeNoActionBar());

        setWiFiChannelPairs(mainContext);

        mainReload = new MainReload(settings);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settings.registerOnSharedPreferenceChangeListener(this);

        setOptionMenu(new OptionMenu());

        ActivityUtils.keepScreenOn(this);

        Toolbar toolbar = ActivityUtils.setupToolbar(this);
        drawerNavigation = new DrawerNavigation(this, toolbar);

        startNavigationMenu = settings.getStartMenu();
        setNavigationMenuView(new NavigationMenuView(this, startNavigationMenu));
        onNavigationItemSelected(getCurrentMenuItem());

        ConnectionView connectionView = new ConnectionView(this);
        mainContext.getScannerService().register(connectionView);

        permissionChecker = new PermissionChecker(this);
        permissionChecker.check();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerNavigation.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerNavigation.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!permissionChecker.isGranted(requestCode, grantResults)) {
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setWiFiChannelPairs(MainContext mainContext) {
        Settings settings = mainContext.getSettings();
        String countryCode = settings.getCountryCode();
        if (!countryCode.equals(currentCountryCode)) {
            Pair<WiFiChannel, WiFiChannel> pair = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairFirst(countryCode);
            mainContext.getConfiguration().setWiFiChannelPair(pair);
            currentCountryCode = countryCode;
        }
    }

    private boolean isLargeScreen() {
        Configuration configuration = getResources().getConfiguration();
        int screenLayoutSize = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
            screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        MainContext mainContext = MainContext.INSTANCE;
        if (mainReload.shouldReload(mainContext.getSettings())) {
            reloadActivity();
        } else {
            ActivityUtils.keepScreenOn(this);
            setWiFiChannelPairs(mainContext);
            update();
        }
    }

    public void update() {
        MainContext.INSTANCE.getScannerService().update();
        updateActionBar();
    }

    private void reloadActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            if (startNavigationMenu.equals(getCurrentNavigationMenu())) {
                super.onBackPressed();
            } else {
                setCurrentNavigationMenu(startNavigationMenu);
                onNavigationItemSelected(getCurrentMenuItem());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        try {
            closeDrawer();
            NavigationMenu navigationMenu = EnumUtils.find(NavigationMenu.class, menuItem.getItemId(), NavigationMenu.ACCESS_POINTS);
            navigationMenu.activateNavigationMenu(this, menuItem);
        } catch (Exception e) {
            reloadActivity();
        }
        return true;
    }

    private boolean closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        MainContext.INSTANCE.getScannerService().pause();
        updateActionBar();
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            MainContext.INSTANCE.getScannerService().resume();
            updateActionBar();
        } catch (Exception e) {
            reloadActivity();
        }
    }

    @Override
    protected void onStop() {
        MainContext.INSTANCE.getScannerService().setWiFiOnExit();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionMenu.create(this, menu);
        updateActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        optionMenu.select(item);
        updateActionBar();
        return true;
    }

    public void updateActionBar() {
        getCurrentNavigationMenu().activateOptions(this);
    }

    public OptionMenu getOptionMenu() {
        return optionMenu;
    }

    void setOptionMenu(@NonNull OptionMenu optionMenu) {
        this.optionMenu = optionMenu;
    }

    @NonNull
    @Override
    public MenuItem getCurrentMenuItem() {
        return navigationMenuView.getCurrentMenuItem();
    }

    @NonNull
    @Override
    public NavigationMenu getCurrentNavigationMenu() {
        return navigationMenuView.getCurrentNavigationMenu();
    }

    @Override
    public void setCurrentNavigationMenu(@NonNull NavigationMenu navigationMenu) {
        navigationMenuView.setCurrentNavigationMenu(navigationMenu);
    }

    @NonNull
    @Override
    public NavigationView getNavigationView() {
        return navigationMenuView.getNavigationView();
    }

    public void mainConnectionVisibility(int visibility) {
        findViewById(R.id.main_connection).setVisibility(visibility);
    }

    public NavigationMenuView getNavigationMenuView() {
        return navigationMenuView;
    }

    void setNavigationMenuView(NavigationMenuView navigationMenuView) {
        this.navigationMenuView = navigationMenuView;
    }

    void setDrawerNavigation(DrawerNavigation drawerNavigation) {
        this.drawerNavigation = drawerNavigation;
    }
}
