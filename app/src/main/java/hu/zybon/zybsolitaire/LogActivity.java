package hu.zybon.zybsolitaire;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import zybandroid.opengl.util.ZybLogToFile;

/**
 * @author zybon
 * Created time: 17.11.01 h:p:30
 */
public class LogActivity extends Activity {
    private static final String TAG = "ZybLogMainActivity";
    private TextView cimView;
    private TextView textView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
        cimView = (TextView) findViewById(R.id.cimview);
        cimView.setText(ZybLogToFile.title);
        
        textView = (TextView) findViewById(R.id.textview);
        textView.setText(ZybLogToFile.log);
    }
    
    public final void bezar(View view) {
        finish();
    }        
    
}
