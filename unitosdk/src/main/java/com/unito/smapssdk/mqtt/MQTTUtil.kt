package com.unito.smapssdk.mqtt

import android.content.Context
import android.os.Build
import android.util.Log
import com.unito.smapssdk.R
import com.unito.smapssdk.UnitoManager
import com.unito.smapssdk.library.ComConvertJson
import com.unito.smapssdk.library.JsonUtils
import com.unito.smapssdk.library.ThreadPoolUtil
import com.unito.smapssdk.library.Utils
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean

class MQTTUtil private constructor(private val context: Context) {

    companion object {
        var publisher: String = ""
        var subscriber: String = ""
//        var name: String = ""
//        var pwd: String = ""
//        var clientId: String = ""

        private const val TAG = "MQTTUtil"
        private const val QOS = 2
        var MQTT_BROKER_URL =
            "ssl://iot.unito-oauth.com:8883"
        private const val TIMEOUT = 10
        private const val KEEP_ALIVE_INTERVAL = 60
        private const val MAX_IN_FLIGHT = 10

        @Volatile
        private var instance: MQTTUtil? = null

        fun getInstance(context: Context): MQTTUtil {
            return instance ?: synchronized(this) {
                instance ?: MQTTUtil(context).also { instance = it }
            }
        }
    }

    private lateinit var client: MqttClient
    private lateinit var options: MqttConnectOptions
    private val isConnected = AtomicBoolean(false)

    fun connect(clientId: String, name: String, pwd: String) {
        val caCrtFile: InputStream = context.getResources().openRawResource(R.raw.ca)
        val crtFile: InputStream = context.getResources().openRawResource(R.raw.cert)
        val keyFile: InputStream = context.getResources().openRawResource(R.raw.key)
        try {
            client = MqttClient(MQTT_BROKER_URL, clientId, MemoryPersistence())
            options = MqttConnectOptions().apply {
                userName = name
                password = pwd.toCharArray()
                isCleanSession = true
                isAutomaticReconnect = false
                connectionTimeout = TIMEOUT
                keepAliveInterval = KEEP_ALIVE_INTERVAL
                maxInflight = MAX_IN_FLIGHT
                socketFactory = SSLUtils.getSocketFactory(caCrtFile, crtFile, keyFile, "")
            }
            client.setCallback(MQTTCallbackHandler())
            client.connect(options)
            isConnected.set(true)
            Log.d(TAG, "Connected to: $MQTT_BROKER_URL")
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to connect to: $MQTT_BROKER_URL", e)
        } finally {
            // 安全地关闭输入流资源
            try {
                caCrtFile?.close()
                crtFile?.close()
                keyFile?.close()
            } catch (ioException: IOException) {
                Log.e(TAG, "关闭证书文件失败", ioException)
            }
        }
    }

    fun disconnect() {
        try {
            if (isConnected.get()) {
                client.disconnect()
                isConnected.set(false)
                Log.d(TAG, "Disconnected from MQTT broker")
            }
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to disconnect from MQTT broker", e)
        }
    }

    fun subscribe() {
        try {
            if (isConnected.get()) {
                client.subscribe(subscriber)
                Log.d(TAG, "Subscribed to: $subscriber")
            } else {
                Log.e(TAG, "Client is not connected. Unable to subscribe.")
            }
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to subscribe to topic: $subscriber", e)
        }
    }

    fun publish(message: ByteArray) {
        try {
            if (isConnected.get()) {
                val mqttMessage = MqttMessage().apply {
                    payload = message
                    qos = QOS
                    isRetained = false
                }
                client.publish(publisher, mqttMessage)
                Log.d(TAG, "Published message to: $publisher");
            } else {
                Log.e(TAG, "Client is not connected. Unable to publish.")
            }
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to publish message to topic: $publisher", e)
        }
    }

    inner class MQTTCallbackHandler : MqttCallback {

        override fun connectionLost(cause: Throwable?) {
            isConnected.set(false)
            val map: MutableMap<String, String> = LinkedHashMap()
            map["destination"] = "appBle"
            map["source"] = "waterSystem"
            map["msgType"] = "response"
            map["target"] = UnitoManager.target
            map["MQTTConnectStatus"] = "lost"
            UnitoManager.getSingleton().notity(JsonUtils.mapToJson(map))
            Log.e(TAG, "Connection lost!")
            // Here you could implement a reconnect mechanism
        }

        override fun messageArrived(topic: String, message: MqttMessage) {
            Log.d(TAG, "Received message: ${Utils.bytesToHex(message.payload)} in $topic")
            // Handle the message, perhaps update UI or store it for later use
            if (message.payload.isNotEmpty()) {
                if (message.payload.get(0) == 0x00.toByte() && message.payload
                        .get(message.payload.size - 1) == 0xff.toByte()
                ) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            ThreadPoolUtil.handler.post {
                                try {
                                    ComConvertJson.CONVERT_MAP[message.payload
                                        .get(3)]!!.accept(
                                        message.payload
                                    )
                                } catch (exception: Exception) {
                                    exception.printStackTrace()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d(TAG, "Delivery Complete")
        }
    }

    fun isConnected() = isConnected.get()
}
