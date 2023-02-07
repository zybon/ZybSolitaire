uniform mat4 u_MMatrix;
uniform mat4 u_MVPMatrix;
uniform float u_lighting;

uniform float u_textureScale;

uniform float u_fadeInCameraDistance;
uniform float u_fadeDensityCameraDistance;

uniform float u_fog_min_dist_from_camera;
uniform float u_fog_gradient_depth;

uniform vec4 u_SunPosition;
uniform vec4 u_CameraPosition;

attribute vec4 a_Position;  
attribute vec3 a_Normal;  
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position; 
varying float diffuse; 
varying float infog;

varying float alphaByDistance; 


void main()                    
{    


    v_TextureCoordinates = a_TextureCoordinates*u_textureScale;
    v_Position = u_MMatrix * a_Position;

    
    vec3 lightVector = normalize(vec3(u_SunPosition));

    vec3 modelViewNormal = normalize(vec3(u_MMatrix * vec4(a_Normal, 0.0)));
    
    //cosine is 0.0 - 1.0   
    float cosine = max(dot(modelViewNormal, lightVector), 0.0);
    diffuse = cosine*0.6+0.4;
    
    
    //set cosine to 0.4-0.7
    //cosine = 0.5+cosine*0.4;
    
    //add ambient
    //float lightningDiffuse = 0.0;
    //if (u_lighting>0.0) {
    //    lightningDiffuse = u_lighting;
    //        lightningDiffuse = lightningDiffuse/2.0;
    //}
    //diffuse = cosine+0.3+lightningDiffuse;
    

    gl_Position = u_MVPMatrix * a_Position;
    
    alphaByDistance = 1.0;
    float distFromCamera = distance(vec3(v_Position), vec3(u_CameraPosition));
    
    float alpha_density = distFromCamera - u_fadeInCameraDistance;
    alphaByDistance -= clamp(alpha_density / (u_fadeDensityCameraDistance+0.0001), 0.0, 1.0);
    
    float fog_density = distFromCamera - u_fog_min_dist_from_camera;
    infog = clamp(fog_density / u_fog_gradient_depth, 0.0, 1.0);     
}       
 
