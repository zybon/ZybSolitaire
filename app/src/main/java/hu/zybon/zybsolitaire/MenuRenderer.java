package hu.zybon.zybsolitaire;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

import java.util.Random;

import hu.zybon.zybsolitaire.names.MeshesNames;
import zybandroid.opengl.blenderdatas.MeshDataCollector;
import zybandroid.opengl.blenderdatas.MeshDataCollectors;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.gui.GUI_Button;
import zybandroid.opengl.gui.GUI_ButtonWithText;
import zybandroid.opengl.gui.GUI_Panel;
import zybandroid.opengl.gui.GaussianBlur;
import zybandroid.opengl.gui.TexturedQuad;
import zybandroid.opengl.programs.GUIShaderProgram;
import zybandroid.opengl.text.TextObject;
import zybandroid.opengl.text.TextRenderer;
import zybandroid.opengl.util.TextureHelper;
import zybandroid.opengl.util.ZybColor;
import zybandroid.opengl.util.ZybGlRenderer;
import zybandroid.util.touchevents.ZybMultiTouchEvent;
import zybandroid.util.touchevents.listeners.ZybOnClickListener;

/**
 *
 * @author zybon
 * Created 2020.06.02. 11:46:05
 */
public class MenuRenderer implements ZybGlRenderer{

    private final GUIShaderProgram guiShaderProgram = GUIShaderProgram.getGUIShaderProgram();

    private final GUI_Panel guiPanel = new GUI_Panel();

    private final GUI_ButtonWithText play;
    private final GUI_ButtonWithText newplay;
    private final GUI_ButtonWithText exit;
    private final GUI_Button logo;
    private final GUI_Button black_background;

    private final GameRenderer gameRenderer;

    private boolean fboInited = false;

    private final GaussianBlur gaussianBlur;
//    private final GaussianBlur gaussianBlur1;
//    private final GaussianBlur gaussianBlur2;


//    private final FrameBufferObject backGroundFBO;
    private final TexturedQuad background;

    private final TextRenderer textRenderer;

//    private final TextObject info;

    private final Vector3D cameraEye = new Vector3D(0f, 1, -1000);
    private final Vector3D cameraTarget = new Vector3D(0f, -9f, -1100);
    private final Vector3D camToTarget = new Vector3D();

    private final Random rnd = new Random();
    private float offsetX;
    private float delta_offsetX = 0.0005f;
    private float offsetY;
    private float delta_offsetY = 0.0005f;

