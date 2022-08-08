package com.phudt7.movie.activity;


import static com.phudt7.movie.Const.Const.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phudt7.movie.R;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.vm.FavouritesViewModel;

public class MainActivity extends AppCompatActivity {
    TextView tv_name, tv_email, tv_date, tv_sex;
    Button btn_edit, btn_show_all;
    SharedPreferences sharedPreferences;
    ImageView img_profile;
    private DrawerLayout mDrawerLayout;
    FavouritesViewModel viewModel;
    BottomNavigationView bottomNavigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        sharedPreferences = getSharedPreferences(MyPreference, MODE_PRIVATE);
        String name = sharedPreferences.getString(Name, "");
        String email = sharedPreferences.getString(Email, "");
        String date = sharedPreferences.getString(DATE_PICKER, "");
        String image = sharedPreferences.getString(IMAGE, "");
        String sex = sharedPreferences.getString(SEX, "");
        tv_name.setText(name);
        tv_email.setText(email);
        tv_date.setText(date);
        tv_sex.setText(sex);
        if (!image.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            img_profile.setImageBitmap(bitmap);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        navController = Navigation.findNavController(this, R.id.nav_host);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        countFavourites();
        editProfile();
        showAllReminder();

    }

    private void showAllReminder() {
        btn_show_all.setOnClickListener(v -> {
            bottomNavigationView.setSelectedItemId(R.id.fragmentSettings);
            navController.navigate(R.id.fragmentReminder);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    private void countFavourites() {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.fragmentFavourites);
        badgeDrawable.setBackgroundColor(Color.RED);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setNumber(MoviesDatabase.getInstance(this).moviesDAO().coutMovie());
        badgeDrawable.setVisible(true);
        viewModel.getCount().observe(this, integer -> badgeDrawable.setNumber(integer));
    }

    private void editProfile() {
        btn_edit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivityForResult(intent, MY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = sharedPreferences.getString(Name, "");
        String email = sharedPreferences.getString(Email, "");
        String date = sharedPreferences.getString(DATE_PICKER, "");
        String sex = sharedPreferences.getString(SEX, "");
        String image = sharedPreferences.getString(IMAGE, "");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                tv_name.setText(name);
                tv_email.setText(email);
                tv_date.setText(date);
                if (sex == MALE) {
                    tv_sex.setText(MALE);
                } else {
                    tv_sex.setText(FEMALE);
                }

                if (!image.equalsIgnoreCase("")) {
                    byte[] b = Base64.decode(image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    img_profile.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        btn_edit = findViewById(R.id.btn_edit);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_date = findViewById(R.id.tv_date);
        tv_sex = findViewById(R.id.tv_sex);
        img_profile = findViewById(R.id.img_profile);
        bottomNavigationView = findViewById(R.id.navView);
        btn_show_all = findViewById(R.id.btn_show_all);
    }
}
