package com.example.an.num;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private int anInt = 2;
    private Context context;
    private ArrayList<String> dataList;
    private  String[] s;

    public MyAdapter(Context context, ArrayList<String> dataList,String[] s) {
        this.context = context;
        this.dataList = dataList;
        this.s = s;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.itemlist, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) view.getTag();
        }

        if(i<5){
            viewHolder.pictureImg.setText("NO."+s[i]+"：");
            viewHolder.contentTv.setText(dataList.get(i));
        }


        return view;

    }

    public class ViewHolder{
        TextView pictureImg,contentTv;
        ViewHolder (View view){
            pictureImg = (TextView) view.findViewById(R.id.picture_img);
            contentTv = (TextView) view.findViewById(R.id.content_tv);
        }
    }
}
