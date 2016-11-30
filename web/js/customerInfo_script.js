$(document).ready(function(){

	if($.browser.msie){
		$(".container").empty();

		msg = "<div class='masthead'><h3 class='muted'>Sprint Tracking Dasboard</h3></div><hr>";
		msg += "<p class='text-error'>This site is compatible with Chrome or Firefox browsers</p>";
		msg += "<br>";
		msg += "<p>Please download Chrome: <a href='https://www.google.com/intl/en/chrome/browser/'>Download</a></p>";
		msg += "<p>Please download Firefox: <a href='http://www.mozilla.org/en-US/firefox/new/'>Download</a></p>";

		$(".container").append(msg);

		return;
	}

$("form").on('submit',function(){

		name = $('#name').val();

		if(name == null || name == ''){
			alert("Please enter customer name");
			$('#name').focus();
			return;
		}
		$('#page_spinner').show();

		$.post("customerInfo", {
				name: name,
			},function(data){

					$('#customer_list').empty();

					if(data.totalCustomers == 0 || data.totalCustomers == '0'){
						msg = 'No Customers found for the selection, please try different name';

						$('#customer_list').append(msg);

					}

					else{
						value = "<p>Total Matches: " +data.totalCustomers +"</p>";
						value += "<table class='table table-condensed table-hover'>";
			            value += "<thead>";
			            value += "<tr Style='background-color:#D8D8D8'>";
			            value += "<th>Customer Name</th>";
			            value += "<th>GUID</th>";
			            value += "<th>&nbsp;</th>";
			            value += "</tr>";
			            value += "</thead>";
			            value += "<tbody>";

			            var i = 1;

						$.each(data.customers, function(k,v){
								value += "<tr>";
								value += "<td>" + String(v.name) +"</td>";
								value += "<td>" +String(v.guid) +"</td>";

								value += "<td>"+"<input type='hidden' id='name_" +String(i) +"' value='" +String(v.name)+"'>" +
								"<input type='hidden' id='guid_" +String(i) +"' value='" +String(v.guid)+"'>" +
								"</td>";

								value += "</tr>";

								i++;
						});

						value += "</tbody>";
						value += "</table>";

					}

					$('#customer_list').append(value);

					$('#page_spinner').hide();

				},"json");

			//return false prevents form from reloading/refreshing/going to other page
			return false;
		});
});










