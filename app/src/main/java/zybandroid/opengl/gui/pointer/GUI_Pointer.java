package zybandroid.opengl.gui.pointer;

import android.opengl.Matrix;
import zybandroid.opengl.camera.Camera;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.gui.GUI_Button;
import zybandroid.opengl.gui.GUI_Line;
import zybandroid.util.touchevents.listeners.ZybOnClickListener;

/**
 *
 * @author zybon
 * Created 2020.09.09. 10:08:35
 */
public class GUI_Pointer {
    
    private GUI_Button pointer;   
    private GUI_Button pointerTarget;
    
    private final GUI_Line line;
    
    private boolean visible = false;
    
    public GUI_Pointer() {
        this.line = new GUI_Line();
    }

    public void setPointer(GUI_Button pointer) {
        this.pointer = pointer;
    }

    public void setPointerTarget(GUI_Button pointerTarget) {
        this.pointerTarget = pointerTarget;
    }
    
    public void hide(){
        this.visible = false;
    }
    
    public void show(){
        this.visible = true;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public void setTargetPosition(Vector3D targetPositonOnScreen, Vector3D grabPositionOnScreen){
        pointerTarget.translateTo(targetPositonOnScreen);

        line.setStartPoint(targetPositonOnScreen);
        line.setEndPoint(grabPositionOnScreen);
        
        pointer.setPositionInOpengGl(grabPositionOnScreen);
        
        pointer.show();    
    }
    
    public void drawPointer(){
    
    }
    
    public void drawLine(){
    
    }
    

}
