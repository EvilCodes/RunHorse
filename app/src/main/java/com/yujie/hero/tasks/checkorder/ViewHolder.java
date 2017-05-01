package com.yujie.hero.tasks.checkorder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yujie.hero.R;

/**
 * Created by yujie on 16-9-14.
 */
class ViewHolder extends RecyclerView.ViewHolder {
    ImageView levelAvatar;
    TextView levelNumber;
    TextView itemUserName;
    TextView itemUserGrade;
    Button itemChallengeBtn;
    CardView itemCardRoot;


    public ViewHolder(View itemView) {
        super(itemView);
        itemCardRoot = (CardView) itemView.findViewById(R.id.item_card_root);
        levelNumber = (TextView) itemView.findViewById(R.id.level_number);
        levelAvatar = (ImageView) itemView.findViewById(R.id.level_avatar);
        itemUserName = (TextView) itemView.findViewById(R.id.item_user_name);
        itemUserGrade = (TextView) itemView.findViewById(R.id.item_user_grade);
        itemChallengeBtn = (Button) itemView.findViewById(R.id.item_challenge_btn);
    }
}
