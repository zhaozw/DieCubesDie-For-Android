//use medium precision
precision mediump float;

//the input texture
uniform sampler2D u_Texture;

//the texture
varying vec2 v_texCoord;
        
void main() {

    //get the texture colour
    vec4 texCol = texture2D(u_Texture, v_texCoord);
    
    //outline
    if (texCol.r < 0.4 && texCol.g < 0.4 && texCol.b > 0.9) {
        
        texCol.r = 0.0;
        texCol.g = 0.0;
        texCol.b = 0.0;
        texCol.a = 1.0;
    }
    //background
    else {
    
        texCol.r = 0.0;
        texCol.g = 0.0;
        texCol.b = 0.0;
        texCol.a = 0.0;
    }
    
    //set the colour
    gl_FragColor = texCol;
}