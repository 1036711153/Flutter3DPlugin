precision highp float;
varying vec2 vPosition;
varying vec4 vattrs; //centrex ,centreY ,r ,alpha

uniform vec4 ucolor;
void main()
{
     float d=distance(vPosition.xy,vattrs.xy);
     if(d<vattrs.z){
       gl_FragColor = mix(ucolor,vec4(ucolor.xyz,0.0),clamp((d)/(vattrs.z),0.0,1.0));//给此片元颜色值
     }
}