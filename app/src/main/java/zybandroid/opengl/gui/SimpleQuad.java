package zybandroid.opengl.gui;

import android.opengl.GLES20;
import static android.opengl.GLES20.GL_TRIANGLES;
import zybandroid.opengl.util.VertexArray;

/**
 *
 * @author zybon
 * Created 2020.06.05. 15:45:40
 */
public class SimpleQuad {
    
    private static final String TAG = "SimpleQuad";
    
    private final VertexArray vertexArray;
    
    private final int vertexNumber = 6;
    
    private int glTextureId;
    private boolean visible = true;
    
//vertex vector list (length: 4)
//	0. (1.0, -1.0, 0.0)
//	1. (-1.0, 1.0, 0.0)
//	2. (-1.0, -1.0, 0.0)
//	3. (1.0, 1.0, 0.0)
//
//normal vector list (length: 1)
//	0. (0.0, 0.0, 1.0)
//
//texture_coord vector list (length: 4)
//	0. (1.0, -1.0)
//	1. (0.0, 0.0)
//	2. (0.0, -1.0)
//	3. (1.0, 0.0)
//
//triangles:  (length: 2)
//	0. triangle [material_index: 0]
//		{0, 0, 0 # 1, 0, 1 # 2, 0, 2}
//	1. triangle [material_index: 0]
//		{0, 0, 0 # 3, 0, 3 # 1, 0, 1}    
    
    public static final float[] fullScreenVertices = new float[]{
            1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f
            
        };
    
    public static SimpleQuad createFullScreenQuad(){
        return new SimpleQuad(fullScreenVertices);
    }
    
    public SimpleQuad(float[] vertexData) {
        this.vertexArray =  new VertexArray(vertexData);
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
        GLES20.glDrawArrays(GL_TRIANGLES, 0, vertexNumber);
    }      
    

}
