precision highp float; 
                        
uniform sampler2D u_ColorTextureUnit;   
//uniform sampler2D u_DepthTextureUnit;   

varying vec4 v_Position;
                                    
varying vec2 v_TextureCoordinates; 

varying vec2 blurTextCoords[11];

  
void main()                         
{   
    vec4 color = vec4(0.0);
    
    color += texture2D(u_ColorTextureUnit, blurTextCoords[0])*0.0093;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[1])*0.028002;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[2])*0.065984;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[3])*0.121703;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[4])*0.175713;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[5])*0.198596;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[6])*0.175713;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[7])*0.121703;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[8])*0.065984;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[9])*0.028002;
    color += texture2D(u_ColorTextureUnit, blurTextCoords[10])*0.0093;



    gl_FragColor = color;
    
    gl_FragColor.a = 1.0;
}
