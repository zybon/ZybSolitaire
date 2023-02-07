package zybandroid.opengl.geometry;

import android.graphics.RectF;
import android.opengl.Matrix;

import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2018.03.09. 11:41:34
 */
public class Triangle extends Plane{
    
    public static final float TWO_PI = (float)(Math.PI*2.0);
    
    private final XZ_Border XZ_Border = new XZ_Border();

//    private final int index;
    
    private final Vector3D center = new Vector3D();
    private float radius;
    private float radiusSquere;
    
    private int materialIndex = -1;
    
    private final Vector3D[] normalInVertex = new Vector3D[3];
    private final ZybColor[] colorInVertex = new ZybColor[3];
    
    private final int[] vertexIndices = new int[3];
    
    private int index;
    
    public static void resetIndexCount(){
        indexCounter = 0;
    }
    
    private static int indexCounter = 0;
    
    public static int getIndexCounter(){
        return indexCounter;
    }

    private int zoneIndex;
    
    public Triangle() {
        normalInVertex[0] = new Vector3D();
        normalInVertex[1] = new Vector3D();
        normalInVertex[2] = new Vector3D();

        colorInVertex[0] = new ZybColor();
        colorInVertex[1] = new ZybColor();
        colorInVertex[2] = new ZybColor();
//        this.index = index;
        index = indexCounter++;
    }
    
    public Triangle(Triangle triangle) {
        this.copyPlaneData(triangle);
//        this.index = index;
        index = indexCounter++;
    }

    public void setIndex(int index){
        this.index = index;

    }

    public void setZoneIndex(int zoneIndex) {
        this.zoneIndex = zoneIndex;
    }

    public int getZoneIndex() {
        return zoneIndex;
    }

    public final void setVertex(int index, float[] xyz) {
        vertices[index].set(xyz);
    }

    public final void setVertexColor(int index, float[] xyz) {
        colorInVertex[index].set(xyz);
    }

    public ZybColor getColorInPosition(Vector3D position){
        float x  = position.getX();
        float z  = position.getZ();

        float D = ((vertices[1].getZ()-vertices[2].getZ())*(vertices[0].getX()-vertices[2].getX())+
                (vertices[2].getX()-vertices[1].getX())*(vertices[0].getZ()-vertices[2].getZ()));
        float C_W0_X = (vertices[1].getZ()-vertices[2].getZ()) / D;
        float C_W0_Y = (vertices[2].getX()-vertices[1].getX()) / D;
        float Wv0 = (x-vertices[2].getX())*C_W0_X + (z-vertices[2].getZ())*C_W0_Y;


        float C_W1_X = (vertices[2].getZ()-vertices[0].getZ()) / D;
        float C_W1_Y = (vertices[0].getX()-vertices[2].getX()) / D;
        float Wv1 = (x-vertices[2].getX())*C_W1_X + (z-vertices[2].getZ())*C_W1_Y;

        float Wv2 = 1.0f - Wv0 - Wv1;

        ZybColor color = new ZybColor();
        color.setR(colorInVertex[0].getR()*Wv0 + colorInVertex[1].getR()*Wv1 + colorInVertex[2].getR()*Wv2);
        color.setG(colorInVertex[0].getG()*Wv0 + colorInVertex[1].getG()*Wv1 + colorInVertex[2].getG()*Wv2);
        color.setB(colorInVertex[0].getB()*Wv0 + colorInVertex[1].getB()*Wv1 + colorInVertex[2].getB()*Wv2);
        return color;
    }

    public int getIndex() {
        return index;
    }
    
    public void setMaterialIndex(int materialIndex) {
        this.materialIndex = materialIndex;
    }

    public int getMaterialIndex() {
        return materialIndex;
    }
    
    public void setNormalInVertex0(Vector3D normalVector){
        this.normalInVertex[0].set(normalVector);
    }
    
    public void setNormalInVertex1(Vector3D normalVector){
        this.normalInVertex[1].set(normalVector);
    }
    
    public void setNormalInVertex2(Vector3D normalVector){
        this.normalInVertex[2].set(normalVector);
    }


    
    public final Vector3D getNormalInVertex0() {
        return normalInVertex[0];
    }    
    
    public final Vector3D getNormalInVertex1() {
        return normalInVertex[1];
    }  
    
    public final Vector3D getNormalInVertex2() {
        return normalInVertex[2];
    }       
    
    public void setVertex0Index(int index){
        this.vertexIndices[0] = index;
    }
    
    public void setVertex1Index(int index){
        this.vertexIndices[1] = index;
    }
    
    public void setVertex2Index(int index){
        this.vertexIndices[2] = index;
    }    
    
