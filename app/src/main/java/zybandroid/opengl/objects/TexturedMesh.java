package zybandroid.opengl.objects;

//import static hu.zybon.zybonotopia.scene.SceneRenderer.shadowMapRenderer;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.camera.Camera;
import zybandroid.opengl.programs.TextureShaderProgram;

/**
 *
 * @author zybon
 * Created 2018.01.14. 14:43:19
 */
public class TexturedMesh extends Mesh {
    private static final String TAG = "TexturedMesh";

    private final TextureShaderProgram textureShaderProgram = TextureShaderProgram.getTextureShaderProgram();
    
    private float alpha = 1.0f;
    
    public TexturedMesh(MeshData meshData) {
        super(meshData);
    }
    
    public void hide() {
        if (alpha>0.0) {
            alpha -= 0.05f;
            if (alpha<0.0f) {
                alpha = 0.0f;
            }
        }
    }
    
    public void show(){
        if (alpha<1.0) {
            alpha += 0.05f;
            if (alpha>1.0f) {
                alpha = 1.0f;
            }
        }
    }
    
//    public boolean isVisible(){
//        return alpha>0;
//    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public void useShaderProgram() {
        textureShaderProgram.useProgram();
    }
    
    @Override
    public void setUniforms() {
        textureShaderProgram.loadAlphaUniform(alpha);
        textureShaderProgram.loadWorldUniforms(getCamera());
        textureShaderProgram.loadModelMatrix(getModelMatrix());
        textureShaderProgram.loadMVPMatrix(getModelViewProjectionMatrix());
        textureShaderProgram.loadTextureUniform(getGlTextureId());
        textureShaderProgram.loadTextureScale(getTextureScale().getX());
    }

    @Override
    public void setAttibuteDatas() {
        setAttibutePositionData(textureShaderProgram.getPositionAttributeLocation());
        setAttibuteNormalData(textureShaderProgram.getNormalAttributeLocation());
        setAttibuteTextureCoordData(textureShaderProgram.getTextureCoordinatesAttributeLocation());
    }

    @Override
    public void onDraw() {
        drawWithTriangles();
    }


}
