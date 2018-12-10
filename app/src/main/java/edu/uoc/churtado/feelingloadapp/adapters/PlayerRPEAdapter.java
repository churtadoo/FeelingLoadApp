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
import edu.uoc.churtado.feelingloadapp.models.PlayerRPE;
import edu.uoc.churtado.feelingloadapp.models.Training;

import static edu.uoc.churtado.feelingloadapp.activities.TrainingDetailsActivity.ARG_ITEM_ID;

public class PlayerRPEAdapter extends RecyclerView.Adapter<PlayerRPEAdapter.ViewHolder> {
    private final List<PlayerRPE> playerRPES;

    public PlayerRPEAdapter(List<PlayerRPE> playerRPES){
        this.playerRPES = playerRPES;
    }

    @Override
    public PlayerRPEAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PlayerRPEAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.playerrpe_list_item;
    }

    @Override
    public void onBindViewHolder(final PlayerRPEAdapter.ViewHolder holder, int position) {
        holder.item = playerRPES.get(position);
        holder.mView.setBackgroundResource(getBackgroundColor(position));
        holder.playerEmail.setText(holder.item.getPlayerEmail());
        holder.rpe.setText(String.valueOf(holder.item.getRPE()));
    }

    private int getBackgroundColor(int position){
        if(position%2 == 0) return R.color.white;
        return R.color.tableRow;
    }

    @Override
    public int getItemCount() {
        if (playerRPES != null) {
            return playerRPES.size() > 0 ? playerRPES.size() : 0;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        final TextView playerEmail;
        final TextView rpe;
        public PlayerRPE item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            playerEmail = (TextView) view.findViewById(R.id.player_email);
            rpe = (TextView) view.findViewById(R.id.rpe);
        }
    }
}
