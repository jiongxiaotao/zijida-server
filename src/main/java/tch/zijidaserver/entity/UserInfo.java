
package tch.zijidaserver.entity;

import java.io.Serializable;


/**
 * Created by ainstain on 2017/11/10.
 */
public class UserInfo{

    private String open_id;
	private String union_id;
	private String cus_name;
    private String status;
    private String province;
    private String city;
    private String avatarUrl;
    private int max_count;
    public UserInfo() {

    }

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getCus_name() {
		return cus_name;
	}

	public void setCus_name(String cus_name) {
		this.cus_name = cus_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getMax_count() {
		return max_count;
	}

	public void setMax_count(int max_count) {
		this.max_count = max_count;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"open_id='" + open_id + '\'' +
				", union_id='" + union_id + '\'' +
				", cus_name='" + cus_name + '\'' +
				", status='" + status + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", max_count=" + max_count +
				'}';
	}
}
