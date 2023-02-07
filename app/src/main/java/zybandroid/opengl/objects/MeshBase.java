package zybandroid.opengl.objects;

import zybandroid.opengl.blenderdatas.MeshData;

/**
 *
 * @author Zybon
 * Created time: 2017.02.19 19:04:27
 */
public class MeshBase extends OpenGlObject{
    
    private static final String TAG = "MeshBase";
    
    private final MeshData meshData;
    
    public MeshBase(MeshData meshData) { 
        super(meshData.getName());
        this.meshData = meshData;
        setOrigLocation(meshData.getPosition());
    }  
    
    public MeshBase(MeshData meshData, OpenGlObject parent) { 
        super(meshData.getName());
        this.meshData = meshData;
        setOrigLocation(meshData.getPosition());
        setParent(parent);
    }     

    public MeshData getMeshData() {
        return meshData;
    }

    public void resetPositionAndAngle(){
        moveToOrigLocation();
        rotateToOrigAngle();
    }
    
    public void rotateToOrigAngle(){
        rotate_around_Axis(meshData.getRotationAngle(), meshData.getRotationAxis());
    }
    
}
