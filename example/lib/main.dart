import 'package:flutter/material.dart';

import 'package:flutter_3d_plugin_example/flutter_3d_demo.dart';

void main() {
  runApp(new MaterialApp(
    home: MyApp(),
  ));
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Widget _buildListTile(BuildContext context, String label, Widget pageWidget) {
    return GestureDetector(
      onTap: () {
        Navigator.of(context)
            .push(new MaterialPageRoute(builder: (BuildContext context) {
          return pageWidget;
        }));
      },
      child: ListTile(
        title: Text(label),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView(
          children: <Widget>[
            _buildListTile(context, 'Flutter 3D Demo', new Flutter3DDemo()),
          ],
        ),
    );
  }
}
