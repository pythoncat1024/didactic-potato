package com.python.cat.potato.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.python.cat.potato.R;
import com.python.cat.potato.domain.ScheduleTask;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTaskAdapter extends RecyclerView
        .Adapter<ScheduleTaskAdapter.VH> {


    private List<ScheduleTask.DataBean.TaskBean> taskBeanList;

    public ScheduleTaskAdapter() {
    }

    public void replaceData(ScheduleTask task) {
        if (taskBeanList == null) {
            taskBeanList = new ArrayList<>();
        }
        List<ScheduleTask.DataBean.TaskBean> dataNew = task.data.datas;
        if (dataNew != null) {
            taskBeanList.clear();
            taskBeanList.addAll(dataNew);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        View inflate = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_schedule_task_layout, container, false);
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        int adapterPosition = vh.getAdapterPosition();
        ScheduleTask.DataBean.TaskBean item = taskBeanList.get(adapterPosition);
        vh.tvTime.setText(item.dateStr);
        vh.tvContent.setText(item.content);
        vh.tvTitle.setText(item.title);
    }

    @Override
    public int getItemCount() {
        return taskBeanList == null ? 0 : taskBeanList.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvTime;

        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_todo_title);
            tvContent = itemView.findViewById(R.id.item_todo_content);
            tvTime = itemView.findViewById(R.id.item_todo_time);
        }
    }
}
