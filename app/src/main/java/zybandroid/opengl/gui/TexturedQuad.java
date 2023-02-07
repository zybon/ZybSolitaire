package zybandroid.opengl.gui;

import android.content.Context;
import android.opengl.GLES20;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import hu.zybon.zybsolitaire.MainRenderer;
import hu.zybon.zybsolitaire.names.ShadersNames;
import zybandroid.opengl.programs.ShaderProgram;
import zybandroid.opengl.util.TextFileReader;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.10.03. 15:40:38
 */
public class TexturedQuad{

    private final SimpleQuad simpleQuad = SimpleQuad.createFullScreenQuad();
    private final TexturedQuadShaderProgram shaderProgram;
    
    private int glTextureId;
    private final ZybColor baseColor = new ZybColor(0);
    private float alpha = 1.0f;
    private float toBnW = 0.0f;

    private float scaleX = 1f;
    private float scaleY = 1f;

    private float offsetX = 0;
    private float offsetY = 0;
    
    public TexturedQuad(Context context) {
        shaderProgram = new TexturedQuadShaderProgram(context);
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public void setBaseColor(ZybColor color){
        this.baseColor.set(color);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setToBnW(boolean toBnW) {
        this.toBnW = toBnW?1.0f:0f;
    }

    public void setGlTextureId(int glTextureId) {
        this.glTextureId = glTextureId;
    }

    public void draw() {
//        clearScreen();
        shaderProgram.useProgram();
        shaderProgram.loadUniforms(offsetX, offsetY, scaleX, scaleY);
        shaderProgram.loadColorTextureUniform(glTextureId);
        shaderProgram.loadColorUniform(baseColor);
        shaderProgram.loadAlphaUniform(alpha);
        shaderProgram.loadToBnWUniform(toBnW);
        simpleQuad.setAttibutePositionData(shaderProgram.getaPositionLocation());
        simpleQuad.drawWithTriangles();
    }   

    
    private void clearScreen(){
        MainRenderer.setViewPortToScreen();
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);    
    }  
    
    public class TexturedQuadShaderProgram extends ShaderProgram{

        // Uniform locations
        private final int uScaleLocation;
        private final int uColorTextureUnitLocation;
        private final int uColorLocation;

        private final int uAlphaLocation;
        private final int uToBnWLocation;

        private final int aPositionLocation;
        private final int aTextureCoordinatesLocation;          

        public TexturedQuadShaderProgram(Context context) {
            super(
                TextFileReader.readTextFromAssets(context,
                            ShadersNames.VertexShaders.textured_quad), 
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.FragmentShaders.textured_quad),
                    "textured_quad");

            uScaleLocation = getUniformLocation("u_Scale");
            uColorTextureUnitLocation = getUniformLocation("u_ColorTextureUnit");
            uColorLocation = getUniformLocation("u_Color");
            uAlphaLocation = getUniformLocation("u_alpha");
            uToBnWLocation = getUniformLocation("u_to_bnw");

            // Retrieve attribute locations for the shader program.
            aPositionLocation = getAttribLocation("a_Position");
            aTextureCoordinatesLocation = getAttribLocation("a_TextureCoordinates");               

        }

        public void loadColorTextureUniform(int textureId){
            loadTextureUniform(GL_TEXTURE0, textureId, uColorTextureUnitLocation, 0);
        }

        public void loadUniforms(float offsetX, float offsetY, float scaleX, float scaleY){
            GLES20.glUniform4f(uScaleLocation, offsetX, offsetY ,scaleX, scaleY);
        }

        public void loadColorUniform(ZybColor color){
            GLES20.glUniform4fv(uColorLocation, 1, color.rgba, 0);
        }

        public void loadAlphaUniform(float alpha){
            GLES20.glUniform1f(uAlphaLocation, alpha);
        }

        public void loadToBnWUniform(float toBnW){
            GLES20.glUniform1f(uToBnWLocation, toBnW);
        }

        public int getaPositionLocation() {
            return aPositionLocation;
        }

        public int getaTextureCoordinatesLocation() {
            return aTextureCoordinatesLocation;
        }        

    }          
    
    
}
      
