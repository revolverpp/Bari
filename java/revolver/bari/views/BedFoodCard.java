package revolver.bari.views;

import android.annotation.SuppressLint;
import android.content.Context;

import revolver.bari.entities.BedFood;
import revolver.bari.entities.Place;

@SuppressLint("ViewConstructor")
public class BedFoodCard extends InfoCard {

    public BedFoodCard(Context ctx, BedFood bf) {
        super(ctx, bf.getTitle(), "", null, bf.getIcon());
    }

    public void use(BedFood bf) {
        setTitle(bf.getTitle());
        setIcon(bf.getIcon());
    }

    public void use(Place p) {
        setTitle(p.getTitle());
        setContentText(p.getContentText());
        setIcon(0);
        setMaxLines(1000);
    }
}
