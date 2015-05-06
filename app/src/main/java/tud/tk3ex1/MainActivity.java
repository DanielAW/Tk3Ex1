package tud.tk3ex1;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import vendor.TouchImageView;

public class MainActivity extends ActionBarActivity {

    private static final int SELECT_PICTURE = 1;
    private ArrayList<Uri> mSelectedPictures = new ArrayList<Uri>();
    private TouchImageView mImageView;
    private FrameLayout mLowerButtons;
    private boolean mLowerButtonsVisible;
    private int mCurrentPictureIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (TouchImageView)findViewById(R.id.imageView);
        mLowerButtons = (FrameLayout)findViewById(R.id.lowerButtons);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLowerButtons();
            }
        });
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
        try {
            Uri uri = mSelectedPictures.get(mCurrentPictureIndex);
            Bitmap b = getBitmapFromUri(uri);

            mImageView.setImageBitmap(b);
            mImageView.setMaxZoom(4f);

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


    private void showLowerButtons() {
        mLowerButtons.setAlpha((float)0.7);
        mLowerButtonsVisible = true;
    }

    private void hideLowerButtons() {
        mLowerButtons.setAlpha(0);
        mLowerButtonsVisible = false;
    }

    private void toggleLowerButtons() {
        if(mLowerButtonsVisible) hideLowerButtons();
        else showLowerButtons();
    }
}
