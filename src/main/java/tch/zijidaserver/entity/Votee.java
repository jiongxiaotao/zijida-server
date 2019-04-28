
package tch.zijidaserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by ainstain on 2017/11/10.
 */
public class Votee {

    private long id;
	private long project_id;
	private String name;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private String create_time;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProject_id() {
		return project_id;
	}

	public void setProject_id(long project_id) {
		this.project_id = project_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		this.create_time = sdf.format(create_time);
	}

	@Override
	public String toString() {
		return "Votee{" +
				"id=" + id +
				", project_id=" + project_id +
				", name='" + name + '\'' +
				'}';
	}

	//Map转本类对象
	public static Votee toObject(Map<String, Object> map) {
		Votee votee = new Votee();
		votee.setId((long) map.get("id"));
		votee.setProject_id((long) map.get("project_id"));
		votee.setName((String) map.get("name"));
		votee.setCreate_time((Timestamp)map.get("create_time"));
		return votee;
	}
	public static List<Votee> toObject(List<Map<String, Object>> mapList){
		List<Votee> list = new ArrayList<Votee>();
		for (Map<String, Object> map : mapList) {
			Votee votee = (Votee) Votee.toObject(map);
			if (votee != null) {
				list.add(votee);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Votee mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setName(rs.getString("name"));
		this.setProject_id(rs.getLong("project_id"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		return this;
	}
}
