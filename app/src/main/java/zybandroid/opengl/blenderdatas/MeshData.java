package zybandroid.opengl.blenderdatas;

import android.content.Context;
import java.util.ArrayList;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.Triangle;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.03.08. 12:47:48
 */
public class MeshData{
    
    private static final String TAG = "MeshData";
    
    private byte[] bytes;
    private int byteIndex;
    
    protected final MeshDataHeader meshDataHeader;
    
    private int[] triangles;
    private int vertexNumber;
    
    private float[] vertexData;   
    
    public MeshData(MeshDataHeader meshDataHeader) {
        this.meshDataHeader = meshDataHeader;
    }
    
//    public MeshData(MeshData.Scratch scratch) {
//        this.meshDataHeader = new MeshDataHeader();
//        this.meshDataHeader.loadFromScratch(scratch);
//        this.vertexData = scratch.vertexData;
//        this.vertexNumber = scratch.vertexNumber;
//    }    

    
    public int readTriangles(Context context, byte[] bytes, int startIndex){
        this.bytes = bytes;
        this.byteIndex = startIndex;

        readTriangles();
        
        createVertexData();
        return byteIndex;
    }
    
    private void readTriangles(){
        int triangleNumber = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;
        
        vertexNumber = triangleNumber*3;
//        Log.i(TAG, "triangleNumber = "+triangleNumber);
        int listLength;
        if (meshDataHeader.isIndexed()) {
            listLength = vertexNumber;
        }
        else {
            listLength = vertexNumber*(meshDataHeader.hasTextureCoords()?3:2);
        }
        
        triangles = DataReadHelper.readIntArray(byteIndex, bytes, listLength);
        byteIndex += listLength*BYTES_PER_INT;
    }   
    
    public void createVertexData(){
        if (meshDataHeader.hasTextureCoords()) {
            if (meshDataHeader.isIndexed()) {
                createIndexed_VertexData_textured();
            }
            else {
                createVertexData_textured();
            }            
        }
        else {
            if (meshDataHeader.isIndexed()) {
                createIndexedVertexData_colored();
            }
            else {
                createVertexData_colored();
            }            
            
        }
    }
    
    private void createVertexData_colored(){
        vertexData = new float[triangles.length*6];
        float[] vertexCoordList = meshDataHeader.getVertexCoordList();
        float[] normalVectorList = meshDataHeader.getNormalVectorList();
//        float[] weightList = meshDataHeader.getWeightList(); 
        int i = 0;
        int v;
        int n;
        for (int j = 0; j < triangles.length; j++) {
            v = triangles[j++]*3;
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v];

            n = triangles[j]*3;
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n]; 
            
        }     
    }
    
    private void createIndexedVertexData_colored(){
        int numberOfVertexCoord = meshDataHeader.getNumberOfVertexCoord();
        vertexData = new float[numberOfVertexCoord*6];
        float[] vertexCoordList = meshDataHeader.getVertexCoordList();
        float[] normalVectorList = meshDataHeader.getNormalVectorList();
//        float[] weightList = meshDataHeader.getWeightList();
        int i = 0;
        int v;
        int n;
        int w;
        for (int vertex_index = 0; vertex_index < numberOfVertexCoord; vertex_index++) {
            v = vertex_index*3;
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v];

            n = vertex_index*3;
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n]; 
            
