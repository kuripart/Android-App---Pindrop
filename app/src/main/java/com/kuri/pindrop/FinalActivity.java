package com.kuri.pindrop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {


    TextView highScoreText;
    Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        highScoreText = (TextView) findViewById(R.id.highScoreText);
        restartButton = (Button) findViewById(R.id.restartButton);
        String highScore = getIntent().getStringExtra("HIGH_SCORE");
        highScoreText.setText("YOUR HIGH SCORE: " + highScore);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToGame = new Intent(FinalActivity.this, MainActivity.class);
                startActivity(goBackToGame);
            }
        });
    }
}
