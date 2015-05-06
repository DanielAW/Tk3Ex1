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

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        params[0].compress(Bitmap.CompressFormat.JPEG, 8, stream);

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
}
