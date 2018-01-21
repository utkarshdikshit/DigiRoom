package com.audora.digiroom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ud on 1/18/2018.
 */

public class QuizAdapter extends ArrayAdapter<Quiz> {

    public QuizAdapter(@NonNull Context context, ArrayList<Quiz> quizzes) {
        super(context, 0,quizzes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView=convertView;

        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.quiz_item_layout,parent,false);
        }

        Quiz currentQuiz=getItem(position);

        TextView nameView=listItemView.findViewById(R.id.quiz_name);
        TextView durationView=listItemView.findViewById(R.id.duration);
        TextView marksView=listItemView.findViewById(R.id.marks);

        nameView.setText(currentQuiz.getName());
        durationView.setText("Duration: "+ currentQuiz.getDuration()+"mins.");
        marksView.setText("Max. marks : "+ currentQuiz.getTotalMarks().toString());



        return listItemView;
    }
}
