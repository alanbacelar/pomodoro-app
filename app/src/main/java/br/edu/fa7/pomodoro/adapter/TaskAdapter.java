package br.edu.fa7.pomodoro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.model.Task;
import br.edu.fa7.pomodoro.util.RecyclerViewOnClickListener;

/**
 * Created by bruno on 19/08/15.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Serializable {

    private Context mContext;
    private List<Task> list;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListener listener;

    public TaskAdapter(Context context, List<Task> list) {
        this.mContext = context;
        this.list = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    public List<Task> getList() {
        return list;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.cardview_item, viewGroup, false);
        TaskViewHolder pvh = new TaskViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder viewHolder, int i) {

        Task task = list.get(i);

        viewHolder.mTitle.setText(task.getTitle());
        viewHolder.mDescription.setText(task.getDescription());
        viewHolder.mTomatoes.setText(
                task.getDoneTomatoes() + " done(s) of " + task.getTomatoes() + " tomatoes"
        );

        if (task.isDone()) {
            viewHolder.mTitle.setPaintFlags(viewHolder.mTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.mLayout.setBackgroundColor(Color.LTGRAY);
            viewHolder.mPlayBtn.setEnabled(false);
            viewHolder.mPlayBtn.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mPlayBtn.setOnClickListener(viewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mDescription;
        private TextView mTomatoes;
        private ImageButton mPlayBtn;
        private RelativeLayout mLayout;

        public TaskViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mTitle = (TextView) itemView.findViewById(R.id.cardview_task_title);
            mDescription = (TextView) itemView.findViewById(R.id.cardview_task_description);
            mTomatoes = (TextView) itemView.findViewById(R.id.cardview_task_tomatoes);
            mPlayBtn = (ImageButton) itemView.findViewById(R.id.cardview_play_btn);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.cardview_layout);

            itemView.getContext().getApplicationContext();
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

}
