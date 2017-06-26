package com.alpha2.duenem;

import android.app.Activity;
import android.content.Intent;

import com.alpha2.duenem.model.Discipline;
import com.alpha2.duenem.model.Topic;
import com.alpha2.duenem.signin.SignInActivity;
import com.alpha2.duenem.view_pager_cards.LessonActivity;
import com.alpha2.duenem.view_pager_cards.TrainActivity;

public abstract class IntentAbstractFactory {

    public static Intent createHomeActivityIntent(Activity source, Discipline discipline) {
        Intent intent = new Intent(source, HomeActivity.class);
        intent.putExtra(BaseActivity.SELECTED_ITEM_ID_EXTRA, R.id.materialestudo);
        intent.putExtra(HomeActivity.DISCIPLINE_EXTRA, discipline);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent createSignInActivityIntent(Activity source) {
        Intent intent = new Intent(source, SignInActivity.class);
        intent.putExtra(BaseActivity.SELECTED_ITEM_ID_EXTRA, R.id.perfil);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent createRankingActivityIntent(Activity source) {
        Intent intent = new Intent(source, RankingActivity.class);
        intent.putExtra(BaseActivity.SELECTED_ITEM_ID_EXTRA, R.id.ranking);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent createTrainActivity(Activity source, Topic topic) {
        Intent intent = new Intent(source, TrainActivity.class);
        intent.putExtra(LessonActivity.TOPIC_EXTRA, topic);
        return intent;
    }
}
