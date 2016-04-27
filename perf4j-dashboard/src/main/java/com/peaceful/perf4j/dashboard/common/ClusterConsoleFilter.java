package com.peaceful.perf4j.dashboard.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.peaceful.perf4j.dashboard.controller.ConfController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/25
 * @since 1.6
 */

public class ClusterConsoleFilter implements Filter {


    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterConsoleFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String currentCluster = request.getParameter("currentCluster");
        String method = request.getParameter("method");
        if (StringUtils.isNotBlank(method) && StringUtils.isBlank(currentCluster)) {
            response.sendRedirect("/");
        } else {
            if (StringUtils.isNotBlank(currentCluster)) {
                request.setAttribute("currentCluster",currentCluster);
                TaskConsoleAPI.setUrl(Conf.NODES.get(currentCluster));
            }
            filterChain.doFilter(request, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
