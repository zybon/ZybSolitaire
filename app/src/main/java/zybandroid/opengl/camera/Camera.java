package zybandroid.opengl.camera;

import android.opengl.Matrix;
import hu.zybon.zybsolitaire.MainRenderer;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.geometry.XZ_Border;
import zybandroid.opengl.objects.OpenGlObject;
import zybandroid.opengl.util.MatrixCreator;

/**
 *
 * @author zybon
 * Created 2018.01.01. 21:52:44
 */
public class Camera{
    
    private static final String TAG = "Camera";

    public static final Vector3D CAMERA_UP_VECTOR = new Vector3D(0f, 1.0f, 0f);
    
    private float screenWidth;
    private float screenHeight;    
    private float aspectRatio = 1.0f;
    private float orthoScale = 7f;    
    
    private final Vector3D EYE = new Vector3D();
    private final Vector3D TARGET = new Vector3D();
    private final Vector3D EYE_TARGET = new Vector3D();
    private final Vector3D WORLD_UP_X = new Vector3D(1f, 0.0f, 0f);
    private final Vector3D WORLD_UP_Y = new Vector3D(0f, 1.0f, 0f);
    private final Vector3D WORLD_UP_Z = new Vector3D(0f, 0.0f, 1f);
    public final Vector3D targetToCamVector = new Vector3D();
    public final Vector3D targetToCamXZVector = new Vector3D();
    public final Vector3D targetToCamYZVector = new Vector3D();
    public final Vector3D sideVector = new Vector3D();
    public final Vector3D upVector = new Vector3D();
    private float pitch;
    private float cosPitch;
    private float sinPitch;
    public float yaw;
    private float cosYaw;
    private float sinYaw;   
    
    
    public float projDeg;
    public float fieldOfView;
    public float clippingStart;
    public float clippingEnd;
    
    float zoom = 1f;
//    public final float[] VIEW_MATRIX = new float[16];
    
    public float cameraAlphaAroundY = 0;
    public float cameraAlphaAroundX = 0;
    
    public final float[] VIEW_MATRIX = new float[]{
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0, 
        0, 0, 0, 1
    };    
    public final float[] PROJECTION_MATRIX = new float[]{
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0, 
        0, 0, 0, 1
    };
    
    public final float[] VIEW_PROJECTION_MATRIX = new float[16];
    
    private final float[] MODEL_MATRIX = new float[16];

//    private final XZ_Border frustumBorder = new XZ_Border();
    private final OpenGlObject frustumHelperObject;
    private final Vector3D frustumHelperObjectVector = new Vector3D();
//    private final CameraData cameraData;




    
    public enum PROJECTION_TYPE{
        ORTHO,
        PERSPECTIVE
    }
    
    private PROJECTION_TYPE projection_type = PROJECTION_TYPE.PERSPECTIVE;

    public Camera() {
//        this.cameraData = dataCollector.getCameraData(ObjectsNames.camera);
        projDeg = 70;//cameraData.getProjDeg();
        clippingStart = 0.1f;//cameraData.getProjNear();
        clippingEnd = 100.0f;//cameraData.getProjFar();
//        initProjectionMatrix();
//        initViewProjectionMtx();
        frustumHelperObject = new OpenGlObject("camera_helper");
    }
    

//    public void moveToOriginalPosition(){
//        setPosition(EYE_ORIGINAL);
//    }

//    public CameraData getCameraData() {
//        return cameraData;
//    }

    public Vector3D getEYE() {
        return EYE;
    }
    
    public Vector3D getTARGET() {
        return TARGET;
    }
    
    public Vector3D getEyeTargetVector(){
        EYE_TARGET.set(EYE, TARGET);
        EYE_TARGET.normalize();
        return EYE_TARGET;
    }
    
    public Vector3D getPosition(){
        return EYE;
    }
    
    public final void setScreenSize(float screenWidth, float screenHeight){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        aspectRatio = screenWidth / screenHeight;
        initProjectionMatrix();
        initViewProjectionMtx();
    }

    public void rotateAroundX(float deg, boolean update){
        pitch += deg;
        if (pitch<-90) {pitch = -90;}
        if (pitch>90) {pitch = 90;}
        if (update) {
            setViewMatrixFromForgatas(); 
        }
    }
    
