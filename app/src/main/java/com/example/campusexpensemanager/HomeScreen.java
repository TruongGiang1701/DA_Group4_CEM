package com.example.campusexpensemanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {
    private TextView roleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        roleTextView = findViewById(R.id.roleTextView);

        // Nhận role từ LoginActivity
        String role = getIntent().getStringExtra("user_role");

        if (role != null) {
            roleTextView.setText("Vai trò của bạn: " + role);
        } else {
            roleTextView.setText("Không xác định vai trò");
        }
    }
}