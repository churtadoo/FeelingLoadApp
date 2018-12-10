package edu.uoc.churtado.feelingloadapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.PlayerRPE;

public class PlayerRPEAdapter extends RecyclerView.Adapter<PlayerRPEAdapter.ViewHolder> {
    private final List<PlayerRPE> playerRPES;

    public PlayerRPEAdapter(List<PlayerRPE> playerRPES){
        this.playerRPES = playerRPES;
    }

    @NonNull
    @Override
    public PlayerRPEAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PlayerRPEAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        // Get the type of view to show
        return R.layout.playerrpe_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayerRPEAdapter.ViewHolder holder, int position) {
        //Get PlayerRPE to show
        holder.item = playerRPES.get(position);
        //Get and set background color for list element
        holder.mView.setBackgroundResource(getBackgroundColor(position));
        //Set player email and rpe
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView playerEmail;
        final TextView rpe;
        PlayerRPE item;

        ViewHolder(View view) {
            super(view);
            mView = view;
            playerEmail = view.findViewById(R.id.player_email);
            rpe = view.findViewById(R.id.rpe);
        }
    }
}
