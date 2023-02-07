package zybandroid.opengl.geometry;

//import android.util.FloatMath;

import zybandroid.opengl.util.ZybMath;


/**
 *
 * @author zybon
 * Created 2017.12.29. 12:47:41
 */
public class Vector3D{
    
    public static final float PI =  (float)Math.PI;
    public static final float RAD = PI/180.0f;
    public static final float PIper4 = PI/4.0f;    




    
    public float[] xyzw = new float[4];    
    
    public Vector3D() {
    }
    
    public Vector3D(float[] xyz){
        set(xyz);
    }    
    
    public Vector3D(Vector3D v){
        set(v);
    }
    
    public Vector3D(float x, float y, float z) {
        set(x, y, z);
    }
    
    public Vector3D(float x, float y, float z, float w) {
        set(x, y, z, w);
    }    

    public Vector3D(Vector3D from, Vector3D to) {
        set(from, to);
    }
    
    public final void reset(){
        this.xyzw[0] = 0;
        this.xyzw[1] = 0;
        this.xyzw[2] = 0;    
        this.xyzw[3] = 1;    
    }
    
    public final void set(float x, float y, float z){
        this.xyzw[0] = x;
        this.xyzw[1] = y;
        this.xyzw[2] = z;    
        this.xyzw[3] = 1;     
    } 
    
    public final void set(float x, float y, float z, float w){
        this.xyzw[0] = x;
        this.xyzw[1] = y;
        this.xyzw[2] = z;    
        this.xyzw[3] = w;     
    }    
    
    public final void set(float[] xyz){
        this.xyzw[0] = xyz[0];
        this.xyzw[1] = xyz[1];
        this.xyzw[2] = xyz[2];   
        this.xyzw[3] = 1;      
    }    
    
    public final void set(Vector3D v){
        this.xyzw[0] = v.xyzw[0];
        this.xyzw[1] = v.xyzw[1];
        this.xyzw[2] = v.xyzw[2];   
        this.xyzw[3] = 1;      
    }
    
    public final void set(Vector3D from, Vector3D to) {
        set(  to.getX()-from.getX(), 
                to.getY()-from.getY(),
                to.getZ()-from.getZ()
                );
    }
    
    public void setToCenterPoint(Vector3D v1, Vector3D v2) {
        set(    (v1.getX()+v2.getX())/2.0f,
                (v1.getY()+v2.getY())/2.0f,
                (v1.getZ()+v2.getZ())/2.0f);        
    }    
    
    public void setCrossProductBetween(Vector3D v1, Vector3D v2){
        set(
                v1.getY()*v2.getZ() - v1.getZ()*v2.getY(), 
                v1.getZ()*v2.getX() - v1.getX()*v2.getZ(), 
                v1.getX()*v2.getY() - v1.getY()*v2.getX() 
        );        
    }
    
    
    public void setX(float x){
        this.xyzw[0] = x;
    }
    
    public void setY(float y){
        this.xyzw[1] = y;
    }
    
    public void setZ(float z){
        this.xyzw[2] = z;
    }  
    
    public void setW(float w){
        this.xyzw[3] = w;
    }     
    
    public final float getX() {
        return xyzw[0];
    }
    
    public final float getY() {
        return xyzw[1];
    }
    
    public final float getZ() {
        return xyzw[2];
    }
    
    public final float getW() {
        return xyzw[3];
    }    
    
    public float length_squere(){
        return xyzw[0]*xyzw[0]+xyzw[1]*xyzw[1]+xyzw[2]*xyzw[2];
    }    
    
    public float length(){
        return (float)Math.sqrt(length_squere());
    }
    
    public void rotateAroundX(float deg) {
        rotateAroundX((float)Math.sin(Math.toRadians(deg)), 
                (float)Math.cos(Math.toRadians(deg)));
    }       
    
    public void rotateAroundX(float sinA, float cosA) {
        float aktY = xyzw[1];
        float aktZ = xyzw[2];
        xyzw[1] = aktY*cosA - aktZ*sinA;
        xyzw[2] = (aktY*sinA + aktZ*cosA);         
    } 
    
