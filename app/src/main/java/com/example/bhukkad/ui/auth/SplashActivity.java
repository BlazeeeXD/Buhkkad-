package com.example.bhukkad.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.R;
import com.example.bhukkad.StaffDashboardActivity;
import com.example.bhukkad.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            getSplashScreen().setOnExitAnimationListener(splashScreenView -> splashScreenView.remove());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> checkUserAndRoute(), SPLASH_TIME_OUT);
    }

    private void checkUserAndRoute() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User is logged in, check their role in Firestore
            FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            String role = document.getString("role");

                            if ("staff".equals(role)) {
                                startActivity(new Intent(SplashActivity.this, StaffDashboardActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            }
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    });
        } else {
            // No user logged in
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
}