package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.afd.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ListFileAdapter extends ArrayAdapter<File> {
    private Context context;
    private File[] files;
    public ListFileAdapter(Context context, File[] files){
        super(context,R.layout.list_file_layout, files);
        this.context=context;
        this.files=files;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_file_layout, null);
        File file=files[position];

        TextView textViewFileName=view.findViewById(R.id.textViewFileName);
        textViewFileName.setText(file.getName());
/*
        TextView textViewFileContent=view.findViewById(R.id.textViewFileContent);
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(file.getName());
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
        textViewFileContent.setText(sb);
*/
        TextView textViewFileSize=view.findViewById(R.id.textViewFileSize);
        textViewFileSize.setText(String.valueOf(file.length()));

        return view;
    }
}
