package com.unito.smapssdk.library;

import android.util.Log;

import com.unito.smapssdk.UnitoManager;
import com.unito.smapssdk.mqtt.MQTTUtil;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.UUID;

public class BLEConstant {


    public static int CONNECTION_TYPE_LAVA = 0;
    public static int CONNECTION_TYPE_SPARKLE = 1;
    public static int CONNECTION_TYPE_INFINITY = 2;
    public static int CONNECTION_TYPE_ELEMENTS = 3;

    public static int connectionType = CONNECTION_TYPE_INFINITY;

    //Remove line checksum from Checksum command
    public static final String DIR_MODE_CHECKSUM_COMMAND = "550800F9000000070000";

    public static UUID HUB_SUCCESS_SERVICE = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static UUID HUB_SUCCESS_CHARACTERSTIC = UUID.fromString("00002a3d-0000-1000-8000-00805f9b34fb");

    public static UUID WATERSYSTEM_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static UUID WATERSYSTEM_CHARACTERSTIC = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static UUID WATERSYSTEM_DESCRIPTORS = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
//    public static final UUID WATERSYSTEM_CHARACTERSTIC = UUID.fromString("00002a3d-0000-1000-8000-00805f9b34fb");
    //TYPE CONSUMABLE

    public static final byte WATER_FILTER = 0x01;
    public static final byte CO2_TANK = 0x02;
    public static final byte FLAVOR_POUCH = 0x03;
    //Identification Success ID
    public static final byte WATER_SYSTEM_IDENFICATION_RESPONSE = (byte) 0x81;
    public static final byte HUB_IDENFICATION_RESPONSE = (byte) 0x88;


    //Destination
    public static byte WATER_SYSTEM_CONTROLLER = 0x01;
    public static byte HOOD_SYSTEM_CONTROLLER = 0x02;
    public static byte COOKER_SYSTEM_CONTROLLER = 0x04;
    public static byte APP_HUB = 0x0b;
    public static byte APP_CLOUD = 0x08;
    public static byte APP_DIRECT = 0x01;
    //Source
    public static byte APP_EXTERNAL = 0x06;
    public static byte APP_INTERNAL = 0x07;


    //Message Category

    public static byte GET = 0x01;
    public static byte SET = 0x02;
    public static byte CLEAR = 0x03;
    public static byte RESPONSE = 0x04;
    public static byte REPORT = 0x05;

    //SET RESPONSE STATUS

    public static byte ACK = 0x06;
    public static byte NACk = 0x015;

    public static int MAX_HOTWATER_TEMP = 100;
    public static int MIN_HOTWATER_TEMP = 60;

    public static int MAX_FLAVOURED_WATER_TEMP = 100;
    public static int MIN_FLAVOURED_WATER_TEMP = 0;


    public static int MAX_COLDWATER_TEMP = 25;
    public static int MIN_COLDWATER_TEMP = 3;/* TODO: Point 4.6 value=3;*/

    public static int MAX_FILTER_VAL = 100;
    public static int MAX_SODA_VAL = 100;

    public static int PROVISIONING_COM_TIME_OUT = 30;


    public static String PAGE_WATERSYTEM_STATUS = "WaterSystemFragment.class.getSimpleName()";

    //MSG ID FOR WATER SYSTEM STATUS
    public static byte MSGID_REQUEST_STATUS_WATER_SYSTEM1 = 0x33;
    public static byte MSGID_REQUEST_STATUS_WATER_SYSTEM2 = 0x0c;

    //MSG ID FOR WATER SYSTEM IDENTIFICATION
    public static byte MSGID_WATER_SYSTEM_IDENTIFICATION1 = (byte) 0xA0;
    public static byte MSGID_WATER_SYSTEM_IDENTIFICATION2 = (byte) 0x01;

    //MSG ID FOR HOT WATER
    public static byte MSGID_GET_HOTWATER1 = 0x21;
    public static byte MSGID_GET_HOTWATER2 = 0x0c;

    //MSGID FOR COLD WATER
    public static byte MSGID_GET_COLDWATER1 = 0x22;
    public static byte MSGID_GET_COLDWATER2 = 0x0c;

    //MSGID FOR SODA WATER
    public static byte MSGID_GET_SODAWATER1 = 0x23;
    public static byte MSGID_GET_SODAWATER2 = 0x0c;


    //MSGID FOR ENABLE PULL OUT SENSOR
    //0C3E
    public static byte MSGID_GET_ENABLE_PULL_OUT_SENSOR_1 = 0x3e;
    public static byte MSGID_GET_ENABLE_PULL_OUT_SENSOR_2 = 0x0c;


    //MSGID FOR ENABLE PULL OUT SENSOR
    //0C45
    public static byte MSGID_GET_AUTO_SODA_REFILL_1 = 0x45;
    public static byte MSGID_GET_AUTO_SODA_REFILL_2 = 0x0C;

    //0C46
    public static byte MSGID_GET_SINGLE_CLICK_1 = 0x46;
    public static byte MSGID_GET_SINGLE_CLICK_2 = 0x0C;

