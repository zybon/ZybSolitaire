package zybandroid.opengl.text;

import android.graphics.RectF;
import zybandroid.opengl.programs.TextShaderProgram;
import android.opengl.GLES20;
import hu.zybon.zybsolitaire.MainRenderer;
import java.util.ArrayList;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.objects.OpenGlObject;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;
import zybandroid.opengl.util.VertexArray;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.10.18. 16:54:22
 */
public final class TextObject extends OpenGlObject{
    
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXCOORD_COMPONENT_COUNT = 2;
    
    private static final int TOTAL_COMPONENT_COUNT = (
            POSITION_COMPONENT_COUNT+
            TEXCOORD_COMPONENT_COUNT);
    
    private static final int STRIDE = TOTAL_COMPONENT_COUNT*BYTES_PER_FLOAT;
    
    private final int TOTAL_COMPONENT_COUNT_PER_QUAD;       

    private final TextMap textMap;
    private final float[] vertexAndTexCoordDataArray;
    private final VertexArray vertexAndTexCoordArray; 
    private int currentOffset;
    
    private int currentCharacterCount;
    private static final int maxCharacterCount = 512;
    
    private final int vertexNumber = 6;
    
    private final Text text = new Text();
    
    private final ZybColor color = new ZybColor(ZybColor.WHITE);
    private float alpha = 0.0f;
    
    
//    private static final long SHOWING_TIME_IN_MS = 3000;
    private long showStartTime;
    private long showingTime = 3000;
    private boolean alwaysDrawable = false;
    
    private final TextShaderProgram textShaderProgram;
    private float scaleY;
    private float scaleX;
    private float postionYInGl;
    private float postionXInGl;
    
    public enum Align{
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT, 
    }
    
    private Align align = Align.TOP_LEFT;
    
    private final Vector3D positionInText = new Vector3D(0f, 0f, 0f);
    private float posStartX = 0;
    private float offsetX = 0;
    private float MAX_LINE_WIDTH = 2f;
    
    private final RectF border = new RectF();
    private final TextBackground background = new TextBackground();
    private boolean backgroundDraw = false;
    
