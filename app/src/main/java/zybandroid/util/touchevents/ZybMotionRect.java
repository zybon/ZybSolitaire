package zybandroid.util.touchevents;

/**
 *
 * @author zybon
 * Created 2017.08.06. 19:20:09
 */
public class ZybMotionRect {
    float left;
    float top;
    float right;
    float bottom;

    float szel;
    float mag;
    float kozepX;
    float kozepY;
    float atlo;

    public ZybMotionRect() {
    }

    public ZybMotionRect(float left, float top, float right, float bottom) {
        set(left, top, right, bottom);
    }

    public final void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        init();
    }

    private void init(){
        this.szel = right-left;
        this.mag = bottom-top;
        this.kozepX = left+szel/2;
        this.kozepY = top+mag/2;
        this.atlo = (float)Math.sqrt(szel*szel+mag*mag);
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public float getSzel() {
        return szel;
    }

    public float getMag() {
        return mag;
    }

    public float getKozepX() {
        return kozepX;
    }

    public float getKozepY() {
        return kozepY;
    }

    public float getAtlo() {
        return atlo;
    }

    

}
    
