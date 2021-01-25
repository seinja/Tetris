package com.example.admin.tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        Button easy = (Button) findViewById(R.id.easyButton);
        Button normal = (Button) findViewById(R.id.normalButton);
        Button hard = (Button) findViewById(R.id.hardButton);


        trowIntent(easy,normal,hard);

    }

    private void trowIntent(Button easy, Button normal, Button hard){
        easy.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("speed",200);
            startActivity(intent);
        });
        normal.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("speed", 150);
            startActivity(intent);
        });
        hard.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("speed",75);
            startActivity(intent);
        });
    }
}