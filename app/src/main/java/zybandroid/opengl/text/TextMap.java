package zybandroid.opengl.text;

import android.content.Context;
import hu.zybon.zybsolitaire.names.MeshesNames;
import java.util.HashMap;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.blenderdatas.MeshDataCollector;
import zybandroid.opengl.blenderdatas.MeshDataCollectors;

/**
 *
 * @author zybon
 * Created 2019.08.09. 10:31:53
 */
public class TextMap {
    
    private final HashMap<String, CharacterMesh> text_map = new HashMap<String, CharacterMesh>();
    
    private final float spaceWidth;
    private final float lineHeight;
    private final float baseHeight;
    
    private static final char[][] CHARACTER_MAP = new char[][] {
        "ÁÉÍÓÖŐÚÜŰáéí".toCharArray(),
        "óöőúüűABCDEFG".toCharArray(),
        "HIJKLMNOPRS".toCharArray(),
        "TUVWXYZbdfhk".toCharArray(),
        "acemnorsuvwxz+".toCharArray(),
        "ilt!?/01234567".toCharArray(),
        "89*[]{}()%#&Q".toCharArray(),
        "gjpqy.,-:=<>".toCharArray()
    }
    ;

    public TextMap(Context context) {
        MeshDataCollector textDataCollector = 
                MeshDataCollectors.getCollector(MeshesNames.Text.collector_file_name);
        for (MeshData meshData : textDataCollector.getMeshDatas()) {
            if (meshData.getName().startsWith("size")){
                continue;
            }
            String[] nameA = meshData.getName().split("_");
            int rowIndex = Integer.parseInt(nameA[1]);
            int columnIndex = Integer.parseInt(nameA[2]);            
            CharacterMesh characterMesh = new CharacterMesh(""+CHARACTER_MAP[rowIndex][columnIndex], meshData);
            text_map.put(characterMesh.getName(), characterMesh);
        }     
        MeshData sizeLineAndSpaceMeshData = textDataCollector.getMeshData(MeshesNames.Text.size_line_and_space);
        spaceWidth = sizeLineAndSpaceMeshData.getDimensions().getX();
        lineHeight = sizeLineAndSpaceMeshData.getDimensions().getY();
        MeshData sizeBaseHeightMeshData = textDataCollector.getMeshData(MeshesNames.Text.size_baseheight);
        baseHeight = sizeBaseHeightMeshData.getDimensions().getY();
    }
    
    public final CharacterMesh getCharacterMesh(char character){
        CharacterMesh meshData = text_map.get(""+character);
        if (meshData == null) {
            throw new IllegalArgumentException("TextMap does not have CharacterMesh for '"+character+"'");
        }
        return meshData;
    }
    
    public final float getCharacterWidth(char character){
        CharacterMesh meshData = text_map.get(""+character);
        if (meshData == null) {
            throw new IllegalArgumentException("TextMap does not have CharacterMesh for '"+character+"'");
        }
        return meshData.getCharWidth();
    }    

    public float getSpaceWidth() {
        return spaceWidth;
    }    

    public float getLineHeight() {
        return lineHeight;
    }

    public float getBaseHeight() {
        return baseHeight;
    }
    
    

}
