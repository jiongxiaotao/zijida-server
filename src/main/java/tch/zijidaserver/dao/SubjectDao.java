
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
import tch.zijidaserver.entity.Subject;

import java.util.List;

@Repository
public class SubjectDao {
    private Log log=LogFactory.getLog(SubjectDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    NamedParameterJdbcTemplate givenParamJdbcTemp;
    //查询所有某项目的所有评分项
    public List<Subject> queryProjectSubjectList(long projectId) {
        try{
            RowMapper<Subject> rowMapper=new BeanPropertyRowMapper<Subject>(Subject.class);
            String sql="select * from t_subject where project_id=? order by create_time";
            log.info("[SQL]"+sql+"<====>"+projectId);
            return Subject.toObject(jdbc.queryForList(sql,projectId));
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }

    //根据id查项目详情
    public Subject querySubjectById(long id) {
        try{
            RowMapper<Subject> rowMapper=new BeanPropertyRowMapper<Subject>(Subject.class);
            String sql="select * from t_subject where id=?";
            log.info("[SQL]"+sql+"<====>"+id);
            Subject subject=jdbc.queryForObject(sql, rowMapper,id);
            return subject;
        }catch (Exception e){
            log.error(e);
            return null;
        }
   }

   //新增项目
   public long insert(Subject subject){
        try{
            String sql="INSERT INTO t_subject (project_id,name,max_score,create_time)" +
                    " VALUES (:project_id,:name,:max_score,SYSDATE())";
            log.info("[SQL]"+sql+"<====>"+subject.getProject_id()+","+subject.getName()+","+subject.getMax_score());
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("project_id", subject.getProject_id());
            params.addValue("name", subject.getName());
            params.addValue("max_score", subject.getMax_score());
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
    public boolean update(Subject subject){
        try {
            String sql="UPDATE t_subject SET name=?,max_score=? WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+subject.getName()+","+subject.getMax_score());
            jdbc.update(sql,subject.getName(),subject.getMax_score(),subject.getId());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //删除项目
    public boolean delete(long subjectId){
        try {
            String sql="DELETE from t_subject WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+subjectId);
            jdbc.update(sql,subjectId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}

