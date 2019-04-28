
package tch.zijidaserver.dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tch.zijidaserver.entity.ProjectUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ProjectUserDao {
    private Log log=LogFactory.getLog(ProjectUserDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    NamedParameterJdbcTemplate givenParamJdbcTemp;
    //查询某user是否给某project打分
    public ProjectUser query(long projectId,String userId) {
        try{
            RowMapper<ProjectUser> rowMapper=new BeanPropertyRowMapper<ProjectUser>(ProjectUser.class);
            String sql="select * from t_project_user where project_id=? and user_id=?";
            log.info("[SQL]"+sql+"<====>"+projectId+","+userId);
            ProjectUser projectUser=jdbc.queryForObject(sql, rowMapper,projectId,userId);
            return projectUser;
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }

   //新增项目
   public boolean insert(long projectId,String userId){
        try{
            String sql="INSERT INTO t_project_user (project_id,user_id,create_time)" +
                    " VALUES (?,?,SYSDATE())";
            log.info("[SQL]"+sql+"<====>"+projectId+","+userId);
            jdbc.update(sql,projectId,userId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
   }

}