//            w = vertex_index;
//            vertexData[i++] = weightList[w];              
        }     
    }    
        
    
    private void createVertexData_textured(){
        vertexData = new float[triangles.length*8];
        float[] vertexCoordList = meshDataHeader.getVertexCoordList();
        float[] normalVectorList = meshDataHeader.getNormalVectorList();        
        float[] textureCoordList = meshDataHeader.getTextureCoordList();   
//        float[] weightList = meshDataHeader.getWeightList(); 
        int i = 0;
        int v;
        int n;
        int t;
        for (int j = 0; j < triangles.length; j++) {
            
            v = triangles[j++]*3;
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v];

            n = triangles[j++]*3;
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n]; 
            
            t = triangles[j]*2;
            vertexData[i++] = textureCoordList[t++];
            vertexData[i++] = textureCoordList[t];
            

        }     
    }  
    
    public void createIndexed_VertexData_textured(){
        int numberOfVertex = meshDataHeader.getNumberOfVertexCoord();
        vertexData = new float[numberOfVertex*8];
        float[] vertexCoordList = meshDataHeader.getVertexCoordList();
        float[] normalVectorList = meshDataHeader.getNormalVectorList();        
        float[] textureCoordList = meshDataHeader.getTextureCoordList(); 
        int i = 0;
        int v;
        int n;
        int t;
        for(int j = 0; j < numberOfVertex; j++) {
            v = j*3;
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v++];
            vertexData[i++] = vertexCoordList[v];

            n = j*3;    
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n++];
            vertexData[i++] = normalVectorList[n]; 
            
            t = j*2; 
            vertexData[i++] = textureCoordList[t++];
            vertexData[i++] = textureCoordList[t];
            

        }     
    }      
    
    public float[] createIndexed_VertexData_texturedToFloatArray(){
        
        int numberOfVertex = meshDataHeader.getNumberOfVertexCoord();
        float[] _vertexData = new float[numberOfVertex*8];
        float[] vertexCoordList = meshDataHeader.getVertexCoordList();
        float[] normalVectorList = meshDataHeader.getNormalVectorList();        
        float[] textureCoordList = meshDataHeader.getTextureCoordList(); 
        int i = 0;
        int v;
        int n;
        int t;
        for(int j = 0; j < numberOfVertex; j++) {
            v = j*3;
            _vertexData[i++] = vertexCoordList[v++];
            _vertexData[i++] = vertexCoordList[v++];
            _vertexData[i++] = vertexCoordList[v];

            n = j*3;    
            _vertexData[i++] = normalVectorList[n++];
            _vertexData[i++] = normalVectorList[n++];
            _vertexData[i++] = normalVectorList[n]; 
            
            t = j*2; 
            _vertexData[i++] = textureCoordList[t++];
            _vertexData[i++] = textureCoordList[t];
            

        }     
        return _vertexData;
    }      
        
    
    public String getName() {
        return meshDataHeader.getName();
    }

    public float[] getVertexCoordList() {
        return meshDataHeader.getVertexCoordList();
    }

    public float[] getNormalVectorList() {
        return meshDataHeader.getNormalVectorList();
    }

