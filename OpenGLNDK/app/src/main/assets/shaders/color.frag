#version 300 es

precision mediump float;

uniform sampler2D texUnit;

in vec4 texCoordV;

out vec4 outputF;

void main() {
	outputF = texture(texUnit, texCoordV.xy);
}