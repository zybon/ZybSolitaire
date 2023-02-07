//package zybandroid.opengl.objects;
//
//import android.opengl.GLES20;
//import static android.opengl.GLES20.GL_TEXTURE0;
//import static android.opengl.GLES20.GL_TRIANGLES;
//import static android.opengl.GLES20.glUniform1f;
//import static android.opengl.GLES20.glUniform4fv;
//import zybandroid.opengl.blenderdatas.MeshData;
//import zybandroid.opengl.programs.ShaderProgram;
//import zybandroid.opengl.util.IndexBuffer;
//import zybandroid.opengl.util.VertexBuffer;
//
///**
// *
// * @author Zybon
// * Created time: 2017.02.19 19:04:27
// */
//public abstract class Indexed_VBO_Mesh extends Mesh{
//    
//    private static final String TAG = "Indexed_VBO_Mesh";
//    
//    private final VertexBuffer vertexBuffer;
//    private final IndexBuffer indexBuffer;
//    
//    public Indexed_VBO_Mesh(MeshData meshData) { 
//        super(meshData);
//        this.vertexBuffer =  new VertexBuffer(meshData.getVertexData());
//        this.indexBuffer = new IndexBuffer(meshData.getTriangles());
//    }       
//    
//    public void setAttibutePositionData(int attributeLocation) {
//        setVertexAttribPointer(0, attributeLocation, 3);
//    }    
//    
//    public void setAttibuteNormalData(int attributeLocation) {
//        setVertexAttribPointer(3, attributeLocation, 3);
//    }      
//    
//    public void setAttibuteTextureCoordData(int attributeLocation) {
//        setVertexAttribPointer(6, attributeLocation, 2);
//    }   
//    
//    public int setVertexAttribPointer(int dataOffset,
//            int attributeLocation,
//            int componentCount) {
//        vertexBuffer.setVertexAttribPointer(
//                dataOffset, 
//                attributeLocation, 
//                componentCount, 
//                getStride());
//        return dataOffset+componentCount;
//    }       
//    
//    public void drawWithTriangles(){
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
//        GLES20.glDrawElements(
//                GL_TRIANGLES, 
//                getVertexNumber(), 
//                GLES20.GL_UNSIGNED_INT, 
//                0
//        );
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
//
//    }  
//    
//    
//
//}