//    public float[] getWeightList() {
//        return meshDataHeader.getWeightList();
//    }
    
    public float[] getTextureCoordList() {
        return meshDataHeader.getTextureCoordList();
    }

    public final int getStride() {
        return meshDataHeader.getStride();
    }

    public boolean hasTextureCoords() {
        return meshDataHeader.hasTextureCoords();
    }    

    public int getTextureResId() {
        return meshDataHeader.getMaterial().getTextureResId();
    }
    
    public int getTextureGlId() {
        return meshDataHeader.getMaterial().getTextureGlId();
    }      
    
    public Vector3D getTextureScale() {
        return meshDataHeader.getMaterial().getTextureScale();
    }     

    public String getTextureName() {
        return meshDataHeader.getMaterial().getTextureName();
    }
    
    public ZybColor getDiffuseColor() {
        return meshDataHeader.getMaterial().getDiffuseColor();
    }

    public boolean isIndexed() {
        return meshDataHeader.isIndexed();
    }
    
    public Vector3D getPosition() {
        return meshDataHeader.getPosition();
    }

    public float getRotationAngle() {
        return meshDataHeader.getRotationAngle();
    }

    public Vector3D getRotationAxis() {
        return meshDataHeader.getRotationAxis();
    }

    public Vector3D getScale() {
        return meshDataHeader.getScale();
    }

    public Vector3D getDimensions() {
        return meshDataHeader.getDimensions();
    }

    public int[] getTriangles() {
        return triangles;
    }
    
    public float[] getVertexData() {
        return vertexData;
    }

    public void clearVertexData(){
        vertexData = null;
    }

    public int getVertexNumber() {
        return vertexNumber;
    }
    
    public ArrayList<Triangle> readRealTriangles(){
        if (meshDataHeader.isIndexed()) {
            return readIndexedRealTriangles();
        }
        
        ArrayList<Triangle> realTriangles = new ArrayList<Triangle>(vertexNumber/3);
        int realTriangleIndex = 0;
        
        int[] trianglesData = getTriangles();
        float[] vertexCoordListData = getVertexCoordList();
        float[] normalVectorListData = getNormalVectorList();        
//        float[] weightListData = getWeightList();  
        int vertexIndex;
        int v;
        int n;
        
//        float weight;
//        int h = 0;
        int stride = 3;
        Vector3D tempVector = new Vector3D();
        for (int j = 0; j < trianglesData.length;) {

//            weight = 0.0f;
            Triangle triangle = new Triangle();
                //0. vertex
                vertexIndex = trianglesData[j];
                v = vertexIndex*3;
                triangle.setVertex0Index(vertexIndex);
                tempVector.set(
                        vertexCoordListData[v++],
                        vertexCoordListData[v++],
                        vertexCoordListData[v]
                );
                tempVector.translateWithVector(getPosition());
                triangle.setVertex0(tempVector);
                
                n = trianglesData[j+1]*3;
                tempVector.set(
                        normalVectorListData[n++],
                        normalVectorListData[n++],
                        normalVectorListData[n]
                );
                triangle.setNormalInVertex0(tempVector);
                
//                weight += weightListData[trianglesData[j+2]];
                
                //1. vertex
                vertexIndex = trianglesData[j+stride];
                v = vertexIndex*3;
                triangle.setVertex1Index(vertexIndex);

                tempVector.set(
                        vertexCoordListData[v++],
                        vertexCoordListData[v++],
                        vertexCoordListData[v]
                );
                tempVector.translateWithVector(getPosition());
                triangle.setVertex1(tempVector);
                
                n = trianglesData[j+1+stride]*3;
                tempVector.set(
                        normalVectorListData[n++],
                        normalVectorListData[n++],
                        normalVectorListData[n]
                );
                triangle.setNormalInVertex1(tempVector);
                
//                weight += weightListData[trianglesData[j+2+stride]];
                
                //2. vertex
                vertexIndex = trianglesData[j+stride*2];
                v = vertexIndex*3;
                triangle.setVertex2Index(vertexIndex);
                tempVector.set(
                        vertexCoordListData[v++],
                        vertexCoordListData[v++],
                        vertexCoordListData[v]
                );
                tempVector.translateWithVector(getPosition());
                triangle.setVertex2(tempVector);
                
                n = trianglesData[j+1+stride*2]*3;
                tempVector.set(
                        normalVectorListData[n++],
                        normalVectorListData[n++],
                        normalVectorListData[n]
                );
                triangle.setNormalInVertex2(tempVector);
                
//                weight += weightListData[trianglesData[j+2+stride*2]];
            triangle.initTriangleAndPlaneConstants();
            triangle.setMaterialIndex(meshDataHeader.getMaterial().getTextureResId());
            realTriangles.add(triangle);
//            Log.i(TAG, triangle.toString());
            j += stride*3;
        }   
        return realTriangles;
    }    
    
    private ArrayList<Triangle> readIndexedRealTriangles(){
        ArrayList<Triangle> realTriangles = new ArrayList<Triangle>(triangles.length/3);
        
        int[] trianglesData = getTriangles();
        float[] vertexCoordListData = getVertexCoordList();
        float[] normalVectorListData = getNormalVectorList();        
        int vertexIndex;
        int v;
        int n;
        
        int stride = 1;
        Vector3D tempVector = new Vector3D();
        for (int j = 0; j < trianglesData.length;) {

//            weight = 0.0f;
            Triangle triangle = new Triangle();
                //0. vertex
                vertexIndex = trianglesData[j];
                v = vertexIndex*3;
                triangle.setVertex0Index(vertexIndex);
                tempVector.set(
                    vertexCoordListData[v++],
                    vertexCoordListData[v++],
                    vertexCoordListData[v]
                );
                triangle.setVertex0(tempVector);
                
                n = trianglesData[j]*3;
                tempVector.set(
                        normalVectorListData[n++],
                        normalVectorListData[n++],
                        normalVectorListData[n]
                );
                triangle.setNormalInVertex0(tempVector);

                
//                weight += weightListData[trianglesData[j+2]];
                
                //1. vertex
                vertexIndex = trianglesData[j+stride];
                v = vertexIndex*3;
                triangle.setVertex1Index(vertexIndex);
                tempVector.set(
                        vertexCoordListData[v++],
                        vertexCoordListData[v++],
                        vertexCoordListData[v]
                );
                triangle.setVertex1(tempVector);
                
                n = trianglesData[j+stride]*3;
                tempVector.set(
                        normalVectorListData[n++],
                        normalVectorListData[n++],
                        normalVectorListData[n]
                );
                triangle.setNormalInVertex1(tempVector);

//                weight += weightListData[trianglesData[j+2+stride]];
                
                //2. vertex
                vertexIndex = trianglesData[j+stride*2];
                v = vertexIndex*3;
                triangle.setVertex2Index(vertexIndex);
                tempVector.set(
                        vertexCoordListData[v++],
                        vertexCoordListData[v++],
                        vertexCoordListData[v]
                );
                triangle.setVertex2(tempVector);
                
                n = trianglesData[j+stride*2]*3;
                tempVector.set(
                        normalVectorListData[n++],
                        normalVectorListData[n++],
                        normalVectorListData[n]
                );
                triangle.setNormalInVertex2(tempVector);

//                weight += weightListData[trianglesData[j+2+stride*2]];
            triangle.initTriangleAndPlaneConstants();
            triangle.setMaterialIndex(meshDataHeader.getMaterial().getTextureResId());
            realTriangles.add(triangle);
//            Log.i(TAG, triangle.toString());
            j += stride*3;
        }   
        return realTriangles;
    }    
        
    
