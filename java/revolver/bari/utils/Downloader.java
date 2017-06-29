package revolver.bari.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Downloader {

    private Downloader() {}

    public enum Type {
        TEXT,
        JSON,
        IMAGE
    }

    public enum ActualType {
        TEXT,
        JSON_ARRAY,
        JSON_OBJ,
        IMAGE,
        NULL
    }

    private static String connect(String url) {
        HttpURLConnection req;
        try {
            req = (HttpURLConnection) new URL(url).openConnection();
            req.connect();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(req.getInputStream())
            );
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void downloadAsyncAndThen(@NonNull final String url,
                                            final OnDownloadFinished callback,
                                            final Type type) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                String s = connect(url);
                if (s == null) {
                    callback.onFinished(null, ActualType.NULL);
                    return;
                }
                switch (type) {
                    case JSON:
                        try {
                            JSONArray arr = new JSONArray(s);
                            callback.onFinished(arr, ActualType.JSON_ARRAY);
                        } catch (JSONException e) {
                            try {
                                JSONObject o = new JSONObject(s);
                                callback.onFinished(o, ActualType.JSON_OBJ);
                            } catch (JSONException f) {
                                callback.onFinished(s, ActualType.TEXT);
                            }
                        }
                        break;
                    case IMAGE:
                        byte[] data = s.getBytes();
                        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
                        if (b != null)
                            callback.onFinished(b, ActualType.IMAGE);
                        else
                            callback.onFinished(null, ActualType.NULL);
                        break;
                    case TEXT:
                    default:
                        callback.onFinished(s, ActualType.TEXT);
                        break;
                }
            }
        })).start();
    }

    public interface OnDownloadFinished {
        void onFinished(Object retrieved, ActualType type);
    }

}
