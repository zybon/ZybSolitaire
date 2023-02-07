package zybandroid.opengl.camera;

import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.objects.OpenGlObject;

/**
 *
 * @author zybon
 * @created 2020.06.16. 11:35
 */
public interface CameraTarget {
    
    public Vector3D getCameraEyePosition();
    public Vector3D getCameraTargetPosition();
//    public OpenGlObject getCameraTargetObject();
//    public float getDirectionModifier();
//    public float getCurrentSpeedPerMaxSpeed();
    public float getAngleAroundY();
//    public boolean isInTheAir();
//    public Vector3D getDirectionVector();    
    
}
