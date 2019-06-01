
package tch.zijidaserver.dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tch.zijidaserver.entity.Score;

import java.util.List;
import java.util.Map;

@Repository
public class ScoreDao {
    private Log log=LogFactory.getLog(ScoreDao.class);
    @Autowired
    private JdbcTemplate jdbc;

    //查询某被评人的各评分项和其平均分
    public List<Map<String,Object>> querySubjectScoreListByVoteeId(long voteeId) {
        try{
            RowMapper<Score> rowMapper=new BeanPropertyRowMapper<Score>(Score.class);
            String sql="select CAST(AVG(score) as DECIMAL(15,2)) as score, b.name,b.max_score" +
                    " from t_score a inner join t_subject b on a.subject_id=b.id " +
                    "where a.votee_id=? GROUP BY a.subject_id";

            log.info("[SQL]"+sql+"<====>"+voteeId);
            return jdbc.queryForList(sql,voteeId);
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
    //查询每个被评人的总分和排名
    public List<Map<String,Object>> queryVoteeScoreOrder(long projectId) {
        try{
            RowMapper<Score> rowMapper=new BeanPropertyRowMapper<Score>(Score.class);
            String sql="select a.votee_id,SUM(score) AS score,b.name " +
                    "from t_score a inner join t_votee b " +
                    "on a.votee_id=b.id where b.project_id=? " +
                    "GROUP BY a.votee_id order by score desc";
            log.info("[SQL]"+sql+"<====>"+projectId);
            return jdbc.queryForList(sql,projectId);
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
    //查询某用户是否给某项目打过分
    public int queryUserProjectDone(long projectId,String userId) {
        try{
            String sql="select count(*) from t_score a inner join t_subject b " +
                    "on a.subject_id=b.id and b.project_id=? and a.user_id=?";
            log.info("[SQL]"+sql+"<====>"+projectId+","+userId);
            return jdbc.queryForObject(sql, Integer.class,projectId,userId);
        }catch (Exception e){
            log.error(e);
            return -1;
        }
   }

   //新增打分
   public boolean insert(Score score){
        try{
            String sql="INSERT INTO t_score (user_id,subject_id,votee_id,score) VALUES (?,?,?,?)";
            log.info("[SQL]"+sql+"<====>"+score.getUser_id()+","+score.getSubject_id()+","+
                    score.getVotee_id()+","+score.getScore());
            jdbc.update(sql,score.getUser_id(),score.getSubject_id(),score.getVotee_id(),score.getScore());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
   }
    //删除某用户给某项目的所有打分
    public boolean deleteUserProjectScore(long projectId,String userId){
        try{
            String sql="delete from t_score where t_score.id in " +
                    "(select id from (select a.* from t_score a,t_subject " +
                    "where a.subject_id=t_subject.id and t_subject.project_id=? and a.user_id=?) t1)";
            log.info("[SQL]"+sql+"<====>"+projectId+","+userId);
            jdbc.update(sql,projectId,userId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}

