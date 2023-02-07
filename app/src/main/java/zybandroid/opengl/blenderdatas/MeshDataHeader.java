package zybandroid.opengl.blenderdatas;

import android.content.Context;
import android.util.Log;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.Vector3D;

/**
 *
 * @author zybon
 * Created 2018.08.19. 17:21:49
 */
public class MeshDataHeader {
    
    private static final String TAG = "MeshDataHeader";
    
    private byte[] bytes;
    private int byteIndex;
    
    private String name;
    
    private final Vector3D position = new Vector3D();
    private float rotationAngle = 0;
    private final Vector3D rotationAxis = new Vector3D();    
    private final Vector3D scale = new Vector3D();
    
    private final Vector3D dimensions = new Vector3D();

    private Material material;
    
    private int stride;

    private int numberOfVertexCoord;
    private float[] vertexCoordList;
    private float[] normalVectorList; 
    private boolean hasTextureCoords;    
    private float[] textureCoordList;   
//    private float[] weightList;  
    
    
    private boolean indexed;
    
    private boolean sectored;

    
    
    
    public int readData(Context context, byte[] bytes, int startIndex){
        this.bytes = bytes;
        this.byteIndex = startIndex;
        
        readName();
        
        readPosition();
        readRotation();
        readScale();
        readDimensions();
        
        readMaterial(context);
        
        readIsIndexed();
        readVertexList();
        readNormalList();
//        readWeightList();
        readTextureCoordList();
        
        readIsSectored();
        return byteIndex;
    }
    
    private void readName(){
        int nameLength = (bytes[byteIndex]&0xff);
        byteIndex += 1;
        name = DataReadHelper.readString(byteIndex, bytes, nameLength);
        byteIndex += nameLength;     
//        Log.i(TAG, "name: "+name);
        
    }
    
