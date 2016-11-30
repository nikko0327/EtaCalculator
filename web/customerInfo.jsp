<%--
  Created by IntelliJ IDEA.
  User: nlee
  Date: 8/9/16
  Time: 9:47 AM
  To change this template use File | Settings | File Templates.
--%>

<%@include file="navbar.jsp" %>

		<br>
		
		<div id="page_spinner" hidden>
		  		<center><i class="icon-spinner icon-spin icon-3x"></i></center>
		</div>
		
		<form class="form-horizontal">
		    <input placeholder = "Customer Name" type="text" name="NAME" id= "name">
		    <button type="submit" class="btn btn-primary" id="search">Search</button>
        </form>

		<div id="customer_list">
			&nbsp;
		</div>

		<input id="username" value='<%= session.getAttribute("username") %>' hidden>
		<input id="email" value='<%= session.getAttribute("mail") %>' hidden>

		<hr>
		<div class="footer">
			<p>&copy; proofpoint 2016</p>
		</div>
	</div>
	<!-- /container -->

	<script src="js/jquery.js"></script>
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

	<script src="js/customerInfo_script.js"></script>
</body>
</html>
