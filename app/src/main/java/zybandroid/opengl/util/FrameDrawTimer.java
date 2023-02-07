package zybandroid.opengl.util;

/**
 *
 * @author zybon
 * Created 2018.09.14. 13:28:55
 */
public class FrameDrawTimer {
    
    
    private static float fps_count_time_start = 0;
    private static float currentTime = 0;
    private static float lastFrameTime = 0;
    private static float deltaTime = 0;
    private static int frameCounter = 0;
    private static float framePerSec = 60.0f;
    private static float inverseFramePerSec = 1f/60.0f;
    private static float inverseFramePerSecPer60 = 1.0f;
    
    public static float slowValue = 1.0f;
    
    public static final void reset(){
        frameCounter = 0;
        fps_count_time_start = System.nanoTime();
        currentTime = System.nanoTime();
    }
    
    public static final void calculateFPS(){
        lastFrameTime = currentTime;
        currentTime = System.nanoTime();
        deltaTime = currentTime-lastFrameTime;
        
        frameCounter++;
        if ((currentTime-fps_count_time_start)>=Constants.ONE_SEC_IN_NANOSEC) {
            framePerSec = frameCounter;
            inverseFramePerSec = 1.0f/framePerSec;
            inverseFramePerSecPer60 = 60.0f/framePerSec;
            frameCounter = 0;
            fps_count_time_start = currentTime;
        }          
    }

    public static int getFramePerSec() {
        return (int)framePerSec;
    }
    
//    public static float getInverseFramePerSecPer60() {
//        return inverseFramePerSecPer60;
//    }    
//    
//    public static float getInverseFramePerSec(){
//        return inverseFramePerSec;
//    }    
    
    public static float getDeltaTime() {
        return deltaTime;
    }
    
    public static float getDeltaTimeInSec() {
        return deltaTime/Constants.ONE_SEC_IN_NANOSEC;
    }    
    
    public static float getMovementInGamePerFrame(float speedInMeterPerSec){
        return speedInMeterPerSec*getDeltaTimeInSec();
    }
    
    public static float getSpeedChangePerFrame(float accelerationInMPSS){
        return accelerationInMPSS*getDeltaTimeInSec();
    }    
}
