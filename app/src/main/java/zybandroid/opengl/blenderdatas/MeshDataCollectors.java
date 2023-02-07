package zybandroid.opengl.blenderdatas;

import android.content.Context;
import java.util.HashMap;

/**
 *
 * @author zybon
 * Created 2020.06.10. 9:31:30
 */
public class MeshDataCollectors {
    
    private static Context context;
    private static final HashMap<String, MeshDataCollector> collectors = new HashMap<String, MeshDataCollector>();
    
    public static void init(Context context){
        MeshDataCollectors.context = context;
    }
    
    public static MeshDataCollector getCollector(String collectorFileName) {
        if (collectors.containsKey(collectorFileName)){
            return collectors.get(collectorFileName);
        }
        
        MeshDataCollector mdc = MeshDataCollector.readFromAsset(context, collectorFileName);
        mdc.setCollectorFileName(collectorFileName);
        collectors.put(collectorFileName, mdc);
        return mdc;
    }
    
    public static void delete(String collectorFileName) {
        collectors.remove(collectorFileName);
    }    

    public static void clear() {
        collectors.clear();
    }
}
