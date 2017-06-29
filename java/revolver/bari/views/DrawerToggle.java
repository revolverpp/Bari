package revolver.bari.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import revolver.bari.utils.Metrics;

@SuppressLint("ViewConstructor")
public class DrawerToggle extends ImageButton
        implements DrawerLayout.DrawerListener, View.OnClickListener {

    private DrawerLayout layout;
    private Toolbar toolbar;
    private int res;
    private int gravity;

    public DrawerToggle(Context ctx, int iconRes, DrawerLayout layout, int gravity, Toolbar toolbar) {
        super(ctx);

        this.layout = layout;
        this.res = iconRes;
        this.gravity = gravity;
        this.toolbar = toolbar;
        setImageResource(iconRes);
        setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.transparent));
        setOnClickListener(this);

        setTag(this.hashCode());
        toolbar.addView(this);

        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i).getTag() != null)
                if (toolbar.getChildAt(i).getTag().equals(this.hashCode())) {
                    Toolbar.LayoutParams params = (Toolbar.LayoutParams)
                            toolbar.getChildAt(i).getLayoutParams();
                    params.gravity = Gravity.CENTER | Gravity.END;
                    params.setMarginEnd((int) Metrics.toPixel(16));
                    break;
            }
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onClick(View v) {
        if (layout.isDrawerOpen(gravity))
            layout.closeDrawer(gravity);
        else
            layout.openDrawer(gravity);
    }

    public void ensureVisibility() {
        boolean visible = false;
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i).getTag() != null)
                if (toolbar.getChildAt(i).getTag().equals(this.hashCode())) {
                    visible = true;
                }
        if (!visible)
            toolbar.addView(new DrawerToggle(layout.getContext(), res, layout, gravity, toolbar));
    }
}
