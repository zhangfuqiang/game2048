package com.zhfq.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class GameView extends GridLayout {
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, offsetX, offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeTop();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Config.CARD_WIDTH = (Math.min(w, h) - 20) / 4;
        addCards(Config.CARD_WIDTH, Config.CARD_WIDTH);
        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        for (int y=0; y < 4; y++) {
            for (int x=0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cardsMap[x][y] = c;
            }
        }
    }

    private void startGame() {
        clearScore();
        // 清理旧数据
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
        MainActivity.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
    }

    private void swipeLeft() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int i = x + 1; i < 4; i++) {
                    if (cardsMap[i][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[i][y], cardsMap[x][y],i, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[i][y].getNum());
                            cardsMap[i][y].setNum(0);

                            x--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[i][y])) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[i][y], cardsMap[x][y],i, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[i][y].setNum(0);

                            addScord(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeRight() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {

                for (int i = x - 1; i >= 0; i--) {
                    if (cardsMap[i][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[i][y], cardsMap[x][y],i, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[i][y].getNum());
                            cardsMap[i][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[i][y])) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[i][y], cardsMap[x][y],i, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[i][y].setNum(0);

                            addScord(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeTop() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int i = y + 1; i < 4; i++) {
                    if (cardsMap[x][i].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][i], cardsMap[x][y], x, x, i, y);

                            cardsMap[x][y].setNum(cardsMap[x][i].getNum());
                            cardsMap[x][i].setNum(0);

                            y--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][i])) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][i], cardsMap[x][y], x, x, i, y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][i].setNum(0);

                            addScord(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeDown() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {

                for (int i = y - 1  ; i >= 0; i--) {
                    if (cardsMap[x][i].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][i], cardsMap[x][y], x, x, i, y);

                            cardsMap[x][y].setNum(cardsMap[x][i].getNum());
                            cardsMap[x][i].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][i])) {
//                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][i], cardsMap[x][y], x, x, i, y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][i].setNum(0);

                            addScord(cardsMap[x][y].getNum());
                            merge = false;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete() {
        boolean complete = true;
        ALL:
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("Game Over").setMessage("游戏结束！")
                    .setPositiveButton("重来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).show();
        }
    }

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<>();

    private void addScord(int s) {
        MainActivity.getMainActivity().addScore(s);
    }

    private void clearScore() {
        MainActivity.getMainActivity().clearScore();
    }
}
