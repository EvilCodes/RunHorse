package com.yujie.hero.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yujie.hero.R;
import com.yujie.hero.bean.ExamResultBean;
import com.yujie.hero.bean.ExerciseBean;

import java.util.ArrayList;


/**
 * Created by yujie on 16-9-14.
 */
class GradeViewHolder extends RecyclerView.ViewHolder {
    ImageView levelAvatar;
    TextView levelNumber;
    TextView itemUserName;
    TextView itemUserGrade;
    Button itemChallengeBtn;
    CardView itemCardRoot;


    public GradeViewHolder(View itemView) {
        super(itemView);
        itemCardRoot = (CardView) itemView.findViewById(R.id.item_card_root);
        levelNumber = (TextView) itemView.findViewById(R.id.level_number);
        levelAvatar = (ImageView) itemView.findViewById(R.id.level_avatar);
        itemUserName = (TextView) itemView.findViewById(R.id.item_user_name);
        itemUserGrade = (TextView) itemView.findViewById(R.id.item_user_grade);
        itemChallengeBtn = (Button) itemView.findViewById(R.id.item_challenge_btn);
    }
}
public class ExamGradeAdapter extends RecyclerView.Adapter<GradeViewHolder> {
    public static final String TAG = RecycleAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<ExamResultBean> data;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ExamGradeAdapter(Context context, ArrayList<ExamResultBean> data) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_layout, parent, false);
        GradeViewHolder holder = new GradeViewHolder(view);
        holder.setIsRecyclable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GradeViewHolder holder, int position) {
        final ExamResultBean item = getItem(position);
        if (position<3){
            holder.levelNumber.setVisibility(View.GONE);
            holder.levelAvatar.setVisibility(View.VISIBLE);
            switch (position){
                case 0:
                    holder.levelAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.one));
                    break;
                case 1:
                    holder.levelAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.two));
                    break;
                case 2:
                    holder.levelAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.three));
            }
        }else {
            holder.levelNumber.setVisibility(View.VISIBLE);
            holder.levelAvatar.setVisibility(View.GONE);
            holder.levelNumber.setText((position+1)+"");
        }
        holder.itemChallengeBtn.setVisibility(View.GONE);
        holder.itemUserName.setText(item.getUser_name());
        holder.itemUserGrade.setText(item.getGrade()+"");
        if (listener!=null){
            holder.itemCardRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v,holder.getPosition(),item);
                }
            });
        }
    }

    public ExamResultBean getItem(int position){
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(ArrayList<ExamResultBean> list){
        if (list.size()==0 | list==null){
            return;
        }
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClickListener(View v,int position,ExamResultBean item);
    }
}
