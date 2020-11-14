import 'package:flutter/material.dart';
import 'package:flutter_3d_plugin/flutter_3d_plugin.dart';

class Flutter3DWidget extends StatefulWidget {
  double width;
  double height;
  int fps;

  Flutter3DWidget({@required this.width, @required this.height, this.fps = 60});

  @override
  _Flutter3DWidgetState createState() => _Flutter3DWidgetState();
}

class _Flutter3DWidgetState extends State<Flutter3DWidget> {
  Flutter3dPlugin plugin = new Flutter3dPlugin();

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    initTexture();
  }

  void initTexture() async {
    await plugin.init(
        width: widget.width, height: widget.height, fps: widget.fps);
    setState(() {});
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    plugin.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: widget.width,
      height: widget.height,
      child: plugin.isInit
          ? Texture(textureId: plugin.textureId)
          : Container(
              color: Colors.white,
            ),
    );
  }
}
