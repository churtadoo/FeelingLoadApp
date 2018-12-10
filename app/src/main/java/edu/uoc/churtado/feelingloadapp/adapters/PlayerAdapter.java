package edu.uoc.churtado.feelingloadapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.activities.PlayerDetailsActivity;
import edu.uoc.churtado.feelingloadapp.models.Player;

import static edu.uoc.churtado.feelingloadapp.activities.PlayerDetailsActivity.ARG_ITEM_ID;

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
        holder.mView.setBackgroundResource(getBackgroundColor(position));
        if(holder.item.getUrlPhoto() != null && !holder.item.getUrlPhoto().isEmpty()){
            Picasso.get().load(holder.item.getUrlPhoto()).into(holder.playerPhoto);
        }
        holder.playerName.setText(holder.item.getName());
        holder.playerSurname.setText(holder.item.getSurname());

        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                Context context = v.getContext();
                Intent intent = new Intent(context, PlayerDetailsActivity.class);
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
