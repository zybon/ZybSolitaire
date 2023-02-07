package zybandroid.opengl.blenderdatas;

import android.content.Context;
import java.io.File;
import zybandroid.opengl.util.RawFileReader;
import java.util.ArrayList;

/**
 *
 * @author zybon
 * Created 2018.01.13. 11:31:50
 */
public class InstanceGroupDataCollector {
    private static final String TAG = "InstanceDataCollector";
    
    private final ArrayList<InstanceGroupData> instanceGroupsDatas = new ArrayList<InstanceGroupData>();
    
    private Context context;
    
    public static final InstanceGroupDataCollector readFromResources(Context c, int dataFileResID) {
        InstanceGroupDataCollector idc = new InstanceGroupDataCollector();
        idc.context = c;
        idc.readBinData(RawFileReader.readRawFileFromResource(c, dataFileResID));
        return idc;
    }
    
    public static final InstanceGroupDataCollector readFromAsset(Context c, String assetDataFile) {
        InstanceGroupDataCollector idc = new InstanceGroupDataCollector();
        idc.context = c;
        idc.readBinData(RawFileReader.readRawFileFromAsset(c, assetDataFile));
        return idc;
    }     
    
    public static final InstanceGroupDataCollector readFromFile(Context c, File dataFile) {
        InstanceGroupDataCollector idc = new InstanceGroupDataCollector();
        idc.context = c;
        idc.readBinData(RawFileReader.readRawFileFromFile(dataFile));
        return idc;
    }  
    
    private InstanceGroupDataCollector(){
    
    }
    
    private void readBinData(byte[] bytes){
        InstanceGroupData instanceData;
        for (int byteIndex = 0; byteIndex < bytes.length;) {
            instanceData = new InstanceGroupData();
            byteIndex = instanceData.readData(context, bytes, byteIndex);
            instanceGroupsDatas.add(instanceData);          
        }
    }    

    public Context getContext() {
        return context;
    }
    
    public InstanceGroupData getInstanceGroupData(String groupDataName){
        for (InstanceGroupData data : instanceGroupsDatas) {
            if (data.getName().equals(groupDataName)) {
                return data;
            }
        }
        throw new IllegalArgumentException("Not found #"+groupDataName+"# data");
    }    

    public ArrayList<InstanceData> getInstanceDatas(String groupDataName){
        for (InstanceGroupData data : instanceGroupsDatas) {
            if (data.getName().equals(groupDataName)) {
                return data.getInstancesDatas();
            }
        }
        throw new IllegalArgumentException("Not found #"+groupDataName+"# data");
    }


}
