precision highp float; 
                        
uniform sampler2D u_ColorTextureUnit;   
uniform sampler2D u_DepthTextureUnit; 
uniform sampler2D u_BlurColorTextureUnit; 

uniform vec2 u_sunPositionOnScreen;

uniform vec4 u_Color;
uniform vec4 u_SunColor;
uniform float u_alpha;
uniform float u_to_bnw;

varying vec4 v_Position;
                                    
varying vec2 v_TextureCoordinates; 

varying float v_sunShine;


void main()                         
{   
    vec4 color = texture2D(u_ColorTextureUnit, v_TextureCoordinates);
    float textureAlpha = color.a;

    color = mix(color, vec4(u_Color.rgb, 1.0), u_Color.a);

    if (u_to_bnw>0.5){
        float LUMIN = color.r*0.2126 + color.g*0.7152 + color.b*0.0722;
        color = vec4(LUMIN);
        color.rgb = ((color.rgb - 0.5) * (0.7))+0.5;
    }

    gl_FragColor = color;
    gl_FragColor.a = textureAlpha*u_alpha;
    
}
