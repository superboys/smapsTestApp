package com.unito.smapssdk.library;

import android.util.Log;

import com.unito.smapssdk.UnitoManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// json转byte指令
public class JsonConvertCom {

    // 策略列表
    public static final Map<String, Consumer<Map>> CONVERT_MAP = new HashMap<>();

    static {
        CONVERT_MAP.put(JsonConstant.bleConnectToWaterSystem, JsonConvertCom::bleConnectToWaterSystem);
        CONVERT_MAP.put(JsonConstant.bleDisconnectFromWaterSystem, JsonConvertCom::bleDisconnectFromWaterSystem);
        CONVERT_MAP.put(JsonConstant.waterSystemAllData, JsonConvertCom::waterSystemAllData);
        CONVERT_MAP.put(JsonConstant.temperatureHotWater, JsonConvertCom::temperatureHotWater);
        CONVERT_MAP.put(JsonConstant.temperatureColdWater, JsonConvertCom::temperatureColdWater);
        CONVERT_MAP.put(JsonConstant.sodaIntensity, JsonConvertCom::sodaIntensity);
        CONVERT_MAP.put(JsonConstant.flavorIntensity, JsonConvertCom::flavorIntensity);
        CONVERT_MAP.put(JsonConstant.timer1, JsonConvertCom::timer1);
        CONVERT_MAP.put(JsonConstant.timer2, JsonConvertCom::timer2);
        CONVERT_MAP.put(JsonConstant.consumableAllData, JsonConvertCom::consumableAllData);
        CONVERT_MAP.put(JsonConstant.sterilizationTimer, JsonConvertCom::sterilizationTimer);
        CONVERT_MAP.put(JsonConstant.childProtect, JsonConvertCom::childProtect);
        CONVERT_MAP.put(JsonConstant.autoSodaMake, JsonConvertCom::autoSodaMake);
        CONVERT_MAP.put(JsonConstant.realTimeClock, JsonConvertCom::realTimeClock);
        CONVERT_MAP.put(JsonConstant.jarPushTime, JsonConvertCom::jarPushTime);
        CONVERT_MAP.put(JsonConstant.singleClickWaterOutTime, JsonConvertCom::singleClickWaterOutTime);
        CONVERT_MAP.put(JsonConstant.knobStatus, JsonConvertCom::knobStatus);
        CONVERT_MAP.put(JsonConstant.version, JsonConvertCom::version);
        CONVERT_MAP.put(JsonConstant.flavorIntensityParameters, JsonConvertCom::flavorIntensityParameters);
        CONVERT_MAP.put(JsonConstant.flavorCleaningParameters, JsonConvertCom::flavorCleaningParameters);
        CONVERT_MAP.put(JsonConstant.flavorDisinfectionParameters, JsonConvertCom::flavorDisinfectionParameters);
        CONVERT_MAP.put(JsonConstant.sodaWaterInParameters, JsonConvertCom::sodaWaterInParameters);
        CONVERT_MAP.put(JsonConstant.sodaCo2InParameters, JsonConvertCom::sodaCo2InParameters);
        CONVERT_MAP.put(JsonConstant.sodaCo2InOnOffParameters, JsonConvertCom::sodaCo2InOnOffParameters);
        CONVERT_MAP.put(JsonConstant.washPipeParameters, JsonConvertCom::washPipeParameters);
        CONVERT_MAP.put(JsonConstant.systemPowerOn, JsonConvertCom::systemPowerOn);
        CONVERT_MAP.put(JsonConstant.waterType, JsonConvertCom::waterType);
        CONVERT_MAP.put(JsonConstant.pullOutFunction, JsonConvertCom::pullOutFunction);
        CONVERT_MAP.put(JsonConstant.waterOutStatus, JsonConvertCom::waterOutStatus);
        CONVERT_MAP.put(JsonConstant.activateFlavorDisinfection, JsonConvertCom::activateFlavorDisinfection);
        CONVERT_MAP.put(JsonConstant.startFlash, JsonConvertCom::startFlash);
        CONVERT_MAP.put(JsonConstant.leakageSensorFunction, JsonConvertCom::leakageSensorFunction);
        CONVERT_MAP.put(JsonConstant.error, JsonConvertCom::error);
        CONVERT_MAP.put(JsonConstant.changeLanguage, JsonConvertCom::changeLanguage);
        CONVERT_MAP.put(JsonConstant.startOTA, JsonConvertCom::startOTA);


    }

