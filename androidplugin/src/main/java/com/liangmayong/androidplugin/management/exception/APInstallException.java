package com.liangmayong.androidplugin.management.exception;

/**
 * APInstallException
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APInstallException extends Exception {

    private static final long serialVersionUID = -2089389391726165180L;

    // apk parser error
    public static final int PARSER_ERROR = -1;
    // verifier update error
    public static final int VERIFIER_UPDATE_ERROR = -2;
    // verifier install error
    public static final int VERIFIER_INSTALL_ERROR = -3;
    // verifier install error
    public static final int VERIFIER_UNZIP_ERROR = -4;
    // signture error
    public static final int SIGNTURE_ERROR = -5;
    // signture error
    public static final int MANAGER_NOT_INIT = -6;

    private int errorCode = 0;

    public APInstallException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public APInstallException(int errorCode, Exception exception) {
        super(exception);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
