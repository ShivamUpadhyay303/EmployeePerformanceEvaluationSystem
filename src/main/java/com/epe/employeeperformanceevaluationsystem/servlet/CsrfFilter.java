
package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.util.CsrfUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class CsrfFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization required.
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String method = httpRequest.getMethod();

        // Create a token when a session is first accessed through GET.
        if ("GET".equalsIgnoreCase(method)) {

            HttpSession session = httpRequest.getSession(true);

            CsrfUtil.getOrCreateToken(session);

            chain.doFilter(request, response);
            return;
        }

        // Validate every POST request before it reaches a Servlet.
        if ("POST".equalsIgnoreCase(method)) {

            HttpSession session = httpRequest.getSession(false);

            String submittedToken =
                    httpRequest.getParameter("csrfToken");

            if (!CsrfUtil.isValidToken(session, submittedToken)) {

                httpResponse.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid or missing security token. Please refresh the page and try again."
                );

                return;
            }

            chain.doFilter(request, response);
            return;
        }

        // Other HTTP methods are passed through.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup required.
    }
}
