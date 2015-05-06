package tud.tk3ex1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends Activity {

    private Discovery disc;
    private Node node;
    private Subscriber m_subscriber;
    private Publisher m_publisher;
    private TextView tv;


    public class TestReceiver extends Receiver {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = new TextView(this);

        //System.loadLibrary("umundoNativeJava");
        System.loadLibrary("umundoNativeJava_d");

        disc = new Discovery(DiscoveryType.MDNS);

        node = new Node();
        disc.add(node);

        m_publisher = new Publisher("duftt");
        node.addPublisher(m_publisher);

        m_subscriber = new Subscriber("duftt");

        FotoReceiver fr = new FotoReceiver();
        //Thread fr_thread = new Thread(fr);

        m_subscriber.setReceiver(fr);
        node.addSubscriber(m_subscriber);


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }









}
