<%@include file="navbar.jsp" %>
<script>
    function myFunction() {
        document.getElementById("search_form").reset();
    }
</script>
<br>

<%--Search Options--%>
<br>
<h1 style="text-align: center">Appliance Assignment Sheet
    <div>
        <button class="btn btn-default"><a href="createApplianceAssignment.jsp">Add Appliance Assignment</a></button>
    </div>
</h1>
<div id="search_options">
    <%--<h1 style="text-align: center"><i class="icon-search"></i> Search Drive</h1>--%>
    <form class="form-horizontal" id="search_form" style="text-align: center">
        <input placeholder="Appliance" type="text" required pattern="^[^']*$" name="appliance" id="appliance"
               title="No quotes allowed." style="height: inherit">
        <input placeholder="current" type="text" name="current" id="current" style="height: inherit">
        <input placeholder="previous" type="text" name="previous" id="previous" style="height: inherit">
        </select>
        <br>
        <br>
        <button type="submit" class="btn btn-primary" id="search">Search</button>
        <button type="submit" class="btn btn-danger btn-md" onclick="myFunction()" value="Reset form">Reset</button>
    </form>
</div>

<div id="search_options">
    <%--Search Options--%>
</div>

<hr>
<!-- Print and export to pdf -->
<input type="button" class="btn btn-default" value="Print this page" onClick="window.print()">

<br>
<br>

<div id="page_spinner" hidden>
    <center><i class="icon-spinner icon-spin icon-3x"></i></center>
</div>

<div id="appliance_list" style="overflow: scroll; height: 800px">
</div>


<!-- Start of udpate modal -->
<div id="updateModal" class="modal hide fade in" style="display: none;">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">?</a>
        <h3>Update Drive</h3>
        <div id="change_customer"></div>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <label class="control-label" for="modal_appliance">Appliance</label>
                <div class="controls">
                    <input type="text" id="modal_appliance" value="" readonly>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_current">Current</label>
                <div class="controls">
                    <input type="text" id="modal_current" value="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="modal_previous">Previous</label>
                <div class="controls">
                    <input type="text" id="modal_previous" value="">
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
<!-- end of update modal -->


<!-- Start of details modal -->
<div id="detailsModal" class="modal hide fade in" style="display: none;">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">?</a>
        <h3>Drive Details</h3>
        <div id="details_change_customer"></div>
    </div>

    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <label class="control-label" for="details_modal_appliance">Appliance</label>
                <div class="controls">
                    <p id="details_modal_appliance"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_current">Current</label>
                <div class="controls">
                    <p id="details_modal_current"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="details_modal_previous">Previous</label>
                <div class="controls">
                    <p id="details_modal_previous"></p>
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

<script src="js/applianceAssignment_script.js"></script>
</body>
</html>
