//package zybandroid.opengl.objects;
//
//import hu.zybon.zybonotopia.scene.ShadowMapRenderer;
//import zybandroid.opengl.blenderdatas.MeshData;
//import hu.zybon.zybonotopia.scene.Sky;
//import zybandroid.opengl.programs.AnimatedTextureShaderProgram;
//import zybandroid.opengl.programs.TextureWithShadowShaderProgram;
//
///**
// *
// * @author zybon
// * Created 2018.01.14. 14:43:19
// */
//public class AnimatedTexturedMesh extends Mesh{
//    private static final String TAG = "TexturedMesh";
//
//    private static AnimatedTextureShaderProgram animatedTextureShaderProgram;
//    
//    public static void setTextureShaderProgram(AnimatedTextureShaderProgram animatedTextureShaderProgram) {
//        AnimatedTexturedMesh.animatedTextureShaderProgram = animatedTextureShaderProgram;
//    }    
//
//    public static AnimatedTextureShaderProgram getTextureShaderProgram() {
//        return animatedTextureShaderProgram;
//    }
//    
//    private static TextureWithShadowShaderProgram textureWithShadowShaderProgram;
//    
//    public static void setTextureWithShadowShaderProgram(TextureWithShadowShaderProgram textureWithShadowShaderProgram) {
//        AnimatedTexturedMesh.textureWithShadowShaderProgram = textureWithShadowShaderProgram;
//    }
//
//    public static TextureWithShadowShaderProgram getTextureWithShadowShaderProgram() {
//        return textureWithShadowShaderProgram;
//    }
//    
//    private int glTextureId;
//    private static ShadowMapRenderer shadowMapRenderer;
//    
//    public static void setShadowMapRenderer(ShadowMapRenderer shadowMapRenderer) {
//        AnimatedTexturedMesh.shadowMapRenderer = shadowMapRenderer;
//    }
//
//    public static ShadowMapRenderer getShadowMapRenderer() {
//        return shadowMapRenderer;
//    }
//    
//    private float alpha = 1.0f;
//    
//    public AnimatedTexturedMesh(MeshData meshData) {
//        super(meshData);
//    }
//    
//    public void hide() {
//        if (alpha>0.0) {
//            alpha -= 0.05f;
//            if (alpha<0.0f) {
//                alpha = 0.0f;
//            }
//        }
//    }
//    
//    public void show(){
//        if (alpha<1.0) {
//            alpha += 0.05f;
//            if (alpha>1.0f) {
//                alpha = 1.0f;
//            }
//        }
//    }
//    
////    public boolean isVisible(){
////        return alpha>0;
////    }
//
//    public float getAlpha() {
//        return alpha;
//    }
//
//    @Override
//    public void useShaderProgram() {
//            animatedTextureShaderProgram.useProgram();
//    }
//    
//    @Override
//    public void setUniforms() {
//        animatedTextureShaderProgram.loadAlphaUniform(alpha);
//        animatedTextureShaderProgram.loadWorldUniforms(getCamera());
//        animatedTextureShaderProgram.loadModelMatrix(getModelMatrix());
//        animatedTextureShaderProgram.loadMVPMatrix(getModelViewProjectionMatrix());
//        animatedTextureShaderProgram.loadTextureUniform(glTextureId);
//    }
//
//    @Override
//    public void setAttibuteDatas() {
//        setAttibutePositionData(animatedTextureShaderProgram.getPositionAttributeLocation());
//        setAttibuteNormalData(animatedTextureShaderProgram.getNormalAttributeLocation());
//        setAttibuteTextureCoordData(animatedTextureShaderProgram.getTextureCoordinatesAttributeLocation());
//        setAttibuteVertexIndexData(animatedTextureShaderProgram.getVertexIndexLocation());
//    }
//
//    @Override
//    public void onDraw() {
//        drawWithTriangles();
//    }
//    
//
//
//}
