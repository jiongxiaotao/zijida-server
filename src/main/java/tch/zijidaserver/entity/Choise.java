
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
public class Choise {

    private long id;
	private long question_id;
	private String name;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private String create_time;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
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
		return "Choise{" +
				"id=" + id +
				", question_id='" + question_id + '\'' +
				", name='" + name + '\'' +
				", create_time='" + create_time + '\'' +
				'}';
	}

	//Map转本类对象
	public static Choise toObject(Map<String, Object> map) {
		Choise choise = new Choise();
		choise.setId((long) map.get("id"));
		choise.setQuestion_id((long) map.get("question_id"));
		choise.setName((String) map.get("name"));
		choise.setCreate_time((Timestamp)map.get("create_time"));
		return choise;
	}
	public static List<Choise> toObject(List<Map<String, Object>> mapList){
		List<Choise> list = new ArrayList<Choise>();
		for (Map<String, Object> map : mapList) {
			Choise choise = (Choise) Choise.toObject(map);
			if (choise != null) {
				list.add(choise);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Choise mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setQuestion_id(rs.getLong("question_id"));
		this.setName(rs.getString("name"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		return this;
	}
}
