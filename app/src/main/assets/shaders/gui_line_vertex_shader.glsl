#version 300 es

uniform vec4 u_Positions[2];

void main()                    
{               
    gl_Position = u_Positions[gl_VertexID];  
}          
