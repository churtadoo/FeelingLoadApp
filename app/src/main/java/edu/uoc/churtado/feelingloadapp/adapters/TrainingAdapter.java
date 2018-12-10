package edu.uoc.churtado.feelingloadapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import edu.uoc.churtado.feelingloadapp.models.Training;

import static edu.uoc.churtado.feelingloadapp.activities.TrainingDetailsActivity.ARG_ITEM_ID;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {
    private final List<Training> trainings;

    public TrainingAdapter(List<Training> trainings) {
        this.trainings = trainings;
    }

    @NonNull
    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new TrainingAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.trainings_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrainingAdapter.ViewHolder holder, int position) {
        //Get training to show
        holder.item = trainings.get(position);
        //Get and set background color for list elements
        int backgroundColor = getBackgroundColor(position);
        holder.mView.setBackgroundResource(backgroundColor);
        //Set date and time formatted in view
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        holder.trainingDate.setText(dateFormat.format(trainings.get(position).getDate()));
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.UK);
        holder.trainingHour.setText(hourFormat.format(trainings.get(position).getDate()));

        //Check if all players in training have registered RPE
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
                //If list item is clicked, go to TrainingDetailsActivitys
                int currentPos = (int) v.getTag();
                Context context = v.getContext();
                Intent intent = new Intent(context, TrainingDetailsActivity.class);
                intent.putExtra(ARG_ITEM_ID, currentPos);
                //Start the new activity
                context.startActivity(intent);
            }
        });
    }

    private int getBackgroundColor(int position){
        if(position%2 == 0) return R.color.white;
        return R.color.tableRow;
    }

    @Override
    public int getItemCount() {
        if (trainings != null) {
            return trainings.size() > 0 ? trainings.size() : 0;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView trainingDate;
        final TextView trainingHour;
        final ImageView rpeCompleted;
        Training item;

        ViewHolder(View view) {
            super(view);
            mView = view;
            trainingDate = view.findViewById(R.id.training_date);
            trainingHour = view.findViewById(R.id.training_hour);
            rpeCompleted = view.findViewById(R.id.rpe_completed);
        }
    }
}
