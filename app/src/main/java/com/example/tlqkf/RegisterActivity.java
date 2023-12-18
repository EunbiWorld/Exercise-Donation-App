package com.example.tlqkf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.join_name);
        editTextEmail = findViewById(R.id.join_email);
        editTextPassword = findViewById(R.id.join_password);

        buttonJoin = findViewById(R.id.join1_button);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")){
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    createUser(editTextEmail.getText().toString(),editTextPassword.getText().toString(),editTextName.getText().toString());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(RegisterActivity.this, "계정과 비밀번호를 입력하세요.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String name){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    // 회원가입 성공시
                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    // 계정이 중복된 경우
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}