#version 300 es

layout (std140) uniform Matrices {
	mat4 projModelViewMatrix;
	mat4 projectionMatrix;
	mat4 modelMatrix;
	mat4 viewMatrix;
	mat4 modelViewMatrix;
	mat3 normalMatrix;
	mat3 normalViewMatrix;
};


uniform vec3 lightDir;

in vec4 position;
in vec2 texCoord;
in vec3 normal;


in vec3 normalV;
in vec2 texCoordV;
in vec3 eyeV;


void main () {
	
	texCoordV = texCoord;

	normalV = normalize(normalMatrix * normal);


	vec3 pos = vec3(modelViewMatrix * position);
	eyeV = normalize(-pos);

	gl_Position = projModelViewMatrix * position;	
}