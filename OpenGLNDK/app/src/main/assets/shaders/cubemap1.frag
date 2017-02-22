#version 300 es

precision mediump float;
precision mediump samplerCube;

uniform samplerCube texUnit;

in vec3 normalV;
in vec3 eyeV;

out vec4 color1;


void main() {

	vec3 n = normalize(normalV);
	vec3 e = normalize(eyeV);

	// Reflection vector provides texture coordinates
	vec3 t = reflect(e, n);

	color1 = texture(texUnit, t);
}

