uniform sampler2D u_DepthTextureUnit; 

uniform vec4 u_Scale;
uniform vec2 u_sunPositionOnScreen;

attribute vec4 a_Position;  
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
varying float v_sunShine;



void main()                    
{            
    v_sunShine = 0.0;

    
    v_Position = a_Position;
    v_Position.x *= u_Scale[2];
    v_Position.y *= u_Scale[3];
    v_Position.x += u_Scale[0];
    v_Position.y += u_Scale[1];

    v_TextureCoordinates = a_Position.xy*0.5+0.5;
    v_TextureCoordinates.y = -v_TextureCoordinates.y;

    gl_Position = v_Position;

}          
