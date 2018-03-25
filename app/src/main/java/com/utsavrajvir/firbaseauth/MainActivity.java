package com.utsavrajvir.firbaseauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    EditText editTextEmail,editTextPassword;//EditText reference Variable
    ProgressBar progress; // ProgressBar reference Variable

    private FirebaseAuth mAuth;//Variable to get Firebase Instance
    FirebaseUser user;//Variable to get User Information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Assigning values of TextField to variable
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword= (EditText) findViewById(R.id.password);
        progress = (ProgressBar)findViewById(R.id.progress);
        this.setTitle("Login");
        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        //Instanciate variable
        mAuth = FirebaseAuth.getInstance();

        //Set Title of Activity
        this.setTitle("Login");


        //To check wheather user is Already Logged In or not
        if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified())
        {
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }

    }


    //Method to check Validation,add user and send email for verification
    private void login()
    {
        String email,password;

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();


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

        progress.setVisibility(View.VISIBLE);
        user = mAuth.getCurrentUser();

        //Let User Log In
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    progress.setVisibility(View.GONE);

                    if(task.isSuccessful())
                    {
                        finish();
                        Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(this, ProfileActivity.class));
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }


                }
            });




    }

    @Override
    public void onClick(View v) {

        //User can Define its choice, accordingly it will work
        switch(v.getId())
        {
            case R.id.textViewSignUp :
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.btnLogin :
                login();
                break;
        }


    }
}
