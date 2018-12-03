package edu.uoc.churtado.feelingloadapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private final List<Player> players;

    public PlayerAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PlayerAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.players_list_item;
    }

    @Override
    public void onBindViewHolder(final PlayerAdapter.ViewHolder holder, int position) {
        holder.item = players.get(position);
        //TODO:photo
        holder.playerName.setText(players.get(position).getName());
        holder.playerSurname.setText(players.get(position).getSurname());

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
        if (players != null) {
            return players.size() > 0 ? players.size() : 0;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        final ImageView playerPhoto;
        final TextView playerName;
        final TextView playerSurname;
        public Player item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            playerPhoto = (ImageView) view.findViewById(R.id.player_photo);
            playerName = (TextView) view.findViewById(R.id.player_name);
            playerSurname = (TextView) view.findViewById(R.id.player_surname);
        }
    }
}
