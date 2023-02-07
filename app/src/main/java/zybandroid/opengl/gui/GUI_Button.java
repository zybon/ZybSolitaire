package zybandroid.opengl.gui;

import android.graphics.RectF;

import hu.zybon.zybsolitaire.MainRenderer;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.programs.GUIShaderProgram;
import zybandroid.opengl.util.ZybColor;
import zybandroid.util.touchevents.ZybMultiTouchEvent;
import zybandroid.util.touchevents.ZybTouchPoint;
import zybandroid.util.touchevents.listeners.ZybOnClickListener;
import zybandroid.util.touchevents.listeners.ZybOnMovedListener;
import zybandroid.util.touchevents.listeners.ZybOnPressedListener;
import zybandroid.util.touchevents.listeners.ZybOnReleasedListener;

/**
 *
 * @author zybon
 * Created 2018.10.03. 15:40:38
 */
public class GUI_Button extends GUI_Mesh implements GUI_PanelElement{
    
    private boolean sticky = false;
    private boolean sticked = false;

    private float scaleX;
    private float scaleY;
    
    private float aspectRatio;

    boolean prevPressedState = false;
    boolean pressed = false;
    float pressedStartTime;
    
    private final RectF touchRect = new RectF();
    private final RectF touchOrigRect = new RectF();
    int infoInt = -1;

    private final GUIShaderProgram guiShaderProgram = GUIShaderProgram.getGUIShaderProgram();
    
    private float alpha = 1.0f;
    private float percent = 1.0f;
    private final Vector3D glPosition = new Vector3D();
    private final Vector3D glOrigPosition = new Vector3D();
//    private float glWidth;
//    private float glHeight;    
    
    private float glDrawOffsetX;
    private float glDrawOffsetY;    
    
    private final ZybTouchPoint startTouchPoint = new ZybTouchPoint();
    private final ZybTouchPoint currentTouchPoint = new ZybTouchPoint();
    private final Vector3D lastMoveVector = new Vector3D();
    private final Vector3D moveSinceStartVector = new Vector3D();
    
    private boolean pressWhenEnter = false;
    private boolean releaseWhenExit = true;
    private int pointerId = -1;
    
    private final ZybColor baseColor = new ZybColor(ZybColor.WHITE);
    private final ZybColor pressedColor = new ZybColor(ZybColor.DARKGREY);
    private boolean coloringWhenPressed = true;
    
    private ZybOnPressedListener onPressedListener;
    private ZybOnMovedListener onMovedListener;
    private ZybOnReleasedListener onReleasedListener;
    private ZybOnClickListener onClickListener;

    private float screenWidth;
    private float screenHeight;
    
    public GUI_Button(MeshData buttonMeshData) {
        super(buttonMeshData);
        placeOnVirtualScreen();
    }
    
    private void placeOnVirtualScreen(){
        this.glPosition.setX(getMeshData().getPosition().getX()/(MainRenderer.virtualScreenWidth/2.0f));
        this.glPosition.setY(getMeshData().getPosition().getY()/(MainRenderer.virtualScreenHeight/2.0f));
        this.glOrigPosition.set(glPosition);

        scaleX = getMeshData().getDimensions().getX()/MainRenderer.virtualScreenWidth;
        scaleY = getMeshData().getDimensions().getY()/MainRenderer.virtualScreenHeight;
        aspectRatio = getMeshData().getDimensions().getX()/getMeshData().getDimensions().getY();

        screenWidth = MainRenderer.getScreenWidth();
        screenHeight = MainRenderer.getScreenHeight();

        float width = scaleX*screenWidth;
        float height = scaleY*screenHeight;

        float left = ((glPosition.getX())+1.0f)*(screenWidth/2.0f)-width/2.0f;
        float top = (1.0f-(glPosition.getY()))*(screenHeight/2.0f)-height/2.0f;
        touchRect.set(left, top, left+width, top+height);
        touchOrigRect.set(touchRect);

        resetPosition();
//        updateTouchRect();

    }

    private boolean screenSizeChanged(){
        return Math.abs(screenWidth-MainRenderer.getScreenWidth())>0.01 ||
                Math.abs(screenHeight-MainRenderer.getScreenHeight())>0.01;
    }
    
