
package tch.zijidaserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by ainstain on 2017/11/10.
 */
public class Project {

    private long id;
	private String user_id;
	private String name;//项目名称
	private String type;//项目类型：p:评分,t:投票,w:问卷
    private String status;//项目状态：状态：0-未发布，1-已发布，2-已开放评分，9-已结束
    private String invite_code;//邀请码，用户进入时的密码
    private int amount;//预计收集量
	private int done_amount;//已完成评分个数
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private String create_time;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private String update_time;
    public Project() {

    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInvite_code() {
		return invite_code;
	}

	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getDone_amount() {
		return done_amount;
	}

	public void setDone_amount(int done_amount) {
		this.done_amount = done_amount;
	}

	public String getCreate_time() {
		return create_time;
	}
	//timestamp转string
	public void setCreate_time(Timestamp create_time) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		this.create_time = sdf.format(create_time);
	}

	public String getUpdate_time() {
		return update_time;
	}
	//timestamp转string
	public void setUpdate_time(Timestamp update_time) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		this.update_time = sdf.format(update_time);
	}

	@Override
	public String toString() {
		return "Project{" +
				"id=" + id +
				", user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", status='" + status + '\'' +
				", invite_code='" + invite_code + '\'' +
				", amount=" + amount +
				", done_amount=" + done_amount +
				", create_time=" + create_time +
				", update_time=" + update_time +
				'}';
	}

	//Map转本类对象
	public static Project toObject(Map<String, Object> map) {
		Project project = new Project();
		project.setId((long) map.get("id"));
		project.setUser_id((String) map.get("user_id"));
		project.setName((String) map.get("name"));
		project.setType((String) map.get("type"));
		project.setStatus((String) map.get("status"));
		project.setInvite_code((String)map.get("invite_code"));
		project.setAmount(((Long)map.get("amount")).intValue());
		project.setDone_amount(((Long)map.get("done_amount")).intValue());
		project.setCreate_time((Timestamp) map.get("create_time"));
		project.setUpdate_time((Timestamp) map.get("update_time"));
		return project;
	}
	public static List<Project> toObject(List<Map<String, Object>> mapList){
		List<Project> list = new ArrayList<Project>();
		for (Map<String, Object> map : mapList) {
			Project project = (Project) Project.toObject(map);
			if (project != null) {
				list.add(project);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Project mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setUser_id(rs.getString("user_id"));
		this.setName(rs.getString("name"));
		this.setType(rs.getString("type"));
		this.setStatus(rs.getString("status"));
		this.setInvite_code(rs.getString("invite_code"));
		this.setAmount(rs.getInt("amount"));
		this.setDone_amount(rs.getInt("done_amount"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		this.setUpdate_time(rs.getTimestamp("update_time"));
		return this;
	}
}
