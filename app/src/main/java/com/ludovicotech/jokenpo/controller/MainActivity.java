package com.ludovicotech.jokenpo.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ludovicotech.jokenpo.model.CpuPlayer;
import com.ludovicotech.jokenpo.model.Player;
import com.ludovicotech.jokenpo.adapter.PlayerAdapter;
import com.ludovicotech.jokenpo.R;
import com.ludovicotech.jokenpo.dao.DbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DbHelper mDatabase;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private PlayerAdapter mAdapter;
    private GameActivity gameActivity;
    public View subView;
    public Player player;
    public CpuPlayer cpu;
    public RecyclerView playerView;
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        playerView.setLayoutManager(linearLayoutManager);
        playerView.setHasFixedSize(true);
        mDatabase = new DbHelper(this);
        allPlayers = mDatabase.listPlayers();
        dialogInstruct();


        if (allPlayers.size() > 0) {
            playerView.setVisibility(View.VISIBLE);
            mAdapter = new PlayerAdapter(this, allPlayers);
            playerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });
    }

    public void navigateForGame() {
        Intent intent = new Intent(subView.getContext(), GameActivity.class);
        intent.putExtra("NAME_PLAYER", name);
        startActivity(intent);
    }

    private void dialogInstruct() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Iniciar");
        builder.setMessage("Para iniciar um novo jogo clique no botão no canto inferior direito da tela");
        builder.create();
        builder.setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        subView = inflater.inflate(R.layout.add_player, null);

        final EditText nameField = subView.findViewById(R.id.enter_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Iniciar novo jogo");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("INICIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = nameField.getText().toString();
                int points = 0;

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Ops!! Algo deu errado. Verifique o valor inserido.", Toast.LENGTH_LONG).show();
                    startActivity(getIntent());
                } else {
                    Player newPlayer = new Player(name, points);
                    mDatabase.addPlayers(newPlayer);
                    finish();

                    navigateForGame();
                }

            }
        });
        builder.setNegativeButton("SAIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Você encerrou o inicio do Jogo", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();

        for (int i = 0; i < allPlayers.size(); i++ ) {
            mDatabase.updatePlayers(allPlayers.get(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null) {
            mDatabase.close();
        }
    }
}
