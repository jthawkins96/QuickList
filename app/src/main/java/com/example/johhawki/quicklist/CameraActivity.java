package com.example.johhawki.quicklist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class CameraActivity extends AppCompatActivity {
    private Button btn;
    private EditText errorMSG;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int IMAGE_CAPTURE = 102;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btn = (Button) findViewById(R.id.cambtn);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
        }
    }

    public void onCamClick(View view) {
        Intent myInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutMedieFileUri(MEDIA_TYPE_IMAGE);
        errorMSG.setText(errorMSG.getText() + "\nfileURI: " + fileUri.getPath());
        myInt.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(myInt, IMAGE_CAPTURE);
    }

    private static File getOutputMediaFile(int type) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Quicklist");
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                Log.d("Quicklist", "failed to create directory");
                return null;
            }
        }

        String myTimeStamp = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        File myMediaFile;
        if(type==MEDIA_TYPE_IMAGE) {
            myMediaFile = new File(myDir.getPath() + File.separator + "IMG_" + myTimeStamp + ".jpg");
        }
        else {
            return null;
        }
        return myMediaFile;
    }

    private static Uri getOutMedieFileUri(int type){
        Uri URI = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() +".provider",getOutputMediaFile(type));
        return URI;
    }

}

