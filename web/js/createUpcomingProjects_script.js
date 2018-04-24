$(document).ready(function () {

    $('#customer').focus();


    //For Date Picker START
    $('#expected_start_month').datepicker({dateFormat: "yy-mm-dd"});
    $('#expected_end_month').datepicker({dateFormat: "yy-mm-dd"});
    //For Date Picker END

    $.urlParam = function (name) {
        return getFromURL(name);
    }


    $("form").on('submit', function () {
        customer_name = $('#customer_name').val();
        sow_created_date = $('#sow_created_date').val();
        estimated_size = $('#estimated_size').val();
        jira = $('#jira').val();
        dc = $('#dc').val();
        tem = $('#tem').val();
        notes = $('#notes').val();
        expected_start_month = $('#expected_start_month').val();
        expected_end_month = $('#expected_end_month').val();
        updated_date = $('#update_date').val();
        apps_needed = $('#apps_needed').val();

        $('#spinner').show();

        $.post("createUpcomingProjects",
            {
                customer_name: customer_name,
                sow_created_date: sow_created_date,
                estimated_size: estimated_size,
                jira: jira,
                dc: dc,
                tem: tem,
                notes: notes,
                expected_start_month: expected_start_month,
                expected_end_month: expected_end_month,
                updated_date: updated_date,
                apps_needed: apps_needed

            },
            function (data) {
                $('#spinner').hide();
                $('#createUpcomingProjects').hide();
                $('#result').empty();

                if (data.customer_name == undefined) {
                    result = '<p><h3>Error:</h3>'
                        + '<br>' + data.message
                        + '</p>';
                }

                else {
                    result = "<p><h3>Upcoming project created in system with following details:</h3>"
                        + "<br>Customer name: " + data.customer_name
                        + "<br>Created date: " + data.sow_created_date
                        + "<br>Estimated size: " + data.estimated_size
                        + "<br>Jira: " + data.jira
                        + "<br>DC: " + data.dc
                        + "<br>TEM: " + data.tem
                        + "<br>Notes: " + data.notes
                        + "<br>Expected start date: " + data.expected_start_month
                        + "<br>Expected end date: " + data.expected_end_month
                        //+ "<br>Last Updated: " + data.updated_date
                        + "<br>Appliance(s) needed: " + data.apps_needed
                }

                $('#result').append(result);
            }, "json");

        //return false prevents form from reloading/refreshing/going to other page
        return false;
    });
});