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

<h1 style="text-align: center;"><i class="icon-magic"></i> Create New Appliance</h1>
<br>

<hr>
<div id="createDrive">
    <form id="myForm" action="uploadServlet12" class="form-horizontal" method="POST">
        <div class="control-group">
            <label class="control-label" for="appliance">Appliance</label>
            <div class="controls">
                <input type="text" required pattern="^([0-9]{1,3}\.){3}[0-9]{1,3}$" id="appliance"
                       title="Must be a valid IP address." placeholder="New Appliance" required>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="current">Current</label>
            <div class="controls">
                <input type="text" id="current" placeholder="Current">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="previous">Previous</label>
            <div class="controls">
                <input type="text" id="previous" title="Previous HERE" placeholder="Previous">
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

<script src="js/createApplianceAssignment_script.js"></script>
</body>
</html>
