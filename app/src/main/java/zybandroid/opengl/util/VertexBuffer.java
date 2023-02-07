package zybandroid.opengl.util;

import android.opengl.GLES20;
import android.opengl.GLES30;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexBuffer {
    
    private final int bufferId;

    public VertexBuffer(float[] vertexData) {
        // Allocate a buffer.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0) {
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        bufferId =  buffers[0];

        // Bind to the buffer. 
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);

        FloatBuffer vertexArray = ByteBuffer
            .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData);  
        vertexArray.position(0);
        
        // Transfer data from native memory to the GPU buffer.              
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_FLOAT,
            vertexArray, GLES20.GL_STATIC_DRAW);                      
         
        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        // We let vertexArray go out of scope, but it won't be released
        // until the next time the garbage collector is run.
    }
    
    public void bindBuffer(){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);
    }
        
    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
        int componentCount, int stride) {
        bindBuffer();
        // This call is slightly different than the glVertexAttribPointer we've
        // used in the past: the last parameter is set to dataOffset, to tell OpenGL
        // to begin reading data at this position of the currently bound buffer.
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, 
            false, stride, dataOffset * BYTES_PER_FLOAT);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        GLES30.glVertexAttribDivisor(attributeLocation, 0);
        unBindBuffer();
        
    }     
    
    public void unBindBuffer(){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);        
    }
}
