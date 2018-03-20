package test.authtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button profile;
    Button orders;
    Button menu;


    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        profile = (Button) findViewById(R.id.buttonProfile);
        orders = (Button) findViewById(R.id.buttonOrders);
        menu = (Button) findViewById(R.id.buttonMenu);

        profile.setOnClickListener(this);
        orders.setOnClickListener(this);
        menu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.buttonProfile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.buttonOrders:
                Toast.makeText(this, "This service will be made available shortly", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonMenu:
                Toast.makeText(this, "This service will be made available shortly", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,MainActivity.class));

        }
    }
}
