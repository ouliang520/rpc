package com.ouliang.protocol;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        new HttpServerHandler().handler(servletRequest, servletResponse);
    }

}
