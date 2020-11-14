import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

const MethodChannel _channel = const MethodChannel('flutter_3d_plugin');

class Flutter3dPlugin {
  int textureId;

  bool get isInit => textureId != null;

  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<int> init(
      {@required width,
      @required height,
      fps = 60,
      isRenderModeContinuously = true}) async {
    textureId = await _channel.invokeMethod('init', {
      'width': width,
      'height': height,
      'fps': fps,
      'isRenderModeContinuously': isRenderModeContinuously
    });
    return textureId;
  }

  Future<bool> dispose() async {
    final bool result =
        await _channel.invokeMethod('dispose', {'textureId': textureId});
    if (result) {
      textureId = null;
    }
    return result;
  }
}
