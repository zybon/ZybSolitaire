package hu.zybon.zybsolitaire;

import android.content.Context;
import android.opengl.GLES20;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.widget.Toast;

import hu.zybon.zybsolitaire.names.ShadersNames;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import zybandroid.opengl.blenderdatas.MeshDataCollectors;
import zybandroid.opengl.programs.GUIShaderProgram;
import zybandroid.opengl.programs.TextureShaderProgram;
import zybandroid.opengl.programs.TextShaderProgram;
import zybandroid.opengl.util.FrameDrawTimer;
import zybandroid.opengl.util.TextFileReader;
import zybandroid.opengl.util.TextureHelper;
import zybandroid.opengl.util.ZybGlRenderer;
import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author zybon
 * Created 2018.01.22. 10:17:40
 */
public class MainRenderer implements GLSurfaceView.Renderer{
    
    private static final String TAG = "SceneRenderer";

    
    
    private static float screenWidth = 1280f;
    private static float screenHeight = 720f;
    private static float screenAspectRatio;
    
    public static float virtualScreenWidth = 0.72f;
    public static float virtualScreenHeight = 1.28f;
    
    public static float getScreenWidth(){
        return MainRenderer.screenWidth;
    }
    
    public static float getScreenHeight(){
        return MainRenderer.screenHeight;
    }    
    
    public static float getScreenAspectRatio(){
        return MainRenderer.screenAspectRatio;
    }    
    
    public static float calcNormalizedX(float screenX){
        return 2f*screenX/screenWidth-1.0f;
    }
    
    public static float calcNormalizedY(float screenY){
        return 1f-2f*screenY/screenHeight;
    }  
    
    public static final void glBlendOn(){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);     
    }
    
    public static final void glBlendOff(){
        glDisable(GL_BLEND);     
    }    
    
    public static final void glCullFaceBackOn(){
        GLES20.glEnable(GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);       
    }
    
    public static final void glCullFaceFrontOn(){
        GLES20.glEnable(GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);       
    }    
    
    public static final void glFaceCullOff(){
        glDisable(GL_CULL_FACE);     
    }     
    
    public static void setViewPortToScreen(){
        glViewport(0, 0, (int)screenWidth, (int)screenHeight);
    }


    
    
    
    private final ZybMultiTouchEvent zybMultiTouchEvent = new ZybMultiTouchEvent();
    
    public static float density = 1.0f;
    
    public static Context context;
    private final MainActivity mainActivity;
    
    private ZybGlRenderer currentRenderer;
    
    private boolean inited;
    private SplashScreenRenderer splashRenderer;
    private GameRenderer gameRenderer;
    private MenuRenderer menuRenderer;
    
    public enum SCENE{
        SPLASH,
        MENU,
        GAME,
        HOME
    }
    
    private boolean sceneChange = false;
    private SCENE scene;
    
    

    public MainRenderer(MainActivity mainActivity) {
        TextureHelper.reset();
        this.context = mainActivity;
        this.mainActivity = mainActivity;     
        
    }
    
    public Context getContext() {
        return context;
    }
    
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

    }  
    
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {

        if (!inited){
            screenWidth = (float)width;
            screenHeight = (float)height;

//            virtualScreenWidth = screenWidth/1000.0f;
//            virtualScreenHeight = screenHeight/1000.0f;

            screenAspectRatio = screenHeight/screenWidth;

            density = context.getResources().getDisplayMetrics().density;

            MeshDataCollectors.clear();
            MeshDataCollectors.init(context);
            FrameDrawTimer.reset();
            createPrograms();
            
            gameRenderer = new GameRenderer(this);
            splashRenderer = new SplashScreenRenderer(this, gameRenderer);
            menuRenderer = new MenuRenderer(this, gameRenderer);

            
            inited = true;        
        }
    }   
    
    
    
    private void createPrograms(){
        TextureShaderProgram.setTextureShaderProgram(new TextureShaderProgram(
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.VertexShaders.textured), 
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.FragmentShaders.textured),
                    "textured")
        );  
        
        TextShaderProgram.setTextShaderProgram(new TextShaderProgram(
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.VertexShaders.text), 
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.FragmentShaders.text),
                    "text")
        );          
        
        GUIShaderProgram.setGUIShaderProgram(new GUIShaderProgram(
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.VertexShaders.gui), 
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.FragmentShaders.gui),
                    "gui")
        ); 
        
//        GUILineShaderProgram.setGUILineShaderProgram(new GUILineShaderProgram(
//                TextFileReader.readTextFromAssets(context,
//                        ShadersNames.VertexShaders.gui_line),
//                TextFileReader.readTextFromAssets(context,
//                        ShadersNames.FragmentShaders.gui_line),
//                    "gui_line")
//        );
        
//        AnimatedTexturedMesh.setTextureShaderProgram(new AnimatedTextureShaderProgram(
//                TextFileReader.readTextFromAssets(context,
//                        "shaders/animated_textured_vertex_shader.glsl"), 
//                TextFileReader.readTextFromAssets(context,
//                        "shaders/textured_fragment_shader.glsl"),
//                    "animated_textured")
//        );         
    }
    
    @Override
    public void onDrawFrame(GL10 glUnused) {
        if (!inited) {
            return;
        }
        
        if (sceneChange){
            changeScene();
            sceneChange = false;
        }
        FrameDrawTimer.calculateFPS();
        
        currentRenderer.draw();
        

                
    }    
    
    public void onTouchEvent(MotionEvent event) {
        zybMultiTouchEvent.update(event);
        currentRenderer.onTouchEvent(zybMultiTouchEvent);
    }
    
    public final void changeSceneTo(SCENE scene){
        this.scene = scene;
        sceneChange = true;
    }
    
    private void changeScene(){
        switch (scene){
            case SPLASH:
                goSplash();
                break;
            case GAME:
                goGame();
                break;                
            case MENU:
                goMenu();
                break;
            case HOME:
                goHome();
                break;
        }
    }
    
    private void goSplash(){
        currentRenderer = splashRenderer;
    }
    
    private void goMenu(){
        MainActivity.soundPlayer.pauseAll();
        currentRenderer = menuRenderer;
    }
    
    private void goGame(){
        MainActivity.soundPlayer.resumeAll();
        gameRenderer.onResume();
        currentRenderer = gameRenderer;
    } 
    
    private void goHome(){
        mainActivity.finish();
    }       
    
    public boolean onBackPressToFinish(){
        return currentRenderer.onBackPressToFinish();
    }
    
    public void onPause(){
        if (gameRenderer != null) {
            gameRenderer.goToMenu();
        }
    }


}
