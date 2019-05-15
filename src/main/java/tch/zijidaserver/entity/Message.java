
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
public class Message {

    private long id;
	private String user_id;
	private String name;
    private String type;
    private int duration;
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private String create_time;
    public Message() {

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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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
		return "Message{" +
				"id=" + id +
				", user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", duration='" + duration + '\'' +
				", create_time='" + create_time + '\'' +
				'}';
	}

	//用于NamedParameterJdbcTemplate中resultset与当前类转换
	public Message mapRow(ResultSet rs, int index) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setUser_id(rs.getString("user_id"));
		this.setName(rs.getString("name"));
		this.setType(rs.getString("type"));
		this.setDuration(rs.getInt("duration"));
		this.setCreate_time(rs.getTimestamp("create_time"));
		return this;
	}
}
