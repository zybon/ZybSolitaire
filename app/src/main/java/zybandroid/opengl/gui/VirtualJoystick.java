package zybandroid.opengl.gui;

import android.graphics.RectF;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.FrameDrawTimer;
import zybandroid.util.touchevents.ZybMultiTouchEvent;
import zybandroid.util.touchevents.ZybTouchPoint;

/**
 *
 * @author zybon
 * Created 2020.02.28. 19:54:00
 */
public class VirtualJoystick implements GUI_PanelElement{
    
    private GUI_Button pointerArea;
    private GUI_Button circle;
    private GUI_Button stick;
    
    private final Vector3D directionTouchVector = new Vector3D();
    private final Vector3D circleCenterVector = new Vector3D();
    
    private boolean active;
    private boolean toReset = false;
    private float resetIng = 0;
    
    private float showAreaStart;
    
    private ZybJoystickActiveChangedListener activateChangedListener = null;

    public VirtualJoystick() {
    }

    public final void setPointerArea(GUI_Button pointerArea) {
        this.pointerArea = pointerArea;
        this.pointerArea.setAlpha(0.1f);
    }
    
    public final void setCircle(GUI_Button circle) {
        this.circle = circle;
        this.circle.setReleaseWhenExit(false);
        this.circle.setAlpha(0.3f);
    }
    
    public final void setStick(GUI_Button stick) {
        this.stick = stick;
        this.stick.setAlpha(0.3f);
    }    

    public void setActivateChangedListener(ZybJoystickActiveChangedListener activateChangedListener) {
        this.activateChangedListener = activateChangedListener;
    }
    
//    public void onSurfaceChanged(float screenWidth, float screenHeight, 
//            float virtualScreenWidth, float virtualScreenHeight) {
//        pointerArea.onSurfaceChanged(screenWidth, screenHeight, virtualScreenWidth, virtualScreenHeight);
//        circle.onSurfaceChanged(screenWidth, screenHeight, virtualScreenWidth, virtualScreenHeight);
//        stick.onSurfaceChanged(screenWidth, screenHeight, virtualScreenWidth, virtualScreenHeight);
//    }  

    public boolean isActive() {
        return active;
    }
    
    public void onTouchEvent(ZybMultiTouchEvent zmte){

        if (!active){
            pointerArea.onTouchEvent(zmte);
            if (pointerArea.isPressed()){
                
                directionTouchVector.reset();
                updateCirclePosition();
                active = true;
                activeChanged();
                circle.onTouchEvent(zmte);
            }
        }else {
            circle.onTouchEvent(zmte);
            if (!circle.isPressed()){
                toReset = true;
                resetIng = 1.0f;
//                setInactive();
            }
            else {
                updateStick();
            }
        }        
    }  
    
    private void reseting(){
        resetIng -= 5f*FrameDrawTimer.getDeltaTimeInSec();
        if (resetIng<0){
            toReset = false;
            setInactive();
            return;
        }
        directionTouchVector.scale(resetIng);
        moveStickAroundCenter();
    }

    private void setInactive(){
        active = false;
        pointerArea.releaseWithoutClick();
        activeChanged();
//                showArea();    
    }
    
    private void activeChanged(){
        if (activateChangedListener != null){
            activateChangedListener.onActivate(active);
        }
    }
    
//    public void resetStickPosition(){
//        circle.moveToInNormalizedCoord(pointerArea.getGlPosX(), pointerArea.getGlPosY());
//        stick.moveToInNormalizedCoord(pointerArea.getGlPosX(), pointerArea.getGlPosY());    
//    }
    
    public void showArea(){
        pointerArea.setAlpha(0.1f);
        circle.setAlpha(0.3f); 
        stick.setAlpha(0.3f);
    }
    
    public void hideArea(){
        showAreaStart = System.nanoTime() / 1000000f;
    }
    
    private void updateCirclePosition(){
        ZybTouchPoint tp = pointerArea.getCurrentTouchPoint();
        circle.moveToInNormalizedCoord(tp.getNormalizedX(), tp.getNormalizedY());
        stick.moveToInNormalizedCoord(tp.getNormalizedX(), tp.getNormalizedY());
    }
    
    private void updateStick(){
        calcTouchVectorFromCircleCenter();
        moveStickAroundCenter();
    }
    
    private final Vector3D touchVectorFromCenter = new Vector3D();
    private void calcTouchVectorFromCircleCenter(){
        ZybTouchPoint tp = circle.getCurrentTouchPoint();
        RectF circleBorder = circle.getTouchRect();
        circleCenterVector.set(circleBorder.centerX(), circleBorder.centerY(), 0);
        touchVectorFromCenter.setX(tp.getX()-circleBorder.centerX());
        touchVectorFromCenter.setY(tp.getY()-circleBorder.centerY());
        if (touchVectorFromCenter.length()>circleBorder.width()/2.0f) {
            touchVectorFromCenter.normalize();
            touchVectorFromCenter.scale(circleBorder.width()/2.0f);
        }
        directionTouchVector.set(touchVectorFromCenter);
        directionTouchVector.scale(2.0f/circleBorder.width());
        directionTouchVector.setY(-directionTouchVector.getY());
    }
    
    private void moveStickAroundCenter(){
        stick.moveToInScreenCoord(
                circle.getTouchRect().centerX()+directionTouchVector.getX()*circle.getTouchRect().width()/2.0f,
                circle.getTouchRect().centerY()-directionTouchVector.getY()*circle.getTouchRect().width()/2.0f
        );
    }

    public Vector3D getTouchVectorFromCenter() {
        return touchVectorFromCenter;
    }

    public Vector3D getCircleCenterPositionInGl() {
        return circle.getPositionInGl();
    }
    
    public float getAxisX() {
        return active?offsetAxisValue(directionTouchVector.getX()):0;
    }
    
    public float getAxisY() {
        return active?offsetAxisValue(directionTouchVector.getY()):0;
    }   
    
    private float offsetAxisValue(float value){
        
        if (value>0){
            value = (float)Math.max(value-0.2f, 0.0)/0.8f;
        }
        if (value<0) {
            value = (float)Math.min(value+0.2f, 0.0)/0.8f;
        }        
        return value;    
    }
    
    public void draw() {
        
//        if (pointerArea.getAlpha()>0) {
//            float currentTime = System.nanoTime() / 1000000f;
//            if (currentTime-showAreaStart > 100f) {
//                pointerArea.setAlpha(pointerArea.getAlpha()-0.05f);
//                showAreaStart = currentTime;
//            }        
//            pointerArea.draw(camera);
//        }
        if (active) {
            if (toReset){
                reseting();
            }            
//            pointerArea.draw(camera);
            circle.draw();
            stick.draw();            
        }
//        else {
//            hideArea();
//            float currentTime = System.nanoTime() / 1000000f;
//            if (currentTime-showAreaStart > 100f) {
//                circle.setAlpha(circle.getAlpha()-0.1f);
//                stick.setAlpha(stick.getAlpha()-0.1f);
//                showAreaStart = currentTime;
//            }          
//        }
    }    

    public static interface ZybJoystickActiveChangedListener {

        public void onActivate(boolean active);
    }
        
}
