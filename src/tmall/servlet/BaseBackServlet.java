package tmall.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tmall.util.Page;
import tmall.dao.*;

public abstract class BaseBackServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6474725960528059632L;

	public abstract String add(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String delete(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String edit(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String update(HttpServletRequest req, HttpServletResponse res, Page page);
	
	protected CategoryDao categoryDao = new CategoryDao();
    protected OrderDao orderDao = new OrderDao();
    protected OrderItemDao orderItemDao = new OrderItemDao();
    protected ProductDao productDao = new ProductDao();
    protected ProductImageDao productImageDao = new ProductImageDao();
    protected PropertyDao propertyDao = new PropertyDao();
    protected PropertyValueDao propertyValueDao = new PropertyValueDao();
    protected ReviewDao reviewDao = new ReviewDao();
    protected UserDao userDao = new UserDao();

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{	
		int start = 0;
		int end = 5;
		
		try {
			start = Integer.parseInt(req.getParameter("page.start"));
			end = Integer.parseInt(req.getParameter("page.end"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Page page = new Page(start, end);
		
		//reflection
		String method = (String) req.getAttribute("method");
		
		try {
			
			Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
			        javax.servlet.http.HttpServletResponse.class, Page.class);
			String redirect = m.invoke(this, req, res, page).toString();
			
			if (redirect.startsWith("@")) {
				res.sendRedirect(redirect.substring(1));
			}
			else if (redirect.startsWith("%")) {
				res.getWriter().println(redirect.substring(1));
			}
			else {
				req.getRequestDispatcher(redirect).forward(req, res);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public InputStream parseUpload(HttpServletRequest req, Map<String, String> params) 
	{
		InputStream is = null;
		
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//��������ļ���С10M
			factory.setSizeThreshold(1024*10240);
			
			List<?> items = upload.parseRequest(req);
			Iterator<?> iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				
				if (!item.isFormField()) {
					is = item.getInputStream();
				}
				else {
					String paramName = item.getFieldName();
					String paramValue = item.getString();
					paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
					params.put(paramName, paramValue);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return is;
	}
	
}
