package com.unito.smapssdk.library;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static long mLastClickTime = 0;
    public static boolean isAnimationApplied = true;
    public static boolean isFragmentAnimationApplied = true;

    public static String MQTT_URL = "iot.unito-oauth.com";
    public static int MQTT_PORT = 8883;

    public static final String BASE_URL_DEFAULT = "https://iot-web.unito-oauth.com";

    public static String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static String secretKey = "YellowSubmarine_"; // Replace with your secret key

    /**
     * 16进制中的字符集
     */
    private static final String HEX_CHAR = "0123456789ABCDEF";

    private static String[] binaryArray =
            {"0000", "0001", "0010", "0011",
                    "0100", "0101", "0110", "0111",
                    "1000", "1001", "1010", "1011",
                    "1100", "1101", "1110", "1111"};

    public static void hideSoftKeyBoard(Context mContext, View view) {
        try {
            final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProgressAnimate(final ProgressBar pb, final int progressTo) {
        if (!isAnimationApplied) {
            return;
        }
        final ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", 0, progressTo);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public static void setImageDrawableWithAnimation(final ImageView imageView, final Drawable drawable, final int duration) {
        final Drawable currentDrawable = imageView.getDrawable();
        if (currentDrawable == null) {
            imageView.setImageDrawable(drawable);
            return;
        }

        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{currentDrawable, drawable});
        transitionDrawable.setCrossFadeEnabled(true);
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(duration);
    }


    public static int[] copyOfRange(byte[] myarray, int from, int to) {
        int[] newarray = new int[to - from];
        for (int i = 0; i < to - from; i++) newarray[i] = myarray[i + from];
        return newarray;
    }

    public static String[] copyOfRange(String[] myarray, int from, int to) {
        String[] newarray = new String[to - from];
        for (int i = 0; i < to - from; i++) newarray[i] = myarray[i + from];
        return newarray;
    }

    public static String bytesToHex(byte[] bytes) {
        //Log.e("LOG :", "------START---------------");
        //        char[] hexChars = new char[bytes.length * 2];
        //        for (int j = 0; j < bytes.length; j++) {
        //            Log.e("HEX VAL :", String.format("%02X", bytes[j]) + "-" + String.format("%d", signTounsigned(bytes[j])));
        //        }
        String value = "";
        for (int j = 0; j < bytes.length; j++) {
            value += String.format("%02X", bytes[j]) + " ";
        }
        //Log.e("Knob VAL :", value);
        //Log.e("LOG :", "---------STOP------------");
        return value;
    }

    public static String bytesToHexLog(byte[] bytes) {
        String value = "";
        for (int j = 0; j < bytes.length; j++) {
            value += String.format("%02X", bytes[j]) + " ";
        }
        return value;
    }

    public static String[] bytesToHexString(byte[] bytes) {
        //        char[] hexChars = new char[bytes.length * 2];
        //        for (int j = 0; j < bytes.length; j++) {
        //            Log.e("HEX VAL :", String.format("%02X", bytes[j]) + "-" + String.format("%d", signTounsigned(bytes[j])));
        //        }
        String value = "";
        for (int j = 0; j < bytes.length; j++) {
            value += String.format("%02X", bytes[j]) + " ";
        }
        return value.split(" ");
    }

    public static String[] bytesToHexForConsumable(byte[] bytes) {
        return bytesToHexForIPAddress(bytes).split(":");
    }

    public static String bytesToHexForIPAddress(byte[] bytes) {
        final StringBuilder value = new StringBuilder();
        for (byte aByte : bytes) {
            value.append(String.format(Locale.getDefault(), "%d:", signTounsigned(aByte)));
        }
        if (value.length() > 2) {
            return value.substring(0, value.length() - 1);
        } else {
            return value.toString();
        }
    }

    public static String convertByteToMacAddress(byte[] bytes) {
        String val = "";
        for (int j = 0; j < bytes.length; j++) {
            if (j == 0) {
                val = String.format("%02X", bytes[j]);
            } else {
                val = val + ":" + String.format("%02X", bytes[j]);
            }

        }
        return val;
    }

    public static String convertByteValueIntoHexString(byte[] bytes) {
        String val = "";
        for (int j = 0; j < bytes.length; j++) {
            val = val + String.format("%02X", bytes[j]);
        }
        return val;
    }

    public static byte[] hexStringToByteArray(String s) {
        try {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    public static String bytesToHexTimer(byte[] bytes) {

        //Log.e("LOG :", "------START---------------");
        char[] hexChars = new char[bytes.length * 2];
        //        for (int j = 0; j < bytes.length; j++) {
        //            Log.e("LOG_VALUE :", String.format("%02X", bytes[j]));
        //        }
        //Log.e("Timer :", "---------STOP------------");
        return new String(hexChars);
    }

    //    public static String decimalToHexString(int val, int val1) {
    //        val = decimalToInt(val);
    //        val1 = decimalToInt(val1);
    //        final String string = Integer.toHexString(val);
    //        final String string1 = Integer.toHexString(val1);
    //
    //        String response;
    //        if (!TextUtils.isDigitsOnly(string) && !TextUtils.isDigitsOnly(string1)) {
    //            if (string.length() == 1 && TextUtils.isDigitsOnly(string)) {
    //                response = "0" + string;
    //            } else {
    //                response = string;
    //            }
    //            if (string1.length() == 1 && TextUtils.isDigitsOnly(string1)) {
    //                response += "0" + string1;
    //            } else {
    //                response += string1;
    //            }
    //            return response;
    //        } else {
    //            return string + string1;
    //        }
    //    }

    public static String decimalToHexString(int val) {
        val = decimalToInt(val);
        return Integer.toHexString(val);
        //        if (string.length() == 1 && TextUtils.isDigitsOnly(string)) {
        //            return "0" + string;
        //        } else {
        //            return string;
        //        }
    }

    public static int decimalToInt(int val) {
        if (val < 0) {
            val = val + 256;
        }
        return val;
    }


    public static String swap2Characters(final String value) {
        if (value == null) {
            return null;
        }
        String tmpValue = value;

        if (tmpValue.length() == 1) {
            tmpValue = "000" + tmpValue;
        } else if (tmpValue.length() == 2) {
            tmpValue = "00" + tmpValue;
        } else if (tmpValue.length() == 3) {
            tmpValue = "0" + tmpValue;
        }
        //Log.i("value", "Original -> " + value + " New ->" + tmpValue + "");

        final StringBuilder stringBuilder = new StringBuilder();
        int posA = 0;
        int posB = 2;

        // swap characters
        while (posA < tmpValue.length() && posB < tmpValue.length()) {
            stringBuilder.append(tmpValue.charAt(posB)).append(tmpValue.charAt(posB + 1)).append(tmpValue.charAt(posA)).append(tmpValue.charAt(posA + 1));
            posA += 4;
            posB += 4;
        }

        // if resulting string is still smaller than original string we missed the last character
        if (stringBuilder.length() < tmpValue.length()) {
            stringBuilder.append(tmpValue.charAt(posA));
        }
        return stringBuilder.toString();
    }

    public static int getCheckSum(byte[] input) {
        int sum = 0;
        int temp = 0;
        for (int i = 0; i < input.length - 2; i++) {
            temp = input[i];
            sum = sum + temp;
        }
        sum = ~(sum & 0x7f) + 1;
        return sum;
    }

    private static AlertDialog dialog;

    public static AlertDialog displayDialogNormalMessage(String title, String msg, final Context context, final String positiveText,
                                                         final DialogInterface.OnClickListener onClickListener, final String negativeText, final DialogInterface.OnClickListener onNegativeClickListener) {
        if (context == null) return null;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setNeutralButton(positiveText.length() == 0 ? context.getString(android.R.string.ok) : positiveText, onClickListener);
        if (!negativeText.isEmpty()) {
            alertDialog.setPositiveButton(TextUtils.isEmpty(negativeText) ? context.getString(android.R.string.cancel) : negativeText, onNegativeClickListener);
        }

        if (context != null && !((Activity) context).isFinishing()) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = alertDialog.create();
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }
            });
        }
        return dialog;
    }

    public static AlertDialog displayDialogNormalMessageWithRedButton(String title, String msg, final Context context, final String positiveText,
                                                                      final DialogInterface.OnClickListener onClickListener, final String negativeText, final DialogInterface.OnClickListener onNegativeClickListener) {
        if (context == null) return null;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setNeutralButton(positiveText.length() == 0 ? context.getString(android.R.string.ok) : positiveText, onClickListener);
        if (!negativeText.isEmpty()) {
            alertDialog.setPositiveButton(TextUtils.isEmpty(negativeText) ? context.getString(android.R.string.cancel) : negativeText, onNegativeClickListener);
        }

        if (!((Activity) context).isFinishing()) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = alertDialog.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                        }
                    });
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }
            });
        }
        return dialog;
    }

    /**
     * Dismiss current progress dialog
     *
     * @param dialog dialog
     */
    public static void dismissAlertDialog(AlertDialog dialog) {
        if (isShowingAlertDialog(dialog)) dialog.dismiss();
    }

    /**
     * Is showing current progress dialog
     *
     * @param dialog dialog
     */
    public static boolean isShowingAlertDialog(AlertDialog dialog) {
        return dialog != null && dialog.isShowing();
    }

    public static int signTounsigned(byte val) {
        if (val >= 0) {
            return val;
        } else {
            return val & 0x7F + 128;
        }
    }

    public static int convertValue(int val) {
        if (val < 0) {
            return 0;
        }
        return val;
    }

    public static float convertValueFloat(float val) {
        if (val < 0) {
            return 0;
        }
        return val;
    }

    public static double convertValueDouble(double val) {
        if (val < 0) {
            val = val + 256;
        }
        return val;
    }

    public static String hexToBin(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return String.valueOf(val);
    }

    public static int SystemClockProtocolToDidplay(int ClockProtoclXX) {

        int ClockDisplayXX;

        ClockDisplayXX = ((ClockProtoclXX & 0xf0) / 16 * 10) + (ClockProtoclXX & 0x0f);

        return (ClockDisplayXX);

    }

    public static int SystemClockDisplayToProtocol(int ClockDisplayXX) {

        int ClockProtocolXX;

        ClockProtocolXX = ClockDisplayXX % 10 + 16 * ((ClockDisplayXX - (ClockDisplayXX % 10)) / 10);

        return (ClockProtocolXX);

    }

    public static String getStringValue(int value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }


    public static int getYearin2digit(long time) {
        DateFormat df = new SimpleDateFormat("yy", Locale.getDefault()); // Just the year, with 2 digits
        String formattedDate = df.format(time);
        return Integer.parseInt(formattedDate);
    }

    public static String getCurrentDateTime(long time, String format) {
        final DateFormat df = new SimpleDateFormat(format, Locale.getDefault()); // Just the year, with 2 digits
        return df.format(time);
    }


    public static String getOtaLogTime() {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.getDefault()); // Just the year, with 2 digits
        return df.format(new Date());
    }

    public static String getTimezoneName() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName();
    }

    public static String getStringFromResourcesByName(Context context, String resourceName, boolean isDynamic, String dynamicval) {

        String packageName = context.getPackageName();
        int resourceId = context.getResources().getIdentifier(resourceName, "string", packageName);
        // Return the string value
        if (isDynamic) {
            return context.getString(resourceId, dynamicval);
        } else {
            return context.getString(resourceId);
        }
    }

    public static boolean isStringFromResourcesByName(Context context, String resourceName) {
        String packageName = context.getPackageName();
        int resourceId = context.getResources().getIdentifier(resourceName, "string", packageName);
        return resourceId != 0;
    }

    public static Boolean[] convertbytetoBits(byte value) {
        byte[] b = new byte[]{value};
        BitSet bitset = BitSet.valueOf(b);
        if (bitset != null && bitset.length() > 0) {
            Boolean[] bits = new Boolean[bitset.length()];
            for (int i = 0; i < bitset.length(); ++i) {
                bits[i] = bitset.get(i);
            }
            return bits;
        }
        return null;
    }

    public static Integer[] convertbytetoBitsInInt(byte value) {
        byte[] b = new byte[]{value};
        BitSet bitset = BitSet.valueOf(b);
        if (bitset != null && bitset.length() > 0) {
            Integer[] bitsInInt = new Integer[bitset.length()];
            for (int i = 0; i < bitset.length(); ++i) {
                if (bitset.get(i)) {
                    bitsInInt[i] = 1;
                } else {
                    bitsInInt[i] = 0;
                }
            }
            return bitsInInt;
        }
        return new Integer[0];
    }

    public static Integer[] reverse(Integer[] a, int n) {
        Integer[] b = new Integer[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }

    public static long convertDatetoMiliseconds(String datetimevalue) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss", Locale.getDefault());
            Date date = sdf.parse(datetimevalue);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String calculateMD5(final File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //Log.e("TAG", "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            //Log.e("TAG", "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                //Log.e("TAG", "Exception on closing MD5 input stream", e);
            }
        }
    }

    public static String getBinaryFromString(final byte b) {
        final String value = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        StringBuilder input1 = new StringBuilder();

        // append a string into StringBuilder input1
        input1.append(value);

        // reverse StringBuilder input1
        return input1.toString();
    }

    public static String getKeyedString(String AccessToken) {
        String Keyed = "";
        int temp = 0;
        String str = "";
        int i = 0;
        AccessToken = AccessToken.toLowerCase();
        for (i = 0; i < AccessToken.length(); i++) {
            temp = Character.codePointAt(AccessToken, i) ^ Character.codePointAt("7A502C79FB81AD426DA9919E2265C3401C45".toLowerCase(), i);// xor i'th value
            str = "0" + Integer.toHexString(temp); // convert to hex + add extra leading zero
            Keyed = Keyed + str.substring(str.length() - 2); // right 2 and concatenate
        }
        return Keyed; //return result on loop end
    }

    // input == one line code in txt file.
    public static String addSingleLineCheckSum(String input) {
        input = input.toUpperCase();
        // turn String into byteArray
        final byte[] byteArray = new byte[input.length() / 2];
        int k = 0; //counter
        //Because it is hexadecimal, it can only take up to 4 digits at most, and conversion to bytes requires two hexadecimal characters, with the high-order first
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(input.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(input.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        // copy byteArray and calculate checksum
        int sum = 0;
        int temp = 0;
        for (int i = 0; i < byteArray.length; i++) {
            temp = byteArray[i];
            sum = sum + temp;
        }
        sum = ~(sum) + 1;
        // make final change on checksum
        String hex = Integer.toHexString(sum).toUpperCase();
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        String output = input + hex.substring(hex.length() - 2);
        return output;
    }

    public static String addCheckSum(String input) {
        input = input.toUpperCase();
        // turn String into byteArray
        final byte[] byteArray = new byte[input.length() / 2];
        int k = 0; //counter
        //Because it is hexadecimal, it can only take up to 4 digits at most, and conversion to bytes requires two hexadecimal characters, with the high-order first
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(input.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(input.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        // copy byteArray and calculate checksum
        int sum = 0;
        int temp = 0;
        for (int i = 0; i < byteArray.length; i++) {
            temp = byteArray[i];
            sum = sum + temp;
        }
        sum = ~(sum) + 1;
        // make final change on checksum
        String hex = Integer.toHexString(sum).toUpperCase();
        return hex;
    }

    public static String calculateChecksum8(final String value) {
        // convert input value to upper case
        String strN = value;
        strN = strN.toUpperCase();

        String strHex = "0123456789ABCDEF";
        int result = 0;
        int fctr = 16;

        for (int i = 0; i < strN.length(); i++) {
            if (strN.charAt(i) == ' ') continue;

            int v = strHex.indexOf(strN.charAt(i));
            if (v < 0) {
                result = -1;
                break;
            }
            result += v * fctr;

            if (fctr == 16) fctr = 1;
            else fctr = 16;
        }

        if (result < 0) {
            //Log.e("Checksum error", "Non - hex character entered");
            return "";
        } else if (fctr == 1) {
            //Log.e("Checksum error", "Odd number of characters entered. e.g. correct value = aa aa");
            return "";
        } else {
            // Calculate 2's complement
            result = (~(result & 0xff) + 1) & 0xFF;
            // Convert result to string
            //strResult = new String(result.toString());
            final String resultString = String.valueOf(strHex.charAt((int) Math.floor(result / 16))) + strHex.charAt(result % 16);
            Log.e("Checksum", "value = " + resultString);
            return resultString;
        }
    }

    public static String loadJSONFromAsset(final Context mContext, final String fileName) {
        String json;
        try {
            final InputStream is = mContext.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }

    public static String getMonth(int month) {
        if (month > 0) return new DateFormatSymbols().getMonths()[month - 1];
        else return new DateFormatSymbols().getMonths()[month];
    }

    public static int signedWord2Dec(String valueHex) {
        int decimal = Integer.parseInt(valueHex, 16);  //unsined decimal value
        if (decimal >= 65536 / 2) {
            decimal = -(65536 - decimal);
        } // turn to signed, if number>=2^15, then negative = -(2^16-number)
        return (decimal);
    }

    public static String dec2SignedWord(int valueInt) {
        String hexString = Integer.toHexString(valueInt);
        hexString = setLength(hexString);
        return (hexString);
    }

    public static String setLength(String hexString) { //adds leading "0" and truncates leading "f" when needed
        while (hexString.length() < 4) {
            hexString = "0" + hexString;
        }
        if (hexString.length() > 4) {
            hexString = hexString.substring(hexString.length() - 4);
        }
        return (hexString).toUpperCase();
    }

    public static int getVisibility(boolean isShow) {
        if (isShow) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }


    /**
     * @param hexString
     * @return 将十六进制转换为二进制字节数组   16-2
     */
    public static byte[] hexStr2BinArr(String hexString) {
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for (int i = 0; i < len; i++) {
            //右移四位得到高位
            high = (byte) ((HEX_CHAR.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) HEX_CHAR.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }

    /**
     * @param hexString
     * @return 将十六进制转换为二进制字符串   16-2
     */
    public static String hexStr2BinStr(String hexString) {
        return bytes2BinStr(hexStr2BinArr(hexString));
    }

    /**
     * @return 二进制数组转换为二进制字符串   2-2
     */
    public static String bytes2BinStr(byte[] bArray) {

        String outStr = "";
        int pos = 0;
        for (byte b : bArray) {
            //高四位
            pos = (b & 0xF0) >> 4;
            outStr += binaryArray[pos];
            //低四位
            pos = b & 0x0F;
            outStr += binaryArray[pos];
        }
        return outStr;
    }

    public static int getBitsNum(byte hex, int bitsCount, int bitsIndex) {
        int bitsToolNum = (int) (Math.pow(2, bitsCount) - 1);
        return (hex & (bitsToolNum << bitsIndex)) >> bitsIndex;
    }

    public static ArrayList<String> txtToArrayList(String path) throws IOException {
        //创建字符缓冲输入流对象
        BufferedReader br = new BufferedReader(new FileReader(path));
        //创建ArrayList集合对象
        ArrayList<String> array = new ArrayList<String>();
        //调用字符缓冲输入流对象的方法读数据
        String line;
        while ((line = br.readLine()) != null) {
            //把读取到的字符串数据存储到集合中
            array.add(line.substring(0,line.length()-2));
        }
        //释放资源
        br.close();
        //遍历集合输出
        for (String s : array) {
            System.out.println(s);
        }
        return array;
    }

    public static String getCurrentTimeZoneId() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    /**
     * FileChannel 获取文件的MD5值
     *
     * @param file 文件路径
     * @return md5
     */
    public static String getFileMd52(File file) {
        MessageDigest messageDigest;
        FileInputStream fis = null;
        FileChannel ch=null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (file == null) {
                return "";
            }
            if (!file.exists()) {
                return "";
            }
            fis = new FileInputStream(file);
            ch = fis.getChannel();
            int size = 1024 * 1024 * 10;
            long part = file.length() / size + (file.length() % size > 0 ? 1 : 0);
            System.err.println("文件分片数" + part);
            for (int j = 0; j < part; j++) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, j * size, j == part - 1 ? file.length() : (j + 1) * size);
                messageDigest.update(byteBuffer);
                byteBuffer.clear();
            }
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5 = bigInt.toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
                if (ch!=null){
                    ch.close();
                    ch=null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归删除子文件夹
                    deleteFolder(file);
                }
                // 删除文件或空文件夹
                file.delete();
            }
        }
        return folder.delete(); // 删除当前空文件夹
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String makeJson(int type, String ssid, String pwd,String uuid) {
        final JSONObject jsonObject = new JSONObject();
        String WaterSystemExisting = "No";

        WaterSystemExisting = "Yes";

        String HoodExisting = "No";
        String CookerExisting = "No";
        String TianaExisting = "No";

        try {
            jsonObject.put("Action", "ProvData");
            jsonObject.put("From", "App");
            jsonObject.put("To", "Hub");
            jsonObject.put("WifiSsid", ssid);
            jsonObject.put("WifiPassword", pwd);
            if (type == 40) {
                jsonObject.put("MqttBaseUrl", "mqtts://" + MQTT_URL + ":" + MQTT_PORT);
            } else {
                jsonObject.put("HubRefreshTimeOutMsec", "1800");
                jsonObject.put("CommTimeoutSec", String.valueOf(BLEConstant.PROVISIONING_COM_TIME_OUT));
                jsonObject.put("BleTimeoutSec", "30");
                jsonObject.put("WifiTimeoutSec", "30");
                jsonObject.put("HoodExisting", HoodExisting);
                jsonObject.put("CookerExisting", CookerExisting);
                jsonObject.put("TianaExisting", TianaExisting);
            }
            jsonObject.put("BaseUrl", BASE_URL_DEFAULT);
            jsonObject.put("OtaBackupBaseUrl", "");
            jsonObject.put("UserAppDeviceUuid", uuid);
            jsonObject.put("InstallationCountry", String.valueOf(getCurrentTimeZone()));
            jsonObject.put("WaterSystemExisting", WaterSystemExisting);
            jsonObject.put("TimeZone", String.valueOf(getCurrentTimeZone()));
            jsonObject.put("UserAppType", "android");
            Log.e("HUB OTA", "json value " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getCurrentTimeZone() {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        return TimeUnit.SECONDS.toHours(zonedDateTime.getOffset().getTotalSeconds());
    }

    public static byte[][] splitData(byte[] data, int chunkSize) {
        int totalChunks = (int) Math.ceil((double) data.length / chunkSize);
        byte[][] chunks = new byte[totalChunks][];

        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int length = Math.min(data.length - start, chunkSize);
            chunks[i] = Arrays.copyOfRange(data, start, start + length);
        }

        return chunks;
    }
}