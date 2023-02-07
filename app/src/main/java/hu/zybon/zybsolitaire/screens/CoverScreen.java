package hu.zybon.zybsolitaire.screens;

import android.content.Context;
import android.opengl.GLES20;
import hu.zybon.zybsolitaire.GameRenderer;
import hu.zybon.zybsolitaire.MainRenderer;
import hu.zybon.zybsolitaire.names.ShadersNames;
import zybandroid.opengl.gui.SimpleQuad;
import zybandroid.opengl.programs.ShaderProgram;
import zybandroid.opengl.text.TextObject;
import zybandroid.opengl.util.TextFileReader;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2021.02.18. 9:50:03
 */
public class CoverScreen {
    
    private final CoverScreenShaderProgram coverScreenShaderProgram;
    
    private final SimpleQuad fullCover = SimpleQuad.createFullScreenQuad();
    
    
    private float movieEffect = 0f;
    private final float MOVIE_EFFECT_BAR_HEIGHT_IN_GL_PERCENT = 0.22f;
    private final float MOVIE_EFFECT_FADE_TIME_IN_MS = 1100f;
    
    private float alpha = 1f;
    private final float COVER_EFFECT_FADE_TIME_IN_MS = 1000f;
    
    private long effectStartTime;
    
    public enum Effect{
        MOVIE_FADE_IN,
        MOVIE,
        MOVIE_FADE_OUT,
        FULL_COVER_FADE_IN,
        FULL_COVER,
        FULL_COVER_FADE_OUT,
        NOTHING
    }
    
    private Effect effect = Effect.NOTHING;
    
    public CoverScreen(Context context) {
        coverScreenShaderProgram = new CoverScreenShaderProgram(context);
    }
    
    public void startMovieEffectFadeIn(){
        effect = Effect.MOVIE_FADE_IN;
        alpha = 1.0f;
        effectStartTime = System.currentTimeMillis();
    }
    
    public void startMovieEffectFadeOUt(){
        effect = Effect.MOVIE_FADE_OUT;
        alpha = 1.0f;
        effectStartTime = System.currentTimeMillis();        
    }   
    
    public void startCoverEffectFadeIn(){
        effect = Effect.FULL_COVER_FADE_IN;
        alpha = 0.0f;
        movieEffect = 0.0f;
        effectStartTime = System.currentTimeMillis();
    }
    
    public void startCoverEffectFadeOut(){
        effect = Effect.FULL_COVER_FADE_OUT;
        alpha = 1.0f;
        movieEffect = 0.0f;
        effectStartTime = System.currentTimeMillis();        
    }     
    
    private void update(){
        switch (effect) {
            case MOVIE_FADE_IN:
                movieEffectFadeIn();
                break;
            case MOVIE:
                movieEffect();
                break;                
            case MOVIE_FADE_OUT:
                movieEffectFadeOut();
                break;  
                
            case FULL_COVER_FADE_IN:
                coverEffectFadeIn();
                break;
            case FULL_COVER_FADE_OUT:
                coverEffectFadeOut();
                break;                 

        }
    
    }
    
    private void movieEffectFadeIn(){
        float elapsedTime = System.currentTimeMillis()-effectStartTime;
        if (elapsedTime>=MOVIE_EFFECT_FADE_TIME_IN_MS){
            movieEffect = 1.0f-MOVIE_EFFECT_BAR_HEIGHT_IN_GL_PERCENT;
            effect = Effect.MOVIE;
            return;
        }
        
        float effectPercent = (MOVIE_EFFECT_BAR_HEIGHT_IN_GL_PERCENT)*elapsedTime/MOVIE_EFFECT_FADE_TIME_IN_MS;
        movieEffect = 1.0f-effectPercent;
        
    }
    
    private void movieEffect(){
        float elapsedTime = System.currentTimeMillis()-effectStartTime;
        if (elapsedTime>=3000){
            startMovieEffectFadeOUt();
        }
        
    }    
    
    private void movieEffectFadeOut(){
        float elapsedTime = System.currentTimeMillis()-effectStartTime;
        if (elapsedTime>=MOVIE_EFFECT_FADE_TIME_IN_MS){
            movieEffect = 1.0f;
            effect = Effect.NOTHING;
            return;
        }
        
        float effectPercent = (MOVIE_EFFECT_BAR_HEIGHT_IN_GL_PERCENT)*(1.0f-elapsedTime/MOVIE_EFFECT_FADE_TIME_IN_MS);
        movieEffect = 1.0f-effectPercent;
        
    }  
    
    private void coverEffectFadeIn(){
        float elapsedTime = System.currentTimeMillis()-effectStartTime;
        if (elapsedTime>=COVER_EFFECT_FADE_TIME_IN_MS){
            effect = Effect.FULL_COVER;
            return;
        }
        
        alpha = elapsedTime/COVER_EFFECT_FADE_TIME_IN_MS;
    }   
    
    private void coverEffectFadeOut(){
        float elapsedTime = System.currentTimeMillis()-effectStartTime;
        if (elapsedTime>=COVER_EFFECT_FADE_TIME_IN_MS){
            effect = Effect.NOTHING;
            return;
        }
        
        alpha = 1.0f-elapsedTime/COVER_EFFECT_FADE_TIME_IN_MS;
        
    }      

    public Effect getEffect() {
        return effect;
    }
    
    
    public void draw(){
        if (effect == Effect.NOTHING) {return;}
        update();
        MainRenderer.glBlendOn();
        coverScreenShaderProgram.useProgram();
        coverScreenShaderProgram.loadDataUniform(alpha, movieEffect);
        fullCover.setAttibutePositionData(coverScreenShaderProgram.aPositionLocation);
        fullCover.drawWithTriangles();
        MainRenderer.glBlendOff();
    }
    


    private class CoverScreenShaderProgram extends ShaderProgram{
        
        private final int uCoverDataLocation;  
        
        private final int aPositionLocation;

        public CoverScreenShaderProgram(Context context) {
            super(
                TextFileReader.readTextFromAssets(context,
                            ShadersNames.VertexShaders.coverscreen), 
                TextFileReader.readTextFromAssets(context,
                        ShadersNames.FragmentShaders.coverscreen),
                    "coverscreen");
            uCoverDataLocation = getUniformLocation("u_coverData");
            
            aPositionLocation = getAttribLocation("a_Position");
        }
        
        public void loadDataUniform(float alpha, float movieEffect){
            GLES20.glUniform2f(uCoverDataLocation, alpha, movieEffect);
        }           
        
        public int getaPositionLocation() {
            return aPositionLocation;
        }        
        
    }
    


}
