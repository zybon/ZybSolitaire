package zybandroid.opengl.gui;

import java.util.ArrayList;
import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author zybon
 * Created 2020.05.26. 9:35:18
 */
public class GUI_Panel implements GUI_PanelElement{
    
    private final ArrayList<GUI_PanelElement> panelElements = new ArrayList<GUI_PanelElement>();

    public GUI_Panel() {
    }

    public final void add(GUI_PanelElement panelElement){
        this.panelElements.add(panelElement);
    }
    
//    public void onSurfaceChanged(float screenWidth, float screenHeight, float virtualScreenWidth, float virtualScreenHeight) {
//        for (GUIPanelElement panelElement : panelElements) {
//            panelElement.onSurfaceChanged(screenWidth, screenHeight, virtualScreenWidth, virtualScreenHeight);
//        }
//    }    
    
    public void onTouchEvent(ZybMultiTouchEvent zmte) {
        for (GUI_PanelElement panelElement : panelElements) {
            panelElement.onTouchEvent(zmte);
        }
    }    
    
    public void draw(){
        for (GUI_PanelElement panelElement : panelElements) {
            panelElement.draw();
        }
    }
    
}
