package zybandroid.opengl.programs;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;
import android.util.Log;
import zybandroid.opengl.util.LoggerConfig;

/**
 *
 * @author zybon
 * Created 2020.02.25. 15:31:11
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int complieVertexShader(String shaderCode, String programName){
        return compileShader(GL_VERTEX_SHADER, shaderCode, programName);
    }

    public static int complieFragmentShader(String shaderCode, String programName){
        return compileShader(GL_FRAGMENT_SHADER, shaderCode, programName);
    }    

    private static int compileShader(int type, String shaderCode, String programName) {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader");
            }
            return 0;
        }
        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
//            if (LoggerConfig.ON) {
//                Log.v(TAG, "Results of compiling source:"
//                        + "\n["+(type == GL_VERTEX_SHADER?"VERTEX SHADER":"FRAGMENT SHADER")+"]"
//                        + "\n" + shaderCode + "\n:"
//                            + glGetShaderInfoLog(shaderObjectId));
//            }   

        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.

            if (LoggerConfig.ON) {
                String error = "Compliation of shader failed:\n"
                        + "\nprogramName: " + programName + "\n"
                        + "\n["+(type == GL_VERTEX_SHADER?"VERTEX SHADER":"FRAGMENT SHADER")+"]"
                        + "\n" + glGetShaderInfoLog(shaderObjectId)
                        + "\nSOURCE CODE:\n" + shaderCode + "\n";
                Log.w(TAG, error);    
                throw new AssertionError(error);
            } 
            glDeleteShader(shaderObjectId);
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        final int programObjectId = glCreateProgram();

        if (programObjectId == 0){
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

//            if (LoggerConfig.ON) {
//                Log.v(TAG, "Results of linkking program source:" + "\n"
//                            + glGetProgramInfoLog(programObjectId));        
//            }

        if (linkStatus[0] == 0){
            //If it failed, delete the program object

            if (LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed");
                Log.w(TAG, "Results of linkking program source:" + "\n"
                                            + glGetProgramInfoLog(programObjectId));                     
            }
            glDeleteProgram(programObjectId);
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
//            Log.v(TAG, "Result of validating program: "+validateStatus[0]
//                    +"\nLog: "+glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource, String programName){
        int program;

        int vertexShaderId = complieVertexShader(vertexShaderSource, programName);
        int fragmentShaderId = complieFragmentShader(fragmentShaderSource, programName);

        program = linkProgram(vertexShaderId, fragmentShaderId);

        if (LoggerConfig.ON){
            ShaderHelper.validateProgram(program);
        }

        return program;
    }

}   

    
