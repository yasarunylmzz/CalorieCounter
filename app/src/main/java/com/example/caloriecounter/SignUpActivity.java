package com.example.caloriecounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText signupEmail, signupPass;
    private Button signupbtn;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.email);
        signupPass = findViewById(R.id.pass);
        signupbtn = findViewById(R.id.signUpBtn);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPass.getText().toString().trim();

                if(user.isEmpty()){
                    signupEmail.setError("Email boş bırakılamaz.");

                }

                if (pass.isEmpty()){
                    signupPass.setError("Şifre Boş Bırakılamaz.");
                }
                else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"Kayıt Oldunuz.",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));

                            }else {
                                Toast.makeText(SignUpActivity.this,"Kayıt Olunmadı Hata." + task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });


    }
}