import 'package:flutter/material.dart';
import 'package:flutter_3d_plugin/flutter_3d_widget.dart';

class Flutter3DDemo extends StatefulWidget {
  @override
  _Flutter3DDemoState createState() => _Flutter3DDemoState();
}

class _Flutter3DDemoState extends State<Flutter3DDemo> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Flutter 3D Demo'),),
      body: Center(
        child: Column(
          children: [
            Container(
              margin: EdgeInsets.all(10),
              child: Flutter3DWidget(
                width: 300,
                height: 200,
                fps: 90,
              ),
            ),
            Container(
              margin: EdgeInsets.all(10),
              child: Flutter3DWidget(
                width: 300,
                height: 200,
                fps: 90,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
