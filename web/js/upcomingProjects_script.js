var globalCustomerName = [];
var startMonth = [];
var endMonth = [];
var applianceInUse = [];
var applianceCount;
var used;
google.charts.load('current', {'packages': ['timeline']});
google.charts.setOnLoadCallback(drawChart);
$(document).ready(function () {

    $('#customer_name').focus();
    $("[id$=month]").datepicker({dateFormat: "yy-mm-dd"});

    $('#search_form').on('submit', currentProject);

    $(document).ready(function () {
        $('#search_form').submit();
    });

    $(document).on('click', '#drive_table a', function (e) {
        e.stopPropagation();
    });

    $(document).on('click', '#drive_table tr', function () {
        var id = $(this).attr('id').replace('tr_', '');
        getValuesById(id);

        $('#details_change_customer').html("<a href='createUpcomingProjects.jsp?customer_name=" + customer_name + "&" +
            "estimated_size=" + estimated_size + "&" +
            "jira=" + jira + "&" +
            "update=true" + "'>Click here to use this Drive for other customer</a>");
        $('#details_modal_customer_name').html(customer_name);
        $('#details_modal_sow_created_date').html(formatDate(sow_created_date));
        $('#details_modal_estimated_size').html(estimated_size);
        $('#details_modal_jira').html(jira);
        $('#details_modal_dc').html(dc);
        $('#details_modal_tem').html(tem);
        $('#details_modal_notes').html(notes);
        $('#details_modal_expected_start_month').html(formatDate(expected_start_month));
        $('#details_modal_expected_end_month').html(formatDate(expected_end_month));
        $('#details_modal_updated_date').html(formatDate(updated_date));
        $('#details_modal_apps_needed').html(apps_needed);

        $('#details_modal_spinner').hide();
        $('#detailsModal').modal();
    });

    $(document).on('click', 'button[name="deleteButton"]', function (e) {

        var id = $(this).attr('id').replace('delete_', '');
        var customer_name = $('#customer_name' + id).val();
        var sow_created_date = $('#sow_created_date' + id).val();
        var estimated_size = $('#estimated_size' + id).val();
        var jira = $('#jira' + id).val();
        var dc = $('#dc' + id).val();
        var tem = $('#tem' + id).val();
        var notes = $('#notes' + id).val();
        var expected_start_month = $('#expected_start_month' + id).val();
        var expected_end_month = $('#expected_end_month' + id).val();
        var updated_date = $('#updated_date' + id).val();
        var apps_needed = $('#apps_needed').val();

        bootbox.confirm("Are you sure you want to delete this project?",
            function (result) {
                if (result == true) {
                    $.post("deleteUpcomingProject",
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
                            $('#modal_spinner').hide();

                            if (data.result == 'success') {
                                //alert("Drive successfully deleted");
                                $('#search_form').submit();
                            }
                            else
                                alert("Error: " + data.result);
                        }, 'json');
                } //if ends here
            });//bootbox ends here

        e.stopPropagation();
    });

    //For updating a row(Changing values)
    $(document).on('click', "button[name='updateButton']", function (e) {
        var id = $(this).attr('id').replace('update_', '');
        getValuesById(id);

        $('#change_customer').html("<a href='createUpcomingProjects.jsp?customer_name=" + customer_name + "&" +
            "sow_created_date=" + sow_created_date + "&" +
            "estimated_size=" + estimated_size + "&" +
            "jira=" + jira + "&" +
            "dc=" + dc + "&" +
            "tem=" + tem + "&" +
            "notes=" + notes + "&" +
            "expected_start_month=" + expected_start_month + "&" +
            "expected_end_month=" + expected_end_month + "&" +
            "updated_date=" + updated_date + "&" +
            "apps_needed=" + apps_needed + "&" +
            "update=true" + "'>Click here to use this Drive for other customer</a>");
        $('#modal_customer_name').val(customer_name);
        $('#modal_sow_created_date').val(sow_created_date);
        $('#modal_estimated_size').val(estimated_size);
        $('#modal_jira').val(jira);
        $('#modal_dc').val(dc);
        $('#modal_tem').val(tem);
        $('#modal_expected_start_month').val(expected_start_month);
        $('#modal_expected_end_month').val(expected_end_month);
        $('#modal_updated_date').val(updated_date);
        $('#modal_apps_needed').val(apps_needed);

        $('#modal_spinner').hide();
        $('#updateModal').modal();

        e.stopPropagation();
    });

    $(document).on('click', '#modalUpdateButton', function () {
        var customer_name = $('#modal_customer_name').val();
        var sow_created_date = $('#modal_sow_created_date').val();
        var estimated_size = $('#modal_estimated_size').val();
        var jira = $('#modal_jira').val();
        var dc = $('#modal_dc').val();
        var tem = $('#modal_tem').val();
        var notes = $('#modal_notes').val();
        var expected_start_month = $('#modal_expected_start_month').val();
        var expected_end_month = $('#modal_expected_end_month').val();
        var updated_date = $('#modal_updated_date').val();
        var apps_needed = $('#modal_apps_needed').val();
        var username = $('#username').val();

        if (customer_name == null || customer_name == "") {
            alert("Drive must have project");
            $("#modal_customer_name").focus();

            return;
        }

        $('#modal_spinner').show();

        $.post("updateUpcomingProject",
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
                apps_needed: apps_needed,
            },
            function (data) {
                $('#modal_spinner').hide();

                if (data.result == 'success') {
                    //alert("Drive successfully updated");
                    $('[data-dismiss="modal"]').click();
                    $('#search_form').submit();
                }
                else
                    alert("Error: " + data.result);
            }, 'json');
    });


    //For STARTING a project(Send to current projects)
    $(document).on('click', "button[name='startButton']", function (e) {
        var id = $(this).attr('id').replace('start_', '');
        getValuesById(id);

        $('#start_modal_customer_name').val(customer_name);
        $('#start_modal_jira').val(jira);
        $('#start_modal_dc').val(dc);
        $('#start_modal_data_size').val(estimated_size);
        // $('#start_modal_import_engr').val(import_engr);
        $('#start_modal_tem').val(tem);
        // $('#start_modal_current_stage').val(current_stage);
        $('#start_modal_created_date').val(expected_start_month);
        $('#start_modal_appliance_count').val(apps_needed);
        $('#start_modal_notes').val(notes);

        $('#modal_spinner').hide();
        $('#startModal').modal();

        e.stopPropagation();
    });

    $(document).on('click', '#startModalUpdateButton', function () {
        var customer = $('#start_modal_customer_name').val();
        var jira = $('#start_modal_jira').val();
        var dc = $('#start_modal_dc').val();
        var data_size = $('#start_modal_data_size').val();
        var import_engr = $('#start_modal_import_engr').val();
        var tem = $('#start_modal_tem').val();
        var current_stage = $('#start_modal_current_stage').val();
        var created_date = $('#start_modal_created_date').val();
        var appliance_count = $('#start_modal_appliance_count').val();
        var notes = $('#start_modal_notes').val();
        var username = $('#username').val();

        $('#modal_spinner').show();

        $.post("createCurrentProjects",
            {
                customer: customer,
                jira: jira,
                dc: dc,
                data_size: data_size,
                import_engr: import_engr,
                tem: tem,
                notes: notes,
                current_stage: current_stage,
                created_date: created_date,
                appliance_count: appliance_count,
                notes: notes,

            },
            function (data) {
                $('#modal_spinner').hide();
                if ((used + parseInt(appliance_count)) > applianceCount) {
                    alert("Error: Number of appliance(s) exceeded\n" + "Customer: " + customer_name);
                    $('[data-dismiss="modal"]').click();
                    ReloadPage();
                } else {
                    alert("Success: Please check the current projects.\n" + "Customer: " + customer_name + "\n" + "Please delete the row.");
                    $('[data-dismiss="modal"]').click();
                    ReloadPage();
                }
            }, 'json');
    });

});

