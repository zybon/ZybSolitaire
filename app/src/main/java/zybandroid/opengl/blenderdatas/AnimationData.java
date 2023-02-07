package zybandroid.opengl.blenderdatas;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.RawFileReader;

/**
 *
 * @author zybon
 * Created 2019.02.14. 22:02:12
 */
public class AnimationData {
    
    private static final String TAG = "AnimationData";
    
    private byte[] bytes;
    private int byteIndex; 
    
    private int verticesCount;
    private int frameStart;
    private int frameEnd;
    private int frameStep;
    
    private final ArrayList<Frame> frames = new ArrayList<Frame>();
    
    private AnimationData() {
    }    
    
    public static final AnimationData readFromResources(Context context, int dataFileResID) {
        AnimationData ad = new AnimationData();
        ad.readBinData(RawFileReader.readRawFileFromResource(context, dataFileResID));
        return ad;
    }  
    
    public static final AnimationData readFromAssets(Context context, String assetDataFieName) {
        AnimationData ad = new AnimationData();
        ad.readBinData(RawFileReader.readRawFileFromAsset(context, assetDataFieName));
        return ad;
    } 
    
    public static final AnimationData readFromFile(Context context, File dataFile) {
        AnimationData ad = new AnimationData();
        ad.readBinData(RawFileReader.readRawFileFromFile(dataFile));
        return ad;
    }     
    
    private void readBinData(byte[] bytes){
        this.bytes = bytes;
        this.byteIndex = 0;
        readVerticesCount();
        readFrameInfos();
        

        readFrames();
    }  
    
    private void readVerticesCount(){
        verticesCount = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;        
    }

    private void readFrameInfos() {
        frameStart = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;       
        frameEnd = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;  
        frameStep = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;          
    }
    
    private void readFrames(){
        for (int frameIndex = frameStart; frameIndex <= frameEnd; frameIndex += frameStep) {
            readFrame();
        }        
    }

    private void readFrame() {
        int frameindex = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT; 
        Frame frame = new Frame(frameindex, verticesCount);
        for (int i = 0; i < verticesCount; i++) {
            frame.addVector(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
            byteIndex += 3*BYTES_PER_INT; 
        }
        frames.add(frame);
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public int getFrameStart() {
        return frameStart;
    }

    public int getFrameEnd() {
        return frameEnd;
    }

    public int getFrameStep() {
        return frameStep;
    }
    
    public float[] getCurrentFrameVectors(int frameIndex){
        return frames.get(frameIndex).vectorsFloatArray;
    }
    
    public static class Frame{
        private final int index;
        private final ArrayList<Vector3D> vectors = new ArrayList<Vector3D>();
        private final float[] vectorsFloatArray;
        private int offset = 0;

        public Frame(int index, int verticesCount) {
            this.index = index;
            this.vectorsFloatArray = new float[verticesCount*3];
        }
        
        public void addVector(float[] vectorInFloatArray){
            vectors.add(new Vector3D(vectorInFloatArray));
            vectorsFloatArray[offset++] = vectorInFloatArray[0];
            vectorsFloatArray[offset++] = vectorInFloatArray[1];
            vectorsFloatArray[offset++] = vectorInFloatArray[2];
        }

        public int getIndex() {
            return index;
        }

        public Vector3D getVector(int index) {
            return vectors.get(index);
        }
        
        
        
    }
    
}
