
package tch.zijidaserver.dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tch.zijidaserver.entity.Votee;

import java.util.List;

@Repository
public class VoteeDao {
    private Log log=LogFactory.getLog(VoteeDao.class);
    @Autowired
    private JdbcTemplate jdbc;

    //查询所有项目列表+当前已完成数
    public List<Votee> queryProjectVoteeList(long projectId) {
        try{
            RowMapper<Votee> rowMapper=new BeanPropertyRowMapper<Votee>(Votee.class);
            String sql="select * from t_votee where project_id=? order by create_time";
            log.info("[SQL]"+sql+"<====>"+projectId);
            return Votee.toObject(jdbc.queryForList(sql,projectId));
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }

    //根据id查项目详情
    public Votee queryVoteeById(long id) {
        try{
            RowMapper<Votee> rowMapper=new BeanPropertyRowMapper<Votee>(Votee.class);
            String sql="select * from t_votee where id=?";
            log.info("[SQL]"+sql+"<====>"+id);
            Votee votee=jdbc.queryForObject(sql, rowMapper,id);
            return votee;
        }catch (Exception e){
            log.error(e);
            return null;
        }
   }

   //新增项目
   public boolean insert(Votee votee){
        try{
            String sql="INSERT INTO t_votee (project_id,name,create_time)" +
                    " VALUES (?,?,SYSDATE())";
            log.info("[SQL]"+sql+"<====>"+votee.getProject_id()+","+votee.getName());
            jdbc.update(sql,votee.getProject_id(),votee.getName());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
   }
    //更新项目
    public boolean update(Votee votee){
        try {
            String sql="UPDATE t_votee SET name=? WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+votee.getName());
            jdbc.update(sql,votee.getName(),votee.getId());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //删除项目
    public boolean delete(long voteeId){
        try {
            String sql="DELETE from t_votee WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+voteeId);
            jdbc.update(sql,voteeId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}