    //0C47
    public static byte MSGID_GET_ENABLE_LEAKAGE_SENSOR_1 = 0x47;
    public static byte MSGID_GET_ENABLE_LEAKAGE_SENSOR_2 = 0x0C;

    //MSGID FOR CHILD PROTECT

    public static byte MSGID_CHILD_PROTECT1 = 0x27;
    public static byte MSGID_CHILD_PROTECT2 = 0x0c;

    //MSGID FOR WATER SYSTEM CLOCK

    public static byte MSGID_WATER_SYSTEM_CLOCK1 = 0x2a;
    public static byte MSGID_WATER_SYSTEM_CLOCK2 = 0x0c;

    //MSGID FOR WATER FILTER
    public static byte MSGID_FILTER_REPLACEMENT1 = 0x25;
    public static byte MSGID_FILTER_REPLACEMENT2 = 0x0c;

    //MSGID FOR CO2 Tank Replacement
    public static byte MSGID_CO2_TANK_REPLACEMENT1 = 0x24;
    public static byte MSGID_CO2_TANK_REPLACEMENT2 = 0x0c;

    //MSGID FOR TIMER 1
    public static byte MSGID_TIMER1_1 = 0x2b;
    public static byte MSGID_TIMER1_2 = 0x0c;

    //MSGID FOR TIMER 2
    public static byte MSGID_TIMER2_1 = 0x2c;
    public static byte MSGID_TIMER2_2 = 0x0c;

    //MSGID FOR STERILIZATION
    public static byte MSGID_STERILIZATION_1 = 0x34;
    public static byte MSGID_STERILIZATION_2 = 0x0c;

    //MSGID FOR CONSUMABLE
    public static byte MSGID_CONSUMABLE_1 = 0x24;
    public static byte MSGID_CONSUMABLE_2 = 0x0c;
    public static byte MSGID_CONSUMABLE_1_NEW = 0x3a;
    public static byte MSGID_CONSUMABLE_2_NEW = 0x0c;

    //MSGID FOR COMMUNICATION
    public static byte MSGID_COMMUNICATION_1 = 0x03;
    public static byte MSGID_COMMUNICATION_2 = (byte) 0xfe;

    //MSGID FOR GET HUB's IP Address
    public static byte MSGID_HUB_IP_ADDRESS_1 = 0x0a;
    public static byte MSGID_HUB_IP_ADDRESS_2 = (byte) 0xfe;

    //MSGID FOR DIAGNOSTIC
    public static byte MSGID_DIAGNOSTIC_1 = 0x01;
    public static byte MSGID_DIAGNOSTIC_2 = 0x01;

    //MSGID FOR DIAGNOSTIC
    public static byte MSGID_UVLAMP_1 = 0x35;
    public static byte MSGID_UVLAMP_2 = 0x0c;

    //MSGID FOR HUB Version
    public static byte MSGID_HUB_VERSION_1 = 0x02;
    public static byte MSGID_HUB_VERSION_2 = (byte) 0xfe;

    //Version
    public static byte VERSION_1 = 0x3d;
    public static byte VERSION_2 = 0x0c;

    //MSGID FOR Water System Version
    public static byte MSGID_WATER_SYSTEM_VERSION_1 = 0x09;
    public static byte MSGID_WATER_SYSTEM_VERSION_2 = (byte) 0xfe;

    //MSGID FOR Water System Uptime
    public static byte MSGID_WATER_SYSTEM_UPTIME_1 = 0x26;
    public static byte MSGID_WATER_SYSTEM_UPTIME_2 = (byte) 0x0C;

    //MSGID FOR Get HUB MAC Address
    public static byte MSGID_HUB_MAC_ADDRESS_1 = 0x08;
    public static byte MSGID_HUB_MAC_ADDRESS_2 = (byte) 0xfe;

    //MSGID FOR DIR Notification
    public static byte MSGID_DIR_NOTIFICATION_1 = 0x01;
    public static byte MSGID_DIR_NOTIFICATION_2 = (byte) 0x80;

    //MSGID FOR DIR mode Flash Process
    public static byte MSGID_DIR_MODE_FLASH_PROCESS_1 = 0x38;
    public static byte MSGID_DIR_MODE_FLASH_PROCESS_2 = (byte) 0x0C;

    //MSGID FOR PAIRING PROCESS
    public static byte MSGID_PAIRING_1 = (byte) 0x39;
    public static byte MSGID_PAIRING_2 = (byte) 0x0C;
    public static byte PAIRING_SET_01 = (byte) 0x01;
    public static byte PAIRING_SET_02 = (byte) 0x02;

