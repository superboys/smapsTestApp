package com.unito.smapssdk.library;

/**
 * Created by IndiaNIC on 16/01/18.
 */

public class ApsHubStatusService {
    public static String HubStatusProtocolToJsonString(int[] HubStatusProtocol) {
        String result;
        result = "{";
        result = result + ParseProtocolHubStatus((HubStatusProtocol[0] & (byte) 0x60) / 32, "Interface", "HubWaterSystem");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[0] & (byte) 0x18) / 8, "Interface", "HubHood");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[0] & (byte) 0x06) / 2, "Interface", "Hubcooker");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[1] & (byte) 0x60) / 32, "Interface", "HubTiana");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[1] & (byte) 0x18) / 8, "Interface", "HubApp");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[1] & (byte) 0x06) / 2, "Interface", "HubWiFi");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[2] & (byte) 0x60) / 32, "Interface", "HubHttps");
        result = result + ParseProtocolHubStatus((HubStatusProtocol[2] & (byte) 0x18) / 8, "Interface", "HubWss");
        result = result.substring(0, result.length() - 1);
        result = result + "}";
        return (result);
    }

    public static String ParseProtocolHubStatus(int HubStatusInt, String UnitType, String key) {
        char Dq = (char) 34;
        String result = "";

        //   if (UnitType=="Interface") {

        String HubStatus = "NotConnected";
        if (HubStatusInt == 1) {
            HubStatus = "Trying";
        }
        if (HubStatusInt == 2) {
            HubStatus = "DoesNotExist";
        }
        if (HubStatusInt == 3) {
            HubStatus = "Connected";
        }
        result = Dq + key + Dq + ":" + Dq + HubStatus + Dq + ",";
        return (result);

//    }
    }
}