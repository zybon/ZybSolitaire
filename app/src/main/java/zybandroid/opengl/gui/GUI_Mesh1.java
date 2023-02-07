package zybandroid.opengl.gui;

import zybandroid.opengl.objects.*;
import android.opengl.GLES20;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4fv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.programs.ShaderProgram;
import zybandroid.opengl.util.IndexBuffer;
import zybandroid.opengl.util.VertexArray;
import zybandroid.opengl.util.VertexBuffer;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author Zybon
 * Created time: 2017.02.19 19:04:27
 */
public abstract class GUI_Mesh1 extends MeshBase {
    
    private static final String TAG = "GUI_Mesh";
    
    private final Structure structure;
    
    private final int vertexNumber;
    private final int stride;
    
    private int glTextureId;
    
    private final boolean indexed;
    private final Vector3D textureScale;
    private final ZybColor diffuseColor;
    
    private boolean visible = true;
    
    public GUI_Mesh1(MeshData meshData) { 
        super(meshData);
        this.indexed = meshData.isIndexed();
        if (indexed) {
            this.structure = new IndexedBufferStructure(meshData.getVertexData(), meshData.getTriangles());
        }
        else {
            if (meshData.getName().contains("terrain")) {
                this.structure = new VertexBufferStructure(meshData.getVertexData());
            }
            else {
                this.structure = new VertexArrayStructure(meshData.getVertexData());
            }
        }
        this.stride = meshData.getStride()*BYTES_PER_FLOAT;
        this.vertexNumber = meshData.getVertexNumber();  
        this.glTextureId = meshData.getTextureGlId();
        this.diffuseColor = meshData.getDiffuseColor();
        this.textureScale = meshData.getTextureScale();
    }       
    
    public void setGlTextureId(int glTextureId){
        this.glTextureId = glTextureId;
    }
    
    public int getGlTextureId() {
        return glTextureId;
    }

    public Vector3D getTextureScale() {
        return textureScale;
    }

    public ZybColor getDiffuseColor() {
        return diffuseColor;
    }
    
    public void show(){
        this.visible = true;
    }
    
    public void hide(){
        this.visible = false;
    }
    
    public void loadUniformTextureScale(int uTextureScaleLocation){
        glUniform1f(uTextureScaleLocation, textureScale.getX());
    }     
    
    public void loadUniformGlTextureId(int uTextureLocation) {
        ShaderProgram.loadTextureUniform(GL_TEXTURE0, glTextureId, uTextureLocation, 0);
    }    
    
    public void loadDiffuseColor(int uDiffuseColorLocation){
        glUniform4fv(uDiffuseColorLocation, 1,  diffuseColor.rgba, 0);
    }        

    public void setAttibutePositionData(int attributeLocation) {
        structure.setVertexAttribPointer(0, attributeLocation, 3, stride);
    }    
    
    public void setAttibuteNormalData(int attributeLocation) {
        structure.setVertexAttribPointer(3, attributeLocation, 3, stride);
    }      
    
    public void setAttibuteTextureCoordData(int attributeLocation) {
        structure.setVertexAttribPointer(6, attributeLocation, 2, stride);
    }   
    
   
    
    public int setAttibuteData(int dataOffset, int attributeLocation, int componentCount) {
        structure.setVertexAttribPointer(dataOffset, attributeLocation, componentCount, stride);
        return dataOffset+componentCount;
    }
    
    public void draw() {
        if (isVisible()) {
            doBeforeDraw();
            onDraw();
        }
    }  

    public final boolean isVisible(){
        return visible;
    }
    
    protected void doBeforeDraw(){
        setUniforms();
        setAttibuteDatas();        
    }
    
    
    protected abstract void setUniforms();
    
    protected abstract void setAttibuteDatas();
    
    protected abstract void onDraw();
    
    
    public final void drawWithPoints(){
        glDrawArrays(GL_POINTS, 0, vertexNumber);
    }  
    
    public final void drawWithLines(){
        glDrawArrays(GL_LINES, 0, vertexNumber);
    }      
    
    public final void drawWithTriangles(){
        structure.drawWithTriangles();
    }
    
    private class VertexArrayStructure implements Structure{
        private final VertexArray vertexArray;

        public VertexArrayStructure(float[] vertexData) {
            this.vertexArray =  new VertexArray(vertexData);
        }
        
        public int setVertexAttribPointer(int dataOffset,
                int attributeLocation,
                int componentCount, 
                int stride) {
            vertexArray.setVertexAttribPointer(dataOffset, attributeLocation, componentCount, stride);
            return dataOffset+componentCount;
        }   
        
        public void drawWithTriangles(){
            glDrawArrays(GL_TRIANGLES, 0, vertexNumber);
        }        
        
    }
    
    private class VertexBufferStructure implements Structure{
        
        private final VertexBuffer vertexBuffer;

        private VertexBufferStructure(float[] vertexData) {
            this.vertexBuffer =  new VertexBuffer(vertexData);
        }
        
        public int setVertexAttribPointer(int dataOffset,
                int attributeLocation,
                int componentCount, 
                int stride) {
            vertexBuffer.setVertexAttribPointer(
                    dataOffset, 
                    attributeLocation, 
                    componentCount, 
                    stride);
            return dataOffset+componentCount;
        }   
        
        public void drawWithTriangles(){
            glDrawArrays(GL_TRIANGLES, 0, vertexNumber);
        }        
        
    }    
        
    
    private class IndexedStructure implements Structure{
        
        private final VertexArray vertexArray;
        private final IntBuffer indexArray;

        private IndexedStructure(float[] vertexData, int[] triangles) {
            this.vertexArray =  new VertexArray(vertexData);
//            int[] indices = new int[triangles.length];
//            for (int i = 0; i < triangles.length; i++) {
//                indices[i] = triangles[i];
//            }
            this.indexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(triangles);
            this.indexArray.position(0);
        }
        
        public int setVertexAttribPointer(int dataOffset,
                int attributeLocation,
                int componentCount, 
                int stride) {
            vertexArray.setVertexAttribPointer(dataOffset, attributeLocation, componentCount, stride);
            return dataOffset+componentCount;
        }   
        
        public void drawWithTriangles(){
            GLES20.glDrawElements(
                    GL_TRIANGLES, 
                    vertexNumber, 
                    GLES20.GL_UNSIGNED_INT, 
                    indexArray
            );
        }        
        
    }    
        
    
    private class IndexedBufferStructure implements Structure{
        
        private final VertexBuffer vertexBuffer;
        private final IndexBuffer indexBuffer;

        private IndexedBufferStructure(float[] vertexData, int[] triangles) {
            this.vertexBuffer =  new VertexBuffer(vertexData);
            this.indexBuffer = new IndexBuffer(triangles);
        }
        
        public int setVertexAttribPointer(int dataOffset,
                int attributeLocation,
                int componentCount, 
                int stride) {
            vertexBuffer.setVertexAttribPointer(
                    dataOffset, 
                    attributeLocation, 
                    componentCount, 
                    stride);
            return dataOffset+componentCount;
        }   
        
        public void drawWithTriangles(){
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
            GLES20.glDrawElements(
                    GL_TRIANGLES, 
                    vertexNumber, 
                    GLES20.GL_UNSIGNED_INT, 
                    0
            );
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            
        }        
        
    }    
    

    private static interface Structure{
        int setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride);
        void drawWithTriangles();
    }
    
}