    public int getVertex0Index(){
        return this.vertexIndices[0];
    }        
    
    public int getVertex1Index(){
        return this.vertexIndices[1];
    }            
    
    public int getVertex2Index(){
        return this.vertexIndices[2];
    }        
    
    public void scale(float scale){
        for (Vector3D vertex : vertices) {
            vertex.scale(scale);
        }
    }
    
    public void rotateAroundY(float sinA, float cosA){
        for (Vector3D vertex : vertices) {
            vertex.rotateAroundY(sinA, cosA);
        }    
    }
    
//    public void rotateAroundAxis(Vector3D rotationAxis, float rotationAngle){
//        for (Vector3D vertex : vertices) {
//            vertex.rotateAroundY(sinA, cosA);
//        }    
//    }    
    
    public void translateWithVector(Vector3D translateVector){
        for (Vector3D vertex : vertices) {
            vertex.translateWithVector(translateVector);
        }     
    }
    
    public void multiplyModelMatrix(float[] modelMatrix){
        for (Vector3D vertex : vertices) {
            Matrix.multiplyMV(vertex.xyzw, 0, modelMatrix, 0, vertex.xyzw, 0);
        }         
    }
    
    public void initTriangleAndPlaneConstants(){
        initPlaneConstant();
        init_XZ_Border();
        initCenter();
        initRadius();
    }

    public void calcTriangleAndPlaneConstants(){
        calcPlaneConstants();
        init_XZ_Border();
        initCenter();
        initRadius();
    }
    
    private void init_XZ_Border(){
        for (Vector3D vertex : vertices) {
            XZ_Border.setWithCompare(vertex.getX(), vertex.getZ());
        }       
    }
    
    private void initCenter(){
        float x = 0;
        float y = 0;
        float z = 0;
        for (Vector3D vertex : vertices) {
            x += vertex.getX();
            y += vertex.getY();
            z += vertex.getZ();
        }
        
        center.set(x/3f, y/3f, z/3f);
    }
    
    private void initRadius(){
        float maxDistanceSquereFromCenter = 0;
        for (Vector3D vertex : vertices) {
            float distanceSquere = vertex.distanceSquereFromVector(center);
            if (distanceSquere>maxDistanceSquereFromCenter) {
                maxDistanceSquereFromCenter = distanceSquere;
            }
        }        
        radiusSquere = maxDistanceSquereFromCenter;
        radius = (float)Math.sqrt(maxDistanceSquereFromCenter);
    }

    public Vector3D getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public float getRadiusSquere() {
        return radiusSquere;
    }

    public XZ_Border getXZ_Border() {
        return XZ_Border;
    }

    public boolean isInRect(RectF rectF) {
        for (Vector3D vertex : vertices) {
            if (rectF.contains(vertex.getX(), vertex.getZ())) {
                return true;
            }
        }
        return false;
    }    
    
    public boolean isXZCircleCollisionWithCircumCircle(Vector3D circleCenterPosition, float circleRadius){
        float sumRadiusSquere = (circleRadius+this.radius) * (circleRadius+this.radius);
        return circleCenterPosition.distanceSquereFromVectorInXZPlane(center)<sumRadiusSquere;
    }

    public boolean isXZinside(float x, float z){
        if (!XZ_Border.contains(x, z)){
            return false;
        }
        if (cross_2d(x-vertices[0].getX(), z-vertices[0].getZ(), 
                vertices[1].getX()-vertices[0].getX(), vertices[1].getZ()-vertices[0].getZ()) > 0.0) {
            return false;
        }
        if (cross_2d(x-vertices[1].getX(), z-vertices[1].getZ(), 
                vertices[2].getX()-vertices[1].getX(), vertices[2].getZ()-vertices[1].getZ()) > 0.0) {
            return false;
        }
        if (cross_2d(x-vertices[2].getX(), z-vertices[2].getZ(), 
                vertices[0].getX()-vertices[2].getX(), vertices[0].getZ()-vertices[2].getZ()) > 0.0) {
            return false;
        }
        return true;       
    }
    
    private float cross_2d(float x1, float z1, float x2, float z2) {
//            float r = z1*x2 - x1*z2;
//            Log.i(TAG, "r = "+r);
        return z1*x2 - x1*z2;
    }
    
