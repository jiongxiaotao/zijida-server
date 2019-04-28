
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
import java.util.TimeZone;


/**
 * Created by ainstain on 2017/11/10.
 */
public class ProjectUser {

    private long id;
    private long project_id;
	private String user_id;
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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	@Override
	public String toString() {
		return "ProjectUser{" +
				"id=" + id +
				", project_id=" + project_id +
				", user_id='" + user_id + '\'' +
				", create_time='" + create_time + '\'' +
				'}';
	}

	//Map转本类对象
	public static ProjectUser toObject(Map<String, Object> map) {
		ProjectUser project = new ProjectUser();
		project.setId((long) map.get("id"));
		project.setUser_id((String) map.get("user_id"));
		project.setProject_id((long) map.get("project_id"));
		project.setCreate_time((Timestamp) map.get("create_time"));
		return project;
	}
	public static List<ProjectUser> toObject(List<Map<String, Object>> mapList){
		List<ProjectUser> list = new ArrayList<ProjectUser>();
		for (Map<String, Object> map : mapList) {
			ProjectUser project = (ProjectUser) ProjectUser.toObject(map);
			if (project != null) {
				list.add(project);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public ProjectUser mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setProject_id(rs.getLong("project_id"));
		this.setUser_id(rs.getString("user_id"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		return this;
	}
}
