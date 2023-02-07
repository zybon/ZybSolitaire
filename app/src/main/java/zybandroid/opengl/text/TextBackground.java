package zybandroid.opengl.text;

import android.graphics.RectF;
import android.opengl.GLES20;
import zybandroid.opengl.programs.ShaderProgram;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;
import zybandroid.opengl.util.VertexArray;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2020.06.18. 12:54:45
 */
public class TextBackground {
    
    public static final TextBackGroundShaderProgram textShaderProgram = new TextBackGroundShaderProgram();
    
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 4;
    
    private static final int TOTAL_COMPONENT_COUNT = (
            POSITION_COMPONENT_COUNT+
            COLOR_COMPONENT_COUNT);
    
    private static final int STRIDE = TOTAL_COMPONENT_COUNT*BYTES_PER_FLOAT;
    
    private final int vertexNumber = 6;   
    private final int TOTAL_COMPONENT_COUNT_PER_QUAD = vertexNumber*TOTAL_COMPONENT_COUNT;  
    
    private final float[] vertexDataArray = new float[TOTAL_COMPONENT_COUNT_PER_QUAD];
    private final VertexArray vertexArray;
    
    private final ZybColor colorTop = new ZybColor(0x88222222);
    private final ZybColor colorBottom = new ZybColor(0x88ffffff);
    private float alpha = 0.4f;

    public TextBackground() {
        this.vertexArray = new VertexArray(vertexDataArray); 
    }
    
    public void setColorTop(ZybColor color){
        this.colorTop.set(color);
    }
    
    public void setColorBottom(ZybColor color){
        this.colorBottom.set(color);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    
    public void setBorder(RectF borderRect){
        updateData(borderRect);
        vertexArray.updateBuffer(vertexDataArray, 0, TOTAL_COMPONENT_COUNT_PER_QUAD);
    }
    
    private void updateData(RectF rect){
        int offset = 0;
        
        //1.
        vertexDataArray[offset++] = rect.right;
        vertexDataArray[offset++] = rect.bottom;
        vertexDataArray[offset++] = 0;
        
        vertexDataArray[offset++] = colorBottom.getR();
        vertexDataArray[offset++] = colorBottom.getG();
        vertexDataArray[offset++] = colorBottom.getB();      
        vertexDataArray[offset++] = colorBottom.getA();
        
        vertexDataArray[offset++] = rect.left;
        vertexDataArray[offset++] = rect.top;
        vertexDataArray[offset++] = 0;
        
        vertexDataArray[offset++] = colorTop.getR();
        vertexDataArray[offset++] = colorTop.getG();
        vertexDataArray[offset++] = colorTop.getB();      
        vertexDataArray[offset++] = colorTop.getA();        
        
        vertexDataArray[offset++] = rect.left;
        vertexDataArray[offset++] = rect.bottom;
        vertexDataArray[offset++] = 0;
        
        vertexDataArray[offset++] = colorBottom.getR();
        vertexDataArray[offset++] = colorBottom.getG();
        vertexDataArray[offset++] = colorBottom.getB();      
        vertexDataArray[offset++] = colorBottom.getA();  
        
        
        //2.
        vertexDataArray[offset++] = rect.right;
        vertexDataArray[offset++] = rect.bottom;
        vertexDataArray[offset++] = 0;
        
        vertexDataArray[offset++] = colorBottom.getR();
        vertexDataArray[offset++] = colorBottom.getG();
        vertexDataArray[offset++] = colorBottom.getB();      
        vertexDataArray[offset++] = colorBottom.getA();        
        
        vertexDataArray[offset++] = rect.right;
        vertexDataArray[offset++] = rect.top;
        vertexDataArray[offset++] = 0;
        
        vertexDataArray[offset++] = colorTop.getR();
        vertexDataArray[offset++] = colorTop.getG();
        vertexDataArray[offset++] = colorTop.getB();      
        vertexDataArray[offset++] = colorTop.getA();        
        
        vertexDataArray[offset++] = rect.left;
        vertexDataArray[offset++] = rect.top;
        vertexDataArray[offset++] = 0;
        
        vertexDataArray[offset++] = colorTop.getR();
        vertexDataArray[offset++] = colorTop.getG();
        vertexDataArray[offset++] = colorTop.getB();      
        vertexDataArray[offset++] = colorTop.getA();   
        
        
        
    }
    
    public void draw(){
        textShaderProgram.loadAlphaUniform(alpha);
        setAttibutePositionData(textShaderProgram.getPositionAttributeLocation());
        setAttibuteColorData(textShaderProgram.getColorAttributeLocation());
        drawTriangles();    
    }
    
    private void setAttibutePositionData(int attributeLocation) {
        vertexArray.setVertexAttribPointer(0, 
                attributeLocation, 
                POSITION_COMPONENT_COUNT, 
                STRIDE);
    }    
    
    private void setAttibuteColorData(int attributeLocation) {
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, 
                attributeLocation, 
                COLOR_COMPONENT_COUNT, 
                STRIDE);
    }  
    
    private void drawTriangles() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexNumber);
    }     
    
    private static final String vertexShader = 
            "attribute vec4 a_Position;  \n" +
            "attribute vec4 a_Color;\n" +
            "\n" +
            "varying vec4 v_Color;\n" +
            "\n" +
            "void main()                    \n" +
            "{               \n" +
            "    v_Color = a_Color;          \n" +
            "    gl_Position = a_Position;  \n" +
            "}  "
    ;
    
    private static final String fragmentShader = 
            "precision mediump float;\n" +
            "uniform float u_alpha; \n" +
            "\n" +
            "varying vec4 v_Color;\n" +
            "\n" +
            "void main()                         \n" +
            "{   \n" +
            "    gl_FragColor = v_Color;\n" +
            "    gl_FragColor.a = v_Color.a*u_alpha;\n" +
            "}";    
    
    public static class TextBackGroundShaderProgram extends ShaderProgram{

        private final int uAlphaLocation;

        // Attribute locations
        private final int aPositionLocation;
        private final int aColorLocation;

        public TextBackGroundShaderProgram() {
            super(vertexShader, fragmentShader, "textbackground");
            uAlphaLocation = getUniformLocation("u_alpha");

            // Retrieve attribute locations for the shader program.
            aPositionLocation = getAttribLocation("a_Position");
            aColorLocation = getAttribLocation("a_Color");            
        }

        public void loadAlphaUniform(float alpha){
            GLES20.glUniform1f(uAlphaLocation, alpha);
        }      

        public int getPositionAttributeLocation() {
            return aPositionLocation;
        }

        public int getColorAttributeLocation() {
            return aColorLocation;
        }
        
        


    }    
}
