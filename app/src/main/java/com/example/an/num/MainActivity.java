package com.example.an.num;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_restart,btn_enter, button_times,button_counts;
    EditText edit;
    ListView list;
    ArrayList<String> anser = new ArrayList<String>();
    ArrayList<String> input = new ArrayList<String>();
    ArrayList<String> List_input = new ArrayList<String>();
    ArrayAdapter adapter;
    Boolean boolen = true, giveup = false, run = false,choicetime = true;
    public static String[] s = {"1","2","3","4","5"};
    public static MyAdapter MyAdapter,countMyAdapter;
    private AlertDialog choiceDialog;
    private String aaa = "";




    private TextView txt;
    private Timer timer;
    private TimerTask timerTask;
    public static ArrayList<String> timeList = new ArrayList<String>();
    public static ArrayList<String> countList = new ArrayList<String>();
    int min=0,sec=0;
    String time;
    int count = 0;
    SimpleDateFormat CurrentTime= new SimpleDateFormat("mm：ss");

    private MenuItem action_choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_restart = (Button)findViewById(R.id.btn_restart);
        btn_enter = (Button)findViewById(R.id.btn_enter);
        edit = (EditText) findViewById(R.id.edit);
        list = (ListView)findViewById(R.id.list);
        txt = (TextView)findViewById(R.id.txt);
