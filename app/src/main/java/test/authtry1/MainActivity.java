package test.authtry1;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText eMail;
    EditText passWord;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        eMail = (EditText) findViewById(R.id.Email);
        passWord = (EditText) findViewById(R.id.Password);
        progressBar = (ProgressBar)findViewById(R.id.ProgressBar);

        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.signUp:
                finish();
                startActivity(new Intent(this,SignupActivity.class));
                break;
            case R.id.loginButton:
                userLogin();
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,HomeActivity.class));

        }
    }

    private void userLogin() {

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
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"User Logged In Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
