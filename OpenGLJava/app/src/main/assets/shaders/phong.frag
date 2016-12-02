#version 300 es

precision mediump float;

uniform vec4 uVec4Diffuse;
uniform vec4 uVec4Specular;
uniform float uFloatShininess;

float uFloatAmbient = 0.25;

uniform sampler2D uSampler2D;
in vec3 normal;
in vec3 eyeCoord;
in vec3 ld;
in vec2 tc;

out vec4 colorOut;
            
void main() {

    vec4 spec = vec4(0.0);

    vec3 n = normalize(normal);
    vec3 e = normalize(eyeCoord);

    float intensity = max(dot(n,ld), 0.0);

    if (intensity > 0.0) {
    
        vec3 h = normalize(ld + e);
        float intSpec = max(dot(h,n), 0.0);
        spec = uVec4Specular * pow(intSpec, uFloatShininess);
    }

    vec4 dif = uVec4Diffuse * texture(uSampler2D, tc);
    colorOut = max(vec4(intensity, intensity, intensity, 1.0) * dif + spec , dif * vec4(uFloatAmbient, uFloatAmbient, uFloatAmbient, 1.0));
}