    public void rotateAroundMinusX(float deg) {
        rotateAroundMinusX((float)Math.sin(Math.toRadians(deg)), 
                (float)Math.cos(Math.toRadians(deg)));
    }       
    
    public void rotateAroundMinusX(float sinA, float cosA) {
        float aktY = xyzw[1];
        float aktZ = xyzw[2];
        xyzw[1] = -aktY*cosA + aktZ*sinA;
        xyzw[2] = -(aktY*sinA - aktZ*cosA);         
    }     
    
    public void rotateAroundY(float deg) {
        rotateAroundY((float)Math.sin(Math.toRadians(deg)), 
                (float)Math.cos(Math.toRadians(deg)));
    }     
    
    public void rotateAroundY(float sinA, float cosA) {
        float aktX = xyzw[0];
        float aktZ = xyzw[2];
        xyzw[0] = (aktX*cosA + aktZ*sinA);
        xyzw[2] = (-aktX*sinA + aktZ*cosA);    
    }      
    
    
    
    public void rotateAroundZ(float deg) {
        rotateAroundZ((float)Math.sin(Math.toRadians(deg)), 
                (float)Math.cos(Math.toRadians(deg)));
    }     
    
    public void rotateAroundZ(float sinA, float cosA) {
        float aktX = xyzw[0];
        float aktY = xyzw[1];
        xyzw[0] = aktX*cosA - aktY*sinA;
        xyzw[1] = aktX*sinA + aktY*cosA; 
    }  
    
     
    
    public Vector3D createtranslatesal(float x, float y, float z){
        return new Vector3D(this.xyzw[0]+x, this.xyzw[1]+y, this.xyzw[2]+z);
    }    
    
    public Vector3D createtranslatesalMasikVektorIranyaba(Vector3D v, float d){
        Vector3D uj = new Vector3D(this);
        uj.translateInVectorDirection(v, d);
        return uj;
    }   
    
    public void addX(float x) {
        this.xyzw[0] += x;
    }    
    
    public void addY(float y) {
        this.xyzw[1] += y;
    }    
    
    public void addZ(float z) {
        this.xyzw[2] += z;
    }        
    
    public void translateInX(float x) {
        this.xyzw[0] += x;
    }    
    
    public void translateInY(float y) {
        this.xyzw[1] += y;
    }    
    
    public void translateInZ(float z) {
        this.xyzw[2] += z;
    }        
    
    public void translateWithVector(Vector3D vector) {
        this.xyzw[0] += vector.xyzw[0];
        this.xyzw[1] += vector.xyzw[1];
        this.xyzw[2] += vector.xyzw[2];
    }  
    
    public void translateWithVectorOpposite(Vector3D vector) {
        this.xyzw[0] -= vector.xyzw[0];
        this.xyzw[1] -= vector.xyzw[1];
        this.xyzw[2] -= vector.xyzw[2];
    }      

    public void translate(float dx, float dy, float dz){
        this.xyzw[0] += dx;
        this.xyzw[1] += dy;
        this.xyzw[2] += dz;
    }    
    
    public void translateInVectorDirection(Vector3D v, float d){
        Vector3D m = v.getNormalizaltVektor();
        this.xyzw[0] += (m.xyzw[0]*d);
        this.xyzw[1] += (m.xyzw[1]*d);
        this.xyzw[2] += (m.xyzw[2]*d);
    }  
    
    public void translateInNormalisedVectorDirection(Vector3D nv, float d){
        this.xyzw[0] += (nv.xyzw[0]*d);
        this.xyzw[1] += (nv.xyzw[1]*d);
        this.xyzw[2] += (nv.xyzw[2]*d);
    }       
    
    public void scale(float scale) {
        set(this.xyzw[0]*scale, this.xyzw[1]*scale, this.xyzw[2]*scale);
    }  
    
