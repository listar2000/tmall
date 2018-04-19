package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductServlet extends BaseBackServlet
{
    @Override
    public String add(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        String name = req.getParameter("name");
        String subTitle = req.getParameter("subTitle");
        float originalPrice = Float.parseFloat(req.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(req.getParameter("promotePrice"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        int cid = Integer.parseInt(req.getParameter("cid"));

        Category c = categoryDao.get(cid);
        Product p = new Product();

        p.setCategory(c);
        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOriginalPrice(originalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);

        productDao.add(p);

        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int id = Integer.parseInt(req.getParameter("id"));
        Product p = productDao.get(id);
        productDao.delete(id);

        return "@admin_product_list?cid =" + p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int id = Integer.parseInt(req.getParameter("id"));
        Product p = productDao.get(id);

        req.setAttribute("p", p);

        return "admin/editProduct.jsp";
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int id = Integer.parseInt(req.getParameter("id"));
        int cid = Integer.parseInt(req.getParameter("cid"));

        Product p = productDao.get(id);

        p.setStock(Integer.parseInt(req.getParameter("stock")));
        p.setPromotePrice(Float.parseFloat(req.getParameter("promotePrice")));
        p.setOriginalPrice(Float.parseFloat(req.getParameter("originalPrice")));
        p.setName(req.getParameter("name"));
        p.setSubTitle(req.getParameter("subTitle"));
        p.setCategory(categoryDao.get(cid));

        productDao.update(p);

        return "@admin_product_list?cid=" + p.getCategory().getId();
    }

    public String list(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        int cid = Integer.parseInt(req.getParameter("cid"));
        Category c = categoryDao.get(cid);

        List<Product> ps = productDao.list(cid, page.getStart(), page.getEnd());
        int total = productDao.getTotal(cid);

        page.setTotal(total);
        page.setParam("&cid="+cid);

        req.setAttribute("c",c);
        req.setAttribute("ps",ps);
        req.setAttribute("page",page);

        return "admin/listProduct.jsp";
    }
}
