package com.lin.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

public class ServletUtil {
    //服务器标识
    private static String hostName = "";

    //响应的ContentType内容
    private static final String RESPONSE_CONTENT_TYPE = "application/json";

    //响应编码
    private static final String RESPONSE_CHARACTER_ENCODING = "utf-8";

    private static Logger logger = LoggerFactory.getLogger(ServletUtil.class);

    static {
        InetAddress inetAddress = InetAddress.getLoopbackAddress();
        hostName = inetAddress.getHostName();
    }

    public static String createSuccessResponse(Integer httpCode, Object result, HttpServletResponse response) {
        return createSuccessResponse(httpCode, result,
                SerializerFeature.WriteMapNullValue, null, response);
    }

    public static String createSuccessResponse(Integer httpCode, Object result,
                                               SerializerFeature serializerFeature, SerializeFilter serializeFilter,
                                               HttpServletResponse response) {
        PrintWriter printWriter = null;
        String json = "";
        try {
            printWriter = response.getWriter();
            response.setCharacterEncoding(RESPONSE_CHARACTER_ENCODING);
            response.setContentType(RESPONSE_CONTENT_TYPE);
            response.setStatus(httpCode);

            if (result != null) {
                if (serializeFilter != null) {
                    json = JSON.toJSONString(result, serializeFilter, serializerFeature);
                }else {
                    json = JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:ss:mm", serializerFeature);
                }
                printWriter.write(json);
            }
        } catch (IOException e) {
            logger.error("create Response fail, ", e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return json;

    }
}
