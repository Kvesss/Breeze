package com.example.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvHaveAnAcc;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        initializeViews();
        setupOnClickListeners();

    }

    private void setupOnClickListeners() {
        tvHaveAnAcc.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegistrationActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegistrationActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setTitle(getString(R.string.dialogTitleCreatingAcc));
            progressDialog.setMessage(getString(R.string.dialogPleaseWait));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    SendToLoginActivity();
                    Toast.makeText(RegistrationActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

    }


    private void initializeViews() {
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvHaveAnAcc = findViewById(R.id.tvAlreadyHaveAnAccount);
        progressDialog = new ProgressDialog(RegistrationActivity.this);
    }

    private void SendToMainActivity() {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private void SendToLoginActivity() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}