
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
import tch.zijidaserver.entity.Choise;

import java.util.List;

@Repository
public class ChoiseDao {
    private Log log=LogFactory.getLog(ChoiseDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    NamedParameterJdbcTemplate givenParamJdbcTemp;
    //查询某选项的所有题目
    public List<Choise> queryQuestionChoiseList(long questionId) {
        try{
            RowMapper<Choise> rowMapper=new BeanPropertyRowMapper<Choise>(Choise.class);
            String sql="select * from t_choise where question_id=? order by create_time";
            log.info("[SQL]"+sql+"<====>"+questionId);
            return Choise.toObject(jdbc.queryForList(sql,questionId));
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }

    //根据id查选项详情
    public Choise queryChoiseById(long id) {
        try{
            RowMapper<Choise> rowMapper=new BeanPropertyRowMapper<Choise>(Choise.class);
            String sql="select * from t_choise where id=?";
            log.info("[SQL]"+sql+"<====>"+id);
            Choise choise=jdbc.queryForObject(sql, rowMapper,id);
            return choise;
        }catch (Exception e){
            log.error(e);
            return null;
        }
   }

   //新增选项
   public long insert(Choise choise){
        try{
            String sql="INSERT INTO t_choise (question_id,name,create_time)" +
                    " VALUES (:question_id,:name,SYSDATE())";
            log.info("[SQL]"+sql+"<====>"+choise.getQuestion_id()+","+choise.getName());
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("question_id", choise.getQuestion_id());
            params.addValue("name", choise.getName());
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
    //更新选项
    public boolean update(Choise choise){
        try {
            String sql="UPDATE t_choise SET name=? WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+choise.getName()+","+choise.getId());
            jdbc.update(sql,choise.getName(),choise.getId());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //删除选项
    public boolean delete(long choiseId){
        try {
            String sql="DELETE from t_choise WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+choiseId);
            jdbc.update(sql,choiseId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //删除某问题所有选项
    public boolean deleteByQuestion(long questionId){
        try {
            String sql="DELETE from t_choise WHERE question_id=?";
            log.info("[SQL]"+sql+"<====>"+questionId);
            jdbc.update(sql,questionId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}

