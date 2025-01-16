package com.goody.utils.huangshan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @author Haotian
 * @version 1.0, 2025/1/13
 */
public class PathUtil {

    public static List<String> getPathsFromResource(String resourcePath) throws IOException, URISyntaxException {
        List<String> fileNames = new ArrayList<>();
        URL url = PathUtil.class.getResource(resourcePath);
        if (url == null) {
            throw new IOException("资源路径不存在: " + resourcePath);
        }

        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            // 处理文件系统中的资源
            java.nio.file.Path path = java.nio.file.Paths.get(url.toURI());
            java.nio.file.Files.list(path).forEach(p -> fileNames.add(p.getFileName().toString()));
        } else if ("jar".equals(protocol)) {
            // 处理 JAR 文件中的资源
            String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
            JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(resourcePath.substring(1)) && !entry.isDirectory()) {
                    fileNames.add(name.substring(resourcePath.length()));
                }
            }
            jarFile.close();
        } else {
            throw new IOException("不支持的协议: " + protocol);
        }

        return fileNames;
    }

    public static List<String> getAbsolutePathsFromResource(String resourcePath) throws IOException, URISyntaxException {
        List<String> absolutePaths = new ArrayList<>();
        URL url = PathUtil.class.getResource(resourcePath);
        if (url == null) {
            return Collections.emptyList();
        }

        if ("file".equals(url.getProtocol())) {
            // 处理文件系统中的资源
            Path path = Paths.get(url.toURI());
            try (Stream<Path> paths = java.nio.file.Files.list(path)) {
                absolutePaths = paths
                    .filter(Files::isRegularFile) // 过滤掉文件夹
                    .map(Path::toAbsolutePath) // 获取绝对路径
                    .map(Path::toString) // 转换为字符串
                    .collect(Collectors.toList());
            }
        } else if ("jar".equals(url.getProtocol())) {
            // JAR 文件中的资源没有直接的文件系统路径，但可以输出 JAR 文件的路径
            String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
            absolutePaths.add(jarPath);
        } else {
            throw new IOException("不支持的协议: " + url.getProtocol());
        }

        return absolutePaths;
    }
}
