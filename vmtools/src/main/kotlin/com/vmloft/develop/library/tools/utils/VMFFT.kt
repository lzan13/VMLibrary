package com.vmloft.develop.library.tools.utils

import com.vmloft.develop.library.tools.utils.logger.VMLog
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType

import kotlin.math.log2


/**
 * Created by lzan13 on 2024/5/16 18:03
 * 描述：音频数据傅里叶转换工具类，此类用到了 commons-math3 库中的数学工具类
 * 参考：https://juejin.cn/post/7278874801438015488
 */
object VMFFT {
    // 采样率
    private val samplingRate = 44100

    // 分段的频率的范围和步长（段数）
    private val frequencyBands = 300
    private val startFrequency = 20.0
    private val endFrequency = 20000.0

    private var spectrumBuffer = DoubleArray(frequencyBands)

    /**
     * 傅里叶转换数据
     * @param buffer 原始数据，数据大小必须是 2 的 n 次幂
     */
    fun transitionFFTData(buffer: ShortArray): DoubleArray {
//        VMLog.e("fft buffer:${buffer.joinToString(",")}")
        val real = DoubleArray(buffer.size)
        // 加汉宁窗，这里必须加上，不然后边经过傅里叶转换后数据都是0
        val hanNing = getHanNingWindow(buffer.size)
        for (i in buffer.indices) {
            real[i] = (buffer[i] * hanNing[i]) / (Short.MAX_VALUE.toDouble())
        }

        val imag = DoubleArray(real.size)
        val complexData = arrayOfNulls<Complex>(real.size)// 创建复数数组
        for (i in real.indices) {
            imag[i] = 0.0 // 给虚部赋值
            complexData[i] = Complex(real[i], imag[i]) // 创建复数
        }
        // 执行fft,对数据有要求，必须是2^n倍，比如2048,4096,8192
        val fft = FastFourierTransformer(DftNormalization.STANDARD)
        val fftResult = fft.transform(complexData, TransformType.FORWARD)// 正向fft
        // 获取频谱数据(振幅)
        val spectrum = DoubleArray(fftResult.size)
        for (i in fftResult.indices) {
            spectrum[i] = fftResult[i].abs()// 使用abs返回计算的振幅
        }
//        VMLog.e("fft spectrum:${spectrum.size}-${spectrum.joinToString(",")}")
        // 分段(顾名思义就是根据频率范围（20-20000）划分为指定份数（这里是100份）
        return splitFrequencyBands(buffer.size, spectrum)
    }

    /**
     * 获取汉宁窗
     */
    private fun getHanNingWindow(size: Int): DoubleArray {
        return DoubleArray(size) { i -> 0.5 * (1.0 - Math.cos(2.0 * Math.PI * i / (size - 1))) }
    }

    /**
     * 分组频谱数据块
     */
    private fun splitFrequencyBands(bufferSize: Int, spectrum: DoubleArray): DoubleArray {
        //1: 创建权重数组
        val aWeights = createFrequencyWeights(bufferSize)
        val bandsMaxFreqAmp = mutableListOf<Pair<Double, Double>>()//存储各分段的最高频率和最高振幅
        //1：根据起止频谱、频带数量确定增长的倍数：2^n
        val n = log2(endFrequency - startFrequency) / frequencyBands
        var lowFrequency = startFrequency
        for (i in 1..frequencyBands) {
            //2：频带的上频点是下频点的2^n倍
            val heightFrequency = lowFrequency * Math.pow(2.0, n)//2^n倍数
            var maxSpectrumInBands = Double.MIN_VALUE
            var centerFrequency = Double.MIN_VALUE
            val fs = samplingRate / bufferSize
            val startFrequencyIndex = Math.max(lowFrequency / fs, startFrequency)
            val endFrequencyIndex = if (i == frequencyBands) endFrequency else (heightFrequency / fs)
            for (j in spectrum.indices) {
                // 假设采样率为44100Hz/窗体大小，也就是数据的大小
                val frequency = (j * fs).toDouble()
                if (frequency in startFrequencyIndex..endFrequencyIndex) {
                    if (spectrum[j] > maxSpectrumInBands) {
                        maxSpectrumInBands = spectrum[j]
                        centerFrequency = frequency
                    }
                }
            }
            val nextBand = Pair(centerFrequency, maxSpectrumInBands)//每段
            bandsMaxFreqAmp.add(nextBand)
            lowFrequency = heightFrequency//上一次的高频变成这次的低频
        }
        val endSpectrum = DoubleArray(bandsMaxFreqAmp.size)//最终去绘制的振幅（频谱）
        val endFrequency = DoubleArray(bandsMaxFreqAmp.size)//最终去绘制的振幅的对应频率
        // 输出每个频段的中心频率和最大幅度
        for (index in 0 until bandsMaxFreqAmp.size) {
            //在筛选有效的原始频谱数据后依次与权重相乘(结果*2倍数，避免起伏不多)
            val haveWeight = (bandsMaxFreqAmp[index].second * aWeights[index]) * 2
            endSpectrum[index] = haveWeight//用去绘制频谱图的
            endFrequency[index] = bandsMaxFreqAmp[index].first//频率
        }
//        val smoothSpectrum = highlightWaveform(endSpectrum)// 加权平均处理（平滑处理）
//        val blinkSpectrum = smoothFrameWaveform(smoothSpectrum)// 闪动优化处理，使用缓存上一帧的方式做
        val blinkSpectrum = smoothFrameWaveform(endSpectrum)// 闪动优化处理，使用缓存上一帧的方式做
        //这个分贝db数据在滤波去噪后拿 TODO 分贝也不需要
//        val mCurrentMaxDb = 0
        //用处理后的数据去拿最高振幅对应最高频率 TODO 暂时不需要最高频率
//        val mCurrentMaxFrequency = endFrequency[getAfterDealMaxFrequency(mBlinkSpectrum)]
        return blinkSpectrum
    }

