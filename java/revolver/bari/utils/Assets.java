package revolver.bari.utils;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public final class Assets {

    private static AssetManager assets;
    private static boolean ready;

    // i don't wanna be instantiated
    private Assets() {}

    // have i been initialized with a context?
    private static boolean isReady() {
        return ready;
    }

    // feed this class with a context, from which to take the assets
    public static void init(Context ctx) {
        assets = ctx.getAssets();
        ready = true;
    }

    // check the ready status, throw exception if not ready
    private static void checkReady() {
        if (!isReady())
            throw new IllegalStateException("Assets manager not initialized!");
    }

    // convenience method to open an asset file
    public static InputStream open(String path) {
        checkReady();
        try {
            return assets.open(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
