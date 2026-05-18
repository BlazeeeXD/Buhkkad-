package com.example.bhukkad.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.databinding.ActivitySignupBinding;
import com.example.bhukkad.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Style "Sign in" part in the prompt
        String fullText = "Already have an account? Sign in";
        SpannableString spannable = new SpannableString(fullText);
        int start = fullText.indexOf("Sign in");
        spannable.setSpan(
                new ForegroundColorSpan(0xFFD38E70),
                start, fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        binding.tvSigninPrompt.setText(spannable);

        binding.btnSignup.setOnClickListener(v -> registerUser());

        binding.tvSigninPrompt.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        String emailOrId = binding.etEmail.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || emailOrId.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String finalEmail = emailOrId.contains("@") ? emailOrId : emailOrId + "@student.bmu.edu.in";

        binding.btnSignup.setEnabled(false);
        binding.btnSignup.setText("Creating Account...");

        mAuth.createUserWithEmailAndPassword(finalEmail, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        Map<String, Object> user = new HashMap<>();
                        user.put("uid", userId);
                        user.put("name", username);
                        user.put("email", finalEmail);
                        user.put("student_id", emailOrId.contains("@") ? "" : emailOrId);

                        // Strict enforcement: Everyone who signs up is a customer
                        user.put("role", "customer");

                        db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignupActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SignupActivity.this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    binding.btnSignup.setEnabled(true);
                                    binding.btnSignup.setText("Sign Up");
                                });
                    } else {
                        Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        binding.btnSignup.setEnabled(true);
                        binding.btnSignup.setText("Sign Up");
                    }
                });
    }
}