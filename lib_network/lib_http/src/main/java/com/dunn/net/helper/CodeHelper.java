package com.dunn.net.helper;

public class CodeHelper {
    /**
     * ERROR
     */
    public static final int NETWORK_ERROR = -1; // the network relative error
    public static final int JSON_ERROR = -2; // the JSON relative error
    public static final int IO_ERROR = -3; // the JSON relative error
    public static final int OTHER_ERROR = -4; // the unknow error

    /**
     * MSG
     */
    public static final String EMPTY_MSG = "";

    /**
     * RESULT
     */
    // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final String RESULT_CODE = "ecode";
    protected final int RESULT_CODE_VALUE = 0;
}

