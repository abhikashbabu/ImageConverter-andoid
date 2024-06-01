package com.github.imageconverter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    private static final  int PICK_IMAGE= 1;
    private  static final int REQUEST_CODE =100;

    private ImageView imageView;
    private Bitmap selectedBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button buttonUpload = findViewById(R.id.buttonUpload);
        Button buttonConvert = findViewById(R.id.buttonConvert);
        Spinner spinnerFormat = findViewById(R.id.spinnerFormat);
         // set up the spinner with image formats

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.images_formats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormat.setAdapter(adapter);

        //check permission for storage

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedBitmap!=null) {
                    String selectedFormat =
                            spinnerFormat.getSelectedItem().toString();
                    convertAndSaveImage(selectedBitmap);
                }
                else{
                    Toast.makeText(MainActivity.this,"NoImage Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }



    private void openGallery(){
        Intent intent = new Intent((Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null){
            Uri imageUri =data.getData();

            try{
                selectedBitmap =MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                imageView.setImageBitmap(selectedBitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    private void convertAndSaveImage(Bitmap bitmap String format) {
        File myDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            myDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        } else {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            myDir = new File(root + "/ConvertedImages");
        }

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fileName = "Image-" + System.currentTimeMillis() + "." + format.toLowerCase();
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();

        try (FileOutputStream out = new FileOutputStream(file)) {
            switch (format) {
                case "PNG":
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    break;

            }catch IOException


        }
    }





}
