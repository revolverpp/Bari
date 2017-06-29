package revolver.bari.constants;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;


import revolver.bari.R;
import revolver.bari.entities.BedFood;
import revolver.bari.entities.Place;

public final class Places {

    private static Resources r;
    private static String packageName;
    public static final double cityLat = 41.1292615;
    public static final double cityLng = 16.8679592;

    public static final Place[] places = {
            newPlace("basilicaSanNicola", Type.CHURCH, new LatLng(41.1302683, 16.8699982), 1),
            newPlace("bedDrinkFood", Type.UNKNOWN, null, 1),
            newPlace("capaDelTurco", Type.UNKNOWN, new LatLng(41.1309747, 16.8689925), 1),
            newPlace("casaPiccinni", Type.HOUSE, new LatLng(41.1282085, 16.8711402), 1),
            newPlace("castelloSvevo", Type.CASTLE, new LatLng(41.1284694, 16.8662122), 1),
            newPlace("cattedrale", Type.CHURCH, new LatLng(41.1285034, 16.8688424), 1),
            newPlace("cisternaBonaSforza", Type.TEMPLE, new LatLng(41.1287756, 16.8689536), 1),
            newPlace("colonnaInfame", Type.SQUARE, new LatLng(41.1279996, 16.871634), 1),
            newPlace("exultet", Type.UNKNOWN, new LatLng(41.1278811, 16.8687153), 2),
            newPlace("fontanaDellaPigna", Type.FOUNTAIN, new LatLng(41.1280842, 16.8713582), 1),
            newPlace("fortino", Type.TEMPLE, new LatLng(41.1282228, 16.8736492), 1),
            newPlace("isolato49", Type.HOUSE, new LatLng(41.1273581, 16.8691514), 1),
            newPlace("palazzoDelSedile", Type.TEMPLE, new LatLng(41.127761, 16.871684), 1),
            newPlace("piazzaFerrarese", Type.SQUARE, new LatLng(41.126494, 16.871887), 1),
            newPlace("piazzaOdegitria", Type.SQUARE, new LatLng(41.128465, 16.868285), 1),
            newPlace("sanGregorio", Type.CHURCH, new LatLng(41.130692, 16.869547), 1),
            newPlace("santaMariaDelBC", Type.CHURCH, new LatLng(41.131689, 16.870264), 1),
            newPlace("santAnna", Type.CHURCH, new LatLng(41.128578, 16.871595), 1),
            newPlace("santaScolastica", Type.CHURCH, new LatLng(41.132151, 16.870813), 1),
            newPlace("viaAppia", Type.SQUARE, new LatLng(41.126891, 16.871858), 1)
    };

    public static final BedFood[] restaurants = {
            new BedFood("La Uascezze", Type.RESTAURANT, new LatLng(41.128762, 16.871804), "https://www.tripadvisor.it/Restaurant_Review-g187874-d1900148-Reviews-La_Uascezze-Bari_Province_of_Bari_Puglia.html"),
            new BedFood("La Cantina di Cianna Cianne", Type.RESTAURANT, new LatLng(41.128899, 16.872209), "https://www.tripadvisor.it/Restaurant_Review-g187874-d1599586-Reviews-La_Cantina_di_Cianna_Cianne-Bari_Province_of_Bari_Puglia.html"),
            new BedFood("Antica Osteria delle Travi", Type.RESTAURANT, new LatLng(41.126854, 16.868544), "https://www.tripadvisor.com/Restaurant_Review-g187874-d1996284-Reviews-Osteria_delle_Travi_di_Bari-Bari_Province_of_Bari_Puglia.html")
    };

    private Places() {}

    public static void init(Context ctx) {
        r = ctx.getResources();
        packageName = ctx.getPackageName();
    }

    private static Place newPlace(String name,
                                  Type type,
                                  LatLng position,
                                  int imagesCount) {
        String[] images = new String[imagesCount];
        if (imagesCount > 1)
            for (int i = 0; i < imagesCount; i++)
                images[i] = name + String.valueOf(i - 1) + ".jpg";
        else
            images[0] = name + ".jpg";
        return new Place(
                name,
                type,
                position,
                images
        );
    }

    public static String getTitle(String name) {
        int id = r.getIdentifier("place_" + name + "_title", "string", packageName);
        if (id == 0)
            return null;
        return r.getString(id);
    }

    public static String getText(String name) {
        int id = r.getIdentifier("place_" + name + "_content", "string", packageName);
        if (id == 0)
            return null;
        return r.getString(id);
    }

    public static int getIcon(Place p) {
        return p.getType().res;
    }

    public static Place getPlaceByTitle(String title) {
        for (Place p : places)
            if (p.getTitle().equals(title))
                return p;
        return null;
    }

    public static Place getPlaceByName(String name) {
        for (Place p : places)
            if (p.getName().equals(name))
                return p;
        return null;
    }

    public static boolean isBedFood(String title) {
        for (BedFood b : restaurants)
            if (b.getTitle().equals(title))
                return true;
        return false;
    }

    public static void updateResources(Resources newRes) {
        r = newRes;
    }
}
