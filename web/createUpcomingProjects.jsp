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

<h1 style="text-align: center;"><i class="icon-magic"></i> Create Upcoming Project</h1>
<br>

<hr>
<div id="createDrive">
    <form id="myForm" action="createUpcomingProject" class="form-horizontal" method="POST">
        <div class="control-group">
            <label class="control-label" for="customer_name">Customer Name</label>
            <div class="controls">
                <input type="text" required pattern="^[^']*$" id="customer_name" title="Customer Name"
                       placeholder="Customer Name" required>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="estimated_size">Estimated Size</label>
            <div class="controls">
                <input type="number" min="0" step="1" id="estimated_size" title="Previous HERE" placeholder="Data size"
                       required>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="jira">JIRA</label>
            <div class="controls">
                <input type="text" id="jira" title="Previous HERE" placeholder="JIRA">
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
            <label class="control-label" for="tem">TEM</label>
            <div class="controls">
                <select id="tem">
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="notes">Notes</label>
            <div class="controls">
                <textarea id="notes"></textarea>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="expected_start_month">Expected Start Date</label>
            <div class="controls">
                <input type="text" required id="expected_start_month" title="Start Date">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="expected_end_month">Expected End Date</label>
            <div class="controls">
                <input type="text" required id="expected_end_month" title="End Date">
            </div>
        </div>

        <%--<div class="control-group">--%>
        <%--<label class="control-label" for="updated_date">Date Updated</label>--%>
        <%--<div class="controls">--%>
        <%--<input type="text" id="updated_date"  title="Previous HERE" placeholder="Previous">--%>
        <%--</div>--%>
        <%--</div>--%>

        <%--<div class="control-group">--%>
        <%--<label class="control-label" for="apps_needed">App(s) Needed</label>--%>
        <%--<div class="controls">--%>
        <%--<textarea id="apps_needed"></textarea>--%>
        <%--</div>--%>
        <%--</div>--%>

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
<script src="js/data_center.js"></script>
<script src="js/tem.js"></script>

<script src="js/createUpcomingProjects_script.js"></script>
</body>
</html>