    private static void changeLanguage(Map map) {

//        Log.e("changeLanguage", JsonUtils.mapToJson(map));
//        if (null != map) {
//            if (map.get("msgType").equals("set")) {
//                
//                Map value = (Map) map.get("value");
//                int leakageSensorFunction = ((boolean) value.get("leakageSensorFunction")) ? 2 : 1;
//
//                byte[] bytes = BLEConstant.(leakageSensorFunction);
//                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
//            } else {
//                byte[] bytes = BLEConstant.getRequestForEnableLeakageSensor();
//                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
//            }
//        }
    }
    private static void startOTA(Map map) {

        Log.e("leakageSensorFunction", JsonUtils.mapToJson(map));
        if (null != map) {
//            if (map.get("msgType").equals("set")) {
//                
//                Map value = (Map) map.get("value");
//                int leakageSensorFunction = ((boolean) value.get("leakageSensorFunction")) ? 2 : 1;
//
//                byte[] bytes = BLEConstant.setRequestForEnableLeakageSensor(leakageSensorFunction);
//                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
//            } else {
//                byte[] bytes = BLEConstant.getRequestForEnableLeakageSensor();
//                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
//            }
        }
    }
    public static void leakageSensorFunction(Map map) {

        Log.e("leakageSensorFunction", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int leakageSensorFunction = ((boolean) value.get("leakageSensorFunction")) ? 2 : 1;

                byte[] bytes = BLEConstant.setRequestForEnableLeakageSensor(leakageSensorFunction);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForEnableLeakageSensor();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void error(Map map) {

        Log.e("error", JsonUtils.mapToJson(map));
        if (null != map) {
            
            Map value = (Map) map.get("value");

            byte[] bytes = BLEConstant.getRequestForGetError();
            LiveDataBus.get().with("sendDataToDir").setValue(bytes);
        }
    }

    public static void activateFlavorDisinfection(Map map) {

        Log.e("activateFlavorDisi", JsonUtils.mapToJson(map));
        if (null != map) {
            
            Map value = (Map) map.get("value");
            int activateFlavorDisinfection = (int) value.get("activateFlavorDisinfection");

            byte[] bytes = BLEConstant.setRequestForActivateFlavorDisinfection(activateFlavorDisinfection);
            LiveDataBus.get().with("sendDataToDir").setValue(bytes);
        }
    }

    public static void startFlash(Map map) {

        Log.e("pullOutFunction", JsonUtils.mapToJson(map));
        if (null != map) {
            
            Map value = (Map) map.get("value");
            int startFlash = (int) value.get("startFlash");

            byte[] bytes = BLEConstant.setRequestForDirModeFlashProcess(startFlash);
            LiveDataBus.get().with("sendDataToDir").setValue(bytes);
        }
    }

    public static void pullOutFunction(Map map) {

        Log.e("pullOutFunction", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int pullOutFunction = ((boolean) value.get("pullOutFunction")) ? 2 : 1;

                byte[] bytes = BLEConstant.setRequestForEnablePullOutSensor(pullOutFunction);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForEnablePullOutSensor();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void waterOutStatus(Map map) {

        Log.e("waterOutStatus", JsonUtils.mapToJson(map));
        if (null != map) {
            
            Map value = (Map) map.get("value");
            int waterType = (int) value.get("waterType");
            if (map.get("msgType").equals("set")) {
                byte[] bytes = BLEConstant.setRequestForWateroutStatus(waterType);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForWaterOutStatus(waterType);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void waterType(Map map) {

        Log.e("waterType", JsonUtils.mapToJson(map));
        if (null != map) {
            byte[] bytes = BLEConstant.getRequestForWaterType();
            LiveDataBus.get().with("sendDataToDir").setValue(bytes);
        }
    }

    public static void washPipeParameters(Map map) {

        Log.e("washPipeParameters", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int boilingWaterFlushTime = (value.get("boilingWaterFlushTime") instanceof Integer) ? (((int) (value.get("boilingWaterFlushTime"))) * 10) : (int) (((double) value.get("boilingWaterFlushTime")) * 10);
                int sparkleWaterFlushTime = (value.get("sparkleWaterFlushTime") instanceof Integer) ? (((int) (value.get("sparkleWaterFlushTime"))) * 10) : (int) (((double) value.get("sparkleWaterFlushTime")) * 10);

                byte[] bytes = BLEConstant.setRequestForWashPipeParameters(boilingWaterFlushTime, sparkleWaterFlushTime);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForWashPipeParameters();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void systemPowerOn(Map map) {

        Log.e("systemPowerOn", JsonUtils.mapToJson(map));
        if (null != map) {
            
            Map value = (Map) map.get("value");
            int systemPowerOn = ((boolean) value.get("systemPowerOn")) ? 2 : 1;
            if (map.get("msgType").equals("set")) {
                byte[] bytes = BLEConstant.setRequestForPowerOn(systemPowerOn);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForPowerOn(systemPowerOn);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void sodaCo2InOnOffParameters(Map map) {

        Log.e("sodaCo2InOnOffPar", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int co2InOnOffStatus = (int) value.get("co2InOnOffStatus");
                int co2InOnNominalTime = (value.get("co2InOnNominalTime") instanceof Integer) ? (((int) (value.get("co2InOnNominalTime"))) * 10) : (int) (((double) value.get("co2InOnNominalTime")) * 10);
                int co2InOnMinTime = (value.get("co2InOnMinTime") instanceof Integer) ? (((int) (value.get("co2InOnMinTime"))) * 10) : (int) (((double) value.get("co2InOnMinTime")) * 10);
                int co2InOnMaxTime = (value.get("co2InOnMaxTime") instanceof Integer) ? (((int) (value.get("co2InOnMaxTime"))) * 10) : (int) (((double) value.get("co2InOnMaxTime")) * 10);
                int co2InOffNominalTime = (value.get("co2InOffNominalTime") instanceof Integer) ? (((int) (value.get("co2InOffNominalTime"))) * 10) : (int) (((double) value.get("co2InOffNominalTime")) * 10);
                int co2InOffMinTime = (value.get("co2InOffMinTime") instanceof Integer) ? (((int) (value.get("co2InOffMinTime"))) * 10) : (int) (((double) value.get("co2InOffMinTime")) * 10);
                int co2InOffMaxTime = (value.get("co2InOffMaxTime") instanceof Integer) ? (((int) (value.get("co2InOffMaxTime"))) * 10) : (int) (((double) value.get("co2InOffMaxTime")) * 10);

                byte[] bytes = BLEConstant.setRequestForSodaCo2InOnOffParameters(co2InOnOffStatus, co2InOnNominalTime, co2InOnMinTime, co2InOnMaxTime, co2InOffNominalTime, co2InOffMinTime, co2InOffMaxTime);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForSodaCo2InOnOffParameters();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void sodaWaterInParameters(Map map) {

        Log.e("sodaWaterInParameters", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int sodaWaterInMinTime = (int) value.get("sodaWaterInMinTime");
                int sodaWaterInMaxTime = (int) value.get("sodaWaterInMaxTime");
                int sodaWaterInTemperature = (value.get("sodaWaterInTemperature") instanceof Integer) ? (((int) (value.get("sodaWaterInTemperature"))) * 10) : (int) (((double) value.get("sodaWaterInTemperature")) * 10);
                byte[] bytes = BLEConstant.setRequestForSodaWaterParameters(sodaWaterInMinTime, sodaWaterInMaxTime, sodaWaterInTemperature);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForSodaWaterParameters();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void sodaCo2InParameters(Map map) {

        Log.e("sodaCo2InParameters", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int co2InMinTime = (value.get("co2InMinTime") instanceof Integer) ? (((int) (value.get("co2InMinTime"))) * 10) : (int) (((double) value.get("co2InMinTime")) * 10);
                int co2InMaxTime = (value.get("co2InMaxTime") instanceof Integer) ? (((int) (value.get("co2InMaxTime"))) * 10) : (int) (((double) value.get("co2InMaxTime")) * 10);
                int co2SoftLevelTime = (value.get("co2SoftLevelTime") instanceof Integer) ? (((int) (value.get("co2SoftLevelTime"))) * 10) : (int) (((double) value.get("co2SoftLevelTime")) * 10);
                int co2MediumLevelTime = (value.get("co2MediumLevelTime") instanceof Integer) ? (((int) (value.get("co2MediumLevelTime"))) * 10) : (int) (((double) value.get("co2MediumLevelTime")) * 10);
                int co2IntenseLevelTime = (value.get("co2IntenseLevelTime") instanceof Integer) ? (((int) (value.get("co2IntenseLevelTime"))) * 10) : (int) (((double) value.get("co2IntenseLevelTime")) * 10);

                byte[] bytes = BLEConstant.setRequestForSodaCo2InParameters(co2InMinTime, co2InMaxTime, co2SoftLevelTime, co2MediumLevelTime, co2IntenseLevelTime);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForSodaSodaCo2InParameters();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void flavorCleaningParameters(Map map) {

        Log.e("flavorCleaningPa", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int flavorReverseTime = (value.get("flavorReverseTime") instanceof Integer) ? (((int) (value.get("flavorReverseTime"))) * 10) : (int) (((double) value.get("flavorReverseTime")) * 10);
                int flavorCleaningInterval = (int) value.get("flavorCleaningInterval");
                byte[] bytes = BLEConstant.setRequestForFlavorParametersCleaning(flavorReverseTime, flavorCleaningInterval);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForFlavorParametersCleaning();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void flavorDisinfectionParameters(Map map) {

        Log.e("flavorDisinfectionPa", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int flavorDisinfectionCycles = (int) value.get("disinfectionCycles");
                int flavorDisinfectionForwardTime = (value.get("disinfectionForwardTime") instanceof Integer) ? (((int) value.get("disinfectionForwardTime")) * 10) : (int) (((double) value.get("disinfectionForwardTime")) * 10);
                int flavorDisinfectionBackwardsTime = (value.get("disinfectionBackwardsTime") instanceof Integer) ? (((int) value.get("disinfectionBackwardsTime")) * 10) : (int) (((double) value.get("disinfectionBackwardsTime")) * 10);
                int flavorDisinfectionVolumeThreshold = (int) value.get("disinfectionVolumeThreshold");
                int low = flavorDisinfectionVolumeThreshold % 256;
                int high = flavorDisinfectionVolumeThreshold / 256;
                int flavorDisinfectionDaysThreshold = (int) value.get("disinfectionDaysThreshold");
                byte[] bytes = BLEConstant.setRequestForFlavorParametersDisinfection(flavorDisinfectionCycles, flavorDisinfectionForwardTime, flavorDisinfectionBackwardsTime, low, high, flavorDisinfectionDaysThreshold);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForFlavorParametersDisinfection();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void version(Map map) {

        Log.e("version", JsonUtils.mapToJson(map));
        if (null != map) {
            
            Map value = (Map) map.get("value");
            int boardType = ((int) value.get("boardType"));
            byte[] bytes = BLEConstant.getRequestVersion(boardType);
            LiveDataBus.get().with("sendDataToDir").setValue(bytes);
        }
    }

    public static void flavorIntensityParameters(Map map) {

        Log.e("flavorIntenParameters", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int softFlavorInjection = (value.get("softFlavorInjection") instanceof Integer) ? (((int) (value.get("softFlavorInjection"))) * 10) : (int) (((double) value.get("softFlavorInjection")) * 10);
                int mediumFlavorInjection = (value.get("mediumFlavorInjection") instanceof Integer) ? (((int) value.get("mediumFlavorInjection")) * 10) : (int) (((double) value.get("mediumFlavorInjection")) * 10);
                int intenseFlavorInjection = (value.get("intenseFlavorInjection") instanceof Integer) ? (((int) value.get("intenseFlavorInjection")) * 10) : (int) (((double) value.get("intenseFlavorInjection")) * 10);
                int boilingChilledFilteredWaterFlavorInjectionTime = (value.get("boilingChilledFilteredWaterFlavorInjectionTime") instanceof Integer) ? (((int) value.get("boilingChilledFilteredWaterFlavorInjectionTime")) * 10) : (int) (((double) value.get("boilingChilledFilteredWaterFlavorInjectionTime")) * 10);
                int sodaWaterFlavorInjectionTime = (value.get("sodaWaterFlavorInjectionTime") instanceof Integer) ? (((int) value.get("sodaWaterFlavorInjectionTime")) * 10) : (int) (((double) value.get("sodaWaterFlavorInjectionTime")) * 10);
                byte[] bytes = BLEConstant.setRequestForFlavorParameters(softFlavorInjection, mediumFlavorInjection, intenseFlavorInjection, boilingChilledFilteredWaterFlavorInjectionTime, sodaWaterFlavorInjectionTime);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForFlavorParameters();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void singleClickWaterOutTime(Map map) {

        Log.e("singleClickWaterOutTime", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int singleClick = ((boolean) value.get("singleClick")) ? 2 : 1;
                int singleClickWaterOutTime = (int) value.get("singleClickWaterOutTime");
                byte[] bytes = BLEConstant.setSingleClick(singleClickWaterOutTime, singleClick);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForSingleClick();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void knobStatus(Map map) {

        Log.e("knobStatus", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                byte reset = (byte) (((boolean) value.get("reset")) ? 0x01 : 0x02);
                byte[] bytes = BLEConstant.setRequestForPairing(reset);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForPairing();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void jarPushTime(Map map) {

        Log.e("jarPushTime", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int jarPushTime = (int) value.get("jarPushTime");
                byte[] bytes = BLEConstant.setRequestForAutoFillTimer(jarPushTime);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForAutoFillTimer();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void realTimeClock(Map map) {

        Log.e("realTimeClock", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                String y = ((String) value.get("years")).substring(2,4);
                int years = Integer.valueOf(y);
                int months = Integer.valueOf((String) value.get("months"));
                int days = Integer.valueOf((String) value.get("days"));
                int weekday = Integer.valueOf((String) value.get("weekday"));
                int hours = Integer.valueOf((String) value.get("hours"));
                int minutes = Integer.valueOf((String) value.get("minutes"));
                int seconds = Integer.valueOf((String) value.get("seconds"));
                byte[] bytes = BLEConstant.setRequestForWaterSystemClock(years, months, weekday, days, hours, minutes, seconds);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForWaterSystemClock();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void childProtect(Map map) {

        Log.e("childProtect", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int childProtect = ((boolean) value.get("childProtect")) ? 2 : 1;
                byte[] bytes = BLEConstant.setRequestForChildProtect(childProtect);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForChildProtect();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void autoSodaMake(Map map) {

        Log.e("autoSodaMake", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int autoSodaMake = ((boolean) value.get("autoSodaMake")) ? 2 : 1;
                byte[] bytes = BLEConstant.setRequestForAutoSodaReFill(autoSodaMake);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForAutoSodaReFill();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void consumableAllData(Map map) {

        Log.e("timer1", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");

                int replacementId = (int) value.get("consumableId");
                int replacementType = (int) value.get("consumableType");
                int quantity = (int) value.get("initialValue");
                int expirationInDays = (int) value.get("initialExpirationDays");
                byte[] bytes = BLEConstant.setRequestForConsumableNew(replacementId, replacementType, quantity, expirationInDays);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForConsumableNew();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void timer2(Map map) {

        Log.e("timer2", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                int[] timerarray = new int[9];

                
                Map value = (Map) map.get("value");
                Map days = (Map) value.get("days");
                String bitDays = "0";
                bitDays += ((Boolean) days.get("day7")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day6")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day5")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day4")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day3")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day2")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day1")) ? "1" : "0";
                int intDays = Integer.parseInt(bitDays, 2);
                Log.e("intDays-->", intDays + "   " + bitDays);
                timerarray[0] = intDays;

                Map interval1 = (Map) value.get("interval1");
                if (null != interval1) {
                    Log.e("interval1===","null");
                }

                String strFrom = (String) interval1.get("from");
                String to = (String) interval1.get("to");
                boolean active = (boolean) interval1.get("active");
                boolean is24h = (boolean) interval1.get("24h");
                if (!is24h) {
                    String[] charFrom = strFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[1] = byteFrom;
                    Log.e("1from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = to.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = toTotalMin + (1 << 7) * 1;
                    timerarray[2] = byteTO;
                    Log.e("1to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);

                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[1] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = 97 + (1 << 7) * 1;
                    timerarray[2] = byteTo;
                    Log.e("1to--->", "24h");
                }

                Map interval2 = (Map) value.get("interval2");

                String interval2StrFrom = (String) interval2.get("from");
                String interval2To = (String) interval2.get("to");
                boolean interval2Active = (boolean) interval2.get("active");
                boolean interval2Is24h = (boolean) interval2.get("24h");
                if (!interval2Is24h) {
                    String[] charFrom = interval2StrFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (interval2Active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[3] = byteFrom;
                    Log.e("2from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = interval2To.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = toTotalMin + (1 << 7) * 1;
                    timerarray[4] = byteTO;
                    Log.e("2to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);
                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[3] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = 97 + (1 << 7) * 1;
                    timerarray[4] = byteTo;
                    Log.e("1to--->", "24h");
                }

                Map interval3 = (Map) value.get("interval3");

                String interval3StrFrom = (String) interval3.get("from");
                String interval3To = (String) interval3.get("to");
                boolean interval3Active = (boolean) interval3.get("active");
                boolean interval3Is24h = (boolean) interval3.get("24h");
                if (!interval3Is24h) {
                    String[] charFrom = interval3StrFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (interval3Active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[5] = byteFrom;
                    Log.e("2from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = interval3To.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = toTotalMin + (1 << 7) * 1;
                    timerarray[6] = byteTO;
                    Log.e("3to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);

                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[5] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = (active) ? 97 + (1 << 7) * 1 : 97 + (1 << 7) * 0;
                    timerarray[6] = byteTo;
                    Log.e("1to--->", "24h");
                }

                Map interval4 = (Map) value.get("interval4");

                String interval4StrFrom = (String) interval4.get("from");
                String interval4To = (String) interval4.get("to");
                boolean interval4Active = (boolean) interval4.get("active");
                boolean interval4Is24h = (boolean) interval4.get("24h");
                if (!interval4Is24h) {
                    String[] charFrom = interval4StrFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (interval4Active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[7] = byteFrom;
                    Log.e("2from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = interval4To.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = totalMin + (1 << 7) * 1;
                    timerarray[8] = byteTO;
                    Log.e("4to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);

                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[7] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = (active) ? 97 + (1 << 7) * 1 : 97 + (1 << 7) * 0;
                    timerarray[8] = byteTo;
                    Log.e("1to--->", "24h");
                }

                byte[] bytes = BLEConstant.setRequestForTimer2(timerarray);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForTimer2();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void timer1(Map map) {

        Log.e("timer1", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                int[] timerarray = new int[9];

                
                Map value = (Map) map.get("value");
                Map days = (Map) value.get("days");
                String bitDays = "0";
                bitDays += ((Boolean) days.get("day7")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day6")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day5")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day4")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day3")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day2")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day1")) ? "1" : "0";
                int intDays = Integer.parseInt(bitDays, 2);
                Log.e("intDays-->", intDays + "   " + bitDays);
                timerarray[0] = intDays;

                Map interval1 = (Map) value.get("interval1");

                String strFrom = (String) interval1.get("from");
                String to = (String) interval1.get("to");
                boolean active = (boolean) interval1.get("active");
                boolean is24h = (boolean) interval1.get("24h");
                if (!is24h) {
                    String[] charFrom = strFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[1] = byteFrom;
                    Log.e("1from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = to.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = toTotalMin + (1 << 7) * 1;
                    timerarray[2] = byteTO;
                    Log.e("1to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);

                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[1] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = (active) ? 97 + (1 << 7) * 1 : 97 + (1 << 7) * 0;
                    timerarray[2] = byteTo;
                    Log.e("1to--->", "24h");
                }

                Map interval2 = (Map) value.get("interval2");

                String interval2StrFrom = (String) interval2.get("from");
                String interval2To = (String) interval2.get("to");
                boolean interval2Active = (boolean) interval2.get("active");
                boolean interval2Is24h = (boolean) interval2.get("24h");
                if (!interval2Is24h) {
                    String[] charFrom = interval2StrFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (interval2Active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[3] = byteFrom;
                    Log.e("2from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = interval2To.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = toTotalMin + (1 << 7) * 1;
                    timerarray[4] = byteTO;
                    Log.e("2to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);
                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[3] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = (active) ? 97 + (1 << 7) * 1 : 97 + (1 << 7) * 0;
                    timerarray[4] = byteTo;
                    Log.e("1to--->", "24h");
                }

                Map interval3 = (Map) value.get("interval3");

                String interval3StrFrom = (String) interval3.get("from");
                String interval3To = (String) interval3.get("to");
                boolean interval3Active = (boolean) interval3.get("active");
                boolean interval3Is24h = (boolean) interval3.get("24h");
                if (!interval3Is24h) {
                    String[] charFrom = interval3StrFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (interval3Active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[5] = byteFrom;
                    Log.e("2from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = interval3To.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = toTotalMin + (1 << 7) * 1;
                    timerarray[6] = byteTO;
                    Log.e("3to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);

                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[5] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = (active) ? 97 + (1 << 7) * 1 : 97 + (1 << 7) * 0;
                    timerarray[6] = byteTo;
                    Log.e("1to--->", "24h");
                }

                Map interval4 = (Map) value.get("interval4");

                String interval4StrFrom = (String) interval4.get("from");
                String interval4To = (String) interval4.get("to");
                boolean interval4Active = (boolean) interval4.get("active");
                boolean interval4Is24h = (boolean) interval4.get("24h");
                if (!interval4Is24h) {
                    String[] charFrom = interval4StrFrom.split(":");
                    int intFromH = Integer.valueOf(charFrom[0]);
                    int intFromM = Integer.valueOf(charFrom[1]);
                    int totalMin = (intFromH * 60 + intFromM) / 15 + 1;
                    int byteFrom = (interval4Active) ? totalMin + (1 << 7) * 1 : totalMin + (1 << 7) * 0;
                    timerarray[7] = byteFrom;
                    Log.e("2from--->", "intFromH-->" + intFromH + "   " + "intFromM-->" + intFromM + "    " + "totalMin-->" + totalMin + "   " + "byteFrom-->" + byteFrom);

                    String[] charTo = interval4To.split(":");
                    int intToH = Integer.valueOf(charTo[0]);
                    int intToM = Integer.valueOf(charTo[1]);
                    int toTotalMin = (intToH * 60 + intToM) / 15 + 1;
                    int byteTO = totalMin + (1 << 7) * 1;
                    timerarray[8] = byteTO;
                    Log.e("4to--->", "intToH-->" + intToH + "   " + "intToM-->" + intToM + "    " + "toTotalMin-->" + toTotalMin + "   " + "byteFrom-->" + byteTO);

                } else {
                    int byteFrom = (active) ? 1 + (1 << 7) * 1 : 1 + (1 << 7) * 0;
                    timerarray[7] = byteFrom;
                    Log.e("1from--->", "24h");

                    int byteTo = (active) ? 97 + (1 << 7) * 1 : 97 + (1 << 7) * 0;
                    timerarray[8] = byteTo;
                    Log.e("1to--->", "24h");
                }

                byte[] bytes = BLEConstant.setRequestForTimer1(timerarray);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForTimer1();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void bleConnectToWaterSystem(Map map) {
        if (UnitoManager.isWrite) {
            UnitoManager.msgId++;
            UnitoManager.isWrite = false;
        }
    }

    public static void bleDisconnectFromWaterSystem(Map map) {
        if (UnitoManager.isWrite) {
            UnitoManager.msgId++;
            UnitoManager.isWrite = false;
        }
    }

    public static void waterSystemAllData(Map map) {
        if (null != map) {
            byte[] bytes = BLEConstant.getRequestForFlavor();
            LiveDataBus.get().with("sendDataToDir").setValue(bytes);
        }
    }

    public static void temperatureColdWater(Map map) {

        Log.e("temperatureColdWater", JsonUtils.mapToJson(map));
        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int desiredColdWaterTemperature = (int) value.get("desiredColdWaterTemperature");
//                int hysteresisTemperature = (int) value.get("hysteresisTemperature");
                byte[] bytes = BLEConstant.setRequestForColdWater(desiredColdWaterTemperature, 3);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForColdWater();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void temperatureHotWater(Map map) {

        if (null != map) {
            if (map.get("msgType").equals("set")) {
                Map value = (Map) map.get("value");
                int desiredHotWaterTemperature = (int) value.get("desiredHotWaterTemperature");
                byte[] bytes = BLEConstant.setRequestForHotWater(desiredHotWaterTemperature, 2);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForHotWater();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void sodaIntensity(Map map) {

        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int sodaIntensity = (int) value.get("sodaIntensity");
                byte[] bytes = BLEConstant.setRequestForSodaWater(sodaIntensity);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForSodaWater();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void flavorIntensity(Map map) {

        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                int flavorIntensity = (int) value.get("flavorIntensity");
                byte[] bytes = BLEConstant.setRequestForFlavorWater(flavorIntensity);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForFlavorWater();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }

    public static void sterilizationTimer(Map map) {

        if (null != map) {
            if (map.get("msgType").equals("set")) {
                
                Map value = (Map) map.get("value");
                Map days = (Map) value.get("days");
                String bitDays = "0";
                bitDays += ((Boolean) days.get("day7")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day6")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day5")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day4")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day3")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day2")) ? "1" : "0";
                bitDays += ((Boolean) days.get("day1")) ? "1" : "0";
                int intDays = Integer.parseInt(bitDays, 2);

                String strFrom = (String) value.get("startTime");

                String[] charFrom = strFrom.split(":");
                int intFromH = Integer.valueOf(charFrom[0]);
                int intFromM = Integer.valueOf(charFrom[1]);
                int totalMin = (intFromH * 60 + intFromM) / 15 + 1;

                byte[] bytes = BLEConstant.setRequestForSterialization(totalMin, intDays);
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            } else {
                byte[] bytes = BLEConstant.getRequestForSterialization();
                LiveDataBus.get().with("sendDataToDir").setValue(bytes);
            }
        }
    }


}
