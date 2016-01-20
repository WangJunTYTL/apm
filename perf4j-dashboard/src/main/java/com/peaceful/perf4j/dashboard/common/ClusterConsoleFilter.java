package com.peaceful.perf4j.dashboard.common;

import com.alibaba.fastjson.JSON;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/25
 * @since 1.6
 */

public class ClusterConsoleFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String currentCluster = request.getParameter("currentCluster");
        if (currentCluster == null || currentCluster.equals("")) {
            Set<String> keys = Conf.getConf().clusterMap.keySet();
            for (String key : keys) {
                currentCluster = key;
                break;
            }
        }
        request.setAttribute("clusterMap", JSON.toJSONString(Conf.getConf().clusterMap));
        request.setAttribute("currentCluster", currentCluster);
        String refresh = request.getParameter("refresh");
        if (refresh == null || refresh.equals("")) refresh = "30";
        request.setAttribute("refresh", Integer.valueOf(refresh));
        Map<String, String> clusterMap = Conf.getConf().clusterMap;
        TaskConsoleAPI.setAdd(clusterMap.get(currentCluster));
        filterChain.doFilter(request, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
