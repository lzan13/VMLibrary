package com.vmloft.develop.library.tools.utils.bitmap

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.utils.VMFile
import com.vmloft.develop.library.tools.utils.VMFile.cacheFromSDCard
import com.vmloft.develop.library.tools.utils.VMFile.createDirectory
import com.vmloft.develop.library.tools.utils.VMFile.getPath
import com.vmloft.develop.library.tools.utils.logger.VMLog
import java.io.*


/**
 * Created by lzan13 on 2016/1/11.
 * 图片处理类，压缩，转换
 */
object VMBitmap {

    /**
     * 将Bitmap 转为 base64 字符串
     *
     * @param bitmap 需要转为Base64 字符串的Bitmap对象
     * @return 返回转换后的字符串
     */
    fun bitmap2Str(bitmap: Bitmap): String? {
        var result: String? = null
        val baos = ByteArrayOutputStream()
        bitmap.compress(PNG, 100, baos)
        val b = baos.toByteArray()
        result = Base64.encodeToString(b, Base64.DEFAULT)
        return result
    }

    /**
     * 将 base64 的字符串 转为 Bitmap
     *
     * @param imageData 需要转为Bitmap的 Base64 字符串
     * @return 转为Bitmap的对象
     */
    fun str2Bitmap(imageData: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val decode = Base64.decode(imageData, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size)
        return bitmap
    }

    /**
     * 获取图片文件的缩略图
     *
     * @param path      图片文件路径
     * @param dimension 设置缩略图最大尺寸
     * @return 返回压缩后的缩略图
     */
    fun loadBitmapThumbnail(path: Any?, dimension: Int = 256): Bitmap? {
        return loadBitmapByFile(path, dimension)
    }

