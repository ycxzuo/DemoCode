package com.yczuoxin.others.api.jdk.nio2;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Nio2Demo extends AbstractMessageSource implements ResourceLoaderAware {

    private static final String FILE_NAME = "default.properties";

    private static final String FILE_PATH = "/META-INF/" + FILE_NAME;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Resource resource;

    private final Properties properties;

    private ResourceLoader resourceLoader;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Nio2Demo() {
        this.resource = getFileResource();
        this.properties = getProperties(getPropertiesReader());
        this.resourceLoader = getResourceLoader();
        registerFileChangedEvent();
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String name = properties.getProperty("name");
        return new MessageFormat(name, locale);
    }

    private void registerFileChangedEvent() {
        FileSystem fileSystem = FileSystems.getDefault();
        try {
            File file = resource.getFile();
            Path filePath = file.toPath();
            Path filePathParent = filePath.getParent();
            WatchService watchService = fileSystem.newWatchService();
            filePathParent.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            watchFileChangedEvent(watchService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void watchFileChangedEvent(WatchService watchService) {
        executorService.submit(() -> {
            while (true) {
                WatchKey watchKey = watchService.take();
                try {
                    if (watchKey.isValid()) {
                        for (WatchEvent<?> pollEvent : watchKey.pollEvents()) {
                            Watchable watchable = watchKey.watchable();
                            Path changedPath = (Path) watchable;
                            Path changedRelativePath = (Path) pollEvent.context();
                            if (FILE_NAME.equals(changedRelativePath.getFileName().toString())) {
                                Path resolvedPath = changedPath.resolve(changedRelativePath);
                                File file = resolvedPath.toFile();
                                Properties newProperties = getProperties(new FileReader(file));
                                synchronized (this.properties) {
                                    this.properties.clear();
                                    this.properties.putAll(newProperties);
                                }
                            }

                        }
                    }
                } finally {
                    watchKey.reset();
                }
            }
        });
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResourceLoader getResourceLoader() {
        if (this.resourceLoader == null) {
            resourceLoader = new DefaultResourceLoader();
        }
        return resourceLoader;
    }

    private Properties getProperties(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private Reader getPropertiesReader() {
        EncodedResource encodedResource = new EncodedResource(this.resource, DEFAULT_CHARSET);
        try {
            return encodedResource.getReader();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private Resource getFileResource() {
        return getResourceLoader().getResource(FILE_PATH);
    }

    public static void main(String[] args) throws InterruptedException {
        Nio2Demo nio2Demo = new Nio2Demo();
        for (int i = 0; i < 10000; i++) {
            String name = nio2Demo.getMessage("name", new Object[]{}, Locale.CHINA);
            System.out.println(name);
            Thread.sleep(1000L);
        }

    }
}
