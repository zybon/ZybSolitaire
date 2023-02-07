uniform mat4 u_MMatrix;
uniform mat4 u_MVPMatrix;
uniform float u_lighting;

uniform vec4 u_SunPosition;
uniform vec4 u_CameraPosition;

uniform mat4 uShadowProjMatrix;

attribute vec4 a_Position;  
attribute vec3 a_Normal;  
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position; 
varying float v_cosine; 
varying float v_diffuse; 
varying vec4 vShadowCoord;

void main()                    
{               
    v_TextureCoordinates = a_TextureCoordinates;
    v_Position = a_Position;
    
    vShadowCoord = uShadowProjMatrix * u_MMatrix * a_Position;

    vec3 lightVector = normalize(vec3(u_SunPosition));

    vec3 modelViewNormal = normalize(vec3(u_MMatrix * vec4(a_Normal, 0.0)));
    
    //cosine is 0.0 - 1.0   
    //float cosine = max(dot(modelViewNormal, lightVector), 0.0);
    
    //set cosine to 0.4-0.7
    //cosine = 0.5+cosine*0.4;
    
    //add ambient
    //float lightningDiffuse = 0.0;
    //if (u_lighting>0.0) {
    //    lightningDiffuse = u_lighting;
    //        lightningDiffuse = lightningDiffuse/2.0;
    //}
    //diffuse = cosine+0.3+lightningDiffuse;
    v_cosine = dot(modelViewNormal, lightVector);

    v_diffuse = max(v_cosine, 0.0)*0.3+0.7; // (cosine*0.4-0.3);
    v_diffuse = min(v_diffuse, 1.0);
    gl_Position = u_MVPMatrix * a_Position;
}       
 