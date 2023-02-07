package zybandroid.opengl.objects;

import static android.opengl.GLES20.glUniformMatrix4fv;
import android.opengl.Matrix;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.transposeM;
import zybandroid.opengl.camera.Camera;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.MatrixCreator;

/**
 *
 * @author zybon
 * Created 2019.04.03. 11:45:00
 */
public class OpenGlObject{
    
    private static final String TAG = "OpenGlObject";
    
    public static final float[] IDENTITY_MATRIX = new float[]{
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0, 
        0, 0, 0, 1
    };     
    
    private final float[] modelMatrix = new float[]{
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0, 
        0, 0, 0, 1
    };    
    
    private final float[] modelViewMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] it_modelViewMatrix = new float[16];
    private final float[] sTemp = new float[16]; 
    private final float[] translateAbsoluteTemp = new float[16];
    
    private final Vector3D positionOrigin = new Vector3D(0,0,0);
    private final Vector3D positionCurrent = new Vector3D();
    private final Vector3D origLocation = new Vector3D();
    private final Vector3D cameraPosition = new Vector3D(0,0,0);
    
    private final String name;
    
    private OpenGlObject parent = null;
    private final Vector3D relPosToParent = new Vector3D();    
    
    public OpenGlObject(String name) { 
        this.name = name;
    }       

    public String getName() {
        return name;
    }

    public final void setOrigLocation(Vector3D origLocation) {
        this.origLocation.set(origLocation);
    }
    
    public final void setOrigLocation(float x, float y, float z) {
        this.origLocation.set(x, y, z);
    }    
    
    public Vector3D getOrigLocation() {
        return origLocation;
    }
    
    public void loadUniformModelMatrix(int uMMatrixLocation){
        glUniformMatrix4fv(uMMatrixLocation, 1, false, modelMatrix, 0);
    }
    
    public void loadUniformModelViewMatrix(int uMVMatrixLocation){
        glUniformMatrix4fv(uMVMatrixLocation, 1, false, modelViewMatrix, 0);
    }    
    
    public void loadUniformMVPMatrix(int uMVPMatrixLocation){
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, modelViewProjectionMatrix, 0);
    }    
    
    public void updateMVMatrix(Camera camera) {
        multiplyMM(modelViewMatrix, 0, camera.VIEW_MATRIX, 0, modelMatrix, 0);
    }  
    
    public void updateIT_MVMatrix(Camera camera) {
        invertM(sTemp, 0, modelViewMatrix, 0);
        transposeM(it_modelViewMatrix, 0, sTemp, 0);
    } 
    
    public void updateMVPMatrix(Camera camera) {
        cameraPosition.set(camera.getEYE());
        
        multiplyMM(
            modelViewProjectionMatrix, 0,
            camera.VIEW_PROJECTION_MATRIX, 0,
            modelMatrix, 0);
    }
    
    public void updateMVPtoFakeBillboard(Camera camera){
        updateMVMatrix(camera);
        modelViewMatrix[0] = 1.0f;
        modelViewMatrix[4] = 0.0f;
        modelViewMatrix[8] = 0.0f;
        
        modelViewMatrix[1] = 0.0f;
        modelViewMatrix[5] = 1.0f;
        modelViewMatrix[9] = 0.0f;
        
        modelViewMatrix[2] = 0.0f;
        modelViewMatrix[6] = 0.0f;
        modelViewMatrix[10] = 1.0f;
        
        Matrix.multiplyMM(
            modelViewProjectionMatrix, 0,
            camera.PROJECTION_MATRIX, 0,
            modelViewMatrix, 0);        
    }    
     
    public void updateMVPtoBillboard(Camera camera){
        Vector3D pos = new Vector3D(getPosition());
        Vector3D toCamVector = new Vector3D(pos, camera.getEYE());
        toCamVector.normalize();
        MatrixCreator.createModelMatrix(modelMatrix, pos, toCamVector);
        multiplyMM(modelViewProjectionMatrix, 0, camera.VIEW_PROJECTION_MATRIX, 0, modelMatrix, 0);        
        
    }    
       
    
    public final void setModelMatrix(float[] m){
        System.arraycopy(m, 0, modelMatrix, 0, 16);
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public float[] getModelViewMatrix() {
        return modelViewMatrix;
    }
    
    public float[] getModelViewProjectionMatrix() {
        return modelViewProjectionMatrix;
    }

    public Vector3D getCameraPosition() {
        return cameraPosition;
    }
    
    public final void resetModelMatrix(){
        Matrix.setIdentityM(modelMatrix, 0);
    }
    
    public final void setPosition(float x, float y, float z) {
        resetModelMatrix();
        translate(x,y,z);
    }
    
    public final void setPosition(Vector3D ujPos) {
        setPosition(ujPos.getX(), ujPos.getY(), ujPos.getZ());
    }
    
    public final void moveToOrigLocation() {
        setPosition(origLocation);
    }    
    
    public void translate(Vector3D translateVector) {
        translate(translateVector.getX(), 
                translateVector.getY(), 
                translateVector.getZ());        
    }      
    
    public void translate(float dx, float dy, float dz){
        translateM(modelMatrix, 0, dx, dy, dz);
    }
    
    public void translateInAbsoluteNormalisedDirection(Vector3D normalisedVector, float distance){
        translateInAbsoluteDirection(
                normalisedVector.getX()*distance,
                normalisedVector.getY()*distance,
                normalisedVector.getZ()*distance);
    }    

    public void translateInAbsoluteDirection(Vector3D directionVector){
        translateInAbsoluteDirection(directionVector.getX(), directionVector.getY(), directionVector.getZ());
    }    
    
    public void translateInAbsoluteDirection(float x, float y, float z){
        modelMatrix[12] += x;
        modelMatrix[13] += y;
        modelMatrix[14] += z;
    }    
    
    public void translateTo(Vector3D positionVector){
        translateTo(positionVector.getX(), positionVector.getY(), positionVector.getZ());
    }        
    
    public void translateTo(float x, float y, float z){
        modelMatrix[12] = x;
        modelMatrix[13] = y;
        modelMatrix[14] = z;
    }       
    
//    public void translateInAbsoluteDirection(float x, float y, float z){
//        Matrix.setIdentityM(translateAbsoluteTemp, 0);
//        Matrix.translateM(translateAbsoluteTemp, 0, x, y, z);
//        Matrix.multiplyMM(sTemp, 0, translateAbsoluteTemp, 0, modelMatrix, 0);    
//        setModelMatrix(sTemp);
//    }  
    
    public final void rotate_around_X(float deg) {
        Matrix.rotateM(modelMatrix, 0, deg, 1.0f, 0, 0);
    }

    public final void rotate_around_Y(float deg) {
        Matrix.rotateM(modelMatrix, 0, deg, 0, 1.0f, 0);
    }    

    public final void rotate_around_Z(float deg) {
        Matrix.rotateM(modelMatrix, 0, deg, 0, 0, 1.0f);
    }
    
    public final void rotate_around_Axis(float deg, Vector3D axis) {
        rotate_around_Axis(deg, axis.getX(),axis.getY(), axis.getZ());
    }     
    
    public final void rotate_around_Axis(float deg, float x, float y, float z) {
        Matrix.rotateM(modelMatrix, 0, deg, x, y, z);
    }    
    
    public final void scale(float scaleValue) {
        scale(scaleValue, scaleValue, scaleValue);
    } 
    
    public final void scale(float x, float y, float z) {
        Matrix.scaleM(modelMatrix, 0, x, y, z);
    }     
    
    public Vector3D getPosition() {
        positionCurrent.set(modelMatrix[12], modelMatrix[13], modelMatrix[14]);
        return positionCurrent;
    }     
    
    public float getDistanceFrom(MeshBase mesh) {
        return getPosition().distanceFromVector(mesh.getPosition());
    }    
    
    public final void setParent(OpenGlObject parent){
        this.parent = parent;
        relPosToParent.set(parent.getOrigLocation(), getOrigLocation());
    }
    
    public final void alignToParent(){
        setModelMatrix(parent.getModelMatrix());
        translate(relPosToParent.getX(), relPosToParent.getY(), relPosToParent.getZ()); 
    }  

    public final float getRelPosToParentX() {
        return relPosToParent.getX();
    }
    
    public final float getRelPosToParentY() {
        return relPosToParent.getY();
    }
    
    public final float getRelPosToParentZ() {
        return relPosToParent.getZ();
    }     
    
}