    public void rotateAroundY(float deg, boolean update){
        yaw += deg;
        if (update) {
            setViewMatrixFromForgatas(); 
        }
    }
    
    public void setToVerticalCamera(){
        this.WORLD_UP_Y.set(0.0f, 0.0f, -1.0f);
    }
    
    public void setUpVector(Vector3D upVector){
        this.WORLD_UP_Y.set(upVector);
    }

    public void setTarget(Vector3D target){
        TARGET.set(target);
        setViewMatrixFromLookAt();
    } 
    
    public void setPosition(Vector3D position){
        EYE.set(position);
        setViewMatrixFromLookAt();
    }
    
    public void setPosition(float x, float y, float z){
        EYE.set(x, y, z);
        setViewMatrixFromLookAt();
    }    
    
    
    public void setViewMatrix(float[] m){
        System.arraycopy(m, 0, VIEW_MATRIX, 0, 16);
        initViewProjectionMtx();
    }
 
    public void setTargetAndEye(Vector3D target, Vector3D eye){
        TARGET.set(target);
        EYE.set(eye);
        
        setViewMatrixFromLookAt();    
    }
      
    private void setViewMatrixFromLookAt(){
        targetToCamVector.set(TARGET, EYE);
        targetToCamVector.normalize();  
//        if (Math.abs(targetToCamVector.getY())>0.99f) {
//            sideVector.setCrossProductBetween(WORLD_UP_Z, targetToCamVector);
//        }
//        else {
            sideVector.setCrossProductBetween(WORLD_UP_Y, targetToCamVector);
//        }
        
        sideVector.normalize();
        upVector.setCrossProductBetween(targetToCamVector, sideVector); 
        calculateAngleAroundY();
        calculateAngleAroundX();
//        pitch = (float)(Math.toDegrees(Math.asin(-targetToCamVector.getY())));
//        yaw = (float)(Math.toDegrees(Math.acos(targetToCamVector.getZ())));
//        
//        if (targetToCamVector.getX()<0) {
//            cameraAlphaAroundY = -yaw;
//        }
//        else {
//            cameraAlphaAroundY = yaw;
//        }
//        Log.i(TAG, "lookat utan: "+pitch+", "+yaw);
        initViewMatrix();
        MatrixCreator.createModelMatrix(MODEL_MATRIX, EYE, getEyeTargetVector());
    }
    
    
    private void calculateAngleAroundY(){
        targetToCamXZVector.set(targetToCamVector);
        targetToCamXZVector.setY(0);    
        targetToCamXZVector.normalize();
//        if (targetToCamXZVector.getZ()<-0.9999f) {
//            cameraAlphaAroundY = 180.0f;
//            return;
//        }
//        if (targetToCamXZVector.getZ()>0.9999f) {
//            cameraAlphaAroundY = 0.0f;
//            return;
//        }
        
        cameraAlphaAroundY = (float)(Math.toDegrees(Math.acos(targetToCamXZVector.getZ())));
        if (targetToCamXZVector.getX()<0f) {
            cameraAlphaAroundY = -cameraAlphaAroundY;
        }   
    }    
    
    private void calculateAngleAroundX(){
        targetToCamYZVector.set(targetToCamVector);
        targetToCamYZVector.setX(0);    
        targetToCamYZVector.normalize();
//        if (targetToCamXZVector.getZ()<-0.9999f) {
//            cameraAlphaAroundY = 180.0f;
//            return;
//        }
//        if (targetToCamXZVector.getZ()>0.9999f) {
//            cameraAlphaAroundY = 0.0f;
//            return;
//        }
        
        cameraAlphaAroundX = (float)(Math.toDegrees(Math.acos(targetToCamYZVector.getZ())));
        if (targetToCamYZVector.getY()<0f) {
            cameraAlphaAroundX = -cameraAlphaAroundX;
        }   
    }    
       
    
    private void setViewMatrixFromForgatas(){
//        Log.i(TAG, "setViewMatrixFromForgatas: "+pitch+", "+yaw);
        cosPitch = (float)Math.cos(Math.toRadians(pitch));
        sinPitch = (float)Math.sin(Math.toRadians(pitch)); 
        cosYaw = (float)Math.cos(Math.toRadians(yaw));
        sinYaw = (float)Math.sin(Math.toRadians(yaw));        
        sideVector.set(cosYaw, 0, -sinYaw);
        upVector.set(sinYaw * sinPitch, cosPitch, cosYaw * sinPitch);
        targetToCamVector.set(sinYaw * cosPitch, -sinPitch, cosPitch * cosYaw);     
        initViewMatrix();
    }
    
