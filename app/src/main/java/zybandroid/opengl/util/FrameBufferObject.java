package zybandroid.opengl.util;

import android.opengl.GLES20;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glTexParameteri;
import android.opengl.GLES30;
import android.util.Log;

/**
 *
 * @author zybon
 * Created 2020.02.13. 13:45:00
 */
public class FrameBufferObject {
    
    public enum DEPTH_TYPE{
        NONE,
        BUFFER,
        BUFFER_WITH_STENCIL,
        TEXTURE
    }
    
    private static final String TAG = "FrameBufferObject";
    
    private int fboId;
    private int colorTextureId;
    private int depthTextureId;
    private int depthRenderBufferId;    
    private int stencilRenderBufferId; 
    
    private final int mWidth;
    private final int mHeight;
    
    private final DEPTH_TYPE depthType;
    
    private final ZybColor clearColor = new ZybColor(0xff000000);
    
    public FrameBufferObject(float width, float height, DEPTH_TYPE depthType){
        mWidth = (int)width;
        mHeight = (int)height;
        this.depthType = depthType;
        init();
    }
    
    public void setTextureWrapToEdge(){
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, colorTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);      
        glBindTexture(GL_TEXTURE_2D, 0);    
    }
    
    public void setTextureFilterToNearest(){
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, colorTextureId);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);      
        glBindTexture(GL_TEXTURE_2D, 0);    
    }    
    
    public void setClearColor(ZybColor color){
        this.clearColor.set(color);
    }
    
    private void init(){
        
//        String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
//
//        if (extensions.contains("OES_depth_texture")) {
//            throw new AssertionError(extensions);
//        }

        createFrameBuffer();
        
        createColorTexture();
//        
        switch(depthType){
            case BUFFER:
                createDepthRenderBuffer(); 
                break;
            case BUFFER_WITH_STENCIL:
//                createDepthRenderBuffer();
                createStencilBuffer();
                break;                
            case TEXTURE:
                createDepthTexture(); 
                break;
        }
        
        

        int FBOstatus = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if(FBOstatus != GLES20.GL_FRAMEBUFFER_COMPLETE) {
                Log.e(TAG, "GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");
                throw new RuntimeException("GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");
        }        
        
        //we are done, reset
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        
    }
    

    
    private void createFrameBuffer(){
        int[] fb = new int[1];
        
        //generate fbo id
        GLES20.glGenFramebuffers(1, fb, 0);
        fboId = fb[0];   
        //Bind Frame buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId); 
    }
    
   
    
    private void createColorTexture(){
        //generate texture
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        colorTextureId = tex[0];
        //Bind texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, colorTextureId);
        
        //Define texture parameters
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);        
           
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mWidth, mHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //Attach texture FBO color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, colorTextureId, 0);

                
    }
    
    private void createDepthRenderBuffer(){
        //generate render buffer
        int[] rb = new int[1];
        GLES20.glGenRenderbuffers(1, rb, 0);
        depthRenderBufferId = rb[0];
        //Bind render buffer and define buffer dimension
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthRenderBufferId);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT24, mWidth, mHeight);
        //Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depthRenderBufferId);
                
    
    }     
    
    private void createStencilBuffer(){
        //generate render buffer
        int[] rb = new int[1];
        GLES20.glGenRenderbuffers(1, rb, 0);
        stencilRenderBufferId = rb[0];
        //Bind render buffer and define buffer dimension
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, stencilRenderBufferId);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES30.GL_DEPTH24_STENCIL8, mWidth, mHeight);
        //Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, stencilRenderBufferId);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_STENCIL_ATTACHMENT, GLES20.GL_RENDERBUFFER, stencilRenderBufferId);
    }    
    
    private void createDepthTexture(){
        //generate texture
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        depthTextureId = tex[0];
        //Bind texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTextureId);

        //Define texture parameters
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Remove artifact on the edges of the depthmap
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );       
//        // Use a depth texture
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES30.GL_DEPTH_COMPONENT24, mWidth, mHeight, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_INT, null);

        // Attach the depth texture to FBO depth attachment point
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthTextureId, 0);        
            
    }
    
    public void bindBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glClearColor(clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA());
//        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
    
    public void unBindBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);    
    }
    
    public final int getId(){
        return fboId;
    }
    
    public final int getDepthRenderBufferId(){
        return depthRenderBufferId;
    }    
    
    public final int getColorTextureId(){
        return colorTextureId;
    }
    
    public final int getDepthTextureId(){
        return  depthTextureId;
    }    
    
}