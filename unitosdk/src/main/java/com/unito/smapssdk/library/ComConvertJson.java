package com.unito.smapssdk.library;

import android.util.Log;

import com.unito.smapssdk.UnitoManager;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

// byte[]指令转成json格式数据
public class ComConvertJson {

    // 策略列表
    public static final Map<Byte, Consumer<byte[]>> CONVERT_MAP = new HashMap<>();

    static {
        CONVERT_MAP.put(ComConstant.waterSystemAllData, ComConvertJson::waterSystemAllData);
        CONVERT_MAP.put(ComConstant.temperatureHotWater, ComConvertJson::temperatureHotWater);
        CONVERT_MAP.put(ComConstant.temperatureColdWater, ComConvertJson::temperatureColdWater);
        CONVERT_MAP.put(ComConstant.sodaIntensity, ComConvertJson::sodaIntensity);
        CONVERT_MAP.put(ComConstant.flavorIntensity, ComConvertJson::flavorIntensity);
        CONVERT_MAP.put(ComConstant.timer1, ComConvertJson::timer1);
        CONVERT_MAP.put(ComConstant.timer2, ComConvertJson::timer2);
        CONVERT_MAP.put(ComConstant.consumableAllData, ComConvertJson::consumableAllData);
        CONVERT_MAP.put(ComConstant.sterilizationTimer, ComConvertJson::sterilizationTimer);
        CONVERT_MAP.put(ComConstant.childProtect, ComConvertJson::childProtect);
        CONVERT_MAP.put(ComConstant.autoSodaMake, ComConvertJson::autoSodaMake);
        CONVERT_MAP.put(ComConstant.realTimeClock, ComConvertJson::realTimeClock);
        CONVERT_MAP.put(ComConstant.jarPushTime, ComConvertJson::jarPushTime);
        CONVERT_MAP.put(ComConstant.singleClickWaterOutTime, ComConvertJson::singleClickWaterOutTime);
        CONVERT_MAP.put(ComConstant.knobStatus, ComConvertJson::knobStatus);
        CONVERT_MAP.put(ComConstant.version, ComConvertJson::version);
        CONVERT_MAP.put(ComConstant.flavorIntensityParameters, ComConvertJson::flavorIntensityParameters);
        CONVERT_MAP.put(ComConstant.flavorCleaningParameters, ComConvertJson::flavorCleaningParameters);
        CONVERT_MAP.put(ComConstant.flavorDisinfectionParameters, ComConvertJson::flavorDisinfectionParameters);
        CONVERT_MAP.put(ComConstant.sodaWaterInParameters, ComConvertJson::sodaWaterInParameters);
        CONVERT_MAP.put(ComConstant.sodaCo2InParameters, ComConvertJson::sodaCo2InParameters);
        CONVERT_MAP.put(ComConstant.sodaCo2InOnOffParameters, ComConvertJson::sodaCo2InOnOffParameters);
        CONVERT_MAP.put(ComConstant.washPipeParameters, ComConvertJson::washPipeParameters);
        CONVERT_MAP.put(ComConstant.systemPowerOn, ComConvertJson::systemPowerOn);
        CONVERT_MAP.put(ComConstant.waterType, ComConvertJson::waterType);
        CONVERT_MAP.put(ComConstant.pullOutFunction, ComConvertJson::pullOutFunction);
        CONVERT_MAP.put(ComConstant.waterOutStatus, ComConvertJson::waterOutStatus);
        CONVERT_MAP.put(ComConstant.activateFlavorDisinfection, ComConvertJson::activateFlavorDisinfection);
        CONVERT_MAP.put(ComConstant.startFlash, ComConvertJson::startFlash);
        CONVERT_MAP.put(ComConstant.leakageSensorFunction, ComConvertJson::leakageSensorFunction);
        CONVERT_MAP.put(ComConstant.error, ComConvertJson::error);


    }

