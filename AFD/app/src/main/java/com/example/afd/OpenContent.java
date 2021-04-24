package com.example.afd;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OpenContent extends AppCompatActivity {
    String content;
    String name;
    int ok=0;

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_content);
        Intent intent=getIntent();
        //fileName
        String messageFileName = intent.getStringExtra("fileName");
        TextView textViewFileName=findViewById(R.id.textViewFileNameOpen);
        textViewFileName.setText(messageFileName);
        //fileContent
        String messageFileContent = intent.getStringExtra("fileContent");
        TextView textViewFileContent=findViewById(R.id.textViewFileContentOpen);
        textViewFileContent.setText(messageFileContent);

        getIntent().putExtra("fileNameEditedOk", "false");
    }

    private String saveName(String fileName){
        try {
            TextView textViewFileName=findViewById(R.id.textViewFileNameOpen);
            textViewFileName.setText(fileName);
            setName(fileName);
            getIntent().putExtra("fileNameEdited", getName());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return "false";
        }
        return "true";
    }

    public void editFileName(View view) {
        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.create_file_dialog_edit_name_layout, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(OpenContent.this);
        builder.setTitle(getText(R.string.create_file));
        builder.setView(view);
        builder.setCancelable(false);
        final Dialog dialog=builder.show();
        EditText editTextFileName=view.findViewById(R.id.editTextFileNameEdited);

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
                String index=saveName(editTextFileName.getText().toString());
                getIntent().putExtra("fileNameEditedOk", index);
                dialog.dismiss();
            }
        });
    }

    public void editFileContent(View view) {
    }
}
