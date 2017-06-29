package revolver.bari.utils;

import android.graphics.Bitmap;

public class Blur {

    public static Bitmap blur(Bitmap input) {
        Bitmap b = input.copy(input.getConfig(), true);
        for (int x = 0; x < input.getWidth(); x++)
            for (int y = 0; y < input.getHeight(); y++)
                b.setPixel(x, y,
                        ((x > 0 && y > 0 ? input.getPixel(x - 1, y - 1) : 0) +
                        (x > 0 ? input.getPixel(x - 1, y) : 0) +
                        (x > 0 && y < input.getHeight() - 1 ? input.getPixel(x - 1, y + 1) : 0) +
                        (y > 0 ? input.getPixel(x, y - 1) : 0) +
                        input.getPixel(x, y) +
                        (y < input.getHeight() - 1 ? input.getPixel(x, y + 1) : 0) +
                        (y > 0 && x < input.getWidth() - 1 ? input.getPixel(x + 1, y - 1) : 0) +
                        (x < input.getWidth() - 1 ? input.getPixel(x + 1, y) : 0) +
                        (x < input.getWidth() - 1 && y < input.getHeight() - 1 ? input.getPixel(x + 1, y + 1) : 0)) / 9
                );
        return b;
    }

}
