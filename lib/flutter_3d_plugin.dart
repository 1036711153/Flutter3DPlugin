
import 'dart:async';

import 'package:flutter/services.dart';

class Flutter3dPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_3d_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
