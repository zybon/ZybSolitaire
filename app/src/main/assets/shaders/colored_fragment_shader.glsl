precision mediump float; 
      	 		
uniform vec4 u_fog_color;
uniform float u_fog_min_dist_from_camera;
uniform float u_fog_gradient_depth;
						
varying vec3 v_Color;  
 

void main()                    		
{   

    vec4 color = vec4(v_Color,1.0);                                  		
    gl_FragColor = color;

    //if (tc.a < 0.2) {discard;}
    //tc = tc * diffuse;
    //float distance_from_cam = (gl_FragCoord.z/gl_FragCoord.w);
    //float fog_density = distance_from_cam - u_fog_min_dist_from_camera;
    //float fog = (fog_density / u_fog_gradient_depth);

    //float fog = fog_coord*u_fog_density;

    //gl_FragColor = mix(u_fog_color, tc, clamp(1.0-fog,0.0,1.0));
}