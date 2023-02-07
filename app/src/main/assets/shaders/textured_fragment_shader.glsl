precision mediump float; 
                        
uniform sampler2D u_TextureUnit;

uniform vec4 u_AmbientColor;

uniform vec4 u_fog_color;
uniform float u_alpha;
uniform float u_sepia;


varying vec4 v_Position;
varying vec2 v_TextureCoordinates;
varying float diffuse; 
varying float infog;

varying float alphaByDistance; 

void main()                         
{                       
    vec4 tc = texture2D(u_TextureUnit, v_TextureCoordinates);
    float alpha = tc.a;
    if (tc.a < 0.15) {discard;}

    vec4 color = tc*diffuse;

    color = mix(color, vec4(u_AmbientColor.rgb, 1.0), u_AmbientColor.a);
    
    //~ float vizes_homokosY = clamp((v_Position.y+5.0)/5.2, 0.0, 1.0)*0.8+0.2;
    //~ color *= vizes_homokosY;    
    
    gl_FragColor = mix(color, u_fog_color, infog);
    
    gl_FragColor.a = u_alpha*alpha;

    gl_FragColor = tc;
}
