package com.ludovicotech.jokenpo.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.ludovicotech.jokenpo.controller.GameActivity;
import com.ludovicotech.jokenpo.controller.MainActivity;
import com.ludovicotech.jokenpo.holder.PlayerViewHolder;
import com.ludovicotech.jokenpo.R;
import com.ludovicotech.jokenpo.dao.DbHelper;
import com.ludovicotech.jokenpo.model.Player;

import java.util.ArrayList;




public class PlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Player> listPlayers;
    private ArrayList<Player> mArrayList;

    private DbHelper mDatabase;

    private MainActivity main;
    private GameActivity game;

    public PlayerAdapter(Context context, ArrayList<Player> listPlayers) {
        this.context = context;
        this.listPlayers = listPlayers;
        this.mArrayList = listPlayers;
        mDatabase = new DbHelper(context);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_list, viewGroup, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder playerViewHolder, int i) {
        final Player players = listPlayers.get(i);
        main = new MainActivity();

        playerViewHolder.name.setText(players.getName());
        playerViewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("NAME_PLAYER", players.getName());
                context.startActivity(intent);
            }
        });

        playerViewHolder.editPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(players);
            }
        });

        playerViewHolder.deletePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                mDatabase.deletePlayer(players.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlayers.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    listPlayers = mArrayList;
                } else {
                    ArrayList<Player> filteredList = new ArrayList<>();

                    for (Player players : mArrayList) {
                        if (players.getName().toLowerCase().contains(charString)) {
                            filteredList.add(players);
                        }
                    }
                    listPlayers = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listPlayers;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listPlayers = (ArrayList<Player>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void editTaskDialog(final Player players){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_player, null);

        final EditText nameField = subView.findViewById(R.id.enter_name);

        if(players != null){
            nameField.setText(players.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Editar nome");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int id = players.getId();
                final String name = nameField.getText().toString();
                final int playerWins = players.getPoints();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Algo deu errado. Check as informações inseridas.", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updatePlayers(new Player(id, name, playerWins));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Atividade cancelada", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

}