    private void readPosition(){
        position.set(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT; 
//        Log.i(TAG, "position: "+position);
    }
    
    private void readRotation(){
        rotationAngle = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;
//        Log.i(TAG, "rotationAngle: "+rotationAngle);
        
        rotationAxis.set(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT;  
//        Log.i(TAG, "rotationAxis: "+rotationAxis);
    }
    
    private void readScale(){
        scale.set(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT;  
//        Log.i(TAG, "scale: "+scale);
    }
    
    private void readDimensions(){
        dimensions.set(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT;    
//        Log.i(TAG, "dimensions: "+dimensions);
    }
    
    private void readMaterial(Context context){
        material = new Material();
        byteIndex = material.readData(context, bytes, byteIndex);
    }
    
//    private void readColor(){
//        float[] color = DataReadHelper.readFloatArray(byteIndex, bytes, 3);
//        byteIndex += 3*BYTES_PER_INT;   
////        Log.i(TAG, "color: "+color[0]+","+color[1]+","+color[2]);
//    } 
//    
//    private void readTexture(Context context){
//        hasUvTexture = (bytes[byteIndex] == 1);
//        byteIndex += 1;
////        Log.i(TAG, "hasUvTexture: "+hasUvTexture);
//        if (hasUvTexture) {
//            int nameLength = (bytes[byteIndex]&0xff);
//            byteIndex += 1;
//            textureName = DataReadHelper.readString(byteIndex, bytes, nameLength);
////            Log.i(TAG, "textureName: "+textureName);
//            textureResId = DataReadHelper.getResourceIdFromString(context, textureName);
//            if (textureResId == 0) {
//                throw new AssertionError("texture of '"+name+"' ("+textureName+") not founded");
//            }
//            textureGlId = TextureHelper.getGlTextureIdFromResID(context, name, textureResId);
////            Log.i(TAG, "textureResId: "+textureResId);
////            Log.i(TAG, "\n");
//            byteIndex += nameLength; 
//            stride = 9;
//        }
//        else {
//            stride = 7;
//        }
//    }   
//         
    

    private void readIsIndexed(){
        indexed = (bytes[byteIndex] == 1);
        byteIndex += 1;
//        Log.i(TAG, "indexed: "+indexed);
    }
    
    private void readVertexList(){
        numberOfVertexCoord = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;
        int listLength = 3*numberOfVertexCoord;
        vertexCoordList = DataReadHelper.readFloatArray(byteIndex, bytes, listLength);
        byteIndex += listLength*BYTES_PER_INT;
//        Log.i(TAG, "numberOfVertexCoord = "+numberOfVertexCoord);
        
    }
    
    private void readNormalList(){
        int normal_number_in_list = DataReadHelper.readInt(byteIndex, bytes);
        byteIndex += BYTES_PER_INT;
        int listLength = 3*normal_number_in_list;
        normalVectorList = DataReadHelper.readFloatArray(byteIndex, bytes, listLength);
        byteIndex += listLength*BYTES_PER_INT;
//        Log.i(TAG, "normal_number_in_list = "+normal_number_in_list);
    }  
    
//    private void readWeightList(){
//        int weight_in_list = DataReadHelper.readInt(byteIndex, bytes);
//        int listLength = weight_in_list;
//        byteIndex += BYTES_PER_INT;
//        weightList = DataReadHelper.readFloatArray(byteIndex, bytes, listLength);
//        byteIndex += listLength*BYTES_PER_INT;
//    }     
    
    private void readTextureCoordList(){
        hasTextureCoords = (bytes[byteIndex] == 1);
        byteIndex += 1;     
//        Log.i(TAG, "hasTextureCoords = "+hasTextureCoords);
        if (hasTextureCoords) {
            int texcoord_in_list = DataReadHelper.readInt(byteIndex, bytes);
            byteIndex += BYTES_PER_INT;
//            Log.i(TAG, "texcoord_in_list = "+texcoord_in_list);
            int listLength = 2*texcoord_in_list;
            
            textureCoordList = DataReadHelper.readFloatArray(byteIndex, bytes, listLength);
            byteIndex += listLength*BYTES_PER_INT;
            stride = 8;
        }
        else {
            stride = 6;
        }
    }   
    
    private void readIsSectored(){
        sectored = (bytes[byteIndex] == 1);
        byteIndex += 1;
//        Log.i(TAG, "indexed: "+indexed);
    }    
        
    public String getName() {
        return name;
    }    

    public int getNumberOfVertexCoord() {
        return numberOfVertexCoord;
    }
    
    public float[] getVertexCoordList() {
        return vertexCoordList;
    }

    public float[] getNormalVectorList() {
        return normalVectorList;
    }

//    public float[] getWeightList() {
//        return weightList;
//    }
    
    public float[] getTextureCoordList() {
        return textureCoordList;
    }   
    
    public int getStride() {
        return stride;
    }
    
    public boolean hasTextureCoords() {
        return hasTextureCoords;
    }

    public Material getMaterial() {
        return material;
    }
    
    
    
//    public int getTextureResId() {
//        return textureResId;
//    }   
//
//    public int getTextureGlId() {
//        return textureGlId;
//    }
    
//    public String getTextureName() {
//        return textureName;
//    }
    
    void setIndexed(boolean indexed){
        this.indexed = indexed;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public boolean isSectored() {
        return sectored;
    }
    
    public Vector3D getPosition() {
        return position;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    public Vector3D getRotationAxis() {
        return rotationAxis;
    }

    public Vector3D getScale() {
        return scale;
    }

    public Vector3D getDimensions() {
        return dimensions;
    }

//    public float[] getColor() {
//        return color;
//    }


//    
//    void loadFromScratch(MeshData.Scratch scratch){
//        this.name = scratch.name;
//
//        this.position.set(scratch.position);
//        this.rotationAngle = scratch.rotationAngle;
//        this.rotationAxis.set(scratch.rotationAxis);    
//        this.scale.set(scratch.scale);
//
//        this.dimensions.set(scratch.dimensions);
//
//        this.color = scratch.color;
//        this.hasUvTexture = scratch.hasUvTexture;
//        this.textureResId = scratch.textureResId;
//        this.textureName = scratch.textureName;        
//
//        this.stride = scratch.stride;
//
//        
//    }
//        
}
