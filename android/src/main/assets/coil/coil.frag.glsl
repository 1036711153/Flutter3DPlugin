precision highp float;
varying vec3 vPosition;
varying  vec4 vColor;
uniform vec4 centreRr;//圆心xy与r

void main()
{
    float dis=distance(vPosition.xy,centreRr.xy);
    gl_FragColor=mix(vec4(vColor.xyz,0.0),mix(vColor,vec4(vColor.xyz,0.0),smoothstep(centreRr.z-3.,centreRr.z,dis)),smoothstep(centreRr.w,centreRr.w+3.0,dis));
}