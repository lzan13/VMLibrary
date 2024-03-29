package com.vmloft.develop.library.tools.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore.Audio
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.Video

import com.vmloft.develop.library.tools.VMTools.context
import com.vmloft.develop.library.tools.utils.VMDate.filenameDateTime
import com.vmloft.develop.library.tools.utils.logger.VMLog

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.net.URI
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Created by lzan13 on 2014/12/16.
 * 文件相关工具类，包括文件的创建 拷贝 删除 以及路径的获取判断的等
 */
object VMFile {
    /**
     * 判断sdcard是否被挂载
     */
    fun hasSdcard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 判断目录是否存在
     *
     * @param path 目录路径
     */
    fun isDirExists(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }
        val dir = File(path)
        return dir.exists()
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     */
    fun isFileExists(path: Any): Boolean {
        if (path is String) {
            val file = File(path)
            return file.exists()
        } else if (path is Uri) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || path.toString().indexOf("content://") != -1) {
                try {
                    var pfd: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(path, "r")
                    pfd?.close()
                } catch (e: FileNotFoundException) {
                    return false
                }
                return true
            } else {
                val file = File(URI.create(path.toString()))
                return file.exists()
            }
        }
        return false
//        if (path.isNullOrEmpty()) {
//            return false
//        }
//        val file = File(path)
//        return file.exists()
    }

    /**
     * 创建目录，多层目录会递归创建
     */
    fun createDirectory(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }
        val dir = File(path)
        return if (!isDirExists(path)) {
            dir.mkdirs()
        } else true
    }

    /**
     * 创建新文件
     */
    fun createFile(filepath: String?): File? {
        val isSuccess: Boolean
        if (filepath.isNullOrEmpty()) {
            return null
        }
        val file = File(filepath)
        // 判断文件上层目录是否存在，不存在则首先创建目录
        if (!isDirExists(file.parent)) {
            createDirectory(file.parent)
        }
        isSuccess = if (!file.isFile) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        } else {
            true
        }
        return if (isSuccess) {
            file
        } else null
    }

    /**
     * 创建新文件，外部传入前缀和后缀
     */
    fun createFile(path: String, prefix: String, suffix: String): File? {
        if (!createDirectory(path)) {
            return null
        }
        val filename = prefix + filenameDateTime() + suffix
        return createFile(path + filename)
    }

    /**
     * 压缩文件
     *
     * @param srcPath  源文件路径
     * @param destPath 压缩问及那路径
     * @return 压缩结果
     */
    fun zipFile(srcPath: String?, destPath: String?): Boolean {
        if (srcPath.isNullOrEmpty() || destPath.isNullOrEmpty()) {
            return false
        }
        try {
            val srcFile = File(srcPath)
            val zipFile = File(destPath)
            // 输入流读取数据
            val input = FileInputStream(srcFile)
            // 输出流输出数据，输出Zip
            val output = ZipOutputStream(FileOutputStream(zipFile))
            // 用于缓存数据
            var temp: Int
            // 用于生成说明，会位于打开Zip后，右边的区域
            output.setComment(srcFile.name)
            // 从输入流中获取的数据不能直接写入 Zip 文件，而是需要在 Zip 文件中新建一个 ZipEntry，然后将数据写入新建的文件
            output.putNextEntry(ZipEntry(srcFile.name))
            while (input.read().also { temp = it } != -1) {
                output.write(temp)
            }
            // 关闭流
            input.close()
            output.close()
            return true
        } catch (e: IOException) {
            VMLog.e("压缩文件失败 " + e.message)
        }
        return false
    }

    /**
     * 复制文件
     *
     * @param srcPath  源文件地址
     * @param destPath 目标文件地址
     * @return 返回复制结果
     */
    fun copyFile(srcPath: String?, destPath: String?): File? {
        if (srcPath.isNullOrEmpty()) {
            VMLog.e("源文件不存在，无法完成复制")
            return null
        }
        val srcFile = File(srcPath)
        if (!srcFile.exists()) {
            VMLog.e("源文件不存在，无法完成复制")
            return null
        }
        if (destPath.isNullOrEmpty()) {
            VMLog.e("目标路径不能为 null")
            return null
        }
        val destFile = File(destPath)
        VMLog.i(destFile.parent)
        if (!isDirExists(destFile.parent)) {
            createDirectory(destFile.parent)
        }
        try {
            val inputStream: InputStream = FileInputStream(srcFile)
            val outputStream = FileOutputStream(destPath)
            val buff = ByteArray(1024)
            var len = 0
            while (inputStream.read(buff).also { len = it } != -1) {
                outputStream.write(buff, 0, len)
            }
            inputStream.close()
            outputStream.close()
            return destFile
        } catch (e: FileNotFoundException) {
            VMLog.e("拷贝文件出错：$e")
        } catch (e: IOException) {
            VMLog.e("拷贝文件出错：$e")
        }
        return null
    }

    /**
     * 读取文件到drawable
     *
     * @param filepath 文件路径
     * @return 返回Drawable资源
     */
    fun fileToDrawable(filepath: String?): Drawable? {
        if (filepath.isNullOrEmpty()) {
            return null
        }
        val file = File(filepath)
        return if (file.exists()) {
            Drawable.createFromPath(filepath)
        } else null
    }

    /**
     * 格式化文件字节大小
     */
    fun formatSize(size: Long): String {
        val result: BigDecimal
        val kiloByte = size / 1024.toDouble()
        if (kiloByte < 1) {
            return size.toString() + "B"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            result = BigDecimal(kiloByte.toString())
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            result = BigDecimal(megaByte.toString())
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraByte = gigaByte / 1024
        if (teraByte < 1) {
            result = BigDecimal(gigaByte.toString())
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        result = BigDecimal(teraByte.toString())
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 递归实现遍历文件夹大小
     *
     * @param fileDir 要计算的文件夹
     */
    fun getFolderSize(fileDir: File): Long {
        var size: Long = 0
        if (!fileDir.exists()) {
            return size
        }
        val fileList = fileDir.listFiles()
        for (file in fileList) {
            size += if (file.isDirectory) {
                getFolderSize(file)
            } else {
                file.length()
            }
        }
        return size
    }

    /**
     * 删除文件
     */
    fun deleteFile(filepath: String?): Boolean {
        if (filepath.isNullOrEmpty()) {
            return false
        }
        val file = File(filepath)
        return if (file.exists() && file.isFile) {
            file.delete()
        } else {
            false
        }
    }

    /**
     * 删除文件集合
     *
     * @param paths 文件路径集合
     */
    fun deleteFiles(paths: List<String?>) {
        for (path in paths) {
            deleteFile(path)
        }
    }

    /**
     * 递归删除文件夹内的文件
     *
     * @param path       需要操作的路径
     * @param deleteThis 删除自己
     */
    fun deleteFolder(path: String?, deleteThis: Boolean) {
        if (path.isNullOrEmpty()) {
            return
        }
        val fileSrc = File(path)
        // 文件/目录存在（包括文件及文件夹）
        if (fileSrc.exists()) {
            if (fileSrc.isFile) {
                fileSrc.delete()
            } else if (fileSrc.isDirectory) {
                //接收文件夹目录下所有的文件实例
                val listFiles = fileSrc.listFiles() ?: return
                //文件夹为空 递归出口
                for (file in listFiles) {
                    deleteFolder(file.absolutePath, true)
                }
                if (deleteThis) {
                    // 递归跳出来的时候删除空文件夹
                    fileSrc.delete()
                }
            }
        }
    }

    /**
     * 根据文件路径解析文件名，不包含扩展类型
     */
    fun parseFilename(path: String?): String {
        if (path.isNullOrEmpty()) {
            return ""
        }
        val index = path.lastIndexOf(File.separator)
        val fileName = if (index == -1) {
            path
        } else {
            path.substring(index + 1)
        }
        return fileName.substring(0, fileName.lastIndexOf("."))
    }

    /**
     * 获取文件扩展名，
     *
     * @param path 可以是路径，可以是文件名
     */
    fun parseSuffix(path: String?): String {
        if (path.isNullOrEmpty()) return ""
        var result = ""
        if (path.isNotEmpty()) {
            val index = path.lastIndexOf(File.separator)
            val filename: String = if (index == -1) {
                path
            } else {
                path.substring(index + 1)
            }
            result = filename.substring(filename.lastIndexOf("."))
        }
        return result
    }


    /**
     * 获取文件 mineType
     */
    fun getMimeType(path: String): String {
        val type = parseSuffix(path)
        return if (type == ".gif") {
            "image/gif"
        } else if (type == ".jpg" || type == ".jpeg") {
            "image/jpeg"
        } else if (type == ".mp3") {
            "audio/mpeg"
        } else if (type == ".mp4") {
            "video/mp4"
        } else if (type == ".mpeg") {
            "video/mpeg"
        } else if (type == ".png") {
            "image/png"
        } else if (type == ".wav") {
            "audio/x-wav"
        } else if (type == ".weba") {
            "audio/webm"
        } else if (type == ".webm") {
            "video/webm"
        } else if (type == ".webp") {
            "image/webp"
        } else {
            "*/*"
        }
    }

    /**
     * --------------------------------------------------------------------------
     * 获取Android系统的一些默认路径
     */

    /**
     * 获取缓存目录路径
     * 优先获取外部 sdcard 存储路径：/sdcard/Android/data/packagename/cache 目录
     * sdcard 不可用获取系统内部路径：/data/data/packagename/cache 目录
     * @return 返回得到的路径 已包含路径分隔符
     */
    val cachePath: String
        get() = (context.externalCacheDir?.absolutePath ?: context.cacheDir.absolutePath) + File.separator

    /**
     * 获取项目文件目录路径
     * 优先获取外部 sdcard 存储路径：/sdcard/Android/data/packagename/files 目录
     * sdcard 不可用获取系统内部路径：/data/data/packagename/files 目录
     * @return 返回得到的路径 已包含路径分隔符
     */
    fun filesPath(path: String = ""): String {
        return (context.getExternalFilesDir(path)?.absolutePath ?: context.filesDir.absolutePath) + File.separator
    }

    /**
     * 获取 /sdcard/Android/obb/packagename 目录
     *
     * @return 返回得到的路径
     */
    val OBB: String
        get() = context.obbDir.absolutePath + File.separator

    /**
     * 获取设备默认的相册目录
     */
    val DCIM: String
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)?.absolutePath + File.separator

    /**
     * 获取设备默认的下载目录
     */
    val download: String
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + File.separator

    /**
     * 获取设备默认的音乐目录
     *
     * @return 返回得到的路径
     */
    val music: String
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)?.absolutePath + File.separator

    /**
     * 获取设备默认的电影目录
     */
    val movies: String
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)?.absolutePath + File.separator

    /**
     * 获取设备默认的图片目录
     */
    val pictures: String
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator

    /**
     * 获取 packagename 目录
     *
     * @return 返回得到的路径
     */
    val packageName: String
        get() = context.packageName

    /**
     * 获取 /data/app/packagename-1.apk 目录
     *
     * @return 返回得到的路径
     */
    val packageCode: String
        get() = context.packageCodePath

    /**
     * 获取 /data/app/packagename-1.apk 目录
     *
     * @return 返回得到的路径
     */
    val packageResource: String
        get() = context.packageResourcePath

    /**
     * 根据 Uri 获取文件的真实路径，这个是网上的方法，用的还是比较多的，可以参考，
     * 不过在选择google相册的图片的时候，如果本地不存在图片会出现问题
     *
     * @param uri 包含文件信息的 Uri
     * @return 返回文件真实路径
     */
    fun getPath(uri: Uri): String? {
        // 判断当前系统 API 4.4（19）及以上
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + File.separator + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // MediaStore (and general)
            // Return the remote address
            // 这里先判断是否是通过 Google 相册 选择的图片，同时这个图片不存在于本地
            return if (isGooglePhotosUri(uri)) {
                //                return null;
                uri.lastPathSegment
            } else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // File
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * 这里我修改了下，我在最新的5.1上选择的一个在 Google相册里的一张图片时，这个 uri.getAuthority() 的值有所改变
     * com.google.android.apps.photos.contentprovider，之前的结尾是content，我测试的为contentprovider
     *
     * @param uri 需要判断的 Uri
     * @return 判断这个 Uri 是否是通过 Google 相册 选择的
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}