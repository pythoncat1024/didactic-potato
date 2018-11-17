package com.python.cat.potato.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.python.cat.potato.R;
import com.python.cat.potato.viewmodel.CalendarVM;

import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalendarInfoAdapter extends RecyclerView.Adapter<CalendarInfoAdapter.VH> {

    private List<String> mInfoList = new ArrayList<>();

    private Context mContext;

    private OnItemLongClickListener<String> mItemLongClickListener;
    private OnItemClickListener<String> mItemClickListener;

    public CalendarInfoAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    public void setCalendarInfoList(List<String> infoList) {
        if (infoList != null) {
            this.mInfoList = infoList;
        } else {
            this.mInfoList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void addList(String... infoArgs) {
        if (mInfoList == null) {
            mInfoList = new ArrayList<>();
        }
        Collections.addAll(mInfoList, infoArgs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_calendar_info_layout, viewGroup, false);
        VH vh = new VH(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        int adapterPosition = vh.getAdapterPosition();
        String info = mInfoList.get(adapterPosition);
        vh.itemView.setOnLongClickListener(v -> {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.click(vh.itemView, info, position);
            }
            return true;
        });
        vh.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.click(vh.itemView, info, position);
            }
        });

        try {
            String jsonWithTime = CalendarVM.formatJsonWithTime(info);
            vh.tvText.setText(jsonWithTime);
        } catch (JSONException e) {
            e.printStackTrace();
            vh.tvText.setText(info);
        }

    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private TextView tvText;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.item_event_info);
        }
    }


    public interface OnItemLongClickListener<T> {

        void click(View targetView, T info, int adapterPosition);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void click(View targetView, T info, int adapterPosition);
    }

    public void setOnItemClickListener(OnItemClickListener<String> listener) {
        this.mItemClickListener = listener;
    }


}
