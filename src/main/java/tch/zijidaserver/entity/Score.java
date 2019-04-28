
package tch.zijidaserver.entity;

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
public class Score {

    private long id;
	private String user_id;
	private long subject_id;
    private long votee_id;
	private int score;

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

	public long getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(long subject_id) {
		this.subject_id = subject_id;
	}

	public long getVotee_id() {
		return votee_id;
	}

	public void setVotee_id(long votee_id) {
		this.votee_id = votee_id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	//Map转本类对象
	public static Score toObject(Map<String, Object> map) {
		Score score = new Score();
		score.setId((long) map.get("id"));
		score.setUser_id((String)map.get("user_id"));
		score.setSubject_id((long)map.get("subject_id"));
		score.setVotee_id((long)map.get("votee_id"));
		score.setScore((int)map.get("score"));
		return score;
	}
	public static List<Score> toObject(List<Map<String, Object>> mapList){
		List<Score> list = new ArrayList<Score>();
		for (Map<String, Object> map : mapList) {
			Score score = (Score) Score.toObject(map);
			if (score != null) {
				list.add(score);
			}
		}
		return list;
	}
	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Score mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setUser_id(rs.getString("user_id"));
		this.setSubject_id(rs.getLong("subject_id"));
		this.setVotee_id(rs.getLong("votee_id"));
		this.setScore(rs.getInt("score"));
		return this;
	}
}
