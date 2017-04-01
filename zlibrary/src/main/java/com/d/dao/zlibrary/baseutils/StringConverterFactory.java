package com.d.dao.zlibrary.baseutils;

/**
 * Created by dao on 2017/2/18.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 用于向Retrofit提供StringConverter
 */
public class StringConverterFactory extends Converter.Factory {

    public static final StringConverterFactory INSTANCE = new StringConverterFactory();

    public static StringConverterFactory create() {
        return INSTANCE;
    }

    // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return mStringConverter;
        }
        //其它类型我们不处理，返回null就行
        return null;
    }

    private StringConverter mStringConverter = new StringConverter();

    /**
     * 自定义Converter实现RequestBody到String的转换
     */
    private class StringConverter implements Converter<ResponseBody, String> {

//        gbk
        @Override
        public String convert(ResponseBody value) throws IOException {
            InputStream in = value.byteStream();
            String result = inputStream2String(in, "utf-8");
            return result;
        }
    }

    public static String inputStream2String(InputStream in, String encoding) throws IOException {
        StringBuffer out = new StringBuffer();
        InputStreamReader inRead = new InputStreamReader(in, encoding);
        char[] b = new char[4096];
        for (int n; (n = inRead.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}
