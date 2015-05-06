package tud.tk3ex1;

import android.util.Log;
import android.widget.ImageView;

import org.umundo.core.Receiver;
import org.umundo.core.Message;


public class FotoReceiver extends Receiver{
    private ImageView mImg;

    public FotoReceiver(ImageView dst) {
        mImg = dst;
    }

    public void receive(Message msg) {

        Log.d("DEBUG", "got msg: " + msg.getMeta().get("txt"));

        if(msg.getMeta().get("type").equals("image")) {
            PhotoToUi ptu = new PhotoToUi(mImg);
            ptu.execute(msg.getData());
        }
    }
}
