uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec4 aColor;

varying vec3 vPosition;
varying  vec4 vColor;
void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vColor = aColor;
   vPosition = aPosition;
}