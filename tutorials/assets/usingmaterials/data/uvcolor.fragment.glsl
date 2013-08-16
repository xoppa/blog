#ifdef GL_ES 
precision mediump float;
#endif
 
uniform vec3 u_colorU;
uniform vec3 u_colorV;
 
varying vec2 v_texCoord0;
 
void main() {
    gl_FragColor = vec4(v_texCoord0.x * u_colorU + v_texCoord0.y * u_colorV, 1.0);
}