    TextObject(TextMap textMap, TextShaderProgram textShaderProgram) {
        super("textobject");
        this.textMap = textMap;
        this.textShaderProgram = textShaderProgram;
        TOTAL_COMPONENT_COUNT_PER_QUAD = vertexNumber*TOTAL_COMPONENT_COUNT;
        this.vertexAndTexCoordDataArray = new float[maxCharacterCount*TOTAL_COMPONENT_COUNT_PER_QUAD];
        this.vertexAndTexCoordArray = new VertexArray(vertexAndTexCoordDataArray); 
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setBackgroundColorTop(ZybColor color) {
        this.background.setColorTop(color);
    }
    
    public void setBackgroundColorBottom(ZybColor color) {
        this.background.setColorBottom(color);
    }    

    public void setBackgroundAlpha(float alpha) {
        this.background.setAlpha(alpha);
    }
    
    
    
    public void showBackground(){
        this.backgroundDraw = true;
    }    
    
    public void hideBackground(){
        this.backgroundDraw = false;
    }

    public boolean isBackgroundDraw() {
        if (backgroundDraw){
            return isDrawable();
        }
        return false;
    }
    
    public void drawBackground(){
        background.draw();
    }
    
    public void setAlwaysDrawable(boolean alwaysDrawable) {
        this.alwaysDrawable = alwaysDrawable;
        if (alwaysDrawable) {
            alpha = 1.0f;
        }
    }
    
    public void hideImmediately() {
        alpha = -1.0f;
    }    
    
    public void hide() {
        if (alpha>0.0) {
            alpha -= 0.05f;
            if (alpha<0.0f) {
                alpha = -1.0f;
            }
        }
    }

    public void show(long timeInMs){
        showingTime = timeInMs;
//        if (alpha<1.0) {
//            alpha += 0.05f;
//            if (alpha>1.0f) {
//                alpha = 1.0f;
//            }
//        }
        alpha = 1.0f;
        showStart();
    }    
    
    public float getAlpha() {
        return alpha;
    }    
    
    public void setPositionInGL(float glX, float glY){
        this.postionXInGl = glX;
        this.postionYInGl = glY;
    }
    
    public void setScale(float scale) {
        scaleY = scale;
        scaleX = scale*MainRenderer.getScreenAspectRatio();
        scale(scaleX, scaleY, 1.0f);
    }
    
    public void setScale2(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        scale(scaleX, scaleY, 1.0f);
    }   
    
    public void setAlign(Align align){
        this.align = align;
    }
    
    public void setColor(ZybColor zybColor) {
        this.color.set(zybColor);
    }

    public ZybColor getColor() {
        return color;
    }
    
    public void setText(String newText) {
        this.text.set(newText);
    }
    
    public boolean isEmpty(){
        return text.originalText.isEmpty();
    }
    
    private void updateTextBuffer(){
        currentOffset = 0;
        currentCharacterCount = 0;
        positionInText.reset();
        
        border.left = 0;
        border.right = text.maxParagraphWidth;
        border.top = textMap.getBaseHeight();
        border.bottom = border.top-text.paragraphList.size()*textMap.getLineHeight(); 
        
        textAlign();
        
        loadParagraphs();
        updateBuffer();
        
        
        
        border.left *= scaleX;
        border.right *= scaleX;
        border.top *= scaleY;
        border.bottom *= scaleY;
//        border.offset(postionXInGl, postionYInGl);
        background.setBorder(border);
        
        
        
        
        text.updated();
    }
    
    private void textAlign(){
        switch (align) {
            case TOP_LEFT:
                border.offset(0, -border.top);
                posStartX = 0;
                offsetX = 0;
                break;             
            case TOP_CENTER:
                border.offset(-border.width()/2, -border.top);
                posStartX = text.maxParagraphWidth/2;
                offsetX = 1;
                break; 
            case TOP_RIGHT:
                border.offset(-border.width(), -border.top);
                posStartX = 0;
                offsetX = 0;
                break;                 
            case BOTTOM_LEFT:
                border.offset(0, -border.bottom);
                posStartX = 0;
                offsetX = 0;
                break;  
            case BOTTOM_CENTER:
                border.offset(-border.width()/2, -border.bottom);
                posStartX = text.maxParagraphWidth/2;
                offsetX = 1;
                break;                
            case BOTTOM_RIGHT:
                border.offset(-border.width(), -border.bottom);
                posStartX = 0;
                offsetX = 0;
                break;  
            case MIDDLE_CENTER:
                border.offset(-border.width()/2, 0);
                posStartX = text.maxParagraphWidth/2;
                offsetX = 1;
                break;                  
                
        }
   
    }

    public RectF getBorder() {
        return border;
    }
    
//    public float getMaxParagraphWidth(){
//        return this.text.maxParagraphWidth*scaleX;
//    }
    
    private void loadParagraphs(){
        for (Paragraph paragraph : text.getParagraphList()) {
            loadParagraph(paragraph);
            lineBreak();
        }        
    }
    
    private void loadParagraph(Paragraph paragraph){
        float lineWidth = 0.0f; 
        positionInText.setX(posStartX-offsetX*paragraph.textWidth/2);
        for (Word word : paragraph.getWordList()) {
            
            if (lineWidth+word.getWidth()*scaleX>MAX_LINE_WIDTH){
                positionInText.setX(posStartX-offsetX*paragraph.textWidth/2);
                lineBreak();
                lineWidth = 0.0f;
            }
            loadWord(word);
            lineWidth+=word.getWidth()*scaleX;
            
            addSpace();
            lineWidth += textMap.getSpaceWidth()*scaleX;
        }        
    }   
    
    private void loadWord(Word word){
        CharacterMesh characterMesh;
        for (char aChar : word.getChars()) {
            if (currentCharacterCount == maxCharacterCount){
                break;
            }            
            characterMesh = textMap.getCharacterMesh(aChar);
            addCharacterDataToArray(characterMesh, positionInText);
            currentCharacterCount++;
            positionInText.translate(characterMesh.getCharWidth(), 0, 0); 

        }        
    }
    
    private void lineBreak(){
        
        positionInText.translate(0, -textMap.getLineHeight(), 0);
    }
    
    private void addSpace(){
        positionInText.translate(textMap.getSpaceWidth(), 0, 0);
            
    }
    
    private void addCharacterDataToArray(CharacterMesh characterMesh, Vector3D positionInText){
        int[] triangles = characterMesh.getMeshData().getTriangles();
        float[] vertexCoordList = characterMesh.getMeshData().getVertexCoordList();
        float[] textureCoordList = characterMesh.getMeshData().getTextureCoordList();   
        int i = 0;
        int v;
        int n;
        int t;
        float posX;
        float posY;
        for (int j = 0; j < triangles.length; j++) {
            
            v = triangles[j++]*3;
            posX = vertexCoordList[v++] + positionInText.getX();
//            if (posX<border.left){
//                border.left = posX;
//            }
//            if (posX>border.right){
//                border.right = posX;
//            }            
            posY = vertexCoordList[v++] + positionInText.getY();
//            if (posY>border.top){
//                border.top = posY;
//            }
//            if (posY<border.bottom){
//                border.bottom = posY;
//            }

            vertexAndTexCoordDataArray[currentOffset++] = posX;
            vertexAndTexCoordDataArray[currentOffset++] = posY;
            vertexAndTexCoordDataArray[currentOffset++] = vertexCoordList[v++] + positionInText.getZ();
            
            //normal
            j++;
            
            t = triangles[j]*2;            
            vertexAndTexCoordDataArray[currentOffset++] = textureCoordList[t++];
            vertexAndTexCoordDataArray[currentOffset++] = textureCoordList[t++];

        }     
        
    }    
    
    private void updateBuffer(){
        
        vertexAndTexCoordArray.updateBuffer(vertexAndTexCoordDataArray, 0, currentCharacterCount*TOTAL_COMPONENT_COUNT_PER_QUAD);
    
    }
    
    private void showStart(){
        showStartTime = System.currentTimeMillis();
    }
    
    void draw(){
        if (text.isChanged()){
            updateTextBuffer();
        }
        translateTo(border.left+postionXInGl, border.top-textMap.getBaseHeight()*scaleY+postionYInGl, 0);
        loadUniformModelMatrix(textShaderProgram.getuMMatrixLocation());
        textShaderProgram.loadAlphaUniform(alpha);
        textShaderProgram.loadColorUniform(color);
        setAttibutePositionData(textShaderProgram.getPositionAttributeLocation());
        setAttibuteTextureCoordData(textShaderProgram.getTextureCoordinatesAttributeLocation());
//        setAttibuteColorData(textShaderProgram.getColorAttributeLocation());
        drawTriangles();
        
    } 
    
    boolean isDrawable(){
        if (alwaysDrawable) {return true;}
        if (alpha < 0) {
            return false;
        }
        if (System.currentTimeMillis()-showStartTime > showingTime) {
            hide();
        }
        return true;
    }

    public void setAttibutePositionData(int attributeLocation) {
        vertexAndTexCoordArray.setVertexAttribPointer(0, 
                attributeLocation, 
                3, 
                STRIDE);
    }   
    
    public void setAttibuteTextureCoordData(int attributeLocation) {
        vertexAndTexCoordArray.setVertexAttribPointer(3, 
                attributeLocation, 
                2, 
                STRIDE);
    }     
    
    public void drawTriangles() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, currentCharacterCount*vertexNumber);
    }          

    private class Text{
        private String originalText = "";
        private final ArrayList<Paragraph> paragraphList = new ArrayList<Paragraph>();
        private boolean changed = false;
        private float maxParagraphWidth;
        
        public Text() {
        }

        public boolean isChanged() {
            return changed;
        }
        
        public void updated(){
            this.changed = false;
        }
        
        private void set(String newText){
            newText = newText.replaceAll("_", " ");
            
            if (this.originalText.equals(newText)) {
                return;
            }
            this.originalText = newText;
            paragraphList.clear();
            if (!originalText.isEmpty()) {
                createParagraphs();
            }           
            calcWidth();
            changed = true;
        }
        
        private void calcWidth(){
            float maxWidth = 0;
            float width;
            for (Paragraph paragraph : paragraphList) {
                width = 0;
                for (Word word : paragraph.wordList) {
                    width += word.width;
                    width += textMap.getSpaceWidth();
                }
                width -= textMap.getSpaceWidth();
                paragraph.textWidth = width;
                
                if (width>maxWidth){
                    maxWidth = width;
                }
            }
            this.maxParagraphWidth = maxWidth;
        }
        
        private void createParagraphs(){
            if (!originalText.contains("\n")) {
                paragraphList.add(new Paragraph(originalText));
                return;
            }
            String[] paragraphs = originalText.split("\n");
            for (String paragraph : paragraphs) {
                paragraphList.add(new Paragraph(paragraph));
            }
        }

        public ArrayList<Paragraph> getParagraphList() {
            return paragraphList;
        }
        
    }
    
    private class Paragraph{
        private final String paragraphText;
        private final ArrayList<Word> wordList = new ArrayList<Word>();
        private float textWidth;

        public Paragraph(String paragraphText) {
            this.paragraphText = paragraphText;
            if (!paragraphText.isEmpty()) {
                createWords();
            }
        }
        
        private void createWords(){
            if (!paragraphText.contains(" ")) {
                wordList.add(new Word(paragraphText));
                return;
            }
            String[] words = paragraphText.split(" ");
            for (String word : words) {
                wordList.add(new Word(word));
            }
        }

        public ArrayList<Word> getWordList() {
            return wordList;
        }
        
        
    }    
   
    private class Word{
        private final char[] chars;
        private final float width;

        public Word(String word) {
            chars = word.toCharArray();
            float w = 0.0f;
            for (char aChar : chars) {
                w += textMap.getCharacterWidth(aChar);
            }
            width = w;
        }

        public float getWidth() {
            return width;
        }

        public char[] getChars() {
            return chars;
        }
        
    }
}

