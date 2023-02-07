package zybandroid.opengl.gui;

import hu.zybon.zybsolitaire.MainRenderer;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.text.TextObject;
import zybandroid.opengl.util.ZybColor;
import zybandroid.util.touchevents.listeners.ZybOnPressedListener;

/**
 *
 * @author zybon
 * Created 2021.04.20. 12:17:10
 */
public class GUI_ButtonWithText extends GUI_Button{
    
    private final TextObject textObject; 

    public GUI_ButtonWithText(MeshData buttonMeshData, TextObject textObject) {
        super(buttonMeshData);
        this.textObject = textObject;   
        this.textObject.setAlign(TextObject.Align.MIDDLE_CENTER);           
        this.textObject.setScale2(0.2f, 0.2f/ MainRenderer.getScreenAspectRatio());
        this.textObject.setPositionInGL(getGlPosX(), getGlPosY());
        this.textObject.setAlwaysDrawable(true);
        this.textObject.setColor(ZybColor.WHITE);  

    }

    @Override
    public void hide(){
        super.hide();
        textObject.setAlwaysDrawable(false);
    }

    @Override
    public void show(){
        super.show();
        textObject.setAlwaysDrawable(true);
    }

    @Override
    protected void onPressed() {
        textObject.setColor(ZybColor.GREEN);
    }
    
    @Override
    protected void onReleased() {
        textObject.setColor(ZybColor.WHITE);
    }    
    
}
