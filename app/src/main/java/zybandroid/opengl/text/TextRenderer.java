package zybandroid.opengl.text;

import zybandroid.opengl.programs.TextShaderProgram;
import android.content.Context;
import hu.zybon.zybsolitaire.R;
import java.util.ArrayList;
import zybandroid.opengl.util.TextureHelper;

/**
 *
 * @author zybon
 * Created 2018.10.16. 11:36:32
 */
public class TextRenderer{
    
    private final TextShaderProgram textShaderProgram = TextShaderProgram.getTextShaderProgram();
    private final int textGlTexture;
    
    private final TextMap textMap;
    
    private final ArrayList<TextObject> texts = new ArrayList<TextObject>();
    
    public TextRenderer(Context context) {

        this.textGlTexture = TextureHelper.getGlTextureIdFromResID(
                context,
                "text", 
                R.drawable.text_map);
        textMap = new TextMap(context);
    }
    
    public TextObject createTextObject(String initString){
        TextObject textObject = new TextObject(textMap, textShaderProgram);
        textObject.setText(initString);
        texts.add(textObject);
        return textObject;
    }
 
    public void draw(){
        
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);  
        
//        TextBackground.textShaderProgram.useProgram();
//        for (TextObject text : texts) {
//            if (text.isBackgroundDraw()){
//                text.drawBackground();
//            }
//        }
        
        textShaderProgram.useProgram();
        textShaderProgram.activateTexture(textGlTexture);        
        for (TextObject text : texts) {
            if(text.isDrawable()) {
                text.draw();
            }
        }
        
//        glDisable(GL_BLEND);
    }
    

}