    /**
     * 获取最大频率
     */
    private fun getAfterDealMaxFrequency(mBlinkSpectrum: DoubleArray): Int {
        //再根据这些振幅中拿到最高振幅对于的频率，代表这所有段里面频率去计算色相之类的
        var maxAllBandsAmp = Double.MIN_VALUE
        //最终拿到最高频率对应的下标，他们的长度是一致的，所以下标有效
        var maxAllBandsIndex = 0
        for (index in mBlinkSpectrum.indices) {
            //拿最高振幅的对应频率，用于计算色相的数据做筛选
            if (mBlinkSpectrum[index] > maxAllBandsAmp) {
                maxAllBandsAmp = mBlinkSpectrum[index]
                maxAllBandsIndex = index
            }
        }
        return maxAllBandsIndex
    }

    /**
     * A计权处理，创建权重数组
     */
    private fun createFrequencyWeights(bufferSize: Int): DoubleArray {
        val deltaF = 44100.0 / bufferSize
        val bins: Int = bufferSize / 2 // 返回数组的大小
        val f = DoubleArray(bins)
        for (i in 0 until bins) {
            f[i] = (i.toFloat() * deltaF)
        }
        val f1 = Math.pow(20.598997, 2.0)
        val f2 = Math.pow(107.65265, 2.0)
        val f3 = Math.pow(737.86223, 2.0)
        val f4 = Math.pow(12194.217, 2.0)
        val num = DoubleArray(bins)
        val den = DoubleArray(bins)
        for (i in 0 until bins) {
            num[i] = (f4 * Math.pow(f[i], 2.0))
            den[i] = ((f[i] + f1) * Math.sqrt((f[i] + f2) * (f[i] + f3)) * (f[i] + f4))
        }
        val weights = DoubleArray(bins)
        for (i in 0 until bins) {
            weights[i] = (1.2589 * num[i] / den[i])
        }
        return weights
    }

    /**
     * 加权平均波形处理
     */
    private fun highlightWaveform(spectrum: DoubleArray): DoubleArray {
        // 1. 定义权重数组，数组中间的5表示自己的权重，个数需要奇数
        val weights = mutableListOf(1.0, 2.0, 3.0, 5.0, 3.0, 2.0, 1.0)
        val totalWeights = weights.reduce { acc, d -> acc + d }//累加
        val averagedSpectrum = DoubleArray(spectrum.size)
        val startIndex = weights.size / 2//结果是3
        // 3. 循环+1渐进重叠式计算加权平均值
        for (i in startIndex until spectrum.size - startIndex) {
            val currentSpe = mutableListOf<Double>()//不要用DoubleArray，会下标对不上
            for (j in i - startIndex..i + startIndex) {
                currentSpe.add(spectrum[j])
            }
            //写法一
            //zip作用: zip([a,b,c], [x,y,z]) -> [(a,x), (b,y), (c,z)]
            //val zipped = currentSpe.zip(weights)
            //val averaged = zipped.map {
            //    it.first * it.second
            //}.reduce { acc, d -> acc + d } / totalWeights
            //写法二
            var sum = 0.0
            for (k in currentSpe.indices) {
                sum += currentSpe[k] * weights[k]
            }
            val averaged = sum / totalWeights

            //统计到每项去
            averagedSpectrum[i] = averaged
        }
        return averagedSpectrum
    }

    /**
     * 平滑波形帧间更新动画
     */
    private fun smoothFrameWaveform(spectrum: DoubleArray): DoubleArray {
        val aWeight = 0.2//权重系数，0-1，越大越柔和
        if (spectrumBuffer.isEmpty()) {
            for (j in spectrum.indices) {
                spectrumBuffer[j] = spectrum[j]
            }
        }
        //（方式一）加权平均做优化
        //注意把旧数据的比重*aWeight,新数据的比重*(1 - aWeight)，这样才可以最大程度保留旧数据的帧，达到柔和目的
        for (i in spectrum.indices) {
            val sum = (spectrumBuffer[i] * aWeight) + (spectrum[i] * (1 - aWeight))
            spectrumBuffer[i] = sum / (aWeight + (1 - aWeight))//加起来就是个1，除不除都行了其实
        }
        //（方式二）这种方式的加权平均和上述的方式没啥区别
        //注意把旧数据的比重*aWeight,新数据的比重*(1 - aWeight)，这样才可以最大程度保留旧数据的帧，达到柔和目的
        //val zipped = spectrumBuffer.zip(spectrum)
        //spectrumBuffer = zipped.map { (it.first * aWeight) + (it.second * (1 - aWeight)) }.toDoubleArray()
        return spectrumBuffer
    }

}