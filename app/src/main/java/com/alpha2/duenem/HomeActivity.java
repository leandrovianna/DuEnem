package com.alpha2.duenem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alpha2.duenem.model.Lesson;
import com.alpha2.duenem.model.Material;
import com.alpha2.duenem.model.Question;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.signin.SignInActivity;
import com.alpha2.duenem.view_pager_cards.LessonActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ArrayAdapter<Topic> mAdapter;

    private ValueEventListener mTopicRefListener;
    private DatabaseReference mTopicRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_home);

        List<Topic> topics = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, topics);

        ListView listView = (ListView) findViewById(R.id.listViewTopics);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic t = (Topic) parent.getItemAtPosition(position);
                Intent intent = new Intent(HomeActivity.this, LessonActivity.class);
                intent.putExtra(LessonActivity.TOPIC_EXTRA, t);
                startActivity(intent);
            }
        });

        mTopicRef = FirebaseDatabase.getInstance().getReference()
                .child("topic");

        // ---- Test Activity ------

        Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);
        Lesson lesson = new Lesson("Titulo", "Descricao");
        List<String> alternatives = new ArrayList<>();

        alternatives.add("representação  do  legislativo  com  a  fórmula  do \n" +
                "bipartidarismo.");
        alternatives.add("detenção  de  lideranças  populares  por  crimes  de \n" +
                "subversão.");
        alternatives.add("presença  de  políticos  com  trajetórias  no  regime \n" +
                "autoritário");
        alternatives.add("prorrogação  das  restrições  advindas  dos  atos \n" +
                "institucionais.");
        alternatives.add("estabilidade da economia com o congelamento anual \n" +
                "de preços.");

        lesson.addMaterial(new Question("Titulo", "\"Batizado  por Tancredo  Neves  de  “Nova  República”, \n" +
                "o  período  que  marca  o  reencontro  do  Brasil  com  os \n" +
                "governos  civis  e  a  democracia  ainda  não  completou \n" +
                "seu  quinto  ano  e  já  viveu  dias  de  grande  comoção. \n" +
                "Começou  com  a  tragédia  de  Tancredo,  seguiu  pela \n" +
                "euforia  do Plano  Cruzado,  conheceu  as  depressões  da \n" +
                "inflação  e das ameaças da  hiperinflação  e desembocou \n" +
                "na  movimentação  que  antecede  as  primeiras  eleições \n" +
                "\n" +
                "diretas para presidente em 29 anos.\" \n\nO  período  descrito  apresenta  continuidades  e  rupturas \n" +
                "em  relação  à  conjuntura  histórica  anterior.  Uma  dessas \n" +
                "continuidades consistiu na", alternatives, 0));

        alternatives.add("Frente pioneira.");
        alternatives.add("Zona de transição");
        alternatives.add("Região polarizada.");
        alternatives.add("Área de conurbação.");
        alternatives.add("Periferia metropolitana.");

        lesson.addMaterial(new Question("Titulo", "\"O  Rio  de  Janeiro  tem  projeção  imediata  no  próprio \n" +
                "estado e no Espírito Santo,  em parcela do sul do estado \n" +
                "da  Bahia,  e  na  Zona  da  Mata,  em  Minas  Gerais,  onde \n" +
                "tem  influência  dividida  com  Belo  Horizonte.  Compõem \n" +
                "a  rede  urbana  do  Rio  de  Janeiro,  entre  outras  cidades: \n" +
                "Vitória,  Juiz de  Fora,  Cachoeiro de  Itapemirim,  Campos \n" +
                "dos Goytacazes, Volta  Redonda - Barra Mansa, Teixeira \n" +
                "de Freitas, Angra dos Reis e Teresópolis.\"\n" +
                "\n" +
                "O  conceito  que  expressa  a  relação  entre  o  espaço \n" +
                "apresentado e a cidade do Rio de Janeiro é:", alternatives, 0));
        lesson.addMaterial(new Question("Titulo", "jfdkajfkljdf\ndjklsafjakjsfjaldfj\nldkajfjl k jl kjdsl jfdj\n\n", alternatives, 2));
        lesson.addMaterial(new Question("Titulo", "jfdkajfkljdf\ndjklsafjakjsfjaldfj\nldkajfjl k jl kjdsl jfdj\n\n", alternatives, 3));
        lesson.addMaterial(new Question("Titulo", "jfdkajfkljdf\ndjklsafjakjsfjaldfj\nldkajfjl k jl kjdsl jfdj\n\n", alternatives, 4));
        intent.putExtra("LESSON", lesson);

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTopicRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setListData(dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                mAdapter.clear();
                Toast.makeText(HomeActivity.this,
                        "É necessário estar logado para usar o app.", Toast.LENGTH_LONG)
                        .show();
            }

        };

        mTopicRef.addValueEventListener(mTopicRefListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTopicRef.removeEventListener(mTopicRefListener);
    }

    private void setListData(Iterable<DataSnapshot> children) {
        mAdapter.clear();
        for (DataSnapshot topicSnap : children) {
            Topic topic = topicSnap.getValue(Topic.class);

            if (topic != null) {
                for (DataSnapshot lessonSnap : topicSnap.child("lessons").getChildren()) {
                    Lesson lesson = lessonSnap.getValue(Lesson.class);


                    topic.addLesson(lesson);
                }
                mAdapter.add(topic);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
