package il.co.noamsl.lostnfound.eitan.server.service;

// Represents success/error codes for the lf_server
// 0 means success, any other negative number means a 
// different error has occurred
public class LFCode {
    public static final Integer SUCCESS = 0;
    public static final Integer BAD_PARAMS = -1;
    public static final Integer EXISTS = -2;
    public static final Integer NOT_EXISTS = -3;
}
