package com.example.mat.util;

public class HtmlUtil {

    // 줄바꿈 -> <br> 변환
    public static String convertNewlinesToHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\n", "<br>");
    }

    // <br> -> 줄바꿈 복원
    public static String convertHtmlToNewlines(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("<br>", "\n");
    }

    // 띄어쓰기 -> &nbsp; 변환
    public static String convertSpacesToHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace(" ", "&nbsp;");
    }

    // &nbsp; -> 띄어쓰기 복원
    public static String convertHtmlToSpaces(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&nbsp;", " ");
    }

    // 종합: 줄바꿈과 띄어쓰기 모두 변환
    public static String convertTextToHtml(String text) {
        if (text == null) {
            return "";
        }
        return convertNewlinesToHtml(convertSpacesToHtml(text));
    }

    // 종합: 줄바꿈과 띄어쓰기 모두 복원
    public static String convertHtmlToText(String text) {
        if (text == null) {
            return "";
        }
        return convertHtmlToSpaces(convertHtmlToNewlines(text));
    }
}