function currentProject() {
    var customer_name = $('#customer_name').val();
    var sow_created_date = $('#sow_created_date').val();
    var estimated_size = $('#estimated_size').val();
    var jira = $('#jira').val();
    var dc = $('#dc').val();
    var tem = $('#tem').val();
    var notes = $('#notes').val();
    var expected_start_month = $('#expected_start_month').val();
    var expected_end_month = $('#expected_end_month').val();
    var updated_date = $('#updated_date').val();
    var apps_needed = $('#apps_needed').val();

    $('#page_spinner').show();

    $.post("upcomingProjects",
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
            apps_needed: apps_needed,
        },
        function (data) {
            $('#appliance_list').empty();
            applianceCount = data.applianceCount;
            used = data.totalApplianceInUse

            if (data.totalMatches == 0) {
                var msg = "No drives found for the selection, please try different combination or "
                    + "update=false" + "'>create a drive</a>";

                $('#appliance_list').append(msg);
            }
            else {
                var value = "<p>Total Matches: " + data.totalMatches + "</p>";
                value += "<table id='drive_table' class='table table-condensed table-hover tablesorter'>";
                value += "<thead>";
                value += "<tr style='background-color:#D8D8D8'>";
                value += "<th>Customer Name</th>";
                value += "<th>SOW Created Date</th>";
                value += "<th>Estimated Size</th>";
                value += "<th>JIRA</th>";
                value += "<th>DC</th>";
                value += "<th>TEM</th>";
                value += "<th>NOTES</th>";
                value += "<th>Expected Start Date</th>";
                value += "<th>Expected End Date</th>";
                value += "<th>Date Updated</th>";
                value += "<th>Appliance(s) Needed</th>";
                value += "<th>Start Project</th>";
                value += "<th>Operations</th>";
                value += "</tr>";
                value += "</thead>";
                value += "<tbody>";


                var i = 1;
                $.each(data.drives, function (k, v) {

                    value += "<tr class='detail' id='tr_" + i + "'>";

                    if (v.customer_name == undefined) {
                        value += "<td>" + "Error: " + v.message + "</td>";
                    } else {
                        globalCustomerName.push(v.customer_name) // Adding customer_name to an array for charting
                        value += "<td>" + v.customer_name + "</td>";
                        value += "<td>" + formatDate(v.sow_created_date) + "</td>";
                        value += "<td>" + v.estimated_size + "</td>";
                        value += "<td>" + v.jira + "</td>";
                        value += "<td>" + v.dc + "</td>";
                        value += "<td>" + v.tem + "</td>";
                        value += "<td>" + v.notes + "</td>";
                        value += "<td>" + formatDate(v.expected_start_month) + "</td>";
                        startMonth.push(v.expected_start_month)// Adding start date to an array for charting
                        value += "<td>" + formatDate(v.expected_end_month) + "</td>";
                        endMonth.push(v.expected_end_month) // Adding end date to an array for charting
                        value += "<td>" + formatDate(v.updated_date) + "</td>";
                        applianceInUse.push(v.apps_needed);
                        value += "<td>" + v.apps_needed + "</td>";

                        //Button for sending the row to current projects
                        value += "<td><button name ='startButton' class='btn btn-mini btn-block' id='start_" + i + "' style='color: green'><b>Start</b></i></button>";

                        //Button for updating a row
                        value += "<td><button name ='updateButton' class='btn btn-mini' id='update_" + i + "'><i class='icon-edit'></i></button>";

                        //Button for deleting a row. Only visible to a specified user
                        if ($('#username').val() == "nokada" || $('#username').val() == "nlee" || $('#username').val() == "mihuang")
                            value += "&nbsp;<button name ='deleteButton' class='btn btn-mini btn-danger' id='delete_" + i + "'><i class='icon-trash'></i></button>";

                        value +=
                            "<input type='hidden' id='customer_name" + i + "' value='" + v.customer_name + "'>" +
                            "<input type='hidden' id='sow_created_date" + i + "' value='" + v.sow_created_date + "'>" +
                            "<input type='hidden' id='estimated_size" + i + "' value='" + v.estimated_size + "'>" +
                            "<input type='hidden' id='jira" + i + "' value='" + v.jira + "'>" +
                            "<input type='hidden' id='dc" + i + "' value='" + v.dc + "'>" +
                            "<input type='hidden' id='tem" + i + "' value='" + v.tem + "'>" +
                            "<input type='hidden' id='notes" + i + "' value='" + v.notes + "'>" +
                            "<input type='hidden' id='expected_start_month" + i + "' value='" + v.expected_start_month + "'>" +
                            "<input type='hidden' id='expected_end_month" + i + "' value='" + v.expected_end_month + "'>" +
                            "<input type='hidden' id='updated_date" + i + "' value='" + v.updated_date + "'>" +
                            "<input type='hidden' id='apps_needed" + i + "' value='" + v.apps_needed + "'>" +
                            "</td>";
                    }
                    value += "</tr>";
                    i++;
                });

                value += "</tbody>";
                value += "</table>";

                $('#appliance_list').append(value);
            }
            //now giving sorting options on specific columns
            $('#drive_table').tablesorter(/*{
             headers:{
             14:{sorter:false}
             }
             }*/);

            $('#page_spinner').hide();

        }, "json");

    //return false prevents form from reloading/refreshing/going to other page
    return false;
}


