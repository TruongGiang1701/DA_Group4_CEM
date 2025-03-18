package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private Button btnMngExpenses, btnMngBudgets, btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvWelcome = findViewById(R.id.ah_tv_welcome);
        btnMngExpenses = findViewById(R.id.ah_btn_mng_expenses);
        btnMngBudgets = findViewById(R.id.ah_btn_mng_budgets);
        btnLogout = findViewById(R.id.ah_btn_logout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "currentUser UID: " + currentUser.getUid());

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        else {
            db.collection("admins").document(currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        Log.d(TAG, "onComplete called");
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, "document: " + document);
                            if(document != null) {
                                Log.d(TAG, "document.exists(): " + document.exists());

                                String role = "Student";
                                if (document.exists()) {
                                    role = "Admin";
                                }
                                tvWelcome.setText("Welcome, " + currentUser.getEmail() + "! (Role: " + role + ")");

                            } else {
                                Log.d(TAG,"Document is null");
                                tvWelcome.setText("Welcome, " + currentUser.getEmail() + "!");
                            }

                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                            tvWelcome.setText("Welcome, " + currentUser.getEmail() + "!");
                        }
                    });

        }

        btnMngExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(HomeActivity.this, ManageExpensesActivity.class);
                //  startActivity(intent);
                Toast.makeText(HomeActivity.this, "This functionality is not yet implemented.", Toast.LENGTH_SHORT).show();            }
        });

        btnMngBudgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(HomeActivity.this, ManageBudgetsActivity.class);
                //  startActivity(intent);
                Toast.makeText(HomeActivity.this, "This functionality is not yet implemented.", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}