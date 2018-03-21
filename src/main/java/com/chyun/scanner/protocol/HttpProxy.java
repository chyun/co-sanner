package com.chyun.scanner.protocol;

import org.apache.commons.lang3.StringUtils;

/**
 * 类的实现描述:
 *
 * @author liqun.wu
 */
public class HttpProxy {
    private final static String                           reqText          = "GET http://www.qq.com/404/search_children.js HTTP/1.1\r\nHost: www.qq.com\r\nAccept: */*\r\nPragma: no-cache\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36\r\n\r\n";
    private final static byte[]                           REQ_HTTP         = reqText.getBytes();
    /**
     * 2018.3.12, 这个页面是包含下面的字符串的
     */
    private final static String ZONE = "qzone.qq.com";
    public static byte[] getRequest() {
        return  REQ_HTTP;
    }
    public static boolean isProxy(String response) {
        if (StringUtils.isNotBlank(response) && StringUtils.contains(response, ZONE)) {
            return true;
        }
        return false;
    }
}
