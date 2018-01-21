package com.audora.digiroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyQuizzes extends Fragment {
    DatabaseReference databaseReference;
    ArrayList<Quiz> quizList=new ArrayList<>();
    ListView quizListview;
    QuizAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_quizzes, container, false);

        FloatingActionButton fab=view.findViewById(R.id.fab); // for floating button
        quizListview= view.findViewById(R.id.quiz_list);
        if(getActivity() instanceof StudentDashBoard){
            fab.setVisibility(View.GONE);
        }



        if(getActivity() instanceof StudentDashBoard){
            databaseReference=FirebaseDatabase.getInstance().getReference("quiz");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Quiz currentQuiz=new Quiz();
                        currentQuiz.setId(postSnapshot.getKey());
                        currentQuiz.setName(postSnapshot.child("name").getValue(String.class));
                        currentQuiz.setDuration(postSnapshot.child("duration").getValue(String.class));
                        currentQuiz.setTotalMarks((postSnapshot.child("marks").getValue(Integer.class)));
                        currentQuiz.setCode(postSnapshot.child("code").getValue(String.class));
                        Log.v("mera currentquiz",currentQuiz.getDuration()+"");
                        quizList.add(currentQuiz);
                    }
                    Log.v("dhiraj",quizList.size()+"");
                    adapter=new QuizAdapter(getActivity(), quizList);
                    quizListview.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }); //to display quizzes for student
        }
        else{
            databaseReference=FirebaseDatabase.getInstance().getReference("quiz_name");
            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
            String uid=currentUser.getUid();
            databaseReference.child(uid).addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Quiz currentQuiz=new Quiz();
                        currentQuiz.setId(postSnapshot.getKey());
                        currentQuiz.setName(postSnapshot.child("name").getValue(String.class));
                        currentQuiz.setDuration(postSnapshot.child("duration").getValue(String.class));
                        currentQuiz.setTotalMarks((postSnapshot.child("marks").getValue(Integer.class)));
                        currentQuiz.setCode(postSnapshot.child("code").getValue(String.class));
                        Log.v("mera currentquiz",currentQuiz.getDuration()+"");
                        quizList.add(currentQuiz);
                    }
                    Log.v("dhiraj",quizList.size()+"");
                    adapter=new QuizAdapter(getActivity(), quizList);
                    quizListview.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });  // to display quizzes for teacher
        }


        quizListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {   // to set clicking listener on quiz item
                final String currentQuizId=adapter.getItem(position).getId();
                final String currentQuizCode=adapter.getItem(position).getCode();
                //Log.v("code1",currentQuizCode+"");
                if(getActivity() instanceof StudentDashBoard){
                    LayoutInflater li=LayoutInflater.from(getContext()); //isko check karna
                    View promptView=li.inflate(R.layout.dialog_layout,null);
                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getContext()); //isko check karna
                    alertDialogBuilder.setView(promptView);
                    final EditText userInput=promptView.findViewById(R.id.dialogEditText);

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                String userCode=userInput.getText().toString();
                                //Log.v("code",currentQuizCode+"");
                                if(userCode.equals(currentQuizCode)){
                                    Intent i1=new Intent(getActivity(),QuizQuestionsActivity.class);
                                    i1.putExtra("id",currentQuizId);
                                    startActivity(i1);
                                }
                                else{
                                    userInput.setText("");
                                    Toast.makeText(getContext(),"Code does not match",Toast.LENGTH_SHORT).show();
                                }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alertDialog=alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    Intent i=new Intent(getActivity(),QuizQuestionsActivity.class);
                    i.putExtra("id",currentQuizId);
                    startActivity(i);
                }
            }
        });


        fab.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), NewQuizActivity.class); // for floating button
                startActivity(i);
            }
        });
        return view;
    }
}
