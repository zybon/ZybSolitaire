package zybandroid.opengl.geometry;

import android.util.SparseArray;

import hu.zybon.zybsolitaire.MainRenderer;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import zybandroid.opengl.blenderdatas.DataReadHelper;
import zybandroid.opengl.blenderdatas.InstanceData;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.blenderdatas.SectorData;
import zybandroid.opengl.blenderdatas.SectoredMeshData;
import zybandroid.opengl.util.Constants;
import zybandroid.opengl.util.RawFileReader;

/**
 *
 * @author zybon
 * Created 2019.02.26. 11:43:43
 */
public class Triangle_XZ_Table{
    
    private static final String TAG = "TriangleTable";
    
    private final ArrayList<Triangle> triangles = new ArrayList<Triangle>();
    

    private final XZ_Border XZ_Border = new XZ_Border();
    private float CELL_SIZE_X;
    private float CELL_SIZE_Z;
    private int COLUMN_NUMBER;
    private int ROW_NUMBER;

    private Cell[] cells;
    
    public static final float OUTSIDE_OF_TABLE_TRIANGLE_Y = -100.0f;
    public static final Triangle OUTSIDE_OF_TABLE_TRIANGLE = new Triangle();
    public static final TriangleInPosition OUTSIDE_TRIANGLE = new TriangleInPosition(OUTSIDE_OF_TABLE_TRIANGLE_Y, OUTSIDE_OF_TABLE_TRIANGLE);
    
    static {
        OUTSIDE_OF_TABLE_TRIANGLE.setVertices(
                new Vector3D(0, OUTSIDE_OF_TABLE_TRIANGLE_Y, -1), 
                new Vector3D(0, OUTSIDE_OF_TABLE_TRIANGLE_Y, 0), 
                new Vector3D(1, OUTSIDE_OF_TABLE_TRIANGLE_Y, 0));
        OUTSIDE_OF_TABLE_TRIANGLE.initTriangleAndPlaneConstants();
    }
    
    private int keresesSzam;

    private final ArrayList<String> solidMaps = new ArrayList<>();

    private byte[] solidMapBytes;
    private int sectorHeaderStartIndex = 24;
    private int sectorHeaderLength;
    private int sectorsStartIndex;

    private int index_size_in_bytes = 1*Constants.BYTES_PER_INT;
    private int vector_size_in_bytes = 3*Constants.BYTES_PER_INT;
    private int vertices_coords_size_in_bytes = 3*vector_size_in_bytes;

    private int normal_size_in_bytes = vector_size_in_bytes;

    private int vertices_colors_size_in_bytes = 3*vector_size_in_bytes;

    private final int triangle_size_in_bytes = index_size_in_bytes +
                                                vertices_coords_size_in_bytes +
                                                normal_size_in_bytes +
                                                vertices_colors_size_in_bytes;

    private final SparseArray<ArrayList<Triangle>> sectors = new SparseArray<>();

    private int triangleZoneIndex = 0;

    public Triangle_XZ_Table() {

    }

    public void addSolidMap(String solidMapName){
        this.solidMapBytes = RawFileReader.readRawFileFromAsset(MainRenderer.context, solidMapName);

        XZ_Border.setXMin(DataReadHelper.readFloat(0, solidMapBytes));
        XZ_Border.setXMax(DataReadHelper.readFloat(4, solidMapBytes));
        XZ_Border.setZMin(DataReadHelper.readFloat(8, solidMapBytes));
        XZ_Border.setZMax(DataReadHelper.readFloat(12, solidMapBytes));
        CELL_SIZE_X = DataReadHelper.readFloat(16, solidMapBytes);
        CELL_SIZE_Z = DataReadHelper.readFloat(20, solidMapBytes);
        COLUMN_NUMBER = (int)(XZ_Border.getSizeX()/CELL_SIZE_X);
        ROW_NUMBER = (int)(XZ_Border.getSizeZ()/CELL_SIZE_Z);
        sectorHeaderLength = ROW_NUMBER*COLUMN_NUMBER * Constants.BYTES_PER_INT;
    }
    
    public void addTriangle(Triangle triangle){
        this.triangles.add(triangle);
    }    
    
    public void addTriangles(ArrayList<Triangle> triangles){
        this.triangles.addAll(triangles);
        
    }

