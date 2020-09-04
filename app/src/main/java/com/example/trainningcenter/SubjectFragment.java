package com.example.trainningcenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    GridView grdSubjects;
    DataAdapter dataAdapter;
    Subjects subjectsModel;
    //GetSubject getSubject;
    public  static String subjectNumber;
    SwipeRefreshLayout swipeRefreshLayout;


    public SubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_subject, container, false);
        grdSubjects = (GridView)view.findViewById(R.id.grdSubjects);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,R.color.colorAccent,R.color.colorPrimaryDark);
        ArrayList<Subjects> myLst;
        if (LoginActivity.gradeNumber != null){
            myLst = new ArrayList<Subjects>(GetSubject.getSubjectData(getActivity(),LoginActivity.gradeNumber));
        }
        else {
            myLst = new ArrayList<Subjects>(GetSubject.getSubjectData(getActivity(),MainActivity.gradeNumber));
        }
        dataAdapter = new DataAdapter(getActivity(), myLst);
        grdSubjects.setAdapter(dataAdapter);

        grdSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subjectsModel = dataAdapter.getItem(position);
                subjectNumber = subjectsModel.getSubjectNumber();
                Toast.makeText(getActivity(),subjectNumber+"",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(),TeacherActivity.class));

            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        ArrayList<Subjects> myLst;
        if (LoginActivity.gradeNumber != null){
            myLst = new ArrayList<Subjects>(GetSubject.getSubjectData(getActivity(),LoginActivity.gradeNumber));
        }
        else {
            myLst = new ArrayList<Subjects>(GetSubject.getSubjectData(getActivity(),MainActivity.gradeNumber));
        }
        dataAdapter = new DataAdapter(getActivity(), myLst);
        grdSubjects.setAdapter(dataAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
