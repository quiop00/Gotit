package com.example.gotit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private TessBaseAPI mTess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageView();
        try {
            prepareLanguageDir();
            mTess = new TessBaseAPI();
            mTess.init(String.valueOf(getFilesDir()), "vie");
        } catch (Exception e) {
            // Logging here
        }
    }

    private void initImageView() {
        ImageView imgView = (ImageView) findViewById(R.id.img_input);
        Bitmap input = BitmapFactory.decodeResource(getResources(), R.drawable.ocr);
        imgView.setImageBitmap(input);
    }
    private void copyFile() throws IOException {
        // work with assets folder
        AssetManager assMng = getAssets();
        InputStream is = assMng.open("tessdata/vie.traineddata");
        OutputStream os = new FileOutputStream(getFilesDir() +
                "/tessdata/vie.traineddata");
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }

        is.close();
        os.flush();
        os.close();
    }
    private void prepareLanguageDir() throws IOException {
        File dir = new File(getFilesDir() + "/tessdata");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File trainedData = new File(getFilesDir() + "/tessdata/vie.traineddata");
        if (!trainedData.exists()) {
            copyFile();
        }
    }
    public void doRecognize(View view) {
        if (mTess == null) {
            return;
        }

        try {
            mTess.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.ocr));
            String result = mTess.getUTF8Text();
            TextView resultView = (TextView) findViewById(R.id.txt_result);
            resultView.setText(result);
        } catch (Exception e) {
            // Do what you like here...
        }
    }
}