    public int getTrianglesSize() {
        return triangles.size();
    }
    
    
    
    public void loadTrianglesFromSectoredMesh(SectoredMeshData sectoredMeshData){
        for (SectorData sectorData : sectoredMeshData.getSectors()) {
            if (sectorData.getVertexNumber() == 0) {
                continue;
            }
            loadTrianglesFromMesh(sectorData);
        }        
    }
    
    public void loadTrianglesFromMesh(MeshData data){
        addTriangles(data.readRealTriangles());
        
    }
    
    public final void loadTrianglesFromPartsicleSystem(MeshData baseMeshData, ArrayList<InstanceData> particlesDatas){
        ArrayList<Triangle> baseMeshTriangles = baseMeshData.readRealTriangles();
        for (InstanceData particleData : particlesDatas) {

            Vector3D positionTranslete = particleData.getPosition();
            float rotationAroundY = particleData.getRotationAngle();
            float sinA = (float)Math.sin(Math.toRadians(rotationAroundY));
            float cosA = (float)Math.cos(Math.toRadians(rotationAroundY));
            float scale = particleData.getScale();          
            for (Triangle baseMeshTriangle : baseMeshTriangles) {
                Triangle triangle = new Triangle();
                triangle.copyPlaneData(baseMeshTriangle);
                triangle.scale(scale);              
                triangle.rotateAroundY(sinA, cosA);
                triangle.translateWithVector(positionTranslete);
                triangle.initTriangleAndPlaneConstants();
                addTriangle(triangle);
            }   
        }
    }    
    

    
    public final void createTableWithoutOverlap(float cellSize_X, float cellSize_Z){
        if (triangles.isEmpty()) {return;}
        init_XZ_Border();
        initCells(cellSize_X, cellSize_Z);
        loadTrianglesToCellsWithoutOverlap();
    }    
    
    public final void createTable(float cellSize_X, float cellSize_Z){
        if (triangles.isEmpty()) {return;}
        init_XZ_Border();
        initCells(cellSize_X, cellSize_Z);
        loadTrianglesToCells();
    }
    
    private void init_XZ_Border(){
        for (Triangle triangle : triangles) {
            XZ_Border.setWithCompare(triangle.getXZ_Border());
        }
    }

