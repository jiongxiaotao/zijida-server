
package tch.zijidaserver.entity;

import java.io.Serializable;

/**
 * Created by ainstain on 2017/11/10.
 */
public class MiniBean implements Serializable {

    protected int errcode;

    protected String errmsg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
