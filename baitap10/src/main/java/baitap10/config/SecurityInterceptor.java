package baitap10.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        // Nếu chưa đăng nhập, chuyển về login
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("/login");
            return false;
        }

        String role = (String) session.getAttribute("role"); // Giả định role được lưu trong session sau khi login
        if (role == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Phân quyền
        if (requestURI.startsWith("/admin/")) {
            if (!"ADMIN".equals(role)) {
                response.sendRedirect("/login?error=access_denied");
                return false;
            }
        } else if (requestURI.startsWith("/user/")) {
            if (!"USER".equals(role)) {
                response.sendRedirect("/login?error=access_denied");
                return false;
            }
        }

        return true;
    }
}