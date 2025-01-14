package com.goody.utils.huangshan;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * util for image join together
 *
 * @author Goody
 * @version 1.0, 2023/09/14
 * @since 1.0.9
 */
public final class ImageJoinerUtil {

    private ImageJoinerUtil() {
    }

    /**
     * combine all images into one image
     *
     * @param files files
     * @return byteArray output stream
     */
    public static OutputStream imageCombineFiles(Collection<File> files) {
        return imageCombine(files.stream().map(is -> {
                try {
                    return new FileInputStream(is);
                } catch (FileNotFoundException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    /**
     * combine all images into one image
     *
     * @param files files
     * @param os    output stream
     * @return byteArray output stream
     */
    public static OutputStream imageCombineFiles(Collection<File> files, OutputStream os) {
        return imageCombine(files.stream().map(is -> {
                    try {
                        return new FileInputStream(is);
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()),
            os);
    }

    /**
     * combine all images into one image
     *
     * @param imageIss files
     * @return byteArray output stream
     */
    public static OutputStream imageCombine(Collection<InputStream> imageIss) {
        return imageCombine(imageIss, new ByteArrayOutputStream());
    }

    /**
     * 将输入密集拼接为一张图片
     * 密集拼接，首选n*n的形式。
     * <ol>
     *     <li>宽用数量开方向上取整，保证在不能完全开平方的时候宽多一个</li>
     *     <li>高用数量开方向下取整，保证在不能完全开平方的时候高度正常</li>
     *     <li>拼接使用最大宽和最大长按照上述宽高进行拼接。这种操作相对最为简单，否则需要严格计算当前xy</li>
     * </ol>
     * <p>
     * 比如:
     *
     * <pre>
     *     -------------image-------------
     *     aaa  bbbb cccccc d e
     *     aaa  bbbb        d
     *          bbbb        d
     *                      d
     *     -------------result--------------
     *     aaa___bbbb_cccccc
     *     aaa___bbbb_______
     *     ______bbbb_______
     *     _________________
     *     d_____e__________
     *     d________________
     *     d________________
     *     d________________
     *
     *
     * </pre>
     *
     * @param imageIss 图片输入流
     * @param os       输出流
     * @return 输出流
     */
    public static OutputStream imageCombine(Collection<InputStream> imageIss, OutputStream os) {
        try {
            // 读取所有图片
            final List<BufferedImage> images = imageIss.stream()
                .map(is -> {
                    try {
                        return ImageIO.read(is);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            final int size = images.size();

            // 计算最大长和宽
            final int imagesPerRow = (int) Math.ceil(Math.sqrt(size));
            final int imagesPerColumn = (int) Math.floor(Math.sqrt(size));

            // NOTE(goody): 2023/9/14 密集排序，9图改为w3*h3，10图改为w4*h3
            // 计算结果图片的宽度和高度
            int width = images.stream().mapToInt(BufferedImage::getWidth).max().orElse(0);
            int height = images.stream().mapToInt(BufferedImage::getHeight).max().orElse(0);

            // 创建一个新的BufferedImage，类型为TYPE_INT_RGB
            BufferedImage combined = new BufferedImage(width * imagesPerRow, height * imagesPerColumn, BufferedImage.TYPE_INT_RGB);

            // 使用Graphics2D绘制所有图片到新的BufferedImage
            Graphics2D g = (Graphics2D) combined.getGraphics();
            for (int j = 0; j < imagesPerColumn; j++) {
                for (int i = 0; i < imagesPerRow; i++) {
                    // 防止最后一行不满的时候超出范围
                    int index = i + (j * imagesPerRow);
                    if (index >= size) {
                        break;
                    }
                    final BufferedImage image = images.get(index);
                    int x = i * width;
                    int y = j * height;
                    g.drawImage(image, x, y, null);
                }
            }

            // 输出新的图片
            ImageIO.write(combined, "JPG", os);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // 原路返回
        return os;
    }
}
