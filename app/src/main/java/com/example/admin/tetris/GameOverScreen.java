package com.example.admin.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.view.View;
import android.widget.RelativeLayout;

public class GameOverScreen extends AppCompatActivity implements View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        GameOverView gv = new GameOverView(this);
        layout.setOnTouchListener(this);
        layout.addView(gv);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            return true;
        }
        return false;
    }

    static class GameOverView extends View {

        public GameOverView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            @SuppressLint("DrawAllocation") Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setTextSize(200);
            canvas.drawText("Game Over", 50, 400, p);
            p.setTextSize(75);
            canvas.drawText("Touch!!!", 450, 800, p);

        }
    }
}
