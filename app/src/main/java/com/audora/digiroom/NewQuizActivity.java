package com.audora.digiroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewQuizActivity extends AppCompatActivity {

    private Spinner spinnerQuestionType,spinnerMarks;
    private DatabaseReference databaseReference;
    DatabaseReference quizReference;
    Quiz newQuiz=new Quiz();
    int quizMarks=0;
    EditText quizName,quizDuration,quizCode;
    EditText questionStatement,option1text,option2text,option3text,option4text,answertext;
    Button nextQuestionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);

        String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("quiz_name/"+myid);
        quizReference = FirebaseDatabase.getInstance().getReference("quiz");
        Button submitButton=(Button)findViewById(R.id.buttonSubmitQuiz);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(NewQuizActivity.this);
                final View promptsView = li.inflate(R.layout.new_quiz_prompt, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewQuizActivity.this);
                alertDialogBuilder.setView(promptsView);
                quizName = (EditText) promptsView.findViewById(R.id.promptName);
                quizDuration = (EditText) promptsView.findViewById(R.id.promptDuration);
                quizCode=promptsView.findViewById(R.id.promptCode);

                if(newQuiz.getQuestions().size()<=0){
                    Toast.makeText(NewQuizActivity.this,"Please Enter at least one Question!",Toast.LENGTH_SHORT).show();
                }
                else{
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String sQuizName = quizName.getText().toString().trim();
                            String sQuizDuration = quizDuration.getText().toString().trim();
                            String sQuizCode = quizCode.getText().toString().trim();

                            if (sQuizName.isEmpty() || sQuizDuration.isEmpty()) {
                                Toast.makeText(NewQuizActivity.this, "One of the required field is empty.", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                newQuiz.setName(sQuizName);
                                newQuiz.setCode(sQuizCode);
                                newQuiz.setDuration(sQuizDuration);

                                String id = databaseReference.push().getKey();
                                databaseReference.child(id+"/name").setValue(newQuiz.getName());
                                databaseReference.child(id+"/date").setValue(System.currentTimeMillis());
                                databaseReference.child(id+"/duration").setValue(newQuiz.getDuration());
                                databaseReference.child(id+"/marks").setValue(newQuiz.getTotalMarks());
                                databaseReference.child(id+"/code").setValue(newQuiz.getCode());

                                quizReference = quizReference.child(id);
                                quizReference.child("name").setValue(newQuiz.getName());
                                quizReference.child("date").setValue(System.currentTimeMillis());
                                quizReference.child("duration").setValue(newQuiz.getDuration());
                                quizReference.child("marks").setValue(newQuiz.getTotalMarks());
                                quizReference.child("code").setValue(newQuiz.getCode());
                                quizReference=quizReference.child("questions");

                                Log.v("size",""+newQuiz.getQuestions().size());
                                for(int i=0;i<newQuiz.getQuestions().size();i++){
                                    quizReference.push().setValue(newQuiz.getQuestions().get(i));
                                }

                                Toast.makeText(NewQuizActivity.this,"Quiz Created!",Toast.LENGTH_SHORT).show();

                                //finish();
                                NewQuizActivity.super.onBackPressed();
                            }
                            //alertDialogBuilder.setView(promptsView);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    //finish();
                }
            }
        });

        spinnerQuestionType = (Spinner) findViewById(R.id.spinnerQuestionType);
        spinnerMarks = (Spinner) findViewById(R.id.spinnerMarks);
        Integer[] marks = new Integer[]{1,2,3,4,5};
        String[] options = new String[]{"MCQ","Paragraph"};

        ArrayAdapter<Integer>adapterMarks = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,marks);
        ArrayAdapter<String> adapterQuestionType = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,options);

        spinnerMarks.setAdapter(adapterMarks);
        spinnerQuestionType.setAdapter(adapterQuestionType);

        spinnerMarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quizMarks=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        questionStatement=(EditText) findViewById(R.id.editTextQuestion);
        option1text=(EditText) findViewById(R.id.editTextOption1);
        option2text=(EditText) findViewById(R.id.editTextOption2);
        option3text=(EditText) findViewById(R.id.editTextOption3);
        option4text=(EditText) findViewById(R.id.editTextOption4);
        answertext=(EditText) findViewById(R.id.editTextAnswer);
        nextQuestionButton=(Button) findViewById(R.id.buttonNextQuestion);
        spinnerQuestionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    option1text.setVisibility(View.VISIBLE);
                    option2text.setVisibility(View.VISIBLE);
                    option3text.setVisibility(View.VISIBLE);
                    option4text.setVisibility(View.VISIBLE);
                    answertext.setVisibility(View.VISIBLE);
                    questionStatement.setMaxLines(4);
                    nextQuestionButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String squestionStatement=questionStatement.getText().toString().trim();
                            String soption1text=option1text.getText().toString().trim();
                            String soption2text=option2text.getText().toString().trim();
                            String soption3text=option3text.getText().toString().trim();
                            String soptiont4text=option4text.getText().toString().trim();
                            String sanswertext=answertext.getText().toString();
                            int ans=0;
                            try{
                                ans=Integer.parseInt(sanswertext);
                            }
                            catch (Exception e){}
                            if(squestionStatement.length()==0 || soption3text.length()==0 || soptiont4text.length()==0 ||
                                    soption2text.length()==0 || soption1text.length()==0 ||sanswertext.length()==0){
                                Toast.makeText(NewQuizActivity.this,"Enter Complete Data!",Toast.LENGTH_SHORT).show();
                            }
                            else if(ans>4 || ans<1){
                                Toast.makeText(NewQuizActivity.this,"Enter Correct value for answer!",Toast.LENGTH_SHORT).show();
                            }

                            else{
                                newQuiz.setTotalMarks(newQuiz.getTotalMarks()+quizMarks);
                                Question q=new Question(squestionStatement,soption1text,soption2text,soption3text,soptiont4text,ans,quizMarks);
                                newQuiz.addQuestion(q);
                                questionStatement.getText().clear();
                                option1text.getText().clear();
                                option2text.getText().clear();
                                option3text.getText().clear();
                                option4text.getText().clear();
                                answertext.getText().clear();
                            }

                        }
                    });
                    Toast.makeText(NewQuizActivity.this,"MCQ",Toast.LENGTH_SHORT).show();
                }
                else if(position==1){
                    option1text.setVisibility(View.INVISIBLE);
                    option2text.setVisibility(View.INVISIBLE);
                    option3text.setVisibility(View.INVISIBLE);
                    option4text.setVisibility(View.INVISIBLE);
                    answertext.setVisibility(View.INVISIBLE);
                    questionStatement.setMaxLines(View.GONE);

                    nextQuestionButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String squestionStatement=questionStatement.getText().toString().trim();
                            if(squestionStatement.length()==0){
                                Toast.makeText(NewQuizActivity.this,"Enter Question Please!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                newQuiz.setTotalMarks(newQuiz.getTotalMarks()+quizMarks);
                                Question q=new Question(squestionStatement,"","","","",0,quizMarks);
                                newQuiz.addQuestion(q);
                                questionStatement.getText().clear();
                                option1text.getText().clear();
                                option2text.getText().clear();
                                option3text.getText().clear();
                                option4text.getText().clear();
                                answertext.getText().clear();
                            }

                        }
                    });
                    Toast.makeText(NewQuizActivity.this,"Paragraph",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}