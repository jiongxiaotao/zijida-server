
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
import tch.zijidaserver.entity.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ProjectDao {
    private Log log=LogFactory.getLog(ProjectDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    NamedParameterJdbcTemplate givenParamJdbcTemp;
    //查询所有项目列表+当前已完成数
    public List<Project> queryUserProjectList(String unionId) {
        try{
            RowMapper<Project> rowMapper=new BeanPropertyRowMapper<Project>(Project.class);
            Map<String, Object> args  = new HashMap<>();
            String sql="select * from t_project where user_id=:id order by create_time desc";
            log.info("[SQL]"+sql+"<====>"+unionId);
            args.put("id",unionId);
            givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbc);
            List<Project> projectList = givenParamJdbcTemp.query(sql, args, new RowMapper<Project>() {
                @Override
                public Project mapRow(ResultSet rs, int index) throws SQLException {
                    Project proj = new Project();
                    proj.mapRow(rs, index);
                    return proj;
                }
            });
            return projectList;
        }catch (Exception e){
            log.error(e);
            return null;
        }

    }
    //按状态查询项目列表+当前已完成数
    public List<Project> queryUserProjectListByStatus(String unionId,String statusList) {
        try{
            RowMapper<Project> rowMapper=new BeanPropertyRowMapper<Project>(Project.class);
            Map<String, Object> args  = new HashMap<>();
            String sql="select * from t_project where user_id=:id and status in (:statusArr) order by create_time desc";
            log.info("[SQL]"+sql+"<====>"+unionId+","+statusList);
            //数组转arrayList
            String[] array=statusList.split(",");
            List<String> statusArr= new ArrayList<String>(array.length);
            Collections.addAll(statusArr, array);
            //拼接参数
            args.put("id", unionId);
            args.put("statusArr", statusArr);
            givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbc);
            List<Project> projectList = givenParamJdbcTemp.query(sql, args, new RowMapper<Project>() {
                @Override
                public Project mapRow(ResultSet rs, int index) throws SQLException {
                    Project proj = new Project();
                    proj.mapRow(rs, index);
                    return proj;
                }
            });
            return projectList;
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
    //根据id查项目详情
    public Project queryProjectById(long id) {
        try{
            RowMapper<Project> rowMapper=new BeanPropertyRowMapper<Project>(Project.class);
            String sql="select * from t_project where id=?";
            log.info("[SQL]"+sql+"<====>"+id);
            Project project=jdbc.queryForObject(sql, rowMapper,id);
            //查完项目基本信息，要查当前完成打分的个数
            return project;
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
    //invite_code
    public Project queryProjectByInviteCode(String inviteCode) {
        try{
            RowMapper<Project> rowMapper=new BeanPropertyRowMapper<Project>(Project.class);
            String sql="select * from t_project where invite_code=?";
            log.info("[SQL]"+sql+"<====>"+inviteCode);
            Project project=jdbc.queryForObject(sql, rowMapper,inviteCode);
            return project;
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
   //新增项目
   public long insert(Project project){
        try{
            String sql="INSERT INTO t_project (user_id,name,type,status,invite_code,amount,create_time)" +
                    " VALUES (:user_id,:name,:type,'0',:invite_code,:amount,SYSDATE())";
            log.info("[SQL]"+sql+"<====>"+project.getUser_id()+","+project.getName()+","
                    +project.getType()+","+project.getInvite_code()+","+project.getAmount());
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user_id", project.getUser_id());
            params.addValue("name", project.getName());
            params.addValue("type", project.getType());
            params.addValue("invite_code", project.getInvite_code());
            params.addValue("amount", project.getAmount());
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
    public boolean update(Project project){
        try {
            String sql="UPDATE t_project SET name=?,type=?,status=?,invite_code=?,amount=? WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+project.getName()+","+project.getType()+","+project.getStatus()+
                    ","+project.getInvite_code()+","+project.getAmount());

            jdbc.update(sql,project.getName(),project.getType(),project.getStatus(),
                    project.getInvite_code(),project.getAmount(),project.getId());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //删除项目
    public boolean delete(long projectId){
        try {
            String sql="DELETE from t_project WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+projectId);
            jdbc.update(sql,projectId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    //已完成个数加1
    public boolean increaseDoneAmount(long projectId){
        try {
            String sql="UPDATE t_project SET done_amount=(done_amount+1) WHERE id=?";
            log.info("[SQL]"+sql+"<====>"+projectId);
            jdbc.update(sql,projectId);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}

