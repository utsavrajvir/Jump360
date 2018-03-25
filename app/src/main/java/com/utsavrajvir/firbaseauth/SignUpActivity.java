package com.utsavrajvir.firbaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail,editTextPassword,editTextcPassword;
    ProgressBar progress;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Sign Up");
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextcPassword = (EditText) findViewById(R.id.cpassword);
        progress = (ProgressBar)findViewById(R.id.progress);


        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }


    //Method to check Validation,add user and send email for verification
    private void registerUser()
    {
        String email,password,cpassword;

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        cpassword = editTextcPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please Enter Valid Email Address");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6 )
        {
            editTextPassword.setError("Password Length Should be greater then 6");
            editTextPassword.requestFocus();
            return;
        }

        if(!cpassword.equals(password))
        {
            editTextcPassword.setError("Password Doesn't Match");
            editTextcPassword.requestFocus();
            return;
        }



        progress.setVisibility(View.VISIBLE);

        //Create user
    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            progress.setVisibility(View.GONE);

            if(task.isSuccessful())
            {
                finish();

                user = mAuth.getCurrentUser();
                user = task.getResult().getUser();

                //Send Verification Mail
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        Toast.makeText(SignUpActivity.this, "Email Verification Sent", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                });

            }
            else
            {
                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
            }


        }
    });

}

    @Override
    public void onClick(View v) {


        //code to logout. This is beacuse it gives every EventSource to get Handle from class file itself.
        switch(v.getId())
        {
            case R.id.btnSignUp:
                registerUser();
                break;

            case R.id.textViewLogin :
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }
}
