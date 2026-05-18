package com.example.bhukkad.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.R;
import com.example.bhukkad.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvName = findViewById(R.id.tv_name);
        TextView tvDetails = findViewById(R.id.tv_user_details);

        // Load user data from Firestore
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            String name = doc.getString("name");
                            String email = doc.getString("email");
                            String phone = doc.getString("phone");

                            tvName.setText(name != null ? name : "User");
                            tvDetails.setText(
                                    (email != null ? email : "") +
                                            (phone != null ? "\n" + phone : "")
                            );
                        } else {
                            // Fallback to Firebase Auth data
                            tvName.setText(user.getEmail() != null
                                    ? user.getEmail().split("@")[0]
                                    : "User");
                            tvDetails.setText(user.getEmail());
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Fallback to Firebase Auth data
                        tvName.setText(user.getEmail() != null
                                ? user.getEmail().split("@")[0]
                                : "User");
                        tvDetails.setText(user.getEmail());
                    });
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.item_order_history).setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, OrdersActivity.class))
        );

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}