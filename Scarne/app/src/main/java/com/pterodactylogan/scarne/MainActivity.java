package com.pterodactylogan.scarne;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public int userOverallScore = 0;
    public int userTurnScore = 0;
    public int compOverallScore = 0;
    public int compTurnScore = 0;
    public int currRoll = 0;
    Random rand = new Random();

    public void Reset(View view){
        userTurnScore=0;
        userOverallScore=0;
        compOverallScore=0;
        compTurnScore=0;
        currRoll=0;
        scoreDisplay();
    }

    public void scoreDisplay(){
        String display = "Your score: " + userOverallScore + " Computer score: " + compOverallScore;
        if(userTurnScore!=0){
            display+=" Turn score: "+userTurnScore;
        }else if(compTurnScore!=0){
            display+=" Turn score: "+compTurnScore;
        }
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText(display);
    }

    public void Roll(View veiw){
        //Log.d("roll test", "rolled");
        RollDisplay();
        if(currRoll==1) {
            userTurnScore = 0;
            scoreDisplay();
            //computerTurn();
        }else{
            userTurnScore+=currRoll;
            scoreDisplay();
        }
    }

    public void Hold(View view){
        userOverallScore+=userTurnScore;
        userTurnScore=0;
        scoreDisplay();
        //computerTurn();
    }

    public void RollDisplay(){
        currRoll = rand.nextInt(6)+1;
        if(currRoll == 1){
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
        }else if(currRoll == 2){
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice2));
        }else if(currRoll==3){
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice3));
        }else if(currRoll==4){
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice4));
        }else if (currRoll==5){
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice5));
        }else {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice6));
        }
    }
    
    public void computerTurn(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