    private Triangle readTriangleFromByteArray(int startIndex){
        Triangle triangle = new Triangle();
        triangle.setIndex(DataReadHelper.readInt(startIndex, solidMapBytes));
        startIndex += 1*Constants.BYTES_PER_INT;
        triangle.setVertex(0, DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;
        triangle.setVertex(1, DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;
        triangle.setVertex(2, DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;

//        triangle.setNormal(DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;

        triangle.setVertexColor(0, DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;
        triangle.setVertexColor(1, DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;
        triangle.setVertexColor(2, DataReadHelper.readFloatArray(startIndex, solidMapBytes, 3));
        startIndex += 3*Constants.BYTES_PER_FLOAT;

        triangle.calcTriangleAndPlaneConstants();
        triangle.setZoneIndex(triangleZoneIndex);
        return triangle;
    }

    private ArrayList<Triangle> readSector(int sectorKey){
        ArrayList<Triangle> trianglesInSector = new ArrayList<>();
        int sectorIndexInHeader = sectorHeaderStartIndex + sectorKey*Constants.BYTES_PER_INT;
        int sectorStartIndexInByteArray = DataReadHelper.readInt(sectorIndexInHeader, solidMapBytes);
        int trianglesNumberInSector = DataReadHelper.readInt(sectorStartIndexInByteArray, solidMapBytes);
        if (trianglesNumberInSector == 0){
            trianglesInSector.add(OUTSIDE_OF_TABLE_TRIANGLE);
            return trianglesInSector;
        }
        trianglesInSector.ensureCapacity(trianglesNumberInSector);
        int startIndex = sectorStartIndexInByteArray+Constants.BYTES_PER_INT;
        for (int i = 0; i < trianglesNumberInSector; i++) {
            trianglesInSector.add(readTriangleFromByteArray(startIndex));
            startIndex += triangle_size_in_bytes;
        }


        return trianglesInSector;
    }
    
    private void initCells(float cellSize_X, float cellSize_Z){
        CELL_SIZE_X = cellSize_X;
        CELL_SIZE_Z = cellSize_Z;  
        COLUMN_NUMBER = ((int)(XZ_Border.getSizeX()/CELL_SIZE_X))+1;
//        Log.i(TAG, "COLUMN_NUMBER = "+COLUMN_NUMBER);
        ROW_NUMBER = ((int)(XZ_Border.getSizeZ()/CELL_SIZE_Z))+1;
//        Log.i(TAG, "ROW_NUMBER = "+ROW_NUMBER);
        cells = new Cell[COLUMN_NUMBER*ROW_NUMBER];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell();
        }        
    }
    
    public static int triangleDb = 0;
    private void loadTrianglesToCells(){
        int i;
        int sMin;
        int oMin;
        int sMax;
        int oMax;
        triangleDb = 0;
        for (Triangle triangle : triangles) {
            if (triangle == null) {continue;}
            XZ_Border triangle_XZ_Border = triangle.getXZ_Border();
//            Log.i(TAG, triangle.toString());
            oMin = (int)((triangle_XZ_Border.getXMin()-XZ_Border.getXMin())/CELL_SIZE_X);
            sMin = (int)((triangle_XZ_Border.getZMin()-XZ_Border.getZMin())/CELL_SIZE_Z);
//            Log.i(TAG, "oMin: "+oMin);
//            Log.i(TAG, "sMin: "+sMin);
            oMax = (int)((triangle_XZ_Border.getXMax()-XZ_Border.getXMin())/CELL_SIZE_X);
            sMax = (int)((triangle_XZ_Border.getZMax()-XZ_Border.getZMin())/CELL_SIZE_Z);
//            Log.i(TAG, "oMax: "+oMax);
//            Log.i(TAG, "sMax: "+sMax);            
            for (int s = sMin; s <= sMax; s++) {
                for (int o = oMin; o <= oMax; o++) {
                    i = s*COLUMN_NUMBER+o;        
                    cells[i].addTriangle(triangle);
                    triangleDb++;
                }
            }
            
        }
    }
    
    private void loadTrianglesToCellsWithoutOverlap(){
        int i;
        int xMin;
        int zMin;
        int xMax;
        int zMax;
        for (Triangle triangle : triangles) {
            if (triangle == null) {continue;}
            XZ_Border triangle_XZ_Border = triangle.getXZ_Border();
//            Log.i(TAG, triangle.toString());
            xMin = (int)((triangle_XZ_Border.getXMin()-XZ_Border.getXMin())/CELL_SIZE_X);
            zMin = (int)((triangle_XZ_Border.getZMin()-XZ_Border.getZMin())/CELL_SIZE_Z);
//            Log.i(TAG, "oMin: "+oMin);
//            Log.i(TAG, "sMin: "+sMin);
            xMax = (int)((triangle_XZ_Border.getXMax()-XZ_Border.getXMin())/CELL_SIZE_X);
            zMax = (int)((triangle_XZ_Border.getZMax()-XZ_Border.getZMin())/CELL_SIZE_Z);
//            Log.i(TAG, "oMax: "+oMax);
//            Log.i(TAG, "sMax: "+sMax);            

            i = zMax*COLUMN_NUMBER+xMax;        
            cells[i].addTriangle(triangle);
            
        }
    }
        
    
    public Cell[] getCells() {
        return cells;
    }

    public float getCELL_SIZE_X() {
        return CELL_SIZE_X;
    }

    public float getCELL_SIZE_Z() {
        return CELL_SIZE_Z;
    }

    public int getCOLUMN_NUMBER() {
        return COLUMN_NUMBER;
    }

    public int getROW_NUMBER() {
        return ROW_NUMBER;
    }

//    public XZ_Border getXZ_Border() {
//        return XZ_Border;
//    }    
//    
//    public float getSizeX(){
//        return XZ_Border.getSizeX();
//    }
//    
//    public float getSizeZ(){
//        return XZ_Border.getSizeZ();
//    }    
    
    private final ArrayList<Cell> cellsList = new ArrayList<Cell>();
    public ArrayList<Cell> getCellsAroundXZPosition(Vector3D position, int columnOffset, int rowOffset){
        cellsList.clear();
        int column = (int)((position.getX()-XZ_Border.getXMin())/CELL_SIZE_X);
        int row = (int)((position.getZ()-XZ_Border.getZMin())/CELL_SIZE_Z); 
        int columnMin = Math.max(0, column-columnOffset);
        int columnMax = Math.min(COLUMN_NUMBER-1, column+columnOffset);
        int rowMin = Math.max(0, row-rowOffset);
        int rowMax = Math.min(ROW_NUMBER-1, row+rowOffset);
        for (int r = rowMin; r <= rowMax; r++) {
            for (int c = columnMin; c <= columnMax; c++) {
                cellsList.add(cells[r*COLUMN_NUMBER+c]);
            }
        }
        return cellsList;
    }
    
    private final ArrayList<Triangle> nearTriangleList = new ArrayList<Triangle>();
    private Cell nearCell;
    public ArrayList<Triangle> getTrianglesAroundXZPosition(Vector3D position, float radius){
        nearTriangleList.clear();
        
        if (!XZ_Border.contains(position)){
            nearTriangleList.add(OUTSIDE_OF_TABLE_TRIANGLE);
            return nearTriangleList;
        }

        int index;
//        ArrayList<Integer> nonUsedSectors = new ArrayList<Integer>();
//        for (int i = 0; i < sectors.size(); i++) {
//            index = sectors.keyAt(i);
//            nonUsedSectors.add(index);
//        }

        int columnMin = (int)((position.getX()-radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMin = Math.max(0, columnMin);

        int rowMin = (int)((position.getZ()-radius-XZ_Border.getZMin())/CELL_SIZE_Z);
        rowMin = Math.max(0, rowMin);

        int columnMax = (int)((position.getX()+radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMax = Math.min(COLUMN_NUMBER-1, columnMax);

        int rowMax = (int)((position.getZ()+radius-XZ_Border.getZMin())/CELL_SIZE_Z);
        rowMax = Math.min(ROW_NUMBER-1, rowMax);


        int sectorKey;
        boolean isInList;

        for (int r = rowMin; r <= rowMax; r++) {
            for (int c = columnMin; c <= columnMax; c++) {
                sectorKey = r*COLUMN_NUMBER + c;
//                nonUsedSectors.remove(Integer.valueOf(sectorKey));
                ArrayList<Triangle> trianglesInSector;
                if (sectors.indexOfKey(sectorKey) < 0) {
                    trianglesInSector = readSector(sectorKey);
                    sectors.put(sectorKey, trianglesInSector);
                } else {
                    trianglesInSector = sectors.get(sectorKey);
                }

                for (Triangle triangle : trianglesInSector) {
                    isInList = false;
                    for (Triangle nearTriangle : nearTriangleList) {
                        if (nearTriangle.getIndex()==triangle.getIndex()){
                            isInList = true;
                            break;
                        }
                    }
                    if (isInList) {continue;}
                    if (triangle.isXZCircleCollisionWithCircumCircle(position, radius)) {
                        nearTriangleList.add(triangle);
                    }
                }
            }
        }
//        for (Integer nonUsedSector : nonUsedSectors) {
//            sectors.remove(nonUsedSector);
//        }

//        MainRenderer.sendDebugInfo("tri: "+nearTriangleList.size()+"\nsector_size: "+sectors.size(), 1000);
        return nearTriangleList;
    }

    public ArrayList<Triangle> getTrianglesAroundXZPosition_ER(Vector3D position, float radius){
        nearTriangleList.clear();

        if (!XZ_Border.contains(position)){
            nearTriangleList.add(OUTSIDE_OF_TABLE_TRIANGLE);
            return nearTriangleList;
        }

        int columnMin = (int)((position.getX()-radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMin = Math.max(0, columnMin);

        int rowMin = (int)((position.getZ()-radius-XZ_Border.getZMin())/CELL_SIZE_Z);
        rowMin = Math.max(0, rowMin);

        int columnMax = (int)((position.getX()+radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMax = Math.min(COLUMN_NUMBER-1, columnMax);

        int rowMax = (int)((position.getZ()+radius-XZ_Border.getZMin())/CELL_SIZE_Z);
        rowMax = Math.min(ROW_NUMBER-1, rowMax);

        int sectorKey;
        boolean isInList;
        ArrayList<Triangle> nearSector;
        for (int r = rowMin; r <= rowMax; r++) {
            for (int c = columnMin; c <= columnMax; c++) {


                nearCell = cells[r*COLUMN_NUMBER+c];

                for (Triangle triangle : nearCell.getTriangles()) {
                    isInList = false;
                    for (Triangle nearTriangle : nearTriangleList) {
                        if (nearTriangle.getIndex()==triangle.getIndex()){
                            isInList = true;
                            break;
                        }
                    }
                    if (isInList) {continue;}
//                    if (triangle.distance(position) > -radius) {
//                        if (triangle.distance(prevPosition) < 0) {
//                            nearTriangleList.add(triangle);
//                        }
//                    }
                    if (triangle.isXZCircleCollisionWithCircumCircle(position, radius)){
                        nearTriangleList.add(triangle);
                    }
                }
            }
        }

//        if (nearTriangleList.isEmpty()){
//            nearTriangleList.add(OUTSIDE_OF_TABLE_TRIANGLE);
//        }
        return nearTriangleList;
    }


    public float getY_in_XZ(Vector3D positionVector, float tolerance){
        TriangleInPosition triangleInPosition = getTriangleInPosition(positionVector, tolerance, "same");
        return triangleInPosition.getY();
                
    }  
    
//    private final ArrayList<TriangleInPosition> trianglesInPosition = new ArrayList<TriangleInPosition>();

    
    
    private static final LinkedHashMap<String, Integer> callers = new LinkedHashMap<String, Integer>();
    
//    public static void clearCallers(){
//        callers.clear();
//    }
//    
//    public static String getCallers(){
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, Integer> entrySet : callers.entrySet()) {
//            String key = entrySet.getKey();
//            Integer value = entrySet.getValue();
//            sb.append(key).append(": ").append(value).append("\n");
//        }
//        return sb.toString();
//    }
    
    synchronized public TriangleInPosition getTriangleInPosition(Vector3D positionVector, float verticalTolerance, String caller){
//        if (callers.containsKey(caller)){
//            int callerCount = callers.get(caller)+1;
//            callers.put(caller, callerCount);
//        }
//        else {
//            callers.put(caller, 1);
//        }
//        
        keresesSzam = 0;
//        trSize = 0;
        if (!XZ_Border.contains(positionVector)) {
            return OUTSIDE_TRIANGLE;
        }     
        float x = positionVector.getX();
        float y = positionVector.getY();
        float z = positionVector.getZ();         
//        Log.i(TAG, "terrain hivo: "+hivo);
        int column = (int)((x-XZ_Border.getXMin())/CELL_SIZE_X);
        column = Math.max(0, column);

        int row = (int)((z-XZ_Border.getZMin())/CELL_SIZE_Z);
        row = Math.max(0, row);


        int sectorKey = row*COLUMN_NUMBER+column;
        float currentTriangleY;
//        trianglesInPosition.clear();
        ArrayList<TriangleInPosition> trianglesInPosition = new ArrayList<TriangleInPosition>();
        ArrayList<Triangle> trianglesInSector;
        if (sectors.indexOfKey(sectorKey) < 0){
            trianglesInSector = readSector(sectorKey);
            sectors.put(sectorKey, trianglesInSector);
        }
        else {
            trianglesInSector = sectors.get(sectorKey);
        }

        for (Triangle triangle : trianglesInSector) {
            keresesSzam++;
            if (triangle.getNormal().getY()<0.001f) {continue;}
            if (triangle.isXZinside(x, z)) {
                currentTriangleY = triangle.getYInPlane(x,z);
                addAndSortTriangleInPosition(trianglesInPosition, new TriangleInPosition(currentTriangleY, triangle));
            }
        }        
        
        int sizeOfTriangles = trianglesInPosition.size();
//        trSize = sizeOfTriangles;
        
        if (trianglesInPosition.isEmpty()) {
            return OUTSIDE_TRIANGLE;
        }

        TriangleInPosition bottomTriangle = trianglesInPosition.get(0);
        if (sizeOfTriangles == 1) {
            return bottomTriangle;
        }
        
        if (y < bottomTriangle.getY()){
            return bottomTriangle;
        }
        
        TriangleInPosition topTriangle = trianglesInPosition.get(sizeOfTriangles-1);
        if (y > topTriangle.getY()-verticalTolerance) {
            return topTriangle;
        }
        
        for (int i = sizeOfTriangles-2; i >= 0; i--) {
            topTriangle = trianglesInPosition.get(i);
            if (y > topTriangle.getY()-verticalTolerance) {
                break;
            }
        }        
        
        return topTriangle;
                
    }  
    
    private void addAndSortTriangleInPosition(ArrayList<TriangleInPosition> trianglesInPosition, TriangleInPosition triangleInPosition){
        TriangleInPosition current;
        for (int i = 0; i < trianglesInPosition.size(); i++) {
            current = trianglesInPosition.get(i);
            if (triangleInPosition.getY()<current.getY()) {
                trianglesInPosition.add(i, triangleInPosition);
                return;
            }
        }

        trianglesInPosition.add(triangleInPosition);

        
    }    
    

    

     
 
         
//    public Triangle getTriangleUnder_er(Vector3D positionVector, float tolerance){
//        keresesSzam = 0;
//        if (!XZ_Border.contains(positionVector)) {
//            return OUTSIDE_OF_TABLE_TRIANGLE;
//        }     
//        float x = positionVector.getX();
//        float y = positionVector.getY();
//        float z = positionVector.getZ();         
////        Log.i(TAG, "terrain hivo: "+hivo);
//        int o = (int)((x-XZ_Border.getXMin())/CELL_SIZE_X);
//        int s = (int)((z-XZ_Border.getZMin())/CELL_SIZE_Z);
////        Log.i(TAG, "s, o: "+s+", "+o);
//        int i;
//        Triangle topTriangle = OUTSIDE_OF_TABLE_TRIANGLE;
//        float topTriangleY = OUTSIDE_OF_TABLE_TRIANGLE_Y;
//        float currentTriangleY;
//        i = s*COLUMN_NUMBER+o; 
//        trianglesInPosition.clear();
//        
//        for (Triangle triangle : cells[i].getTriangles()) {
//            keresesSzam++;
//            if (triangle.getNormal().getY()<0.01f) {continue;}
//            if (triangle.isXZinside(x, z)) {
////                Log.i(TAG, triangle.toString());
//                currentTriangleY = triangle.getYInPlane(x,z);
//                if ((currentTriangleY-tolerance)<y) {
//                    if (currentTriangleY>topTriangleY) {
//                        topTriangle = triangle;
//                        topTriangleY = currentTriangleY;
//                    }
//                }
//            }
//        }            
//        return topTriangle;
//                
//    }   
//        
  
    public float getY_in_XZ_Er(Vector3D positionVector, float tolerance){
        keresesSzam = 0;
        float x = positionVector.getX();
        float y = positionVector.getY();
        float z = positionVector.getZ();        
        if (!XZ_Border.contains(x, z)) {
            return OUTSIDE_OF_TABLE_TRIANGLE_Y;
        }      

//        Log.i(TAG, "terrain hivo: "+hivo);
        int o = (int)((x-XZ_Border.getXMin())/CELL_SIZE_X);
        int s = (int)((z-XZ_Border.getZMin())/CELL_SIZE_Z);
//        Log.i(TAG, "s, o: "+s+", "+o);
        int i;
//        Triangle topTriangle = OUTSIDE_OF_TABLE_TRIANGLE;
        float topTriangleY = OUTSIDE_OF_TABLE_TRIANGLE_Y;
        boolean noSet = true;
        float currentTriangleY;
        i = s*COLUMN_NUMBER+o; 
        for (Triangle triangle : cells[i].getTriangles()) {
            keresesSzam++;
            if (triangle.isXZinside(x, z)) {
//                Log.i(TAG, triangle.toString());
                currentTriangleY = triangle.getYInPlane(x, z);
                if (noSet) {
                    noSet = false;
                    topTriangleY = currentTriangleY;
                    continue;
                }
                if ((currentTriangleY-tolerance)<y) {
                    if (currentTriangleY>topTriangleY) {
//                            topTriangle = triangle;
                        topTriangleY = currentTriangleY;
                    }
                }
            }
        }            
        return topTriangleY;
                
    }   
        
    public int getKeresesSzam() {
        return keresesSzam;
    }

}
