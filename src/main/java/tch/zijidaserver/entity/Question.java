
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
public class Question {

    private long id;
	private long project_id;
	private String name;
    private String type;
    private int number;
	private int max_multi_choise;
	private List<Choise> choiseList;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getMax_multi_choise() {
		return max_multi_choise;
	}

	public void setMax_multi_choise(int max_multi_choise) {
		this.max_multi_choise = max_multi_choise;
	}

	public List<Choise> getChoiseList() {
		return choiseList;
	}

	public void setChoiseList(List<Choise> choiseList) {
		this.choiseList = choiseList;
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
		return "Question{" +
				"id=" + id +
				", project_id=" + project_id +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", number='" + number + '\'' +
				", max_multi_choise=" + max_multi_choise +
				", create_time='" + create_time + '\'' +
				'}';
	}

	//Map转本类对象
	public static Question toObject(Map<String, Object> map) {
		Question question = new Question();
		question.setId((long) map.get("id"));
		question.setProject_id((long) map.get("project_id"));
		question.setName((String) map.get("name"));
		question.setType((String) map.get("type"));
		question.setNumber((int) map.get("number"));
		question.setMax_multi_choise((int) map.get("max_multi_choise"));
		question.setCreate_time((Timestamp)map.get("create_time"));
		return question;
	}
	public static List<Question> toObject(List<Map<String, Object>> mapList){
		List<Question> list = new ArrayList<Question>();
		for (Map<String, Object> map : mapList) {
			Question question = (Question) Question.toObject(map);
			if (question != null) {
				list.add(question);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Question mapRow(ResultSet rs, int number) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setProject_id(rs.getLong("project_id"));
		this.setName(rs.getString("name"));
		this.setType(rs.getString("type"));
		this.setNumber(rs.getInt("number"));
		this.setMax_multi_choise(rs.getInt("max_multi_choise"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		return this;
	}
}
