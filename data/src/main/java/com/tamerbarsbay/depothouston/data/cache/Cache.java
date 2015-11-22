package com.tamerbarsbay.depothouston.data.cache;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.serializer.JsonSerializer;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;

import java.io.File;

public class Cache {

    String SETTINGS_FILE_NAME = "com.tamerbarsbay.depothouston.SETTINGS";
    String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";
    long EXPIRATION_TIME = 60 * 10 * 1000;

    protected final Context context;
    protected final File cacheDir;
    protected final JsonSerializer serializer;
    protected final FileManager fileManager;
    protected final ThreadExecutor threadExecutor;

    public Cache(Context context, JsonSerializer serializer,
                 FileManager fileManager, ThreadExecutor executor) {
        if (context == null || serializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Cannot have null parameter.");
        }
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.serializer = serializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    public boolean isCached(String entityId, String fileNamePrefix) {
        File entityFile = this.buildFile(entityId, fileNamePrefix);
        return this.fileManager.exists(entityFile);
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }

    public synchronized void evictAll() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
    }

    protected File buildFile(String entityId, String fileNamePrefix) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(fileNamePrefix);
        fileNameBuilder.append(entityId);

        return new File(fileNameBuilder.toString());
    }

    protected void setLastCacheUpdateTimeMillis() {
        long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    protected long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    protected void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    protected static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    /**
     * {@link Runnable} class for evicting all the cached files
     */
    protected static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }

        @Override
        public void run() {
            this.fileManager.clearDirectory(this.cacheDir);
        }
    }

}