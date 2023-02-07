package zybandroid.opengl.programs;

import android.opengl.GLES20;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glUniform1f;
import static zybandroid.opengl.programs.ShaderProgram.loadTextureUniform;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2019.03.27. 15:04:19
 */
public class GUIShaderProgram extends ShaderProgram{
    
    private static GUIShaderProgram guiShaderProgram;
    
    public static void setGUIShaderProgram(GUIShaderProgram guiShaderProgram) {
        GUIShaderProgram.guiShaderProgram = guiShaderProgram;
    }    

    public static GUIShaderProgram getGUIShaderProgram() {
        return guiShaderProgram;
    }     
    
    
    private final int uMMatrixLocation; 
    private final int uTextureUnitLocation;
    private final int uAlphaLocation;
    private final int uColorLocation;
    private final int uPercentLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;         

    public GUIShaderProgram(String vertexShader, String fragmentShader, String name) {
        super(vertexShader, fragmentShader, name);
        uMMatrixLocation = getUniformLocation("u_MMatrix");
        uTextureUnitLocation = getUniformLocation("u_TextureUnit");
        uAlphaLocation = getUniformLocation("u_alpha");
        uColorLocation = getUniformLocation("u_Color");
        uPercentLocation = getUniformLocation("u_Percent");
        
        aPositionLocation = getAttribLocation("a_Position");
        aTextureCoordinatesLocation = getAttribLocation("a_TextureCoordinates");            
    }
    
    public void loadTextureUniform(int textureId){
        loadTextureUniform(GL_TEXTURE0, textureId, uTextureUnitLocation, 0);
    }   

    public void loadAlphaUniform(float alpha){
        glUniform1f(uAlphaLocation, alpha);
    }  
    
    public void loadColorUniform(ZybColor color){
        GLES20.glUniform4fv(uColorLocation, 1, color.rgba, 0);
    }

    public void loadPercentUniform(float percent){
        glUniform1f(uPercentLocation, percent);
    }

    public int getuMMatrixLocation() {
        return uMMatrixLocation;
    }
    
    public int getaPositionLocation() {
        return aPositionLocation;
    }

    public int getaTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
    
}

