package zybandroid.opengl.geometry;

import zybandroid.opengl.util.ZybMath;

/**
 *
 * @author zybon
 * Created 2019.01.23. 11:01:45
 */
public class XZ_Border {
    
    private float xMin = Integer.MAX_VALUE;
    private float xMax = Integer.MIN_VALUE;
    
    private float zMin = Integer.MAX_VALUE;
    private float zMax = Integer.MIN_VALUE;    
    
    public XZ_Border() {
        
    }

    public XZ_Border(XZ_Border xz_border) {
        this.set(xz_border);
    }

    public XZ_Border(float xMin, float xMax, float zMin, float zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }    
    
    public final void toZero(){
        xMin = 0;
        xMax = 0;

        zMin = 0;
        zMax = 0;         
    }    
    
    public final void reset(){
        xMin = Integer.MAX_VALUE;
        xMax = Integer.MIN_VALUE;

        zMin = Integer.MAX_VALUE;
        zMax = Integer.MIN_VALUE;         
    }

    public void setXMin(float xMin) {
        this.xMin = xMin;
    }
    
    public void setXMinWithCompare(float x) {
        if (x<xMin) {
            this.xMin = x;
        }
    }    

    public void setXMax(float xMax) {
        this.xMax = xMax;
    }
    
    public void setXMaxWithCompare(float x) {
        if (x>xMax) {
            this.xMax = x;
        }
    }       

    public void setZMin(float zMin) {
        this.zMin = zMin;
    }
    
    public void setZMinWithCompare(float z) {
        if (z<zMin) {
            this.zMin = z;
        }
    }       

    public void setZMax(float zMax) {
        this.zMax = zMax;
    }
    
    public void setZMaxWithCompare(float z) {
        if (z>zMax) {
            this.zMax = z;
        }
    }       
    
    public void setWithCompare(float x, float z){
        if (x<xMin) {
            this.xMin = x;
        } 
        if (x>xMax) {
            this.xMax = x;
        }  
        if (z<zMin) {
            this.zMin = z;
        } 
        if (z>zMax) {
            this.zMax = z;
        }               
    }   
    
    public final void set(XZ_Border border){
        this.xMin = border.xMin;
        this.xMax = border.xMax;
        this.zMin = border.zMin;
        this.zMax = border.zMax;
    }      
    
    public void setWithCompare(XZ_Border border){
        if (border.xMin<xMin) {
            this.xMin = border.xMin;
        } 
        if (border.xMax>xMax) {
            this.xMax = border.xMax;
        }  
        if (border.zMin<zMin) {
            this.zMin = border.zMin;
        } 
        if (border.zMax>zMax) {
            this.zMax = border.zMax;
        }               
    }     

    public float getXMin() {
        return xMin;
    }

    public float getXMax() {
        return xMax;
    }

    public float getZMin() {
        return zMin;
    }

    public float getZMax() {
        return zMax;
    }

    public float getCenterX() {
        return (xMin+xMax)/2f;
    }
    
    public float getCenterZ() {
        return (zMin+zMax)/2f;
    }    
    
    public float getSizeX(){
        return xMax-xMin;
    }
    
    public float getSizeZ(){
        return zMax-zMin;
    }    
    
    public boolean contains(float x, float z) {
        return (x>=xMin && x<=xMax && z>=zMin && z<=zMax);
    }  
    
    public boolean contains(Vector3D position) {
        return (position.getX()>=xMin && position.getX()<=xMax && 
                position.getZ()>=zMin && position.getZ()<=zMax);
    }    
    
    public void moveBorderFromCenter(float offset){
        float x_size_Per2 = getSizeX()/2.0f;
        float z_size_Per2 = getSizeZ()/2.0f;
        float centerX = getCenterX();
        float centerZ = getCenterZ();
        this.xMin = centerX-x_size_Per2-offset;
        this.xMax = centerX+x_size_Per2+offset;
        this.zMin = centerZ-z_size_Per2-offset;
        this.zMax = centerZ+z_size_Per2+offset;        
    }
    
    public void moveWithVector(Vector3D vector){
        this.xMin += vector.getX();
        this.xMax += vector.getX();
        this.zMin += vector.getZ();
        this.zMax += vector.getZ();         
    }
    
    public void clamp(float xMin, float xMax, float zMin, float zMax) {
        this.xMin = ZybMath.clamp(this.xMin, xMin, xMax);
        this.xMax = ZybMath.clamp(this.xMax, xMin, xMax);
        this.zMin = ZybMath.clamp(this.zMin, zMin, zMax);
        this.zMax = ZybMath.clamp(this.zMax, zMin, zMax);
    }    
//
//    @Override
//    public String toString() {
//        return getSizeX()+"x"+getSizeZ();
//    }

    @Override
    public String toString() {
        return "{" + "xMin=" + xMin + ", xMax=" + xMax + ", zMin=" + zMin + ", zMax=" + zMax + '}';
    }


    
    

}
