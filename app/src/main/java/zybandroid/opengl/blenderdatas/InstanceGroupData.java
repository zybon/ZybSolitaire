package zybandroid.opengl.blenderdatas;

import android.content.Context;
import java.util.ArrayList;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2019.03.03. 14:19:05
 */
public class InstanceGroupData {

    private static final String TAG = "InstanceGroupData";
    
    private byte[] bytes;
    private int byteIndex;    
    
    private String name;
    
    private int maxDrawableCount;
    private float visiblityDistance;
    private float sectorCellSizeX;
    private float sectorCellSizeZ;
    
    private String baseMeshCollector;
    private String baseMeshName;
    private int instancesNumber;
    
    private final ArrayList<InstanceData> instancesDatas = new ArrayList<InstanceData>();

    public InstanceGroupData() {
    }
    
    int readData(Context context, byte[] bytes, int startIndex){
        this.bytes = bytes;
        this.byteIndex = startIndex;
        readName();
        readMaxDrawableCount();
        readVisibilityDistance();
        readSectorData();
        readBaseMeshData();        
        readInstancesNumber();
        readInstancesDatas();
        return byteIndex;
    }  
    
    private void readName() {
        int nameLength = (bytes[byteIndex]&0xff);
        byteIndex += 1;
        name = DataReadHelper.readString(byteIndex, bytes, nameLength);
        byteIndex += nameLength;           
    }   
    
    private void readMaxDrawableCount(){
        maxDrawableCount = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;  
    }     
    
    private void readVisibilityDistance(){
        visiblityDistance = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;  
    }      
    
    private void readSectorData(){
        sectorCellSizeX = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;  
        sectorCellSizeZ = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;  
    }    

    private void readBaseMeshData() {
        int nameLength = (bytes[byteIndex]&0xff);
        byteIndex += 1;
        baseMeshCollector = DataReadHelper.readString(byteIndex, bytes, nameLength);
        byteIndex += nameLength;   
        
        nameLength = (bytes[byteIndex]&0xff);
        byteIndex += 1;
        baseMeshName = DataReadHelper.readString(byteIndex, bytes, nameLength);
        byteIndex += nameLength;          
    }

    private void readInstancesNumber() {
        instancesNumber = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;    
        instancesDatas.ensureCapacity(instancesNumber);
    }    

    private void readInstancesDatas() {
        for (int i = 0; i < instancesNumber; i++) {
            InstanceData instanceData = new InstanceData();
            instanceData.position.set(readPosition());
            instanceData.rotationAngle = readRotationAngle();
            instanceData.rotationAxis.set(readRotationAxis());
            instanceData.scale = readScale();
            instanceData.modelMatrix = readModelMatrix();
            instancesDatas.add(instanceData);
        }
    }
    
    private Vector3D readPosition(){
        Vector3D position = new Vector3D(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT; 
//        Log.i(TAG, "position: "+position);
        return position;
    }
    
    private float readRotationAngle(){
        float rotationAngle = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;
//        Log.i(TAG, "rotationAngle: "+rotationAngle);
        return rotationAngle;
    }
    
    private Vector3D readRotationAxis(){
        Vector3D rotationAxis = new Vector3D(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT;  
//        Log.i(TAG, "rotationAxis: "+rotationAxis);
        return rotationAxis;
    }
    
    private float readScale(){
        float scale = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;  
//        Log.i(TAG, "scale: "+scale);
        return scale;
    }  
    
    private float[] readModelMatrix(){
        float[] modelMatrix = DataReadHelper.readFloatArray(byteIndex, bytes, 16);
        byteIndex += 16*BYTES_PER_INT;  
//        Log.i(TAG, "scale: "+scale);
        return modelMatrix;
    } 

    public String getName() {
        return name;
    }

    public int getMaxDrawableCount() {
        return maxDrawableCount;
    }
    
    public float getVisiblityDistance() {
        return visiblityDistance;
    }

    public float getSectorCellSizeX() {
        return sectorCellSizeX;
    }

    public float getSectorCellSizeZ() {
        return sectorCellSizeZ;
    }
    
    public String getBaseMeshCollector() {
        return baseMeshCollector;
    }
    
    public String getBaseMeshName() {
        return baseMeshName;
    }

    public int getInstancesNumber() {
        return instancesNumber;
    }
    
    public ArrayList<InstanceData> getInstancesDatas() {
        return instancesDatas;
    }

}
