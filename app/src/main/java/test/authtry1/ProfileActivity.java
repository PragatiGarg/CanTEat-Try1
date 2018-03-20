package test.authtry1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    EditText editText;
    Uri profileImage;
    ProgressBar progressBar;
    String downloadUrl;
    TextView textView;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        editText = (EditText) findViewById(R.id.displayName);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBarImage);
        textView = (TextView) findViewById(R.id.emailVerify);


        imageView.setOnClickListener(this);
        findViewById(R.id.saveButton).setOnClickListener(this);
        findViewById(R.id.signOut).setOnClickListener(this);

        loadUserInformation();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,MainActivity.class));

        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){
            if(user.getPhotoUrl()!=null){
                String photoUri = user.getPhotoUrl().toString();
                Glide.with(this)
                        .load(photoUri)
                        .into(imageView);
            }
            if(user.getDisplayName()!=null){
                String displayName = user.getDisplayName();
                editText.setText(displayName);
            }
            if(user.isEmailVerified()){
                textView.setText("Email Verified");
            } else{
                textView.setText("Email Not Verified (CLick to verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this,"Verifiaction Email Sent",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

        }




    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imageView:
                showImageChooser();
                break;
            case R.id.saveButton:
                saveUserInformation();
                break;
            case R.id.signOut:
                signOutUser();
                break;

        }
    }

    private void signOutUser() {

        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    private void saveUserInformation() {
        String displayName = editText.getText().toString().trim();
        if(displayName.isEmpty()){
            editText.setError("Name Required");
            editText.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null && downloadUrl!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(downloadUrl))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(ProfileActivity.this, "Some Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            profileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),profileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileRef = FirebaseStorage.getInstance().getReference("profilePics/"+System.currentTimeMillis()+".jpg");
        if(profileImage!=null){
            progressBar.setVisibility(View.VISIBLE);

            profileRef.putFile(profileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    downloadUrl = taskSnapshot.getDownloadUrl().toString();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"),CHOOSE_IMAGE);
    }
}
