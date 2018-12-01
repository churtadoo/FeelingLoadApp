package edu.uoc.churtado.feelingloadapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;

public class PlayerTrainingAdapter extends ArrayAdapter<PlayerTraining> {
    private Context context;
    private ArrayList<PlayerTraining> playerTrainings;

    public PlayerTrainingAdapter(Context context, ArrayList<PlayerTraining> playerTrainings){
        super(context, R.layout.playertraining_list_item);
        this.context = context;
        this.playerTrainings = playerTrainings;
    }

    @Override
    public int getCount(){
        return playerTrainings.size();
    }

    @Override
    public PlayerTraining getItem(int position){
        return playerTrainings.get(position);
    }

    @Override
    public long getItemId(int position){
        return playerTrainings.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;

        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.playertraining_list_item, parent, false);
            viewHolder.trainingDate = (TextView) view.findViewById(R.id.training_date);
            viewHolder.rpeRegistered = (TextView) view.findViewById(R.id.rpe_registered);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        // Set text with the item name and item id
        viewHolder.trainingDate.setText(String.valueOf(playerTrainings.get(position).getDate()));
        viewHolder.rpeRegistered.setText(String.valueOf(playerTrainings.get(position).getRPE()));

        return view;
    }

    static class ViewHolder {
        protected TextView trainingDate;
        protected TextView rpeRegistered;
    }
}
