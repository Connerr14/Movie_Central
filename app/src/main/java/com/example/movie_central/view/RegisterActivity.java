package com.example.movie_central.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_central.R;
import com.example.movie_central.databinding.ActivityMainBinding;
import com.example.movie_central.databinding.RegisterBinding;
import com.example.movie_central.viewmodel.MovieViewModel;

public class RegisterActivity extends AppCompatActivity {
    MovieViewModel viewModel;
    RegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set up view binding with MovieViewModel
        binding = RegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text input from the text edit buttons
                String username = binding.username.getText().toString().trim();
                String password = binding.password.getText().toString().trim();


                if (username.isEmpty() || password.isEmpty()) {
                    binding.lblWarning.setText("Please fill out all fields");
                }
                else {
                    binding.lblWarning.setText(""); // clear old message
                    viewModel.addUser(username, password, null);

                    // Direct the user to the login page
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }
}