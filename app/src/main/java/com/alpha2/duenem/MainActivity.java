package com.alpha2.duenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alpha2.duenem.signin.SignInActivity;
import com.alpha2.duenem.view_pager_cards.LessonActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alpha2.duenem.R.layout.activity_main);
    }

    public void testSignIn(View v) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void testQuestionActivity(View v) {
        Intent intent;
        intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("QUESTION", new Question());
        startActivity(intent);
    }
    public void testHomeActivity(View v) {
        Intent intent;
        intent = new Intent(this, LessonActivity.class);
        Topic topic = new Topic("Matematica Financeira", "Conceitos Basicos, Juros Simples, Juros Compostos");

        // example Topic
        topic.addLesson(new Lesson("Conceitos Basicos", "Conceito de capital, juros Simples e juros Compostos, quando usamos juros simples e compostos, e taxa de juros"));
        topic.addLesson(new Lesson("Juros Simples", "Juros simples, fórmula para calcular juros simples, fórmula para calcular o montante e exercicios"));
        topic.addLesson(new Lesson("Juros Compostos", "Juros compostos, capitalização, fórmula para calcular juros compostos, com exemplo resolvido."));
        topic.addLesson(new Lesson("Relação entre juros e progressões",  "Tipos de taxas: taxas equivalentes, taxas nominais, taxas efetivas, taxa real."));
        //

        intent.putExtra("TOPIC", topic);

        startActivity(intent);
    }
}
