package com.audora.digiroom;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizQuestionsActivity extends AppCompatActivity {
    DatabaseReference databaseReference,isStudentReference;
    ArrayList<Question> quizQuestions;
    Button prev,next;
    TextView statementView;
    TextView option1View;
    TextView option2View;
    TextView option3View;
    TextView option4View;
    EditText ansView;
    boolean isStudent;
    int i=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        statementView=findViewById(R.id.question_statement);
        next=findViewById(R.id.nextButton);
        prev=findViewById(R.id.prevButton);

        option1View=findViewById(R.id.option1);
        option2View=findViewById(R.id.option2);
        option3View=findViewById(R.id.option3);
        option4View=findViewById(R.id.option4);
        ansView=findViewById(R.id.answer);
        quizQuestions=new ArrayList<>();



        String currentQuizId = getIntent().getStringExtra("id");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        isStudentReference=FirebaseDatabase.getInstance().getReference("profile/"+uid);
        isStudentReference.child("isStudent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isStudent=dataSnapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //to find whether student or not



        databaseReference= FirebaseDatabase.getInstance().getReference("quiz"+"/"+currentQuizId+"/questions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Question newQuestion=new Question();
                    newQuestion.setStatement(postSnapshot.child("statement").getValue(String.class));
                    newQuestion.setAnswer(postSnapshot.child("answer").getValue(Integer.class));
                    newQuestion.setOption1(postSnapshot.child("option1").getValue(String.class));
                    newQuestion.setOption2(postSnapshot.child("option2").getValue(String.class));
                    newQuestion.setOption3(postSnapshot.child("option3").getValue(String.class));
                    newQuestion.setOption4(postSnapshot.child("option4").getValue(String.class));
                    newQuestion.setMarks(postSnapshot.child("marks").getValue(Integer.class));
                    quizQuestions.add(newQuestion);
                    Log.v("yaha par size",quizQuestions.size()+"");

                    i=0;
                    showQuestion(i);
                    if(i==0)
                        prev.setActivated(false);

                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                if(i==0)
                                    prev.setActivated(false);
                                else{
                                    prev.setActivated(true);
                                    i--;
                                    showQuestion(i);
                                }
                        }
                    });

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(i==quizQuestions.size()-1)
                                next.setActivated(false);
                            else{
                                next.setActivated(true);
                                i++;
                                showQuestion(i);
                            }
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });  // to display quizzes

    }

   public void showQuestion(int i){

       Question currentQuestion=quizQuestions.get(i);
        if(currentQuestion.getAnswer()==(0)){

            statementView.setText(currentQuestion.getStatement());
            option1View.setText("");
            option2View.setText("");
            option3View.setText("");
            option4View.setText("");
            ansView.setText("");
            if(!isStudent)
                ansView.setEnabled(false);
        }
        else{
            statementView.setText(currentQuestion.getStatement());

            option1View.setActivated(true);
            option2View.setActivated(true);
            option3View.setActivated(true);
            option4View.setActivated(true);
            ansView.setActivated(true);

            option1View.setText(currentQuestion.getOption1());
            option2View.setText(currentQuestion.getOption2());
            option3View.setText(currentQuestion.getOption3());
            option4View.setText(currentQuestion.getOption4());
            if(isStudent){
                ansView.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else{
                ansView.setText(currentQuestion.getAnswer()+"");
                ansView.setEnabled(false);
            }

        }
    }
}


