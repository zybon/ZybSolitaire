package zybandroid.opengl.blenderdatas;

import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2019.03.03. 14:52:20
 */
public class InstanceData {
    
    final Vector3D position = new Vector3D();
    float rotationAngle;
    final Vector3D rotationAxis = new Vector3D(0, 1, 0);
    float scale = 1.0f;
    float[] modelMatrix;
    float rndValue = 0.0f;
    float rndValue2 = 0.0f;
    float windDelay = 0.0f;
    
    public void setPosition(Vector3D position){
        this.position.set(position);
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }
    
    public void setRotationAxis(Vector3D rotationAxis){
        this.rotationAxis.set(rotationAxis);
    }

    public Vector3D getRotationAxis() {
        return rotationAxis;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    
    public float getScale() {
        return scale;
    }
    
    public void setModelMatrix(float[] modelMatrix){
        this.modelMatrix = modelMatrix;
    }

    public void setRNDValue(float rndValue) {
        this.rndValue = rndValue;
    }

    public float getRNDValue() {
        return rndValue;
    }

    public float getRndValue2() {
        return rndValue2;
    }

    public void setRndValue2(float rndValue2) {
        this.rndValue2 = rndValue2;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }
    
    
    
        

}
