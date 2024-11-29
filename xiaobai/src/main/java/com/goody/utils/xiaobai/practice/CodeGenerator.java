package com.goody.utils.xiaobai.practice;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * code generator
 *
 * @author Goody
 * @version 1.0, 2024/11/29
 * @since 1.0.0
 */
public class CodeGenerator {

    /**
     * the main method to generate by freemarker
     *
     * @param directoryPath template's direction
     * @param templatePath  template
     * @param values        map value
     * @param outputPath    output path
     * @throws Exception
     */
    private static void freemarkerGenerate(String directoryPath, String templatePath, Map<String, Object> values, String outputPath) throws Exception {
        // create cfg
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setDirectoryForTemplateLoading(new File(directoryPath)); // 指定模板文件的路径

        // load template
        final Template template = cfg.getTemplate(templatePath);


        // output
        try (Writer out = new FileWriter(outputPath)) {
            template.process(values, out);
        }
    }

    /**
     * generate by class info
     *
     * @param clazz class info
     * @return map value
     */
    private static Map<String, Object> getInternalVariablesInfo(Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            final Class<?> type = field.getType();
            final String name = field.getName();
            final String simpleName = type.getSimpleName();
            // TODO(goody): 2024/11/29 impl biz
        }
        Map<String, Object> map = new HashMap<>();
        // TODO(goody): 2024/11/29 impl biz
        return map;
    }

    public static void main(String[] args) throws Exception {
        final Class<?>[] clazzs = new Class[]{};
        for (Class<?> clazz : clazzs) {
            final Map<String, Object> internalVariablesInfo = getInternalVariablesInfo(clazz);
            final String directoryPath = "xiaobai/src/main/resources";
            final String templatePath = "template.ftl";
            final String outputPath = String.format("xiaobai/src/main/resources/template/%s.java", clazz.getSimpleName());
            freemarkerGenerate(directoryPath, templatePath, internalVariablesInfo, outputPath);
        }
    }
}
