package com.yang.auto.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Created by zhs on 18-8-12.
 * <p>
 * 日志追踪工具类
 * <p>
 * 需要在slf4j/log4j配置文件里做相应配置
 * <p>
 * 以logback为例，在encoder的pattern里加上"%X{sbtTraceId}"，完整示例如下："%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%L] %X{sbtTraceId} %m%n"
 */
public class SbtLogTrace {

    public static final String SBT_TRACE_ID = "sbtTraceId";

    /**
     * 进入trace。可以在各个入口调用, 比如Filter开头
     *
     * @return 可追踪上下文信息设置是否成功
     */
    public static boolean enterTrace() {
        String sbtTraceId = MDC.get(SBT_TRACE_ID);
        if (sbtTraceId != null) {
            // 如果统一给dubbo方法加切面, 很可能会出现这种情况。保证正常情况没问题, 忽略这种情况的日志。
            return false; // ignore
        }
        sbtTraceId = "sbtt" + UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(SBT_TRACE_ID, sbtTraceId);
        return true;
    }

    /**
     * 退出trace。可以在出口的finally代码块里调用, 比如Filter结尾
     */
    public static void exitTrace() {
        MDC.remove(SBT_TRACE_ID);
    }

    public static String getTraceId() {
        return MDC.get(SBT_TRACE_ID);
    }

}
