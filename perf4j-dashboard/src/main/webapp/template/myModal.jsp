<%@ page contentType="text/html; charset=utf-8" %>
<!-- Modal -->
<%--触发modal的方式
1. data-toggle='modal' data-target='#myModal'

或者手动方式
1. $("#myModal).modal('show')
2. $("#myModal).modal('hide')
--%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
            </div>
            <div class="modal-body" id="myModalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="modalConfirmMenu">Save changes</button>
            </div>
        </div>
    </div>
</div>
