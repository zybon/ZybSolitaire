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
public class MeshDataCollector {
    private static final String TAG = "MeshDataCollector";
    
    private final ArrayList<MeshData> meshDatas = new ArrayList<MeshData>();
    private String collectorFileName = "MeshDataCollector";
    private Context context;
    
    public static final MeshDataCollector readFromResource(Context context, final int dataFileResID){
        MeshDataCollector mdc = new MeshDataCollector();
        mdc.context = context;
        mdc.readBinData(RawFileReader.readRawFileFromResource(context, dataFileResID));
        return mdc;
    }    
    
    static final MeshDataCollector readFromAsset(Context context, final String dataFileAsset){
        MeshDataCollector mdc = new MeshDataCollector();
        mdc.context = context;
        mdc.readBinData(RawFileReader.readRawFileFromAsset(context, dataFileAsset));
        return mdc;
    }
    
    public static final MeshDataCollector readFromFile(Context context, final File dataFile){
        MeshDataCollector mdc = new MeshDataCollector();
        mdc.context = context;
        mdc.readBinData(RawFileReader.readRawFileFromFile(dataFile));
        return mdc;
    }    
    
    private MeshDataCollector(){
    
    }    
    
    private void readBinData(byte[] bytes){
        MeshDataHeader meshDataHeader;
        MeshData meshData;
        SectoredMeshData sectoredMeshData;
        for (int byteIndex = 0; byteIndex < bytes.length;) {
            meshDataHeader = new MeshDataHeader();
            byteIndex = meshDataHeader.readData(context, bytes, byteIndex);
            
            if (meshDataHeader.isSectored()){
                sectoredMeshData = new SectoredMeshData(meshDataHeader);
                byteIndex = sectoredMeshData.readData(context, bytes, byteIndex);
                meshDatas.add(sectoredMeshData);                 
            }
            else {
                meshData = new MeshData(meshDataHeader);
                byteIndex = meshData.readTriangles(context, bytes, byteIndex);
                meshData.createVertexData();
                meshDatas.add(meshData);          
            }
        }
    }   

    public void setCollectorFileName(String collectorFileName) {
        this.collectorFileName = collectorFileName;
    }
    
    public Context getContext() {
        return context;
    }

    public ArrayList<MeshData> getMeshDatas() {
        return meshDatas;
    }

    public MeshData getMeshData(String name){
        for (MeshData data : meshDatas) {
            if (data.getName().equals(name)) {
                return data;
            }
        }
        throw new IllegalArgumentException("Not found #"+name+"# data in "+collectorFileName);
    }


}
