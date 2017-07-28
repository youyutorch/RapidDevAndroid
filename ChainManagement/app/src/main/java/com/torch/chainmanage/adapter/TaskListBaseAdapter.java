package com.torch.chainmanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.torch.chainmanage.R;
import com.torch.chainmanage.model.Task;

import java.util.List;

/**
 * HomeFragment任务列表适配器
 */
public class TaskListBaseAdapter extends RecyclerView.Adapter<TaskListBaseAdapter.TaskViewHolder> {
    private Context mContxt;
    private List<Task> list;
    private int count;

    public TaskListBaseAdapter(Context mContxt, List<Task> list, int count) {
        this.mContxt = mContxt;
        this.list = list;
        this.count = count;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContxt, R.layout.fragment_home_task_item, null);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        Task rd = list.get(position);
        holder.title.setText(rd.getTitle());
        if (position == count - 1) {
            holder.view.setVisibility(View.GONE);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到任务详情
                Toast.makeText(mContxt, "任务详情界面敬请期待", Toast.LENGTH_SHORT).show();
                int[] outLocation = new int[]{0, 0};
                holder.root.getLocationOnScreen(outLocation);
                Log.w("TaskListBaseAdapter", "onStart - holder.root location=" + outLocation[0] + "," + outLocation[1]);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return count;
        } else {
            return 0;
        }
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView arrow;
        TextView title;
        RelativeLayout root;
        View view;

        public TaskViewHolder(View itemView) {
            super(itemView);
            arrow = (ImageView) itemView.findViewById(R.id.fragment_home_task_item_arrow);
            title = (TextView) itemView.findViewById(R.id.fragment_home_task_item_title);
            root = (RelativeLayout) itemView.findViewById(R.id.item_root_home);
            view = itemView.findViewById(R.id.item_home_task);
        }
    }

}
