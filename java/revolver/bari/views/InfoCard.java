package revolver.bari.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import revolver.bari.R;
import revolver.bari.utils.Assets;
import revolver.bari.utils.Metrics;

public class InfoCard extends CardView {

    private Button button;
    private View root;
    private TextView contentTextView, titleTextView;

    private boolean isSwipedLeft, isSwipeEnabled = false;

    public InfoCard(Context ctx,
                    String title,
                    String contentText,
                    String buttonText,
                    @DrawableRes int res) {
        super(ctx);

        initCard();
        root = inflate(ctx, R.layout.infocard_layout, this);

        button = (Button) root.findViewById(R.id.infocard_button);

        // if the text for the button is null or empty, the button won't show up;
        // this way this InfoCard can be used either with or without it
        if (buttonText == null || buttonText.isEmpty()) {
            button.setVisibility(GONE);
            setContentPadding(
                    getPaddingLeft(),
                    getPaddingTop(),
                    getPaddingRight(),
                    // additional bottom padding when button is not there;
                    // it matches the top padding
                    getPaddingBottom() + (int) Metrics.toPixel(8)
            );
        } else {
            button.setText(buttonText);
        }

        contentTextView = (TextView) root.findViewById(R.id.infocard_contentText);
        titleTextView = (TextView) root.findViewById(R.id.infocard_title);

        if (res != 0) {
            contentTextView.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(ctx, res),
                    null,
                    null,
                    null
            );
            contentTextView.setCompoundDrawablePadding((int) Metrics.toPixel(8));
        }

        contentTextView.setText(contentText);
        titleTextView.setText(title);

        swipeX = getX();
    }

    public InfoCard(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        initCard();
        root = inflate(ctx, R.layout.infocard_layout, this);

        TypedArray styledAttrs = ctx.getTheme().obtainStyledAttributes(
                attrs, R.styleable.InfoCard, 0, 0
        );
        String title, contentText, buttonText;

        title = styledAttrs.getString(R.styleable.InfoCard_title);
        contentText = styledAttrs.getString(R.styleable.InfoCard_contentText);
        buttonText = styledAttrs.getString(R.styleable.InfoCard_buttonText);

        button = (Button) root.findViewById(R.id.infocard_button);
        button.setText(buttonText);

        contentTextView = (TextView) root.findViewById(R.id.infocard_contentText);
        titleTextView = (TextView) root.findViewById(R.id.infocard_title);

        contentTextView.setText(contentText);
        titleTextView.setText(title);
    }

    private void initCard() {
        setRadius(Metrics.toPixel(4));
        setCardElevation(Metrics.toPixel(5));

        setContentPadding(
                (int) Metrics.toPixel(16),
                (int) Metrics.toPixel(16),
                (int) Metrics.toPixel(16),
                (int) Metrics.toPixel(4)
        );
    }

    public Button getButton() {
        return this.button;
    }

    public void setSwipeEnabled(boolean b) {
        isSwipeEnabled = b;
    }

    public void setMaxLines(int m) {
        contentTextView.setMaxLines(m);
    }

    public void setContentText(String text) {
        contentTextView.setText(text);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setIcon(int res) {
        if (res == 0)
            contentTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else
            contentTextView.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(getContext(), res),
                    null,
                    null,
                    null
            );
    }

    public void setMargins(int left, int top, int right, int bottom) {
        // it only works with LinearLayout.LayoutParams
        // maybe because the layout it's put in is a LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(
                (int) Metrics.toPixel(left),
                (int) Metrics.toPixel(top),
                (int) Metrics.toPixel(right),
                (int) Metrics.toPixel(bottom)
        );
        setLayoutParams(params);
    }

    public void loadImageAsync(final String path) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                Drawable d = Drawable.createFromStream(Assets.open(path), null);
                if (d == null)
                    return;
                ScaleDrawable scaled = new ScaleDrawable(
                        d,
                        Gravity.CENTER,
                        Metrics.toPixel(50) / d.getIntrinsicWidth(),
                        Metrics.toPixel(50) / d.getIntrinsicHeight()
                );
                contentTextView.setCompoundDrawablesWithIntrinsicBounds(
                        scaled, null, null, null
                );
            }
        })).start();
    }

    private float swipeX;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isSwipeEnabled)
            return super.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            swipeX = ev.getX();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (ev.getX() < swipeX) {
                if (swipeX - ev.getX() > Metrics.toPixel(40)) {
                    if (!isSwipedLeft) {
                        isSwipedLeft = true;
                        root.animate()
                                .translationXBy(-Metrics.toPixel(70))
                                .setDuration(175)
                                .start();
                    }
                }
            } else {
                if (ev.getX() - swipeX > Metrics.toPixel(40)) {
                    if (isSwipedLeft) {
                        isSwipedLeft = false;
                        root.animate()
                                .translationXBy(Metrics.toPixel(70))
                                .setDuration(175)
                                .start();
                    }
                }
            }
        }
        return true;
    }


}
