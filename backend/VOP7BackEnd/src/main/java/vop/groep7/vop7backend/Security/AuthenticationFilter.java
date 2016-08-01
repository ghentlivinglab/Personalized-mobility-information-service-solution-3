package vop.groep7.vop7backend.Security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Controllers.Processor;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;

/**
 *
 * @author Backend Team
 */
@CrossOrigin
@Component
public class AuthenticationFilter implements Filter {

    private static final String SECURITY_TOKEN_HEADER = "X-Token";
    private static final String ROLE_ALL = "ALL";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_OPERATOR = "ROLE_OPERATOR";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String FORGOT_PASSWORD = "/user/forgot_password.*";
    private static final String VERIFY = "/user/verify.*";
    private static final String REFRESH_REGULAR = "/refresh_token/regular/?";
    private static final String REFRESH_FACEBOOK = "/refresh_token/facebook/?";
    private static final String ACCESS_TOKEN = "/access_token/?";
    private static final String USER = "/user/?";
    private static final String TRANSPORT = "/transportationtype/?";
    private static final String ROLE = "/role/?";
    private static final String EVENTTYPE = "/eventtype/?";
    private static final String LOGOUT = "/refresh_token/logout/?";
    private static final String ALL_EVENT = "/event.*";
    private static final String ALL_ADMIN = "/admin.*";
    private static final String ALL_USER = "/user/.*";

    private Map<String, ArrayList<String>> urls;

    @Autowired
    private TokenManager tokenManager;

    private UserDAO userDAO;

    public AuthenticationFilter() {
        createUrls();
    }

    private synchronized UserDAO getUserDAO() {
        if (userDAO == null) {
            try {
                userDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getUserDAO();
            } catch (DataAccessException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, "Could not get userDAO", ex);
            }
        }
        return userDAO;
    }

    private void createUrls() {
        urls = new HashMap<>();

        //ALL
        ArrayList<String> allList = new ArrayList<>();

        //Only POST allowed
        allList.add(REFRESH_REGULAR);
        allList.add(REFRESH_FACEBOOK);
        allList.add(ACCESS_TOKEN);
        allList.add(USER);
        allList.add(FORGOT_PASSWORD);
        allList.add(VERIFY);

        urls.put(ROLE_ALL, allList);

        //USER
        ArrayList<String> userList = new ArrayList<>();

        userList.add(TRANSPORT);
        userList.add(ROLE);
        userList.add(EVENTTYPE);
        userList.add(LOGOUT);
        userList.add(VERIFY);

        urls.put(ROLE_USER, userList);

        //OPERATOR   
        ArrayList<String> operaterList = new ArrayList<>();

        operaterList.add(TRANSPORT);
        operaterList.add(ROLE);
        operaterList.add(EVENTTYPE);
        operaterList.add(USER);
        operaterList.add(ALL_EVENT);
        operaterList.add(LOGOUT);
        operaterList.add(VERIFY);

        urls.put(ROLE_OPERATOR, operaterList);

        //ADMIN
        ArrayList<String> adminList = new ArrayList<>();

        adminList.add(TRANSPORT);
        adminList.add(ROLE);
        adminList.add(EVENTTYPE);
        adminList.add(ALL_EVENT);
        adminList.add(USER);
        adminList.add(ALL_ADMIN);
        adminList.add(LOGOUT);
        adminList.add(VERIFY);

        urls.put(ROLE_ADMIN, adminList);
    }

    private boolean matchesInList(ArrayList<String> list, String url) {
        return list.stream().anyMatch((s) -> (url.matches(s)));
    }

    private ArrayList<String> getAllowedUrls(HttpServletRequest request) throws DataAccessException {
        String token = request.getHeader(SECURITY_TOKEN_HEADER);
        String method = request.getMethod();

        if (token == null || !tokenManager.validateAccessToken(token)) {
            if (method.equalsIgnoreCase("POST")) {
                return (ArrayList<String>) urls.get(ROLE_ALL).clone();
            } else {
                return new ArrayList<>();
            }
        }

        String[] parts = token.split(":");
        Credentials c = getUserDAO().getCredentials(Integer.valueOf(parts[1]));
        String role = c.getRole();

        ArrayList<String> allowed = (ArrayList<String>) urls.get(role).clone();
        allowed.add("/user/" + c.getUserIdentifier() + ".*");

        // events for user url allowed for all roles
        if (method.equalsIgnoreCase("GET") && request.getRequestURI().matches("/event/") && Objects.equals(request.getParameter("user_id"), c.getUserIdentifier() + "")) {
            allowed.add(ALL_EVENT);
        }

        // delete user url allowed for admins
        if (role.equalsIgnoreCase(ROLE_ADMIN) && method.equalsIgnoreCase("DELETE") && request.getRequestURI().matches(ALL_USER)) {
            allowed.add(ALL_USER);
        }

        return allowed;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getRequestURI().matches("/monitoring.*")) {
            chain.doFilter(request, response);

            return;
        }
        try {
            ArrayList<String> allowed = getAllowedUrls(request);
            if (matchesInList(allowed, request.getRequestURI())) {
                chain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You have no access to this url or you are not logged in.");
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, "Could not filter request.", ex);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
