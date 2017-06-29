package revolver.bari.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;

public final class Metrics {

    private static DisplayMetrics dm;
    private static SparseArray<Float> computed = new SparseArray<>();

    private Metrics() {}

    public static void init(Context ctx) {
        dm = ctx.getResources().getDisplayMetrics();
    }

    public static float toPixel(int dp) {
        if (computed.get(dp) != null)
            return computed.get(dp);
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        computed.put(dp, px);
        return px;
    }

    public static float toDp(int px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, dm);
    }

    public static float screenWidth() {
        return dm.widthPixels;
    }

    public static float screenHeight() {
        return dm.heightPixels;
    }

}
