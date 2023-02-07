package zybandroid.util.touchevents;

import android.view.MotionEvent;

import java.util.ArrayList;

/**
 *
 * @author zybon
 * Created 2018.10.16. 16:01:49
 */
public class ZybMultiTouchEvent {
    
    private final ArrayList<ZybTouchPoint> touchPoints = new ArrayList<ZybTouchPoint>();
    private MotionEvent currentEvent = MotionEvent.obtain(
            0, 0, 0, 0, 0, 0);

    public ZybMultiTouchEvent() {
    }
    
    public static String ACTION_STRING = "";
    public static String POINTER_COUNT = "";
    
    private int ACTION;
    
    private ZybTouchPoint actionTouchPoint;

//    private int update = 0;
    
    public final void update(MotionEvent event) {
        this.currentEvent = event;
        ACTION_STRING = event.getActionIndex()+" "+actionToString(event);
        ACTION = event.getActionMasked();
        int currentId = event.getPointerId(event.getActionIndex());
        int pointerIndex = event.findPointerIndex(currentId);
        actionTouchPoint = new ZybTouchPoint(currentId, 
                event.getX(pointerIndex), event.getY(pointerIndex)
        );
        touchPoints.clear();
//        update++;
//        if (isActionDown()) {
//            MainRenderer.sendDebugInfo("update: "+update+"\n"+ACTION_STRING);            
//        }          
        if (isActionUp()) {
            POINTER_COUNT = "0 = "+touchPoints.size();
            return;
        }        
        if (isActionDown() ||
                isActionPointerDown() ||
                   isActionMove()
                ) {
            addTouchPointers(event);   
            POINTER_COUNT = event.getPointerCount()+" = "+touchPoints.size();
        }
        if (isActionPointerUp()) {
            addTouchPointersExceptThisAction(event);
            POINTER_COUNT = (event.getPointerCount()-1)+" = "+touchPoints.size();
            
        }

    }

    public static String actionToString(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) { return "Down";}
        if (event.getActionMasked()== MotionEvent.ACTION_POINTER_DOWN){ return "Pointer Down";}
        
//        if (event.getActionMasked() == MotionEvent.ACTION_MOVE){ return "Move";}
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE){ return "Pointer Move";}
        
        
        if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){ return "Pointer Up";}
        if (event.getActionMasked() == MotionEvent.ACTION_UP){ return "Up";}
        
        if (event.getActionMasked() == MotionEvent.ACTION_OUTSIDE){ return "Outside";}
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL){ return "Cancel";}
        return "UNDEFINIED!!!!";
    }
    
//    private void addTouchPointer(MotionEvent event){
//        int pointerId = event.getPointerId(event.getActionIndex());
//        int pointerIndex = event.findPointerIndex(pointerId);
//        touchPoints.add(new ZybTouchPoint(pointerId, event.getX(pointerIndex), event.getY(pointerIndex)));    
//        
//    }    
//    
//    private void removeTouchPointer(MotionEvent event){
//        int pointerId = event.getPointerId(event.getActionIndex());
//        touchPoints.remove(pointerId);    
//        
//    }      
    
    public int getActionPointerId(){
        return actionTouchPoint.getPointerId();
    }
    
    public ZybTouchPoint getActionTouchPoint(){
        return actionTouchPoint;
    }    
    
    public boolean hasTouchPoint(){
        return touchPoints.size()>0;
    }
    
    public boolean isActionDown(){
        return ACTION == MotionEvent.ACTION_DOWN;
    }
    
    public boolean isActionPointerDown(){
        return ACTION == MotionEvent.ACTION_POINTER_DOWN;
    }
    
    public boolean isActionMove(){
        return ACTION == MotionEvent.ACTION_MOVE;
    }
    
    public boolean isActionPointerUp(){
        return ACTION == MotionEvent.ACTION_POINTER_UP;
    }
    
    public boolean isActionUp(){
        return ACTION == MotionEvent.ACTION_UP;
    }    
    
    public int getEventPointerCount(){
        return currentEvent.getPointerCount();
    }
    
    private void addTouchPointers(MotionEvent event){
        int pointerId;
        int pointerIndex;
        for (int i = 0; i < event.getPointerCount(); i++) {
            pointerId = event.getPointerId(i);
            pointerIndex = event.findPointerIndex(pointerId);
            touchPoints.add(new ZybTouchPoint(pointerId, event.getX(pointerIndex), event.getY(pointerIndex)));    
        }
        
    }
    
    private void addTouchPointersExceptThisAction(MotionEvent event){
        int currentId = event.getPointerId(event.getActionIndex());
        int pointerId;
        int pointerIndex;
        for (int i = 0; i < event.getPointerCount(); i++) {
            pointerId = event.getPointerId(i);
            if (pointerId != currentId) {
                pointerIndex = event.findPointerIndex(pointerId);
                touchPoints.add(new ZybTouchPoint(pointerId, event.getX(pointerIndex), event.getY(pointerIndex)));       
            }
        }
    }
    
    
    public boolean isTouchPointActive(int pointerId){
        for (ZybTouchPoint touchPoint : touchPoints) {
            if (touchPoint.getPointerId() == pointerId){
                return true;
            }
        }
        return false;
    }

    public int getTouchPointCount(){
        return touchPoints.size();
    }
    
    public ArrayList<ZybTouchPoint> getTouchPoints() {
        return touchPoints;
    }
    
    public ZybTouchPoint getTouchPointByPointerId(int pointerId) {
        for (ZybTouchPoint touchPoint : touchPoints) {
            if (touchPoint.getPointerId() == pointerId){
                return touchPoint;
            }
        }   
        throw new AssertionError("invalid pointerId: "+pointerId);
    }    

    public MotionEvent getCurrentEvent() {
        return currentEvent;
    }
    
    private String e = "NONE";
    @Override
    public String toString() {
        if (currentEvent == null){
            return "NONE";
        }
//        if (currentEvent.getAction() == MotionEvent.ACTION_MOVE){
//            return e;
//        }
        e = ACTION_STRING +" -> "+ currentEvent.getPointerId(currentEvent.getActionIndex()) +"\n";
        int pointerId;
        int pointerIndex;
        for (int i = 0; i < currentEvent.getPointerCount(); i++) {
            pointerId = currentEvent.getPointerId(i);
            pointerIndex = currentEvent.findPointerIndex(pointerId);
            e += i+": (id: "+ pointerId +")" +
                  " x=" +  currentEvent.getX(pointerIndex) +
                  " y=" +  currentEvent.getY(pointerIndex) +
                  " s=" +  currentEvent.getSize(pointerIndex) + "\n";
        }        
        return e;
    }


    
    


}
