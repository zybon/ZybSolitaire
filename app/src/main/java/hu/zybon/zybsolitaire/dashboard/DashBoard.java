package hu.zybon.zybsolitaire.dashboard;

import android.content.Context;

import hu.zybon.zybsolitaire.GameField;
import hu.zybon.zybsolitaire.R;
import hu.zybon.zybsolitaire.names.MeshesNames;
import zybandroid.opengl.camera.Camera;
import hu.zybon.zybsolitaire.GameRenderer;
import zybandroid.opengl.blenderdatas.MeshDataCollector;
import zybandroid.opengl.blenderdatas.MeshDataCollectors;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.gui.GUI_Button;
import zybandroid.opengl.gui.GUI_ButtonWithText;
import zybandroid.opengl.programs.GUIShaderProgram;
import zybandroid.opengl.text.TextObject;
import zybandroid.opengl.util.TextureHelper;
import zybandroid.util.touchevents.ZybMultiTouchEvent;
import zybandroid.util.touchevents.listeners.ZybOnClickListener;

/**
 *
 * @author zybon
 * Created 2018.01.21. 22:58:57
 */
public class DashBoard{
    
    private static final String TAG = "DashBoard";
    
    private final GUIShaderProgram guiShaderProgram = GUIShaderProgram.getGUIShaderProgram();
    
    
    private final Context context;
    private final MeshDataCollector dashBoardCollector;
    private final Vector3D virtualScreenDimension = new Vector3D();

    private final GUI_Button menu;
    private final GUI_Button undo;
    private final GUI_Button restart;
//    private final GUI_Button driveMode;    

    private GameField gameField;
    
    final int dashBoardGlTextureId;
    
    private final TextObject info;
    
    private final GameRenderer gameRenderer;
    
    
    public DashBoard(Context context, final GameRenderer gameRenderer) {
        this.context = context;
        this.gameRenderer = gameRenderer;
        dashBoardCollector = MeshDataCollectors.getCollector(MeshesNames.Dashboard.collector_file_name);
        virtualScreenDimension.set(dashBoardCollector.getMeshData(
                MeshesNames.Dashboard.virtual_screen).getDimensions());
        
        dashBoardGlTextureId = TextureHelper.getGlTextureIdFromResID(context, "dashboard", R.drawable.two_dimensions);
        

        menu = new GUI_Button(dashBoardCollector.getMeshData(MeshesNames.Dashboard.to_menu));
        menu.setTouhArea(dashBoardCollector.getMeshData(MeshesNames.Dashboard.to_menu_toucharea));
        menu.setOnClickListener(new ZybOnClickListener() {

            public void onClick() {
                gameRenderer.goToMenu();
            }


        });

        undo = new GUI_Button(dashBoardCollector.getMeshData(MeshesNames.Dashboard.to_menu));
        undo.setPositionInOpengGl(new Vector3D(0.8f, 0.35f, 0f));
        undo.setOnClickListener(new ZybOnClickListener() {

            public void onClick() {
                gameField.undoLastMove();
            }


        });

        restart = new GUI_ButtonWithText(dashBoardCollector.getMeshData(MeshesNames.Dashboard.game_restart),
                GameRenderer.textRenderer.createTextObject(context.getString(R.string.restart)));
        restart.hide();
        restart.setOnClickListener(new ZybOnClickListener() {

            public void onClick() {
                gameField.startNewGame();
            }


        });

//        settings = new GUI_Button(dashBoardCollector.getMeshData(MeshesNames.Dashboard.settings));
//        settings.setTouhArea(dashBoardCollector.getMeshData(MeshesNames.Dashboard.settings_toucharea));
//        settings.setOnClickListener(new ZybOnClickListener() {
//
//            public void onClick() {
//                SettingsFrame.show();
//            }
//
//
//        });
   
        
        GUI_Button infoFieldTextPlace = new GUI_Button(dashBoardCollector.getMeshData(MeshesNames.Dashboard.info_field_text_place));        
        info = GameRenderer.textRenderer.createTextObject("");
        info.setPositionInGL(infoFieldTextPlace.getGlPosX(), infoFieldTextPlace.getGlPosY());           
        info.setScale(0.08f);

    }
    
    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }
    
    public Context getContext() {
        return context;
    }

    public Vector3D getVirtualScreenDimension() {
        return virtualScreenDimension;
    }
    
    public void draw(Camera camera){

        guiShaderProgram.useProgram();
        guiShaderProgram.loadTextureUniform(dashBoardGlTextureId);
        menu.draw();
        if (gameField.isGameOver()) {
            restart.show();
        }
        else {
            restart.hide();
        }
        if (gameField.isUndoable()) {
            undo.draw();
        }

    }  
    

    public void onTouchEvent(ZybMultiTouchEvent zmte) {
        if (gameField.isGameOver()) {
            restart.onTouchEvent(zmte);
        }
        if (gameField.isUndoable()) {
            undo.onTouchEvent(zmte);
        }
        menu.onTouchEvent(zmte);
    }



    


}
