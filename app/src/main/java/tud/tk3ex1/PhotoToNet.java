package tud.tk3ex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.umundo.core.Message;
import org.umundo.core.Publisher;

import java.io.ByteArrayOutputStream;


public class PhotoToNet extends AsyncTask<Bitmap, Void, byte[]> {
    private Publisher mPublisher;

    public PhotoToNet(Publisher pub) {
        mPublisher = pub;
    }

    @Override
    protected byte[] doInBackground(Bitmap... params) {
        Bitmap b = params[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap n = resize(b, 1280, 1280);
        n.compress(Bitmap.CompressFormat.JPEG, 9, stream);

        return stream.toByteArray();
    }

    @Override
    protected void onPostExecute(byte[] result) {
        Log.d("DEBUG", "Sending image");
        Message m = new Message();
        m.putMeta("type", "image");
        m.setData(result);
        mPublisher.send(m);
    }


    // source: http://stackoverflow.com/questions/15440647/scaled-bitmap-maintaining-aspect-ratio
    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;

        }
    }
}