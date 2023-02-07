package zybandroid.opengl.geometry;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import zybandroid.opengl.blenderdatas.InstanceData;
import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.blenderdatas.SectorData;
import zybandroid.opengl.blenderdatas.SectoredMeshData;

/**
 *
 * @author zybon
 * Created 2019.02.26. 11:43:43
 */
public class Triangle_XZ_Table_er extends XZ_Table{

    private static final String TAG = "TriangleTable";

    private final ArrayList<Triangle> triangles = new ArrayList<Triangle>();

    private Cell[] cells;
//    private float CELL_SIZE_X;
//    private float CELL_SIZE_Z;
//    private int COLUMN_NUMBER;
//    private int ROW_NUMBER;

//    private final XZ_Border XZ_Border = new XZ_Border();

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

    public Triangle_XZ_Table_er() {

    }

    public void addSolidMap(String solidMapName){


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
    
    
        
    
//    public void loadTrianglesFromMesh_er(MeshData data){
//        int[] trianglesData = data.getTriangles();
//        float[] verticesData = data.getVertices();
//        float[] normalsData = data.getNormals();
//        
//        int v;
//        int n;
//        Triangle triangle;
//        Vector3D cs1;
//        Vector3D cs2;
//        Vector3D cs3;
//        Vector3D normal;
////        int h = 0;
//        int rh = -1;
//        int stride = 3;
//        for (int j = 0; j < trianglesData.length;) {
//            rh += 1;
//            n = trianglesData[j+1]*3;
//            normal = new Vector3D(
//                    normalsData[n++], 
//                    normalsData[n++], 
//                    normalsData[n]
//            );
////            if (normal.getY()<=0.0001f){
////                j += stride*3;
////                continue;
////            }            
//            
//            triangle = new Triangle();
//                //elso csucs
//                //vertex
//                v = trianglesData[j]*3;
//                cs1 = new Vector3D(
//                        verticesData[v++], 
//                        verticesData[v++], 
//                        verticesData[v]
//                );
//                
//                //2. csucs
//                //vertex                
//                v = trianglesData[j+stride]*3;
//                cs2 = new Vector3D(
//                        verticesData[v++], 
//                        verticesData[v++], 
//                        verticesData[v]
//                );
//                
//                //3. csucs
//                v = trianglesData[j+stride*2]*3;
//                cs3 = new Vector3D(
//                        verticesData[v++], 
//                        verticesData[v++], 
//                        verticesData[v]
//                );         
//   
//            triangle.setVertices(cs1, cs2, cs3);
////            triangle.setNormal(normal);
//            triangle.createNormal();
//            triangle.initPlaneConstant();
//            
//            triangle.initTriangleConstants();
//            XZ_Border.setWithCompare(triangle.getXZ_Border());            
//            addTriangle(triangle);
////            Log.i(TAG, triangle.toString());
//            j += stride*3;
//        }   
//        
//    }
    
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
        
        int columnMin = (int)((position.getX()-radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMin = Math.max(0, columnMin);
        
        int rowMin = (int)((position.getZ()-radius-XZ_Border.getZMin())/CELL_SIZE_Z); 
        rowMin = Math.max(0, rowMin);
        
        int columnMax = (int)((position.getX()+radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMax = Math.min(COLUMN_NUMBER-1, columnMax);
        
        int rowMax = (int)((position.getZ()+radius-XZ_Border.getZMin())/CELL_SIZE_Z); 
        rowMax = Math.min(ROW_NUMBER-1, rowMax);
        
        boolean isInList;
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
    
    public ArrayList<Triangle> getTrianglesAroundXZPosition_ER(Vector3D position, float radius){
        nearTriangleList.clear();
        
        
        
        int columnMin = (int)((position.getX()-radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMin = Math.max(0, columnMin);
        
        int rowMin = (int)((position.getZ()-radius-XZ_Border.getZMin())/CELL_SIZE_Z); 
        rowMin = Math.max(0, rowMin);
        
        int columnMax = (int)((position.getX()+radius-XZ_Border.getXMin())/CELL_SIZE_X);
        columnMax = Math.min(COLUMN_NUMBER-1, columnMax);
        
        int rowMax = (int)((position.getZ()+radius-XZ_Border.getZMin())/CELL_SIZE_Z); 
        rowMax = Math.min(ROW_NUMBER-1, rowMax);
        for (int r = rowMin; r <= rowMax; r++) {
            for (int c = columnMin; c <= columnMax; c++) {
                nearCell = cells[r*COLUMN_NUMBER+c];
                for (Triangle triangle : nearCell.getTriangles()) {
                    if (triangle.distance(position) > -radius) {
                        nearTriangleList.add(triangle);
                    }
                }
            }
        }
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
        int columnIndex = getColumnIndex(x);//(int)((x-XZ_Border.getXMin())/CELL_SIZE_X);
        int rowIndex = getRowIndex(z);//(int)((z-XZ_Border.getZMin())/CELL_SIZE_Z);
//        Log.i(TAG, "s, o: "+s+", "+o);
        int cellIndex = rowIndex*COLUMN_NUMBER+columnIndex; 
        float currentTriangleY;
//        trianglesInPosition.clear();
        ArrayList<TriangleInPosition> trianglesInPosition = new ArrayList<TriangleInPosition>();
        for (Triangle triangle : cells[cellIndex].getTriangles()) {
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
