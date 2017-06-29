package revolver.bari.entities;

import android.content.Context;
import android.widget.ImageView;

public class AnimatedImageView extends ImageView implements Runnable {

    public AnimatedImageView(Context ctx) {
        super(ctx);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        
    }
}

