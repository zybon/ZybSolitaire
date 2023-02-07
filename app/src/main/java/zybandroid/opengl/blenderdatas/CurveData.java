package zybandroid.opengl.blenderdatas;
//
//import android.content.Context;
//import hu.zybon.zybonotopia.car.Car;
//import zybandroid.opengl.geometry.Vector3D;
//
///**
// *
// * @author zybon
// * Created 2018.03.08. 12:47:48
// */
//public class CurveData extends ObjectData{
//    
//    private static final String TAG = "CurveData";
//    
//    private Vector3D[] curvePoints;
//
//    public CurveData(String nev) {
//        super(nev, TIPUS.CURVE);
//    }
//    
//    /*
//    * MESH:
//     * 
//     * vertices:
//     *      ff = ee+4
//     *      ee-ff: Lv = vertices tomb hossza [int]
//     * 
//     *      gg = ff+Lv*3*4
//     *      ff-gg: vertices tomb [float[3*Lv]] {az x,y,z koordináták egymás után}
//     * 
//     
//     */        
//    public int readData(byte[] bytes, int i){
////        Log.i(TAG, "nev: "+getNev());
//        int l;
//        l = readInt(i, bytes, INT_BYTE_DB);
//        i+=INT_BYTE_DB;
//        createList(createFloatTomb_b(i, bytes, l/INT_BYTE_DB));
//        i+=l;
//        return i;
//    }
//    
//    public Vector3D[] getCurvePoints() {
//        return curvePoints;
//    }
//
//    private void createList(float[] curvePointsF) {
//        int points = curvePointsF.length/3;
//        curvePoints = new Vector3D[points];
//        int i = 0;
//        for (int p = 0; p < points; p++) {
//            curvePoints[p] = new Vector3D(curvePointsF[i++], curvePointsF[i++], curvePointsF[i++]);
//        }
//        
//    }
//
//    
//    
//}
