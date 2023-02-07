package zybandroid.opengl.util;

import android.opengl.GLES20;
import android.opengl.GLES30;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class UniformBuffer {
    
    private final int bufferId;
    
    private final FloatBuffer floatBuffer;

    public UniformBuffer(int capacity) {
        // Allocate a buffer.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0) {
            throw new RuntimeException("Could not create a new UniformBuffer object.");
        }
        bufferId =  buffers[0];

        // Bind to the buffer. 
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);

        floatBuffer = ByteBuffer
            .allocateDirect(capacity * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
//            .put(bufferData);  
        floatBuffer.position(0);
        
        // Transfer data from native memory to the GPU buffer.              
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer.capacity() * BYTES_PER_FLOAT,
            floatBuffer, GLES20.GL_STREAM_DRAW);                      
         
        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        // We let floatBuffer go out of scope, but it won't be released
        // until the next time the garbage collector is run.
    }
    
    public void loadDataToFloatBuffer(float[] bufferData, int start, int count){
        floatBuffer.position(start);
        floatBuffer.put(bufferData, start, count);
        floatBuffer.position(0);      
    }
    
    public void updateBuffer(int start, int count){
      
        bindBuffer();
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, start* BYTES_PER_FLOAT, count* BYTES_PER_FLOAT, floatBuffer);
        unBindBuffer();
    }
    
    public void bindBuffer(){
        GLES20.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferId);
    }
        
    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
        int componentCount, int stride) {
//        bindBuffer();
        // This call is slightly different than the glVertexAttribPointer we've
        // used in the past: the last parameter is set to dataOffset, to tell OpenGL
        // to begin reading data at this position of the currently bound buffer.
        GLES20.glEnableVertexAttribArray(attributeLocation);
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, 
            false, stride * BYTES_PER_FLOAT, dataOffset * BYTES_PER_FLOAT);
        GLES30.glVertexAttribDivisor(attributeLocation, 1);
//        unBindBuffer();
        
    }     
    
    public void unBindBuffer(){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);        
    }
}
