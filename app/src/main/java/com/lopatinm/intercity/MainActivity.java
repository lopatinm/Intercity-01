package com.lopatinm.intercity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.lopatinm.intercity.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final String APP_PREFERENCES = "IntercitySettings";
    SharedPreferences sp;
    Menu nav_Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_registration, R.id.nav_registration_dispatcher, R.id.nav_registration_user, R.id.nav_registration_driver)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        nav_Menu = navigationView.getMenu();
        Intent intentMyIntentService = new Intent(this, IntercityIntentService.class);
        startService(intentMyIntentService);
        roleCheck();
        nav_Menu.findItem(R.id.nav_exit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("access_token", "");
                editor.apply();
                roleCheck();
                fragmentReplacer(R.id.nav_home);
                drawer.close();
                return false;
            }
        });
    }

    public void fragmentReplacer(int resId){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(resId);
    }

    public void roleCheck(){
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String isLogin = sp.getString("access_token", "");
        String role = sp.getString("role", "");
        if(!isLogin.equals("")){
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_Menu.findItem(R.id.nav_registration).setVisible(false);
            if(role.equals("user")) {
                nav_Menu.findItem(R.id.nav_order).setVisible(true);
            }
            if(role.equals("moderator")) {
                nav_Menu.findItem(R.id.nav_profile_dispatcherprofile).setVisible(true);
            }
            nav_Menu.findItem(R.id.nav_exit).setVisible(true);
        }else{
            nav_Menu.findItem(R.id.nav_order).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile_dispatcherprofile).setVisible(false);
            nav_Menu.findItem(R.id.nav_exit).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
            nav_Menu.findItem(R.id.nav_registration).setVisible(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }





}