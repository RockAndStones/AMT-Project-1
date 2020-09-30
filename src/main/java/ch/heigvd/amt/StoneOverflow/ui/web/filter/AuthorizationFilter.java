package ch.heigvd.amt.StoneOverflow.ui.web.filter;

import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.AuthenticatedUserDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/*")
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        //Get path without request context path
        String reqPath = req.getRequestURI().substring(req.getContextPath().length());

        //Allow access to public resources
        if (isPublicResource(reqPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //Read DTO from session attribute
        AuthenticatedUserDTO authenticatedUser = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            String targetReq = reqPath;
            if (req.getQueryString() != null)
                targetReq += "?" + req.getQueryString();

            req.getSession().setAttribute("targetReq", targetReq);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        //User is authenticated and resource is not public
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isPublicResource(String path) {
        if (path.startsWith("/assets"))
            return true;
        else if (path.startsWith("/login"))
            return true;
        else if (path.startsWith("/register"))
            return true;
        else if (path.startsWith("/home"))
            return true;
        else if (path.startsWith("/favicon.ico"))
            return true;

        return false;
    }
}
