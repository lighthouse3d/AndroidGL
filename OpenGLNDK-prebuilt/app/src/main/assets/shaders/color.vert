#version 300 es

layout (std140) uniform Matrices {
	mat4 m_pvm;
	mat4 m_model;
	mat4 m_vm;
	mat3 m_normalM;
	mat3 m_normal;
};

in vec4 position;
in vec4 texCoord;

out vec4 texCoordV;


void main()
{
	texCoordV = texCoord;
	gl_Position = m_pvm * position ;
} 