    //MSGID FOR DIR mode OTA Process
    public static byte MSGID_DIR_MODE_OTA_PROCESS_1 = Objects.requireNonNull(Utils.hexStringToByteArray("55"))[0];
    public static byte MSGID_DIR_MODE_OTA_PROCESS_CHECKSUM_1 = Objects.requireNonNull(Utils.hexStringToByteArray("08"))[0];
    public static byte MSGID_DIR_MODE_OTA_PROCESS_ERASE_1 = Objects.requireNonNull(Utils.hexStringToByteArray("03"))[0];
    public static byte MSGID_DIR_MODE_OTA_PROCESS_WRITE_1 = Objects.requireNonNull(Utils.hexStringToByteArray("02"))[0];
    public static byte MSGID_DIR_MODE_OTA_PROCESS_WRITE_2 = Objects.requireNonNull(Utils.hexStringToByteArray("66"))[0];
    public static byte MSGID_DIR_MODE_OTA_PROCESS_WS_RESTART_1 = Objects.requireNonNull(Utils.hexStringToByteArray("09"))[0];

    //MSGID FOR DIR mode OTA Process
    public static byte MSGID_HUB_MODE_WS_OTA_1 = 0x04;
    public static byte MSGID_HUB_MODE_WS_OTA_2 = (byte) 0xfe;

    //MSGID FOR Auto Fill Timer
    public static byte MSGID_AUTO_FILL_TIMER_1 = 0x37;
    public static byte MSGID_AUTO_FILL_TIMER_2 = (byte) 0x0C;

    //MSGID FOR Auto Fill Timer
    public static byte MSGID_FLUSH_TIMER_1 = 0x32;
    public static byte MSGID_FLUSH_TIMER_2 = (byte) 0x0C;

    //MSGID FOR FlavorParameters 1 to 5
    public static byte MSGID_FLAVOR_PARAMETERS_1 = 0x41;
    public static byte MSGID_FLAVOR_PARAMETERS_2 = (byte) 0x0C;

    //MSGID FOR FlavorParameters 6 to 7
    public static byte MSGID_FLAVOR_PARAMETERS_CLEANING_1 = 0x42;
    public static byte MSGID_FLAVOR_PARAMETERS_CLEANING_2 = (byte) 0x0C;

    //MSGID FOR FlavorParameters 6 to 7
    public static byte MSGID_FLAVOR_PARAMETERS_DISINFECTION_1 = 0x43;
    public static byte MSGID_FLAVOR_PARAMETERS_DISINFECTION_2 = (byte) 0x0C;

    //MSGID FOR FlavorWater Type
    public static byte MSGID_FLAVOR_WATER_TYPE_1 = 0x40;
    public static byte MSGID_FLAVOR_WATER_TYPE_2 = (byte) 0x0C;

    //MSGID FOR Soda Water In Parameters Type
    public static byte MSGID_SODA_WATER_PARAMETERS_1 = 0x2E;
    public static byte MSGID_SODA_WATER_PARAMETERS_2 = (byte) 0x0C;

    //MSGID FOR Soda co2 In Parameters Type
    public static byte MSGID_SODA_CO2_PARAMETERS_1 = 0x2F;
    public static byte MSGID_SODA_CO2_PARAMETERS_2 = (byte) 0x0C;

    //MSGID FOR Soda co2 In Parameters Type
    public static byte MSGID_SODA_CO2_INONOFF_PARAMETERS_1 = 0x30;
    public static byte MSGID_SODA_CO2_INONOFF_PARAMETERS_2 = (byte) 0x0C;

    //MSGID FOR Soda co2 In Parameters Type
    public static byte MSGID_WASH_PIPE_PARAMETERS_1 = 0x32;
    public static byte MSGID_WASH_PIPE_PARAMETERS_2 = (byte) 0x0C;

    //MSGID FOR Soda co2 In Parameters Type
    public static byte MSGID_POWER_ON_PARAMETERS_1 = 0x20;
    public static byte MSGID_POWER_ON_PARAMETERS_2 = (byte) 0x0C;

    //MSGID FOR water Type
    public static byte MSGID_WATER_TYPE_1 = 0x28;
    public static byte MSGID_WATER_TYPE_2 = (byte) 0x0C;

    //MSGID FOR water Type
    public static byte MSGID_WATER_OUT_STATUS_1 = 0x13;
    public static byte MSGID_WATER_OUT_STATUS_2 = (byte) 0x00;

    //MSGID FOR water Type
    public static byte MSGID_ERROR = 0x29;
    public static byte MSGID_ERROR2 = (byte) 0x0C;

    //MSGID FOR water Type
    public static byte MSGID_ACTIVATE_FLAVOR_DISINFECTION = 0x44;
    public static byte MSGID_ACTIVATE_FLAVOR_DISINFECTION2 = (byte) 0x0C;

    public static byte START_ESP_OTA_1 = 0x38;
    public static byte START_ESP_OTA_2 = (byte) 0xfe;

    //---------------------------System Identification----------------------//

