
package tch.zijidaserver.dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tch.zijidaserver.entity.UserInfo;

@Repository
public class UserInfoDao {
    private Log log=LogFactory.getLog(UserInfoDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    //根据openId+status查询用户信息
    public UserInfo getByOpenIdStatus(String openId, String status) {
        try{
            RowMapper<UserInfo> rowMapper=new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
            String sql="select * from t_user where open_id=? and status=?";
            log.info("[SQL]"+sql+"<====>"+openId);
            return jdbc.queryForObject(sql, rowMapper,openId,status);
        }catch (Exception e){
            log.error(e);
            return null;
        }

    }
  //根据unionId唯一标识查询用户信息
    public UserInfo getByUnionId(String unionId) {
        try{
            RowMapper<UserInfo> rowMapper=new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
            String sql="select * from t_user where union_id=?";
            log.info("[SQL]"+sql+"<====>"+unionId);
            return jdbc.queryForObject(sql, rowMapper,unionId);
        }catch (Exception e){
            log.error(e);
            return null;
        }

   }
   //新增小程序用户信息
   public boolean insert(UserInfo userInfo){
        try{
            String sql="INSERT INTO t_user (open_id,union_id,cus_name,status,province,city,avatarUrl)" +
                    " VALUES (?,?,?,?,?,?,?)";
            log.info("[SQL]"+sql+"<====>"+userInfo.getOpen_id());
            jdbc.update(sql,userInfo.getOpen_id(),userInfo.getUnion_id(),userInfo.getCus_name(),
                    userInfo.getStatus(),userInfo.getProvince(),userInfo.getCity(),userInfo.getAvatarUrl());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }

   }
    //更新小程序用户信息
    public boolean update(UserInfo userInfo){
        try {
            String sql="UPDATE t_user SET union_id=?,cus_name=?,status=?,province=?,city=?,avatarUrl=? WHERE open_id=?";
            log.info("[SQL]"+sql+"<====>"+userInfo.getOpen_id());
            jdbc.update(sql,userInfo.getUnion_id(),userInfo.getCus_name(), userInfo.getStatus(),
                    userInfo.getProvince(),userInfo.getCity(),userInfo.getAvatarUrl(),userInfo.getOpen_id());
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }

    //根据user_id获取最大项目个数
    public int getUserMaxProjectCount(String unionId) {
        try{
            String sql="select max_count from t_user where union_id=?";
            log.info("[SQL]"+sql+"<====>"+unionId);
            return jdbc.queryForObject(sql,Integer.class,unionId);
        }catch (Exception e){
            log.error(e);
            return 0;
        }
    }
}

