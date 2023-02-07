package hu.zybon.zybsolitaire.sound;

import android.media.SoundPool;

/**
 *
 * @author zybon
 * Created 2020.07.01. 11:56:17
 */
public class Sound {
    
    private final SoundPool soundPool;
    private final int resId;
    
    private boolean loaded = false;
    private int soundID;
    private float leftVolume = 1.0f;
    private float rightVolume = 1.0f;
    private int loop = 0;
    private float rate = 1.0f;
    private int priority;
    
    private int streamID;
    
    private long duration;
    private long playStartTime;
    
    private boolean playing = false;

    Sound(SoundPool soundPool, int resId) {
        this.soundPool = soundPool;
        this.resId = resId;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public int getResId() {
        return resId;
    }
    
    void setSoundID(int soundID) {
        this.soundID = soundID;
    }

    int getSoundID() {
        return soundID;
    }
    
    void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }
    
    public void setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        if (isPlaying()){
            soundPool.setVolume(streamID, leftVolume, rightVolume);
        }
    }

    public float getLeftVolume() {
        return leftVolume;
    }

    public float getRightVolume() {
        return rightVolume;
    }

    public void setLoop(boolean looping) {
        this.loop = looping?-1:0;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void play() {
        if (!isLoaded()) {return;}
        if (isPlaying()) {return;}
        streamID = soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
        playing = true;
        playStartTime = System.currentTimeMillis();
    }    

    public boolean isPlaying() {
        if (playing) {
            if (loop == -1){
                return true;
            }
            if (System.currentTimeMillis()-playStartTime<duration){
                return true;
            }
            
        }
        playing = false;
        return false;
    }
    
    public void stop() {
        soundPool.stop(streamID);
        playing = false;
    }      

    
    

}

