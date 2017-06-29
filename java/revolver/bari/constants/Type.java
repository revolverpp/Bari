package revolver.bari.constants;

import revolver.bari.R;

public enum Type {
    CASTLE(R.drawable.ic_castle),
    SQUARE(R.drawable.ic_obelisk),
    HOUSE(R.drawable.ic_house),
    TEMPLE(R.drawable.ic_temple),
    FOUNTAIN(R.drawable.ic_fountain),
    CHURCH(R.drawable.ic_church),

    RESTAURANT(R.drawable.ic_action_restaurant),
    ACCOMODATION(R.drawable.ic_hotel),

    UNKNOWN(R.drawable.ic_obelisk);

    public int res;

    Type(int res) {
        this.res = res;
    }
}