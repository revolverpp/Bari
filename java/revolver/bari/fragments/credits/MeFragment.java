package revolver.bari.fragments.credits;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import revolver.bari.R;
import revolver.bari.utils.Metrics;

public class MeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.me_frag_layout, container, false);
        Button mailButton = (Button) v.findViewById(R.id.me_contact_mail);
        Button twitterButton = (Button) v.findViewById(R.id.me_contact_twitter);
        ImageView pic = (ImageView) v.findViewById(R.id.me_contact_pic);

        mailButton.setText(getString(R.string.mail));
        mailButton.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_action_mail),
                null, null, null
        );

        twitterButton.setText(getString(R.string.twitter));

        AnimationDrawable twitter = new AnimationDrawable();
        twitter.addFrame(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_action_twitter), 250);
        twitter.addFrame(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_action_twitter_2), 250);

        twitterButton.setCompoundDrawablesWithIntrinsicBounds(twitter, null, null, null);
        twitter.start();

        return v;
    }
}
