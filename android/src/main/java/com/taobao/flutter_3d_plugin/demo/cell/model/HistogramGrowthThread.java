package com.taobao.flutter_3d_plugin.demo.cell.model;


import com.taobao.flutter_3d_plugin.demo.cell.HistogramUI;

public class HistogramGrowthThread extends Thread {
    private float runTime = 800;
    private float startTime = 0;
    public volatile boolean flag = true;
    private HistogramUI[] histogramUIs;

    public HistogramGrowthThread(HistogramUI[] histogramUIs) {
        this.histogramUIs = histogramUIs;
    }

    @Override
    public void run() {
        while (flag) {
            startTime += 16;
            for (HistogramUI ui : histogramUIs) {
                ui.cylinederUI.cylinderSideUI.h = ui.h * (startTime / runTime);
                ui.cylinederUI.circleUI.h = ui.h * (startTime / runTime);
            }
            if (startTime >= runTime) {
                for (HistogramUI ui : histogramUIs) {
                    ui.textUI.uHideText = 0;
                }
                flag = false;
            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