    public MenuRenderer(final MainRenderer mainRenderer, final GameRenderer gameRenderer) {
        textRenderer = new TextRenderer(mainRenderer.getContext());
        this.gameRenderer = gameRenderer;
        MeshDataCollector mdc = MeshDataCollectors.getCollector(MeshesNames.Dashboard.collector_file_name);
        newplay = new GUI_ButtonWithText(mdc.getMeshData(MeshesNames.Dashboard.menu_newplay),
                textRenderer.createTextObject(mainRenderer.getContext().getString(R.string.new_game)));
        newplay.setOnClickListener(new ZybOnClickListener() {

            public void onClick() {
                fboInited = false;
                gameRenderer.startNewGame();
                mainRenderer.changeSceneTo(MainRenderer.SCENE.GAME);
//                MainRenderer.debugInfoRenderer.setInfoText("play");
            }
        });

        play = new GUI_ButtonWithText(mdc.getMeshData(MeshesNames.Dashboard.menu_play),
                textRenderer.createTextObject(mainRenderer.getContext().getString(R.string.cont_game)));
        play.setOnClickListener(new ZybOnClickListener() {

            public void onClick() {
                fboInited = false;
                mainRenderer.changeSceneTo(MainRenderer.SCENE.GAME);
//                MainRenderer.debugInfoRenderer.setInfoText("play");
            }
        });

        exit = new GUI_ButtonWithText(mdc.getMeshData(MeshesNames.Dashboard.menu_exit),
                textRenderer.createTextObject(mainRenderer.getContext().getString(R.string.exit)));
        exit.setOnClickListener(new ZybOnClickListener() {

            public void onClick() {
                mainRenderer.changeSceneTo(MainRenderer.SCENE.HOME);
//                MainRenderer.debugInfoRenderer.setInfoText("exit");
            }
        });

        logo = new GUI_Button(mdc.getMeshData(MeshesNames.Dashboard.menu_logo));
        logo.setToSquereBasedY();
        black_background = new GUI_Button(mdc.getMeshData(MeshesNames.Dashboard.menu_background));

        background = new TexturedQuad(mainRenderer.getContext());
        background.setScaleX(2);
        background.setScaleY(2/MainRenderer.getScreenAspectRatio());
        offsetX = rnd.nextFloat()*1.6f-0.8f;
        offsetY = rnd.nextFloat()*1.6f-0.8f;
        background.setGlTextureId(TextureHelper.getGlTextureIdFromResID(mainRenderer.getContext(), "main", R.drawable.jatekter));
//        background.setBaseColor(new ZybColor(0x22222222));
        background.setAlpha(0.4f);
//        background.setToBnW(true);
        guiPanel.add(play);
        guiPanel.add(newplay);
        guiPanel.add(exit);

        gaussianBlur = new GaussianBlur(mainRenderer.getContext(), MainRenderer.getScreenWidth()/3, MainRenderer.getScreenHeight()/3);
//        gaussianBlur1 = new GaussianBlur(mainRenderer.getContext(), MainRenderer.getScreenWidth()/8, MainRenderer.getScreenHeight()/8);
//        gaussianBlur2 = new GaussianBlur(mainRenderer.getContext(), MainRenderer.getScreenWidth()/8, MainRenderer.getScreenHeight()/8);
//        String version = "";
//        try {
//            PackageManager manager = mainRenderer.getContext().getPackageManager();
//            PackageInfo pinfo = manager.getPackageInfo(
//                    mainRenderer.getContext().getPackageName(), 0);
//            version = "version: "+pinfo.versionName;
//        }
//        catch (Exception e){
//
//
//        }
//        info = textRenderer.createTextObject(version);
//        info.setPositionInGL(-1.0f, 1.0f);
//        info.setAlign(TextObject.Align.TOP_LEFT);
//        info.setScale(0.04f);
//        info.setAlwaysDrawable(true);
    }

    private void updateCamera(){
//        cameraEye.setY(ZybMath.mix(cameraEye.getY()-1, Water.getWaveHeightIn(cameraEye)*0.5f, 0.1f)+1);
        camToTarget.set(0, 0, -1);
        cameraTarget.set(cameraEye);
        cameraTarget.translateInVectorDirection(camToTarget, 10);
    }

    private void updateBackgorundPosition(){
        offsetX += delta_offsetX;
        if (delta_offsetX>0){
            if (offsetX>0.8f){
                delta_offsetX = -delta_offsetX;
            }
        }
        else {
            if (offsetX<-0.8f){
                delta_offsetX = -delta_offsetX;
            }
        }
        offsetY += delta_offsetY;
        if (delta_offsetY>0){
            if (offsetY>0.8f){
                delta_offsetY = -delta_offsetY;
            }
        }
        else {
            if (offsetY<-0.8f){
                delta_offsetY = -delta_offsetY;
            }
        }

        background.setOffsetX(offsetX);
        background.setOffsetY(offsetY);
    }

    private void update(){
        updateCamera();
        updateBackgorundPosition();
//        if (gameRenderer.hasSavedGame()){
//            play.show();
//        }
//        else {
//            play.hide();
//        }
    }

    public void draw() {
        update();
        clearScreen();
        MainRenderer.glBlendOn();


        background.draw();

        guiShaderProgram.useProgram();
        guiShaderProgram.loadTextureUniform(black_background.getGlTextureId());

        logo.draw();

        textRenderer.draw();

        MainRenderer.glBlendOff();
    }

    private void clearScreen(){
        MainRenderer.setViewPortToScreen();
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void onTouchEvent(ZybMultiTouchEvent zmte) {
        guiPanel.onTouchEvent(zmte);
    }

    public boolean onBackPressToFinish() {
        return true;
    }


}