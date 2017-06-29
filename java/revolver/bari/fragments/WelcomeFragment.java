package revolver.bari.fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import revolver.bari.R;
import revolver.bari.views.InfoCard;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_frag_layout, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        setupButtons(v);
        DrawerLayout drawerLayout = (DrawerLayout)
                getActivity().findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        return v;
    }

    private void setupButtons(View v) {
        Button projButton = ((InfoCard) v.findViewById(R.id.projCard)).getButton();
        Button historyButton = ((InfoCard) v.findViewById(R.id.historyCard)).getButton();

        projButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext(), R.style.AlertDialogWithButtons)
                        .setTitle(getString(R.string.projTitle))
                        .setMessage(getString(R.string.projAim))
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext(), R.style.AlertDialogWithButtons)
                        .setTitle(getString(R.string.historyTitle))
                        .setMessage(getString(R.string.history))
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void todo() {
        new AlertDialog.Builder(getContext(), R.style.AlertDialogWithButtons)
                .setTitle("TODO")
                .setItems(new String[] {
                        "- Customize infowindow in mapFragment https://developers.google.com/maps/documentation/android-api/infowindows\n" +
                                "- Add a little dialog window when a place is clicked on the map (wikipedia-style)"
                }, null)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getActivity().getSupportFragmentManager().getFragments().get(0)
                instanceof WelcomeFragment)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.frame, new WelcomeFragment())
                    .commit();
    }
}
