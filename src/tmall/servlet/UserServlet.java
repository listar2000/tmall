package tmall.servlet;

import tmall.bean.User;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserServlet extends BaseBackServlet {

    public String list(HttpServletRequest req, HttpServletResponse res, Page page)
    {
        List<User> us = userDao.list(page.getStart(), page.getEnd());
        int total = userDao.getTotal();

        page.setTotal(total);

        req.setAttribute("us", us);
        req.setAttribute("page", page);

        return "admin/listUser.jsp";
    }

    @Override
    public String add(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest req, HttpServletResponse res, Page page) {
        return null;
    }

}
