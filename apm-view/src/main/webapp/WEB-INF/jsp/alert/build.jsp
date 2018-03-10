<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="/template/01/pageHeader.jsp"></jsp:include>


<div class="container starter-left">
    <!-- /.row -->
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <form role="form">
                    <div class="box-body">
                        <div class="form-group">
                            <label for="service">Service</label>
                            <select class="form-control" id="service">
                                <c:forEach items="${serviceList}" var="service">
                                    <option value="${service}">${service}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="tag">Tag</label>
                            <select class="form-control" id="tag">
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="term">Term Expression
                                <small>
                                    [variable:service、tag、count、tps、mean、max、min、interval、datetime]
                                </small>
                            </label>
                            <textarea class="form-control" rows="3" id="term"
                                      placeholder="example:(count>100 && mean >1000) || (count == 0)"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="interval">Interval</label>
                            <select class="form-control" id="interval">
                                <option value="1">1min</option>
                                <option value="5">5min</option>
                                <option value="30">30min</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="smsMsg">Sms Message</label>
                            <textarea class="form-control" rows="3" id="smsMsg"
                                      placeholder="example:Current Invoke Count:\${count}"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="mailMsg">Mail Message
                                <small>[html format]</small>
                            </label>
                            <input id="subject" class="form-control" placeholder="subject">
                            <textarea class="form-control" rows="3" id="mailMsg"
                                      placeholder="example:Current Invoke Count:\${count}"></textarea>
                        </div>
                        <div class="form-group">
                            <label>Reveiver Groups</label>
                            <c:forEach items="${userGroups}" var="group">
                                <label class="checkbox-inline">
                                    <input type="checkbox" value="${group.key}"> ${group.value}
                                </label>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="box-footer">
                        <button type="button" class="btn btn-success" id="submit">Submit</button>
                    </div>
                </form>
            </div>
            <!-- /.box -->
        </div>
    </div>
</div>
<script src="/js/page/build.js"></script>
<jsp:include page="/template/01/pageFooter.jsp"></jsp:include>

