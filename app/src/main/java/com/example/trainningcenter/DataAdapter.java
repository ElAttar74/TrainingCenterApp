package com.example.trainningcenter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class DataAdapter extends ArrayAdapter<Subjects> {
    Context context;
    ArrayList<Subjects> mContent;

    public  DataAdapter(Context context,ArrayList<Subjects>content){
        super(context,R.layout.subject_layout,content);
        this.context = context;
        this.mContent = content;

    }
    public class Holder{
        TextView subjectName;
        ImageView subjectPic;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Subjects subjectsData = getItem(position);
        Holder viewHolder = new Holder();
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.subject_layout,parent,false);
            viewHolder.subjectPic = (ImageView)convertView.findViewById(R.id.imgMySubject);
            viewHolder.subjectName = (TextView)convertView.findViewById(R.id.txtSubjectName);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(Holder)convertView.getTag();
        }
        PicassoClient.downloadImage(context,mContent.get(position).getImgURL(),viewHolder.subjectPic);
        viewHolder.subjectName.setText(subjectsData.getSubjectName());
        return convertView;
    }
}
