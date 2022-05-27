package com.hp.gre3000.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hp.gre3000.R;
import com.hp.gre3000.model.WordBean;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<WordBean> list;

    private boolean isVis;

    public WordAdapter(List<WordBean> list, boolean isVis) {
        this.list = list;
        this.isVis = isVis;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new WordViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder, int position) {
        WordBean wordBean = list.get(position);
        holder.english.setText(wordBean.getEnglish());
        holder.chinese.setText(wordBean.getChinese());
        holder.para.setText(wordBean.getPara());
        holder.chinese.setVisibility(isVis ? View.VISIBLE : View.INVISIBLE);
        holder.para.setVisibility(isVis ? View.VISIBLE : View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.chinese.setVisibility(holder.chinese.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                holder.para.setVisibility(holder.para.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void updateItem(boolean isVis) {
        this.isVis = isVis;
        notifyDataSetChanged();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView english;
        private TextView chinese;
        private TextView para;


        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            english = itemView.findViewById(R.id.english);
            chinese = itemView.findViewById(R.id.chinese);
            para = itemView.findViewById(R.id.para);

        }
    }
}
