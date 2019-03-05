package com.example.storageaccessdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.FileOutputStream;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EditText editText = findViewById(R.id.editText);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String text = bundle.getString("data");
        editText.setText(text);
    }

    private final int OPEN_REQUEST_CODE = 100;
    private final int SAVE_REQUEST_CODE = 100;

    public void openOnclick(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "newFile.txt");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public void saveOnclick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "newFile.txt");
        startActivityForResult(intent, SAVE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SAVE_REQUEST_CODE) {
                Uri uri = data.getData();
                writeFile(uri);
            }
        }
    }

    private void writeFile(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());

            EditText editText = findViewById(R.id.editText);
            String text =  editText.getText().toString();

            fileOutputStream.write(text.getBytes());
            fileOutputStream.close();
            pfd.close();

        } catch (Exception e) {

        }
    }
}
