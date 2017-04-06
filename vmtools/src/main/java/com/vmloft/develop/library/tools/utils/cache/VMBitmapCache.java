package com.vmloft.develop.library.tools.utils.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by lzan13 on 2016/3/28.
 * 图片资源缓存类，实现加载图片到内存的缓存
 */
public class VMBitmapCache {

    private static VMBitmapCache instance;
    private LruCache<String, Bitmap> cache = null;


    /**
     * 构造函数，初始化缓存
     */
    private VMBitmapCache() {
        cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 获取当前对象的单例实例
     *
     * @return 返回当前类的实例
     */
    public static synchronized VMBitmapCache getInstance() {
        if (instance == null) {
            instance = new VMBitmapCache();
        }
        return instance;
    }

    /**
     * 将Bimap对象添加进缓存中
     *
     * @param key    保存Bitmap对象到缓存的Key
     * @param bitmap 需要保存到缓存中的bitmap
     * @return 返回保存成功的Bitmap对象
     */
    public Bitmap putBitmap(String key, Bitmap bitmap) {
        return cache.put(key, bitmap);
    }

    /**
     * 根据Key 获取还从中的Bitmap对象
     *
     * @param key 需要获取的缓存Bitmap的Key
     * @return 返回需要获取的Bitmap
     */
    public Bitmap optBitmap(String key) {
        return cache.get(key);
    }

}
