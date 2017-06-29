package revolver.bari;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import revolver.bari.entities.Place;
import revolver.bari.utils.Assets;

public class DetailActivity extends AppCompatActivity {

    private Place place;

    private ImageView imageView;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_detail);

        imageView = (ImageView) findViewById(R.id.detail_image);
        place = getIntent().getParcelableExtra("place");
        Log.d("detailActivity", "detail: " + place.getTitle());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView contentTextView = (TextView) findViewById(R.id.detail_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle(place.getTitle());
        contentTextView.setText(place.getContentText());

        loadImageAsync();
    }

    private void loadImageAsync() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                if (place.getImageFiles().length > 1)
                    return;
                final Bitmap b = BitmapFactory.decodeStream(
                        Assets.open(place.getImageFiles()[0])
                );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(b);
                    }
                });
            }
        })).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