    private void initViewMatrix(){
        VIEW_MATRIX[0] = sideVector.getX();
        VIEW_MATRIX[4] = sideVector.getY();
        VIEW_MATRIX[8] = sideVector.getZ();
        VIEW_MATRIX[12] = -sideVector.dotProduct(EYE);
           
        VIEW_MATRIX[1] = upVector.getX(); 
        VIEW_MATRIX[5] = upVector.getY();
        VIEW_MATRIX[9] = upVector.getZ(); 
        VIEW_MATRIX[13] = -upVector.dotProduct(EYE); 
                
        VIEW_MATRIX[2] = targetToCamVector.getX(); 
        VIEW_MATRIX[6] = targetToCamVector.getY();
        VIEW_MATRIX[10] = targetToCamVector.getZ(); 
        VIEW_MATRIX[14] = -targetToCamVector.dotProduct(EYE); 
                
        VIEW_MATRIX[3] = 0f;
        VIEW_MATRIX[7] = 0f;
        VIEW_MATRIX[11] = 0f; 
        VIEW_MATRIX[15] = 1f;     
        initViewProjectionMtx();
    }
    
    public void initViewProjectionMtx(){
        Matrix.multiplyMM(VIEW_PROJECTION_MATRIX, 0, PROJECTION_MATRIX, 0, VIEW_MATRIX, 0);
    }  
    
//    public final void setToOrto(){
//        if (aspectRatio>1) {
//            // Landscape
//            Matrix.orthoM(PROJECTION_MATRIX, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            // Portrait or square
//            Matrix.orthoM(PROJECTION_MATRIX, 0, -1f, 1f, -1/aspectRatio, 1/aspectRatio, -1f, 1f);
//        }  
//        zoom = 1;
//    }   

    public void setClippingEnd(float clippingEnd) {
        this.clippingEnd = clippingEnd;
    }

    public float getClippingEnd() {
        return clippingEnd;
    }
    
    public void closeFOV() {
        setProjDeg(projDeg-1.0f);
    }
    
    public void openFOV() {
        setProjDeg(projDeg+1.0f);
    }

    public void setProjDeg(float projDeg){
        this.projDeg = projDeg;
        initProjectionMatrix();
        
    }
    
    public float getProjDeg() {
        return projDeg;
    }   
    
    public void setProjection_type(PROJECTION_TYPE projection_type) {
        this.projection_type = projection_type;
    }
    
    public final void initProjectionMatrix(){
        switch (projection_type) {
            case PERSPECTIVE:
                initProjectionMatrixPerspective();
                break;
            case ORTHO:
                initProjectionMatrixToOrtho();                
        }
    }

    public void setOrthoScale(float orthoScale) {
        this.orthoScale = orthoScale;
    }
    
    public void addOrthoScale(){
        this.orthoScale += 1.0f;
        initProjectionMatrix();
    }
    
    public void minusOrthoScale(){
        this.orthoScale -= 1.0f;
        initProjectionMatrix();
    }    

    public float getOrthoScale() {
        return orthoScale;
    }
    
    
    
