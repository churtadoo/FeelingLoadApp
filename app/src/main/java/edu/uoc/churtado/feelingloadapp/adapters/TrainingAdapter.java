package edu.uoc.churtado.feelingloadapp.adapters;

import android.content.Context;
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
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.Training;

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

        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                Context context = v.getContext();
                //Intent intent = new Intent(context, PlayerTrainingDetailsActivity.class);
                //intent.putExtra(ARG_ITEM_ID, currentPos);
                //Start the new activity
                //context.startActivity(intent);
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
        public Training item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            trainingDate = (TextView) view.findViewById(R.id.training_date);
            trainingHour = (TextView) view.findViewById(R.id.training_hour);
        }
    }
}
