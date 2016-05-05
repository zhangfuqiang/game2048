package com.zhfq.game2048;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/4.
 */
public class Card extends FrameLayout {
    private int num = 0;
    private TextView label;
    private int[] colors = {0xffeee4da, 0xffede0c8, 0xfff2b179, 0xfff59563, 0xfff67c5f, 0xfff65e3b, 0xffedcf72, 0xffedcb60,
    0xffedc850, 0xffedc53f};

    public Card(Context context) {
        super(context);
        label = new TextView(getContext());
        label.setTextSize(18);
        label.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(20, 20, 0, 0);
        addView(label, lp);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num == 0) {
            label.setText("");
        } else {
            label.setText(num + "");
        }

        switch (num) {
            case 0:
                label.setBackgroundColor(0x00000000);//透明色
                break;
            case 2:
                label.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                label.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                label.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                label.setBackgroundColor(0xfff59563);
                break;
            default:
                label.setBackgroundColor(0xff3c3a32);
                break;
        }
    }

    public boolean equals(Card o) {
        return getNum() == o.getNum();
    }
}