//        radom();
        btn_enter.setOnClickListener(this);
        btn_restart.setOnClickListener(this);

        Tools.load(this);
        choiceDialog();



    }

    @Override
    public void onClick(View v) {
        if(v == button_times){
            Toast.makeText(this, "計時", Toast.LENGTH_SHORT).show();
            choicetime = true;
            choiceDialog.cancel();
            action_choice.setIcon(R.drawable.ic_baseline_settings_24);
        }
        if(v == button_counts){
            Toast.makeText(this, "計次", Toast.LENGTH_SHORT).show();
            choicetime = false;
            choiceDialog.cancel();
            action_choice.setIcon(R.drawable.ic_launcher_background);
        }
        if(v == btn_restart && run){
            run = false;
            count = 0;
            boolen = true;
            giveup =true;
            List_input.clear();
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, List_input);
            list.setAdapter(adapter);
//            MyAdapter = new MyAdapter(this,List_input,s);
//            list.setAdapter(MyAdapter);
            if (choicetime){
                stopTimer();
                btn_enter.setText(getString(R.string.ts_3));
                txt.setText(getText(R.string.ts_time));
            }
        }

        if(v == btn_enter){
            if(!run){
                btn_enter.setText("猜");
                run = true;
                radom();
            }else {
                int a=0,b=0;
                boolen = true;
                input.clear();
                if(edit.getText().length() == 4){
                    for(int i=0;i<4;i++){
                        input.add(String.valueOf(edit.getText().charAt(i)));
                    }

                    //判斷4位數字有無重複
                    for (int i=0;i<3;i++){
                        for (int j=i+1;j<4;j++){
                            if(input.get(i).equals(input.get(j))){
                                Toast.makeText(this, "請重新輸入", Toast.LENGTH_SHORT).show();
                                boolen = false;
                            }
                        }
                    }

                    if(boolen){//4位數字無重複
                        count++;
                        for (int i=0;i<4;i++){
                            for (int j=0;j<4;j++){
                                if (input.get(j).equals(anser.get(i))){
                                    if (i == j) {
                                        a=a+1;
                                    }else {
                                        b=b+1;
                                    }
                                }
                            }
                        }
                        Collections.reverse(List_input);
//                        List_input.add("第"+(List_input.size()+1)+"次："+edit.getText().toString()+",  "+a+"A"+b+"B");
                        List_input.add("第"+count+"次："+edit.getText().toString()+",  "+a+"A"+b+"B");
                        edit.setText("");
                        if (a == 4){
                            run = false;
                            btn_enter.setText(getString(R.string.ts_3));
                            if(choicetime){
                                stopTimer();
                            }
                            if(!giveup){        //若沒放棄則更新成績
                                saveList(choicetime);
                            }
                            Tools.save(this,choicetime);
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("結束");
                            if(choicetime){
                                builder.setMessage("用時："+txt.getText());
                            }else {
                                builder.setMessage("次數："+count+"次");
                            }
                            count = 0;
                            builder.setPositiveButton("重新開始", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    boolen = true;
                                    List_input.clear();
                                    adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, List_input);
                                    list.setAdapter(adapter);
                                    txt.setText(getText(R.string.ts_time));//時間歸零
                                }
                            });
                            builder.setCancelable(false);
                            builder.show();
                        }

                        if(List_input.size() == 1){
                            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, List_input);
                            list.setAdapter(adapter);
                        }else {
                            Collections.reverse(List_input);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    Toast.makeText(this, "請輸入4位數", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void radom(){
        Random r = new Random();
        anser.clear();

        for (int i=0; i<4; i++){
            anser.add(String.valueOf(r.nextInt(10)));
            for (int j=0; j<i;){
                if (anser.get(j)==anser.get(i)){
                    anser.set(i,String.valueOf(r.nextInt(10)));
                    j=0;
                }
                else j++;
            }
        }
        if(choicetime){
            startTimer();
        }

        String s="";
        for(int i=0;i<4;i++){
            s = s+anser.get(i);
        }

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void startTimer() {
        giveup = false;
        min = 0; sec = 0;
        if(timer == null){
            timer = new Timer(true);
        }
        if(timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sec++;
                            String m="",s="";
                            if(sec == 60){
                                min++;sec=0;
                            }
                            if(min<10){ m = "0"+min;}else {m=min+"";}
                            if(sec<10){ s = "0"+sec;}else {s=sec+"";}
                            time = m+"："+s;
                            txt.setText(time);
                        }
                    });
                }
            };
        }
        if(timer != null && timerTask != null){
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void stopTimer(){
        timer.cancel();
        timer = null;
        timerTask.cancel();
        timerTask = null;
    }

    private void saveList(boolean b){
        boolean ok=true; //判斷成績是否新增, 未新增true
        if(b){
            if(timeList.size()>0){
                for(int i=0;i<timeList.size();i++){
                    try {
                        Date beginTime=CurrentTime.parse(time);             //本次成績
                        Date endTime=CurrentTime.parse(timeList.get(i));    //歷史成績
                        if(beginTime.getTime()-endTime.getTime()<0 && ok){
                            timeList.add(i,time);
                            if(timeList.size()>5){
                                timeList.remove(5);
                                MyAdapter.notifyDataSetChanged();
                            }else {
                                MyAdapter.notifyDataSetChanged();
                            }
                            ok = false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(ok && timeList.size()<5){
                    timeList.add(time);
                    MyAdapter.notifyDataSetChanged();
                }
            }else {
                timeList.add(time);
                MyAdapter = new MyAdapter(this,timeList,s);
            }
        }else {//次
            if(countList.size()>0){
                for(int i=0;i<countList.size();i++){
                    int history = Integer.parseInt(countList.get(i));    //歷史成績
                    if(count-history<0 && ok){
                        countList.add(i,count+"");
                        if(countList.size()>5){
                            countList.remove(5);
                            countMyAdapter.notifyDataSetChanged();
                        }else {
                            countMyAdapter.notifyDataSetChanged();
                        }
                        ok = false;
                    }
                }
                if(ok && countList.size()<5){
                    countList.add(count+"");
                    countMyAdapter.notifyDataSetChanged();
                }
            }else {
                countList.add(count+"");
                countMyAdapter = new MyAdapter(this,countList,s);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.main_menu, menu);
        action_choice = (MenuItem) menu.findItem(R.id.action_choice);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 取得點選項目的id
        int id = item.getItemId();
        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_settings) {
            // 按下「設定」要做的事
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("紀錄");
            dialog_list.setAdapter(MyAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog_list.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                }
            });
            dialog_list.show();
            return true;
        }
        else if (id == R.id.action_help) {
            // 按下「使用說明」要做的事
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("紀錄");
            dialog_list.setAdapter(countMyAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog_list.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                }
            });
            dialog_list.show();
            return true;
        }
        else if (id == R.id.action_choice) {
            // 按下「關於」要做的事
            choiceDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void choiceDialog(){
        View view1 = getLayoutInflater().inflate(R.layout.dialog, null);
        choiceDialog = new AlertDialog.Builder(this).create();
        choiceDialog.show();

        button_times = view1.findViewById(R.id.button_times);
        button_counts = view1.findViewById(R.id.button_counts);

        Window window = choiceDialog.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(view1);

        button_times.setOnClickListener(this);
        button_counts.setOnClickListener(this);
    }
}