    private void initProjectionMatrixToOrtho(){
        
        final float left = -aspectRatio*orthoScale;
        final float right = aspectRatio*orthoScale;
        final float bottom = -1*orthoScale;
        final float top = 1*orthoScale;
        
        final float r_width  = 1.0f / (right - left);
        final float r_height = 1.0f / (top - bottom);
        final float r_depth  = 1.0f / (clippingEnd - clippingStart);
        final float x =  2.0f * (r_width);
        final float y =  2.0f * (r_height);
        final float z = -2.0f * (r_depth);
        final float tx = -(right + left) * r_width;
        final float ty = -(top + bottom) * r_height;
        final float tz = -(clippingEnd + clippingStart) * r_depth;
        PROJECTION_MATRIX[0] = x;
        PROJECTION_MATRIX[5] = y;
        PROJECTION_MATRIX[10] = z;
        PROJECTION_MATRIX[12] = tx;
        PROJECTION_MATRIX[13] = ty;
        PROJECTION_MATRIX[14] = tz;
        PROJECTION_MATRIX[15] = 1.0f;
        PROJECTION_MATRIX[1] = 0.0f;
        PROJECTION_MATRIX[2] = 0.0f;
        PROJECTION_MATRIX[3] = 0.0f;
        PROJECTION_MATRIX[4] = 0.0f;
        PROJECTION_MATRIX[6] = 0.0f;
        PROJECTION_MATRIX[7] = 0.0f;
        PROJECTION_MATRIX[8] = 0.0f;
        PROJECTION_MATRIX[9] = 0.0f;
        PROJECTION_MATRIX[11] = 0.0f;       
        
    }      
       
    
    private void initProjectionMatrixPerspective(){
        calcFieldOfView();
        
        final float angleInRadius = (float)(projDeg * Math.PI/180.0);
        
        final float a = (float) (1.0 / Math.tan(angleInRadius/2.0));
        
        PROJECTION_MATRIX[0] = a / aspectRatio;
        PROJECTION_MATRIX[1] = 0f;
        PROJECTION_MATRIX[2] = 0f;
        PROJECTION_MATRIX[3] = 0f;
        
        PROJECTION_MATRIX[4] = 0f;
        PROJECTION_MATRIX[5] = a;
        PROJECTION_MATRIX[6] = 0f;
        PROJECTION_MATRIX[7] = 0f;
        
        PROJECTION_MATRIX[8] = 0f;
        PROJECTION_MATRIX[9] = 0f;
        PROJECTION_MATRIX[10] = -((clippingEnd + clippingStart) / (clippingEnd - clippingStart));
        PROJECTION_MATRIX[11] = -1f;
        
        PROJECTION_MATRIX[12] = 0f;
        PROJECTION_MATRIX[13] = 0f;
        PROJECTION_MATRIX[14] = -((2f * clippingEnd * clippingStart) / (clippingEnd - clippingStart));
        PROJECTION_MATRIX[15] = 0f;        
        
    }      
    
    private void calcFieldOfView(){
        final float angleInRadius = (float)(projDeg * Math.PI/180.0);
        final float fieldOfViewInRadius = (float)(2f * Math.atan((0.5f * screenWidth) / (0.5f * screenHeight / Math.tan(angleInRadius/2.0))));
        fieldOfView = (float)Math.toDegrees(fieldOfViewInRadius);
    }
    
    public XZ_Border getFrustumXZ_Border(float distance){
        XZ_Border frustumBorder = new XZ_Border();
        //frustumBorder.reset();
        //near
        frustumHelperObject.setModelMatrix(MODEL_MATRIX);
//        if (nearDistance>0.0f) {      
//            frustumHelperObject.translate(0, 0, -nearDistance);
//        }
        frustumHelperObjectVector.set(frustumHelperObject.getPosition());
        frustumBorder.setWithCompare(frustumHelperObjectVector.getX(), frustumHelperObjectVector.getZ());
        
        //far
        frustumHelperObject.setModelMatrix(MODEL_MATRIX);
        frustumHelperObject.translate(0, 0, -distance);
        frustumHelperObjectVector.set(frustumHelperObject.getPosition());
        frustumBorder.setWithCompare(frustumHelperObjectVector.getX(), frustumHelperObjectVector.getZ());        
        
        //far right
        frustumHelperObject.setModelMatrix(MODEL_MATRIX);
        frustumHelperObject.rotate_around_Y(-fieldOfView/2.0f);
        frustumHelperObject.translate(0, 0, -distance);
        frustumHelperObjectVector.set(frustumHelperObject.getPosition());
        frustumBorder.setWithCompare(frustumHelperObjectVector.getX(), frustumHelperObjectVector.getZ());
        
        //far left
        frustumHelperObject.setModelMatrix(MODEL_MATRIX);
        frustumHelperObject.rotate_around_Y(fieldOfView/2.0f);
        frustumHelperObject.translate(0, 0, -distance);
        frustumHelperObjectVector.set(frustumHelperObject.getPosition());
        frustumBorder.setWithCompare(frustumHelperObjectVector.getX(), frustumHelperObjectVector.getZ());    
        
        return frustumBorder;
    }
        
   
    
    @Override
    public String toString() {
        return "Camera{" + "EYE=" + EYE + '}';
    }

    

}
