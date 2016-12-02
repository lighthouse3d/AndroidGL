#version 300 es

uniform mat4 uMat4PVM;   
uniform mat3 uMat3Normal;
uniform mat4 uMat4ViewModel;
uniform mat4 uMat4View;


vec4 LightDir = vec4(1.0f,1.0f,1.0f, 0.0f);

in vec4 aVec4Position;  
in vec3 aVec3Normal;
in vec2 aVec2TexCoord;

out vec3 normal;
out vec3 eyeCoord;
out vec3 ld;
out vec2 tc;

void main() {

    tc = aVec2TexCoord;
    normal = normalize(uMat3Normal * aVec3Normal);
    eyeCoord = -vec3(uMat4ViewModel * aVec4Position);
    ld = normalize(vec3(uMat4View * LightDir));
	gl_Position = uMat4PVM * aVec4Position;
}
