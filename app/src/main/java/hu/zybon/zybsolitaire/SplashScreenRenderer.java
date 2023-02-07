package hu.zybon.zybsolitaire;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import hu.zybon.zybsolitaire.names.MeshesNames;
import zybandroid.opengl.blenderdatas.MeshDataCollector;
import zybandroid.opengl.blenderdatas.MeshDataCollectors;
import zybandroid.opengl.gui.GUI_Button;
import zybandroid.opengl.gui.GUI_Slider;
import zybandroid.opengl.programs.GUIShaderProgram;
import zybandroid.opengl.util.Constants;
import zybandroid.opengl.util.ZybColor;
import zybandroid.opengl.util.ZybGlRenderer;
import zybandroid.opengl.util.ZybMath;
import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author zybon
 * Created 2020.05.25. 16:24:14
 */
public class SplashScreenRenderer implements ZybGlRenderer{
    
    private final GUIShaderProgram guiShaderProgram = GUIShaderProgram.getGUIShaderProgram();
    
    private final GUI_Button splash;
    private final GUI_Slider splash_loader;
    
    private final float preLoadingTime = 2*Constants.ONE_SEC_IN_NANOSEC;
    private boolean preLoad;
    private long startTime= -1;
    private float preLoadPercent;
    private final GameRenderer gameRenderer;
    
    private final MainRenderer mainRenderer;
    
    private boolean interrupted = false;

    
    public SplashScreenRenderer(MainRenderer mainRenderer, GameRenderer gameRenderer) {
        this.mainRenderer = mainRenderer;
        this.gameRenderer = gameRenderer;
        MeshDataCollector mdc = MeshDataCollectors.getCollector(MeshesNames.Dashboard.collector_file_name);
        splash = new GUI_Button(mdc.getMeshData(MeshesNames.Dashboard.splash_image));
        splash_loader = new GUI_Slider(mdc.getMeshData(MeshesNames.Dashboard.splash_loader_slider_container),
                mdc.getMeshData(MeshesNames.Dashboard.splash_loader_slider_value));
        splash_loader.setValueColor(ZybColor.CYAN);
        splash_loader.setPercentValue(0.001f);
        preLoad = true;
        
        
    }
    
    private void update() {
        if (interrupted) {
            mainRenderer.changeSceneTo(MainRenderer.SCENE.HOME);
            return;
        }
        if (preLoad) {
            if (startTime<0){
                startTime = System.nanoTime();
            }
            preLoadPercent = (System.nanoTime()-startTime)/(preLoadingTime);
            preLoadPercent = ZybMath.clamp(preLoadPercent, 0.0f, 1.0f);
            if (preLoadPercent>=0.01f){
                preLoad = false;
            }
        }
        else {
            if (gameRenderer.getGameLoader().hasLoader()){
             
                gameRenderer.getGameLoader().loadNext();
//                MainRenderer.sendDebugInfo((int)(gameRenderer.getGameLoader().getLoadPercent()*100)+"%\n"
//                        + gameRenderer.getGameLoader().getNextLoaderName(), 300);
                splash_loader.setPercentValue(gameRenderer.getGameLoader().getLoadPercent());
            }
            else {
//                MainRenderer.sendDebugInfo("");
                gameRenderer.loadSavedGame();
//                gameRenderer.update();
                mainRenderer.changeSceneTo(MainRenderer.SCENE.MENU);
//                ((MainActivity)mainRenderer.getContext()).startSetting();
            }
        }
    }    
    

    public void draw() {
        update();
        clearScreen();
        MainRenderer.glBlendOn();
        guiShaderProgram.useProgram();
        guiShaderProgram.loadTextureUniform(splash.getGlTextureId());
        splash.setAlpha(preLoadPercent);
        splash.draw();
//        guiShaderProgram.loadTextureUniform(splash_loader.getGlTextureId());
        splash_loader.draw();        

        
        
        
        MainRenderer.glBlendOff();        
    }
    

    private void clearScreen(){
        MainRenderer.setViewPortToScreen();
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);    
    }    

    public void onTouchEvent(ZybMultiTouchEvent zmte) {
    }

    public boolean onBackPressToFinish() {
        interrupted = true;
        return false;
    }


}
