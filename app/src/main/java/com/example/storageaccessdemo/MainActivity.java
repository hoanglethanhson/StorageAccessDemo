package com.example.storageaccessdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> fileName;
    ArrayList<String> fileType;
    ListView listView;
    ArrayAdapter adapter;
    String currentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listView);
        currentFolder = Environment.getExternalStorageDirectory().toString();
        updateListView(currentFolder);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TwoLineListItem twoLineListItem = (TwoLineListItem) view;
                String fileName = twoLineListItem.getText1().getText().toString();
                String fileType = twoLineListItem.getText2().getText().toString();
                if (fileType.equalsIgnoreCase("Folder")) {
                    currentFolder = currentFolder + "/" + fileName;
                    updateListView(currentFolder);
                } else {
                    if (fileType.equalsIgnoreCase("txt")) {
                        File file = new File(currentFolder + "/" + fileName);
                        StringBuilder text = new StringBuilder();
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                text.append(line + "\n");
                            }
                            reader.close();
                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            intent.putExtra("data", text.toString());
                            startActivity(intent);
                        } catch (Exception e) {

                        }
                    } else {
                        //run file with default app
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        File file = new File(currentFolder);
        currentFolder = file.getParent();
        if (currentFolder == null) {
            super.onBackPressed();
            return;
        }
        updateListView(currentFolder);
    }

    private void updateListView(String foler) {
        fileName = new ArrayList<>();
        fileType = new ArrayList<>();

        File directory = new File(foler);
        File [] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i <files.length; i++) {
            fileName.add(files[i].getName());

            if (files[i].isDirectory()) {
                fileType.add("Folder");
            } else {
                fileType.add(MimeTypeMap.getFileExtensionFromUrl(files[i].getAbsolutePath()));
            }
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, fileName) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(fileName.get(position));
                text2.setText(fileType.get(position));
                return view;
            }
        };

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
