package revolver.bari.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

import revolver.bari.R;
import revolver.bari.utils.Metrics;

public class SupportMapFragmentWrapper extends SupportMapFragment {

    private Toolbar toolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout.LayoutParams toolbarParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) Metrics.toPixel(45)
        );
        toolbar = new Toolbar(getContext());
        toolbar.setTag("toolbar");

        toolbarParams.setMargins(
                (int) Metrics.toPixel(16),
                (int) Metrics.toPixel(16),
                (int) Metrics.toPixel(16),
                (int) Metrics.toPixel(16)
        );

        toolbar.setLayoutParams(toolbarParams);
        toolbar.setTitle(getArguments().getString("title", "Map"));
        toolbar.setSubtitle(getArguments().getString("subtitle", ""));
        toolbar.setTitleTextAppearance(getContext(), R.style.MapToolbarTitle);
        toolbar.setBackgroundResource(R.drawable.map_toolbar_background);

        DrawerLayout drawerLayout = (DrawerLayout)
                getActivity().findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name
        );
        toggle.getDrawerArrowDrawable().setColorFilter(
                new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ViewGroup vg = (ViewGroup) view;
        vg.addView(toolbar);
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }
}
