package com.goody.utils.huangshan;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static com.goody.utils.huangshan.PathUtil.getAbsolutePathsFromResource;

/**
 * TODO
 *
 * @author Haotian
 * @version 1.0, 2025/1/10
 */
public class ImageRecognitionUtil2 {
    static int i = 1;

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        String libPath = "";

        if (osName.contains("win")) {
            // Windows 系统
            libPath = ImageRecognitionUtil2.class.getResource("/opencv/opencv_java490.dll").getPath();
        } else if (osName.contains("linux")) {
            // Linux 系统
            libPath = ImageRecognitionUtil2.class.getResource("/opencv/libopencv_java490.so").getPath();
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + osName);
        }

        System.load(libPath);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String[] templates = new String[]{"D:\\goody-utils\\huangshan\\src\\main\\resources\\template\\group-detail@3x.png"};
        String[] paths = new String[]{"/nologo", "/haveLogo"};

        for (final String template : templates) {
            for (final String pathss : paths) {
                // 读取指定图片和目标图片
                for (final String path : getAbsolutePathsFromResource(pathss)) {
                    final boolean match = matchByPath(path, template);
                    if (match) {
                        System.out.println(path);
                        System.out.println(template);
                        System.out.println();
                    }
                }
            }
        }
        System.out.println(matchByUrl("https://dev-cdn-common.bcmcdn.com/dev/403/20250114105013", "D:\\goody-utils\\huangshan\\src\\main\\resources\\template\\group-detail@3x.png"));
    }

    public static boolean matchByPath(String srcPath) {
        // 读取指定图片和目标图片
        Mat src = Imgcodecs.imread(srcPath);
        Mat template = null;
        try {
            template = matFromJar("/template/group-detail@3x.png");
            return match(src, template);
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean matchByPath(String srcPath, String templatePath) {
        // 读取指定图片和目标图片
        Mat src = Imgcodecs.imread(srcPath);
        Mat template = Imgcodecs.imread(templatePath);
        return match(src, template);
    }

    public static boolean matchByUrl(String srcUrl) {
        // 读取指定图片和目标图片
        Mat src = matFromUrl(srcUrl);
        if (src == null) {
            return false;
        }
        Mat template = null;
        try {
            template = matFromJar("/template/group-detail@3x.png");
            return match(src, template);
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean matchByUrl(String srcUrl, String templatePath) {
        // 读取指定图片和目标图片
        Mat src = matFromUrl(srcUrl);
        if (src == null) {
            return false;
        }
        Mat template = Imgcodecs.imread(templatePath);
        return match(src, template);
    }

    public static boolean match(Mat src, Mat template) {
        for (double scaleValue = 0.2; scaleValue < 1.2; scaleValue += 0.05) {
            // 缩放模板
            Mat resizedTemplate = new Mat();
            Imgproc.resize(template, resizedTemplate, new Size(), scaleValue, scaleValue);

            // 检查缩放后的模板尺寸是否小于源图像尺寸
            if (resizedTemplate.rows() > src.rows() || resizedTemplate.cols() > src.cols()) {
                continue;
            }

            final boolean match = isMatch(src, resizedTemplate, scaleValue);

            if (match) {
                // 释放资源
                src.release();
                template.release();
                return true;
            }
        }
        // 释放资源
        src.release();
        template.release();
        return false;
    }

    private static boolean isMatch(Mat src, Mat resizedTemplate, double scaleValue) {
        // 设置匹配阈值
        double threshold = 0.9;
        // 多尺度匹配
        boolean found = false;
        double maxVal = 0;
        double scale = 1.0;

        Point maxLoc = new Point();
        // 创建匹配结果矩阵
        Mat result = new Mat();
        int result_cols = src.cols() - resizedTemplate.cols() + 1;
        int result_rows = src.rows() - resizedTemplate.rows() + 1;
        result.create(result_rows, result_cols, CvType.CV_32FC1);

        // 进行模板匹配
        Imgproc.matchTemplate(src, resizedTemplate, result, Imgproc.TM_CCOEFF_NORMED);

        // 查找匹配结果中的最大值位置
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double currentMaxVal = mmr.maxVal;
        Point currentMaxLoc = mmr.maxLoc;

        // 释放资源
        result.release();
        resizedTemplate.release();

        // 更新最佳匹配结果
        if (currentMaxVal > maxVal) {
            maxVal = currentMaxVal;
            maxLoc = currentMaxLoc;
            scale = scaleValue;
            found = true;
        }

        // 判断是否匹配成功
        if (found && maxVal >= threshold) {
            System.out.println("目标图片在指定图片中，位置为: (" + maxLoc.x + ", " + maxLoc.y + ")");
            System.out.println("匹配值: " + maxVal + ", 缩放比例: " + scale);        // 计算矩形框的右下角坐标
            Point bottomRight = new Point(maxLoc.x + resizedTemplate.cols(), maxLoc.y + resizedTemplate.rows());

            // 绘制矩形框
            Imgproc.rectangle(src, maxLoc, bottomRight, new Scalar(0, 255, 0), 2);

            // 保存结果图片到本地
            String resultImagePath = "huangshan/src/main/resources/output/image" + ++i + ".jpg"; // 修改为你想要保存的路径
            Imgcodecs.imwrite(resultImagePath, src);

            return true;
        } else {
            return false;
        }
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
            e.printStackTrace();
            return null;
        }
    }

    public static Mat matFromJar(String resourcePath) throws IOException {
        // 使用类加载器获取资源的输入流
        InputStream inputStream = ImageRecognitionUtil2.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("资源未找到: " + resourcePath);
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
        Mat image = Imgcodecs.imdecode(mob, Imgcodecs.IMREAD_UNCHANGED);

        if (image.empty()) {
            throw new IOException("无法解码图像: " + resourcePath);
        }

        return image;
    }
}
