precision mediump float; 
                        
uniform sampler2D u_TextureUnit;                                        
uniform vec4 u_Color;

uniform float u_Alpha;  

varying vec2 v_TextureCoordinates; 
  
void main()                         
{                       
    vec4 tc = texture2D(u_TextureUnit, v_TextureCoordinates);
    if (tc.a < 0.8) {
        //~ gl_FragColor = vec4(0.2);
        //~ return;
        discard;
    }
    tc *= u_Color;
    tc.a = u_Alpha;
    gl_FragColor = tc;//vec4(1.0,1.0,1.0,0.2);                                  
}
