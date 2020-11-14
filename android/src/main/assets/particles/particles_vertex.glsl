uniform mat4 uMVPMatrix;
attribute vec2 aPosition;
attribute vec4 attrs;
varying  vec2 vPosition;
varying  vec4 vattrs;
void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,0,1);
   vPosition= aPosition;
   vattrs=attrs;
}