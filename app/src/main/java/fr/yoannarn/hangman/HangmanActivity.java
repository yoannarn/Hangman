package fr.yoannarn.hangman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HangmanActivity extends AppCompatActivity {

    private List<Word> words = new ArrayList<>();
    private Word wordToFound;
    private TextView wordToDiscover;
    private TextView lifeView;
    private int life;

    public static final int WINNER = 0;
    public static final int LOOSER = 1;

    public static final int MAX_LIFE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);

        wordToDiscover = findViewById(R.id.wordToDiscover);
        lifeView = findViewById(R.id.life);

        startGame();
    }

    protected void startGame(){
        chooseRandomWord();
        wordToDiscover.setText(wordToFound.getWordWithDiscoverLetters());

        createKeyboard();

        life = MAX_LIFE;
        lifeView.setText("Life : "+life);

    }


    private void readWordsFile(){
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.words));
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            words.add(new Word(word));
        }
        scanner.close();
    }

    private void chooseRandomWord(){
        readWordsFile();
        Random rand = new Random();
        int randomNumber = rand.nextInt(words.size()-1);
        wordToFound = words.get(randomNumber);
    }

    private void createKeyboard() {

        TableRow tableRow1 =  findViewById(R.id.tableRow1);
        tableRow1.removeAllViewsInLayout();

        TableRow tableRow2 =  findViewById(R.id.tableRow2);
        tableRow2.removeAllViewsInLayout();

        TableRow tableRow3 =  findViewById(R.id.tableRow3);
        tableRow3.removeAllViewsInLayout();

        for (char letter = 'a'; letter <= 'z'; letter++) {
            final Button button = new Button(this);
            button.setText(Character.toString(letter));
            button.setId(letter);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.weight = 1;
            button.setLayoutParams(params);
            if (letter <= 'i') {
                tableRow1.addView(button);
            } else if (letter <= 'r')
                tableRow2.addView(button);
            else {
                tableRow3.addView(button);
            }
            button.setOnClickListener(listener);
        }

    }


    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Button button = (Button) findViewById(v.getId());
            Character letter = button.getText().charAt(0);
            if(wordToFound.getLetterToFound().contains(letter)){
                wordToFound.getLetterToFound().remove(letter);
                button.setBackgroundColor(Color.GREEN);
                wordToDiscover.setText(wordToFound.getWordWithDiscoverLetters());
            }else{
                button.setBackgroundColor(Color.RED);
                if(life>0){
                    life--;
                    lifeView.setText("Life : "+life);
                }
                if(life == 0)
                    showEndGameDialog(LOOSER);
            }
            button.setClickable(false);
            if(wordToFound.getLetterToFound().isEmpty()){
                showEndGameDialog(WINNER);
            }
        }
    };

    public void showEndGameDialog(int type ){

        final AlertDialog alertDialog = new AlertDialog.Builder(HangmanActivity.this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_endgame, null);
        alertDialog.setView(dialogView);

        TextView msgPlayer = dialogView.findViewById(R.id.playerMsg);
        ImageView imageView = dialogView.findViewById(R.id.smileyView);
        Button restartButton = dialogView.findViewById(R.id.restartButton);

        if(type == WINNER){
            msgPlayer.setText("Winner !!");
            imageView.setImageResource(R.drawable.happy_smiley);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        }else{
            msgPlayer.setText("Looser !!");
            imageView.setImageResource(R.drawable.nothappy_smiley);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        }

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startGame();
            }
        });

        alertDialog.show();
    }

}