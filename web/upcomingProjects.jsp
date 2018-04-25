<%--
  Created by IntelliJ IDEA.
  User: nlee
  Date: 8/9/16
  Time: 9:47 AM
  To change this template use File | Settings | File Templates.
--%>

<%@include file="navbar.jsp" %>
<br>

<div id="search_options">
    <h1>Upcoming Project Sheet</h1>
    <div>
        <button class="btn btn-default"><a href="createUpcomingProjects.jsp">Add Upcoming Project</a></button>
    </div>
    <br>
    <form class="form-horizontal" id="search_form">

    </form>
</div>

<hr>
<!-- Print and export to pdf -->
<input type="button" class="btn btn-default" value="Print this page" onClick="window.print()">
<br>
<br>

<div id="page_spinner" hidden>
    <center><i class="icon-spinner icon-spin icon-3x"></i></center>
</div>


<div class="container">
    <%--Added for charting Start--%>
    <div id="chart_div"></div>
    <%--Added for charting End--%>
</div>

<div id="appliance_list" style="overflow: scroll; height: 800px">
</div>


<!-- Start of update modal -->
<div id="updateModal" class="modal hide fade in" style="display: none;">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">?</a>
        <h3>Update Upcoming Project</h3>
        <div id="change_customer"></div>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">

            <div class="control-group">
                <label class="control-label" for="modal_customer_name">Customer Name</label>
                <div class="controls">
                    <input type="text" id="modal_customer_name" value="" readonly>
                </div>
            </div>


            <div class="control-group">
                <label class="control-label" for="modal_estimated_size">Estimate Size</label>
                <div class="controls">
                    <input type="text" id="modal_estimated_size" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_jira">JIRA</label>
                <div class="controls">
                    <input type="text" id="modal_jira" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_dc">DC</label>
                <div class="controls">
                    <select id="modal_dc">
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_tem">TEM</label>
                <div class="controls">
                    <select id="modal_tem">
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_notes">Notes</label>
                <div class="controls">
                    <input type="text" id="modal_notes" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_expected_start_month">Expected Start Month</label>
                <div class="controls">
                    <input type="text" id="modal_expected_start_month" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_expected_end_month">Expected End Month</label>
                <div class="controls">
                    <input type="text" id="modal_expected_end_month" value="">
                </div>
            </div>

            <div id="modal_spinner" hidden>
                <center><i class="icon-spinner icon-spin icon-3x"></i></center>
            </div>
        </form>
    </div>

    <div class="modal-footer">
        <a href="#" class="btn btn-success" id="modalUpdateButton">Update</a>
        <a href="#" class="btn" data-dismiss="modal">Close</a>
    </div>
</div>
<!-- end of udpate modal -->


<!-- Start of details modal -->
<div id="detailsModal" class="modal hide fade in" style="display: none;">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">?</a>
        <h3>Upcoming project details</h3>
        <div id="details_change_customer"></div>
    </div>

    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <label class="control-label" for="details_modal_customer_name">Customer Name</label>
                <div class="controls">
                    <p id="details_modal_customer_name"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_sow_created_date">Created Date</label>
                <div class="controls">
                    <p id="details_modal_sow_created_date"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_estimated_size">Estimated Size</label>
                <div class="controls">
                    <p id="details_modal_estimated_size"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_jira">JIRA</label>
                <div class="controls">
                    <p id="details_modal_jira"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_dc">DC</label>
                <div class="controls">
                    <p id="details_modal_dc"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_tem">TEM</label>
                <div class="controls">
                    <p id="details_modal_tem"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_notes">Notes</label>
                <div class="controls">
                    <p id="details_modal_notes"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_expected_start_month">Expected Start Date</label>
                <div class="controls">
                    <p id="details_modal_expected_start_month"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_expected_end_month">Expected End Date</label>
                <div class="controls">
                    <p id="details_modal_expected_end_month"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_updated_date">Updated Date</label>
                <div class="controls">
                    <p id="details_modal_updated_date"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_apps_needed">Appliances Needed</label>
                <div class="controls">
                    <p id="details_modal_apps_needed"></p>
                </div>
            </div>

            <div id="details_modal_spinner" hidden>
                <center><i class="icon-spinner icon-spin icon-3x"></i></center>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Close</a>
    </div>
</div>
<!-- end of details modal -->


<!-- Start of start modal -->
<div id="startModal" class="modal hide fade in" style="display: none;">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">?</a>
        <h3>Start Project</h3>
        <div id="start_change_customer"></div>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">

            <div class="control-group">
                <label class="control-label" for="start_modal_customer_name">Customer name</label>
                <div class="controls">
                    <input type="text" id="start_modal_customer_name" value="" readonly>
                </div>
            </div>


            <div class="control-group">
                <label class="control-label" for="start_modal_jira">JIRA</label>
                <div class="controls">
                    <input type="text" id="start_modal_jira" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_dc">DC</label>
                <div class="controls">
                    <input type="text" id="start_modal_dc" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_data_size">Data size</label>
                <div class="controls">
                    <input type="text" id="start_modal_data_size" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_import_engr">Import engineer</label>
                <div class="controls">
                    <select id="start_modal_import_engr">
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_tem">TEM</label>
                <div class="controls">
                    <select id="start_modal_tem">
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_current_stage">Current stage</label>
                <div class="controls">
                    <select id="start_modal_current_stage">
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_created_date">Created date</label>
                <div class="controls">
                    <input type="text" id="start_modal_created_date" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="start_modal_appliance_count">Appliances needed</label>
                <div class="controls">
                    <input type="text" id="start_modal_appliance_count" value="">
                </div>
            </div>


            <div class="control-group">
                <label class="control-label" for="start_modal_notes">Notes</label>
                <div class="controls">
                    <input type="text" id="start_modal_notes" value="">
                </div>
            </div>

            <div id="start_modal_spinner" hidden>
                <center><i class="icon-spinner icon-spin icon-3x"></i></center>
            </div>
        </form>
    </div>

    <div class="modal-footer">
        <a href="#" class="btn btn-success" id="startModalUpdateButton">Start</a>
        <a href="#" class="btn" data-dismiss="modal">Close</a>
    </div>
</div>
<!-- end of start modal -->


<input id="username" value='<%= userName %>' hidden>
<hr>

<%@include file="footer.jsp" %>
</div>
<!-- /container -->

<script src="js/jquery.js"></script>
<script src="js/jquery-ui-1.8.13.custom.min.js"></script>
<script src="js/bootstrap-transition.js"></script>
<script src="js/bootstrap-alert.js"></script>
<script src="js/bootstrap-modal.js"></script>
<script src="js/bootstrap-dropdown.js"></script>
<script src="js/bootstrap-scrollspy.js"></script>
<script src="js/bootstrap-tab.js"></script>
<script src="js/bootstrap-tooltip.js"></script>
<script src="js/bootstrap-popover.js"></script>
<script src="js/bootstrap-button.js"></script>
<script src="js/bootstrap-collapse.js"></script>
<script src="js/bootstrap-carousel.js"></script>
<script src="js/bootstrap-typeahead.js"></script>
<script src="js/jquery.tablesorter.js"></script>
<script src="js/bootbox.js"></script>
<script src="js/data_center.js"></script>
<script src="js/current_stage.js"></script>
<script src="js/import_engr.js"></script>
<script src="js/tem.js"></script>

<%--Script for charting--%>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script src="js/upcomingProjects_script.js"></script>
</body>
</html>
