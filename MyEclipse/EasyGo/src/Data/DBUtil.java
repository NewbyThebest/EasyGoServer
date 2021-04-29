package Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
  
 public class DBUtil {
   //鍏朵腑mysql鏄暟鎹簱鍚嶇О锛屽湪mysql57鐗堟湰鐨勬暟鎹簱涓凡缁忛鍏堟柊寤哄畬鎴愶紱3306鏄痬ysql鏁版嵁搴撶殑绔彛鍙枫��
	 private static final String url = "jdbc:mysql://localhost:3306/sys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
   //com.mysql.jdbc.Driver鏄痬ysql-connector-java-5.1.40涓殑椹卞姩鍖呰矾寰�
   private static String driverClass="com.mysql.cj.jdbc.Driver";
   //mysql鐨勮处鍙峰拰瀵嗙爜鏄湪瀹夎mysql涓繘琛岃缃殑锛岃繖閲屾嬁鏉ョ敤鍗冲彲銆�
   private static String username="root";
   private static String password="903064675";
   private static Connection conn;
   //瑁呰浇椹卞姩
   static{
     try{
       Class.forName(driverClass);
     }
     catch(ClassNotFoundException e){
       e.printStackTrace();
     }
   }
   //鑾峰彇鏁版嵁搴撹繛鎺�
   public static Connection getConnection(){
     try{
       conn=DriverManager.getConnection(url,username,password);
     }
     catch(SQLException e){
       e.printStackTrace();
     }
     return conn;
   }                 
   
   public static Boolean addUserInfo(String uid,String psw,String type,String phone){ 
	try {
		   String sql = "insert into sys.user_info (uid,password,type,phone) values(?,?,?,?)";
		   PreparedStatement stmt = getConnection().prepareStatement(sql);
		   stmt.setString(1, uid);
           stmt.setString(2, psw);
           stmt.setString(3, type);
           stmt.setString(4, phone);
           stmt.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}finally {		
		close();
	}
	return true;
 }
   
   public static Boolean addGoodsInfo(String title,String price,String seller,
		   String category,String sellerId,String url){ 
		try {
			   String sql = "insert into sys.goods_info (title,price,seller,category,sellerId,imgUrl,buyerId) values(?,?,?,?,?,?,?)";
			   PreparedStatement stmt = getConnection().prepareStatement(sql);
			   stmt.setString(1, title);
	           stmt.setString(2, price);
	           stmt.setString(3, seller);
			   stmt.setString(4, category);
	           stmt.setString(5, sellerId);
	           stmt.setString(6, url);
	           stmt.setString(7, "");
	           stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {		
			close();
		}
		return true;
	 }
   
   public static Boolean deleteGoodsInfo(String uid){ 
		try {
	
			   String sql = "delete from sys.goods_info where id="+uid;
			   PreparedStatement stmt = getConnection().prepareStatement(sql);
		
	           stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {		
			close();
		}
		return true;
	 }
   
   public static Boolean updateGoodsInfo(String uid,String title,
		   String price, String category, String buyerId,String url){ 
	try {
		   String sql = "update sys.goods_info set title=?,price=?,category=?,buyerId=?,imgUrl=? where id=?";
		   PreparedStatement stmt = getConnection().prepareStatement(sql);
		   stmt.setString(1, title);
           stmt.setString(2, price);
           stmt.setString(3, category);
           stmt.setString(4, buyerId);
           stmt.setString(5, url);
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
	    	  result.put("password", rs.getString("password"));
	    	  result.put("name", rs.getString("name"));
	    	  result.put("phone", rs.getString("phone"));
	    	  result.put("url", rs.getString("url"));
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
   
   public static String queryGoods(String query,int type){ 
		try {
			String sql="select * from sys.goods_info where buyerId is null or buyerId = ''";
			if(type == 0) {
				sql="select * from sys.goods_info where buyerId is null or buyerId = ''";
			}else if(type == 1) {
				sql="select * from sys.goods_info where category='"+query+"' and (buyerId is null or buyerId = '')";
			}else if(type == 2) {
				sql="select * from sys.goods_info where buyerId='"+query+"'";
			}else {
				sql="select * from sys.goods_info where sellerId='"+query+"' and buyerId <> ''";
			}
		       
		       Gson gson = new Gson();
		       PreparedStatement stmt = getConnection().prepareStatement(sql);
		       ResultSet rs = stmt.executeQuery();
		       List<Map<String,String>> list = new ArrayList<>();
		      
		       while(rs.next())
		       {
		    	 Map<String,String> result = new HashMap<>();
		    	  result.put("uid", rs.getString("id"));
		    	  result.put("seller", rs.getString("seller"));
		    	  result.put("title", rs.getString("title"));
		    	  result.put("price", rs.getString("price"));
		    	  result.put("category", rs.getString("category"));
		    	  result.put("imgUrl", rs.getString("imgUrl"));
		    	  result.put("sellerId", rs.getString("sellerId"));
		    	  result.put("buyerId", rs.getString("buyerId"));
		    	  list.add(result);
		       }
		       
		       String info = gson.toJson(list);
		       return info;
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}finally {		
			close();
		}
	   }
   
   public static String queryGoods(String sellerId,String query,int type){ 
		try {
			String sql="select * from sys.goods_info where category='"+query+"' and sellerId='"+sellerId+"' and (buyerId is null or buyerId = '')";
	
			if(type == 0) {
				sql="select * from sys.goods_info where sellerId='"+sellerId+"' and (buyerId is null or buyerId = '')";
			}
		       
		       Gson gson = new Gson();
		       PreparedStatement stmt = getConnection().prepareStatement(sql);
		       ResultSet rs = stmt.executeQuery();
		       List<Map<String,String>> list = new ArrayList<>();
		      
		       while(rs.next())
		       {
		    	 Map<String,String> result = new HashMap<>();
		    	  result.put("uid", rs.getString("id"));
		    	  result.put("seller", rs.getString("seller"));
		    	  result.put("title", rs.getString("title"));
		    	  result.put("price", rs.getString("price"));
		    	  result.put("category", rs.getString("category"));
		    	  result.put("imgUrl", rs.getString("imgUrl"));
		    	  result.put("sellerId", rs.getString("sellerId"));
		    	  result.put("buyerId", rs.getString("buyerId"));
		    	  list.add(result);
		       }
		       
		       String info = gson.toJson(list);
		       return info;
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}finally {		
			close();
		}
	   }
  
   
   //鍏抽棴鏁版嵁搴撹繛鎺�
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
