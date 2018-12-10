package edu.uoc.churtado.feelingloadapp.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.activities.PlayerTrainingDetailsActivity;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;
import static edu.uoc.churtado.feelingloadapp.activities.PlayerTrainingDetailsActivity.ARG_ITEM_ID;

public class PlayerTrainingAdapter extends RecyclerView.Adapter<PlayerTrainingAdapter.ViewHolder> {
    private final List<PlayerTraining> playerTrainings;

    public PlayerTrainingAdapter(List<PlayerTraining> playerTrainings) {
        this.playerTrainings = playerTrainings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.playertraining_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //Get PlayerTraining to show
        holder.item = playerTrainings.get(position);
        //Get and set background color for list element
        holder.mView.setBackgroundResource(getBackgroundColor(position));
        //Set training date and check if player has registered rpe
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
        holder.trainingDate.setText(dateFormat.format(playerTrainings.get(position).getDate()));
        holder.rpeRegistered.setChecked(playerTrainings.get(position).HasRegisteredRPE());

        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                Context context = v.getContext();
                //If element is clicked, go to PlayerTrainingDetailsActivity
                Intent intent = new Intent(context, PlayerTrainingDetailsActivity.class);
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
        if (playerTrainings != null) {
            return playerTrainings.size() > 0 ? playerTrainings.size() : 0;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView trainingDate;
        final CheckBox rpeRegistered;
        PlayerTraining item;

        ViewHolder(View view) {
            super(view);
            mView = view;
            trainingDate = view.findViewById(R.id.training_date);
            rpeRegistered = view.findViewById(R.id.rpe_registered);
        }
    }
}
