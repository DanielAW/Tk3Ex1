package tud.tk3ex1;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.umundo.core.Discovery;
import org.umundo.core.Discovery.DiscoveryType;
import org.umundo.core.Message;
import org.umundo.core.Node;
import org.umundo.core.Publisher;
import org.umundo.core.Receiver;
import org.umundo.core.Subscriber;

import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import vendor.TouchImageView;

public class MainActivity extends ActionBarActivity {

    private static final int SELECT_PICTURE = 1;
    private ArrayList<Uri> mSelectedPictures = new ArrayList<Uri>();
    private PhotoDisplay mPhotoDisplay;
    private FrameLayout mLowerButtons;
    private boolean mLowerButtonsVisible;
    private int mCurrentPictureIndex;

    private Discovery disc;
    private Node node;
    private Subscriber m_subscriber;
    private Publisher m_publisher;
    private TextView tv;

    public Publisher getPublisher() {
        return m_publisher;
    }


    public class TestReceiver extends Receiver {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoDisplay = new PhotoDisplay(this, (FrameLayout)findViewById(R.id.imageArea));
        mLowerButtons = (FrameLayout)findViewById(R.id.lowerButtons);
        hideLowerButtons();

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPictureIndex++;
                if (mCurrentPictureIndex == mSelectedPictures.size()) mCurrentPictureIndex = 0;
                showCurrentPicture();
            }
        });

        findViewById(R.id.button_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPictureIndex == 0) mCurrentPictureIndex = mSelectedPictures.size();
                mCurrentPictureIndex--;
                showCurrentPicture();
            }
        });
        //System.loadLibrary("umundoNativeJava");
        System.loadLibrary("umundoNativeJava_d");

        disc = new Discovery(DiscoveryType.MDNS);

        node = new Node();

        m_publisher = new Publisher("duftt");
        node.addPublisher(m_publisher);

        m_subscriber = new Subscriber("duftt");

        FotoReceiver fr = new FotoReceiver(mPhotoDisplay);
        //Thread fr_thread = new Thread(fr);

        m_subscriber.setReceiver(fr);
        node.addSubscriber(m_subscriber);

        disc.add(node);


    }

    public void onSendBtn(View v) {
        Message m = new Message();
        m.putMeta("txt", "test123");
        Log.d("DEBUG", "try to send data");
        m_publisher.send(m);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            shareImages();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                mSelectedPictures.clear();
                // only one selected?
                if(data.getData() != null) {
                    mSelectedPictures.add(data.getData());
                }
                else { // multiple selected
                    ClipData d = data.getClipData();
                    for (int i = 0; i < d.getItemCount(); i++) {
                        mSelectedPictures.add(d.getItemAt(i).getUri());
                    }
                    showLowerButtons();
                }
                mCurrentPictureIndex = 0;
                showCurrentPicture();
            }
        }
    }

    private void showCurrentPicture() {
        if(mSelectedPictures.size() == 0) {
            Toast.makeText(this, "No picture to show. Please select one first.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Uri uri = mSelectedPictures.get(mCurrentPictureIndex);
            Bitmap b = resizeBitmap(getBitmapFromUri(uri), 1280, 1280);

            mPhotoDisplay.show(b);

            PhotoToNet sender = new PhotoToNet(m_publisher);
            sender.execute(b);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    // source: http://stackoverflow.com/questions/15440647/scaled-bitmap-maintaining-aspect-ratio
    private static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
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


    private void showLowerButtons() {
        mLowerButtons.setAlpha((float)0.7);
        mLowerButtonsVisible = true;
    }

    private void hideLowerButtons() {
        mLowerButtons.setAlpha(0);
        mLowerButtonsVisible = false;
    }

    public void toggleLowerButtons() {
        if(mLowerButtonsVisible) hideLowerButtons();
        else showLowerButtons();
    }
}
