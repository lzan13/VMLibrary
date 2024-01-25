package com.vmloft.develop.library.tools.voice.encoder

import android.media.AudioRecord
import android.media.AudioRecord.OnRecordPositionUpdateListener
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.vmloft.develop.library.tools.utils.logger.VMLog

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections

/**
 * Created by lzan13 on 2024/1/23 10:00
 * 描述：编码任务类
 */
class EncodeTask(file: File, bufferSize: Int) : HandlerThread("EncodeTask"), OnRecordPositionUpdateListener {

    // 任务 Handler
    lateinit var taskHandler: TaskHandler

    // 任务集合
    private val taskList = Collections.synchronizedList(ArrayList<TaskBean>())

    // 转换缓冲区
    private var mp3Buffer: ByteArray

    // 文件输出流
    private var fileOutputStream: FileOutputStream

    init {
        fileOutputStream = FileOutputStream(file)
        mp3Buffer = ByteArray((7200 + bufferSize * 2 * 1.25).toInt())

    }

    /**
     * 添加任务
     */
    fun addTask(rawData: ShortArray, readSize: Int) {
        taskList.add(TaskBean(rawData, readSize))
    }

    /**
     * 开始任务
     */
    @Synchronized
    fun startTask() {
        start()
        taskHandler = TaskHandler(looper, this)
    }

    /**
     * 停止任务
     */
    fun stopTask() {
        taskHandler.sendEmptyMessage(0)
    }

    /**
     * 获取 handler
     */
    fun getHandler(): Handler {
        return taskHandler
    }

    override fun onMarkerReached(recorder: AudioRecord) {
        // Do nothing
    }

    /**
     * 收到录音机周期通知
     */
    override fun onPeriodicNotification(recorder: AudioRecord) {
        processData()
    }

    /**
     * 从缓冲区中读取并处理数据，使用lame编码MP3
     * @return  从缓冲区中读取的数据的长度
     * 缓冲区中没有数据时返回0
     */
    fun processData(): Int {
        if (taskList.size > 0) {
            val task = taskList.removeAt(0)
            val buffer: ShortArray = task.data
            val readSize: Int = task.readSize
            val encodedSize = VMLame.encode(buffer, buffer, readSize, mp3Buffer)
            if (encodedSize > 0) {
                try {
                    fileOutputStream.write(mp3Buffer, 0, encodedSize)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return readSize
        }
        return 0
    }

    /**
     * 将缓冲区剩余所有数据写入文件
     */
    fun flushAndRelease() {
        // 将 mp3 结尾信息写入 buffer 中
        val flushResult = VMLame.flush(mp3Buffer)
        if (flushResult > 0) {
            try {
                fileOutputStream.write(mp3Buffer, 0, flushResult)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                VMLog.i("EncodeTask.flushAndRelease")
                VMLame.close()
            }
        }
    }


    /**
     * 任务 Handler
     */
    inner class TaskHandler(looper: Looper, private val encodeThread: EncodeTask) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            // 处理缓冲区中的数据
            while (encodeThread.processData() > 0);
            // 移除其他任务
            removeCallbacksAndMessages(null)
            encodeThread.flushAndRelease()
            looper.quit()
        }
    }

}
/**
 * 任务数据 Bean
 */
data class TaskBean(
    var rawData: ShortArray,
    val readSize: Int
) {
    // 拷贝数据属性
    val data: ShortArray

    init {
        data = rawData.clone()
    }
}
