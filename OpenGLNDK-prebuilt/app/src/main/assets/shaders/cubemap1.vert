#version 300 es

layout (std140) uniform Matrices {
	mat4 m_pvm;
	mat4 m_model;
	mat4 m_vm;
	mat3 m_normalM;
	mat3 m_normal;
};

uniform vec3 camPosWorld;

in vec4 position;
in vec3 normal;

out vec3 normalV;
out vec3 eyeV;


void main () {
	
	normalV = normalize(m_normalM * normal);

	vec3 pos = vec3(m_model * position);
	eyeV = pos - camPosWorld;

	gl_Position = m_pvm * position;	
}