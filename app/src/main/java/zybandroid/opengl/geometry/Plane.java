package zybandroid.opengl.geometry;

/**
 *
 * @author zybon
 * Created 2018.02.25. 21:55:44
 */
public class Plane {
    
    
    protected final Vector3D[] vertices = new Vector3D[3];
    protected final Vector3D normal = new Vector3D();
    protected final Vector3D aPointInPlane = new Vector3D();

    protected float planeConstant;

    public Plane() {
        this.vertices[0] = new Vector3D();
        this.vertices[1] = new Vector3D();
        this.vertices[2] = new Vector3D();       
    }
    
    public final void copyPlaneData(Plane plane){
        this.vertices[0].set(plane.vertices[0]);
        this.vertices[1].set(plane.vertices[1]);
        this.vertices[2].set(plane.vertices[2]);
        this.normal.set(plane.normal);
        this.aPointInPlane.set(plane.aPointInPlane);
        this.planeConstant = plane.planeConstant;       
    }

    public final void setVertices(Vector3D v1, Vector3D v2, Vector3D v3) {
        vertices[0].set(v1);
        vertices[1].set(v2);
        vertices[2].set(v3);
    }


    
    public final void setVertex0(Vector3D vector) {
        vertices[0].set(vector);
    }    
    
    public final void setVertex1(Vector3D vector) {
        vertices[1].set(vector);
    } 
    
    public final void setVertex2(Vector3D vector) {
        vertices[2].set(vector);
    } 
    
    public final Vector3D getVertex0() {
        return vertices[0];
    }    
    
    public final Vector3D getVertex1() {
        return vertices[1];
    }  
    
    public final Vector3D getVertex2() {
        return vertices[2];
    }      
    
    public final void setNormal(float[] xyz){
        normal.set(xyz);
    }
    
    private void createNormal(){
        Vector3D v1 = new Vector3D(vertices[2], vertices[0]);
        v1.normalize();
        Vector3D v2 = new Vector3D(vertices[2], vertices[1]);
        v2.normalize();
        normal.setCrossProductBetween(v1, v2);
        normal.normalize();
    }

    public final void initPlaneConstant(){
        createNormal();
        for (Vector3D vertex : vertices) {
            if (!vertex.isNullVector()) {
                aPointInPlane.set(vertex);
                break;
            }
        }
        planeConstant = aPointInPlane.getX()*normal.getX()+aPointInPlane.getY()*normal.getY()+aPointInPlane.getZ()*normal.getZ();
    }
    
    public final void calcPlaneConstants(){
        createNormal();
        for (Vector3D vertex : vertices) {
            if (!vertex.isNullVector()) {
                aPointInPlane.set(vertex);
                break;
            }
        }
        planeConstant = aPointInPlane.getX()*normal.getX()+aPointInPlane.getY()*normal.getY()+aPointInPlane.getZ()*normal.getZ();
    }


    
    public float getYInPlane(float x, float z){
        return (planeConstant-normal.getX()*x-normal.getZ()*z)/normal.getY();
    }  

    public float getXInPlane(float y, float z){
        return (planeConstant-normal.getY()*y-normal.getZ()*z)/normal.getX();
    }

    public float getZInPlane(float x, float y){
        return (planeConstant-normal.getX()*x-normal.getY()*y)/normal.getZ();
    }        
    
    public float distance(float x, float y, float z){
        return planeConstant-(normal.getX()*x+normal.getY()*y+normal.getZ()*z);
    }
    
    public float distance(Vector3D v){
        return planeConstant-(normal.getX()*v.getX()+normal.getY()*v.getY()+normal.getZ()*v.getZ());
    }    
    
    public float getAbsoluteDistanceFrom(Vector3D v){
        return Math.abs(planeConstant-(normal.getX()*v.getX()+normal.getY()*v.getY()+normal.getZ()*v.getZ()));
    }    

    public Vector3D getNormal() {
        return normal;
    }
    
    public boolean isPointBehind(Vector3D positionVector){
        return distance(positionVector) > 0;
    }
    
    public boolean isPointFront(Vector3D positionVector){
        return distance(positionVector) < 0;
    }    

    public Vector3D getIntersectionPointWithRay(Vector3D startVector, Vector3D endVector){
        Vector3D e = new Vector3D(startVector, endVector);
        float A = startVector.getX()*normal.getX()+startVector.getY()*normal.getY()+startVector.getZ()*normal.getZ(); 
        float B = e.getX()*normal.getX()+e.getY()*normal.getY()+e.getZ()*normal.getZ(); 
        float t = (planeConstant-A)/B;
        return new Vector3D(startVector.getX()+e.getX()*t,startVector.getY()+e.getY()*t,startVector.getZ()+e.getZ()*t);
    }   
    
    public Vector3D getIntersectionPointWithRay2(Vector3D startVector, Vector3D endVector){
        Vector3D rayVector = new Vector3D(startVector, endVector);
        float t = distance(startVector)/rayVector.dotProduct(normal);
        rayVector.scale(t);
        rayVector.translateWithVector(startVector);
        return rayVector;
    } 
    
    
//    public Vector3D getDofesPontPontEsVector(Vector3D v1, Vector3D e){
//        float A = v1.getX()*normal.getX()+v1.getY()*normal.getY()+v1.getZ()*normal.getZ(); 
//        float B = e.getX()*normal.getX()+e.getY()*normal.getY()+e.getZ()*normal.getZ(); 
//        float t = (planeConstant-A)/B;
//        return new Vector3D(v1.getX()+e.getX()*t,v1.getY()+e.getY()*t,v1.getZ()+e.getZ()*t);
//    }      

}
