package com.example.x.lekcja12;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_PERMISSION = 101;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    Button przycisk_aparat;
    Button przycisk_kamera;
    String plikImage;
    String plikVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int writePermissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED || writePermissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_PERMISSION);
            }
        przycisk_aparat = (Button) findViewById(R.id.przycisk_aparat);
        przycisk_kamera = (Button) findViewById(R.id.przycisk_kamera);
        OnClickListeners();
    }

    void zrobZdjecie() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File image = null;
            try {
                image = zapiszZdjecie();
            } catch (IOException ex) {}
            if (image != null) {
                Uri uriSavedImage = Uri.fromFile(image);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    void nagrajWideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File video = null;
            try {
                video = zapiszWideo();
            } catch (IOException ex) {}
            if (video != null) {
                Uri uriSavedVideo = Uri.fromFile(video);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    File zapiszZdjecie() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        plikImage = image.getAbsolutePath();
        return image;
    }

    File zapiszWideo() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(videoFileName, ".mp4", storageDir);
        plikVideo = video.getAbsolutePath();
        return video;
    }

    protected void OnClickListeners() {
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.przycisk_aparat:
                        zrobZdjecie();
                        break;
                    case R.id.przycisk_kamera:
                        nagrajWideo();
                        break;
                    default:
                        break;
                }
            }
        };
        przycisk_aparat.setOnClickListener(listener);
        przycisk_kamera.setOnClickListener(listener);
    }
}

