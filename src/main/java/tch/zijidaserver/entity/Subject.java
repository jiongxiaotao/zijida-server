
package tch.zijidaserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Date;
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
public class Subject {

    private long id;
	private long project_id;
	private String name;
    private int max_score;
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

	public int getMax_score() {
		return max_score;
	}

	public void setMax_score(int max_score) {
		this.max_score = max_score;
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
		return "Subject{" +
				"id=" + id +
				", project_id=" + project_id +
				", name='" + name + '\'' +
				", max_score='" + max_score + '\'' +
				'}';
	}

	//Map转本类对象
	public static Subject toObject(Map<String, Object> map) {
		Subject subject = new Subject();
		subject.setId((long) map.get("id"));
		subject.setProject_id((long) map.get("project_id"));
		subject.setName((String) map.get("name"));
		subject.setMax_score((int) map.get("max_score"));
		subject.setCreate_time((Timestamp)map.get("create_time"));
		return subject;
	}
	public static List<Subject> toObject(List<Map<String, Object>> mapList){
		List<Subject> list = new ArrayList<Subject>();
		for (Map<String, Object> map : mapList) {
			Subject subject = (Subject) Subject.toObject(map);
			if (subject != null) {
				list.add(subject);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Subject mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setName(rs.getString("name"));
		this.setProject_id(rs.getLong("project_id"));
		this.setMax_score(rs.getInt("max_score"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		return this;
	}
}
