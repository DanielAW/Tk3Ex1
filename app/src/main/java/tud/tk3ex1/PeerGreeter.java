package tud.tk3ex1;

import android.util.Log;

import org.umundo.core.Greeter;
import org.umundo.core.Publisher;
import org.umundo.core.SubscriberStub;


public class PeerGreeter extends Greeter {
    PhotoDisplay mDisp;
    public PeerGreeter(PhotoDisplay disp) {
        mDisp = disp;
    }

    public void welcome(Publisher pub, SubscriberStub sub) {
        Log.d("PeerGreeter", "welcome: " + sub.getUUID());

        if(mDisp.isLastPhotoFromLocalSource()) {

            PhotoToNet ptn = new PhotoToNet(pub);
            byte[] data = ptn.toBytes(mDisp.getLastPhoto());
            ptn.send(data);

        }
    }
    public void farewell(Publisher arg0, SubscriberStub arg1) {

    }
}
