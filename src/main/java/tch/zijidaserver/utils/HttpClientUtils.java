
package tch.zijidaserver.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ainstain on 2017/11/10.
 */
public class HttpClientUtils {

    private static int socketTimeout = 100000;

    private static int requestTimeout = 100000;

    private static final Log log = LogFactory.getLog(HttpClientUtils.class);

    /**
     * getTicket methods
     */
    public static String get(String url, Map<String, String> requestParms) {
        return get(url, requestParms, new BasicResponseHandler());
    }

    public static <T> T get(String url, Map<String, String> requestParms, ResponseHandler<T> responseHandler) {
        return get(url, requestParms, responseHandler, null);
    }

    public static <T> T get(String url, Map<String, String> requestParms, ResponseHandler<T> responseHandler, RequestConfig requestConfig) {
        URI uri = buildURI(url, requestParms);
        HttpGet get = new HttpGet(uri);
        if (requestConfig == null) {
            requestConfig = createDefaultRequestConfig();
        }
        get.setConfig(requestConfig);

        return sendRequest(uri, get, responseHandler);
    }

    /**
     * post methods
     */
    public static String post(String url, Map<String, String> requestParms) {
        return post(url, requestParms, null, new BasicResponseHandler());
    }

    public static <T> T post(String url, Map<String, String> requestParms, String body, ResponseHandler<T> responseHandler) {
        return post(url, requestParms, body, responseHandler, null);
    }

    public static <T> T post(String url, Map<String, String> requestParms, String body, ResponseHandler<T> responseHandler, RequestConfig requestConfig) {

        URI uri = buildURI(url, requestParms);

        HttpPost post = new HttpPost(uri);

        if (requestConfig == null) {
            requestConfig = createDefaultRequestConfig();
        }
        post.setConfig(requestConfig);

        // set entity
        if (body != null) {
            post.setEntity(new StringEntity(body, "UTF-8"));
        }
        return sendRequest(uri, post, responseHandler);
    }

    private static <T> T sendRequest(URI uri, HttpUriRequest request, ResponseHandler<T> responseHandler) {
        CloseableHttpClient client = createHttpClient();
        try {
            log.error("http request :" + uri);
            T result = client.execute(request, responseHandler);
            try {
                log.error("http response:" + JSON.toJSONString(request));
            } catch (Throwable t) {

            }
            return result;
        } catch (Throwable t) {
            log.error("设置请求报文失败", t);
            throw new RuntimeException("XDCP2ECP00AO", t);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.error("关闭http client失败", e);
            }
        }
    }

    public static CloseableHttpClient createHttpClient() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Throwable t) {
            log.error("创建sslContext失败", t);
        }

        return HttpClients.custom().
                setSslcontext(sslContext).
                setSSLHostnameVerifier(new NoopHostnameVerifier()).
                build();
    }

    /***
     *utils
     */

    public static URI buildURI(String url, Map<String, String> requestParms) {
        URI uri = null;
        try {
            List<NameValuePair> params = toNameValuePairs(requestParms);
            uri = new URIBuilder(url).addParameters(params).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("", e);
        }
        return uri;
    }

    public static List<NameValuePair> toNameValuePairs(Map<String, String> requestParms) {
        List<NameValuePair> params = new ArrayList<NameValuePair>(4);

        if (requestParms == null) {
            return params;
        }

        for (Map.Entry<String, String> entry : requestParms.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    private static RequestConfig createDefaultRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(requestTimeout)
                .setConnectionRequestTimeout(requestTimeout)
//                .setProxy(HttpHost.create("88.16.223.1:8080"))
                .build();
    }
}
