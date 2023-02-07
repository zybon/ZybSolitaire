package zybandroid.opengl.objects;

import android.opengl.GLES20;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import android.util.SparseArray;
import java.util.ArrayList;
import zybandroid.opengl.blenderdatas.SectorData;
import zybandroid.opengl.blenderdatas.SectorDataCollector;
import zybandroid.opengl.blenderdatas.SectoredMeshData;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.geometry.XZ_Border;
import zybandroid.opengl.geometry.XZ_Table;
import zybandroid.opengl.programs.ShaderProgram;
import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;
import zybandroid.opengl.util.VertexArray;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.08.19. 17:53:11
 */
public class SectoredMeshTable1{
    
    private static final String TAG = "SectoredMeshTable";
    
    private final XZ_Table XZ_Table = new XZ_Table();
    private final SparseArray<SectorMesh> sectors = new SparseArray<SectorMesh>();
    
    private final ArrayList<SectorMesh> visibleSectors = new ArrayList<SectorMesh>();
//    private final VisibilityHelper farHelper = new VisibilityHelper(500);
//    private final VisibilityHelper nearHelper = new VisibilityHelper(0);  
    private int[] drawedSectors;
    
    private final SectoredMeshData sectoredMeshData;
    
    public SectoredMeshTable1(SectoredMeshData sectoredMeshData) {
        this.sectoredMeshData = sectoredMeshData;
//        Log.i(TAG, "init kezd");
//        Log.i(TAG, "sectordata betoltve");
        this.XZ_Table.setXZ_Border(sectoredMeshData.getXZ_Border());
        this.XZ_Table.setCellSizeX(sectoredMeshData.getColumnSize());
//        Log.i(TAG, "columnSize: "+columnSize);
        this.XZ_Table.setCellSizeZ(sectoredMeshData.getRowSize());
//        Log.i(TAG, "rowSize: "+rowSize);
        this.XZ_Table.setColumnCount(sectoredMeshData.getColumnNumber());
//        Log.i(TAG, "columnNumber: "+columnNumber);
        this.XZ_Table.setRowCount(sectoredMeshData.getRowNumber());
//        Log.i(TAG, "rowNumber: "+rowNumber);
        
        for (SectorData sectorData : sectoredMeshData.getSectors()) {
            if (sectorData.getVertexNumber() == 0) {
                continue;
            }
            sectors.append(sectorData.getSectorIndex(),new SectorMesh(sectorData));
        }
    }
    
    public int getGlTextureId() {
        return sectoredMeshData.getTextureGlId();
    }

    public ZybColor getDiffuseColor() {
        return sectoredMeshData.getDiffuseColor();
    }

    public Vector3D getTextureScale() {
        return sectoredMeshData.getTextureScale();
    }
    
    public XZ_Table getXZ_Table() {
        return XZ_Table;
    }
    
//    public void setVisibilityFarDistance(float visibilityFarDistance) {
//        this.farHelper.setDistanceFromCamera(visibilityFarDistance);
//    }   
//    
//    public void setVisibilityNearDistance(float visibilityNearDistance) {
//        this.nearHelper.setDistanceFromCamera(visibilityNearDistance);
//    }    
    
//    private final Vector3D camToTargetXZ = new Vector3D();
//    public ArrayList<SectorMesh> getVisibleSectors(Camera camera){
//        camToTargetXZ.set(camera.getEyeTargetVector());
//        camToTargetXZ.setY(0);
//        camToTargetXZ.normalize();
////        nearHelper.init(camera);
////        farHelper.init(camera);
//        
//        visibleSectors.clear();
//        
//        drawedSectors = new int[XZ_Table.getCellCount()];
////        Log.i(TAG, "drawSectors");
//        int sectorIndex;
//        SectorMesh sector;
//        for (int s = farHelper.rowMin; s <= farHelper.rowMax; s++) {
//            for (int o = farHelper.columnMin; o <= farHelper.columnMax; o++) {
//                sectorIndex = s*XZ_Table.getColumnCount()+o; 
////                Log.i(TAG, "sectorIndex = "+sectorIndex);
//                if (nearHelper.isIndexInside(s,o)) {
//                    continue;
//                }
////                Log.i(TAG, "sectorIndex = "+sectorIndex);
//                sector = sectors.get(sectorIndex);
//                if (sector != null) {
//                    sector.show();
//                    visibleSectors.add(sector);
//                    drawedSectors[sectorIndex] = 1;
//                }
//            }
//        }  
//        for (int i = 0; i < drawedSectors.length; i++) {
//            if (drawedSectors[i]==0) {
//                sector = sectors.get(i);
//                if (sector != null) {
//                    sector.alpha = 0.0f;
////                    sector.hide();
////                    if (sector.alpha>0.0) {
////                        visibleSectors.add(sector);
////                    }                    
//                }
//
//            }
//        }        
//        
//        return visibleSectors;
//    }
    
