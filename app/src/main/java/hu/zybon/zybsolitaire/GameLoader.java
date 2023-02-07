package hu.zybon.zybsolitaire;

import java.util.ArrayList;
import zybandroid.opengl.util.ZybLoader;

/**
 *
 * @author zybon
 * Created 2020.06.03. 13:02:52
 */
public class GameLoader {
    
//    private float loaderCount;
    private float loaderCurrentWeight;
    private float loaderSumWeight;
    private String loaderName;
    private final ArrayList<ZybLoader> loaders = new ArrayList<ZybLoader>();

    public GameLoader() {
        
    }
    
    public void addLoader(ZybLoader loader){
        loaders.add(loader);
//        loaderCount += 1f;
        loaderSumWeight += loader.getWeight();
    }
    
    public boolean hasLoader(){
        return !loaders.isEmpty();
    }
    
    public String getNextLoaderName(){
        if (loaders.isEmpty()) {return "";}
        return loaders.get(0).getName();
    }    
    
    public void loadNext(){
        ZybLoader loader = loaders.remove(0);
        loader.load();
        loaderCurrentWeight += loader.getWeight();
    }
    
    public float getLoadPercent(){
        return (loaderCurrentWeight/loaderSumWeight);
    }
    

    
}
