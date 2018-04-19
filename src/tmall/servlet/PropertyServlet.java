package tmall.servlet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.Page;

public class PropertyServlet extends BaseBackServlet{
	
	public String list(HttpServletRequest req, HttpServletResponse res, Page page) 
	{
		int cid = Integer.parseInt(req.getParameter("cid"));
		List<Property> ps = propertyDao.list(cid, page.getStart(), page.getEnd());
		int total = propertyDao.getTotal(cid);
		Category c = categoryDao.get(cid);

		page.setTotal(total);
		page.setParam("&cid="+c.getId());

		req.setAttribute("page", page);
		req.setAttribute("theps", ps);
		req.setAttribute("c", c);
		
		return "admin/listProperty.jsp";
	}

	@Override
	public String add(HttpServletRequest req, HttpServletResponse res, Page page)
	{
		int cid = Integer.parseInt(req.getParameter("cid"));
		Property p = new Property();
		p.setCatagory(categoryDao.get(cid));
		p.setName(req.getParameter("name"));

		propertyDao.add(p);

		return "@admin_property_list?cid=" + cid;
	}

	@Override
	public String delete(HttpServletRequest req, HttpServletResponse res, Page page)
	{
		int id = Integer.parseInt(req.getParameter("id"));
		Property p = propertyDao.get(id);
		propertyDao.delete(id);

		return "@admin_property_list?cid=" + p.getCatagory().getId();
	}

	@Override
	public String edit(HttpServletRequest req, HttpServletResponse res, Page page)
	{
		int id = Integer.parseInt(req.getParameter("id"));
		Property p = propertyDao.get(id);
		req.setAttribute("p", p);
		return "admin/editProperty.jsp";
	}

	@Override
	public String update(HttpServletRequest req, HttpServletResponse res, Page page)
	{
		String name = req.getParameter("name");
		int id = Integer.parseInt(req.getParameter("id"));
		int cid = Integer.parseInt(req.getParameter("pid"));

		Property p = new Property();
		p.setId(id);
		p.setName(name);
		p.setCatagory(categoryDao.get(cid));

		propertyDao.update(p);

		return "@admin_property_list?cid=" + p.getCatagory().getId();
	}

}
