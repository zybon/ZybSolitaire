attribute vec4 a_Position;
varying vec4 v_Position;

void main()                    
{            
    v_Position = a_Position;
    gl_Position = a_Position;  
}          
