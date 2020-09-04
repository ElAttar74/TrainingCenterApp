package com.example.trainningcenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataAdapterTeacher extends ArrayAdapter<Teacher> {
    Context context;
    ArrayList<Teacher> mContent;

    public DataAdapterTeacher(Context context, ArrayList<Teacher>content){
        super(context,R.layout.teachers_list,content);
        this.context = context;
        this.mContent = content;

    }
    public class Holder{
        TextView teacherName,capacity,cost;
        ImageView teacherPic;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Teacher teacherData = getItem(position);
        Holder viewHolder = new Holder();
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.teachers_list,parent,false);
            viewHolder.teacherName = (TextView) convertView.findViewById(R.id.txtTeacherName);
            viewHolder.capacity = (TextView) convertView.findViewById(R.id.txtTeacherCapacity);
            viewHolder.cost = (TextView) convertView.findViewById(R.id.txtTeacherCost);
            viewHolder.teacherPic = (ImageView) convertView.findViewById(R.id.imgTeacherPhoto);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(Holder)convertView.getTag();
        }
        PicassoClient.downloadImage(context,mContent.get(position).getImgURL(),viewHolder.teacherPic);
        viewHolder.teacherName.setText("Name: "+teacherData.getFullName());
        viewHolder.capacity.setText("Capacity: "+teacherData.getCapacity()+" Students");
        viewHolder.cost.setText(teacherData.getPublicCost()+" LE");
        return convertView;
    }
}
