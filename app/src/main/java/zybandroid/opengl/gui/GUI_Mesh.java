package zybandroid.opengl.gui;

import zybandroid.opengl.objects.*;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4fv;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.programs.ShaderProgram;
import zybandroid.opengl.util.VertexArray;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author Zybon
 * Created time: 2017.02.19 19:04:27
 */
public abstract class GUI_Mesh extends MeshBase {
    
    private static final String TAG = "GUI_Mesh";
    
    private final VertexArray vertexArray;
    
    private final int vertexNumber;
    private final int stride;
    
    private int glTextureId;
    
    private final Vector3D textureScale;
    private final ZybColor diffuseColor;
    
    private boolean visible = true;
    
    public GUI_Mesh(MeshData meshData) { 
        super(meshData);
        this.vertexArray = new VertexArray(meshData.getVertexData());
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
        setAttributeDatas();
    }
    
    
    protected abstract void setUniforms();
    
    protected abstract void setAttributeDatas();
    
    protected abstract void onDraw();
    
    
    public final void drawWithPoints(){
        glDrawArrays(GL_POINTS, 0, vertexNumber);
    }  
    
    public final void drawWithLines(){
        glDrawArrays(GL_LINES, 0, vertexNumber);
    }  
    
    public void setAttributePositionData(int aPositionLocation) {
        setVertexAttribPointer(0, aPositionLocation, 3, stride);
    }
    
//    public void setAttibuteNormalData(int aPositionLocation) {
//        setVertexAttribPointer(3, aPositionLocation, 3, stride);
//    }    

    public void setAttributeTextureCoordData(int aTextureCoordinatesLocation) {
        setVertexAttribPointer(6, aTextureCoordinatesLocation, 2, stride);
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
