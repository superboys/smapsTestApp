package com.unito.smapssdk.library

import android.util.Log
import com.unito.smapssdk.UnitoManager
import com.unito.smapssdk.UnitoManager.commandList
import org.bouncycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi.OAEP
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import kotlin.concurrent.thread

class SocketUtil(address: String, port: Int) {
    private var len: Int = 0
    private lateinit var espBuffer: ByteArray
    private lateinit var espfis: FileInputStream
    private var length: Long = 0
    private val connection: Socket = Socket(address, port)
    private var connected: Boolean = true
    var num = 0
    private var path:String = ""

    init {
        println("Connected to server at $address on port $port")
    }

    private val writer: OutputStream = connection.getOutputStream()
    private val inputStream: InputStream = connection.getInputStream()

    fun setESPFilePath(path: String) {
        this.path = path
    }

    fun run(byteArray: ByteArray) {
        ThreadPoolUtil.execute {
            try {
                read(byteArray)
            } catch (ex: Exception) {
                Log.e("run: ", "socket disconnect")
            }
        }
    }

    fun write(message: ByteArray) {
        Log.e("write: ", Utils.bytesToHex(message))
        try {
            writer.write(message)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    var runnableTimeOut: Runnable = Runnable {
        Log.e("timeOut--->", "blueT TimeOut")
        ThreadPoolUtil.execute {
            write(Utils.hexStringToByteArray(commandList[0]))
        }
    }

    private fun read(byteArray: ByteArray) {
        val readBuffer = ByteArray(16)
        while (connected) {
            var byte = inputStream.read(readBuffer)
            if (byte > 0) {
                Log.e("read: ", Utils.bytesToHex(readBuffer))
                if (UnitoManager.getSingleton().isota == 5) {
                    if (readBuffer[3] == 0x38.toByte() && readBuffer[4] == 0xfe.toByte()
                    ) {
                        getESPFile()
                        UnitoManager.getSingleton().isota = 6
                    }
                } else if (UnitoManager.getSingleton().isota == 6) {
                    if (readBuffer[3] == 0x38.toByte() && readBuffer[4] == 0xfe.toByte()
                    ) {
                        if (espfis.read(espBuffer) != -1) {
                            len++
                            encrypt(espBuffer, Utils.secretKey)?.let { write(it) }
                            Log.e(
                                "progress ：",
                                String.format(
                                    "%.1f",
                                    ((len * 1024).toDouble() / length.toDouble()) * 100
                                ) + "%"
                            )
                        } else {
                            Log.e("", "eps升级完成")
                            UnitoManager.getSingleton().isota = 7
                            encrypt(
                                UnitoManager.getSingleton().setESPOTA5(0x05),
                                Utils.secretKey
                            )?.let {
                                write(
                                    it
                                )
                            }
                        }
                    }
                } else if (UnitoManager.getSingleton().isota == 7) {
                    if (readBuffer[3] == 0x38.toByte() && readBuffer[4] == 0xfe.toByte()
                    ) {
                        UnitoManager.getSingleton().isota = 8
                        write(UnitoManager.getSingleton().selectType(0x0a, byteArray))
                    }
                } else if (UnitoManager.getSingleton().isota == 8) {
                    Log.e("", "所有升级完成 --》esp ota完成")
                    if (readBuffer[3] == 0x38.toByte() && readBuffer[4] == 0xfe.toByte()
                    ) {
                        CloseConnect()
                    }
                }
                if (readBuffer[0] == 0xfe.toByte() && readBuffer[3] == 0x38.toByte() && readBuffer[4] == 0xfe.toByte()
                ) {
                    if (UnitoManager.getSingleton().isota == 0) {
                        Thread.sleep(200)
                        write(Utils.hexStringToByteArray(commandList[num]))
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 1000)
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 2100)
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 3200)
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 4300)
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 5400)
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 6500)
                    } else if (UnitoManager.getSingleton().isota == 1) {
                        if (UnitoManager.getSingleton().isBoth) {
                            Log.e("", "准备升级esp---》")
                            ThreadPoolUtil.handler.postDelayed(kotlinx.coroutines.Runnable {
                                ThreadPoolUtil.execute{
                                    write(UnitoManager.getSingleton().selectType(0x00, byteArray))
                                    UnitoManager.getSingleton().isota = 5
                                }
                            },1500)
                        } else {
                            write(UnitoManager.getSingleton().selectType(0x0a, byteArray))
                            UnitoManager.getSingleton().isota = 2
                        }
                    }
                } else if (readBuffer[3] == 0x38.toByte() && readBuffer[4] == 0xfe.toByte()
                ) {
                    if (UnitoManager.getSingleton().isota == 2) {
                        Log.e("", "所有升级 --》ws ota完成")
                        CloseConnect()
                    }
                } else if (readBuffer[0] == 0x55.toByte() && readBuffer[1] == 0x08.toByte()) {
                    if (UnitoManager.getSingleton().isota == 1) {
                        num++
                        write(Utils.hexStringToByteArray(commandList[num]))
                    } else {
                        UnitoManager.getSingleton().isota = 1
                        ThreadPoolUtil.handler.removeCallbacks(runnableTimeOut)
                        num++
                        write(Utils.hexStringToByteArray(commandList[num]))
                        ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 1000)
                    }
                } else if (readBuffer[0] == 0x55.toByte() && readBuffer[1] == 0x03.toByte() && readBuffer[4] == 0x00.toByte() && readBuffer[5] == 0x00.toByte() && readBuffer[7] == 0x10.toByte() && readBuffer[10] == 0x01.toByte()
                ) {
                    ThreadPoolUtil.handler.removeCallbacks(runnableTimeOut)
                    num++
                    write(Utils.hexStringToByteArray(commandList[num]))
                    ThreadPoolUtil.handler.postDelayed(runnableTimeOut, 1000)
                } else if (readBuffer[0] == 0x55.toByte() && readBuffer[1] == 0x03.toByte() && readBuffer[4] == 0x00.toByte() && readBuffer[5] == 0x00.toByte() && readBuffer[7] == 0x88.toByte() && readBuffer[10] == 0x01.toByte()
                ) {
                    ThreadPoolUtil.handler.removeCallbacks(runnableTimeOut)
                    num++
                    write(Utils.hexStringToByteArray(commandList[num]))
                } else if (readBuffer[0] == 0x55.toByte() && readBuffer[1] == 0x02.toByte()) {
                    num++
                    write(Utils.hexStringToByteArray(commandList[num]))
                } else if (readBuffer[0] == 0x55.toByte() && readBuffer[1] == 0x09.toByte()) {
                    Log.e("", "单个 ws ota完成")
                    write(UnitoManager.getSingleton().setESPOTA5(0x05))
                    num = 0
                }
            }
        }
    }

    fun getESPFile() {
        len = 0
        length = File(path).length()
        espfis =
            FileInputStream(path)
        espBuffer = ByteArray(1024)
        var num: Int
        if (length % 1024 > 0) {
            num = (length / 1024 + 1).toInt()
        } else {
            num = (length / 1024).toInt()
        }
        encrypt(
            UnitoManager.getSingleton()
                .setESPOTA2(0x02, (num % 256).toByte(), (num / 256).toByte()), Utils.secretKey
        )?.let {
            write(
                it
            )
        }
    }

    /**
     * 关闭连接
     */
    fun CloseConnect() {
        try {
            if (writer != null) {
                writer!!.close()
            }
            if (inputStream != null) {
                inputStream!!.close()
            }
            if (connection != null) {
                connection!!.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        System.out.println("关闭连接")
    }
}