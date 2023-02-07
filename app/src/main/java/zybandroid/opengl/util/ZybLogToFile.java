package zybandroid.opengl.util;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;

import hu.zybon.zybsolitaire.LogActivity;
import zybandroid.util.naptar.ZybNaptar;

/**
 *
 * @author zybon
 * Created 2020.07.16. 15:20:18
 */
public class ZybLogToFile {
    
    public static String title = ""; 
    public static String log = ""; 
    
    public static void createHandlerForActivity(final Activity activity, final String title){
//        Thread.UncaughtExceptionHandler eredeti = Thread.getDefaultUncaughtExceptionHandler();
        
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            
            public void uncaughtException(Thread thread, Throwable thrwbl) {
                String log = getError(thread, thrwbl);
                ZybLogToFile.title = title;
                ZybLogToFile.log = log;
                activity.startActivity(new Intent(activity, LogActivity.class));
//                writeToFile(title, log);
                
//                activity.sendBroadcast(createBroadCastIntent(logNev, log));
                activity.finish();
//                killCurrentProcess();
            }
            
//            private void writeToFile(String title, String content){
//                String dirPath = Environment.getExternalStorageDirectory().getPath()+"/zyblogs";
//                File dir = new File(dirPath);
//                if (!dir.exists()){
//                    dir.mkdir();
//                }
//                String time = ZybNaptar.millisecToFormatString(System.currentTimeMillis(), "Y_m_d_H_i_s_v");
//                File file = new File(dir, "log_"+title+"_"+time+".txt");
//                try {
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(content.getBytes());
//                fos.flush();
//                fos.close();
//                }
//                catch (Exception e){
//
//                }
//            }
        });
    }
    
    public static void createHandlerForService(final Service service){
//        Intent intent = createIntent(logNev, "proba");
//        if (intent.resolveActivity(service.getPackageManager()) == null) {
//            Log.i(logNev, "Nincs telepítve a ZybLog");
//            return;
//        }
        
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            
            public void uncaughtException(Thread thread, Throwable thrwbl) {
                String logNev = ""+service.getApplicationInfo().loadLabel(service.getPackageManager());
                String log = getError(thread, thrwbl);
                service.sendBroadcast(createBroadCastIntent(logNev, log));  
                service.stopSelf();
                killCurrentProcess();
            }

        });
    }
    
    public static final Intent createBroadCastIntent(String nev, String log){
        Intent intent = new Intent();
        intent.setAction("hu.zybon.zyblog.ACTION_NEWLOG");
        intent.putExtra("hu.zybon.zyblog.EXTRA_CRASHEDNAME", nev);
        intent.putExtra("hu.zybon.zyblog.EXTRA_LOG", log);
        return intent;
    }
     
    public static final String getError(Thread thread, Throwable thrwbl){
        StringBuilder sb = new StringBuilder();
        sb.append("Hiba ideje: ");
        sb.append(ZybNaptar.millisecToFormatString(System.currentTimeMillis(), "Y.m.d H:i:s.v"));
        sb.append('\n');
        sb.append(thread.toString());
        sb.append("\nHiba oka:\n");
        sb.append(thrwbl.getCause());
        sb.append("\nHiba üzenet:\n");
        sb.append(thrwbl.getMessage());
        sb.append('\n');
        sb.append("\nHiba helye:\n");
        for (StackTraceElement stackTrace : thrwbl.getStackTrace()) {
            sb.append(stackTrace.getClassName());  
            sb.append(".");
            sb.append(stackTrace.getMethodName());  
            sb.append('\n');
            sb.append(stackTrace.getFileName()); 
            sb.append(" ");
            sb.append(stackTrace.getLineNumber());  
            sb.append(". sor");
            sb.append("\n\n");
        }

        return sb.toString();
    }            

    public static final void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }      
}

