package hu.zybon.zybsolitaire;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

import zybandroid.opengl.util.SaveManager;
import hu.zybon.zybsolitaire.dashboard.DashBoard;
import android.content.Context;
import android.opengl.GLES20;

import zybandroid.opengl.camera.Camera;
import hu.zybon.zybsolitaire.screens.CoverScreen;
import zybandroid.opengl.geometry.Triangle;
import zybandroid.opengl.text.TextRenderer;
import zybandroid.opengl.util.FrameDrawTimer;
import zybandroid.opengl.util.ZybGlRenderer;
import zybandroid.opengl.util.ZybLoader;
import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author zybon
 * Created 2018.01.22. 10:17:40
 */
public class GameRenderer implements ZybGlRenderer{
    
    private static final String TAG = "GameRenderer";

    private final Context context;
    
    private final GameLoader gameLoader = new GameLoader();

    private static final String SAVE_MANAGER_NAME = "game_state";
    private SaveManager saveManager;
    
    private final Camera mainCamera;
    
    public static TextRenderer textRenderer;
    
    private DashBoard dashBoard;
    
    private GameField gameField;
           
    private CoverScreen coverScreen;
    
    private final MainRenderer mainRenderer;
    
    private boolean paused = false;
    
    public GameRenderer(MainRenderer mainRenderer) {
        mainCamera = new Camera();
        this.mainRenderer = mainRenderer;
        this.context = mainRenderer.getContext();
        init();
    }
    
    public Context getContext() {
        return context;
    }
    
    GameLoader getGameLoader() {
        return gameLoader;
    }    

    public SaveManager getSaveManager() {
        return saveManager;
    }

    private void init(){
        saveManager = new SaveManager(context, SAVE_MANAGER_NAME);
        
        initObjects();
        
        FrameDrawTimer.reset();
    }

    private void initObjects(){

        gameLoader.addLoader(new ZybLoader("load gamefield", 1f) {
            
            @Override
            public void load() {
                Triangle.resetIndexCount();
                textRenderer = new TextRenderer(context);
                gameField = new GameField(context);
                saveManager.addSaveAble(gameField);
            }
        });
        
        gameLoader.addLoader(new ZybLoader("dashBoard", 0.2f){

            public void load() {                  

                dashBoard = new DashBoard(context, GameRenderer.this);
                dashBoard.setGameField(gameField);
                mainCamera.setScreenSize(MainRenderer.getScreenWidth(), MainRenderer.getScreenHeight());

                coverScreen = new CoverScreen(context);
            }
        });
        
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public void draw(){
        MainRenderer.setViewPortToScreen();
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        MainRenderer.glBlendOn();

        gameField.draw();

        drawGUI();
        coverScreen.draw();
        
    }

    private void drawGUI(){
        if (!paused) {
            MainRenderer.glBlendOn();
            dashBoard.draw(mainCamera);
            textRenderer.draw();
            MainRenderer.glBlendOff();
        }    
    }
    
    private void clearScreen(){
        MainRenderer.setViewPortToScreen();
        GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glLineWidth(2);
    }


    public void onTouchEvent(ZybMultiTouchEvent zybMultiTouchEvent) {
        dashBoard.onTouchEvent(zybMultiTouchEvent);
        gameField.onTouchEvent(zybMultiTouchEvent);
    }

    public void startNewGame(){
        gameField.startNewGame();
        coverScreen.startCoverEffectFadeOut();
        saveManager.clearSavedGame();

    }
    
    public void saveGame(){
        saveManager.saveGame();
    }
    
    public void loadSavedGame(){
        if (saveManager.hasSavedData()) {
            saveManager.loadSavedGame();
        }
        else {
            startNewGame();
        }
    
    }

    public boolean hasSavedGame(){
        return saveManager.hasSavedData();
    }
    
    public void onResume(){
        gameField.onResume();
        paused = false;
        coverScreen.startCoverEffectFadeOut();
    }

    public boolean onBackPressToFinish() {
        goToMenu();
        return false;
    }
    
    public void goToMenu(){
        paused = true;
        gameField.pause();
        saveGame();
        mainRenderer.changeSceneTo(MainRenderer.SCENE.MENU);
    }
    


}