//    public static class Scratch{
//        String name;
//
//        Vector3D position = new Vector3D();
//        float rotationAngle = 0;
//        Vector3D rotationAxis = new Vector3D();    
//        Vector3D scale = new Vector3D();
//
//        Vector3D dimensions = new Vector3D();
//
//        float[] color;
//        boolean hasUvTexture;
//        int textureResId;
//        String textureName;
//        
//        int stride;
//        
//        private int vertexNumber;
//        private float[] vertexData;
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setPosition(Vector3D position) {
//            this.position = position;
//        }
//
//        public void setRotationAngle(float rotationAngle) {
//            this.rotationAngle = rotationAngle;
//        }
//
//        public void setRotationAxis(Vector3D rotationAxis) {
//            this.rotationAxis = rotationAxis;
//        }
//
//        public void setScale(Vector3D scale) {
//            this.scale = scale;
//        }
//
//        public void setDimensions(Vector3D dimensions) {
//            this.dimensions = dimensions;
//        }
//
//        public void setColor(float[] color) {
//            this.color = color;
//        }
//
//        public void setHasUvTexture(boolean hasUvTexture) {
//            this.hasUvTexture = hasUvTexture;
//        }
//
//        public void setTextureResId(int textureResId) {
//            this.textureResId = textureResId;
//        }
//
//        public void setTextureName(String textureName) {
//            this.textureName = textureName;
//        }        
//
//        public void setStride(int stride) {
//            this.stride = stride;
//        }
//
//        public void setVertexData(float[] vertexData) {
//            this.vertexData = vertexData;
//        }
//
//        public void setVertexNumber(int vertexNumber) {
//            this.vertexNumber = vertexNumber;
//        }
//
//        
//        
//    }
//   
    
    

}
