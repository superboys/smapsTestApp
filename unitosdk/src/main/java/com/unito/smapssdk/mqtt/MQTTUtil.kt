package com.unito.smapssdk.mqtt

import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.concurrent.atomic.AtomicBoolean

class MQTTUtil private constructor() {

    companion object {
        private const val TAG = "MQTTUtil"
        private const val QOS = 2
        private const val MQTT_BROKER_URL =
            "iot.unito-oauth.com:8883"
        private const val TIMEOUT = 10
        private const val KEEP_ALIVE_INTERVAL = 60
        private const val MAX_IN_FLIGHT = 10

        @Volatile
        private var instance: MQTTUtil? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: MQTTUtil().also { instance = it }
        }
    }

    private lateinit var client: MqttClient
    private lateinit var options: MqttConnectOptions
    private val isConnected = AtomicBoolean(false)

    init {
        val clientId = MqttClient.generateClientId()
        client = MqttClient(MQTT_BROKER_URL, clientId, MemoryPersistence())

        options = MqttConnectOptions().apply {
            isCleanSession = true
            isAutomaticReconnect = true
            connectionTimeout = TIMEOUT
            keepAliveInterval = KEEP_ALIVE_INTERVAL
            maxInflight = MAX_IN_FLIGHT
        }
        client.setCallback(MQTTCallbackHandler())
    }

    fun connect(url: String,clientId:String,userName:String,pwd:String) {
        try {
            client.connect(options)
            isConnected.set(true)
            Log.d(TAG, "Connected to: $MQTT_BROKER_URL")
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to connect to: $MQTT_BROKER_URL", e)
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

    fun subscribe(topic: String) {
        try {
            if (isConnected.get()) {
                client.subscribe(topic)
                Log.d(TAG, "Subscribed to: $topic")
            } else {
                Log.e(TAG, "Client is not connected. Unable to subscribe.")
            }
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to subscribe to topic: $topic", e)
        }
    }

    fun publish(topic: String, message: String) {
        try {
            if (isConnected.get()) {
                val mqttMessage = MqttMessage().apply {
                    payload = message.toByteArray()
                    qos = QOS
                    isRetained = false
                }
                client.publish(topic, mqttMessage)
                Log.d(TAG, "Published message to: $topic")
            } else {
                Log.e(TAG, "Client is not connected. Unable to publish.")
            }
        } catch (e: MqttException) {
            Log.e(TAG, "Failed to publish message to topic: $topic", e)
        }
    }

    inner class MQTTCallbackHandler : MqttCallback {

        override fun connectionLost(cause: Throwable?) {
            isConnected.set(false)
            Log.e(TAG, "Connection lost!")
            // Here you could implement a reconnect mechanism
        }

        override fun messageArrived(topic: String, message: MqttMessage) {
            Log.d(TAG, "Received message: ${String(message.payload)} in $topic")
            // Handle the message, perhaps update UI or store it for later use
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d(TAG, "Delivery Complete")
        }
    }

    fun isConnected() = isConnected.get()
}