    public void scale(float scaleX, float scaleY, float scaleZ) {
        set(this.xyzw[0]*scaleX, this.xyzw[1]*scaleY, this.xyzw[2]*scaleZ);
    }     
    
    public Vector3D crossProduct(Vector3D v2){
        return new Vector3D(
                getY()*v2.getZ() - getZ()*v2.getY(), 
                getZ()*v2.getX() - getX()*v2.getZ(), 
                getX()*v2.getY() - getY()*v2.getX() 
        );
    }
    
    public float dotProduct(Vector3D v2){
        return getX()*v2.getX() + getY()*v2.getY() + getZ()*v2.getZ();
    }    
    
    public float angleInRad(Vector3D v2){
        return (float)(Math.acos(dotProduct(v2)/(length()*v2.length())));
    }    
    
    public float angleInDegrees(Vector3D v2){
        float divider = length()*v2.length();
        if (divider<0.001) {return 0.0f;}
        return (float)(Math.toDegrees(Math.acos(dotProduct(v2)/divider)));
    }        
    
    public float hajlasSzogAbs(Vector3D v2){
        float s = Math.abs(dotProduct(v2)/(length()*v2.length()));
        if (kerekit(s, 5) == 1) {return 0;}
        if (kerekit(s, 5) == 0) {return 90;}
        return (float)(Math.acos(s)/RAD);
    }
    
    public float hajlasSzog(Vector3D v2){
        float s = dotProduct(v2)/(length()*v2.length());
//        return (float)(Math.acos(s)/RAD);
//        Vector3D vsz = this.crossProduct(v2);
//        if (vsz.getZ()>0) {
//            if (kerekit(s, 5) == 1) {return 0;}
//            if (kerekit(s, 5) == 0) {return 90;}
    //        System.out.println(s+" "+Math.acos(s)+" "+Math.acos(s)/RAD);
            return (float)(Math.acos(s)/RAD);        
//        }
//        else {
//            if (kerekit(s, 5) == 1) {return 0;}
//            if (kerekit(s, 5) == 0) {return 270;}
//    //        System.out.println(s+" "+Math.acos(s)+" "+Math.acos(s)/RAD);
//            return (float)(360-(Math.acos(s)/RAD));            
//        }

    }    
    
    public float distanceSquereFromVector(Vector3D v2){
        float dx = getX()-v2.getX();
        float dy = getY()-v2.getY();
        float dz = getZ()-v2.getZ();        
        return dx*dx + dy*dy + dz*dz;
    }    
    
    
    
    public float distanceFromVector(Vector3D v2){
        return (float)Math.sqrt(distanceSquereFromVector(v2));
    }
    
    public float distanceSquereFromVectorInXZPlane(Vector3D v2){
        float dx = getX()-v2.getX();
        float dz = getZ()-v2.getZ();        
        return dx*dx + dz*dz;
    }
    
    public float distanceFromVectorInXZPlane(Vector3D v2){
        return (float)Math.sqrt(distanceSquereFromVectorInXZPlane(v2));
    }      
    
    public static Vector3D createCenterPoint(Vector3D v1, Vector3D v2){
        return new Vector3D(
                (v1.getX()+v2.getX())/2.0f,
                (v1.getY()+v2.getY())/2.0f,
                (v1.getZ()+v2.getZ())/2.0f
        );
        
    }
    
    public static float distanceSquereBetweenTwoVectors(Vector3D v1, Vector3D v2) {
        float dx = v1.getX()-v2.getX();
        float dy = v1.getY()-v2.getY();
        float dz = v1.getZ()-v2.getZ();
//        if (Math.abs(dx)<0.0001f &&
//                Math.abs(dy)<0.0001f &&
//                Math.abs(dz)<0.0001f ) {
//            return 0f;
//        }
        return dx*dx + dy*dy + dz*dz;
    }    
      
    public static Vector3D vectorBetween(Vector3D v1, Vector3D v2){
        return new Vector3D(
                v1.xyzw[0]-v2.xyzw[0],
                v1.xyzw[1]-v2.xyzw[1],
                v1.xyzw[2]-v2.xyzw[2]);
    }     
    
