package zybandroid.opengl.objects;

import zybandroid.opengl.blenderdatas.MeshData;

/**
 *
 * @author zybon
 * Created 2020.02.25. 10:29:10
 */
public class MeshBuilder {
    
    public static TexturedMesh createTexturedMesh(MeshData meshdata){
        TexturedMesh tm = new TexturedMesh(meshdata);
        return tm;
    }
    
    public static TexturedMesh createIndexedMesh(MeshData meshdata){
        TexturedMesh tm = new TexturedMesh(meshdata);
        return tm;
    }    

}
