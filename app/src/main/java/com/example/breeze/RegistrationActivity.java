package com.example.breeze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvHaveAnAcc;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initializeViews();
        setupOnClickListeners();

    }

    private void setupOnClickListeners() {
        tvHaveAnAcc.setOnClickListener(v -> {
            sendToLoginActivity();
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

                    String userID = mAuth.getCurrentUser().getUid();
                    databaseReference.child("Users").child(userID).setValue("");
                    sendToMainActivity();
                    Toast.makeText(RegistrationActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    displayRegistrationAlerter();

                }
                else {
                    Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void displayRegistrationAlerter(){
        Alerter.create(this).setTitle("Welcome!").setText("You have successfully registered to Breeze!")
                .setDuration(3000).setBackgroundResource(R.drawable.border).enableSwipeToDismiss().setIcon(R.drawable.breezelogo)
                .setEnterAnimation(R.anim.alerter_slide_in_from_bottom).setExitAnimation(R.anim.alerter_slide_out_to_top).show();
    }


    private void initializeViews() {
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvHaveAnAcc = findViewById(R.id.tvAlreadyHaveAnAccount);
        progressDialog = new ProgressDialog(RegistrationActivity.this);
    }

    private void sendToMainActivity() {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void sendToLoginActivity() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}