#version 300 es

precision mediump float; 
                        
uniform vec4 u_Color;
uniform float u_alpha;

out vec4 fragmentColour;
                                    
void main()                         
{   
    fragmentColour = vec4(1.0, 1.0, 1.0, 0.4f);                        
}
