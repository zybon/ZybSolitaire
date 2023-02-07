package zybandroid.opengl.util;

/**
 *
 * @author zybon
 * Created 2020.06.03. 13:46:09
 */
public abstract class ZybLoader {
    
    private final String name;
    private final float weight;

    public ZybLoader(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }
    
    public abstract void load();
}
