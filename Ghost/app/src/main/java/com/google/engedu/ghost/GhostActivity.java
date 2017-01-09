/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String TAG = "GhostActivity";

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String KEY_USER_TURN = "keyUserTurn";
    private static final String KEY_CURRENT_WORD = "keyCurrentWord";
    private static final String KEY_SAVED_STATUS = "keySavedStatus";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String currentWord = "";

    private boolean won = false;

    @Override
    public void onSaveInstanceState(Bundle outState){
        TextView label = (TextView) findViewById(R.id.gameStatus);
        super.onSaveInstanceState(outState);
        outState.putString("key_currWord", currentWord);
        outState.putString("key_status", label.getText().toString());
        outState.putBoolean("key_won", won);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        if(savedInstanceState!=null){
            currentWord = savedInstanceState.getString("key_currWord");
            String message = savedInstanceState.getString("key_status");
            TextView label = (TextView) findViewById(R.id.gameStatus);
            won = savedInstanceState.getBoolean("key_won");
            TextView ghostText = (TextView) findViewById(R.id.ghostText);
            label.setText(message);
            ghostText.setText(currentWord);
        }
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
            // Initialize your dictionary from the InputStream.
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        if(savedInstanceState==null) onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(won) return super.onKeyUp(keyCode,event);
        int key = event.getUnicodeChar();
        //Log.d("key pressed", Character.toString((char)key));
        if(key<65 || (key>90 && key<97) || key>122){
            return super.onKeyUp(keyCode, event);
        }else{
            currentWord+= Character.toString((char)key);
            //update veiw
            TextView ghostText = (TextView) findViewById(R.id.ghostText);
            ghostText.setText(currentWord);
            //computer turn
            computerTurn();
            return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        Restart();
        return true;
    }

    private void computerTurn() {
        if(won) return;
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        if(currentWord.length()>=4 && dictionary.isWord(currentWord)) {
            label.setText("You have created a word, the computer wins.");
            won=true;
            return;
        }else{
            String word = dictionary.getAnyWordStartingWith(currentWord);
            if(word==null){
                label.setText("There are no valid words beginning with that fragment, the computer wins");
                won=true;
                return;
            }else{
                String letter = word.substring(currentWord.length(),currentWord.length()+1);
                currentWord+=letter;
                TextView ghostText = (TextView) findViewById(R.id.ghostText);
                ghostText.setText(currentWord);
            }
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    public void Challenge(View view){
        if(won) return;
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView ghostText = (TextView) findViewById(R.id.ghostText);
        if(currentWord.length()>=4 && dictionary.isWord(currentWord)){
            label.setText("The computer has completed a word, you win!");
            won=true;
        }else if(dictionary.getAnyWordStartingWith(currentWord)==null) {
            label.setText("This is not a valid prefix, you win!");
            won=true;
        }else{
            String word = dictionary.getAnyWordStartingWith(currentWord);
            label.setText("This is a valid prefix, the computer wins.");
            won=true;
            ghostText.setText(word);
        }
    }

    public void Restart(View view){
        Restart();
        //return;
    }

    public void Restart(){
        won=false;
        currentWord="";
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
    }
}
