package example.jforce.todoapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import example.jforce.todoapplication.R;
import example.jforce.todoapplication.interfaces.finishClickListener;
import example.jforce.todoapplication.model.taskHelper;

/**
 * Created by harshal on 5/18/2019.
 */

public class RvTaskAdapter extends RecyclerView.Adapter<RvTaskAdapter.ViewMaker> {
    private ArrayList<taskHelper> taskList;
    private finishClickListener listener;

    public RvTaskAdapter(ArrayList<taskHelper> taskList, finishClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @Override
    public ViewMaker onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new ViewMaker(v);
    }

    @Override
    public void onBindViewHolder(ViewMaker holder, final int position) {
        holder.text.setText(taskList.get(position).getTask());
        holder.textDate.setText("Date : " + formattedDate(taskList.get(position).getDate()));
        if (taskList.get(position).getStaus().equals("1")) {
            holder.finish.setVisibility(View.GONE);

        } else {
            holder.finish.setVisibility(View.VISIBLE);
            holder.finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.finishClick(taskList.get(position).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class ViewMaker extends RecyclerView.ViewHolder {
        TextView text, textDate;
        Button finish;

        ViewMaker(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            finish = itemView.findViewById(R.id.finish);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }

    String formattedDate(String dateString) {
        String final_date = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, dd MMM yyyy");
        final_date = fmtOut.format(date);
        return final_date;
    }

}
