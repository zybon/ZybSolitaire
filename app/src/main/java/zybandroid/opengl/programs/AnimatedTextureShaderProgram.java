package zybandroid.opengl.programs;

import static android.opengl.GLES20.glUniform3fv;

/**
 *
 * @author zybon
 * Created 2018.07.30. 23:00:21
 */
public class AnimatedTextureShaderProgram extends TextureShaderProgram{
    
    private final int uTranslateVectorsLocation;

    private final int aVertexIndexLocation; 
    
    public AnimatedTextureShaderProgram(String vertex_shader, String fragment_shader, String programName) {
        super(vertex_shader, fragment_shader, programName);
        
        // Retrieve uniform locations for the shader program.
        uTranslateVectorsLocation = getUniformLocation("u_TranslateVectors"); 
        
        aVertexIndexLocation = getAttribLocation("a_VertexIndex"); 
    }
    
    public void loadTranslateVectors(float[] vectors, int count){
        glUniform3fv(uTranslateVectorsLocation, count,  vectors, 0);
    }

    public int getVertexIndexLocation() {
        return aVertexIndexLocation;
    }

}    

