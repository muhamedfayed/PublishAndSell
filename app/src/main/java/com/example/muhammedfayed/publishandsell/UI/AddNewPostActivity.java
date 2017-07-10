package com.example.muhammedfayed.publishandsell.UI;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.models.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddNewPostActivity extends AppCompatActivity {

    ImageView imageView;
    Button uploadBtn, addBtn;
    EditText title, phone, description;
    Uri imageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        setTitle(getText(R.string.add_new));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getText(R.string.loading));
        progressDialog.setCancelable(false);
        imageView = (ImageView) findViewById(R.id.imageview);
        uploadBtn = (Button) findViewById(R.id.upload_btn);
        addBtn = (Button) findViewById(R.id.publish_btn);
        title = (EditText) findViewById(R.id.title_edittext);
        phone = (EditText) findViewById(R.id.phone_edittext);
        description = (EditText) findViewById(R.id.desc_edittext);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST);

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titleString = title.getText().toString().trim();
                final String phoneString = phone.getText().toString().trim();
                final String desString = description.getText().toString().trim();
                final Post post = new Post();
                if (titleString.equals("") || phoneString.equals("") || desString.equals("") || imageUri == null) {
                    Toast.makeText(AddNewPostActivity.this, getText(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    StorageReference filepath = storageRef.child("Images").child(imageUri.getLastPathSegment());
                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();


                            post.setTitle(titleString);
                            post.setPhone(phoneString);
                            post.setDes(desString);
                            post.setImageUrl(String.valueOf(downloadUrl));
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            post.setUsername(sharedPreferences.getString("username", "No name"));

                            databaseRef.push().setValue(post, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddNewPostActivity.this, getText(R.string.error), Toast.LENGTH_SHORT).show();
                                        System.out.println("Data could not be saved " + databaseError.getMessage());
                                    } else {
                                        progressDialog.dismiss();
                                        System.out.println("Data saved successfully.");
                                        finish();
                                    }
                                }
                            });

                        }
                    });
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                imageUri = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