function getValuesById(id) {
    customer_name = $('#customer_name' + id).val();
    sow_created_date = $('#sow_created_date' + id).val();
    estimated_size = $('#estimated_size' + id).val();
    jira = $('#jira' + id).val();
    dc = $('#dc' + id).val();
    tem = $('#tem' + id).val();
    notes = $('#notes' + id).val();
    expected_start_month = $('#expected_start_month' + id).val();
    expected_end_month = $('#expected_end_month' + id).val();
    updated_date = $('#updated_date' + id).val();
    apps_needed = $('#apps_needed' + id).val();
}

// For charting Start

function drawChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Customer');
    data.addColumn('date', 'Start Date');
    data.addColumn('date', 'End Date');
    var startSplit = "";
    var endSplit = "";
    for (var i = 0; i <= globalCustomerName.length - 1; i++) {
        if (startMonth[i] != "" && endMonth[i] != "") {
            startSplit = startMonth[i].split("-");
            endSplit = endMonth[i].split("-");
        }
        data.addRows([
            [globalCustomerName[i] + "(" + applianceInUse[i] + ")", new Date(startSplit[0], startSplit[1] - 1, startSplit[2]), new Date(endSplit[0], endSplit[1] - 1, endSplit[2])],
        ]);
        var options = {
            height: 450,
            timeline: {
                groupByRowLabel: true
            }
        };
        var chart = new google.visualization.Timeline(document.getElementById('chart_div'));
        chart.draw(data, options);
    }
    console.log('Customer Name: ')
    console.log(globalCustomerName.toString())
    console.log('Start Date: ')
    console.log(startMonth.toString())
    console.log('End Date: ')
    console.log(endMonth.toString())
}

// For charting End

//For Reloading the page
function ReloadPage() {
    location.reload();
};

//Formatting date from yyyy-MM-dd to M/dd/yyyy
function formatDate(d) {
    var date = new Date(d);
    return (date.getMonth() + 1) + '/' + (date.getDate()) + '/' + date.getFullYear();
}




