package edu.uoc.churtado.feelingloadapp.adapters;
import android.content.Context;
import android.content.Intent;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.playertraining_list_item;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = playerTrainings.get(position);
        holder.mView.setBackgroundResource(getBackgroundColor(position));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
        holder.trainingDate.setText(dateFormat.format(playerTrainings.get(position).getDate()));
        holder.rpeRegistered.setChecked(playerTrainings.get(position).HasRegisteredRPE());

        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                Context context = v.getContext();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        final TextView trainingDate;
        final CheckBox rpeRegistered;
        public PlayerTraining item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            trainingDate = (TextView) view.findViewById(R.id.training_date);
            rpeRegistered = (CheckBox) view.findViewById(R.id.rpe_registered);
        }
    }
}