    public boolean isInside(Vector3D pointVector){
        if (!XZ_Border.contains(pointVector.getX(), pointVector.getZ())){
            return false;
        }        
        Vector3D v1 = new Vector3D(pointVector, vertices[0]);
//        if (v1.isNullVector()) {
////            Log.i("isInside", "v1.isNullVektor()");
//            return true;
//        }
        Vector3D v2 = new Vector3D(pointVector, vertices[1]);
//        
//        if (v2.isNullVector()) {
////            Log.i("isInside", "v2.isNullVektor()");
//            return true;
//        }        
        Vector3D v3 = new Vector3D(pointVector, vertices[2]);
//        if (v3.isNullVector()) {
////            Log.i("isInside", "v3.isNullVektor()");
//            return true;
//        }        
        float angleSum = v1.angleInRad(v2)+v1.angleInRad(v3)+v2.angleInRad(v3);
//        Log.i("isInside", "szog = "+szog);
//        Log.i("isInside", "szog/KET_PI = "+szog/KET_PI);
        return (Math.abs(angleSum-TWO_PI) < 0.0001f);
    }    
    
    /**
     * Két pont által felírt egyenes metszi a háromszöget:
     * Igaz lehetőségek:
     *      háromszög síkja párhuzamos:
     *          ha a távolság 0, és valamelyik pont a háromszögön belül van
     *      háromszög síkja nem párhuzamos:
     *           ha valamelyik pont a háromszögön belül van (ez a döféspont)
     *           ha a két pont a sík két oldalán helyezkedik el:
     *              a döféspont a háromszögön belül van
     * @param v1 Egyik pont koordinátája
     * @param v2 Másik pont koordinátája
     * @return true ha metszi az egyenes a háromszöget
     */
    public void egyenesMetsziE(CollisionTest collisionTest, Vector3D v1, Vector3D v2){
        float tav1 = distance(v1);
        float tav2 = distance(v2);
        collisionTest.tav1 = tav1;
        collisionTest.tav2 = tav2;
//        Log.i("UtkozesTeszt", "tav1 = "+tav1);
//        Log.i("UtkozesTeszt", "tav2 = "+tav2);

//        párhuzamos az egyenes a síkkal
        if (tav1 == tav2) {
//            síkon fekszik
            if (tav1 == 0) {
                if (isInside(v1)) {
//                    Log.i("UtkozesTeszt", "tav1 == tav2 == 0; v1 belül");
                    collisionTest.dofesPont.set(v1);
                    collisionTest.vanDofesPont = true;
                    return;
                }
                if (isInside(v2)){
//                    Log.i("UtkozesTeszt", "tav1 == tav2 == 0; v2 belül");
                    collisionTest.dofesPont.set(v2);
                    collisionTest.vanDofesPont = true;
//                    return;
                }
//                return;  
            }
//            else {
//                return;  
//            }
        }
//        nem párhuzamos, azaz valahol metszi a síkot        
        else {
//            az egyik pont a síkon fekszik, azaz ez a döféspont
            if (tav1 == 0) {
                if (isInside(v1)){
//                    Log.i("UtkozesTeszt", "tav1 == 0 és v1 belül");
                    collisionTest.dofesPont.set(v1);
                    collisionTest.vanDofesPont = true;
                    return;
                }
                else {
                    return;
                }
            }
//            a másik pont a síkon fekszik, azaz ez a döféspont        
            if (tav2 == 0) {
                if (isInside(v2)){
//                    Log.i("UtkozesTeszt", "tav2 == 0 és v2 belül");
                    collisionTest.dofesPont.set(v2);
                    collisionTest.vanDofesPont = true;
                    return;
                }
                else {
                    return;
                }
            }
//            egyik pont sem fekszik a síkon        
//            a pontok a sík két oldalán fekszenek, másszóval a sík döféspontja a 
//                a két pont között lesz
                if ((tav1<0 && tav2>0) || (tav1>0 && tav2<0)) {
                    Vector3D dp = getIntersectionPointWithRay(v1, v2);
                    if (isInside(dp)){
//                        Log.i("UtkozesTeszt", "dp belül");
                        collisionTest.dofesPont.set(dp);
                        collisionTest.vanDofesPont = true;
//                        return METSZES_INFO;
                    }
//                    else {
//                        return;
//                    }
//                a pontok a sík azonos oldalán fekszenek, másszóval a sík döféspontja a 
//                a két ponton kívül lesz, így nincs ütközés
                }
//                else {
//                    return;  
//                }
        }
    }


    
    public static class CollisionTest{
        public float tav1;
        public float tav2;
        
        public final Vector3D dofesPont = new Vector3D();
        public boolean vanDofesPont;
        
        public Triangle triangle;

        public CollisionTest() {
            vanDofesPont = false;
        }
        
        

        private void reset() {
            vanDofesPont = false;
            dofesPont.set(0, 0, 0);
                     
        }
        
    }

    @Override
    public String toString() {
        return "triangle:\n(v0: "+vertices[0]+"\nv1: "+vertices[1]+"\nv2: "+vertices[2]+")\nn: "+normal+"\n";
    }

}
  
