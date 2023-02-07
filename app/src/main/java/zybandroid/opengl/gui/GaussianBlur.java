package zybandroid.opengl.gui;

import android.content.Context;
import android.opengl.GLES20;
import static android.opengl.GLES20.GL_TEXTURE0;
import hu.zybon.zybsolitaire.names.ShadersNames;
import zybandroid.opengl.programs.ShaderProgram;
import zybandroid.opengl.util.FrameBufferObject;
import zybandroid.opengl.util.TextFileReader;

/**
 *
 * @author zybon
 * Created 2018.10.03. 15:40:38
 */
public class GaussianBlur{
    
    private final SimpleQuad simpleQuad = SimpleQuad.createFullScreenQuad();
    private final FrameBufferObject horizontalBlurFBO;
    private final FrameBufferObject verticalBlurFBO;    

    private final GaussianBlurShader shaderProgram;
    private final float targetWidth;
    private final float targetHeight;
    
    public GaussianBlur(Context context, float targetWidth, float targetHeight) {
        shaderProgram = new GaussianBlurShader(context);
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        horizontalBlurFBO = new FrameBufferObject(targetWidth, targetHeight, FrameBufferObject.DEPTH_TYPE.BUFFER);
        verticalBlurFBO = new FrameBufferObject(targetWidth, targetHeight, FrameBufferObject.DEPTH_TYPE.BUFFER);        
    }

    public void blurTexture(int glTextureId){
        shaderProgram.useProgram();
        simpleQuad.setAttibutePositionData(shaderProgram.getaPositionLocation());
        
        horizontalBlurFBO.bindBuffer();
        shaderProgram.loadTargetSizeXY(targetWidth, -1);
        shaderProgram.loadColorTextureUniform(glTextureId);
        simpleQuad.drawWithTriangles();
        horizontalBlurFBO.unBindBuffer();  

        verticalBlurFBO.bindBuffer();
        shaderProgram.loadTargetSizeXY(-1, targetHeight);
        shaderProgram.loadColorTextureUniform(horizontalBlurFBO.getColorTextureId());
        simpleQuad.drawWithTriangles();
        verticalBlurFBO.unBindBuffer();    
        
//        horizontalBlurFBO.bindBuffer();
//        shaderProgram.loadTargetSizeXY(targetWidth, -1);
//        shaderProgram.loadColorTextureUniform(verticalBlurFBO.getColorTextureId());
//        simpleQuad.drawWithTriangles();
//        horizontalBlurFBO.unBindBuffer();
//
//        verticalBlurFBO.bindBuffer();
//        shaderProgram.loadTargetSizeXY(-1, targetHeight);
//        shaderProgram.loadColorTextureUniform(horizontalBlurFBO.getColorTextureId());
//        simpleQuad.drawWithTriangles();
//        verticalBlurFBO.unBindBuffer();

        
    }
    
    public int getBluredTextureId(){
        return verticalBlurFBO.getColorTextureId();
    }

    private class GaussianBlurShader extends ShaderProgram{
        
        private final int uColorTextureUnitLocation;
        private final int u_targetSizeXYLocation;
        private int u_sample;
        
        private final int aPositionLocation;

        public GaussianBlurShader(Context context) {
         super(
             TextFileReader.readTextFromAssets(context,
                         ShadersNames.VertexShaders.gaussianblur), 
             TextFileReader.readTextFromAssets(context,
                     ShadersNames.FragmentShaders.gaussianblur),
                 "gaussianblur");
            uColorTextureUnitLocation = getUniformLocation("u_ColorTextureUnit");
            u_targetSizeXYLocation = getUniformLocation("u_targetSizeXY");
            aPositionLocation = getAttribLocation("a_Position");
        }

        public void loadTargetSizeXY(float targetWidth, float targetHeight) {
            GLES20.glUniform2f(u_targetSizeXYLocation, targetWidth, targetHeight);
        }
        
        public void loadColorTextureUniform(int textureId){
            loadTextureUniform(GL_TEXTURE0, textureId, uColorTextureUnitLocation, 0);
        }            
        
        public int getaPositionLocation() {
            return aPositionLocation;
        }

        
    }
    
}
      
