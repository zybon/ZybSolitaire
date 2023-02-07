package hu.zybon.zybsolitaire;

/**
 * @author zybon
 * Created time: 2018.06.02. 13:20
 */
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import hu.zybon.zybsolitaire.sound.SoundPlayer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import zybandroid.opengl.util.ZybLogToFile;

public class MainActivity extends Activity {
    
    private static final String TAG = "MainActivity";
    
    private GLSurfaceView glSurfaceView;
    private MainRenderer mainRenderer;
    
//    private ZybSurfaceViewAboveGl zybSurfaceViewAboveGl;
    
    private boolean renderSet = false;
    
    public static SoundPlayer soundPlayer;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        hideNavBar();
        ZybLogToFile.createHandlerForActivity(this, "ZybSolitaire");
        setContentView(R.layout.main);
        soundPlayer = new SoundPlayer(this);
        
        final ActivityManager activityManager = 
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;        
//        Toast.makeText(this, "configurationInfo: "+configurationInfo, Toast.LENGTH_LONG).show();
        glSurfaceView = (GLSurfaceView)findViewById(R.id.glsurf);
//        glSurfaceView = new GLSurfaceView(this);
//        setContentView(glSurfaceView);

//        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
//            glSurfaceView.setEGLConfigChooser(new MyConfigChooser());
//            glSurfaceView.setPreserveEGLContextOnPause(true);
//            Log.i(TAG, "new SceneRenderer");
            mainRenderer = new MainRenderer(this);
            glSurfaceView.setRenderer(mainRenderer);
            mainRenderer.changeSceneTo(MainRenderer.SCENE.SPLASH);
//            Log.i(TAG, "setZybSurfaceViewAboveGl");
            renderSet = true;
            glSurfaceView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, final MotionEvent me) {
                    if (me != null) {
//                        glSurfaceView.queueEvent(new Runnable() {
//                            
//                            @Override
//                            public void run() {
                                mainRenderer.onTouchEvent(me);
//                            }
//                        });
                        return true;
                    }
                    return false;
                }
            });    
       }
        
           
    }
    
    @Override
    public void onBackPressed() {
        if (mainRenderer.onBackPressToFinish()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        soundPlayer.release();
        soundPlayer = null;
//        Toast.makeText(this, "ondestroy", Toast.LENGTH_LONG).show();
        super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
        
    }
    
    @Override
    protected void onPause() {

        super.onPause();
        
        if (renderSet){
//            glSurfaceView.onPause();
            mainRenderer.onPause();
//            Toast.makeText(this, "onPause actvity", Toast.LENGTH_LONG).show();
//            MainRenderer.changeSceneTo(MainRenderer.SCENE.MENU);
        }        
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavBar();
//            glSurfaceView.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideNavBar();
        }
    }
    
    public void hideNavBar(){
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                      | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


    }
    
//    public boolean isShowNavBar(){
//        View decorView = getWindow().getDecorView();
//        int uiOptions = decorView.getSystemUiVisibility();
//        return (uiOptions & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
//    }

    class MyConfigChooser implements GLSurfaceView.EGLConfigChooser {
        @Override
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int attribs[] = {
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_RENDERABLE_TYPE, 4,  // EGL_OPENGL_ES2_BIT
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 24,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 4,  // This is for 4x MSAA.
                EGL10.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] configCounts = new int[1];
            egl.eglChooseConfig(display, attribs, configs, 1, configCounts);

            if (configCounts[0] == 0) {
                // Failed! Error handling.
                return null;
            } else {
                return configs[0];
            }
        }
    }    

}
