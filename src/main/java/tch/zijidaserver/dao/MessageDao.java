
package tch.zijidaserver.dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tch.zijidaserver.entity.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class MessageDao {
    private Log log=LogFactory.getLog(MessageDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    NamedParameterJdbcTemplate givenParamJdbcTemp;
    //查询留言列表
    public List<Message> queryUserMessageList(String unionId) {
        try{
            RowMapper<Message> rowMapper=new BeanPropertyRowMapper<Message>(Message.class);
            Map<String, Object> args  = new HashMap<>();
            String sql="select * from t_message where user_id=:id order by create_time desc";
            log.info("[SQL]"+sql+"<====>"+unionId);
            args.put("id",unionId);
            givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbc);
            List<Message> messageList = givenParamJdbcTemp.query(sql, args, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int index) throws SQLException {
                    Message proj = new Message();
                    proj.mapRow(rs, index);
                    return proj;
                }
            });
            return messageList;
        }catch (Exception e){
            log.error(e);
            return null;
        }

    }
   //新增留言
   public long insert(Message message){
        try{
            String sql="INSERT INTO t_message (user_id,name,type,duration)" +
                    " VALUES (:user_id,:name,'0',:duration)";
            log.info("[SQL]"+sql+"<====>"+message.getUser_id()+","+message.getName()+","+message.getDuration());
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user_id", message.getUser_id());
            params.addValue("name", message.getName());
            params.addValue("duration", message.getDuration());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            long autoIncId = 0;
            givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbc);
            givenParamJdbcTemp.update( sql, params, keyHolder, new String[] {"id" });
            autoIncId = keyHolder.getKey().intValue();
            log.info("autoincid="+autoIncId);
            return autoIncId;
        }catch (Exception e){
            log.error(e);
            return -1;
        }
   }

}

