package tud.tk3ex1;

import android.util.Log;
import android.widget.ImageView;

import org.umundo.core.Receiver;
import org.umundo.core.Message;


public class FotoReceiver extends Receiver{
    private PhotoDisplay mDisp;

    public FotoReceiver(PhotoDisplay disp) {
        mDisp = disp;
    }

    public void receive(Message msg) {

        Log.d("DEBUG", "got msg: " + msg.getMeta().get("type"));
        String type = msg.getMeta().get("type");
        if(type.equals("image")) {
            PhotoToUi ptu = new PhotoToUi(mDisp);
            ptu.execute(msg.getData());
        }

        else if(type.equals("move")) {
            mDisp.move(msg.getData());
        }
        else if(type.equals("scale")) {
            mDisp.scale(msg.getData());
        }
    }
}
