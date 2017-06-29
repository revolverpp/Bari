package revolver.bari.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import revolver.bari.DetailActivity;
import revolver.bari.R;
import revolver.bari.constants.Places;
import revolver.bari.entities.Place;
import revolver.bari.utils.Metrics;
import revolver.bari.views.DrawerToggle;

public class MapsFragment extends Fragment
        implements OnMapReadyCallback, OnNavigationItemSelectedListener,
                GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private SupportMapFragmentWrapper mapFragment;
    private DrawerLayout layout;
    private DrawerToggle toggle;
    private GoogleMap theMap;

    private PopupWindow currentInfoDialog;

    private boolean loaded;

    @Override
    public void onStart() {
        super.onStart();

        if (!loaded) {
            Bundle args = new Bundle();
            args.putString("title", getContext().getString(R.string.toolbar_map_title));
            args.putString("subtitle", getContext().getString(R.string.toolbar_map_subt));

            mapFragment = new SupportMapFragmentWrapper();
            mapFragment.setArguments(args);
            loadMapAsync();
        }
        if (toggle != null)
            toggle.ensureVisibility();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_frag_layout, container, false);

        NavigationView navi = (NavigationView) v.findViewById(R.id.map_navi);
        layout = (DrawerLayout) v.findViewById(R.id.map_frag_layout);

        navi.setNavigationItemSelectedListener(this);

        Menu placesList = navi.getMenu();
        placesList.clear();
        for (Place p : Places.places)
            if (p.hasPosition())
                placesList.add(p.getTitle()).setIcon(Places.getIcon(p));

        return v;
    }

    private void loadMapAsync() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.map_frame, mapFragment)
                        .commit();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mapFragment.getMapAsync(MapsFragment.this);
                    }
                });
            }
        })).start();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onMapReady(GoogleMap googleMap) {
        this.theMap = googleMap;
        this.loaded = true;
        theMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Places.cityLat, Places.cityLng), 15.78f
        ));
        theMap.setOnMarkerClickListener(this);
        theMap.setOnInfoWindowClickListener(this);

        Toolbar toolbar = mapFragment.getToolbar();
        toggle = new DrawerToggle(
                getContext(), R.drawable.ic_action_pin, layout, Gravity.END, toolbar
        );
        layout.addDrawerListener(toggle);

        if (getArguments() != null)
            if (getArguments().getParcelable("position") != null) {
                LatLng position = getArguments().getParcelable("position");
                String title = getArguments().getString("title");

                addMarker(title, position, true);
            }
    }

    private Marker addMarker(String title, LatLng position, boolean show) {
        MarkerOptions marker = new MarkerOptions();
        marker.title(title);
        marker.draggable(false);
        marker.visible(true);
        marker.position(position);

        theMap.clear();
        Marker m = theMap.addMarker(marker);

        if (mapFragment.getView() != null) {
            if (mapFragment.getToolbar() != null)
                mapFragment.getToolbar().setSubtitle(title);
        }
        return m;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        layout.closeDrawer(GravityCompat.END);

        Place selected = Places.getPlaceByTitle(item.getTitle().toString());
        if (selected == null)
            return false;

        spawnInfoDialog(selected);
        addMarker(selected.getTitle(), selected.getPosition(), true);

        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent i = new Intent(getContext(), DetailActivity.class);
        if (Places.isBedFood(marker.getTitle())) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else {
            Place p = Places.getPlaceByTitle(marker.getTitle());
            i.putExtra("place", p);
            startActivity(i);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        if (!Places.isBedFood(marker.getTitle())) {
            if (currentInfoDialog.isShowing())
                currentInfoDialog.dismiss();
            spawnInfoDialog(Places.getPlaceByTitle(marker.getTitle()));
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private void spawnInfoDialog(final Place p) {
        View v = View.inflate(getContext(), R.layout.map_infodialog_layout, null);
        TextView textView = (TextView) v.findViewById(R.id.map_infodialog_text);
        ImageButton closeButton = (ImageButton) v.findViewById(R.id.map_infodialog_close);
        Button readMore = (Button) v.findViewById(R.id.map_infodialog_button);

        textView.setText(p.getContentText());

        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_action_compass);
        icon.setColorFilter(new PorterDuffColorFilter(
                Color.parseColor("#676767"), PorterDuff.Mode.SRC_ATOP
        ));
        icon.setBounds(0, 0, (int) Metrics.toPixel(50), (int) Metrics.toPixel(50));
        textView.setCompoundDrawablePadding((int) Metrics.toPixel(16));
        textView.setCompoundDrawables(icon, null, null, null);

        currentInfoDialog = new PopupWindow(getContext(), null, R.style.InfoDialog);
        currentInfoDialog.setContentView(v);
        currentInfoDialog.setHeight((int) Metrics.toPixel(250));
        currentInfoDialog.setWidth((int) Metrics.screenWidth());
        currentInfoDialog.setAnimationStyle(R.style.InfoDialog_Animation);
        if (Build.VERSION.SDK_INT >= 21)
            currentInfoDialog.setElevation(Metrics.toPixel(4));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().getOverlay().clear();
                currentInfoDialog.dismiss();
            }
        });
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInfoDialog.dismiss();
                getView().getOverlay().clear();
                Intent i = new Intent(getContext(), DetailActivity.class);
                i.putExtra("place", p);
                startActivity(i);
            }
        });

        // apply dim to the map
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, getView().getWidth(), getView().getHeight());
        dim.setAlpha((int) (255 * 0.4));
        getView().getOverlay().clear();
        getView().getOverlay().add(dim);

        currentInfoDialog.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        currentInfoDialog.update();
    }
}
