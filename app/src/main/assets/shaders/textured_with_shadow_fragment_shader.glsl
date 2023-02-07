precision mediump float; 
      	 				
uniform sampler2D u_TextureUnit;
uniform sampler2D uShadowTexture; 

uniform vec4 u_AmbientColor;

uniform vec4 u_fog_color;
uniform float u_alpha;
uniform float u_sepia;
uniform float u_fog_min_dist_from_camera;
uniform float u_fog_gradient_depth;

varying vec2 v_TextureCoordinates;
varying float v_diffuse; 
varying float v_cosine; 

// shadow coordinates
varying vec4 vShadowCoord;

const float d_tex = 1.0/1024.0;

float isInShadowArea(){
    if (vShadowCoord.w < 0.0) {
        return 0.0;
    }

    vec4 shadowMapPosition = vShadowCoord / vShadowCoord.w;
    if (shadowMapPosition.s<d_tex){
        return 0.0;
    }
    if (shadowMapPosition.t<d_tex){
        return 0.0;
    }
    if (shadowMapPosition.s>1.0-d_tex){
        return 0.0;
    }
    if (shadowMapPosition.t>1.0-d_tex){
        return 0.0;
    }
    return 1.0;
}

//Simple shadow mapping
float shadowSimple(vec2 d_vec) 
{ 
	vec4 shadowMapPosition = vShadowCoord / vShadowCoord.w;
        float distanceFromLight = texture2D(uShadowTexture, shadowMapPosition.st+d_vec).r;
	//add bias to reduce shadow acne (error margin)
	float bias = 0.0001;

	//1.0 = not in shadow (fragmant is closer to light than the value stored in shadow map)
	//0.0 = in shadow
	return float(distanceFromLight > shadowMapPosition.z - bias);
}

void main()                    		
{                       
    vec4 tc = texture2D(u_TextureUnit, v_TextureCoordinates);
    float alpha = tc.a;
    if (tc.a < 0.2) {discard;}
    
    //if (u_sepia > 0.0){
    //    gl_FragColor = to_sepia(tc);
    //    return;
    //}
    
    vec4 color = tc*(v_diffuse);

    float shadow = 1.0;
        if (isInShadowArea() > 0.0)  {      
/*  
                float s = 0.3*shadowSimple(vec2(0.0));
                s += 0.175*shadowSimple(vec2(-d_tex, -d_tex));
                s += 0.175*shadowSimple(vec2(d_tex, -d_tex));
                s += 0.175*shadowSimple(vec2(-d_tex, d_tex));
                s += 0.175*shadowSimple(vec2(d_tex, d_tex));
*/

                shadow = (shadowSimple(vec2(0.0)) * 0.1) + 0.9;
        }

    color = mix(color, vec4(u_AmbientColor.rgb, 1.0), u_AmbientColor.a);
    color *= shadow;

    color.a = (alpha);//*u_alpha);

    float distance_from_cam = (gl_FragCoord.z/gl_FragCoord.w);
    float fog_density = distance_from_cam - u_fog_min_dist_from_camera;
    float fog = (fog_density / u_fog_gradient_depth);
    gl_FragColor = mix(u_fog_color, color, clamp(1.0-fog,0.05,1.0));

    gl_FragColor.a = u_alpha*alpha;
}
