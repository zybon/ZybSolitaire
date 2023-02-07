package zybandroid.opengl.programs;

import android.opengl.GLES20;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;

/**
 *
 * @author Zybon
 * Created time: 2017.02.12 22:18:48
 */
public class ShaderProgram{
    // Shader program
    private final int programId;
    private final String programName;
    
    public ShaderProgram(String vertex_shader, String fragment_shader, String programName){
        this.programName = programName;
        programId = ShaderHelper.buildProgram(vertex_shader, fragment_shader, programName);
    }
    
    public String getName() {
        return programName;
    }
    
    public final int getProgramId() {
        return programId;
    }
    
    public final void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(programId);
    }  
    
    public final void stopProgram() {
        glUseProgram(0);
    }     

    public final int getUniformLocation(String name){
        return glGetUniformLocation(programId, name);
    }
    
    public final int getAttribLocation(String name){
        return glGetAttribLocation(programId, name);
    }    
    
    public static final void loadTextureUniform(int gltexture, int textureId, int uniformLocation, int activateInt){
        glActiveTexture(gltexture);
        // Bind the texture to this unit.
        glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit "activateInt".
        glUniform1i(uniformLocation, activateInt);    
    }    
    
    public static final void loadCubeTextureUniform(int gltexture, int textureId, int uniformLocation, int activateInt){
        glActiveTexture(gltexture);
        // Bind the texture to this unit.
        glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit "activateInt".
        glUniform1i(uniformLocation, activateInt);    
    }      
    


}
