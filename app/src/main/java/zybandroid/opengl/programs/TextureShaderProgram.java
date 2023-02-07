package zybandroid.opengl.programs;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

import zybandroid.opengl.camera.Camera;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.07.30. 23:00:21
 */
public class TextureShaderProgram extends ShaderProgram{
    private static TextureShaderProgram textureShaderProgram;
    
    public static void setTextureShaderProgram(TextureShaderProgram textureShaderProgram) {
        TextureShaderProgram.textureShaderProgram = textureShaderProgram;
    }    

    public static TextureShaderProgram getTextureShaderProgram() {
        return textureShaderProgram;
    } 
    
    // Uniform locations
    private final int uMMatrixLocation; 
    private final int uMVPMatrixLocation; 
    private final int uTextureUnitLocation;
    
    private final int uTextureScaleLocation;
    
    private final int uAlphaLocation;
    private final int uDiffuseColorLocation;
    
    private final int uFadeInCameraDistanceLocation;
    private final int uFadeDensityCameraDistanceLocation;
    
    public final int uAmbientColorLocation;
    
    private final int uFogColorLocation;
    private final int uFogMinDistFromCamLocation;
    private final int uFogGradientDepthLocation;

    private final int uCameraPositionLocation;
    private final int uCameraDirectionLocation;
    private final int uSunPositionLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aNormalLocation;
    private final int aTextureCoordinatesLocation;  
    
    
    public TextureShaderProgram(String vertex_shader, String fragment_shader, String programName) {
        super(vertex_shader, fragment_shader, programName);
        // Retrieve uniform locations for the shader program.
        uMMatrixLocation = getUniformLocation("u_MMatrix");
        uMVPMatrixLocation = getUniformLocation("u_MVPMatrix");
        uTextureUnitLocation = getUniformLocation("u_TextureUnit");
        
        uTextureScaleLocation = getUniformLocation("u_textureScale");
        
        uAlphaLocation = getUniformLocation("u_alpha");
        
        uAmbientColorLocation = getUniformLocation("u_AmbientColor");
        
        uDiffuseColorLocation = getUniformLocation("u_diffuseColor");

        uFogColorLocation = getUniformLocation("u_fog_color");
        uFogMinDistFromCamLocation = getUniformLocation("u_fog_min_dist_from_camera");
        uFogGradientDepthLocation = getUniformLocation("u_fog_gradient_depth");

        uSunPositionLocation = getUniformLocation("u_SunPosition");
        uCameraPositionLocation = getUniformLocation("u_CameraPosition");
        uCameraDirectionLocation = getUniformLocation("u_CameraDirection");
        
        uFadeInCameraDistanceLocation = getUniformLocation("u_fadeInCameraDistance");
        uFadeDensityCameraDistanceLocation = getUniformLocation("u_fadeDensityCameraDistance");        
        
        // Retrieve attribute locations for the shader program.
        aPositionLocation = getAttribLocation("a_Position");
        aNormalLocation = getAttribLocation("a_Normal");
        aTextureCoordinatesLocation = getAttribLocation("a_TextureCoordinates");        
    }

    public static void releaseTexture(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }            

    public void loadTextureUniform(int textureId){
        loadTextureUniform(GL_TEXTURE0, textureId, uTextureUnitLocation, 0);
    }  
    
    public void loadTextureScale(float textureScale){
        glUniform1f(uTextureScaleLocation, textureScale);
    }     
    
    public void loadAmbientColor(){
//        glUniform4fv(uAmbientColorLocation, 1,  GameWorld.getAmbientColor().rgba, 0);
    }    
    
    public void loadAmbientColor(ZybColor color){
        glUniform4fv(uAmbientColorLocation, 1,  color.rgba, 0);
    }       
    
    public void loadDiffuseColor(ZybColor diffuseColor){
        glUniform4fv(uDiffuseColorLocation, 1,  diffuseColor.rgba, 0);
    }    
    
    public void loadDiffuseColor(float[] rgba){
        glUniform4fv(uDiffuseColorLocation, 1,  rgba, 0);
    }     
    
    public void loadAlphaUniform(float alpha){
        glUniform1f(uAlphaLocation, alpha);
    }    
    
    public void loadSunPosition(){
//        glUniform4fv(uSunPositionLocation, 1,  Sun.POSITION.xyzw, 0);
    }    
    
    public void loadCameraPosition(Vector3D cameraPosition){
        glUniform4fv(uCameraPositionLocation, 1,  cameraPosition.xyzw, 0);
    }
    
    public void loadCameraDirection(Vector3D cameraDirection){
        glUniform4fv(uCameraDirectionLocation, 1,  cameraDirection.xyzw, 0);
    }    
    
    public void loadWorldUniforms(Camera camera){
        glUniform4fv(uCameraPositionLocation, 1,  camera.getEYE().xyzw, 0);
        glUniform4fv(uCameraDirectionLocation, 1,  camera.getEyeTargetVector().xyzw, 0);
        
//        glUniform4fv(uSunPositionLocation, 1,  Sun.POSITION.xyzw, 0);
        
//        glUniform4fv(uAmbientColorLocation, 1,  GameWorld.getAmbientColor().rgba, 0);
//
//        glUniform4fv(uFogColorLocation, 1, GameWorld.getFogColor().rgba, 0);
//        glUniform1f(uFogMinDistFromCamLocation, GameWorld.getFogMinDistance());
//        glUniform1f(uFogGradientDepthLocation, GameWorld.getFogDensity());
    }

    public void loadFogColor(ZybColor fogColor){
        glUniform4fv(uFogColorLocation, 1, fogColor.rgba, 0);
    }

    public void loadFadingUniforms(float fadeIn, float fadeDensity){
        glUniform1f(uFadeInCameraDistanceLocation, fadeIn);
        glUniform1f(uFadeDensityCameraDistanceLocation, fadeDensity);
    }     

    public void loadModelMatrix(float[] matrix){
        glUniformMatrix4fv(uMMatrixLocation, 1, false, matrix, 0);
    }

    public void loadMVPMatrix(float[] matrix){
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrix, 0);
    } 

    public int getuAlphaLocation() {
        return uAlphaLocation;
    }
    
    public int getuMMatrixLocation() {
        return uMMatrixLocation;
    }

    public int getuMVPMatrixLocation() {
        return uMVPMatrixLocation;
    }

    public int getuTextureScaleLocation() {
        return uTextureScaleLocation;
    }
    
    public int getuTextureUnitLocation() {
        return uTextureUnitLocation;
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }    

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }    

    public int getuDiffuseColorLocation() {
        return uDiffuseColorLocation;
    }
    
    

}    

