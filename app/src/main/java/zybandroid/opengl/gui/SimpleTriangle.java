package zybandroid.opengl.gui;

import android.opengl.GLES20;
import zybandroid.opengl.util.VertexArray;

/**
 *
 * @author zybon
 * Created 2020.06.29. 16:45:40
 */
public class SimpleTriangle {
    
    private static final String TAG = "SimpleTriangle";
    
    private final VertexArray vertexArray;
    
    private final int vertexNumber = 3;
    
    private int glTextureId;
    private boolean visible = true;
    
    public SimpleTriangle() {
        this.vertexArray =  new VertexArray(new float[]{
            1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f
            
        });
    }
    
    public void setGlTextureId(int glTextureId){
        this.glTextureId = glTextureId;
    }
    
    public int getGlTextureId() {
        return glTextureId;
    }
    
    public void show(){
        this.visible = true;
    }
    
    public void hide(){
        this.visible = false;
    }    
    
    public final boolean isVisible(){
        return visible;
    }    
    
    public void setAttibutePositionData(int attributeLocation) {
        vertexArray.setVertexAttribPointer(0, attributeLocation, 3, 0);
    }     
    
    public void drawWithTriangles(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexNumber);
    }      
    
    public void drawWithLines(){
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexNumber);
    }      
    

}
