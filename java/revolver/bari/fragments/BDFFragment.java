package revolver.bari.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

import revolver.bari.R;
import revolver.bari.constants.Places;
import revolver.bari.entities.BedFood;
import revolver.bari.entities.Place;
import revolver.bari.views.BedFoodCard;

public class BDFFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.places_frag_layout, container, false);
        final RecyclerView placesContainer = (RecyclerView) v.findViewById(R.id.places_container);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        DrawerLayout layout = (DrawerLayout)
                getActivity().findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), layout, toolbar, R.string.app_name, R.string.app_name
        );
        layout.addDrawerListener(toggle);
        toggle.syncState();

        placesContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        placesContainer.setAdapter(new Adapter(Places.restaurants));

        return v;
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private BedFood[] places;

        private Adapter(BedFood[] places) {
            this.places = places;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            BedFoodCard card;

            ViewHolder(BedFoodCard card) {
                super(card);
                this.card = card;
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.card.setMargins(8, 8, 8, 8);

            if (position == 0)
                holder.card.use(Places.getPlaceByName("bedDrinkFood"));
            else {
                holder.card.use(places[position - 1]);

                places[position - 1].fetchInfo(new BedFood.OnFetchedInfo() {
                    @Override
                    public void whenDone(float rating, int rateCount, String address) {
                        final StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 5; i++, rating--)
                            sb.append(rating >= 1 ? "★" : "☆");
                        sb.append("  ").append(rateCount).append("\n\n").append(address);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.card.setContentText(sb.toString());
                            }
                        });
                    }
                });

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent web = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(places[holder.getAdapterPosition() - 1].getUrl())
                        );
                        final Bundle mapArgs = new Bundle();
                        mapArgs.putParcelable("position",
                                places[holder.getAdapterPosition() - 1].getPosition());
                        mapArgs.putString("title",
                                places[holder.getAdapterPosition() - 1].getTitle());

                        new AlertDialog.Builder(getContext())
                                .setItems(new String[]{
                                        getString(R.string.bedfood_dialog_web),
                                        getString(R.string.bedfood_dialog_map)
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                startActivity(web);
                                                break;
                                            case 1:
                                                MapsFragment frag = new MapsFragment();
                                                frag.setArguments(mapArgs);

                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.frame, frag)
                                                        .addToBackStack("viewBedFoodOnMap")
                                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                        .commit();
                                        }
                                        dialog.dismiss();
                                    }
                                }).setTitle(places[holder.getAdapterPosition() - 1].getTitle())
                                .show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            // return the length of the array + 1, that is the introduction card
            return places.length + 1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new BedFoodCard(getContext(), Places.restaurants[0]));
        }
    }
}
