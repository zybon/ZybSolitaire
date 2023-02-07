package zybandroid.util.touchevents;

import hu.zybon.zybsolitaire.MainRenderer;

/**
 *
 * @author zybon
 * Created 2020.02.28. 11:14:23
 */
public class ZybTouchPoint {

    private int pointerId;

    private float x;
    private float y;
    
    private float normalizedX;
    private float normalizedY;   

    public ZybTouchPoint() {
    }
    
    
    
    public ZybTouchPoint(int pointerId, float x, float y) {
        this.pointerId = pointerId;
        this.x = x;
        this.y = y;
        this.normalizedX = MainRenderer.calcNormalizedX(x);
        this.normalizedY = MainRenderer.calcNormalizedY(y);
    }
    
    public void copyFrom(ZybTouchPoint ztp) {
        this.pointerId = ztp.pointerId;
        this.x = ztp.x;
        this.y = ztp.y;
        this.normalizedX = ztp.normalizedX;
        this.normalizedY = ztp.normalizedY;
    }    
    
    public int getPointerId() {
        return pointerId;
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getNormalizedX() {
        return normalizedX;
    }

    public float getNormalizedY() {
        return normalizedY;
    }
    
    @Override
    public String toString() {
        return "pointerId:"+pointerId+" (x="+x+", y="+y+")";
    }



}    

