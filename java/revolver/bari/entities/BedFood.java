package revolver.bari.entities;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import revolver.bari.constants.Type;
import revolver.bari.utils.Downloader;

public class BedFood extends Place {

    private String url;

    private float rating = -1;
    private int rateCount = -1;
    private String address = null;

    public BedFood(String name, Type type, LatLng position, String url) {
        super(name, type, position, null);
        this.url = url;
    }

    public void fetchInfo(final OnFetchedInfo callback) {
        if (address == null || rateCount < 0 || rating < 0) {
            Downloader.downloadAsyncAndThen(url, new Downloader.OnDownloadFinished() {
                @Override
                public void onFinished(Object retrieved, Downloader.ActualType type) {
                    String page;
                    if (type.equals(Downloader.ActualType.TEXT)) {
                        page = (String) retrieved;
                    } else {
                        Log.w(getTitle(), "failed to fetch info");
                        return;
                    }

                    // extract json data from a <script> element of the page
                    Matcher m = Pattern
                            .compile("<script type=\"application/ld\\+json\">(.*?)</script>")
                            .matcher(page);
                    if (!m.find()) {
                        Log.w(getTitle(), "failed to fetch info");
                    } else {
                        try {
                            JSONObject obj = new JSONObject(m.group(1));
                            JSONObject ratingObj = obj.getJSONObject("aggregateRating");
                            JSONObject addressObj = obj.getJSONObject("address");

                            float rating = Float.parseFloat(ratingObj.getString("ratingValue"));
                            int rateCount = Integer.parseInt(ratingObj.getString("reviewCount"));
                            String address = addressObj.getString("streetAddress");

                            BedFood.this.rating = rating;
                            BedFood.this.rateCount = rateCount;
                            BedFood.this.address = address;

                            callback.whenDone(rating, rateCount, address);
                        } catch (JSONException e) {
                            Log.w(getTitle(), "failed to fetch info");
                        }
                    }
                }
            }, Downloader.Type.TEXT);
        } else {
            callback.whenDone(rating, rateCount, address);
        }
    }

    public interface OnFetchedInfo {
        void whenDone(float rating, int rateCount, String address);
    }

    public String getUrl() {
        return url;
    }
}
