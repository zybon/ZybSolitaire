package zybandroid.opengl.util;

import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2019.03.06. 10:36:01
 */
public class MatrixCreator {
    
    public static final Vector3D WORLD_FORWARD = new Vector3D(0f, 0.0f, -1.0f);
    public static final Vector3D WORLD_RIGHT = new Vector3D(1.0f, 0.0f, 0f);
    public static final Vector3D WORLD_UP = new Vector3D(0f, 1.0f, 0f);
    
    private static final Vector3D directionVector = new Vector3D(0,0,-1);
    
    private static final Vector3D upVector = new Vector3D();
    private static final Vector3D sideVector = new Vector3D();    
    private static final Vector3D basePointPosition = new Vector3D();
    
    
//    private static final float[] orientationMatrix = new float[16];  
    
//    private static final float[] translateHelperMatrix = new float[16];
    
    public static void createModelMatrix(float[] resultMatrix, Vector3D position, Vector3D normalisedDirection){
        basePointPosition.set(position);
        directionVector.set(normalisedDirection);
        calcBaseVectors();
        updateBasePointMatrix(resultMatrix);     
//        translateInAbsoluteDirection(resultMatrix);         
    }
    
    private static void calcBaseVectors(){
        sideVector.setCrossProductBetween(directionVector, WORLD_UP);
        sideVector.normalize();

        upVector.setCrossProductBetween(sideVector, directionVector); 
        upVector.normalize();
    }    
    
    public static void createModelMatrix(float[] resultMatrix, Vector3D position, Vector3D normalisedDirection, Vector3D normalisedUp){
        basePointPosition.set(position);
        directionVector.set(normalisedDirection);
        calcBaseVectors(normalisedUp);
        updateBasePointMatrix(resultMatrix);     
//        translateInAbsoluteDirection(resultMatrix);         
    }    
    
    private static void calcBaseVectors(Vector3D normalisedUpVector){
        sideVector.setCrossProductBetween(directionVector, normalisedUpVector);
        sideVector.normalize();

        upVector.setCrossProductBetween(sideVector, directionVector); 
        upVector.normalize();
    }    
    
   


    
    private static void updateBasePointMatrix(float[] resultMatrix){
        resultMatrix[0] = sideVector.getX();
        resultMatrix[4] = upVector.getX();
        resultMatrix[8] = -directionVector.getX();
        resultMatrix[12] = basePointPosition.getX();

        resultMatrix[1] = sideVector.getY();
        resultMatrix[5] = upVector.getY();
        resultMatrix[9] = -directionVector.getY();
        resultMatrix[13] = basePointPosition.getY();

        resultMatrix[2] = sideVector.getZ();
        resultMatrix[6] = upVector.getZ();
        resultMatrix[10] = -directionVector.getZ();
        resultMatrix[14] = basePointPosition.getZ();

        resultMatrix[3] = 0.0f;
        resultMatrix[7] = 0.0f;
        resultMatrix[11] = 0.0f;
        resultMatrix[15] = 1.0f;     

 
    }      

//    private static void updateBasePointMatrix(){
//        orientationMatrix[0] = sideVector.getX();
//        orientationMatrix[4] = upVector.getX();
//        orientationMatrix[8] = -directionVector.getX();
//        orientationMatrix[12] = 0.0f;
//
//        orientationMatrix[1] = sideVector.getY();
//        orientationMatrix[5] = upVector.getY();
//        orientationMatrix[9] = -directionVector.getY();
//        orientationMatrix[13] = 0.0f;
//
//        orientationMatrix[2] = sideVector.getZ();
//        orientationMatrix[6] = upVector.getZ();
//        orientationMatrix[10] = -directionVector.getZ();
//        orientationMatrix[14] = 0.0f;
//
//        orientationMatrix[3] = 0.0f;
//        orientationMatrix[7] = 0.0f;
//        orientationMatrix[11] = 0.0f;
//        orientationMatrix[15] = 1.0f;     
//
// 
//    }  
//
//    private static void translateInAbsoluteDirection(float[] resultMtx){
//        Matrix.setIdentityM(translateHelperMatrix, 0);
//        Matrix.translateM(translateHelperMatrix, 0, 
//                basePointPosition.getX(), 
//                basePointPosition.getY(),
//                basePointPosition.getZ());
//        Matrix.multiplyMM(resultMtx, 0, translateHelperMatrix, 0, orientationMatrix, 0);        
//    }          
//        
        
    
}