    public void drawSectorsIn(XZ_Border xz_border, Locations locations){
        int columnMin = XZ_Table.getColumnIndex(xz_border.getXMin());
        int rowMin = XZ_Table.getRowIndex(xz_border.getZMin());

        int columnMax = XZ_Table.getColumnIndex(xz_border.getXMax());
        int rowMax = XZ_Table.getRowIndex(xz_border.getZMax());
        
        visibleSectors.clear();
        
        drawedSectors = new int[XZ_Table.getCellCount()];
//        Log.i(TAG, "drawSectors");
        int sectorIndex;
        SectorMesh sector;
        for (int row = rowMin; row <= rowMax; row++) {
            for (int column = columnMin; column <= columnMax; column++) {
                sectorIndex = row*XZ_Table.getColumnCount()+column; 
//                Log.i(TAG, "sectorIndex = "+sectorIndex);
                sector = sectors.get(sectorIndex);
                if (sector != null) {
                    sector.show();
                    sector.loadAlphaUniform(locations.alphaUniform);
                    sector.setAttibutePositionData(locations.positionAtrr);
                    sector.setAttibuteNormalData(locations.normalAttr);
                    sector.setAttibuteTextureCoordData(locations.textureAttr);
                    sector.drawWithTriangles();
                    drawedSectors[sectorIndex] = 1;
                }
            }
        }  
        for (int i = 0; i < drawedSectors.length; i++) {
            if (drawedSectors[i]==0) {
                sector = sectors.get(i);
                if (sector != null) {
                    sector.alpha = 0.0f;
//                    sector.hide();
//                    if (sector.alpha>0.0) {
//                        visibleSectors.add(sector);
//                    }                    
                }

            }
        }        
        
    }
    
    public static class Locations{
        public int alphaUniform;
        public int positionAtrr;
        public int normalAttr;
        public int textureAttr;
    
    }
        
    
    public ArrayList<SectorMesh> getSectorsIn(XZ_Border xz_border){
        int columnMin = XZ_Table.getColumnIndex(xz_border.getXMin());
        int rowMin = XZ_Table.getRowIndex(xz_border.getZMin());

        int columnMax = XZ_Table.getColumnIndex(xz_border.getXMax());
        int rowMax = XZ_Table.getRowIndex(xz_border.getZMax());
        
        visibleSectors.clear();
        
        drawedSectors = new int[XZ_Table.getCellCount()];
//        Log.i(TAG, "drawSectors");
        int sectorIndex;
        SectorMesh sector;
        for (int row = rowMin; row <= rowMax; row++) {
            for (int column = columnMin; column <= columnMax; column++) {
                sectorIndex = row*XZ_Table.getColumnCount()+column; 
//                Log.i(TAG, "sectorIndex = "+sectorIndex);
                sector = sectors.get(sectorIndex);
                if (sector != null) {
                    sector.show();
                    visibleSectors.add(sector);
                    drawedSectors[sectorIndex] = 1;
                }
            }
        }  
        for (int i = 0; i < drawedSectors.length; i++) {
            if (drawedSectors[i]==0) {
                sector = sectors.get(i);
                if (sector != null) {
                    sector.alpha = 0.0f;
//                    sector.hide();
//                    if (sector.alpha>0.0) {
//                        visibleSectors.add(sector);
//                    }                    
                }

            }
        }        
        
        return visibleSectors;
    }
    
    public ArrayList<SectorMesh> getSectorsIn(XZ_Border near_xz_border, XZ_Border far_xz_border){
        int farColumnMin = XZ_Table.getColumnIndex(far_xz_border.getXMin());
        int farRowMin = XZ_Table.getRowIndex(far_xz_border.getZMin());

        int farColumnMax = XZ_Table.getColumnIndex(far_xz_border.getXMax());
        int farRowMax = XZ_Table.getRowIndex(far_xz_border.getZMax()); 
        
        int nearColumnMin = XZ_Table.getColumnIndex(near_xz_border.getXMin());
        int nearRowMin = XZ_Table.getRowIndex(near_xz_border.getZMin());

        int nearColumnMax = XZ_Table.getColumnIndex(near_xz_border.getXMax());
        int nearRowMax = XZ_Table.getRowIndex(near_xz_border.getZMax());       
        
        visibleSectors.clear();
        
        drawedSectors = new int[XZ_Table.getCellCount()];
//        Log.i(TAG, "drawSectors");
        int sectorIndex;
        SectorMesh sector;
        for (int row = farRowMin; row <= farRowMax; row++) {
            for (int column = farColumnMin; column <= farColumnMax; column++) {
                
                if (nearColumnMin<=column && column<=nearColumnMax &&
                    nearRowMin<=row && row<=nearRowMax ) {
                    continue;
                }
                
                
                sectorIndex = row*XZ_Table.getColumnCount()+column; 
//                Log.i(TAG, "sectorIndex = "+sectorIndex);
                sector = sectors.get(sectorIndex);
                if (sector != null) {
                    sector.show();
                    visibleSectors.add(sector);
                    drawedSectors[sectorIndex] = 1;
                }
            }
        }  
        for (int i = 0; i < drawedSectors.length; i++) {
            if (drawedSectors[i]==0) {
                sector = sectors.get(i);
                if (sector != null) {
                    sector.alpha = 0.0f;
//                    sector.hide();
//                    if (sector.alpha>0.0) {
//                        visibleSectors.add(sector);
//                    }                    
                }

            }
        }        
        
        return visibleSectors;
    }
    
