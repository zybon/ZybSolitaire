package zybandroid.opengl.lamp;

import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2019.03.28. 21:09:49
 */
public class SpotLight {
    
    private final Vector3D position = new Vector3D();
    private final Vector3D direction = new Vector3D();
    private float cutOff;

    public void setPosition(Vector3D position) {
        this.position.set(position);
    }
    
    public Vector3D getPosition() {
        return position;
    }    
    
    public void setDirectionMarkerPosition(Vector3D directionMarkerPosition) {
        this.direction.set(position, directionMarkerPosition);
        this.direction.normalize();
    }    

    public void setDirection(Vector3D direction) {
        this.direction.set(direction);
    }
    
    public Vector3D getDirection() {
        return direction;
    }  
    
    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    public float getCutOff() {
        return cutOff;
    }


    
    

}
