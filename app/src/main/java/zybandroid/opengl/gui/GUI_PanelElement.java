package zybandroid.opengl.gui;

import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author zybon    
 * 
 */
public interface GUI_PanelElement {

    public void draw();
    public void onTouchEvent(ZybMultiTouchEvent zmte);
    
}