    public int getSectorNumber(){
        return XZ_Table.getCellCount();
    }
    
    
//    public SectorMesh getSectorMesh(int column, int row){
//        int key = row*columnNumber+column;
//        return sectors.get(key);
//    }
    
    public static class SectorMesh{
        
        private final VertexArray vertexArray;
        private final int vertexNumber;
        private final int stride;        
        
        private float alpha = 1.0f;
        private float maxAlpha = 1.0f;

        private SectorMesh(SectorData sectorData) {
            this.vertexArray =  new VertexArray(sectorData.getVertexData());
            this.stride = sectorData.getStride()*BYTES_PER_FLOAT;
            this.vertexNumber = sectorData.getVertexNumber();  
        }

        public void setMaxAlpha(float maxAlpha) {
            this.maxAlpha = maxAlpha;
        }
        
        public void loadAlphaUniform(int u_AlphaLocation){
            GLES20.glUniform1f(u_AlphaLocation, alpha);
        }        
        
        public void setAttibutePositionData(int attributeLocation) {
            vertexArray.setVertexAttribPointer(0, attributeLocation, 3, stride);
        }    

        public void setAttibuteNormalData(int attributeLocation) {
            vertexArray.setVertexAttribPointer(3, attributeLocation, 3, stride);
        }  

        public void setAttibuteTextureCoordData(int attributeLocation) {
            vertexArray.setVertexAttribPointer(6, attributeLocation, 2, stride);
        }       
        
        public final void drawWithPoints(){
            glDrawArrays(GL_POINTS, 0, vertexNumber);
        }  

        public final void drawWithLines(){
            glDrawArrays(GL_LINES, 0, vertexNumber);
        }      

        public final void drawWithTriangles(){
            glDrawArrays(GL_TRIANGLES, 0, vertexNumber);
        }        
        
        public void hide() {
            if (alpha>0.0) {
                alpha -= 0.04f;
                if (alpha<0.0f) {
                    alpha = 0.0f;
                }
            }
        }

        public void show(){
            if (alpha<maxAlpha) {
                alpha += 0.2f;
                if (alpha>maxAlpha) {
                    alpha = maxAlpha;
                }
            }
        }
    
    }
    
//    private class VisibilityHelper{
//        
//        private final Vector3D visibilityCenter = new Vector3D();
//        
//        private int columnMin;
//        private int rowMin;
//        private int columnMax;
//        private int rowMax;       
//        
//        private float distanceFromCamera;
//
//        public VisibilityHelper(float distanceFromCamera) {
//            this.distanceFromCamera = distanceFromCamera;
//        }
//        
//        public void setDistanceFromCamera(float distanceFromCamera) {
//            this.distanceFromCamera = distanceFromCamera;
//        }
//
//        private void init(Camera camera){
//            visibilityCenter.set(camera.getEYE());
//            visibilityCenter.translateInNormalisedVectorDirection(
//                    camToTargetXZ, 
//                    distanceFromCamera/2.0f);
//            columnMin = (int)((visibilityCenter.getX()-distanceFromCamera/2.0f-XZ_Table.getXMin())/XZ_Table.getColumnSize());
//            rowMin = (int)((visibilityCenter.getZ()-distanceFromCamera/2.0f-XZ_Table.getZMin())/XZ_Table.getRowSize());
//            columnMin = Math.max(0, columnMin);
//            rowMin = Math.max(0, rowMin);
//
//            columnMax = (int)((visibilityCenter.getX()+distanceFromCamera/2.0f-XZ_Table.getXMin())/XZ_Table.getColumnSize());
//            rowMax = (int)((visibilityCenter.getZ()+distanceFromCamera/2.0f-XZ_Table.getZMin())/XZ_Table.getRowSize());
//            columnMax = Math.min(XZ_Table.getColumnNumber()-1, columnMax);
//            rowMax = Math.min(XZ_Table.getRowNumber()-1, rowMax);         
//        }
//
//        private boolean isIndexInside(int row, int column) {
//            if (distanceFromCamera>0) {
//                if (row>=rowMin && column >= columnMin &&
//                    row<=rowMax && column <= columnMax) {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }    
//    
}