    public void setBaseColor(ZybColor color){
        this.baseColor.set(color);
    }
    
    public void setBaseColor(float r, float g, float b, float a){
        this.baseColor.set(r, g, b, a);
    }    
    
    public void setPressedColor(ZybColor color){
        this.pressedColor.set(color);
    }    

    public void setColoringWhenPressed(boolean coloringWhenPressed) {
        this.coloringWhenPressed = coloringWhenPressed;
    }
    
    public float getAspectRatio() {
        return aspectRatio;
    }
   
//    public void onSurfaceChanged(float screenWidth, float screenHeight, 
//            float virtualScreenWidth, float virtualScreenHeight) {
//        this.glPosX = getMeshData().getPosition().getX()/(virtualScreenWidth/2.0f);
//        this.glPosY = getMeshData().getPosition().getY()/(virtualScreenHeight/2.0f);
//        scaleX = getMeshData().getDimensions().getX()/virtualScreenWidth;
//        scaleY = getMeshData().getDimensions().getY()/virtualScreenHeight;
//        aspectRatio = getMeshData().getDimensions().getX()/getMeshData().getDimensions().getY();
//        this.screenWidth = screenWidth;
//        this.screenHeight = screenHeight;
//        setPositionInOpengGl();
//        updateTouchRect();
//    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public void setReleaseWhenExit(boolean releaseWhenExit) {
        this.releaseWhenExit = releaseWhenExit;
    }

    public void setPressWhenEnter(boolean pressWhenEnter) {
        this.pressWhenEnter = pressWhenEnter;
    }

    public void setOnPressedListener(ZybOnPressedListener onPressedListener) {
        this.onPressedListener = onPressedListener;
    }

    public void setOnMovedListener(ZybOnMovedListener onMovedListener) {
        this.onMovedListener = onMovedListener;
    }

    public void setOnReleasedListener(ZybOnReleasedListener onReleasedListener) {
        this.onReleasedListener = onReleasedListener;
    }

    public void setOnClickListener(ZybOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    
    public void setPositionInOpengGl(Vector3D newPositionGl){
        this.glPosition.set(newPositionGl);
        updatePosition(); 
        updateTouchRect();
    }    

    public void resetPosition(){
        setOrigLocation(glPosition);
//        pressedLocation.set(glPosX+0.01f, glPosY-0.01f, 0);
        updatePosition();
        scaleToOrig();
    }
    
    public void scaleToOrig(){
        scale(scaleX, scaleY, 1);        
    }

    public void scaleTo(float scaleX, float scaleY){
        resetModelMatrix();
        scale(scaleX, scaleY, 1);
        updatePosition();
        updateTouchRect();
    }

    public void setToSquereBasedX(){
        resetModelMatrix();
        scale(scaleX, scaleX/MainRenderer.getScreenAspectRatio(), 1);
        updatePosition();
        updateTouchRect();
    }

    public void setToSquereBasedY(){
        resetModelMatrix();
        scale(scaleY*MainRenderer.getScreenAspectRatio(), scaleY, 1);
        updatePosition();
        updateTouchRect();
    }
    
    public Vector3D getPositionInGl(){
        return glPosition;
    }
    
    public void setTouhArea(MeshData meshData){
        float touchGlPosX = meshData.getPosition().getX()/(MainRenderer.virtualScreenWidth/2.0f);
        float touchGlPosY = meshData.getPosition().getY()/(MainRenderer.virtualScreenHeight/2.0f);
        float touchScaleX = meshData.getDimensions().getX()/MainRenderer.virtualScreenWidth;
        float touchScaleY = meshData.getDimensions().getY()/MainRenderer.virtualScreenHeight;

        float width = touchScaleX*MainRenderer.getScreenWidth();
        float height = touchScaleY*MainRenderer.getScreenHeight();
        float left = ((touchGlPosX)+1.0f)*(MainRenderer.getScreenWidth()/2.0f)-width/2.0f;
        float top = (1.0f-(touchGlPosY))*(MainRenderer.getScreenHeight()/2.0f)-height/2.0f;
        touchRect.set(left, top, left+width, top+height);
        touchOrigRect.set(touchRect);
    }

    private void updateTouchRect(){
        float dx = ((glPosition.getX()-glOrigPosition.getX())/2f)*MainRenderer.getScreenWidth();
        float dy = ((glOrigPosition.getY()-glPosition.getY())/2f)*MainRenderer.getScreenHeight();
        touchRect.set(touchOrigRect);
        touchRect.offset(dx,dy);
    }

    public RectF getTouchRect() {
        return touchRect;
    }
    
    boolean pointInside(ZybTouchPoint p){
        return touchRect.contains(p.getX(), p.getY());
    }

    public void setGlDrawOffset(float glDrawOffsetX, float glDrawOffsetY) {
        this.glDrawOffsetX = glDrawOffsetX;
        this.glDrawOffsetY = glDrawOffsetY;
        updatePosition();
    }




    public void setInfoInt(int infoInt) {
        this.infoInt = infoInt;
    }

    public int getInfoInt() {
        return infoInt;
    }
    
//    public float getGlHeight(){
//        return scaleY/2.0f;
//    }
    
    public void updatePosition(){
        if (pressed) {
            translateTo(glPosition.getX()+0.03f+glDrawOffsetX, glPosition.getY()-0.03f+glDrawOffsetY, 0);
        }
        else {
            translateTo(glPosition.getX()+glDrawOffsetX, glPosition.getY()+glDrawOffsetY, 0);
        }
 
    }
    
    public void updateScale(){
        scale(scaleX, scaleY, 1);
    }
    
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }
    
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }    

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    public void setScaleWithAspect(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }    

    public float getGlPosX() {
        return glPosition.getX();
    }

    public float getGlPosY() {
        return glPosition.getY();
    }

    public float getGlWidth() {
        return scaleX*2.0f;
    }

    public float getGlHeight() {
        return scaleY*2.0f;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }
    
    
    public void onTouchEvent(ZybMultiTouchEvent zmte) {
        if (!isVisible()) {
            return;
        }
        if (zmte.isActionUp()){
            if (pointerId == zmte.getActionPointerId()){
                release();
            }
            else {
                releaseWithoutClick();
            }
            return;
        }         
        if (!zmte.hasTouchPoint()){
            releaseWithoutClick();
            return;
        }
        if (!pressed) {
            if (zmte.isActionDown() || zmte.isActionPointerDown()){
                if (pointInside(zmte.getActionTouchPoint())){
                    actionDown(zmte.getActionTouchPoint());
                }
                return;
            }            
            if (pressWhenEnter && zmte.isActionMove()){
                for (ZybTouchPoint tp: zmte.getTouchPoints()) {
                    if (pointInside(tp)) {
                        actionDown(tp);
                        return;
                    }
                }
            }
        }
        else {
            if (!zmte.isTouchPointActive(pointerId) && zmte.getActionPointerId()!=pointerId){
                releaseWithoutClick();
                return;
            }
            if (zmte.isActionPointerUp()){
                if (pointerId == zmte.getActionPointerId()){
                    ZybTouchPoint tp = zmte.getActionTouchPoint();
                    currentTouchPoint.copyFrom(tp);
                    release();
                    return;
                }
            }
            if (zmte.isActionMove()){
                ZybTouchPoint tp = zmte.getTouchPointByPointerId(pointerId);
                if (pointInside(tp)) {
                    actionMove(tp);
                }
                else {
                    if (releaseWhenExit){
                        releaseWithoutClick();
                    }
                    else {
                        actionMove(tp);
                    }
                }
                

            }            
        }
    
    }  
    
    private void actionDown(ZybTouchPoint tp){
        startTouchPoint.copyFrom(tp);
        currentTouchPoint.copyFrom(tp);
        moveSinceStartVector.reset();
        lastMoveVector.reset();
        pointerId = tp.getPointerId();
        pressed = true;
        if (onPressedListener != null){
            onPressedListener.onPressed();
        }
        onPressed();

    }
    
    protected void onPressed(){}
    
    private void actionMove(ZybTouchPoint tp){
        lastMoveVector.set(tp.getX()-currentTouchPoint.getX(),
                        tp.getY()-currentTouchPoint.getY(), 
                        0);
        moveSinceStartVector.set(tp.getX()-startTouchPoint.getX(),
                        tp.getY()-startTouchPoint.getY(), 
                        0);        
        currentTouchPoint.copyFrom(tp);
        if (onMovedListener != null){
            onMovedListener.onMoved();
        }        
        onMove();
    }
    
    protected void onMove(){}
    
    public void releaseWithoutClick(){
        if (pressed) {
            pressed = false;
            pointerId = -1;   
            if (onReleasedListener != null){
                onReleasedListener.onReleased();
            }
            onReleased();
        }
       
    }  
    
    protected void onReleased(){}
    
    public void release(){
        if (pressed) {
            if (onClickListener != null){
                if (pointInside(currentTouchPoint)) {
                    onClickListener.onClick();
                }
                onClick();
            }         
            pressed = false;
            pointerId = -1;   
            if (onReleasedListener != null){
                onReleasedListener.onReleased();
            }
            onReleased();
            if (sticky){
                sticked = !sticked;
            }
        }
       
    }
    
    protected void onClick(){}

    public Vector3D getLastMoveVector() {
        return lastMoveVector;
    }

    public Vector3D getMoveSinceStartVector() {
        return moveSinceStartVector;
    }
    
    public ZybTouchPoint getCurrentTouchPoint() {
        return currentTouchPoint;
    }
    
    public void moveToInScreenCoord(float x, float y) {
        this.glPosition.set(MainRenderer.calcNormalizedX(x), MainRenderer.calcNormalizedY(y), 0);
        updateTouchRect();
        updatePosition();
    }    

    public void moveToInNormalizedCoord(float x, float y) {
        this.glPosition.set(x, y, 0);
        updateTouchRect();
        updatePosition();
    }

    public void moveInNormalizedCoord(float x, float y) {
        this.glPosition.translate(x, y, 0);
        updateTouchRect();
        updatePosition();
    }    
    
     
    
