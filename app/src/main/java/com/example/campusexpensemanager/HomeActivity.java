package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private LinearLayout btnMngExpenses, btnMngBudgets, btnRecurringExpenses, btnReports, btnFeedback, btnLogout;

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
        btnRecurringExpenses = findViewById(R.id.ah_btn_re_expenses);
        btnReports = findViewById(R.id.ah_btn_report);
        btnFeedback = findViewById(R.id.ah_btn_feedback);
        btnLogout = findViewById(R.id.ah_btn_logout);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            fetchUserRole(currentUser);
        }

        btnMngExpenses.setOnClickListener(this::onManageExpensesClick);
        btnMngBudgets.setOnClickListener(this::onManageBudgetsClick);
        btnRecurringExpenses.setOnClickListener(this::onRecurringExpensesClick);
        btnReports.setOnClickListener(this::onReportClick);
        btnFeedback.setOnClickListener(this::onFeedbackClick);
        btnLogout.setOnClickListener(this::onLogoutClick);
    }

    private void fetchUserRole(FirebaseUser currentUser) {

        db.collection("roles").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String role = "Student";
                    if (documentSnapshot.exists()) {
                        role = documentSnapshot.getString("role");
                    }
                    fetchAndDisplayUserInfo(currentUser, role);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting user role", e);
                    tvWelcome.setText("Welcome, " + currentUser.getEmail() + "!");
                });
    }
    private void fetchAndDisplayUserInfo(FirebaseUser currentUser, String role) {

        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(userDoc -> {
                    String displayName = currentUser.getEmail();
                    if (userDoc.exists() && userDoc.getString("name") != null) {
                        displayName = userDoc.getString("name");
                    }

                    tvWelcome.setText("Welcome, " + displayName + "! (Role: " + role + ")");

                    if ("Student".equals(role)) {
                        btnFeedback.setVisibility(View.GONE);
                    } else {
                        btnFeedback.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting user info", e);
                    tvWelcome.setText("Welcome, " + currentUser.getEmail() + "! (Role: " + role +")");
                    if ("Student".equals(role)) {
                        btnFeedback.setVisibility(View.GONE);
                    } else {
                        btnFeedback.setVisibility(View.VISIBLE);
                    }
                });
    }
    public void onManageExpensesClick(View view) {
        startActivity(new Intent(this, ManageExpensesActivity.class));
    }

    public void onManageBudgetsClick(View view) {
        startActivity(new Intent(this, ManageBudgetsActivity.class));
    }

    public void onRecurringExpensesClick(View view) {
        startActivity(new Intent(this, RecurringExpensesActivity.class));
    }

    public void onReportClick(View view) {
        startActivity(new Intent(this, ReportActivity.class));
    }

    public void onFeedbackClick(View view) {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    public void onLogoutClick(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}