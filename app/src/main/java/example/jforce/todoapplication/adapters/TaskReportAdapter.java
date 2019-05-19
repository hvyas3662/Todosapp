package example.jforce.todoapplication.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import example.jforce.todoapplication.R;
import example.jforce.todoapplication.model.TaskReportHelper;

public class TaskReportAdapter extends RecyclerView.Adapter<TaskReportAdapter.ViewMaker> {
    private ArrayList<TaskReportHelper> taskReportList;

    public TaskReportAdapter(ArrayList<TaskReportHelper> taskReportList) {
        this.taskReportList = taskReportList;
    }

    @NonNull
    @Override
    public ViewMaker onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_report_item, viewGroup, false);
        return new ViewMaker(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMaker holder, int i) {
        holder.tvDate.setText(formattedDate(taskReportList.get(i).getDate()));
        holder.tvPending.setText(taskReportList.get(i).getPending() + "");
        holder.tvFinished.setText(taskReportList.get(i).getFinished() + "");
        holder.tvTotal.setText(taskReportList.get(i).getTotal() + "");
    }

    @Override
    public int getItemCount() {
        return taskReportList.size();
    }


    class ViewMaker extends RecyclerView.ViewHolder {
        TextView tvDate, tvPending, tvFinished, tvTotal;

        ViewMaker(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvPending = itemView.findViewById(R.id.tvPending);
            tvFinished = itemView.findViewById(R.id.tvFinished);
            tvTotal = itemView.findViewById(R.id.tvTotal);
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
