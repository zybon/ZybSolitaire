package hu.zybon.zybsolitaire.sound;

import static android.content.Context.AUDIO_SERVICE;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.util.SparseArray;

import hu.zybon.zybsolitaire.MainActivity;

/**
 *
 * @author zybon
 * Created 2019.06.28. 10:45:09
 */
public class SoundPlayer {
    
    private final SoundPool soundPool;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;
    
    private final MainActivity mainActivity;

    private final SparseArray<Sound> sounds = new SparseArray<Sound>();
    
    public SoundPlayer(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) mainActivity.getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
 
        //Hardware buttons setting to adjust the media sound
        mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);     
        // the counter will help us recognize the stream id of the sound played  now
        counter = 0;
 
        // Load the sounds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool= createSoundPoolNew();
        } else {
            soundPool = createSoundPoolOld();
        }
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            
            @Override
            public void onLoadComplete(SoundPool soundPool, int playerId, final int status) {
                setSoundToLoaded(playerId);
            }


        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool createSoundPoolNew() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        return new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(10)
                .build();
    }

    @SuppressWarnings("deprecation")
    private SoundPool createSoundPoolOld(){
        return new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    }
    
    public Sound createSound(int resId){
        Sound sound = new Sound(soundPool, resId);
        sound.setDuration(readDuration(resId));
        int soundID = soundPool.load(mainActivity, sound.getResId(), 1);
        sound.setSoundID(soundID);
        sounds.append(soundID, sound);
        return sound;
    }
    
    private long readDuration(int resID){
        AssetFileDescriptor  assetsFileDescriptor = mainActivity.getResources().openRawResourceFd(resID);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        mediaMetadataRetriever.setDataSource(
                assetsFileDescriptor.getFileDescriptor(),
                assetsFileDescriptor.getStartOffset(),
                assetsFileDescriptor.getLength());

        String durationString = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationString);
    }
    
    private void setSoundToLoaded(int playerId) {
        Sound sound = sounds.get(playerId);
        sound.setLoaded(true);
    }    
 
    public void pauseAll(){
        soundPool.autoPause();
    }
    
    public void resumeAll(){
        soundPool.autoResume();
    }
    
    public void release(){
        for (int i = 0; i < sounds.size(); i++) {
            int key = sounds.keyAt(i);
            Sound sound = sounds.get(key);
            sound.stop();
        }
        soundPool.release();
    }
    
    
}
