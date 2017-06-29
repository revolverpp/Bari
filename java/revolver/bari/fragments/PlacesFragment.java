package revolver.bari.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import revolver.bari.DetailActivity;
import revolver.bari.R;
import revolver.bari.constants.Places;
import revolver.bari.entities.Place;
import revolver.bari.views.InfoCard;

public class PlacesFragment extends Fragment {

    private RecyclerView placesContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.places_frag_layout, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)
                getActivity().findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        placesContainer = (RecyclerView) v.findViewById(R.id.places_container);
        placesContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        placesContainer.setAdapter(new Adapter(Places.places));
                    }
                });
            }
        }).start();

        return v;
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            InfoCard card;

            ViewHolder(InfoCard infoCard) {
                super(infoCard);
                this.card = infoCard;
            }
        }

        private Place[] places;

        private Adapter(Place[] places) {
            this.places = places;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            InfoCard current = (InfoCard) holder.itemView;
            current.setMargins(8, 8, 8, 8);
            current.setMaxLines(3);

            current.setTitle(places[position].getTitle());
            current.setContentText(places[position].getContentText());
            current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open the BedFood fragment if that card was clicked
                    if (places[holder.getAdapterPosition()].getName().equals("bedDrinkFood")) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, new BDFFragment())
                                .addToBackStack("places->Food")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } else {
                        Intent i = new Intent(getContext(), DetailActivity.class);
                        i.putExtra("place", places[holder.getAdapterPosition()]);
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return places.length;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new InfoCard(getContext(), "", "", null, 0));
        }
    }
}
