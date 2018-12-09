package edu.uoc.churtado.feelingloadapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.activities.TrainingDetailsActivity;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.Training;

import static edu.uoc.churtado.feelingloadapp.activities.TrainingDetailsActivity.ARG_ITEM_ID;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {
    private final List<Training> trainings;

    public TrainingAdapter(List<Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new TrainingAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.trainings_list_item;
    }

    @Override
    public void onBindViewHolder(final TrainingAdapter.ViewHolder holder, int position) {
        holder.item = trainings.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        holder.trainingDate.setText(dateFormat.format(trainings.get(position).getDate()));
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.UK);
        holder.trainingHour.setText(hourFormat.format(trainings.get(position).getDate()));

        if(trainings.get(position).allPlayersWithRPERegistered()){
            holder.rpeCompleted.setImageResource(android.R.drawable.checkbox_on_background);
        }
        else {
            holder.rpeCompleted.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                Context context = v.getContext();
                Intent intent = new Intent(context, TrainingDetailsActivity.class);
                intent.putExtra(ARG_ITEM_ID, currentPos);
                //Start the new activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (trainings != null) {
            return trainings.size() > 0 ? trainings.size() : 0;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        final TextView trainingDate;
        final TextView trainingHour;
        final ImageView rpeCompleted;
        public Training item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            trainingDate = (TextView) view.findViewById(R.id.training_date);
            trainingHour = (TextView) view.findViewById(R.id.training_hour);
            rpeCompleted = (ImageView) view.findViewById(R.id.rpe_completed);
        }
    }
}
