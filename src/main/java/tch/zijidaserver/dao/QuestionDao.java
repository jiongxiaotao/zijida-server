
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
import tch.zijidaserver.entity.Question;

import java.util.List;

@Repository
public class QuestionDao {
    private Log log=LogFactory.getLog(QuestionDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    NamedParameterJdbcTemplate givenParamJdbcTemp;
    //查询所有某项目的所有题目
    public List<Question> queryProjectQuestionList(long projectId) {
        try{
            RowMapper<Question> rowMapper=new BeanPropertyRowMapper<Question>(Question.class);
            String sql="select * from t_question where project_id=? order by number";
            log.info("[SQL]"+sql+"<====>"+projectId);
            return Question.toObject(jdbc.queryForList(sql,projectId));
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }

    //根据id查项目详情
    public Question queryQuestionById(long id) {
        try{
            RowMapper<Question> rowMapper=new BeanPropertyRowMapper<Question>(Question.class);
            String sql="select * from t_question where id=?";
            log.info("[SQL]"+sql+"<====>"+id);
            Question question=jdbc.queryForObject(sql, rowMapper,id);
            return question;
        }catch (Exception e){
            log.error(e);
            return null;
        }
   }

   //新增项目
   public long insert(Question question){
        try{
            String sql="INSERT INTO t_question (project_id,name,type,number,max_multi_choise,create_time)" +
                    " VALUES (:project_id,:name,:type,:number,:max_multi_choise,SYSDATE())";
            log.info("[SQL]"+sql+"<====>"+question.getProject_id()+","+question.getName()+","+
                    question.getType()+","+question.getNumber()+","+question.getMax_multi_choise());
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("project_id", question.getProject_id());
            params.addValue("name", question.getName());
            params.addValue("type", question.getType());
            params.addValue("number", question.getNumber());
            params.addValue("max_multi_choise", question.getMax_multi_choise());
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
    //更新项目
    public boolean update(Question question){
        try {
            String sql="UPDATE t_question SET name=?,type=?,number=?,max_multi_choise=? WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+question.getName()+","+question.getType()+","+
                    question.getNumber()+","+question.getMax_multi_choise()+","+question.getId());
            jdbc.update(sql,question.getName(),question.getType(),question.getNumber(),
                    question.getMax_multi_choise(),question.getId());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //删除项目
    public boolean delete(long questionId){
        try {
            String sql="DELETE from t_question WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+questionId);
            jdbc.update(sql,questionId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}

