package Data;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
  
 public class DBUtil {
   //其中mysql是数据库名称，在mysql57版本的数据库中已经预先新建完成；3306是mysql数据库的端口号。
	 private static final String url = "jdbc:mysql://localhost:3306/sys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
   //com.mysql.jdbc.Driver是mysql-connector-java-5.1.40中的驱动包路径
   private static String driverClass="com.mysql.cj.jdbc.Driver";
   //mysql的账号和密码是在安装mysql中进行设置的，这里拿来用即可。
   private static String username="root";
   private static String password="903064675";
   private static Connection conn;
   //装载驱动
   static{
     try{
       Class.forName(driverClass);
     }
     catch(ClassNotFoundException e){
       e.printStackTrace();
     }
   }
   //获取数据库连接
   public static Connection getConnection(){
     try{
       conn=DriverManager.getConnection(url,username,password);
     }
     catch(SQLException e){
       e.printStackTrace();
     }
     return conn;
   }                 
   
   public static Boolean addUserInfo(String uid,String psw,String type){ 
	try {
		   String sql = "insert into sys.user_info (uid,password,type) values(?,?,?)";
		   PreparedStatement stmt = getConnection().prepareStatement(sql);
		   stmt.setString(1, uid);
           stmt.setString(2, psw);
           stmt.setString(3, type);
           stmt.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}finally {		
		close();
	}
	return true;
 }
   
   public static Boolean updateUserInfo(String uid,String psw,
		   String name, String address, String phone,String url){ 
	try {
		   String sql = "update sys.user_info set name=?,address=?,phone=?,imgUrl=?,password=? where uid=?";
		   PreparedStatement stmt = getConnection().prepareStatement(sql);
		   stmt.setString(1, name);
           stmt.setString(2, address);
           stmt.setString(3, phone);
           stmt.setString(4, url);
           stmt.setString(5, psw);
           stmt.setInt(6, Integer.parseInt(uid));
           stmt.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}finally {		
		close();
	}
	return true;
 }

   public static String userExist(String uid,String psw,String type){ 
	try {
		   Statement stmt = getConnection().createStatement();
	       String sql="select * from sys.user_info where uid='"+uid+"' and password='"+psw +"' and type='" + type + "'";
	       ResultSet rs=stmt.executeQuery(sql);
	       Map<String,String> result = new HashMap<>();
	       while(rs.next())
	       {
	    	  result.put("uid", rs.getString("uid"));
	    	  result.put("seller", rs.getString("password"));
	    	  result.put("title", rs.getString("name"));
	    	  result.put("price", rs.getString("phone"));
	    	  result.put("category", rs.getString("address"));
	       }
	       Gson gson = new Gson();
	       String info = gson.toJson(result);
	       return info;
	} catch (SQLException e) {
		e.printStackTrace();
		return "";
	}finally {		
		close();
	}
   }
   
   //关闭数据库连接
   public static void close(){
     if(conn!=null){
       try{
         conn.close();
       }
       catch(SQLException e){
         e.printStackTrace();
       }
     }
   }
 }
