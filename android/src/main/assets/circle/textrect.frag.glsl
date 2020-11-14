precision highp float;
uniform vec3 centreR;//圆心xy与r
uniform vec4 uColor;
varying vec3 vpos;
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;
void main()
{
      float dis=distance(vpos.xy,centreR.xy);
      if(dis<centreR.z){
            vec4 finalColor=uColor*vambient*1.5+uColor*vspecular*1.5+uColor*vdiffuse*1.5;
            gl_FragColor = mix(finalColor,vec4(finalColor.xyz,0.5),smoothstep(centreR.z-5.,centreR.z,dis));
      }
}