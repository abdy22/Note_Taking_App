/*
Author: Abdirahman Hassan
Description: The activity lets user extract text from image.
             The tool for text extraction is firebase ML KIT text recognition API.
*/



package com.example.inote.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inote.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.List;

public class extractTextActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView extractedtext;
    private ImageView capturedImage,takeApicture;
    private Button extractionButton,extractionDone,extractionCancel;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_text);
        extractedtext=findViewById(R.id.extractedTextView);
        capturedImage=findViewById(R.id.resultImage);
        takeApicture=findViewById(R.id.takeApicture);
        extractionButton=findViewById(R.id.buttonExtraction);
        extractionDone=findViewById(R.id.extractionDone);
        extractionCancel=findViewById(R.id.extractionCancel);
        takeApicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        extractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTextFromPic();
            }
        });

    }

    private void getTextFromPic() {
        FirebaseVisionImage visionImage=FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer recognizer=FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        recognizer.processImage(visionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                List<FirebaseVisionText.TextBlock> blocks=firebaseVisionText.getTextBlocks();
                if(blocks.size()==0) Toast.makeText(getApplicationContext(),"No text found",Toast.LENGTH_SHORT).show();
                else{
                    StringBuffer buffer=new StringBuffer();
                    for(FirebaseVisionText.TextBlock block:firebaseVisionText.getTextBlocks()){
                            String blockText = block.getText();
                            buffer.append(blockText);
                            Float blockConfidence = block.getConfidence();
                            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                            Point[] blockCornerPoints = block.getCornerPoints();
                            Rect blockFrame = block.getBoundingBox();
                            for (FirebaseVisionText.Line line : block.getLines()) {
                                String lineText = line.getText();
                                buffer.append(lineText);
                                Float lineConfidence = line.getConfidence();
                                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                                Point[] lineCornerPoints = line.getCornerPoints();
                                Rect lineFrame = line.getBoundingBox();
                                for (FirebaseVisionText.Element element : line.getElements()) {
                                    String elementText = element.getText();
                                    buffer.append(elementText);
                                    Float elementConfidence = element.getConfidence();
                                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                                    Point[] elementCornerPoints = element.getCornerPoints();
                                }
                            }
                    }
                    extractedtext.setText(buffer.toString());
                }




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(imageBitmap);
        }
    }
    public void doneExtracting(View view ){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("X",extractedtext.getText().toString());
        setResult(RESULT_OK, returnIntent);
        finish();

    }
    public void cancelExtracting(View view){
        finish();

    }


}