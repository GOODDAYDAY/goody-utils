package com.goody.utils.huangshan;

import lombok.Getter;
import org.opencv.core.Mat;

@Getter
public class AutoCloseableMat implements AutoCloseable {
    private final Mat mat;

    public AutoCloseableMat() {
        this.mat = new Mat();
    }

    public AutoCloseableMat(Mat mat) {
        this.mat = mat;
    }

    @Override
    public void close() {
        if (mat != null) {
            mat.release();
        }
    }
}
