package com.example.anupam.logix1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScanDocument extends AppCompatActivity {


    ImageView image;
    Button capture,detect;
    TextView readText;
    Bitmap imageBitmap;
    int flag =0;
    Uri selectedImage;
    DatabaseReference healthbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_document);
        image= (ImageView)findViewById(R.id.imgview);
        capture = (Button)findViewById(R.id.capture_image);
        detect = (Button)findViewById(R.id.detect_text);
        readText = (TextView)findViewById(R.id.text);
        healthbase = FirebaseDatabase.getInstance().getReference("Drivers");



        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                readText.setText("");

            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTextFromImage();
            }
        });




    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage;
        try {
         firebaseVisionImage = FirebaseVisionImage.fromFilePath(ScanDocument.this.getApplicationContext(),selectedImage);

        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
            displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanDocument.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if(blockList.size() ==0)
        {
            Toast.makeText(ScanDocument.this,"No Text FOund",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String mtext ="";

            for(FirebaseVisionText.TextBlock block :firebaseVisionText.getTextBlocks())
            {
                String text = block.getText();
                mtext +=text;
                //if(text.toLowerCase().contains("negative")) {
                    readText.setText(text);


            }
            if(mtext.toLowerCase().contains("negative"))
            {
                flag=0;
                readText.setText("Status: Negative");


            }
            else
            {
                flag=1;
                readText.setText("Status: Positive");
            }

            healthbase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for(DataSnapshot csnapshot: dataSnapshot.getChildren())
                    {

                        Drivers d1 = csnapshot.getValue(Drivers.class);
                        Log.e("Header",d1.getName());
                        if(d1.getName().equals(((GlobalVariables)ScanDocument.this.getApplication()).getEmail()))
                        {
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c);
                            d1.setLastUpdated(formattedDate);
                            d1.setSick(flag == 1? "true":"false");
                           healthbase.child(csnapshot.getKey()).setValue(d1);
                            break;
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;

    private void dispatchTakePictureIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            image.setImageURI(selectedImage);
        }
    }
}
