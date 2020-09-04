package com.example.trainningcenter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    DatabaseConnection databaseConnection = new DatabaseConnection();


    Spinner spnPhase,spnGrade,spnSubject;
    Button btnSearch;

    List<String> phase = new ArrayList<String>();
    List<String> grade = new ArrayList<String>();
    List<String> subject = new ArrayList<String>();
    List<String> gradeNumber = new ArrayList<String>();



    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);

        spnPhase = (Spinner)view.findViewById(R.id.spnPhase);
        spnGrade = (Spinner)view.findViewById(R.id.spnGrade);
        spnSubject = (Spinner)view.findViewById(R.id.spnSubject);
        btnSearch = (Button)view.findViewById(R.id.btnSearch);

        Connection connection = databaseConnection.connectDB(getActivity());
        if(connection == null){
            Toast.makeText(getActivity(),"Please check you internet connection",Toast.LENGTH_LONG).show();
        }
        else {
            ResultSet resultSetPhase = databaseConnection.getData("Select Distinct Phase from Grades");
            try {
                while (resultSetPhase.next()){
                    phase.add(resultSetPhase.getString(1));

                }
                ArrayAdapter<String>phaseAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,phase);
                spnPhase.setAdapter(phaseAdapter);

            }
            catch (SQLException ex){
                ;
            }
            spnPhase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    gradeNumber.clear();
                    grade.clear();
                    databaseConnection.connectDB(getActivity());
                    ResultSet resultSetGrade = databaseConnection.getData("Select GradeName,GradeNo from Grades where Phase = '"+spnPhase.getSelectedItem()+"'");
                    try {
                        while (resultSetGrade.next()){
                            grade.add(resultSetGrade.getString(1));
                            gradeNumber.add(resultSetGrade.getString(2));

                        }
                        ArrayAdapter<String>gradeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,grade);
                        spnGrade.setAdapter(gradeAdapter);
                    }
                    catch (SQLException ex){
                        ;
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spnGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    subject.clear();
                    databaseConnection.connectDB(getActivity());
                    ResultSet resultSetSubject = databaseConnection.getData("Select * from Subject where GradeNo = '"+gradeNumber.get(spnGrade.getSelectedItemPosition())+"'");
                    try {
                        while (resultSetSubject.next()){
                            subject.add(resultSetSubject.getString(2));
                        }
                        ArrayAdapter<String>subjectAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,subject);
                        spnSubject.setAdapter(subjectAdapter);
                    }
                    catch (SQLException ex){
                        ;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }

        return view;

    }

}
