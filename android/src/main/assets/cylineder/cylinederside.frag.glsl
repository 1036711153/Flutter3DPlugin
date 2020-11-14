precision highp float;
varying vec3 vPosition;
uniform vec4 uColor;
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;
void main()
{
    gl_FragColor = uColor*vambient*1.2+uColor*vspecular*1.2+uColor*vdiffuse*1.2;
}