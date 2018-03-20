package test.authtry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{


    EditText eMail;
    EditText passWord;
    Button registerButton;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        eMail = (EditText)findViewById(R.id.eMail);
        passWord = (EditText)findViewById(R.id.passWord);
        registerButton = (Button)findViewById(R.id.registerButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.login).setOnClickListener(this);
        registerButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = eMail.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        if(email.isEmpty()){
            eMail.setError("Email is required");
            eMail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            eMail.setError("Email is invalid");
            eMail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passWord.setError("Password is required");
            passWord.requestFocus();
            return;
        }

        if(password.length()<6){
            passWord.setError("Minimum length of password is 6");
            passWord.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User alreagy registered",Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

