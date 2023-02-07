//package zybandroid.opengl.objects;
//
//import android.opengl.GLES20;
//import java.util.ArrayList;
//import zybandroid.opengl.blenderdatas.MeshData;
//import zybandroid.opengl.camera.Camera;
//import zybandroid.opengl.geometry.Cell;
//import zybandroid.opengl.geometry.Triangle;
//import zybandroid.opengl.geometry.Triangle_XZ_Table;
//import zybandroid.opengl.geometry.Vector3D;
//import zybandroid.opengl.geometry.XZ_Border;
//import zybandroid.opengl.geometry.XZ_Table;
//import zybandroid.opengl.programs.TextureShaderProgram;
//import static zybandroid.opengl.util.Constants.BYTES_PER_FLOAT;
//import zybandroid.opengl.util.IndexBuffer;
//import zybandroid.opengl.util.VertexBuffer;
//import zybandroid.opengl.util.ZybMath;
//
///**
// *
// * @author zybon
// * Created 2018.02.16. 14:05:50
// */
//public class IndexedSectoredMeshTable{
//
//    private static final String TAG = "IndexedSectoredMeshTable";
//
//    private final VertexBuffer vertexBuffer;
//    private IndexBuffer[] indexBuffers;
//    private int[] vertexNumbersForIndexBuffer;
//    private final XZ_Table index_XZ_Table = new XZ_Table();
//    private final int stride;
//
//    public IndexedSectoredMeshTable(MeshData meshData, float sectorSizeX, float sectorSizeZ) {
//
////        this.index_XZ_Table.setXZ_Border(sectoredMeshData.getXZ_Border());
////        this.index_XZ_Table.setColumnSize(sectoredMeshData.getColumnSize());
//////        Log.i(TAG, "columnSize: "+columnSize);
////        this.index_XZ_Table.setRowSize(sectoredMeshData.getRowSize());
//////        Log.i(TAG, "rowSize: "+rowSize);
////        this.index_XZ_Table.setColumnCount(sectoredMeshData.getColumnNumber());
//////        Log.i(TAG, "columnNumber: "+columnNumber);
////        this.index_XZ_Table.setRowCount(sectoredMeshData.getRowNumber());
//////        Log.i(TAG, "rowNumber: "+rowNumber);
//
//        this.stride = meshData.getStride()*BYTES_PER_FLOAT;
////        float[] data = sectoredMeshData.createIndexed_VertexData_texturedToFloatArray();
//        this.vertexBuffer = new VertexBuffer(meshData.getVertexData());
////        meshData.clearVertexData();
//        createIndexArrays(meshData, sectorSizeX, sectorSizeZ);
////        data = null;
//    }
//
//    private void createIndexArrays(MeshData meshData, float sectorSizeX, float sectorSizeZ){
//        Triangle_XZ_Table table = new Triangle_XZ_Table();
//        table.addTriangles(meshData.readRealTriangles());
//        table.createTableWithoutOverlap(sectorSizeX, sectorSizeZ);
//
//
//
//        this.index_XZ_Table.set(table);
//        Cell[] cells = table.getCells();
//
//        this.indexBuffers = new IndexBuffer[cells.length];
//        this.vertexNumbersForIndexBuffer = new int[cells.length];
//        int I = 0;
//        for (Cell cell : cells) {
//            ArrayList<Triangle> realTris = cell.getTriangles();
//            int[] triangles = new int[realTris.size()*3];
//            int i = 0;
//            for (Triangle realTri : realTris) {
//                triangles[i++] = realTri.getVertex0Index();
//                triangles[i++] = realTri.getVertex1Index();
//                triangles[i++] = realTri.getVertex2Index();
//            }
//            indexBuffers[I] = new IndexBuffer(triangles);
//            vertexNumbersForIndexBuffer[I++] = i;
//        }
//
//    }
//
//    public float getTableSizeX(){
//        return index_XZ_Table.getSizeX();
//    }
//
//    public float getTableSizeZ(){
//        return index_XZ_Table.getSizeZ();
//    }
//
//    public void setAttibutePositionData(int attributeLocation) {
//        setVertexAttribPointer(0, attributeLocation, 3, stride);
//    }
//
//    public void setAttibuteNormalData(int attributeLocation) {
//        setVertexAttribPointer(3, attributeLocation, 3, stride);
//    }
//
//    public void setAttibuteTextureCoordData(int attributeLocation) {
//        setVertexAttribPointer(6, attributeLocation, 2, stride);
//    }
//
//    private int setVertexAttribPointer(int dataOffset,
//            int attributeLocation,
//            int componentCount,
//            int stride) {
//        vertexBuffer.setVertexAttribPointer(
//                dataOffset,
//                attributeLocation,
//                componentCount,
//                stride);
//        return dataOffset+componentCount;
//    }
//
//    public void initShaderProgram(TextureShaderProgram shaderProgram){
//        setAttibutePositionData(shaderProgram.getPositionAttributeLocation());
//        setAttibuteNormalData(shaderProgram.getNormalAttributeLocation());
//        setAttibuteTextureCoordData(shaderProgram.getTextureCoordinatesAttributeLocation());
//    }
//
//    public void drawNearSectors(Camera camera, int maxSectorOffset){
//        int cameraInColumn = index_XZ_Table.getColumnIndex(camera.getEYE().getX());
//        int cameraInRow = index_XZ_Table.getRowIndex(camera.getEYE().getZ());
//
//        int columnMin = ZybMath.clamp(cameraInColumn-maxSectorOffset, 0, index_XZ_Table.getColumnCount()-1);
//        int rowMin = ZybMath.clamp(cameraInRow-maxSectorOffset, 0, index_XZ_Table.getRowCount()-1);
//
//        int columnMax = ZybMath.clamp(cameraInColumn+maxSectorOffset, 0, index_XZ_Table.getColumnCount()-1);
//        int rowMax = ZybMath.clamp(cameraInRow+maxSectorOffset, 0, index_XZ_Table.getRowCount()-1);
//        int sectorIndex;
//        for (int row = rowMin; row <= rowMax; row++) {
//            for (int column = columnMin; column <= columnMax; column++) {
//                sectorIndex = row*index_XZ_Table.getColumnCount()+column;
//                drawWithIndexArrays(sectorIndex);
//            }
//        }
//    }
//
//    public void drawWithShaderProgram(TextureShaderProgram shaderProgram, XZ_Border XZ_border){
//        setAttibutePositionData(shaderProgram.getPositionAttributeLocation());
//        setAttibuteNormalData(shaderProgram.getNormalAttributeLocation());
//        setAttibuteTextureCoordData(shaderProgram.getTextureCoordinatesAttributeLocation());
//        drawSectorsIn(XZ_border);
//    }
//
//    public void drawSectorsIn(XZ_Border xz_border){
//        int columnMin = index_XZ_Table.getColumnIndex(xz_border.getXMin());
//        int rowMin = index_XZ_Table.getRowIndex(xz_border.getZMin());
//
//        int columnMax = index_XZ_Table.getColumnIndex(xz_border.getXMax());
//        int rowMax = index_XZ_Table.getRowIndex(xz_border.getZMax());
//        int sectorIndex;
//        for (int row = rowMin; row <= rowMax; row++) {
//            for (int column = columnMin; column <= columnMax; column++) {
//                sectorIndex = row*index_XZ_Table.getColumnCount()+column;
//                drawWithIndexArrays(sectorIndex);
//            }
//        }
//
//    }
//
//    public void loadToShaderProgram(TextureShaderProgram shaderProgram){
//        setAttibutePositionData(shaderProgram.getPositionAttributeLocation());
//        setAttibuteNormalData(shaderProgram.getNormalAttributeLocation());
//        setAttibuteTextureCoordData(shaderProgram.getTextureCoordinatesAttributeLocation());
//
//    }
//
//    public void drawSectorsInWithout(XZ_Border xz_border, Vector3D position){
//        int columnMin = index_XZ_Table.getColumnIndex(xz_border.getXMin());
//        int rowMin = index_XZ_Table.getRowIndex(xz_border.getZMin());
//
//        int columnMax = index_XZ_Table.getColumnIndex(xz_border.getXMax());
//        int rowMax = index_XZ_Table.getRowIndex(xz_border.getZMax());
//        int sectorIndex;
//
//        int skipColumnIndex = index_XZ_Table.getColumnIndex(position.getX());
//        int skipRowIndex = index_XZ_Table.getRowIndex(position.getZ());
//        for (int row = rowMin; row <= rowMax; row++) {
//
//            for (int column = columnMin; column <= columnMax; column++) {
//                if (row==(skipRowIndex-1) && column==(skipColumnIndex-1)){continue;}
//                if (row==(skipRowIndex-1) && column==(skipColumnIndex)){continue;}
//                if (row==(skipRowIndex-1) && column==(skipColumnIndex+1)){continue;}
//
//                if (row==(skipRowIndex) && column==(skipColumnIndex-1)){continue;}
//                if (row==(skipRowIndex) && column==(skipColumnIndex)){continue;}
//                if (row==(skipRowIndex) && column==(skipColumnIndex+1)){continue;}
//
//                if (row==(skipRowIndex+1) && column==(skipColumnIndex-1)){continue;}
//                if (row==(skipRowIndex+1) && column==(skipColumnIndex)){continue;}
//                if (row==(skipRowIndex+1) && column==(skipColumnIndex+1)){continue;}
//                sectorIndex = row*index_XZ_Table.getColumnCount()+column;
//                drawWithIndexArrays(sectorIndex);
//            }
//        }
//
//    }
//
//    private void drawWithIndexArrays(int indexOfIndexArrays){
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffers[indexOfIndexArrays].getBufferId());
//        GLES20.glDrawElements(
//                GLES20.GL_TRIANGLES,
//                vertexNumbersForIndexBuffer[indexOfIndexArrays],
//                GLES20.GL_UNSIGNED_INT,
//                0
//        );
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
//
//    }
//
//
//
//}