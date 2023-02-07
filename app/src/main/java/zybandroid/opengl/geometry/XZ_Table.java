package zybandroid.opengl.geometry;

import static zybandroid.opengl.util.ZybMath.clamp;

/**
 *
 * @author zybon
 * Created 2019.03.05. 16:35:35
 */
public class XZ_Table {
    
    protected final XZ_Border XZ_Border = new XZ_Border();
    protected float CELL_SIZE_X;
    protected float CELL_SIZE_Z;
    protected int COLUMN_NUMBER;
    protected int ROW_NUMBER; 

    public XZ_Table() {
    }
    
    public void set(XZ_Table xZ_Table){
        XZ_Border.set(xZ_Table.XZ_Border);
        CELL_SIZE_X = xZ_Table.CELL_SIZE_X;
        CELL_SIZE_Z = xZ_Table.CELL_SIZE_Z;
        COLUMN_NUMBER = xZ_Table.COLUMN_NUMBER;
        ROW_NUMBER = xZ_Table.ROW_NUMBER;
    }
    
    public void setXZ_Border(XZ_Border xZ_Border){
        XZ_Border.set(xZ_Border);
    }

    public void setCellSizeX(float CELL_SIZE_X) {
        this.CELL_SIZE_X = CELL_SIZE_X;
    }

    public void setCellSizeZ(float CELL_SIZE_Z) {
        this.CELL_SIZE_Z = CELL_SIZE_Z;
    }
    
    public void calcCellCountFromSize(){
        this.COLUMN_NUMBER = (int)(XZ_Border.getSizeX()/CELL_SIZE_X)+1;
        this.ROW_NUMBER = (int)(XZ_Border.getSizeZ()/CELL_SIZE_Z)+1;
    }

    public void setColumnCount(int COLUMN_NUMBER) {
        this.COLUMN_NUMBER = COLUMN_NUMBER;
    }

    public void setRowCount(int ROW_NUMBER) {
        this.ROW_NUMBER = ROW_NUMBER;
    }
    
    public void calcCellSizesFromCount(){
        this.CELL_SIZE_X = XZ_Border.getSizeX()/(float)COLUMN_NUMBER;
        this.CELL_SIZE_Z = XZ_Border.getSizeZ()/(float)ROW_NUMBER;
    }    

//    public XZ_Border getXZ_Border() {
//        return XZ_Border;
//    }

    public float getColumnSize() {
        return CELL_SIZE_X;
    }

    public float getRowSize() {
        return CELL_SIZE_Z;
    }

    public int getColumnCount() {
        return COLUMN_NUMBER;
    }

    public int getRowCount() {
        return ROW_NUMBER;
    }
    
    public int getCellCount(){
        return COLUMN_NUMBER*ROW_NUMBER;
    }

    public float getXMin() {
        return XZ_Border.getXMin();
    }

    public float getZMin() {
        return XZ_Border.getZMin();
    }

    public float getXMax() {
        return XZ_Border.getXMax();
    }

    public float getZMax() {
        return XZ_Border.getZMax();
    }
    
    public float getSizeX() {
        return XZ_Border.getSizeX();
    }

    public float getSizeZ() {
        return XZ_Border.getSizeZ();
    }

    public boolean contains(float x, float z) {
        return XZ_Border.contains(x, z);
    }

    public boolean contains(Vector3D position) {
        return XZ_Border.contains(position);
    }
    
    
    public int getColumnIndex(float x){
        int column = (int)((x-getXMin())/getColumnSize());
        return clamp(column, 0, COLUMN_NUMBER-1);
    }    
    
    public int getRowIndex(float z){
        int row = (int)((z-getZMin())/getRowSize());
        return clamp(row, 0, ROW_NUMBER-1);
    }
    
    public int getTableIndex(float x, float z){
        int column = getColumnIndex(x);
        int row = getColumnIndex(z);
        return row*COLUMN_NUMBER+column;
    }
    
    public XZ_Border getCellBorder(int columnIndex, int rowIndex){
        XZ_Border cellBorder = new XZ_Border();
        cellBorder.setXMin(this.getXMin()+columnIndex*this.getColumnSize());
        cellBorder.setXMax(cellBorder.getXMin()+this.getColumnSize());
        cellBorder.setZMin(this.getZMin()+rowIndex*this.getRowSize());
        cellBorder.setZMax(cellBorder.getZMin()+this.getRowSize());
        return cellBorder;
    }
    

}
