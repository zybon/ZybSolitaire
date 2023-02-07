precision mediump float; 
                        
uniform sampler2D u_TextureUnit;   

uniform vec4 u_Color;
uniform float u_alpha;
uniform float u_Percent;

varying vec2 v_Position;
varying vec2 v_TextureCoordinates; 

float isDrawable(){
    if (u_Percent>0.999){return 1.0;}
    float fragmentValue = clamp((v_Position.x+0.96)/(2.0*0.96), 0.0, 1.0);
    if (fragmentValue > u_Percent){
        return 0.0;
    }
    return 1.0;
}

void main()                         
{   
    vec4 tc = texture2D(u_TextureUnit, v_TextureCoordinates);
    if (isDrawable()<0.5){
        discard;

    }
    //if (tc.a < 0.2) {discard;}
    //~ tc.a = u_alpha;
    gl_FragColor = tc*u_Color;//vec4(1.0,1.0,1.0,0.2);         
    gl_FragColor.a = tc.a*u_alpha;                         
}
