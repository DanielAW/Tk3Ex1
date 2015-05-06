package tud.tk3ex1;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.umundo.core.Message;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import vendor.TouchImageView;

public class PhotoDisplay {
    private FrameLayout mParent;
    private MainActivity mMain;
    private TouchImageView mView;

    public PhotoDisplay(MainActivity main, FrameLayout parent) {
        mMain = main;
        mParent = parent;
    }

    public void show(Bitmap b) {
        mView = new TouchImageView(mMain);
        mView.setImageBitmap(b);
        mView.setMaxZoom(4f);
        mParent.removeAllViews();
        mParent.addView(mView);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMain.toggleLowerButtons();
            }
        });
        mView.setOnViewChangeListener(new TouchImageView.OnViewChange() {

            @Override
            public void OnMove(float deltaX, float deltaY) {
                Log.d("AAA", deltaX + " - " + deltaY);

                byte[] data = ByteBuffer.allocate(8).putFloat(deltaX).putFloat(deltaY).array();
                send(data, "move");
            }

            @Override
            public void OnScale(float scaleFactor) {
                Log.d("AAA", "" + scaleFactor);

                byte[] data = ByteBuffer.allocate(4).putFloat(scaleFactor).array();
                send(data, "scale");
            }

            private void send(byte[] data, String type) {
                Message m = new Message();
                m.setData(data);
                m.putMeta("type", type);
                mMain.getPublisher().send(m);
            }
        });
    }

    public void move(byte[] data) {
        final float deltaX = ByteBuffer.wrap(data, 0, 4).getFloat();
        final float deltaY = ByteBuffer.wrap(data, 4, 4).getFloat();
        Log.d("AAA", deltaX + " - " + deltaY);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mView.move(deltaX, deltaY);
            }
        }.execute();
    }

    public void scale(byte[] data) {
        final float scale = ByteBuffer.wrap(data).getFloat();
        Log.d("AAA", "" + scale);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mView.setScale(scale);
            }
        }.execute();

    }
}