//    public void onTouchEvent(ZybMultiTouchEvent zmte) {
//        currentTouchPoint.reset();
//        boolean state = false;
//        for (ZybTouchPoint tp : zmte.getTouchPoints()) {
//            if (pointInside(tp)) {
//                currentTouchPoint.set(tp);
//                state = true;
//                break;
//            }                        
//        }
//        prevPressedState = pressed;
//        if (state != pressed) {
//            
//            pressed = state;
//            if (pressed && !prevPressedState) {
//                pressedStartTime = System.nanoTime() / 1000000000f;
//            }
////            updatePosition();
//        }        
//    }     

//    public ZybTouchPoint getCurrentTouchPoint() {
//        return currentTouchPoint;
//    }
//    
    
    
    
//    public void onTouchEvent_Er(ZybMultiTouchEvent zmte) {
//        boolean state = false;
//        for (ZybTouchPoint tp : zmte.getTouchPoints()) {
//            if (tp.getState() != 0) {
//                if (pointInside(tp)) {
//                    state = true;
//                    break;
//                }                        
//            }
//        }
//
//        if (state != pressed) {
//            pressed = state;
//            updatePosition();
//        }        
//    }        

    public boolean isPressed() {
        if (sticky && sticked){
            return true;
        }
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
    
    public void setSticked(boolean sticked){
        this.sticked = false;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setPercentValue(float percent){
        this.percent = percent;
    }

    public float getPercent() {
        return percent;
    }

    @Override
    public void draw() {
        if (alpha<=0 || !isVisible()) {return;}
//        if (screenSizeChanged()){
//            placeOnVirtualScreen();
//        }
        setUniforms();
        setAttributeDatas();
        onDraw();
    }   
    
    @Override
    protected void setUniforms() {
        loadUniformModelMatrix(guiShaderProgram.getuMMatrixLocation());
        guiShaderProgram.loadAlphaUniform(getAlpha());
        guiShaderProgram.loadPercentUniform(percent);
        if (coloringWhenPressed && isPressed()){
            guiShaderProgram.loadColorUniform(pressedColor);
        }
        else {
            guiShaderProgram.loadColorUniform(baseColor);
        }

    }

    @Override
    protected void setAttributeDatas() {
        setAttributePositionData(guiShaderProgram.getaPositionLocation());
        setAttributeTextureCoordData(guiShaderProgram.getaTextureCoordinatesLocation());              
    }

    @Override
    protected void onDraw() {
        drawWithTriangles();
    }    


    
    
}
      
