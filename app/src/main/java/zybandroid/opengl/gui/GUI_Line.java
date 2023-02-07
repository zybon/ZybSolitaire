package zybandroid.opengl.gui;

import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2020.09.09. 10:05:55
 */
public class GUI_Line {
    
    private final Vector3D startPoint = new Vector3D();
    private final Vector3D endPoint = new Vector3D();

    public void setStartPoint(Vector3D point){
        startPoint.set(point);
    }
    
    public void setStartPoint(float x, float y){
        startPoint.set(x, y, 0);
    }    
    
    public void setEndPoint(Vector3D point){
        endPoint.set(point);
    }
    
    public void setEndPoint(float x, float y){
        endPoint.set(x, y, 0);
    }        
    
    public Vector3D getEndPoint() {
        return endPoint;
    }

    public Vector3D getStartPoint() {
        return startPoint;
    }
    
    

}
