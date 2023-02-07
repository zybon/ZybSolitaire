package zybandroid.opengl.blenderdatas;

import android.content.Context;
import android.util.Log;
import java.io.File;
import zybandroid.opengl.util.RawFileReader;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.XZ_Border;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.01.13. 11:31:50
 */
public class SectorDataCollector {
    private static final String TAG = "SectorDataCollector";
    
    private byte[] bytes;
    private int byteIndex;   
    
    private final MeshDataHeader meshDataHeader = new MeshDataHeader();
    
    private final XZ_Border XZ_Border = new XZ_Border();
    private float columnSize;
    private float rowSize;
    private int columnNumber;
    private int rowNumber;    
    private SectorData[] sectors;
    
    private Context context;
    
    public static final SectorDataCollector readFromResources(Context c, int dataFileResId) {
        SectorDataCollector sdc = new SectorDataCollector();
        sdc.context = c;
        sdc.bytes = RawFileReader.readRawFileFromResource(c, dataFileResId);
        sdc.readBinData();
        return sdc;
    }
    
    public static final SectorDataCollector readFromAssets(Context c, String assetFileName) {
        SectorDataCollector sdc = new SectorDataCollector();
        sdc.context = c;
        sdc.bytes = RawFileReader.readRawFileFromAsset(c, assetFileName);
        sdc.readBinData();
        return sdc;
    }  
    
    public static final SectorDataCollector readFromFile(Context c, File dataFile) {
        SectorDataCollector sdc = new SectorDataCollector();
        sdc.context = c;
        sdc.bytes = RawFileReader.readRawFileFromFile(dataFile);
        sdc.readBinData();
        return sdc;
    }     
    
    private SectorDataCollector(){
        
    }
    
    private void readBinData(){
        byteIndex = meshDataHeader.readData(context, bytes, byteIndex);   
        while (byteIndex<bytes.length) {
            readSectorHeaderData();
            readSectorDatas();
        }
        this.bytes = null;
    }
    
    private void readSectorHeaderData(){
        readRect();
        readColumnInfo();
        readRowInfo();
        sectors = new SectorData[columnNumber*rowNumber];
    }    
    
    private void readRect(){
        float minX = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;
        float minZ = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;
        float maxX = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;
        float maxZ = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;  
        XZ_Border.setXMin(minX);
        XZ_Border.setZMin(minZ);
        XZ_Border.setXMax(maxX);
        XZ_Border.setZMax(maxZ);
        Log.i(TAG, ""+XZ_Border);
    }
    
    private void readColumnInfo(){
        columnSize = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;   
//        Log.i(TAG, "columnSize = "+columnSize);
        columnNumber = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;  
//        Log.i(TAG, "columnNumber = "+columnNumber);
    }
    
    private void readRowInfo(){
        rowSize = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;  
//        Log.i(TAG, "rowSize = "+rowSize);
        rowNumber = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex+=BYTES_PER_INT;      
//        Log.i(TAG, "rowNumber = "+rowNumber);
    }    
    
    private void readSectorDatas(){
        SectorData sector;
        int sectorIndex;
        for (;;) {
            sectorIndex = DataReadHelper.readInt(byteIndex, bytes);
            byteIndex += BYTES_PER_INT;            
//            Log.i(TAG, "sectorIndex = "+sectorIndex);
            sector = new SectorData(meshDataHeader, sectorIndex);
            
            byteIndex = sector.readTriangles(context, bytes, byteIndex);
//            Log.i(TAG, "sectorIndex = "+sectorIndex);
//            Log.i(TAG, "vertex number = "+sector.getVertexNumber());
            sectors[sectorIndex] = sector;
            if (sectorIndex == (rowNumber*columnNumber-1)){
                break;
            }
        }        
    }
    
//    private void readSectorDatas(){
//        SectorData sector;
//        int sectorIndex;
//        for (; byteIndex < bytes.length;) {
//            sectorIndex = DataReadHelper.readInt(byteIndex, bytes);
//            byteIndex += BYTES_PER_INT;            
//            Log.i(TAG, "sectorIndex = "+sectorIndex);
//            sector = new SectorData(meshDataHeader, sectorIndex);
//            
//            byteIndex = sector.readData(context, bytes, byteIndex);
////            Log.i(TAG, "sectorIndex = "+sectorIndex);
////            Log.i(TAG, "vertex number = "+sector.getVertexNumber());
//            sectors[sectorIndex] = sector;
//        }
//    }    
    
    public Context getContext() {
        return context;
    }

    public SectorData[] getSectors() {
        return sectors;
    }
    
    public SectorData getSectorData(int index){
        return sectors[index];
    }

    public XZ_Border getXZ_Border() {
        return XZ_Border;
    }

    public float getColumnSize() {
        return columnSize;
    }

    public float getRowSize() {
        return rowSize;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getName() {
        return meshDataHeader.getName();
    }

    public int getTextureResId() {
        return meshDataHeader.getMaterial().getTextureResId();
    }
    
    public int getTextureGlId() {
        return meshDataHeader.getMaterial().getTextureGlId();
    }      

    public String getTextureName() {
        return meshDataHeader.getMaterial().getTextureName();
    }
    
    public ZybColor getDiffuseColor() {
        return meshDataHeader.getMaterial().getDiffuseColor();
    }
      

}
