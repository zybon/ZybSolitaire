package zybandroid.opengl.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zybon
 * Created 2020.06.05. 10:06:01
 */
public class SaveManager {
        
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedEditor;  
    
    private final HashMap<String, Saveable> saveables = new HashMap<String, Saveable>();

    public SaveManager(Context context, String managerName) {
        sharedPreferences = context.getSharedPreferences(managerName, Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();              
    }

    public boolean hasSavedData(){
        return !sharedPreferences.getAll().isEmpty();
    }

    public void clearSavedGame(){
        sharedEditor.clear();
        sharedEditor.commit();
    }
    
    public void addSaveAble(Saveable saveable){
        saveables.put(saveable.getSaveLabel(), saveable);
    }

    public void saveGame(){
        for (Map.Entry<String, Saveable> entrySet : saveables.entrySet()) {
            sharedEditor.putString(entrySet.getKey(), entrySet.getValue().getSavedData()); 
        }
        sharedEditor.commit();          
    }

    public void loadSavedGame(){
        for (Map.Entry<String, Saveable> entrySet : saveables.entrySet()) {
            String savedData = sharedPreferences.getString(entrySet.getKey(), "");
            if (savedData.isEmpty()) {
                continue;
            }

            entrySet.getValue().loadSavedData(savedData);
        }        
    }         

    public static interface Saveable{
        
        public String getSaveLabel();
        public String getSavedData();
        public void loadSavedData(String savedData);
    
    }

}    
