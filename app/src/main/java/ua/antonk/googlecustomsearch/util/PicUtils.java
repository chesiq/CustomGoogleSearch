package ua.antonk.googlecustomsearch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Anton on 03.08.2015.
 */
public class PicUtils {

    public enum ImageType{
        IMAGE,
        THUMBNAIL
    }

    public static String saveImage(Context context, String link, String fileName, ImageType type){
        if (link == null)
            return null;

        final String imagePath = context.getCacheDir() + "/" + type.toString().toLowerCase();
        final String imageAbsolutePath = imagePath + "/" + fileName + ".png";

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File directory = new File(imagePath);
                        directory.mkdirs();

                        File image = new File(imageAbsolutePath);
                        try {
                            image.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(image);
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,ostream);
                            ostream.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };


        Picasso.with(context).load(link).into(target);

        return imageAbsolutePath;
    }
}
