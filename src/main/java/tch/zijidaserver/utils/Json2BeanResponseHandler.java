
package tch.zijidaserver.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * Created by ainstain on 2017/11/10.
 */
public class Json2BeanResponseHandler<T> implements ResponseHandler<T> {

    private Log log = LogFactory.getLog(Json2BeanResponseHandler.class);

    private String uri;

    private Class<T> targetClass;

    public Json2BeanResponseHandler(String uri, Class<T> targetClass) {
        this.uri = uri;
        this.targetClass = targetClass;
    }

    @Override
    public T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (isOk(status)) {

            HttpEntity entity = httpResponse.getEntity();

            String str = EntityUtils.toString(entity, "utf-8");

            return JSON.parseObject(str, targetClass);
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }

    private boolean isOk(int status) {
        return status >= 200 && status < 300;
    }
}
