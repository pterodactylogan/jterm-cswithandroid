package com.pterodactylogan.scarne;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    public int userOverallScore = 0;
    public int userTurnScore = 0;
    public int compOverallScore = 0;
    public int compTurnScore = 0;
    public int currRoll = 0;
    Random rand = new Random();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void Reset(View view) {
        userTurnScore = 0;
        userOverallScore = 0;
        compOverallScore = 0;
        compTurnScore = 0;
        currRoll = 0;
        scoreDisplay("your");
    }

    public void scoreDisplay(String player) {
        String display = "Your score: " + userOverallScore + " Computer score: " + compOverallScore;
        if (userTurnScore != 0) {
            display += " Turn score: " + userTurnScore;
        } else if (compTurnScore != 0) {
            display += " Turn score: " + compTurnScore;
        }
        display += "It is " + player + " turn!"
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText(display);
    }

    public void Roll(View veiw) {
        //Log.d("roll test", "rolled");
        RollDisplay();
        if (currRoll == 1) {
            userTurnScore = 0;
            scoreDisplay("the computer's");
            computerTurn();
        } else {
            userTurnScore += currRoll;
            scoreDisplay("your");
        }
    }

    public void Hold(View view) {
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        scoreDisplay("the computer's");
        computerTurn();
    }

    public void RollDisplay() {
        currRoll = rand.nextInt(6) + 1;
        if (currRoll == 1) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
        } else if (currRoll == 2) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice2));
        } else if (currRoll == 3) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice3));
        } else if (currRoll == 4) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice4));
        } else if (currRoll == 5) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice5));
        } else {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice6));
        }
    }

    final Handler handler = new Handler();

    public void computerTurn() {
        //disable buttons
        Button hold = (Button) findViewById(hold);
        hold.setEnabled(false);
        Button roll = (Button) findViewById(roll);
        roll.setEnabled(false);
        Button reset = (Button) findViewById(reset);
        reset.setEnabled(false);

        compTurn.run();

        //enable buttons
        Button hold = (Button) findViewById(hold);
        hold.setEnabled(true);
        Button roll = (Button) findViewById(roll);
        roll.setEnabled(true);
        Button reset = (Button) findViewById(reset);
        reset.setEnabled(true);
        return;

    }

    final Runnable compTurn = new Runnable() {
        @Override
        public void run() {
            //hold if score is 20 or over
            if (compTurnScore >= 20) {
                compOverallScore += compTurnScore;
                compTurnScore = 0;
                scoreDisplay("your");
                return;
            } else {
                RollDisplay();
                if (currRoll == 1) {
                    compTurnScore = 0;
                    scoreDisplay("your");
                    return;
                } else {
                    compTurnScore += currRoll;
                    scoreDisplay("the computer's");
                    handler.postDelayed(this, 1000);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
