precision highp float;
                        
uniform vec2 u_coverData;

varying vec4 v_Position;

void main()
{   
    if (abs(v_Position.y)<u_coverData[1]){
            discard;
    }
    gl_FragColor = vec4(0.0, 0.0, 0.0, u_coverData[0]);
}
