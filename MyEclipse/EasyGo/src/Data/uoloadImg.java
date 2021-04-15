package Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class uoloadImg extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public uoloadImg() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String result = "";
		try {
			String bitmapStr = request.getParameter("img");
	
	
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String name = df.format(new Date()) + ".png";
			String dir = request.getSession().getServletContext().getRealPath("/img");
		    
			 Map<String,String> res = new HashMap<>();
			File file = new File(dir);
			if (!file.exists())  
			{  
				file.mkdir();  
			} 
			
	
			String path = dir + "/" + name;
			
		
			try {
				// Base64解码
				byte[] b = Base64.getMimeDecoder().decode(bitmapStr.replace("\r\n", ""));
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {
						b[i] += 256;
					}
				}
			
				OutputStream out = new FileOutputStream(path);
				out.write(b);
				out.flush();
				out.close();
				res.put("imgUrl", name);
				
			} catch (Exception e) {
				throw e;
			}
			 PrintWriter out = response.getWriter();
		
			 Gson gson = new Gson();
			 result = gson.toJson(res);
		     out.print(result);
		     out.flush();
		     out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
