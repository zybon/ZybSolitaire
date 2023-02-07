package zybandroid.opengl.blenderdatas;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.Triangle;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.geometry.XZ_Border;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.01.13. 11:31:50
 */
public class SectoredMeshData extends MeshData{
    private static final String TAG = "SectoredMeshData";
    
    private byte[] bytes;
    private int byteIndex;   

    private final XZ_Border XZ_Border = new XZ_Border();
    private float columnSize;
    private float rowSize;
    private int columnNumber;
    private int rowNumber;    
    private SectorData[] sectors;
    
    private Context context;
    
    public SectoredMeshData(MeshDataHeader meshDataHeader) {
        super(meshDataHeader);
    }    
    
    public int readData(Context context, byte[] bytes, int startIndex){
        this.context = context;
        this.byteIndex = startIndex;
        this.bytes = bytes;
        readSectorHeaderData();
        if (isIndexed()){
            createIndexed_VertexData_textured();
        }
        readSectorDatas();
        return byteIndex;
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
        
        int sectorIndex;
        for (;;) {
            sectorIndex = DataReadHelper.readInt(byteIndex, bytes);
            byteIndex += BYTES_PER_INT;            
//            Log.i(TAG, "sectorIndex = "+sectorIndex);
            SectorData sector = new SectorData(meshDataHeader, sectorIndex);
            
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
    
    @Override
    public ArrayList<Triangle> readRealTriangles(){
        if (meshDataHeader.isIndexed()) {
            return readIndexedRealTriangles();
        }
        realTriangles.clear();
        for (SectorData sector : sectors) {
            realTriangles.addAll(readRealTrianglesFromSector(sector));
        }
        return realTriangles;
    }
    
    private ArrayList<Triangle> readRealTrianglesFromSector(SectorData sectorData){
        
        ArrayList<Triangle> realTriangles = new ArrayList<Triangle>(sectorData.getVertexNumber()/3);
        int realTriangleIndex = 0;
        
        int[] trianglesData = sectorData.getTriangles();
        float[] vertexCoordListData = sectorData.getVertexCoordList();
        float[] normalVectorListData = sectorData.getNormalVectorList();        
//        float[] weightListData = getWeightList();  
        int vertexIndex;
        int v;
        int n;
        
//        float weight;
//        int h = 0;
        int stride = 3;
        for (int j = 0; j < trianglesData.length;) {

//            weight = 0.0f;
            Triangle triangle = new Triangle();
                //0. vertex
                vertexIndex = trianglesData[j];
                v = vertexIndex*3;
                triangle.setVertex0Index(vertexIndex);
                triangle.setVertex0(new Vector3D(
                    vertexCoordListData[v++], 
                    vertexCoordListData[v++], 
                    vertexCoordListData[v]
                ));
                
                n = trianglesData[j+1]*3;
                triangle.setNormalInVertex0(new Vector3D(
                    normalVectorListData[n++], 
                    normalVectorListData[n++], 
                    normalVectorListData[n]
                ));                
                
//                weight += weightListData[trianglesData[j+2]];
                
                //1. vertex
                vertexIndex = trianglesData[j+stride];
                v = vertexIndex*3;
                triangle.setVertex1Index(vertexIndex);
                triangle.setVertex1(new Vector3D(
                        vertexCoordListData[v++], 
                        vertexCoordListData[v++], 
                        vertexCoordListData[v]
                ));
                
                n = trianglesData[j+1+stride]*3;
                triangle.setNormalInVertex1(new Vector3D(
                    normalVectorListData[n++], 
                    normalVectorListData[n++], 
                    normalVectorListData[n]
                ));                  
                
//                weight += weightListData[trianglesData[j+2+stride]];
                
                //2. vertex
                vertexIndex = trianglesData[j+stride*2];
                v = vertexIndex*3;
                triangle.setVertex2Index(vertexIndex);
                triangle.setVertex2(new Vector3D(
                        vertexCoordListData[v++], 
                        vertexCoordListData[v++], 
                        vertexCoordListData[v]
                ));   
                
                n = trianglesData[j+1+stride*2]*3;
                triangle.setNormalInVertex2(new Vector3D(
                    normalVectorListData[n++], 
                    normalVectorListData[n++], 
                    normalVectorListData[n]
                ));    
                
//                weight += weightListData[trianglesData[j+2+stride*2]];
            triangle.initTriangleAndPlaneConstants();
            triangle.setMaterialIndex(meshDataHeader.getMaterial().getTextureResId());
            realTriangles.add(triangle);
//            Log.i(TAG, triangle.toString());
            j += stride*3;
        }   
        return realTriangles;
    }    
    
    private final ArrayList<Triangle> realTriangles = new ArrayList<Triangle>();
    private ArrayList<Triangle> readIndexedRealTriangles(){
        realTriangles.clear();
        
        for (SectorData sector : sectors) {
            readIndexedRealTrianglesFromSector(sector);
        }
        return realTriangles;
    }
    
    private void readIndexedRealTrianglesFromSector(SectorData sector){
        int[] trianglesData = sector.getTriangles();
        float[] vertexCoordListData = getVertexCoordList();
        float[] normalVectorListData = getNormalVectorList();        
        int vertexIndex;
        int v;
        int n;
        
        int stride = 1;
        for (int j = 0; j < trianglesData.length;) {

//            weight = 0.0f;
            Triangle triangle = new Triangle();
                //0. vertex
                vertexIndex = trianglesData[j];
                v = vertexIndex*3;
                triangle.setVertex0Index(vertexIndex);
                triangle.setVertex0(new Vector3D(
                    vertexCoordListData[v++], 
                    vertexCoordListData[v++], 
                    vertexCoordListData[v]
                ));
                
                n = trianglesData[j]*3;
                triangle.setNormalInVertex0(new Vector3D(
                    normalVectorListData[n++], 
                    normalVectorListData[n++], 
                    normalVectorListData[n]
                ));                
                
//                weight += weightListData[trianglesData[j+2]];
                
                //1. vertex
                vertexIndex = trianglesData[j+stride];
                v = vertexIndex*3;
                triangle.setVertex1Index(vertexIndex);
                triangle.setVertex1(new Vector3D(
                        vertexCoordListData[v++], 
                        vertexCoordListData[v++], 
                        vertexCoordListData[v]
                ));
                
                n = trianglesData[j+stride]*3;
                triangle.setNormalInVertex1(new Vector3D(
                    normalVectorListData[n++], 
                    normalVectorListData[n++], 
                    normalVectorListData[n]
                ));                  
                
//                weight += weightListData[trianglesData[j+2+stride]];
                
                //2. vertex
                vertexIndex = trianglesData[j+stride*2];
                v = vertexIndex*3;
                triangle.setVertex2Index(vertexIndex);
                triangle.setVertex2(new Vector3D(
                        vertexCoordListData[v++], 
                        vertexCoordListData[v++], 
                        vertexCoordListData[v]
                ));   
                
                n = trianglesData[j+stride*2]*3;
                triangle.setNormalInVertex2(new Vector3D(
                    normalVectorListData[n++], 
                    normalVectorListData[n++], 
                    normalVectorListData[n]
                ));    
                
//                weight += weightListData[trianglesData[j+2+stride*2]];
            triangle.initTriangleAndPlaneConstants();
            triangle.setMaterialIndex(meshDataHeader.getMaterial().getTextureResId());
            realTriangles.add(triangle);
//            Log.i(TAG, triangle.toString());
            j += stride*3;
        }   
    }    
        
    
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

}
