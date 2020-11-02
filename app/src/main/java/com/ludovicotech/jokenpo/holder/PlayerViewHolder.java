package com.ludovicotech.jokenpo.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ludovicotech.jokenpo.R;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public ImageView deletePlayer;
    public ImageView editPlayer;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.player_name);
        deletePlayer = itemView.findViewById(R.id.delete_player);
        editPlayer = itemView.findViewById(R.id.edit_player);
    }
}
