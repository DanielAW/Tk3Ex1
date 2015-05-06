package tud.tk3ex1;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

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
        return toBytes(params[0]);
    }

    @Override
    protected void onPostExecute(byte[] result) {
        send(result);
    }

    public byte[] toBytes(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 9, stream);
        return stream.toByteArray();
    }

    public void send(byte[] data) {
        Log.d("DEBUG", "Sending image");
        Message m = new Message();
        m.putMeta("type", "image");
        m.setData(data);
        mPublisher.send(m);
    }
}
