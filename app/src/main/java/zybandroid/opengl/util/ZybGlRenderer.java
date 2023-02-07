/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zybandroid.opengl.util;

import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author root
 */
public interface ZybGlRenderer {
    
    public void draw();
    public void onTouchEvent(ZybMultiTouchEvent zmte);
    public boolean onBackPressToFinish();
    
}