    private static void leakageSensorFunction(byte[] bytes) {
        Log.e("leakageSensorFun-->", Utils.bytesToHex(bytes));
        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "leakageSensorFunction");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->",JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "leakageSensorFunction");
            Map value = new LinkedHashMap();
            value.put("leakageSensorFunction", (bytes[8] == 0x01) ? false : true);

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }
    private static void error(byte[] bytes) {
        Log.e("activateFlavorDis-->", Utils.bytesToHex(bytes));
        Map map = new LinkedHashMap();
        map.put("msgId", UnitoManager.msgId);
        map.put("destination", "appBle");
        map.put("source", "waterSystem");
        map.put("msgType", "response");
        map.put("target", "activateFlavorDisinfection");
        if (bytes[7] == (byte) 0x06) {
            map.put("response", "ack");
        } else {
            map.put("response", "nack");
        }
        LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
        Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
    }

    private static void activateFlavorDisinfection(byte[] bytes) {
        Log.e("activateFlavorDis-->", Utils.bytesToHex(bytes));
        Map map = new LinkedHashMap();
        map.put("msgId", UnitoManager.msgId);
        map.put("destination", "appBle");
        map.put("source", "waterSystem");
        map.put("msgType", "response");
        map.put("target", "activateFlavorDisinfection");
        if (bytes[7] == (byte) 0x06) {
            map.put("response", "ack");
        } else {
            map.put("response", "nack");
        }
        LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
        Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
    }

    private static void startFlash(byte[] bytes) {
        Log.e("startFlash-->", Utils.bytesToHex(bytes));
        Map map = new LinkedHashMap();
        map.put("msgId", UnitoManager.msgId);
        map.put("destination", "appBle");
        map.put("source", "waterSystem");
        map.put("msgType", "response");
        map.put("target", "startFlash");
        if (bytes[7] == (byte) 0x06) {
            map.put("response", "ack");
        } else {
            map.put("response", "nack");
        }
        LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
        Log.e("comConvertJson-->", JsonUtils.mapToJson(map));

    }

    private static void pullOutFunction(byte[] bytes) {
        Log.e("pullOutFunction-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "pullOutFunction");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "pullOutFunction");
            Map value = new LinkedHashMap();
            value.put("pullOutFunction", (bytes[8] == 0x01) ? false : true);

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void waterOutStatus(byte[] bytes) {
        Log.e("waterOutStatus-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "waterOutStatus");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "waterOutStatus");
            Map value = new LinkedHashMap();
            value.put("waterOutStatus", bytes[8]);
            value.put("onOffStatus", Utils.getBitsNum(bytes[9], 1, 7));
            value.put("waterType", Utils.getBitsNum(bytes[9], 4, 0));

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void waterType(byte[] bytes) {
        Log.e("waterType-->", Utils.bytesToHex(bytes));

        String[] bytes1 = Utils.bytesToHexString(bytes);
        Map map = new LinkedHashMap();
        map.put("msgId", UnitoManager.msgId);
        map.put("destination", "appBle");
        map.put("source", "waterSystem");
        map.put("msgType", "response");
        map.put("target", "waterType");
        Map value = new LinkedHashMap();
        value.put("boilingWaterExistence", (Utils.getBitsNum(bytes[8], 1, 1) == 0) ? false : true);
        value.put("chilledWaterExistence", (Utils.getBitsNum(bytes[8], 1, 2) == 0) ? false : true);
        value.put("sodaWaterExistence", (Utils.getBitsNum(bytes[8], 1, 3) == 0) ? false : true);
        value.put("filterWaterExistence", (Utils.getBitsNum(bytes[8], 1, 4) == 0) ? false : true);

        map.put("value", value);
        LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
        Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
    }

    private static void washPipeParameters(byte[] bytes) {
        Log.e("washPipeParameters-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "washPipeParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "washPipeParameters");
            Map value = new LinkedHashMap();
            value.put("boilingWaterFlushTime", ((double) Integer.valueOf(bytes1[7],16)) / 10);
            value.put("sparkleWaterFlushTime", ((double) Integer.valueOf(bytes1[8],16) / 10));

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void systemPowerOn(byte[] bytes) {
        Log.e("systemPowerOn-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "systemPowerOn");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "systemPowerOn");
            Map value = new LinkedHashMap();
            boolean systemPowerState = (bytes[8] == 0x01) ? false : true;
            value.put("systemPowerState", systemPowerState);

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void sodaCo2InOnOffParameters(byte[] bytes) {
        Log.e("sodaCo2InOnOffPa-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaCo2InOnOffParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaCo2InParameters");
            Map value = new LinkedHashMap();
            value.put("co2InOnOffStatus", bytes[7]);
            value.put("co2InOnNominalTime", ((double) bytes[8]) / 10);
            value.put("co2InOnMinTime", ((double) bytes[9]) / 10);
            value.put("co2InOnMaxTime", ((double) bytes[10]) / 10);
            value.put("co2InOffNominalTime", ((double) bytes[11]) / 10);
            value.put("co2InOffMinTime", ((double) bytes[11]) / 10);
            value.put("co2InOffMaxTime", ((double) bytes[11]) / 10);

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void sodaCo2InParameters(byte[] bytes) {
        Log.e("sodaCo2InParameters-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaCo2InParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaCo2InParameters");
            Map value = new LinkedHashMap();
            value.put("co2InMinTime", ((double) bytes[7]) / 10);
            value.put("co2InMaxTime", ((double) bytes[8]) / 10);
            value.put("co2SoftLevelTime", ((double) bytes[9]) / 10);
            value.put("co2MediumLevelTime", ((double) bytes[10]) / 10);
            value.put("co2IntenseLevelTime", ((double) bytes[11]) / 10);

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void sodaWaterInParameters(byte[] bytes) {
        Log.e("sodaWaterInPa-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaWaterInParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaWaterInParameters");
            Map value = new LinkedHashMap();
            value.put("sodaWaterInMinTime", bytes[7]);
            value.put("sodaWaterInMaxTime", bytes[8]);
            value.put("sodaWaterInTemperature", (double) (((double) Integer.valueOf(bytes1[9], 16)) / 10));
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void flavorCleaningParameters(byte[] bytes) {
        Log.e("flavorCleaningPa-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorCleaningParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorCleaningParameters");
            Map value = new LinkedHashMap();
            value.put("flavorReverseTime", ((double) bytes[7]) / 10);
            value.put("flavorCleaningInterval", bytes[8]);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void flavorDisinfectionParameters(byte[] bytes) {
        Log.e("flavorDisinfectionPa-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorDisinfectionParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorDisinfectionParameters");
            Map value = new LinkedHashMap();
            value.put("flavorDisinfectionCycles", bytes[7]);
            value.put("flavorDisinfectionForwardTime", ((double) bytes[8]) / 10);
            value.put("flavorDisinfectionBackwardsTime ", ((double) bytes[9]) / 10);
            value.put("flavorDisinfectionVolumeThreshold", Integer.parseInt(bytes1[11], 16) * 256 + Integer.parseInt(bytes1[10], 16));
            value.put("flavorDisinfectionDaysThreshold", bytes[12]);
            value.put("flavorDisinfectionRemainingQuantity", bytes[13]);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void flavorIntensityParameters(byte[] bytes) {
        Log.e("singleClickWater-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorIntensityParameters");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorIntensityParameters");
            Map value = new LinkedHashMap();
            value.put("softFlavorInjection", ((double) bytes[7]) / 10);
            value.put("mediumFlavorInjection", ((double) bytes[8]) / 10);
            value.put("intenseFlavorInjection", ((double) bytes[9]) / 10);
            value.put("boilingChilledFilteredWaterFlavorInjectionTime", ((double) bytes[10]) / 10);
            value.put("sodaWaterFlavorInjectionTime", ((double) bytes[11]) / 10);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void version(byte[] bytes) {
        Log.e("version-->", Utils.bytesToHex(bytes));

        String[] bytes1 = Utils.bytesToHexString(bytes);
        Map map = new LinkedHashMap();
        map.put("msgId", UnitoManager.msgId);
        map.put("destination", "appBle");
        map.put("source", "waterSystem");
        map.put("msgType", "response");
        map.put("target", "version");
        Map value = new LinkedHashMap();
        int boardType = bytes[7];

        String firmwareVersionYear = (bytes[8] == 0x00) ? "" : (int) bytes[8] + "." + (int) bytes[9];
        String firmwareVersionMonth = (bytes[10] == 0x00) ? "" : (int) bytes[10] + "." + (int) bytes[11];
        String firmwareVersionDay = (bytes[12] == 0x00) ? "" : (int) bytes[12] + "." + (int) bytes[13];
        String firmwareVersionNumber = String.valueOf(bytes[14]);
        String hardwareVersionNumber = (bytes[15] == 0x00) ? "" : (int) bytes[16] + "." + (int) bytes[16];

        value.put("boardType", boardType);
        value.put("firmwareVersionYear", firmwareVersionYear);
        value.put("firmwareVersionMonth", firmwareVersionMonth);
        value.put("firmwareVersionDay", firmwareVersionDay);
        value.put("firmwareVersionNumber", firmwareVersionNumber);
        value.put("hardwareVersionNumber", hardwareVersionNumber);
        map.put("value", value);
        LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
        Log.e("comConvertJson-->", JsonUtils.mapToJson(map));

    }

    private static void singleClickWaterOutTime(byte[] bytes) {
        Log.e("singleClickWater-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "singleClickWaterOutTime");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "singleClickWaterOutTime");
            Map value = new LinkedHashMap();
            boolean singleClick = (bytes[7] == 0x01) ? false : true;
            int singleClickWaterOutTime = bytes[8];
            value.put("singleClick", singleClick);
            value.put("singleClickWaterOutTime", singleClickWaterOutTime);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void knobStatus(byte[] bytes) {
        Log.e("knobStatus-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "knobStatus");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "knobStatus");
            Map value = new LinkedHashMap();

            boolean pushOn = (bytes[7] == 0x01) ? true : false;
            boolean sensorARotationOn = (bytes[8] == 0x01) ? true : false;
            boolean sensorBRotationOn = (bytes[9] == 0x01) ? true : false;
            boolean redLedOn = (bytes[10] == 0x01) ? true : false;
            boolean greenLedOn = (bytes[11] == 0x01) ? true : false;
            boolean blueLedOn = (bytes[12] == 0x01) ? true : false;
            boolean clickOnKnobPush = (bytes[13] == 0x01) ? true : false;
            boolean doubleClickOnKnobPush = (bytes[14] == 0x01) ? true : false;
            boolean tribleClickOnKnobPush = (bytes[15] == 0x01) ? true : false;
            value.put("pushOn", pushOn);
            value.put("sensorARotationOn", sensorARotationOn);
            value.put("sensorBRotationOn", sensorBRotationOn);
            value.put("redLedOn", redLedOn);
            value.put("greenLedOn", greenLedOn);
            value.put("blueLedOn", blueLedOn);
            value.put("clickOnKnobPush", clickOnKnobPush);
            value.put("doubleClickOnKnobPush", doubleClickOnKnobPush);
            value.put("tribleClickOnKnobPush", tribleClickOnKnobPush);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void jarPushTime(byte[] bytes) {
        Log.e("jarPushTime-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "jarPushTime");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "jarPushTime");
            Map value = new LinkedHashMap();

            value.put("jarPushTime", bytes[8]);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void realTimeClock(byte[] bytes) {
        Log.e("realTimeClock-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "realTimeClock");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "realTimeClock");
            Map value = new LinkedHashMap();

            value.put("years", "20"+Integer.valueOf(bytes1[7],16));
            value.put("months", String.valueOf(Integer.valueOf(bytes1[8],16)));
            value.put("days", String.valueOf(Integer.valueOf(bytes1[9],16)));
            value.put("weekday", String.valueOf(Integer.valueOf(bytes1[10],16)));
            value.put("hours", String.valueOf(Integer.valueOf(bytes1[11],16)));
            value.put("minutes", String.valueOf(Integer.valueOf(bytes1[12],16)));
            value.put("seconds", String.valueOf(Integer.valueOf(bytes1[13] ,16)));

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void childProtect(byte[] bytes) {
        Log.e("childProtect-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "childProtect");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "childProtect");
            Map value = new LinkedHashMap();
            if (bytes[8] == 0x01) {
                value.put("childProtect", false);
            } else {
                value.put("childProtect", true);
            }
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void autoSodaMake(byte[] bytes) {
        Log.e("autoSodaMake-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "autoSodaMake");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "autoSodaMake");
            Map value = new LinkedHashMap();
            if (bytes[8] == 0x01) {
                value.put("autoSodaMake", false);
            } else {
                value.put("autoSodaMake", true);
            }

            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void sterilizationTimer(byte[] bytes) {
        Log.e("sterilizationTimer-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sterilizationTimer");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sterilizationTimer");
            Map value = new LinkedHashMap();

            Map days = new LinkedHashMap();
            days.put("day0", (Utils.getBitsNum(bytes[7], 1, 0) == 0) ? false : true);
            days.put("day1", (Utils.getBitsNum(bytes[7], 1, 1) == 0) ? false : true);
            days.put("day2", (Utils.getBitsNum(bytes[7], 1, 2) == 0) ? false : true);
            days.put("day3", (Utils.getBitsNum(bytes[7], 1, 3) == 0) ? false : true);
            days.put("day4", (Utils.getBitsNum(bytes[7], 1, 4) == 0) ? false : true);
            days.put("day5", (Utils.getBitsNum(bytes[7], 1, 5) == 0) ? false : true);
            days.put("day6", (Utils.getBitsNum(bytes[7], 1, 6) == 0) ? false : true);
            value.put("days", days);

            int fromTotalMin = (Utils.getBitsNum(bytes[8], 8, 0) - 1) * 15;
            String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
            String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");
            value.put("startTime", fromH + ":" + fromM);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void consumableAllData(byte[] bytes) {
        Log.e("consumableAllData-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "consumableAllData");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "consumableAllData");
            Map value = new LinkedHashMap();

            value.put("filterId", (int) bytes[7]);
            value.put("filterStatusRemainingPercent", (int) bytes[8]);
            value.put("filterExpirationRemainingDays",Integer.valueOf(bytes1[10],16) * 255 + Integer.valueOf(bytes1[9],16));
            value.put("disinfectionExpirationRemainingDays", Integer.valueOf(bytes1[12],16) * 255 + Integer.valueOf(bytes1[11],16));
            value.put("co2Id", (int) bytes[13]);
            value.put("co2StatusRemainingPercent", (int) bytes[14]);
            value.put("flavorQuantityRemaingCc", (int) bytes[15]);
            value.put("flavorStatusRemainingPercent", (int) bytes[16]);
            map.put("value", value);

            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void timer2(byte[] bytes) {
        Log.e("timer2-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "timer2");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "timer2");

            Map value = new LinkedHashMap();
            Map days = new LinkedHashMap();
            days.put("day0", (Utils.getBitsNum(bytes[7], 1, 0) == 0) ? false : true);
            days.put("day1", (Utils.getBitsNum(bytes[7], 1, 1) == 0) ? false : true);
            days.put("day2", (Utils.getBitsNum(bytes[7], 1, 2) == 0) ? false : true);
            days.put("day3", (Utils.getBitsNum(bytes[7], 1, 3) == 0) ? false : true);
            days.put("day4", (Utils.getBitsNum(bytes[7], 1, 4) == 0) ? false : true);
            days.put("day5", (Utils.getBitsNum(bytes[7], 1, 5) == 0) ? false : true);
            days.put("day6", (Utils.getBitsNum(bytes[7], 1, 6) == 0) ? false : true);
            value.put("days", days);

            Map interval1 = new LinkedHashMap();
            boolean is24h = false;
            boolean active = (Utils.getBitsNum(bytes[8], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[8], 7, 0) == Utils.getBitsNum(bytes[9], 7, 0) || (Utils.getBitsNum(bytes[8], 7, 0) == 1) && Utils.getBitsNum(bytes[9], 7, 0) == 97) {
                is24h = true;
                interval1.put("from", "");
                interval1.put("to", "");
            } else {
                is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[8], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[9], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval1.put("from", fromH + ":" + fromM);
                interval1.put("to", toH + ":" + toM);
            }

            interval1.put("active", active);
            interval1.put("24h", is24h);

            value.put("interval1", interval1);

            Map interval2 = new LinkedHashMap();
            boolean interval2Is24h = false;
            boolean interval2Active = (Utils.getBitsNum(bytes[10], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[10], 7, 0) == Utils.getBitsNum(bytes[11], 7, 0) || (Utils.getBitsNum(bytes[10], 7, 0) == 1) && Utils.getBitsNum(bytes[11], 7, 0) == 97) {
                interval2Is24h = true;
                interval2.put("from", "");
                interval2.put("to", "");
            } else {
                interval2Is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[10], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[11], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval2.put("from", fromH + ":" + fromM);
                interval2.put("to", toH + ":" + toM);
            }

            interval2.put("active", interval2Active);
            interval2.put("24h", interval2Is24h);
            value.put("interval2", interval2);

            Map interval3 = new LinkedHashMap();
            boolean interval3Is24h = false;
            boolean interval3Active = (Utils.getBitsNum(bytes[12], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[12], 7, 0) == Utils.getBitsNum(bytes[13], 7, 0) || (Utils.getBitsNum(bytes[12], 7, 0) == 1) && Utils.getBitsNum(bytes[13], 7, 0) == 97) {
                interval3Is24h = true;
                interval3.put("from", "");
                interval3.put("to", "");
            } else {
                interval3Is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[12], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[13], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval3.put("from", fromH + ":" + fromM);
                interval3.put("to", toH + ":" + toM);
            }

            interval3.put("active", interval3Active);
            interval3.put("24h", interval3Is24h);
            value.put("interval3", interval3);


            Map interval4 = new LinkedHashMap();
            boolean interval4Is24h = false;
            boolean interval4Active = (Utils.getBitsNum(bytes[14], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[14], 7, 0) == Utils.getBitsNum(bytes[15], 7, 0) || (Utils.getBitsNum(bytes[14], 7, 0) == 1) && Utils.getBitsNum(bytes[15], 7, 0) == 97) {
                interval4Is24h = true;
                interval4.put("from", "");
                interval4.put("to", "");
            } else {
                interval4Is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[14], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[15], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval4.put("from", fromH + ":" + fromM);
                interval4.put("to", toH + ":" + toM);
            }

            interval4.put("active", interval4Active);
            interval4.put("24h", interval4Is24h);
            value.put("interval4", interval4);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void timer1(byte[] bytes) {
        Log.e("timer1-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "timer1");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "timer1");

            Map value = new LinkedHashMap();
            Map days = new LinkedHashMap();
            days.put("day0", (Utils.getBitsNum(bytes[7], 1, 0) == 0) ? false : true);
            days.put("day1", (Utils.getBitsNum(bytes[7], 1, 1) == 0) ? false : true);
            days.put("day2", (Utils.getBitsNum(bytes[7], 1, 2) == 0) ? false : true);
            days.put("day3", (Utils.getBitsNum(bytes[7], 1, 3) == 0) ? false : true);
            days.put("day4", (Utils.getBitsNum(bytes[7], 1, 4) == 0) ? false : true);
            days.put("day5", (Utils.getBitsNum(bytes[7], 1, 5) == 0) ? false : true);
            days.put("day6", (Utils.getBitsNum(bytes[7], 1, 6) == 0) ? false : true);
            value.put("days", days);

            Map interval1 = new LinkedHashMap();
            boolean is24h = false;
            boolean active = (Utils.getBitsNum(bytes[8], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[8], 7, 0) == Utils.getBitsNum(bytes[9], 7, 0) || (Utils.getBitsNum(bytes[8], 7, 0) == 1) && Utils.getBitsNum(bytes[9], 7, 0) == 97) {
                is24h = true;
                interval1.put("from", "");
                interval1.put("to", "");
            } else {
                is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[8], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[9], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval1.put("from", fromH + ":" + fromM);
                interval1.put("to", toH + ":" + toM);
            }

            interval1.put("active", active);
            interval1.put("24h", is24h);

            value.put("interval1", interval1);

            Map interval2 = new LinkedHashMap();
            boolean interval2Is24h = false;
            boolean interval2Active = (Utils.getBitsNum(bytes[10], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[10], 7, 0) == Utils.getBitsNum(bytes[11], 7, 0) || (Utils.getBitsNum(bytes[10], 7, 0) == 1) && Utils.getBitsNum(bytes[11], 7, 0) == 97) {
                interval2Is24h = true;
                interval2.put("from", "");
                interval2.put("to", "");
            } else {
                interval2Is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[10], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[11], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval2.put("from", fromH + ":" + fromM);
                interval2.put("to", toH + ":" + toM);
            }

            interval2.put("active", interval2Active);
            interval2.put("24h", interval2Is24h);
            value.put("interval2", interval2);

            Map interval3 = new LinkedHashMap();
            boolean interval3Is24h = false;
            boolean interval3Active = (Utils.getBitsNum(bytes[12], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[12], 7, 0) == Utils.getBitsNum(bytes[13], 7, 0) || (Utils.getBitsNum(bytes[12], 7, 0) == 1) && Utils.getBitsNum(bytes[13], 7, 0) == 97) {
                interval3Is24h = true;
                interval3.put("from", "");
                interval3.put("to", "");
            } else {
                interval3Is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[12], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[13], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval3.put("from", fromH + ":" + fromM);
                interval3.put("to", toH + ":" + toM);
            }

            interval3.put("active", interval3Active);
            interval3.put("24h", interval3Is24h);
            value.put("interval3", interval3);


            Map interval4 = new LinkedHashMap();
            boolean interval4Is24h = false;
            boolean interval4Active = (Utils.getBitsNum(bytes[14], 1, 7) == 0) ? false : true;
            if (Utils.getBitsNum(bytes[14], 7, 0) == Utils.getBitsNum(bytes[15], 7, 0) || (Utils.getBitsNum(bytes[14], 7, 0) == 1) && Utils.getBitsNum(bytes[15], 7, 0) == 97) {
                interval4Is24h = true;
                interval4.put("from", "");
                interval4.put("to", "");
            } else {
                interval4Is24h = false;
                int fromTotalMin = (Utils.getBitsNum(bytes[14], 7, 0) - 1) * 15;
                int toTotalMin = (Utils.getBitsNum(bytes[15], 7, 0) - 1) * 15;
                String fromH = String.format("%2d", fromTotalMin / 60).replace(" ", "0");
                String fromM = String.format("%2d", fromTotalMin % 60).replace(" ", "0");

                String toH = String.format("%2d", toTotalMin / 60).replace(" ", "0");
                String toM = String.format("%2d", toTotalMin % 60).replace(" ", "0");
                interval4.put("from", fromH + ":" + fromM);
                interval4.put("to", toH + ":" + toM);
            }

            interval4.put("active", interval4Active);
            interval4.put("24h", interval4Is24h);
            value.put("interval4", interval4);
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void waterSystemAllData(byte[] bytes) {
        Log.e("waterSystemAllData-->", Utils.bytesToHex(bytes));

//        String[] bytes1 = Utils.bytesToHexString(bytes);
//
//        Map map = new LinkedHashMap();
//        map.put("msgId", UnitoManager.msgId);
//        map.put("destination", "appBle");
//        map.put("source", "waterSystem");
//        map.put("msgType", "response");
//        map.put("target", "waterSystemAllData");
//        Map value = new LinkedHashMap();
//        value.put("physicalHotWaterTempeprature", Utils.hexToInt(bytes1[7]));
//        value.put("physicalColdWaterTemperature", Utils.hexToInt(bytes1[8]));
//        value.put("filterStatus", Utils.hexToInt(bytes1[9]));
//        value.put("co2Status", Utils.hexToInt(bytes1[10]));
//        value.put("uvStatus", Utils.hexToInt(bytes1[11]));
//
//        if (Utils.getBitsNum(bytes[12], 1, 1) == 0) {
//            value.put("timer1On", false);
//        } else {
//            value.put("timer1On", true);
//        }
//        if (Utils.getBitsNum(bytes[12], 1, 2) == 0) {
//            value.put("boilerActive", false);
//        } else {
//            value.put("boilerActive", true);
//        }
//        if (Utils.getBitsNum(bytes[12], 1, 3) == 0) {
//            value.put("chillerActive", false);
//        } else {
//            value.put("chillerActive", true);
//        }
//        if (Utils.getBitsNum(bytes[12], 1, 4) == 0) {
//            value.put("childProtect", false);
//        } else {
//            value.put("childProtect", true);
//        }
//
//        if (Utils.getBitsNum(bytes[13], 1, 1) == 0) {
//            value.put("hotWaterExistence", false);
//        } else {
//            value.put("hotWaterExistence", true);
//        }
//
//        if (Utils.getBitsNum(bytes[13], 1, 2) == 0) {
//            value.put("coldWaterExistence", false);
//        } else {
//            value.put("coldWaterExistence", true);
//        }
//
//        if (Utils.getBitsNum(bytes[13], 1, 3) == 0) {
//            value.put("sodaWaterExistence", false);
//        } else {
//            value.put("sodaWaterExistence", true);
//        }
//
//        if (Utils.getBitsNum(bytes[13], 1, 4) == 0) {
//            value.put("filterWaterExistence", false);
//        } else {
//            value.put("filterWaterExistence", true);
//        }
//
//        if (Utils.getBitsNum(bytes[13], 1, 5) == 0) {
//            value.put("mixWaterExistence", false);
//        } else {
//            value.put("mixWaterExistence", true);
//        }
//
//        if (Utils.getBitsNum(bytes[13], 1, 6) == 0) {
//            value.put("flavorExistence", false);
//        } else {
//            value.put("flavorExistence", true);
//        }
//
//        if (Utils.getBitsNum(bytes[14], 1, 1) == 0) {
//            value.put("filterAlert", false);
//        } else {
//            value.put("filterAlert", true);
//        }
//
//        if (Utils.getBitsNum(bytes[14], 1, 2) == 0) {
//            value.put("co2Alert", false);
//        } else {
//            value.put("co2Alert", true);
//        }
//
//        if (Utils.getBitsNum(bytes[14], 1, 3) == 0) {
//            value.put("tankDisinfectionAlert", false);
//        } else {
//            value.put("tankDisinfectionAlert", true);
//        }
//
//        if (Utils.getBitsNum(bytes[14], 1, 4) == 0) {
//            value.put("flavorAlert", false);
//        } else {
//            value.put("flavorAlert", true);
//        }
//
//        if (Utils.getBitsNum(bytes[14], 1, 5) == 0) {
//            value.put("flavorDisinfection20%Alert", false);
//        } else {
//            value.put("flavorDisinfection20%Alert", true);
//        }
//
//        if (Utils.getBitsNum(bytes[14], 1, 6) == 0) {
//            value.put("flavorDisinfection0%Alert", false);
//        } else {
//            value.put("flavorDisinfection0%Alert", true);
//        }
//
//        value.put("sodaIntensity", Utils.getBitsNum(bytes[15], 2, 1));
//        value.put("flavorIntensity", Utils.getBitsNum(bytes[15], 2, 3));
//        value.put("flavorQuanity", Utils.convertValue(Utils.hexToInt(bytes1[16])));
//        value.put("flavorStatus", Utils.convertValue(Utils.hexToInt(bytes1[17])));
//        map.put("value", value);
        LiveDataBus.get().with("showComConvertJson").setValue(new String(bytes, StandardCharsets.UTF_8));
//        Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
    }

    private static void temperatureColdWater(byte[] bytes) {
        Log.e("temperatureColdWater-->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "temperatureColdWater");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);

            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "temperatureColdWater");
            Map value = new LinkedHashMap();
            value.put("desiredColdWaterTemperature", Utils.hexToInt(bytes1[7]));
            value.put("hysteresisTemperature", Utils.convertValue(Utils.hexToInt(bytes1[8])));
            value.put("physicalColdWaterTemperature", Utils.hexToInt(bytes1[9]));
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void temperatureHotWater(byte[] bytes) {
        Log.e("temperatureHotWater--->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "temperatureHotWater");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));

            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);

            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "temperatureColdWater");
            Map value = new LinkedHashMap();
            value.put("desiredHotWaterTemperature", Utils.hexToInt(bytes1[7]));
            value.put("hysteresisTemperature", Utils.convertValue(Utils.hexToInt(bytes1[8])));
            value.put("physicalHotWaterTemperature", Utils.hexToInt(bytes1[9]));
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void sodaIntensity(byte[] bytes) {
        Log.e("sodaIntensity--->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaIntensity");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);

            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "sodaIntensity");
            Map value = new LinkedHashMap();
            value.put("sodaIntensity", Utils.hexToInt(bytes1[8]));
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }

    private static void flavorIntensity(byte[] bytes) {
        Log.e("flavorIntensity--->", Utils.bytesToHex(bytes));

        if (bytes[6] == (byte) 0x01) { // set
            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorIntensity");
            if (bytes[7] == (byte) 0x06) {
                map.put("response", "ack");
            } else {
                map.put("response", "nack");
            }
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        } else {  // get
            String[] bytes1 = Utils.bytesToHexString(bytes);

            Map map = new LinkedHashMap();
            map.put("msgId", UnitoManager.msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "flavorIntensity");
            Map value = new LinkedHashMap();
            value.put("flavorIntensity", Utils.hexToInt(bytes1[8]));
            map.put("value", value);
            LiveDataBus.get().with("showComConvertJson").setValue(JsonUtils.mapToJson(map));
            Log.e("comConvertJson-->", JsonUtils.mapToJson(map));
        }
    }
}
