package tud.tk3ex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


public class PhotoToUi extends AsyncTask<byte[], Void, Bitmap> {
    private ImageView mDst;

    public PhotoToUi(ImageView dst) {
        mDst = dst;
    }

    @Override
    protected Bitmap doInBackground(byte[]... params) {
        return BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
    }


    @Override
    protected void onPostExecute(Bitmap result) {
        mDst.setImageBitmap(result);
    }
}
