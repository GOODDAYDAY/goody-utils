package com.goody.utils.xueya.bean;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * application context, main class of Xueya
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
public class XueyaApplicationContext {
    private final Class<?> configClass;
    private final ConcurrentHashMap<String, BeanDefinition> nameBeanDefinition;
    private final ConcurrentHashMap<String, Object> nameSingletonPool;

    public XueyaApplicationContext(Class<?> configClass) {
        this.configClass = configClass;
        final String[] paths = this.componentScanPath(configClass);

        this.nameBeanDefinition = new ConcurrentHashMap<>();
        this.generateBeanDefinition(paths);

        this.nameSingletonPool = new ConcurrentHashMap<>();
        this.initializeSingletonPool();
    }

    /**
     * get bean by beanName
     *
     * @param name beanName
     * @return bean
     */
    public <T> T getBean(String name) {
        if (this.nameBeanDefinition.containsKey(name)) {
            final BeanDefinition beanDefinition = this.nameBeanDefinition.get(name);
            if (Scope.SINGLETON == beanDefinition.getScope()) {
                if (!this.nameSingletonPool.containsKey(name)) {
                    throw new NullPointerException("singleton bean not init yet");
                }
                return (T) this.nameSingletonPool.get(name);
            } else if (Scope.PROTOTYPE == beanDefinition.getScope()) {
                return (T) this.createBean(beanDefinition.getClazz());
            }
        }
        throw new NullPointerException("bean not exist");
    }

    /**
     * the input class must has the annotation {@link ComponentScan}.
     * <p>
     * get the path in annotation
     *
     * @param configClass config class with path
     * @return paths
     */
    private String[] componentScanPath(Class<?> configClass) {
        return configClass.getDeclaredAnnotation(ComponentScan.class).path();
    }

    /**
     * find all files in paths input.Find java class with annotation {@link Component} and generate {@link BeanDefinition}
     *
     * @param packagePaths paths
     */
    private void generateBeanDefinition(String[] packagePaths) {
        Arrays.stream(packagePaths)
                .flatMap(packagePath -> {
                    final String path = this.convertPackageToPath(packagePath);
                    final URL resource = this.configClass.getClassLoader().getResource(path);
                    if (null == resource) {
                        return Stream.empty();
                    }
                    final File file = new File(resource.getFile());
                    return this.generateBeanDefinition(file, packagePath);
                })
                .filter(Objects::nonNull)
                .forEach(beanDefinition -> this.nameBeanDefinition.put(beanDefinition.getName(), beanDefinition));
    }

    /**
     * generate the {@link BeanDefinition} with file input and the file's package path.
     *
     * <p> traverse all path with recursion
     *
     * @param file            classFile
     * @param filePackagePath file's package path
     * @return Stream
     */
    private Stream<BeanDefinition> generateBeanDefinition(File file, String filePackagePath) {
        // handle directory file, recurse the directory file and filePackagePath
        if (file.isDirectory()) {
            return Arrays.stream(file.listFiles()).flatMap(subFile -> this.generateBeanDefinition(subFile, this.generatePackagePath(filePackagePath, subFile.getName())));
        }
        // only .class file should be instantiated
        if (!file.getName().endsWith(".class")) {
            return Stream.empty();
        }
        // instantiate the class
        try {
            final Class<?> clazz = this.configClass.getClassLoader().loadClass(filePackagePath);
            final BeanDefinition beanDefinition = this.convertClassToBeanDefinition(clazz);
            return Stream.of(beanDefinition);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return Stream.empty();
    }

    /**
     * convert class with annotation {@link Component} to {@link BeanDefinition}.
     *
     * @param clazz class
     * @return beanDefinition
     */
    private BeanDefinition convertClassToBeanDefinition(Class<?> clazz) {
        // only init the class which has annotation component
        if (!clazz.isAnnotationPresent(Component.class)) {
            return null;
        }
        final Component component = clazz.getDeclaredAnnotation(Component.class);
        if (null == component) {
            return null;
        }
        // filter the duplicate beanDefinition
        final String beanName = component.value();
        if (this.nameBeanDefinition.contains(beanName)) {
            return null;
        }
        // init definition
        final BeanDefinition definition = new BeanDefinition();
        // set value
        definition.setName(beanName);
        definition.setClazz(clazz);
        definition.setScope(component.scope());
        return definition;
    }

    /**
     * initialize singleton pool, init instance of Class and wire field to avoid cycle dependencies
     */
    private void initializeSingletonPool() {
        // init instance value
        this.nameBeanDefinition.forEach((name, beanDefinition) -> {
            if (Scope.SINGLETON == beanDefinition.getScope()) {
                this.nameSingletonPool.put(name, this.instanceOf(beanDefinition.getClazz()));
            }
        });
        // wire fields
        this.nameSingletonPool.forEach((name, singletonBean) -> {
            this.wireField(singletonBean);
        });
    }

    /**
     * create instance by class,
     *
     * @param clazz class
     * @return instance
     */
    private Object createBean(Class<?> clazz) {
        // generate instance
        final Object object = this.instanceOf(clazz);
        // wire field
        this.wireField(object);
        return object;
    }

    /**
     * wire bean value to field
     *
     * @param object object
     */
    private void wireField(Object object) {
        if (null == object) {
            throw new NullPointerException("wire filed of null object");
        }
        try {
            // set bean value to field
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    final Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
                    final Object fieldBean;
                    // default "" means set value by class
                    if (null == autowired.value() || "".equals(autowired.value())) {
                        fieldBean = this.getBean(field.getName());
                    } else {
                        fieldBean = this.getBean(autowired.value());
                    }
                    field.setAccessible(true);
                    field.set(object, fieldBean);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * generate the instance of class
     *
     * @param clazz class
     * @return object
     */
    private Object instanceOf(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * the path input is packagePath. {@link ClassLoader} reads sources with path like com/goody
     *
     * @param packagePath path like com.goody
     * @return com/goody
     */
    private String convertPackageToPath(String packagePath) {
        return packagePath.replace(".", "/");
    }

    /**
     * assemble the package path with file
     *
     * @param packagePath packagePath like com.goody
     * @param fileName    file with suffix like XueYa.class
     * @return packagePath like com.goody.class
     */
    private String generatePackagePath(String packagePath, String fileName) {
        return String.format("%s.%s", packagePath, fileName.split("\\.")[0]);
    }
}
