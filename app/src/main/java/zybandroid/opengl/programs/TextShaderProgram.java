package zybandroid.opengl.programs;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4fv;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.10.18. 16:58:47
 */
public class TextShaderProgram extends ShaderProgram{
    private static TextShaderProgram textShaderProgram;
    
    public static void setTextShaderProgram(TextShaderProgram textShaderProgram) {
        TextShaderProgram.textShaderProgram = textShaderProgram;
    }    

    public static TextShaderProgram getTextShaderProgram() {
        return textShaderProgram;
    }     
    
    private final int uMMatrixLocation; 
    private final int uTextureUnitLocation;
    
    private final int uAlphaLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;         
    private final int uColorLocation;

    public TextShaderProgram(String vertex_shader, String fragment_shader, String programName) {
        super(vertex_shader, fragment_shader, programName);
        uMMatrixLocation = getUniformLocation("u_MMatrix");
        uColorLocation = getUniformLocation("u_Color");
        
        uTextureUnitLocation = getUniformLocation("u_TextureUnit");
        uAlphaLocation = getUniformLocation("u_Alpha");
        
        // Retrieve attribute locations for the shader program.
        aPositionLocation = getAttribLocation("a_Position");
        aTextureCoordinatesLocation = getAttribLocation("a_TextureCoordinates");            
    }

    public void activateTexture(int textureId){
        loadTextureUniform(GL_TEXTURE0, textureId, uTextureUnitLocation, 0);
    }  
    
    public void loadAlphaUniform(float alpha){
        glUniform1f(uAlphaLocation, alpha);
    }      

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

    public int getuMMatrixLocation() {
        return uMMatrixLocation;
    }

    public void loadColorUniform(ZybColor color) {
        glUniform4fv(uColorLocation, 1,  color.rgba, 0);
    }
    
    


}
    
