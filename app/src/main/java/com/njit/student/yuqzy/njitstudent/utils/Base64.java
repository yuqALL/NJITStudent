package com.njit.student.yuqzy.njitstudent.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/12.
 */

public class Base64 {

    static String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // public method for encoding
    public static String encode(String input) {
        List<Character> output = new ArrayList<>();
        char chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        input = _utf8_encode(input);

        while (i < input.length())

        {
            chr1 = input.charAt(i++);
            chr2 = input.charAt(i++);
            chr3 = input.charAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (Double.isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (Double.isNaN(chr3)) {
                enc4 = 64;
            }
            int x1=_keyStr.charAt(enc1) ;
            int x2=_keyStr.charAt(enc2) ;
            int x3=_keyStr.charAt(enc3) ;
            int x4=_keyStr.charAt(enc4) ;
            output.add((char)x1);
            output.add((char)x2);
            output.add((char)x3);
            output.add((char)x4);
        }
        String result="";
        for(char c:output){
            result+=c;
        }

        return result;
    }

    // public method for decoding
    public static String decode(String input) {

        String output = "";
        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        input = input.replace("[^A-Za-z0-9+/=]", "");
        while (i < input.length()) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.valueOf(chr1);
            if (enc3 != 64) {
                output = output + String.valueOf(chr2);
            }
            if (enc4 != 64) {
                output = output + String.valueOf(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }

    // private method for UTF-8 encoding
    public static String _utf8_encode(String string) {
        string = string.replace("/\r\n/g", "\n");
        List<Character> utftext = new ArrayList();
        int utftextlen = 0;
        for (int n = 0; n < string.length(); n++) {
            char c = string.charAt(n);
            if (c < 128) {
                utftext.add(c);

            } else if ((c > 127) && (c < 2048)) {
                utftext.add((char) ((c >> 6) | 192));
                utftext.add((char) ((c & 63) | 128));
            } else {
                utftext.add((char) ((c >> 12) | 224));
                utftext.add((char) (((c >> 6) & 63) | 128));
                utftext.add((char) ((c & 63) | 128));
            }
        }
        String result = "";
        for (char c : utftext) {
            result += c;
        }
        return result;
    }

    // private method for UTF-8 decoding
    public static String _utf8_decode(String utftext) {

        String string = "";
        int i = 0;
        int c, c2, c3;
        c = c3 = c2 = 0;
        while (i < utftext.length()) {
            c = utftext.charAt(i);
            if (c < 128) {
                // Unicode 编码转为一个字符:
                string += String.valueOf(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charAt(i + 1);
                string += String.valueOf(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charAt(i + 1);
                c3 = utftext.charAt(i + 2);
                string += String.valueOf(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }

}
