package revolver.bari.fragments.credits;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import revolver.bari.R;
import revolver.bari.utils.Assets;

public class CreditFragment extends Fragment {

    private ImageView image;
    private TextView text;

    @Override
    public void onStart() {
        super.onStart();

        String text = getArguments().getString("text");
        String imageFile = getArguments().getString("image");

        this.text.setText(text);
        loadImageAsync(imageFile);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.credit_page_layout, container, false);
        ScrollView scroller = (ScrollView) v.findViewById(R.id.credit_page_scroller);

        image = (ImageView) v.findViewById(R.id.credit_page_image);
        text = (TextView) v.findViewById(R.id.credit_page_text);

        scroller.fling(-10);

        return v;
    }

    private void loadImageAsync(final String filename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap b = BitmapFactory.decodeStream(Assets.open(filename));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageBitmap(b);
                    }
                });
            }
        }).start();
    }
}
