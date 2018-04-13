<%--
  Created by IntelliJ IDEA.
  User: nlee
  Date: 8/9/16
  Time: 9:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="navbar.jsp" %>


<style>
    .control-label {
        font-weight: bold;
    }
</style>

<script>
    function myFunction() {
        document.getElementById("myForm").reset();
    }
</script>

<br>

<h1 style="text-align: center;"><i class="icon-magic"></i> Create Current Project</h1>
<br>

<hr>
<div id="createDrive">
    <form id="myForm" action="uploadServletCurrent" class="form-horizontal" method="POST">
        <div class="control-group">
            <label class="control-label" for="customer">Customer</label>
            <div class="controls">
                <input type="text" required pattern="^[^']*$" id="customer" title="Appliance HERE" placeholder="New Appliance" required>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="jira">JIRA</label>
            <div class="controls">
                <input type="text" id="jira" placeholder="Current">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="dc">DC</label>
            <div class="controls">
                <select id="dc">
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="data_size">Data Size</label>
            <div class="controls">
                <input type="number" id="data_size" title="Previous HERE" placeholder="Data Size" required>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="import_engr">Import Engineer</label>
            <div class="controls">
                <select id="import_engr">
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="tem">TEM</label>
            <div class="controls">
                <select id="tem">
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="current_stage">Current Stage</label>
            <div class="controls">
                <select id="current_stage">
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="created_date">Created Date</label>
            <div class="controls">
                <input type="text" id="created_date" title="Previous HERE" placeholder="Created Date">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="tem">Appliance Count</label>
            <div class="controls">
                <input type="number" id="appliance_count" title="Previous HERE" placeholder="Number Of Appliance(s)"
                       required>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="notes">Notes</label>
            <div class="controls">
                <textarea id="notes"></textarea>
            </div>
        </div>

        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary btn-lg" id="create">Create</button>
                <button type="submit" class="btn btn-danger btn-lg" onclick="myFunction()" value="Reset form">Reset
                </button>
            </div>
        </div>

        <div id="spinner" hidden>
            <center><i class="icon-spinner icon-spin icon-3x"></i></center>
        </div>

        <p id="error-message" class="text-error" hidden><b>Missing Value/s</b></p>
    </form>
</div>

<div id="page_spinner" hidden>
    <center><i class="icon-spinner icon-spin icon-3x"></i></center>
</div>

<div id="result">
</div>

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
<script src="js/bootbox.js"></script>
<script src="js/methods.js"></script>
<script src="js/import_engr.js"></script>
<script src="js/tem.js"></script>
<script src="js/current_stage.js"></script>
<script src="js/data_center.js"></script>

<script src="js/createCurrentProjects_script.js"></script>
</body>
</html>
