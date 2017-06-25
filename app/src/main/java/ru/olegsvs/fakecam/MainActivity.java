package ru.olegsvs.fakecam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareToSnapPicture();
        finish();
    }

    private void prepareToSnapPicture() {
        checkSdCard();
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            snapPicture(intent);
            setResult(RESULT_OK);
        } else {
            Log.i("tgmm", "Unable to capture photo. Missing Intent Extras.");
            setResult(RESULT_CANCELED);
        }
    }

    private void checkSdCard() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "External SD card not mounted", Toast.LENGTH_LONG).show();
            Log.i("tgmm", "External SD card not mounted");
        }
    }

    private void snapPicture(Intent intent) {
        try {
            this.copyFile(getPicturePath(intent));
//            Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.i("tgmm", "Can't copy photo");
            e.printStackTrace();
        }
    }

    private File getPicturePath(Intent intent) {
        Uri uri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
        return new File(uri.getPath());
    }

    private void copyFile(File destination) throws IOException {
        InputStream in = getResources().openRawResource(getPhoto());
        OutputStream out = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;

        if (in != null) {
            Log.i("tgmm", "Input photo stream is null");

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
        }

        out.close();
    }

    private int getPhoto() {
        return R.drawable.android_1;
    }
}
