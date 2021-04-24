package com.example.afd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.ListFileAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    public ListView listViewFiles;
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewFiles=findViewById(R.id.listViewFiles);
        registerForContextMenu(listViewFiles);
        loadData();
    }

    private void loadData(){
        File dir=getFilesDir();
        ListFileAdapter listFileAdapter=new ListFileAdapter(getApplicationContext(), dir.listFiles());
        listViewFiles.setAdapter(listFileAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.file_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.create_file){
            openCreateFileDialog();
        }
        return true;
    }
    private void openCreateFileDialog(){
        final String[] c = {};
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.create_file_dialog_layout, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getText(R.string.create_file));
        builder.setView(view);
        builder.setCancelable(false);
        final Dialog dialog=builder.show();
        EditText editTextFileName=view.findViewById(R.id.editTextFileNameEdited);
        EditText editTextFileContent=view.findViewById(R.id.editTextContentFileEdited);
        Button buttonCancel=view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button buttonSave=view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(editTextFileName.getText().toString(), editTextFileContent.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void saveFile(String fileName, String content){
        try {
            File file = new File(getFilesDir()+File.separator+fileName);
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            loadData();
            setContent(content);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.listViewFiles){
            String [] actions = getResources().getStringArray(R.array.context_menu);
            for (int i=0;i<actions.length;i++){
                menu.add(Menu.NONE, i, i, actions[i]);
            }

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();
        String [] menuItems = getResources().getStringArray(R.array.context_menu);
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName) {

            case "Edit":
                openFile(item);
                if(getIntent().getStringExtra("fileNameEditedOk").equals("true"))
                {
                String messageFileName1 = getIntent().getStringExtra("fileNameEdited");
                System.out.println(messageFileName1);
                TextView textViewFileName1 = findViewById(R.id.textViewFileName);
                textViewFileName1.setText(messageFileName1);
                }
                break;

            case "Delete":
                deleteFile(item);
                break;
        }
        return true;
    }
    private void openFile(MenuItem item){
        Intent intent= new Intent(getApplicationContext(), OpenContent.class);
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View view=adapterContextMenuInfo.targetView;

        TextView textViewFileName=view.findViewById(R.id.textViewFileName);
        String messageFileName=textViewFileName.getText().toString();
        intent.putExtra("fileName", messageFileName);

        File file= new File(messageFileName);
            FileInputStream fis = null;
            try {
                fis = getApplicationContext().openFileInput(file.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(line);
            }
            intent.putExtra("fileContent", (Serializable) sb);
        /*TextView textViewFileContent=view.findViewById(R.id.textViewFileContent);
        String messageFileContent=textViewFileContent.getText().toString();
         */


        //intent.putExtra("fileContent", getContent());

        //fileName
        startActivity(intent);
    }
    private void deleteFile(MenuItem item){
        try {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            View view=adapterContextMenuInfo.targetView;
            TextView textViewFileName=view.findViewById(R.id.textViewFileName);
            String filename=textViewFileName.getText().toString();
            Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
            for (File file: getFilesDir().listFiles()){
                if(file.getName().equalsIgnoreCase(filename)){
                    file.delete();
                    break;
                }
            }
            loadData();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}