package com.poho.stuup.growth;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GrowthFactory {

    private static final String GROWTH_PACKAGE = "com.poho.stuup.growth";

    private final ClassLoader classLoader = getClass().getClassLoader();

    private List<Class<? extends GrowthStrategy<?>>> growthStrategyList;

    public GrowthFactory() {
        init();
    }

    public static GrowthFactory getInstance() {
        return GrowthFactoryInstance.instance;
    }

    /**
     * 根据传入的code创建对应的策略
     */
    public GrowthStrategy<?> createGrowthStrategy(String handle) {
        for (Class<? extends GrowthStrategy<?>> aClass : growthStrategyList) {
            RecCode recCodeAnnotation = getRecCodeAnnotation(aClass);
            if (recCodeAnnotation.code().equals(handle)) {
                try {
                    return aClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("策略获取失败失败");
                }
            }
        }
        throw new RuntimeException("策略获取失败失败");
    }

    /*
     *获取策略的枚举
     */
    private RecCode getRecCodeAnnotation(Class<? extends GrowthStrategy<?>> aClass) {
        return aClass.getAnnotation(RecCode.class);
    }

    //在工厂初始化时要初始化策略列表
    private void init() {
        growthStrategyList = new ArrayList<>();
        File[] resources = getResources();
        Class<GrowthStrategy<?>> growthStrategyClass;

        try {
            growthStrategyClass = (Class<GrowthStrategy<?>>) classLoader.loadClass(GrowthStrategy.class.getName());  //使用相同的加载器加载策略接口
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到策略接口");
        }

        int length = resources.length;

        for (File resource : resources) {
            try {
                Class<?> aClass = classLoader.loadClass(GROWTH_PACKAGE + "." + resource.getName().replace(".class", ""));
                //判断是否是 GrowthStrategy 的实现类并且不是 GrowthStrategy 它本身，满足的话加入到策略列表
                if (GrowthStrategy.class.isAssignableFrom(aClass) && aClass != growthStrategyClass) {
                    growthStrategyList.add((Class<? extends GrowthStrategy<?>>) aClass);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取扫描包下的所有class文件
     */
    private File[] getResources() {
        try {
            File file = new File(Objects.requireNonNull(classLoader.getResource(GROWTH_PACKAGE.replace(".", "/"))).toURI());
            return file.listFiles(pathname -> {
                return pathname.getName().endsWith(".class"); // 只扫描class文件
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException("未找到策略资源");
        }
    }

    private static class GrowthFactoryInstance {
        // 单例
        private static final GrowthFactory instance = new GrowthFactory();
    }
}
