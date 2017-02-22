#version 300 es

precision mediump float;

layout (std140) uniform Material {
	vec4 diffuse;
	vec4 ambient;
	vec4 specular;
	vec4 emissive;
	float shininess;
	int texCount;
};


in vec3 normalV;
in vec2 texCoordV;
in vec3 eyeV;


uniform vec3 lightDir;
uniform sampler2D texUnit;

out vec4 colorOut;

void main() {

	vec4 dif;
	vec4 spec;

	float intensity = max(dot(normalize(normalV),lightDir), 0.3);

	vec3 h = normalize(lightDir + normalize(eyeV));
	float intSpec = max(dot(h,normalize(normalV)), 0.0);
	spec = specular * pow(intSpec,100.0f);
	dif = diffuse;

	if (texCount != 0)
		dif = dif * texture(texUnit, texCoordV);
	colorOut = intensity * dif + spec + emissive;
	//colorOut=diffuse;
}
