package com.goody.utils.huangshan;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * 校验图片是否存在目标模板类
 *
 * @author Haotian
 * @version 1.0, 2025/1/10
 */
@Slf4j
public class ImageRecognitionUtil {

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        String libPath = null;

        if (osName.contains("win")) {
            // Windows 系统。该dll有50MB，较大，后续可以考虑删除
            libPath = extractOpenCVLibrary("opencv_java490.dll");
        } else if (osName.contains("linux")) {
            // Linux 系统
            libPath = extractOpenCVLibrary("libopencv_java490.so");
        } else {
            // 不能因为不支持mac就不允许启动
            log.warn("mac系统暂不支持opencv");
        }

        if (null == libPath) {
            log.warn("opencv-lib装载失败");
        } else {
            System.load(libPath);
        }
    }

    public static boolean matchWechatGroupByPath(String srcPath) {
        try (
            // 读取指定图片和目标图片，此处创建对象，保证对象必须存在，下文match对内容判空
            AutoCloseableMat src = new AutoCloseableMat(Imgcodecs.imread(srcPath));
            AutoCloseableMat template = new AutoCloseableMat(loadTemplate());
        ) {
            return match(src.getMat(), template.getMat());
        }
    }

    public static boolean matchWechatGroupByUrl(String srcUrl) {
        try (
            // 读取指定图片和目标图片
            AutoCloseableMat src = new AutoCloseableMat(matFromUrl(srcUrl));
            AutoCloseableMat template = new AutoCloseableMat(loadTemplate());
        ) {
            return match(src.getMat(), template.getMat());
        }
    }

    public static boolean matchByPath(String srcPath, String templatePath) {
        try (
            // 读取指定图片和目标图片
            AutoCloseableMat src = new AutoCloseableMat(Imgcodecs.imread(srcPath));
            AutoCloseableMat template = new AutoCloseableMat(Imgcodecs.imread(templatePath));
        ) {
            return match(src.getMat(), template.getMat());
        }
    }

    public static boolean matchGroupByUrl(String srcUrl, String templatePath) {
        try (
            // 读取指定图片和目标图片
            AutoCloseableMat src = new AutoCloseableMat(matFromUrl(srcUrl));
            AutoCloseableMat template = new AutoCloseableMat(Imgcodecs.imread(templatePath));
        ) {
            return match(src.getMat(), template.getMat());
        }
    }

    public static boolean match(Mat src, Mat template) {
        if (null == src || null == template) {
            return false;
        }
        for (double scaleValue = 0.2; scaleValue < 1.2; scaleValue += 0.05) {
            try (
                // 缩放模板
                AutoCloseableMat autoCloseableResizedTemplate = new AutoCloseableMat();
                AutoCloseableMat autoCloseableResult = new AutoCloseableMat();
            ) {
                Mat resizedTemplate = autoCloseableResizedTemplate.getMat();
                Imgproc.resize(template, resizedTemplate, new Size(), scaleValue, scaleValue);

                // 检查缩放后的模板尺寸是否小于源图像尺寸
                if (resizedTemplate.rows() > src.rows() || resizedTemplate.cols() > src.cols()) {
                    continue;
                }

                final boolean match = isMatch(autoCloseableResult.getMat(), src, resizedTemplate);
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isMatch(Mat result, Mat src, Mat resizedTemplate) {
        // 设置匹配阈值
        double threshold = 0.9;
        // 多尺度匹配
        boolean found = false;
        double maxVal = 0;

        // 创建匹配结果矩阵
        int result_cols = src.cols() - resizedTemplate.cols() + 1;
        int result_rows = src.rows() - resizedTemplate.rows() + 1;
        result.create(result_rows, result_cols, CvType.CV_32FC1);

        // 进行模板匹配
        Imgproc.matchTemplate(src, resizedTemplate, result, Imgproc.TM_CCOEFF_NORMED);

        // 查找匹配结果中的最大值位置
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double currentMaxVal = mmr.maxVal;

        // 更新最佳匹配结果
        if (currentMaxVal > maxVal) {
            maxVal = currentMaxVal;
            found = true;
        }

        // 判断是否匹配成功
        return found && maxVal >= threshold;
    }

    private static Mat matFromUrl(String url) {
        try {
            URL imageUrl = new URL(url);
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] byteArray = baos.toByteArray();
            return Imgcodecs.imdecode(new MatOfByte(byteArray), Imgcodecs.IMREAD_COLOR);
        } catch (IOException e) {
            log.warn("url资源下载失败 {}", url, e);
            return null;
        }
    }

    public static Mat loadTemplate() {
        try {
            return matFromJar("/template/group-detail@3x.png");
        } catch (IOException e) {
            log.warn("Template资源未找到", e);
            return null;
        }
    }

    public static Mat matFromJar(String resourcePath) throws IOException {
        // 使用类加载器获取资源的输入流
        try (InputStream inputStream = ImageRecognitionUtil.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                log.warn("资源未找到 {}", resourcePath);
                return null;
            }

            // 将 InputStream 转换为 byte 数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // 使用 OpenCV 的 imdecode 方法从 byte 数组中读取图像
            MatOfByte mob = new MatOfByte(imageBytes);
            Mat image = Imgcodecs.imdecode(mob, Imgcodecs.IMREAD_COLOR);

            if (image.empty()) {
                log.warn("无法解码图像 {}", resourcePath);
                return null;
            }
            return image;
        }
    }

    private static String extractOpenCVLibrary(String libName) {
        URL libUrl = ImageRecognitionUtil.class.getResource("/opencv/" + libName);
        if (libUrl == null) {
            log.warn("lib 资源未找到 {}", libName);
            return null;
        }

        try {
            Path tempDir = Files.createTempDirectory("native-libs-");
            Path libFile = tempDir.resolve(libName);

            try (InputStream in = libUrl.openStream();
                 FileOutputStream out = new FileOutputStream(libFile.toFile())) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            // 设置可执行权限
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows系统
                File file = libFile.toFile();
                file.setExecutable(true, false);
            } else {
                // Unix/Linux系统
                Files.setPosixFilePermissions(libFile, PosixFilePermissions.fromString("rwxr-xr-x"));
            }
            return libFile.toAbsolutePath().toString();
        } catch (Exception e) {
            log.warn("opencv装载失败 {}", libName, e);
        }
        return null;
    }
}
