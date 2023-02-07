uniform vec2 u_targetSizeXY;

attribute vec4 a_Position;  
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position;

varying vec2 blurTextCoords[11];

void main()                    
{               
    v_Position = a_Position;
    v_TextureCoordinates = a_TextureCoordinates;          
    gl_Position = a_Position;  
    
    vec2 centerCoords = a_Position.xy*0.5+0.5;
    
    if (u_targetSizeXY.y<0.0) {
        float pixelSize = 1.0/u_targetSizeXY.x;
        
        for (int i=-5; i<=5;i++){
                blurTextCoords[i+5] = centerCoords + vec2(pixelSize*float(i),0.0);
        }
    }
    
    if (u_targetSizeXY.x<0.0) {
        float pixelSize = 1.0/u_targetSizeXY.y;
        
        for (int i=-5; i<=5;i++){
                blurTextCoords[i+5] = centerCoords + vec2(0.0,pixelSize*float(i));
        }
    }   

}          
