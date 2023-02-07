uniform mat4 u_MMatrix;

attribute vec4 a_Position;  
attribute vec2 a_TextureCoordinates;

varying vec2 v_Position;
varying vec2 v_TextureCoordinates;

void main()                    
{               
    v_Position = a_Position.xy;
    v_TextureCoordinates = a_TextureCoordinates;	  	  
    gl_Position = (u_MMatrix * a_Position);  

}          