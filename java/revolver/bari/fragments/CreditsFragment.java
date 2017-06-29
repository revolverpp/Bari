package revolver.bari.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import revolver.bari.R;
import revolver.bari.fragments.credits.CreditFragment;
import revolver.bari.fragments.credits.MeFragment;

public class CreditsFragment extends Fragment {

    private Toolbar toolbar;
    private DrawerLayout layout;

    private int current = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.credits_frag_layout, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.credits_viewpager);
        viewPager.setAdapter(new PagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                current = position;
                if (position == 3)
                    setColors(Color.parseColor("#9a9a9a"), Color.WHITE);
                else
                    resetColors();

            }
        });

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        layout = (DrawerLayout) getActivity()
                .findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), layout, toolbar, R.string.app_name, R.string.app_name
        );
        layout.addDrawerListener(toggle);
        toggle.syncState();

        return v;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        private PagerAdapter() {
            super(getActivity().getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newCreditPage(getString(R.string.intro), "sanNicola.png");
                case 1:
                    return newCreditPage(getString(R.string.cityCredit), "logo_comune.png");
                case 2:
                    return newCreditPage(getString(R.string.schoolCredit), "logo_scuola.png");
                case 3:
                    return new MeFragment();
                default:
                    return newCreditPage("", "");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        private Fragment newCreditPage(String text, String imageFile) {
            Fragment frag = new CreditFragment();
            Bundle args = new Bundle();
            args.putString("text", text);
            args.putString("image", imageFile);

            frag.setArguments(args);
            return frag;
        }
    }

    private void resetColors() {
        setColors(getColor(R.color.colorPrimary), getColor(R.color.colorPrimaryDark));
    }

    private void setColors(int toolbarColor, int statusBarColor) {
        toolbar.setBackgroundColor(toolbarColor);
        layout.setStatusBarBackgroundColor(statusBarColor);
    }

    private int getColor(int res) {
        return ContextCompat.getColor(getContext(), res);
    }

    @Override
    public void onPause() {
        super.onPause();
        resetColors();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (current == 3)
            setColors(Color.parseColor("#9a9a9a"), Color.WHITE);
    }
}
