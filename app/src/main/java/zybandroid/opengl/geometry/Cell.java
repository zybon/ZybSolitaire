package zybandroid.opengl.geometry;

import java.util.ArrayList;

/**
 *
 * @author zybon
 * Created 2019.01.28. 12:32:47
 */
public class Cell{
        
    private final ArrayList<Triangle> triangles = new ArrayList<Triangle>();

    public void addTriangle(Triangle triangle){
        triangles.add(triangle);
    }

    public ArrayList<Triangle> getTriangles(){
        return triangles;
    }

}   