    public static float distanceBetweenTwoVectors(Vector3D v1, Vector3D v2) {
        float dx = v1.getX()-v2.getX();
        float dy = v1.getY()-v2.getY();
        float dz = v1.getZ()-v2.getZ();
//        if (Math.abs(dx)<0.0001f &&
//                Math.abs(dy)<0.0001f &&
//                Math.abs(dz)<0.0001f ) {
//            return 0f;
//        }
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }    
    
    public void normalize(){
        scale(1f/length());
    }      

    public Vector3D getNormalizaltVektor(){
        float h = length();
        return new Vector3D(getX()/h, getY()/h, getZ()/h);
    }    

    public boolean isNullVector(){
        return (Math.abs(xyzw[0])<0.0001f && Math.abs(xyzw[1])<0.0001f && Math.abs(xyzw[2])<0.0001f);
    }
    
    public void slerp(Vector3D vector2, float sleerpFactor){
        if (this.distanceSquereFromVector(vector2)<0.000001) {
            return;
        }  
        sleerpFactor = ZybMath.clamp(sleerpFactor, 0.0f, 1.0f);
        this.xyzw[0] += ((vector2.xyzw[0]-xyzw[0])*sleerpFactor);
        this.xyzw[1] += ((vector2.xyzw[1]-xyzw[1])*sleerpFactor);
        this.xyzw[2] += ((vector2.xyzw[2]-xyzw[2])*sleerpFactor);
    }
    
    public void blend(Vector3D vector1, Vector3D vector2, float blendFactor){
        if (vector1.distanceSquereFromVector(vector2)<0.000001) {
            set(vector2);
            return;
        }  
        blendFactor = ZybMath.clamp(blendFactor, 0.0f, 1.0f);
        this.xyzw[0] = vector1.xyzw[0]+((vector2.xyzw[0]-vector1.xyzw[0])*blendFactor);
        this.xyzw[1] = vector1.xyzw[1]+((vector2.xyzw[1]-vector1.xyzw[1])*blendFactor);
        this.xyzw[2] = vector1.xyzw[2]+((vector2.xyzw[2]-vector1.xyzw[2])*blendFactor);
    }    

    public static Vector3D mix(Vector3D vector1, Vector3D vector2, float blendFactor) {
        Vector3D mix = new Vector3D(vector1, vector2);
        if (mix.length_squere()<0.0001) {
            mix.set(vector1);
            return mix;
        }
        mix.scale(blendFactor);
        mix.translateWithVector(vector1);
        return mix;
    }
    
    public static float kerekit(float szam, int tizedes){
        float tiz = 1.0f;
        while (tizedes>0) {
            tiz *= 10.0f;
            tizedes--;
        }
        return Math.round(szam*tiz)/tiz;
    }      
    
    @Override
    public String toString() {
        return "(x:"+kerekit(xyzw[0], 3)+", y:"+kerekit(xyzw[1], 3)+", z:"+kerekit(xyzw[2], 3)+")";
    }

    public final float calculateAngleAroundY(){
        Vector3D vectorTemp = new Vector3D();
        vectorTemp.set(getX(), 0, getZ());
        vectorTemp.normalize();        
        float angleAroundY = (float)(Math.toDegrees(Math.acos(-vectorTemp.getZ())));
        if (vectorTemp.getX()>0f) {
            angleAroundY = 180f-angleAroundY;
        }    
        else {
            angleAroundY = angleAroundY-180f;
        }
        return angleAroundY;
    } 
    
    public final float calculateAngleAroundX(){
        float Y = calculateAngleAroundY();
        
        Vector3D vectorTemp = new Vector3D();
        vectorTemp.set(getX(), getY(), getZ());  
        vectorTemp.rotateAroundY(-Y);
        vectorTemp.normalize();        
        float angleAroundX = (float)(Math.toDegrees(Math.acos(vectorTemp.getY())));
        angleAroundX = angleAroundX-90f;
        return angleAroundX;
    }     


    
}
