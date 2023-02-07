uniform mat4 u_MMatrix;
uniform mat4 u_MVPMatrix;

uniform vec3 u_Color;

attribute vec4 a_Position;  
attribute vec3 a_Normal; 

varying vec3 v_Color; 

float diffuse;

void main()
{
    //vec4 v_worldPosition = u_MMatrix*a_Position;
    vec3 lightVector = normalize(vec3(2.0,3.0,1.0));

    vec3 modelViewNormal = normalize(vec3(u_MMatrix * vec4(a_Normal, 0.0)));
    
    //cosine is 0.0 - 1.0   
    float cosine = max(dot(modelViewNormal, lightVector), 0.0);
    
    //set cosine to 0.4-0.7
    cosine = 0.5+cosine*0.4;
    
    //add ambient
    diffuse = cosine+0.3;
 
    v_Color = u_Color*diffuse;

    // gl_Position is a special variable used to store the final position.
    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    gl_Position = u_MVPMatrix * a_Position;

}   




