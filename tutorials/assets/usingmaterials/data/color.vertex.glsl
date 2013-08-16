attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;

void main() { 
	gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}