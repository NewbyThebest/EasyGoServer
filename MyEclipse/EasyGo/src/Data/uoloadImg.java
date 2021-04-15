package Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		//返回数据给客户端
		String result = "";
		try {
			String bitmapStr = request.getParameter("img");
		
			//获取当前时间
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String name = df.format(new Date());
	
			
			String dir = request.getRealPath("/image");  
			
			String path = dir + "/" + name + ".png";
			
			File file = new File(path);
			if (!file.exists())  
			{  
			file.mkdir();  
			} 
	
			try {
				// Base64解码
				byte[] b = Base64.getMimeDecoder().decode(bitmapStr.replace("\r\n", ""));
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}
			
				OutputStream out = new FileOutputStream(path);
				out.write(b);
				out.flush();
				out.close();
				result = path;

			} catch (Exception e) {
				throw e;
			}
			 PrintWriter out = response.getWriter();
		
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