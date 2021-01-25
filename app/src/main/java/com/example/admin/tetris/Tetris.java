package com.example.admin.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ViewConstructor")
public class Tetris extends View implements View.OnClickListener {

    private final MediaPlayer mediaPlayer;
    private final GameBoard gameBoard;
    private final MainActivity mainActivity;
    private Timer timer = new Timer();
    private final Random random = new Random();
    private final ArrayList<Piece> pieceList;
    private final TextView currentLevelTextView;
    private final TextView highscoreLevelTextView;
    private final TextView currentPunkteTextView;
    private final Points points;
    private final int score = 10;
    private int timerPeriod = 155;
    private int level = 0;
    private Bundle argumnts;

    @SuppressLint("ClickableViewAccessibility")
    public Tetris(Context context, GameBoard gameBoard) {
        super(context);

        this.mainActivity = (MainActivity) context;
        this.gameBoard = gameBoard;
        pieceList = gameBoard.getPieceList();
        mediaPlayer = mainActivity.getMediaPlayer();
        points = new Points(context);

        currentLevelTextView = mainActivity.getCurrentLevelTextView();
        highscoreLevelTextView = mainActivity.getHighscoreLevelTextView();
        currentPunkteTextView = mainActivity.getPointTextView();

        currentLevelTextView.append("0");
        currentPunkteTextView.append("0");
        highscoreLevelTextView.append("" + points.loadHighscore());

        ImageButton rotateButton = mainActivity.getRotateButton();
        ImageButton rightButton = mainActivity.getRightButton();
        ImageButton downButton = mainActivity.getDownButton();
        ImageButton leftButton = mainActivity.getLeftButton();

        rotateButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        downButton.setOnClickListener(this);
        downButton.setOnTouchListener((View view, MotionEvent motionEvent) -> {
            gameBoard.fastDrop(gameBoard.getCurrentPiece());
            return true;
        });
        leftButton.setOnClickListener(this);
        gameLoop();
    }

    public void gameLoop() {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                mainActivity.runOnUiThread(new TimerTask() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {

                        if (!gameOver() && mainActivity.getPause()) {

                            gameBoard.moveDown(gameBoard.getCurrentPiece());

                            if (!gameBoard.can_Move_Down(gameBoard.getCurrentPiece())) {
                                int deletedRows = gameBoard.clearRows();
                                gameBoard.clearRows();
                                pieceList.remove(gameBoard.getCurrentPiece());
                                pieceList.add(new Piece(random.nextInt(7) + 1));

                                if (deletedRows > 0) {
                                    points.setCurrentPoints(points.getCurrentPoints() + deletedRows * score);
                                    int p = points.getCurrentPoints();

                                    if(p % 10 == 0){
                                        timerPeriod -= 10;
                                    }
                                    points.setLevel();

                                    currentPunkteTextView.setText("Points:" + " " + p);
                                    currentLevelTextView.setText("Level" + " " + points.getLevel());

                                    if (points.getLevel() > points.loadHighscore()) {
                                        points.writeHighscore();
                                        highscoreLevelTextView.setText("Highscore:" + " " + points.getLevel());
                                    }
                                }

                                if (points.getLevel() > level) {
                                    level++;
                                    timerPeriod = timerPeriod - (points.getLevel() * 20);
                                    timer.cancel();
                                    timer = new Timer();
                                    gameLoop();
                                }
                            }
                            invalidate();
                        }
                    }
                });
            }
        }, 0,mainActivity.getArguments());
    }

    public boolean gameOver() {

        if (gameBoard.checkGameOver(gameBoard.getCurrentPiece())) {
            timer.cancel();
            pieceList.clear();
            gameBoard.clearGameBoard();
            mainActivity.setPause(true);
            mediaPlayer.stop();
            showGameOverScreen();
            return true;
        }
        return false;
    }

    public void resetGame() {
        timer.cancel();
        pieceList.clear();
        gameBoard.clearGameBoard();
        mainActivity.setPause(true);
        mediaPlayer.stop();
        invalidate();
        Intent intent = new Intent(this.getContext(), StartActivity.class);
        getContext().startActivity(intent);
    }

    public void showGameOverScreen() {
        Intent intent = new Intent(this.getContext(), GameOverScreen.class);
        getContext().startActivity(intent);
    }

    /*
    change colorCode to specific Color and paint on Game board
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint p = new Paint();
        @SuppressLint("DrawAllocation") Paint paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);

        for (int x = 0; x < gameBoard.getBoardHeight(); x++) {
            for (int y = 0; y < gameBoard.getBoardWidth(); y++) {

                int color = gameBoard.codeToColor(x, y);
                p.setColor(color);
                canvas.drawRect(y * 50, x * 50, y * 50 + 50, x * 50 + 50, p);
                canvas.drawRect(y * 50, x * 50, y * 50 + 50, x * 50 + 50, paintBorder);
            }
        }
    }

    /*
    control falling pieces with buttons
     */

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (mainActivity.getPause()) {

            switch (v.getId()) {
                case R.id.rightButton:
                    gameBoard.moveRight(gameBoard.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.downButton:
                    gameBoard.fastDrop(gameBoard.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.leftButton:
                    gameBoard.moveLeft(gameBoard.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.rotateButton:
                    gameBoard.rotatePiece(gameBoard.getCurrentPiece());
                    invalidate();
                    break;
            }
        }
    }


    /*
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getX() > 400){
            gameBoard.moveRight(gameBoard.getCurrentPiece());
        }else if(event.getX() < 400){
            gameBoard.moveLeft(gameBoard.getCurrentPiece());
        }
        return super.onTouchEvent(event);
    }



     */

}
