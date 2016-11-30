var globalCustomerName = [];
var startMonth = [];
var endMonth = [];
var applianceInUse =[];
var allDates = [];
var allDatesToStringSplit; //Get all the dates and split to ("-");
google.charts.load('current', {'packages':['timeline']});
google.charts.setOnLoadCallback(drawChart);

$(document).ready(function() {

    $('#customer').focus();
    $("[id$=date]").datepicker({ dateFormat: "yy-mm-dd"});
    $('#modal_appliance_date').datepicker({ dateFormat: "yy-mm-dd"}).datepicker('setDate', new Date());

    $('#search_form').on('submit', currentProject);

    $(document).ready(function() {
        $('#search_form').submit();
    });

    $(document).on('click', '#drive_table a', function(e) {
        e.stopPropagation();
    });

    $(document).on('click', '#drive_table tr', function(){
        var id = $(this).attr('id').replace('tr_','');
        var estimate =
        getValuesById(id);
        $('#details_change_customer').html("<a href='createCurrentProjects.jsp?customer=" + customer + "&" +
            "jira=" + jira + "&" +
            "dc=" + dc + "&" +
            "update=true" + "'>Click here to use this Drive for other customer</a>");
        $('#details_modal_customer').html(customer);
        $('#details_modal_jira').html(jira);
        $('#details_modal_dc').html(dc);
        $('#details_modal_data_size').html(data_size);
        $('#details_modal_import_engineer').html(import_engr);
        $('#details_modal_tem').html(tem);
        $('#details_modal_current_stage').html(current_stage);
        $('#details_modal_created_date').html(formatDate(created_date));
        $('#details_modal_notes').html(notes);
        $('#details_modal_appliance_count').html(appliance_count);
        $('#details_modal_expected').html(etaDateGetter());

        $('#details_modal_spinner').hide();
        $('#detailsModal').modal();
    });

    $(document).on('click', 'button[name="deleteButton"]', function(e) {

        var id = $(this).attr('id').replace('delete_','');
        var customer = $('#customer' + id).val();
        var jira = $('#jira' + id).val();
        var dc = $('#dc' + id).val();
        var data_size = $('#data_size' + id).val();
        var import_engr = $('#import_engr' + id).val();
        var tem = $('#tem' + id).val();
        var current_stage = $('#current_stage' + id).val();
        var created_date = $('#created_date' + id).val();
        var notes = $('#notes' + id).val();
        var appliance_count = $('#appliance_count' + id).val();
        var username = $('#username').val();

        bootbox.confirm("Are you sure you want to delete this project?",
            function(result) {
                if(result == true) {
                    $.post("deleteCurrentProject",
                        {
                            customer : customer,
                            jira : jira,
                            dc : dc,
                            data_size : data_size,
                            import_engr : import_engr,
                            tem : tem,
                            current_stage : current_stage,
                            created_date : created_date,
                            notes : notes,
                            appliance_count : appliance_count
                        },
                        function(data) {
                            $('#modal_spinner').hide();

                            if(data.result == 'success') {
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

    $(document).on('click', "button[name='updateButton']", function(e) {
        var id = $(this).attr('id').replace('update_','');
        getValuesById(id);

        $('#change_customer').html("<a href='createCurrentProjects.jsp?customer=" + customer + "&" +
            "jira=" + jira + "&" +
            "dc=" + dc + "&" +
            "data_size=" + data_size + "&" +
            "import_engr=" + import_engr + "&" +
            "tem=" + tem + "&" +
            "current_stage=" + current_stage + "&" +
            "created_date=" + created_date + "&" +
            "notes=" + notes + "&" +
            "appliance_count=" + appliance_count + "&" +
            "is_completed=" + is_completed + "&" +
            "update=true" + "'>Click here to use this Drive for other customer</a>");
        $('#modal_customer').val(customer);
        $('#modal_jira').val(jira);
        $('#modal_dc').val(dc);
        $('#modal_data_size').val(data_size);
        $('#modal_import_engr').val(import_engr);
        $('#modal_tem').val(tem);
        $('#modal_current_stage').val(current_stage);
        $('#modal_created_date').val(created_date);
        $('#modal_notes').val(notes);
        $('#modal_appliance_count').val(appliance_count);
        $('#modal_is_completed').val(is_completed);
        $('#modal_spinner').hide();
        $('#updateModal').modal();

        e.stopPropagation();
    });

    $(document).on('click', '#modalUpdateButton', function() {
        var customer = $('#modal_customer').val();
        var jira = $('#modal_jira').val();
        var dc = $('#modal_dc').val();
        var data_size = $('#modal_data_size').val();
        var import_engr = $('#modal_import_engr').val();
        var tem = $('#modal_tem').val();
        var current_stage = $('#modal_current_stage').val();
        var created_date = $('#modal_created_date').val();
        var notes = $('#modal_notes').val();
        // var username = $('#username').val();
        var appliance_count = $('#modal_appliance_count').val();
        var is_completed = $('#modal_is_completed').val();

        if(customer == null || customer == "") {
            alert("Drive must have project");
            $("#modal_customer").focus();

            return;
        }

        $('#modal_spinner').show();

        $.post("updateCurrentProject",
            {
                customer : customer,
                jira : jira,
                dc : dc,
                data_size : data_size,
                import_engr : import_engr,
                tem : tem,
                current_stage : current_stage,
                created_date : created_date,
                notes : notes,
                appliance_count : appliance_count,
                is_completed : is_completed

            },
            function(data) {
                $('#modal_spinner').hide();

                if(data.result == 'success') {
                    //alert("Drive successfully updated");
                    $('[data-dismiss="modal"]').click();
                    $('#search_form').submit();
                }
                else
                    alert("Error: " + data.result);
            }, 'json');
    });
});

function currentProject() {
    var customer = $('#customer').val();
    var jira = $('#jira').val();
    var dc = $('#dc').val();
    var data_size = $('#data_size').val();
    var import_engr = $('#import_engr').val();
    var tem = $('#tem').val();
    var current_stage = $('#current_stage').val();
    var created_date = $('#created_date').val();
    var notes = $('#notes').val();
    var appliance_count = $('#appliance_count').val();
    var is_completed = $('#is_completed').val();


    $('#page_spinner').show();

    $.post("currentProjects",
        {
            customer : customer,
            jira : jira,
            dc : dc,
            data_size : data_size,
            import_engr : import_engr,
            tem : tem,
            current_stage : current_stage,
            notes : notes,
            appliance_count : appliance_count,
            created_date : created_date,
            is_completed : is_completed
        },
        function(data) {
            $('#appliance_list').empty();

            if(data.totalMatches == 0) {
                var msg = "No drives found for the selection, please try different combination or "
                    + "update=false" + "'>create a drive</a>";

                $('#appliance_list').append(msg);
            }
            else {
                var value = "<p>Total Appliances: " + data.totalMatches +"</p> <p>Total Appliances in Use: " + data.totalApplianceInUse +"</p> <p>Total Appliances Available: " + (data.totalMatches - data.totalApplianceInUse) +"</p>";
                value += "<table id='drive_table' class='table table-condensed table-hover tablesorter'>";
                value += "<thead>";
                value += "<tr style='background-color:#D8D8D8'>";
                value += "<th>Customer</th>";
                value += "<th>JIRA</th>";
                value += "<th>DC</th>";
                value += "<th>Data Size(GB)</th>";
                value += "<th>Import Engineer</th>";
                value += "<th>TEM</th>";
                value += "<th>Current Stage</th>";
                value += "<th>Notes</th>";
                value += "<th>Appliance(s)</th>";
                value += "<th>Date Created</th>";
                value += "<th>Finish Date</th>";
                value += "<th>Completed</th>";
                // value += "<th>Expected Date</th>"; //Show days that will take to finish the process
                value += "<th>Operations</th>";
                value += "</tr>";
                value += "</thead>";
                value += "<tbody>";

                var i = 1;
                $.each(data.drives, function(k,v) {
                    value += "<tr class='detail' id='tr_"+ i + "'>";

                    if(v.customer == undefined) {
                        value += "<td>" + "Error: " + v.message + "</td>";
                    }else {
                        globalCustomerName.push(v.customer);
                        value += "<td>" + v.customer + "</td>";
                        value += "<td>" + v.jira + "</td>";
                        value += "<td>" + v.dc + "</td>";
                        value += "<td>" + v.data_size + "</td>";
                        value += "<td>" + v.import_engr + "</td>";
                        value += "<td>" + v.tem + "</td>";
                        value += "<td>" + v.current_stage + "</td>";
                        value += "<td>" + v.notes + "</td>";
                        applianceInUse.push(v.appliance_count);
                        value += "<td>" + v.appliance_count + "</td>";
                        startMonth.push(v.created_date + "-" + v.appliance_count);
                        value += "<td>" + formatDate(v.created_date) + "</td>";
                        // value += "<td>" + (Math.ceil(v.data_size / (v.appliance_count * 150)) + 14) + "</td>"; //Show days that will take to finish the process
                        endMonth.push(v.closing_date + "-" + v.appliance_count);

                        if(v.daysBetweenTodayAndFinish > 0 && v.daysBetweenTodayAndFinish <= 14){
                            value += "<td style='background-color: yellow; font-weight: bold;'>" + formatDate(v.closing_date) + "</td>";
                        } else if(v.daysBetweenTodayAndFinish < 0){
                            value += "<td style='background-color: red; font-weight: bold;'>" + formatDate(v.closing_date) + "</td>";
                        } else{
                            value += "<td style='color: black; font-weight: bold;'>" + formatDate(v.closing_date) + "</td>";
                        }

                        if((v.is_completed) == 'Yes'){
                            // value += "<td style = 'color: green'>" + "<strong>" + v.return_media_to_customer + "</strong>" + "</td>";
                            value += "<td style = 'color: green; font-size: 120%;'>" + '<i class="fa fa-check">' + '</i>' + 'Yes' + "</td>";
                        } else {
                            // value += "<td style='color: red'>" +  "<strong>" + v.return_media_to_customer + "</strong>" + "</td>";
                            value += "<td style = 'color: red; font-size: 120%;'>" + '<i class="fa fa-times">' + '</i>' + 'No' + "</td>";
                        }

                        value += "<td><button name ='updateButton' class='btn btn-mini' id='update_" + i + "'><i class='icon-edit'></i></button>";

                        if($('#username').val() == "nokada" || $('#username').val() == "nlee")
                            value += "&nbsp;<button name ='deleteButton' class='btn btn-mini btn-danger' id='delete_" + i +"'><i class='icon-trash'></i></button>";
                        value +=
                            "<input type='hidden' id='customer" + i + "' value='" + v.customer + "'>" +
                            "<input type='hidden' id='jira" + i + "' value='" + v.jira +"'>" +
                            "<input type='hidden' id='dc" + i + "' value='" + v.dc + "'>" +
                            "<input type='hidden' id='data_size" + i + "' value='" + v.data_size + "'>" +
                            "<input type='hidden' id='import_engr" + i + "' value='" + v.import_engr +"'>" +
                            "<input type='hidden' id='tem" + i + "' value='" + v.tem + "'>" +
                            "<input type='hidden' id='current_stage" + i + "' value='" + v.current_stage +"'>" +
                            "<input type='hidden' id='notes" + i + "' value='" + v.notes + "'>" +
                            "<input type='hidden' id='appliance_count" + i + "' value='" + v.appliance_count + "'>" +
                            "<input type='hidden' id='created_date" + i + "' value='" + v.created_date + "'>" +
                            "<input type='hidden' id='is_completed" + i + "' value='" + v.is_completed + "'>" +

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
    customer = $('#customer' + id).val();
    jira = $('#jira' + id).val();
    dc = $('#dc' + id).val();
    data_size = $('#data_size' + id).val();
    import_engr = $('#import_engr' + id).val();
    tem = $('#tem' + id).val();
    current_stage = $('#current_stage' + id).val();
    notes = $('#notes' + id).val();
    appliance_count = $('#appliance_count' + id).val();
    created_date = $('#created_date' + id).val();
    is_completed = $('#is_completed' + id).val();
    return_ = $('#created_date' + id).val();
}


function etaDateGetter(){
    //DATE ADDER
    var dateSplit = created_date.split("-");
    var date =  new Date(dateSplit[0], dateSplit[1], dateSplit[2]);
    date = new Date(date.setDate(date.getDate() + (Math.ceil(data_size / (appliance_count * 150)) + 14)));
    var finalClosingDate = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate();
    return finalClosingDate;
}


// For charting Start
function drawChart() {
    var data = new google.visualization.DataTable();
    data.addColumn({ type: 'string', id: 'Customer' });
    data.addColumn({ type: 'string', id: 'Number of Apps' });
    data.addColumn({ type: 'date', id: 'Start Date' });
    data.addColumn({ type: 'date', id: 'End Date' });
    var startSplit = "";
    var endSplit = "";
    for(var i = 0; i <= globalCustomerName.length - 1; i++){
        if(startMonth[i] != "" && endMonth[i] != ""){
            startSplit = startMonth[i].split("-");
            endSplit = endMonth[i].split("-");
        }
        data.addRows([
            [globalCustomerName[i], applianceInUse[i] + " Appliances",  new Date(startSplit[0], startSplit[1] - 1, startSplit[2]), new Date(endSplit[0], endSplit[1] - 1, endSplit[2])],
        ]);
        var options = {
            height: 450,
            timeline: {
                groupByRowLabel: true,
                // singleColor: '#8d8'
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
    allDates = startMonth.concat(endMonth);
    console.log('All: ');
    console.log(allDates.toString());
    console.log("All Sorted: ");
    console.log(allDates.sort().toString());
    var allDatesToString = allDates.toString();
    allDatesToStringSplit = allDatesToString.split("-");
    console.log("Splitted Dates: ");
    console.log(allDatesToStringSplit.toString());
}
// For charting End

//Formatting date from yyyy-MM-dd to M/dd/yyyy
function formatDate(d){
    var date = new Date(d);
    return (date.getMonth() + 1) + '/' + (date.getDate() + 1) + '/' +  date.getFullYear();
}