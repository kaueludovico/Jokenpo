package com.ludovicotech.jokenpo.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ludovicotech.jokenpo.model.CpuPlayer;
import com.ludovicotech.jokenpo.model.Player;
import com.ludovicotech.jokenpo.R;
import com.ludovicotech.jokenpo.dao.DbHelper;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    String cpuMove;
    String playerMove;
    Player players;
    CpuPlayer cpuPlayer;
    public int playerPoint;
    public int cpuPoint;
    private DbHelper db = new DbHelper(this);;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    public View subView;
    ImageButton cpuJo, cpuKen, cpuPo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        cpuPlayer = new CpuPlayer(0);
        players = new Player(0);

        cpuJo = findViewById(R.id.cpuJo);
        cpuKen = findViewById(R.id.cpuKen);
        cpuPo = findViewById(R.id.cpuPo);

        cpuJo.setEnabled(false);
        cpuKen.setEnabled(false);
        cpuPo.setEnabled(false);


        allPlayers = db.listPlayers();
        fetchNameRegistered();
        switchPlaying();
        dialogInstruct();
    }

    private void dialogInstruct() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Teste uma vez antes de jogar!");
        builder.setMessage("Ao selecionar uma das imagens a baixo, sua jogada é executada e o jogo é finalizado automáticamente.");
        builder.create();
        builder.setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void fetchNameRegistered() {
        String namePlayer = getIntent().getStringExtra("NAME_PLAYER");
        TextView text = findViewById(R.id.name);
        text.setText(namePlayer);
    }

    public void switchPlaying() {
        final ImageButton jo = findViewById(R.id.jo);
        final ImageButton ken = findViewById(R.id.ken);
        final ImageButton po = findViewById(R.id.po);

        jo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonPlayer(v, 320,-400);
                playerMove = "jo";
                setResult(playerMove, randomButtonCPU());
            }
        });

        ken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonPlayer(v, 0, -400);
                playerMove = "ken";
                setResult(playerMove, randomButtonCPU());
            }
        });

        po.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonPlayer(v, -320, -400);
                playerMove = "po";
                setResult(playerMove, randomButtonCPU());
            }
        });
    }

    public void moveButtonPlayer(final View view, int x, int y) {
        TranslateAnimation anim = new TranslateAnimation(x, 0, y, 0);
        anim.setDuration(3000);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) { }
        });
        view.startAnimation(anim);
    }

    public String randomButtonCPU() {


        ImageButton[] listButtons = {cpuJo, cpuKen, cpuPo};

        Random random = new Random();
        ImageButton randomBtn = listButtons[random.nextInt(listButtons.length)];
        randomBtn.performClick();

        cpuJo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonCPU(v, -320, 400);
                cpuMove = "jo";
            }
        });

        cpuKen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonCPU(v, 0, 400);
                cpuMove = "ken";
            }
        });

        cpuPo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonCPU(v, 320, 400);
                cpuMove = "po";
            }
        });

        return cpuMove;
    }

    public void moveButtonCPU(final View view, int x, int y) {
        TranslateAnimation anim = new TranslateAnimation(x, 0, y, 0);
        anim.setDuration(3000);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) { }
        });
        view.startAnimation(anim);
    }

    private void setResult(String playerMove, String cpuMove) {
        if (((playerMove == "jo" || playerMove == "ken")
                || playerMove == "po") && cpuMove == playerMove) {
            resultGame("Você Empatou com o outro jogador!");
        } else {
            if (playerMove == "jo" && cpuMove == "po") {
                resultGame("Você Venceu!");
                playerPoint += 1;
                players.setPoints(playerPoint);
            } else if (playerMove == "jo" && cpuMove == "ken") {
                resultGame("A CPU Venceu!");
                cpuPoint += 1;
                cpuPlayer.setPoints(cpuPoint);
            } else if (playerMove == "ken" && cpuMove == "jo" ) {
                resultGame("Você Venceu!");
                playerPoint += 1;
                players.setPoints(playerPoint);
            } else if (playerMove == "ken" && cpuMove == "po") {
                resultGame("A CPU Venceu!");
                cpuPoint += 1;
                cpuPlayer.setPoints(cpuPoint);
            } else if (playerMove == "po" && cpuMove == "ken") {
                resultGame("Você Venceu!");
                playerPoint += 1;
                players.setPoints(playerPoint);
            } else if (playerMove == "po" && cpuMove == "jo") {
                resultGame("A CPU Venceu!");
                cpuPoint += 1;
                cpuPlayer.setPoints(cpuPoint);
            }
        }
    }

    public void resultGame(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Jogo Encerrado");
        builder.setMessage(text);
        builder.create();

        builder.setNegativeButton("Encerrar partida", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setPositiveButton("Jogar Novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        updateAllPlayers();
    }

    private void updateAllPlayers() {
        for (int i = 0; i < allPlayers.size(); i++) {
            db.updatePlayers(allPlayers.get(i));
        }
    }
}
