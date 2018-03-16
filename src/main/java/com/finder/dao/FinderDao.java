package com.finder.dao;

import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.List;  
import org.springframework.jdbc.core.BeanPropertyRowMapper;  
import org.springframework.jdbc.core.JdbcTemplate;  
import org.springframework.jdbc.core.RowMapper;  
import com.finder.model.FinderInfo;  
  
public class FinderDao {  
JdbcTemplate template;  
  
public void setTemplate(JdbcTemplate template) {  
    this.template = template;  
}  
public int save(FinderInfo finderInfo){  
    String sql="insert into finder(area,recepti,date,cause,estate,lotnumber,house,folderpath,username,password) values('"+finderInfo.getArea()+"',"+finderInfo.getRecepti()+",'"+finderInfo.getDate()+",'"+finderInfo.getCause()+",'"+finderInfo.getEstate()+"',"+finderInfo.getLotnumber()+",'"+finderInfo.getHouse()+"',"+finderInfo.getFolderpath()+"',"+finderInfo.getUsername()+"',"+finderInfo.getPassword()+"')";  
    return template.update(sql);  
}  

//public int delete(int id){  
//    String sql="delete from finder where id="+id+"";  
//    return template.update(sql);  
//}  
//public FinderInfo getEmpById(int id){  
//    String sql="select * from finder where id=?";  
//    return template.queryForObject(sql, new Object[]{id},new BeanPropertyRowMapper<FinderInfo>(FinderInfo.class));  
//}  
//public List<FinderInfo> getEmployees(){  
//    return template.query("select * from finder",new RowMapper<FinderInfo>(){  
//        public FinderInfo mapRow(ResultSet rs, int row) throws SQLException {  
//            FinderInfo f=new FinderInfo();  
//            f.setId(rs.getInt(1));  
//            f.setArea(rs.getString(2));
//            f.setRecepti(rs.getString(3));
//            f.setDate(rs.getString(4));
//            f.setCause(rs.getString(5));
//            f.setEstate(rs.getString(6));
//            f.setLotnumber(rs.getString(7));
//            f.setHouse(rs.getString(8));
//            f.setPassword(rs.getString(9));
//            f.setUsername(rs.getString(10));
//            return f;  
//        }  
//    });  
//}  
} 
