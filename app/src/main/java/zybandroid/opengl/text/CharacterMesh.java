package zybandroid.opengl.text;

import zybandroid.opengl.blenderdatas.MeshData;

/**
 *
 * @author zybon
 * Created 2019.08.09. 10:13:18
 */
public class CharacterMesh {
    
    private final MeshData meshData;
    private final String name;
    
    private final float charWidth;
    private final float charHeight;
    
    public CharacterMesh(String name, MeshData meshData) {
        this.meshData = meshData;
        this.name = name;
        charWidth = meshData.getDimensions().getX();
        charHeight = meshData.getDimensions().getY();
    }

    public MeshData getMeshData() {
        return meshData;
    }

    public String getName() {
        return name;
    }

    public float getCharWidth() {
        return charWidth;
    }

    public float getCharHeight() {
        return charHeight;
    }
    
    
    
    

}
