package com.example.an.num;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class Tools {

    public static void save(Context context,boolean b){
        String File = "", Date = "";
        if (b){
            File = "TIME.txt";
            Date = MainActivity.timeList.size() + "=";

            for (int i = 0; i < MainActivity.timeList.size(); i++) {
                Date += MainActivity.timeList.get(i)+"|";
            }
            getFile(context,File, Date);
        }else {
            File = "COUNT.txt";
            Date = MainActivity.countList.size() + "=";

            for (int i = 0; i < MainActivity.countList.size(); i++) {
                Date += MainActivity.countList.get(i)+"|";
            }
            getFile(context,File, Date);
        }




    }

    public static void load(Context context){
        String File;
        String Date = "", DateTmp;
        int tmp = 0, run = 0;

        MainActivity.timeList.clear();
        MainActivity.countList.clear();

        File = "TIME.txt";

        try {
            FileInputStream fin = context.openFileInput(File);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int size = fin.available();
            byte[] data = new byte[size];
            fin.read(data);
            bos.write(data);
            Date = bos.toString();
            bos.close();
            fin.close();
            if (Date.length() > 0) {
                run = Integer.parseInt(Date.substring(tmp).substring(0, Date.substring(tmp).indexOf('=')));
                tmp += ((run + "").length() + 1);
                for (int i = 0; i < run; i++) {
                    DateTmp = Date.substring(tmp + i).substring(0, Date.substring(tmp + i).indexOf('|'));
                    MainActivity.timeList.add(DateTmp);
                    tmp += DateTmp.length();
                }
                MainActivity.MyAdapter = new MyAdapter(context,MainActivity.timeList,MainActivity.s);
//                listView.setAdapter(adapter);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }


         tmp = 0; run = 0;
        File = "COUNT.txt";

        try {
            FileInputStream fin = context.openFileInput(File);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int size = fin.available();
            byte[] data = new byte[size];
            fin.read(data);
            bos.write(data);
            Date = bos.toString();
            bos.close();
            fin.close();
            if (Date.length() > 0) {
                run = Integer.parseInt(Date.substring(tmp).substring(0, Date.substring(tmp).indexOf('=')));
                tmp += ((run + "").length() + 1);
                for (int i = 0; i < run; i++) {
                    DateTmp = Date.substring(tmp + i).substring(0, Date.substring(tmp + i).indexOf('|'));
                    MainActivity.countList.add(DateTmp);
                    tmp += DateTmp.length();
                }
                MainActivity.countMyAdapter = new MyAdapter(context,MainActivity.countList,MainActivity.s);
//                listView.setAdapter(adapter);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    private static void getFile(Context context, String File, String Date) {
        try {
            FileOutputStream outStream = context.openFileOutput(File, MODE_PRIVATE);
            outStream.write(Date.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }



}
