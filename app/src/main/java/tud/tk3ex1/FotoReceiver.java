package tud.tk3ex1;

import android.util.Log;

import org.umundo.core.Receiver;
import org.umundo.core.Message;

/**
 * Created by daniel on 5/6/15.
 */
public class FotoReceiver extends Receiver{
    private Message m_msg;

    public void receive(Message msg) {
        m_msg = msg;

        Log.d("DEBUG", "got msg: " + m_msg.getMeta().get("txt"));
    }
}
