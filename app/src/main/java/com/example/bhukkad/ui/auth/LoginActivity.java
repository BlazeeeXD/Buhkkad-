package com.example.bhukkad.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.StaffDashboardActivity;
import com.example.bhukkad.databinding.ActivityLoginBinding;
import com.example.bhukkad.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Style "Create account"
        String fullText = "Don't have an account? Create account";
        SpannableString spannable = new SpannableString(fullText);
        int start = fullText.indexOf("Create account");
        spannable.setSpan(
                new ForegroundColorSpan(0xFFD38E70),
                start, fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        binding.tvSignupPrompt.setText(spannable);

        binding.btnLogin.setOnClickListener(v -> loginUser());

        binding.tvSignupPrompt.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        // The Staff Login Backdoor has been REMOVED.
        // Everyone must log in through the main button now.

        binding.tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Password reset coming soon", Toast.LENGTH_SHORT).show()
        );
    }

    private void loginUser() {
        String emailOrId = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (emailOrId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format ID to dummy email if no '@' is present (This turns "staff" into "staff@student.bmu.edu.in")
        String finalEmail = emailOrId.contains("@") ? emailOrId : emailOrId + "@student.bmu.edu.in";

        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(finalEmail, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Check role in Firestore to route correctly
                        db.collection("users").document(userId).get()
                                .addOnCompleteListener(roleTask -> {
                                    if (roleTask.isSuccessful() && roleTask.getResult() != null) {
                                        String role = roleTask.getResult().getString("role");

                                        if ("staff".equals(role)) {
                                            startActivity(new Intent(LoginActivity.this, StaffDashboardActivity.class));
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        }
                                        finish();
                                    } else {
                                        // Default fallback
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed. Check credentials.", Toast.LENGTH_LONG).show();
                        binding.btnLogin.setEnabled(true);
                        binding.btnLogin.setText("Login");
                    }
                });
    }
}