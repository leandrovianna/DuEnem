package com.alpha2.duenem.view_pager_cards;

/**
 * Created by root on 19/06/17.
 */

import android.support.v7.widget.CardView;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}