    /**
     * 通过文件加载图片，这里也可以加载大图，保证不会出现 OOM，
     *
     * @param path      要压缩的图片路径
     * @param dimension 定义压缩后的最大尺寸
     * @return 返回经过压缩处理的图片
     */
    fun loadBitmapByFile(path: Any?, dimension: Int = 2048): Bitmap? {
        val bitmap = compressByDimension(path, dimension) ?: return null
        return compressBitmapByMatrix(bitmap, dimension)
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view 需要保存图像的 View 控件
     * @return 返回保存的 Bitmap 图像
     */
    fun loadCacheBitmapFromView(view: View): Bitmap? {
        val drawingCacheEnabled = true
        view.isDrawingCacheEnabled = drawingCacheEnabled
        view.buildDrawingCache(drawingCacheEnabled)
        val drawingCache = view.drawingCache
        val bitmap: Bitmap?
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache)
            view.isDrawingCacheEnabled = false
        } else {
            bitmap = null
        }
        return bitmap
    }

    /**
     * 获取最佳缩放比例
     *
     * @param actualWidth  Bitmap的实际宽度
     * @param actualHeight Bitmap的实际高度
     * @param dimension    定义压缩后最大尺寸
     * @return 返回最佳缩放比例
     */
    fun getZoomScale(actualWidth: Int, actualHeight: Int, dimension: Int): Int {
        var scale = 1
        if (actualWidth > actualHeight && actualWidth > dimension) {
            // 如果宽度大的话根据宽度固定大小缩放
            scale = (actualWidth / dimension)
        } else if (actualWidth < actualHeight && actualHeight > dimension) {
            // 如果高度高的话根据高度固定大小缩放
            scale = (actualHeight / dimension)
        }
        if (scale <= 0) {
            scale = 1
        }
        return scale
    }

    /**
     * 根据传入的路径，获取图片的宽高
     *
     * @param filepath 图片文件的路径
     * @return 返回图片的宽高是拼接的字符串
     */
    fun getImageSize(filepath: String?): String {
        val options = Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设为true了
        // 这个参数的意义是仅仅解析边缘区域，从而可以得到图片的一些信息，比如大小，而不会整个解析图片，防止OOM
        options.inJustDecodeBounds = true

        // 此时bitmap还是为空的
        val bitmap = BitmapFactory.decodeFile(filepath, options)
        val actualWidth = options.outWidth
        val actualHeight = options.outHeight
        return "$actualWidth.$actualHeight"
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    fun getBitmapDegree(path: String?): Int {
        var degree = 0
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(path)
            // 获取图片的旋转信息
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        return newBitmap
    }
    /**
     * -------------------- 图片压缩处理 --------------------
     */
    /**
     * 临时压缩图片
     *
     * @param path 图片路径
     * @param dimension 最大尺寸
     * @param size      最大大小
     * @return 压缩后的图片临时路径
     */
    fun compressTempImage(path: Any?, dimension: Int = 2048, size: Int = 512): String? {
        VMLog.d("compressTempImage start")
        val srcBitmap = compressByDimension(path, dimension) ?: return null

        // 进一步进行质量压缩
        val bitmap = compressByQuality(compressBitmapByMatrix(srcBitmap, dimension), size)
        VMLog.d("compressTempImage end")
        // 生成临时文件名
        val tempName = generateTempName(path)
        // 临时存放路径
        val tempPath = cacheFromSDCard + "temp"
        createDirectory(tempPath)
        saveBitmapToFiles(bitmap, "$tempPath/$tempName")
        return "$tempPath/$tempName"
    }

    /**
     * 按比例压缩到指定尺寸记载图片 默认到 1920 以下
     *
     * @param path 图片地址 可以是 String 也可以是 Uri
     * @param dimension 最大尺寸
     * @param size      最大大小
     * @return 压缩后的 bitmap 对象
     */
    fun compressByDimension(path: Any?, dimension: Int = 2048): Bitmap? {
        val options = Options()
        // 开始读入图片，此时把 options.inJustDecodeBounds 设回 true，这里只是加载图片边界信息
        options.inJustDecodeBounds = true

        var bitmap: Bitmap?
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P && path is Uri) {
            var pfd: ParcelFileDescriptor? = VMTools.context.contentResolver.openFileDescriptor(path as Uri, "r");
            if (pfd?.fileDescriptor != null) {
                BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor, null, options)
                pfd.close()
            } else {
                return null
            }
        } else {
            BitmapFactory.decodeFile(path as String, options)
        }

        val w = options.outWidth
        val h = options.outHeight
        // 缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        val be = getZoomScale(w, h, dimension)
        // 设置缩放比例
        options.inSampleSize = be
        // 重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false，这里才会真正加载图片内容
        options.inJustDecodeBounds = false

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P && path is Uri) {
            var parcelFileDescriptor: ParcelFileDescriptor? = VMTools.context.contentResolver.openFileDescriptor(path as Uri, "r");
            var fileDescriptor: FileDescriptor?
            if (parcelFileDescriptor?.fileDescriptor != null) {
                fileDescriptor = parcelFileDescriptor.fileDescriptor
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
            } else {
                return null
            }
        } else {
            bitmap = BitmapFactory.decodeFile(path as String, options)
        }
        return bitmap
    }

    /**
     * 质量压缩，默认压缩到 500K 以下
     *
     * @param bitmap 原图
     * @param size   指定大小
     * @return 压缩后的图片
     */
    fun compressByQuality(bitmap: Bitmap, size: Int = 512): Bitmap? {
        val baos = ByteArrayOutputStream()
        var quality = 90
        //质量压缩方法，100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(JPEG, quality, baos)
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (quality > 20 && baos.toByteArray().size / 1024 > size) {
            // 每次都减少20
            quality -= 20
            // 重置baos即清空baos
            baos.reset()
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(JPEG, quality, baos)
        }
        // 把压缩后的数据存放到ByteArrayInputStream中
        val isBm = ByteArrayInputStream(baos.toByteArray())

        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * 通过矩阵压缩 Bitmap 到指定尺寸
     *
     * @param bitmap    需要压缩的 Bitmap
     * @param dimension 图片压缩后最大宽高
     * @return 返回压缩后的 Bitmap
     */
    fun compressBitmapByMatrix(bitmap: Bitmap, dimension: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        // 图片比目标大小还小则不进行处理直接返回
        if (w < dimension && h < dimension) return bitmap

        val scale = if (w > h) {
            dimension.toFloat() / w
        } else {
            dimension.toFloat() / h
        }
        // 使用矩阵进行压缩图片
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 通过矩阵按比例压缩 Bitmap
     *
     * @param bitmap 需要压缩的 Bitmap
     * @param scale  图片压缩比率(0-1)
     * @return 返回压缩后的 Bitmap
     */
    fun compressBitmapByScale(bitmap: Bitmap, scale: Int): Bitmap {
        // 使用矩阵进行压缩图片
        val matrix = Matrix()
        val s = 1.toFloat() / scale
        matrix.postScale(s, s)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 生成临时文件名
     *
     * @param path 文件原始路径
     */
    private fun generateTempName(path: Any?): String {
        if (path is String) {
            return System.currentTimeMillis().toString() + getExtensionName(path)
        } else if (path is Uri) {
            val tempPath = getPath(path)
            return System.currentTimeMillis().toString() + getExtensionName(tempPath)
        } else {
            return System.currentTimeMillis().toString() + ".jpeg"
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param path 文件原始路径
     */
    fun getExtensionName(path: String?): String {
        if (!path.isNullOrEmpty()) {
            val dot = path.lastIndexOf('.')
            if (dot > -1 && dot < path.length - 1) {
                return path.substring(dot)
            }
        }
        return ".jpeg"
    }

    /**
     * 保存 Bitmap 到指定路径
     *
     * @param bitmap 需要保存的图片数据
     * @param path   保存路径
     */
    fun saveBitmapToPictures(bitmap: Bitmap, path: String, name: String, format: CompressFormat = JPEG): Uri? {
        val values = ContentValues()
        // 需要指定文件信息时，非必须
        values.put(MediaStore.Images.Media.TITLE, name)
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/${format.name.decapitalize()}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + path)
        } else {
            values.put(MediaStore.MediaColumns.DATA, "${VMFile.pictures}/${path}${name}")
        }

        val uri = VMTools.context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = VMTools.context.contentResolver.openOutputStream(uri)
                bitmap.compress(format, 100, outputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                outputStream?.close()
            }
        }

        return uri
    }

    /**
     * 保存 Bitmap 到指定路径
     *
     * @param bitmap 需要保存的图片数据
     * @param path   保存路径
     * @param format 格式类型
     */
    fun saveBitmapToFiles(bitmap: Bitmap?, path: String, format: CompressFormat = JPEG): Boolean {
        VMLog.d("saveBitmapToSDCard -start-")
        bitmap ?: return false
        var result: Boolean
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(path)
            bitmap.compress(format, 100, outputStream)
            result = true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            result = false
        } finally {
            outputStream?.close()
        }
        VMLog.d("saveBitmapToSDCard -end-")
        return result
    }
}