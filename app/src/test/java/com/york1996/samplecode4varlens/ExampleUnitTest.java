package com.york1996.samplecode4varlens;

import org.junit.Test;

import static org.junit.Assert.*;

import android.graphics.Rect;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void subStr() {
        assertEquals(leftStr("abcd1234", 3), "abc");
        assertEquals(leftStr("Zine是一个写作平台", 8), "Zine是一");
    }

    private String leftStr(String str, int n) {
        if (str == null || str.isEmpty() || n <= 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int length = 0;
        int i = 0;
        while (i < str.length() && length < n) {
            char c = str.charAt(i);
            result.append(c);

            // 中文占2位，英文占1位
            if (c > 127) {
                length += 2;
            } else {
                length += 1;
            }

            i++;
        }
        return result.toString();
    }

    @Test
    public void compareVersion() {
        assertEquals(compareVersions("1.2.1", "1.2.0"), "1.2.1");
        assertEquals(compareVersions("1.3.2-alpha.1", "1.3.2-beta"), "1.3.2-beta");
        assertEquals(compareVersions("1.3.2-alpha.1", "1.3.2-alpha.2"), "1.3.2-alpha.2");
    }

    private String compareVersions(String version1, String version2) {
        // eg. 1.0.1-alpha.1 先划分为 1.0.1 与 alpha.1
        List<String> v1 = Arrays.asList(version1.split("-"));
        List<String> v2 = Arrays.asList(version2.split("-"));

        // 先比较数字版本号
        String[] parts1 = v1.get(0).split("\\.");
        String[] parts2 = v2.get(0).split("\\.");
        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            int v1Part = (i < parts1.length) ? Integer.parseInt(parts1[i]) : 0;
            int v2Part = (i < parts2.length) ? Integer.parseInt(parts2[i]) : 0;

            if (v1Part < v2Part) {
                return version2;
            } else if (v1Part > v2Part) {
                return version1;
            }
        }

        // 如果数字版本号都相等，比较预发布字符串（alpha < beta）
        if (v1.size() > 1 && v2.size() > 1) {
            String preRelease1 = v1.get(1);
            String preRelease2 = v2.get(1);

            // alpha < beta
            if (preRelease1.contains("beta") && preRelease2.contains("alpha")) {
                return version1;
            } else if (preRelease1.contains("alpha") && preRelease2.contains("beta")) {
                return version2;
            }

            // 再比较预发布字符串内的数字大小
            String[] preParts1 = preRelease1.split("\\.");
            int preReleaseNumber1 = Integer.parseInt(preParts1[preParts1.length - 1]);
            String[] preParts2 = preRelease2.split("\\.");
            int preReleaseNumber2 = Integer.parseInt(preParts2[preParts2.length - 1]);
            if (preReleaseNumber1 < preReleaseNumber2) {
                return version2;
            } else if (preReleaseNumber1 > preReleaseNumber2) {
                return version1;
            }
        } else if (v1.size() > 1) { // 带预发布字符串的比不带的版本小
            return version2;
        } else if (v2.size() > 1) {
            return version1;
        }

        return version1; // 数字版本号相等且无预发布字符串，默认返回 version1
    }


    private Rect getRotationRect(Rect originalRect, int angle) {
        // 计算矩形的中心点坐标
        float cx = (originalRect.left + originalRect.right) / 2.0f;
        float cy = (originalRect.top + originalRect.bottom) / 2.0f;

        // 计算矩形的宽度和高度
        float width = originalRect.width();
        float height = originalRect.height();

        // 将旋转角度转换为弧度
        float radians = (float) Math.toRadians(angle);

        // 计算旋转后的矩形的四个顶点坐标
        float rotatedX1 = cx + (float) (width / 2.0 * Math.cos(radians)) - (float) (height / 2.0 * Math.sin(radians));
        float rotatedY1 = cy + (float) (width / 2.0 * Math.sin(radians)) + (float) (height / 2.0 * Math.cos(radians));
        float rotatedX2 = cx - (float) (width / 2.0 * Math.cos(radians)) - (float) (height / 2.0 * Math.sin(radians));
        float rotatedY2 = cy - (float) (width / 2.0 * Math.sin(radians)) + (float) (height / 2.0 * Math.cos(radians));

        return new Rect((int) rotatedX1, (int) rotatedY1, (int) rotatedX2, (int) rotatedY2);
    }

}