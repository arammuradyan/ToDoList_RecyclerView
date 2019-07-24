package com.example.todolist10;



import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter <TaskAdapter.TaskItemHolder> {
    private List<Task> taskList=new ArrayList<>();
    private Listener onTaskClickListener;
    private final String Tag="tag";




    public TaskAdapter(Listener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setList(List<Task> list){
        taskList.addAll(list);

    }
    @NonNull
    @Override
    public TaskAdapter.TaskItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTaskClickListener.onTaskItemClick((int)v.getTag());
            }
        });
        return  new TaskItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskItemHolder taskItemHolder, int position) {
        Task task = taskList.get(position);
        taskItemHolder.bind(task);
        taskItemHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public interface Listener {
        void onTaskItemClick(int i);
    }

    public void delete(int index) {
        taskList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index,getItemCount());
    }

    public void add(Task task) {
        taskList.add(task);
        notifyItemInserted(getItemCount() -1);
   }

    public void save(int index, Task task) {
       Task changedTask = taskList.get(index);
       changedTask.setType(task.getType());
       changedTask.setName(task.getName());
       changedTask.setDescription(task.getDescription());
       changedTask.setImgUri(task.getImgUri());
       notifyItemChanged(index);
    }


    static public class TaskItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDescription;
        RatingBar itemType;
        ImageView itemImg;
        View view_background;
        private Utils utils=new Utils();

        public TaskItemHolder(@NonNull View itemView) {
            super(itemView);
            view_background = itemView.findViewById(R.id.view_rv);
            itemName = itemView.findViewById(R.id.tv_name_rv);
            itemDescription = itemView.findViewById(R.id.tv_description_rv);
            itemType = itemView.findViewById(R.id.tv_type_rv);
            itemImg = itemView.findViewById(R.id.imgView_rv);
        }

        public void bind(Task task) {
            itemName.setText(task.getName());
            itemDescription.setText(task.getDescription());
            itemType.setRating(task.getType());
            itemType.setEnabled(false);
            Uri uri = Uri.parse(task.getImgUri());

            itemImg.setImageURI(uri);
            utils.setBitmap(itemView.getContext(),itemImg,uri,300,600);
            utils.onBackgroundColorSet(task, view_background);
        }

    }
}