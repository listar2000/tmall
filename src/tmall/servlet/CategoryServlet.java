package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.util.DBUtil;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class CategoryServlet extends BaseBackServlet{

	@Override
	public String add(HttpServletRequest req, HttpServletResponse res, Page page) 
	{
		Map<String, String> params = new HashMap<>();
		InputStream is = parseUpload(req, params);
		
		String name = params.get("name");
		
		Category c = new Category();
		c.setName(name);
		categoryDao.add(c);
		
		File imageFolder = new File(req.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, c.getId()+".jpg");
		file.getParentFile().mkdirs();

		DBUtil.copyFile(is, file);

		return "@admin_category_list";
	}

	@Override
	public String delete(HttpServletRequest req, HttpServletResponse res, Page page) 
	{
		int id = Integer.parseInt(req.getParameter("id"));
		categoryDao.delete(id);
		return "@admin_category_list";
	}

	@Override
	public String edit(HttpServletRequest req, HttpServletResponse res, Page page) 
	{
		int id = Integer.parseInt(req.getParameter("id"));
		Category c = categoryDao.get(id);
		req.setAttribute("c", c);
		return "admin/editCategory.jsp";
	}

	@Override
	public String update(HttpServletRequest req, HttpServletResponse res, Page page) 
	{
		Map<String, String> params = new HashMap<>();
		
		InputStream is = super.parseUpload(req, params);
		//System.out.println(params);
		String name = params.get("name");
		//����������id��ֵ����Ϊurl����ʱ����id
		int id = Integer.parseInt(params.get("id"));
		
		Category c = new Category();
		c.setName(name);
		c.setId(id);
		
		categoryDao.update(c);
		
		File imageFolder = new File(req.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, c.getId()+".jpg");
		file.getParentFile().mkdirs();
		
		try {
			if (is != null && is.available() != 0) {
				try(FileOutputStream fos = new FileOutputStream(file)) {
	                byte b[] = new byte[1024 * 1024];
	                int length = 0;
	                while (-1 != (length = is.read(b))) {
	                    fos.write(b, 0, length);
	                }
	                fos.flush();
	                BufferedImage img = ImageUtil.change2JPG(file);
	                ImageIO.write(img, "jpg", file);       
	            }
	            catch(Exception e){
	                e.printStackTrace();
	            }
			}
		} catch (Exception e) {
		}
		return "@admin_category_list";
	}
	
	public String list(HttpServletRequest req, HttpServletResponse res, Page page) 
	{
		List<Category> cs = categoryDao.list(page.getStart(), page.getEnd());
		int total = categoryDao.getTotal();
		page.setTotal(total);
		
		req.setAttribute("page", page);
		req.setAttribute("thecs", cs);
		
		return "admin/listCategory.jsp";
	}

}