    /**
     * Request for water system identification
     *
     * @return
     */
    public static byte[] setRequestForWaterSystemIdentification() {

        //For Direct
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00;
        bytes[1] = getDestinationAddressForIdentification();
        bytes[2] = getSourceAddress();
        bytes[3] = MSGID_WATER_SYSTEM_IDENTIFICATION1;
        bytes[4] = MSGID_WATER_SYSTEM_IDENTIFICATION2;
        bytes[5] = GET;
        bytes[6] = (byte) 0x01;
        bytes[7] = (byte) 0x01;
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;

        //For Hub
        //        byte[] bytes = new byte[10];
        //        bytes[0] = 0x00;
        //        bytes[1] = 0x0b;
        //        bytes[2] = 0x07;
        //        bytes[3] = (byte) 0xA0;
        //        bytes[4] = 0x01;
        //        bytes[5] = 0x01;
        //        bytes[6] = 0x01;
        //        bytes[7] = 0x01;
        //        bytes[8] = (byte) Utils.getCheckSum(bytes);
        //        bytes[9] = (byte) 0xFF;
        //        return bytes;

    }

    //---------------------------Water System Status----------------------//

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public static byte[] setRequestForSodaWaterParameters(int sodaWaterInMinTime, int sodaWaterInMaxTime, int sodaWaterInTemperature) {
        byte[] bytes = new byte[12];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_SODA_WATER_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_WATER_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x03;//DATA LENGTH
        bytes[7] = (byte) sodaWaterInMinTime;//VAL
        bytes[8] = (byte) sodaWaterInMaxTime;//VAL
        bytes[9] = (byte) sodaWaterInTemperature;//VAL
        bytes[10] = (byte) Utils.getCheckSum(bytes);
        bytes[11] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForSodaWaterParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_SODA_WATER_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_WATER_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForGetError() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_ERROR;//MSGID High
        bytes[4] = MSGID_ERROR2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }



    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public static byte[] setRequestForSodaCo2InParameters(int co2InMinTime, int co2InMaxTime, int co2SoftLevelTime,int co2MediumLevelTime,int co2IntenseLevelTime) {
        byte[] bytes = new byte[14];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_SODA_CO2_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x05;//DATA LENGTH
        bytes[7] = (byte) co2InMinTime;//VAL
        bytes[8] = (byte) co2InMaxTime;//VAL
        bytes[9] = (byte) co2SoftLevelTime;//VAL
        bytes[10] = (byte) co2MediumLevelTime;//VAL
        bytes[11] = (byte) co2IntenseLevelTime;//VAL
        bytes[12] = (byte) Utils.getCheckSum(bytes);
        bytes[13] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForSodaSodaCo2InParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_SODA_CO2_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }


    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public static byte[] setRequestForSodaCo2InOnOffParameters(int co2InOnOffStatus, int co2InOnNominalTime, int co2InOnMinTime,int co2InOnMaxTime,int co2InOffNominalTime,int co2InOffMinTime,int co2InOffMaxTime) {
        byte[] bytes = new byte[16];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_SODA_CO2_INONOFF_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_INONOFF_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x07;//DATA LENGTH
        bytes[7] = (byte) co2InOnOffStatus;//VAL
        bytes[8] = (byte) co2InOnNominalTime;//VAL
        bytes[9] = (byte) co2InOnMinTime;//VAL
        bytes[10] = (byte) co2InOnMaxTime;//VAL
        bytes[11] = (byte) co2InOffNominalTime;//VAL
        bytes[12] = (byte) co2InOffMinTime;//VAL
        bytes[13] = (byte) co2InOffMaxTime;//VAL
        bytes[14] = (byte) Utils.getCheckSum(bytes);
        bytes[15] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForSodaCo2InOnOffParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_SODA_CO2_INONOFF_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_INONOFF_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of watersystem dashboard
     *
     * @return
     */
    public static byte[] setRequestForWaterSystemData() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_REQUEST_STATUS_WATER_SYSTEM1;//MSGID High
        bytes[4] = MSGID_REQUEST_STATUS_WATER_SYSTEM2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public static byte[] setRequestForWashPipeParameters(int boilingWaterFlushTime,int sparkleWaterFlushTime) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WASH_PIPE_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_WASH_PIPE_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x02;//DATA LENGTH
        bytes[7] = (byte) boilingWaterFlushTime;//VAL
        bytes[8] = (byte) sparkleWaterFlushTime;//VAL
        bytes[9] = (byte) Utils.getCheckSum(bytes);
        bytes[10] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForWashPipeParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WASH_PIPE_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_WASH_PIPE_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }


    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public static byte[] setRequestForWateroutStatus(int waterType) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WATER_OUT_STATUS_1;//MSGID High
        bytes[4] = MSGID_WATER_OUT_STATUS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) waterType;//VAL=
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForWaterOutStatus(int waterType) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WATER_OUT_STATUS_1;//MSGID High
        bytes[4] = MSGID_WATER_OUT_STATUS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) waterType;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }


    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public static byte[] setRequestForPowerOn(int systemPowerOn) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_POWER_ON_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_POWER_ON_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) systemPowerOn;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForPowerOn(int systemPowerOn) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_POWER_ON_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_POWER_ON_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) systemPowerOn;//DATA LENGTH
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public static byte[] getRequestForWaterType() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WATER_TYPE_1;//MSGID High
        bytes[4] = MSGID_WATER_TYPE_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//DATA LENGTH
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        return bytes;
    }

    public static byte[] getRequestForFlavor() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = 0x0B; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = (byte) 0XEE; //MSGID High
        bytes[4] = (byte) 0XFE; //MSGID LOW
        bytes[5] = 0X01; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    //---------------------------HOT Water System Status----------------------//

    /**
     * Request for gettting value of hot water system
     *
     * @return
     */
    public static byte[] getRequestForHotWater() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_HOTWATER1; //MSGID High
        bytes[4] = MSGID_GET_HOTWATER2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        Log.d("getRequestForHotWater", "SET HEX = " + Utils.bytesToHex(bytes));
        return bytes;
    }

    /**
     * Request for set value of hot water system
     */
    public static byte[] setRequestForHotWater(int desiretemp, int hysteresistemp) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_HOTWATER1; //MSGID High
        bytes[4] = MSGID_GET_HOTWATER2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) desiretemp; //Desire temp
        bytes[8] = (byte) hysteresistemp; //Hysteresis temp
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        Log.d("setRequestForHotWater", "GET HEX = " + Utils.bytesToHex(bytes));
        return bytes;
    }

    //---------------------------Cold Water System Status----------------------//

    /**
     * Request for gettting value of Cold water system
     *
     * @return
     */
    public static byte[] getRequestForColdWater() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_COLDWATER1; //MSGID High
        bytes[4] = MSGID_GET_COLDWATER2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        Log.d("getRequestForColdWater", "GET HEX = " + Utils.bytesToHex(bytes));
        return bytes;
    }


    /**
     * Request for set value of Cold water system
     *
     * @return
     */
    public static byte[] setRequestForColdWater(int desiretemp, int hysteresistemp) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_COLDWATER1; //MSGID High
        bytes[4] = MSGID_GET_COLDWATER2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) desiretemp; //Desire temp
        bytes[8] = (byte) hysteresistemp; //Hysteresis temp
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        Log.d("getRequestForColdWater", "GET HEX = " + Utils.bytesToHex(bytes));
        return bytes;
    }

    //---------------------------Soda Water System Status----------------------//

    /**
     * Request for gettting value of Soad water system
     *
     * @return
     */
    public static byte[] getRequestForSodaWater() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_SODAWATER1; //MSGID High
        bytes[4] = MSGID_GET_SODAWATER2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Request for set value of Soda Water system
     *
     * @return
     */
    public static byte[] setRequestForSodaWater(int val) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_SODAWATER1; //MSGID High
        bytes[4] = MSGID_GET_SODAWATER2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) val; //Desire temp
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    //---------------------------CHILD PROTECT---------------------//

    /**
     * Request for get child protection data
     *
     * @return
     */

    public static byte[] getRequestForChildProtect() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CHILD_PROTECT1; //MSGID High
        bytes[4] = MSGID_CHILD_PROTECT2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Request for set child protection
     *
     * @param isProtect
     * @return
     */
    public static byte[] setRequestForChildProtect(int isProtect) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CHILD_PROTECT1; //MSGID High
        bytes[4] = MSGID_CHILD_PROTECT2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isProtect; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }
    //---------------------------GET WATER SYSTEM CLOCK---------------------//

    public static byte[] getRequestForWaterSystemClock() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WATER_SYSTEM_CLOCK1; //MSGID High
        bytes[4] = MSGID_WATER_SYSTEM_CLOCK2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForWaterSystemClock(int year, int month, int date, int day, int hour, int min, int sec) {
        byte[] bytes = new byte[16];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WATER_SYSTEM_CLOCK1; //MSGID High
        bytes[4] = MSGID_WATER_SYSTEM_CLOCK2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x07; //DATA LENGTH
        bytes[7] = (byte) year; //YEAR
        bytes[8] = (byte) month; //MONTH
        bytes[9] = (byte) day; //DAY
        bytes[10] = (byte) date; //DATE
        bytes[11] = (byte) hour; //HOUR
        bytes[12] = (byte) min; //MIN
        bytes[13] = (byte) sec; //SEC
        bytes[14] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[15] = (byte) 0xFF; //LAST
        return bytes;
    }

    //-------------------------Water Filter-------------------------
    public static byte[] getRequestForWaterFilter() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FILTER_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_FILTER_REPLACEMENT2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public static byte[] setRequestForFilterReplacement(int filter) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FILTER_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_FILTER_REPLACEMENT2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) filter; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public static byte[] setRequestForActivateFlavorDisinfection(int activateFlavorDisinfection) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_ACTIVATE_FLAVOR_DISINFECTION; //MSGID High
        bytes[4] = MSGID_ACTIVATE_FLAVOR_DISINFECTION2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) activateFlavorDisinfection; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }


    //-------------------------C02 Tank Replacement-------------------------
    public static byte[] getRequestForTankReplacement() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CO2_TANK_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_CO2_TANK_REPLACEMENT2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public static byte[] setRequestForTankReplacement(int filter) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CO2_TANK_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_CO2_TANK_REPLACEMENT2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) filter; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;


    }

    //---------------------------Check Message---------------------//
    public static boolean checkMessage(byte[] val, byte msgidHigh, byte msgidlow) {
        if (val[0] != (byte) 0x00) {
            return false;
        } else if (val[val.length - 1] != (byte) 0xFF) {
            return false;
        } else if (val.length - 9 != val[6]) {
            return false;
        } else if (Utils.getCheckSum(val) != val[val.length - 2]) {
            return false;
        } else if (val[3] != msgidHigh) {
            return false;
        } else return val[4] == msgidlow;
    }

    //-----------------Timer--------------------------

    public static byte[] getRequestForTimer1() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_TIMER1_1; //MSGID High
        bytes[4] = MSGID_TIMER1_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForTimer2() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_TIMER2_1; //MSGID High
        bytes[4] = MSGID_TIMER2_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public static byte[] setRequestForTimer1(int[] timerarray) {
        byte[] bytes = new byte[18];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_TIMER1_1; //MSGID High
        bytes[4] = MSGID_TIMER1_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x09; //DATA LENGTH

        bytes[7] = (byte) timerarray[0]; //DATA DAYS
        bytes[8] = (byte) timerarray[1]; //DATA FROM TIME1
        bytes[9] = (byte) timerarray[2]; //DATA TO TIME1
        bytes[10] = (byte) timerarray[3]; //DATA FROM TIME2
        bytes[11] = (byte) timerarray[4]; //DATA TO TIME2
        bytes[12] = (byte) timerarray[5]; //DATA FROM TIME3
        bytes[13] = (byte) timerarray[6]; //DATA TO TIME3
        bytes[14] = (byte) timerarray[7]; //DATA FROM TIME4
        bytes[15] = (byte) timerarray[8]; //DATA TO TIME4

        bytes[16] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[17] = (byte) 0xFF; //LAST
        return bytes;
    }


    public static byte[] setRequestForTimer2(int[] timerarray) {
        byte[] bytes = new byte[18];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_TIMER2_1; //MSGID High
        bytes[4] = MSGID_TIMER2_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x09; //DATA LENGTH

        bytes[7] = (byte) timerarray[0]; //DATA DAYS
        bytes[8] = (byte) timerarray[1]; //DATA FROM TIME1
        bytes[9] = (byte) timerarray[2]; //DATA TO TIME1
        bytes[10] = (byte) timerarray[3]; //DATA FROM TIME2
        bytes[11] = (byte) timerarray[4]; //DATA TO TIME2
        bytes[12] = (byte) timerarray[5]; //DATA FROM TIME3
        bytes[13] = (byte) timerarray[6]; //DATA TO TIME3
        bytes[14] = (byte) timerarray[7]; //DATA FROM TIME4
        bytes[15] = (byte) timerarray[8]; //DATA TO TIME4

        bytes[16] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[17] = (byte) 0xFF; //LAST
        return bytes;
    }

    //------------------------------Sterialization----------------------------------------------------------
    public static byte[] getRequestForSterialization() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_STERILIZATION_1; //MSGID High
        bytes[4] = MSGID_STERILIZATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForSterialization(int time, int days) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_STERILIZATION_1; //MSGID High
        bytes[4] = MSGID_STERILIZATION_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) days; //DATA DAYS
        bytes[8] = (byte) time; //DATA TIME
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        return bytes;
    }

    //------------------------------Consumable----------------------------------------------------------

    public static byte[] setRequestForConsumable(int type, int val, byte setOrGet) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CONSUMABLE_1; //MSGID High
        bytes[4] = MSGID_CONSUMABLE_2; //MSGID LOW
        bytes[5] = setOrGet; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) type; //SYSTEM TYPE
        bytes[8] = (byte) val; //SYSTEM TYPE
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForConsumableNew() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CONSUMABLE_1_NEW; //MSGID High
        bytes[4] = MSGID_CONSUMABLE_2_NEW; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //SYSTEM TYPE
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForConsumableNew(int replacementId,int replacementType,int quantity,int expirationInDays) {
        byte[] bytes = new byte[14];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_CONSUMABLE_1_NEW; //MSGID High
        bytes[4] = MSGID_CONSUMABLE_2_NEW; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x05; //DATA LENGTH
        bytes[7] = (byte) replacementId; // replacementId
        bytes[8] = (byte) replacementType; // replacementType
        bytes[9] = (byte) quantity; // quantity
        bytes[10] = (byte) (expirationInDays % 0xff); // L
        bytes[11] = (byte) (expirationInDays / 0xff); // H
        bytes[12] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[13] = (byte) 0xFF; //LAST
        return bytes;
    }

    //------------------------------Communication----------------------------------------------------------

    public static byte[] getRequestForHubCommunication() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_COMMUNICATION_1; //MSGID High
        bytes[4] = MSGID_COMMUNICATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForHubProvisoingMode() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_COMMUNICATION_1; //MSGID High
        bytes[4] = MSGID_COMMUNICATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    //------------------------------Diagnostic----------------------------------------------------------
    public static byte[] getRequestForDiagnostic() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddress(); //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_DIAGNOSTIC_1; //MSGID High
        bytes[4] = MSGID_DIAGNOSTIC_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    //---------------------------------UVLAMP--------------------------------------------------

    /**
     * Request for check UVLamp exits
     *
     * @return
     */
    public static byte[] getRequestForUVLamp() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddress();
        //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_UVLAMP_1; //MSGID High
        bytes[4] = MSGID_UVLAMP_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Request for set UVLamp Status
     *
     * @param isexits
     * @return
     */
    public static byte[] setRequestForForUVLamp(int isexits) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddress();
        //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_UVLAMP_1; //MSGID High
        bytes[4] = MSGID_UVLAMP_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isexits; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    // Hub Version
    public static byte[] getRequestForHubVersion() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First

        //        if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS_FOR_IDENTIFICATION, BLEConstant.APP_HUB) == BLEConstant.APP_HUB) {
        //            bytes[3] = MSGID_HUB_VERSION_1; //MSGID High
        //            bytes[4] = MSGID_HUB_VERSION_2;
        //            bytes[1] = APP_HUB; //Destination
        //            bytes[2] = getSourceAddress(); //Source
        //        } else {
        //            bytes[3] = MSGID_HUB_VERSION_1; //MSGID High
        //            bytes[4] = MSGID_HUB_VERSION_2;
        //            bytes[1] = getDestinationAddress(); //Destination
        //            bytes[2] = getSourceAddress(); //Source
        //        }

        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_VERSION_1; //MSGID High
        bytes[4] = MSGID_HUB_VERSION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    // Version
    public static byte[] getRequestVersion(int boardType) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = VERSION_1; //MSGID High
        bytes[4] = VERSION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) boardType; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    // Get Request WaterSystem Uptime
    public static byte[] getWaterSystemUptime() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_WATER_SYSTEM_UPTIME_1; //MSGID High
        bytes[4] = MSGID_WATER_SYSTEM_UPTIME_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForHubMacAddress() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_MAC_ADDRESS_1; //MSGID High
        bytes[4] = MSGID_HUB_MAC_ADDRESS_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForHubIPAddress() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_IP_ADDRESS_1; //MSGID High
        bytes[4] = MSGID_HUB_IP_ADDRESS_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForDIRNotification() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_DIR_NOTIFICATION_1; //MSGID High
        bytes[4] = MSGID_DIR_NOTIFICATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForDirModeFlashProcess(int startFlash) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_DIR_MODE_FLASH_PROCESS_1; //MSGID High
        bytes[4] = MSGID_DIR_MODE_FLASH_PROCESS_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) startFlash; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForHUBModeOTAForHUB() {
        //        00 0b 06 04 fe 02 01 02
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddressForIdentification(); //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = 0x04; //MSGID High
        bytes[4] = (byte) 0xfe; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForHUBModeOTAForWS() {
        //        00 0b 06 04 fe 02 01 01
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddressForIdentification(); //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_MODE_WS_OTA_1; //MSGID High
        bytes[4] = MSGID_HUB_MODE_WS_OTA_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForPairing(byte valueToBeSet) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_PAIRING_1; //MSGID High
        bytes[4] = MSGID_PAIRING_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) valueToBeSet; //DATA
        bytes[8] = (byte) valueToBeSet; //DATA
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForPairing() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_PAIRING_1; //MSGID High
        bytes[4] = MSGID_PAIRING_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForWSOTAEnabled() {
        //        00 01 07 04 FE 01 01 01 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_MODE_WS_OTA_1; //MSGID High
        bytes[4] = MSGID_HUB_MODE_WS_OTA_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForHubOTAEnabled() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_MODE_WS_OTA_1; //MSGID High
        bytes[4] = MSGID_HUB_MODE_WS_OTA_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    /**
     * Get the source address  weather BLE is connected via INTERNAL(HUB,DIR) or EXTERNAL(CLOUD)
     *
     * @return
     */
    public static byte getSourceAddress() {
        if (UnitoManager.getSingleton().getConnectStatus()) {
            return APP_INTERNAL;
        }
//        else if (Unito.getInstance().getSharedPreferences().getInt(PREF.SOURCE_ADDRESS, APP_INTERNAL) == APP_EXTERNAL) {
//            return APP_EXTERNAL;
//        }
        return APP_EXTERNAL;
    }

    /**
     * Get the destination address  weather BLE is connected via DIR,HUB or CLOUD
     *
     * @return
     */
    public static byte getDestinationAddress() {
//        if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS, WATER_SYSTEM_CONTROLLER) == COOKER_SYSTEM_CONTROLLER) {
//            return COOKER_SYSTEM_CONTROLLER;
//        } else if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS, WATER_SYSTEM_CONTROLLER) == HOOD_SYSTEM_CONTROLLER) {
//            return HOOD_SYSTEM_CONTROLLER;
//        } else {
//            return WATER_SYSTEM_CONTROLLER;
//        }
        return 0;
    }

    public static byte getDestinationAddressForIdentification() {
//        if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS_FOR_IDENTIFICATION, APP_DIRECT) == APP_CLOUD) {
//            return APP_CLOUD;
//        } else if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS_FOR_IDENTIFICATION, APP_DIRECT) == APP_HUB) {
//            return APP_HUB;
//        } else {
//            return APP_DIRECT;
//        }
        return 0;
    }


    /**
     * Request for set ENABLE DISABLE Pull out sensor
     *
     * @param isEnablePullOutSensor
     * @return
     */

    /*D (1043386) GATTS: Received message from App:
I (1043390) GATTS: 00 0b 07 3e 0c 01 01 02 a0 ff
D (1043394) GATTS: Routing incoming message.
E (1043398) ROUTE: Unsupported SMAPS message received id = 3134, ignored.*/
    public static byte[] setRequestForEnablePullOutSensor(int isEnablePullOutSensor) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isEnablePullOutSensor; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }


    public static byte[] getRequestForEnablePullOutSensor() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForAutoSodaReFill() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_AUTO_SODA_REFILL_1; //MSGID High
        bytes[4] = MSGID_GET_AUTO_SODA_REFILL_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForAutoSodaReFill(int isEnablePullOutSensor) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_AUTO_SODA_REFILL_1; //MSGID High
        bytes[4] = MSGID_GET_AUTO_SODA_REFILL_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isEnablePullOutSensor; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForAutoFillTimer() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_AUTO_FILL_TIMER_1; //MSGID High
        bytes[4] = MSGID_AUTO_FILL_TIMER_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForAutoFillTimer(int timerValue) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_AUTO_FILL_TIMER_1; //MSGID High
        bytes[4] = MSGID_AUTO_FILL_TIMER_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) timerValue; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }


    public static byte[] getRequestForFlushTimerBoiling() {
        //[0, 7, 1, 50, 12, 4, 2, 15, 14, -105, -1]
        //00 07 01 32 0C 04 02 0F 0E 97 FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLUSH_TIMER_1; //MSGID High
        bytes[4] = MSGID_FLUSH_TIMER_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForFlushTimerBoiling(int timerBoiling, int timerSparkle) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLUSH_TIMER_1; //MSGID High
        bytes[4] = MSGID_FLUSH_TIMER_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) timerBoiling; //timerBoiling
        bytes[8] = (byte) timerSparkle; //timerSparkle
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForFlavorParameters() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForFlavorParametersCleaning() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_CLEANING_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_CLEANING_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForFlavorParametersDisinfection() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForFlavorParameters(int flavorIntense, int flavorMedium, int flavorSoft, int waterPerInjection, int sodaPerInjection) {
        byte[] bytes = new byte[14];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x05; //DATA LENGTH
        bytes[7] = (byte) flavorIntense; //VALUE FOR flavorInjection
        bytes[8] = (byte) flavorMedium; //VALUE FOR flavorMedium
        bytes[9] = (byte) flavorSoft; //VALUE FOR flavorIntense
        bytes[10] = (byte) waterPerInjection; //VALUE FOR waterPerInjection
        bytes[11] = (byte) sodaPerInjection; //VALUE FOR sodaPerInjection
        bytes[12] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[13] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForFlavorParametersCleaning(int durationSecond, int durationHours) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_CLEANING_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_CLEANING_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) durationSecond; //VALUE FOR flavorInjection
        bytes[8] = (byte) durationHours; //VALUE FOR flavorMedium
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForFlavorParametersDisinfection(int fisinfectionCycles, int disinfectionForward, int disinfectionBackwards,
            int disinfectionThresholdLow, int disinfectionThresholdHigh, int disinfectionThreshold) {
        byte[] bytes = new byte[15];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x06; //DATA LENGTH
        bytes[7] = (byte) fisinfectionCycles;
        bytes[8] = (byte) disinfectionForward;
        bytes[9] = (byte) disinfectionBackwards;
        bytes[10] = (byte) disinfectionThresholdLow;
        bytes[11] = (byte) disinfectionThresholdHigh;
        bytes[12] = (byte) disinfectionThreshold;
        bytes[13] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[14] = (byte) 0xFF; //LAST
        return bytes;
    }


    public static byte[] getRequestForFlavorWater() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_WATER_TYPE_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_WATER_TYPE_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setRequestForFlavorWater(int flavorType) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_FLAVOR_WATER_TYPE_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_WATER_TYPE_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) flavorType; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForEnableLeakageSensor() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }


    public static byte[] setRequestForEnableLeakageSensor(int isEnablePullOutSensor) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isEnablePullOutSensor; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        Log.d("Indianic BLE CO", "SET HEX = " + Utils.bytesToHexLog(bytes));
        return bytes;
    }

    public static byte[] getRequestForSingleClick() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_SINGLE_CLICK_1; //MSGID High
        bytes[4] = MSGID_GET_SINGLE_CLICK_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] setSingleClick(int resolution, int enableDisable) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_GET_SINGLE_CLICK_1; //MSGID High
        bytes[4] = MSGID_GET_SINGLE_CLICK_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) enableDisable; //resolution
        bytes[8] = (byte) resolution; //enableDisable
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        return bytes;
    }
}
