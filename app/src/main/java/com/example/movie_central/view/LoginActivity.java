package com.example.movie_central.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_central.R;
import com.example.movie_central.databinding.ActivityMainBinding;
import com.example.movie_central.databinding.LoginBinding;
import com.example.movie_central.viewmodel.AuthCallBack;
import com.example.movie_central.viewmodel.MovieViewModel;

public class LoginActivity extends AppCompatActivity {
    MovieViewModel viewModel;
    LoginBinding binding;

    private final AuthCallBack loginCallback = new AuthCallBack() {
        @Override
        public void onResult(boolean success, String message) {
            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                binding.lblWarning1.setText("Login failed: " + message);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set up view binding with MovieViewModel
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Direct the user to the register page
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code for when the login button is clicked
                String email = binding.username.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                viewModel.logUserIn(email, password, loginCallback);
            }
        });

    }

}


