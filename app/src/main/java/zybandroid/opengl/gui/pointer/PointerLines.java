package zybandroid.opengl.gui.pointer;

import android.opengl.GLES20;
import java.util.ArrayList;
import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2020.06.29. 16:45:40
 */
public class PointerLines {
    
    private static final String TAG = "PointerLines";
    
    
    private boolean visible = true;
    private final Vector3D startPosition = new Vector3D(0, 0, 0);
    private final Vector3D middlePosition = new Vector3D(-1, -1, 0);
    private final Vector3D endPosition = new Vector3D(1, 0, 0);
    
    private final ArrayList<Vector3D> linesEndpoints = new ArrayList<Vector3D>();
    
//    private final VertexArray vertexArray;
    
    public PointerLines() {
//        this.vertexArray =  new VertexArray(new float[]{
//            1.0f, -1.0f, 0.0f,
//            -1.0f, 1.0f, 0.0f,
//            -1.0f, -1.0f, 0.0f
//            
//        });        
    }
    
    public void setStartPosition(Vector3D startPosition){
        this.startPosition.set(startPosition);
    }
    
    public void setMiddlePosition(Vector3D middlePosition){
        this.middlePosition.set(middlePosition);
    }
    
    public void setEndPosition(Vector3D endPosition){
        this.endPosition.set(endPosition);
    }    
    
//    public void setAttibutePositionData(int attributeLocation) {
//        vertexArray.setVertexAttribPointer(0, attributeLocation, 3, 0);
//    }      
    
    public void show(){
        this.visible = true;
    }
    
    public void hide(){
        this.visible = false;
    }    
    
    public final boolean isVisible(){
        return visible;
    }    

    public Vector3D getStartPosition() {
        return startPosition;
    }

    public Vector3D getMiddlePosition() {
        return middlePosition;
    }

    public Vector3D getEndPosition() {
        return endPosition;
    }
    
    public void drawWithLines(){
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 4);
    } 

}
