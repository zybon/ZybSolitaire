package zybandroid.opengl.geometry;

/**
 *
 * @author zybon
 * Created 2020.11.16. 15:03:39
 */
public class TriangleInPosition {
    
    private final float y;
    private final Triangle triangle;



    public TriangleInPosition(float y, Triangle triangle) {
        this.y = y;
        this.triangle = triangle;

    }

    public Triangle getTriangle() {
        return triangle;
    }

    public float getY() {
        return y;
    }    

}
