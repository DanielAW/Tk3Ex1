package tud.tk3ex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


public class PhotoToUi extends AsyncTask<byte[], Void, Bitmap> {
    private PhotoDisplay mDst;

    public PhotoToUi(PhotoDisplay dst) {
        mDst = dst;
    }

    @Override
    protected Bitmap doInBackground(byte[]... params) {
        return BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
    }


    @Override
    protected void onPostExecute(Bitmap result) {
        mDst.show(result, false);
    }
}
