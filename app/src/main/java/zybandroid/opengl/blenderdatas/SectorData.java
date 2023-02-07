package zybandroid.opengl.blenderdatas;

/**
 *
 * @author zybon
 * Created 2018.08.19. 18:01:14
 */
public class SectorData extends MeshData{
    
    private final int index;

    public SectorData(MeshDataHeader meshDataHeader, int index) {
        super(meshDataHeader);
        this.index = index;
    }

    public int getSectorIndex() {
        return index;
    }
    
    

}
