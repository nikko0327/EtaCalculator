$(document).ready(function() {

    $('#appliance').focus();
    $("[id$=date]").datepicker({ dateFormat: "yy-mm-dd"});

    $('#search_form').on('submit', applianceAssignment);

    $(document).ready(function() {
        $('#search_form').submit();
    });

    $(document).on('click', '#drive_table a', function(e) {
        e.stopPropagation();
    });

    $(document).on('click', '#drive_table tr', function(){
        var id = $(this).attr('id').replace('tr_','');
        getValuesById(id);

        $('#details_change_customer').html("<a href='createApplianceAssignment.jsp?appliance=" + appliance + "&" +
            "current=" + current + "&" +
            "previous=" + previous + "&" +
            "update=true" + "'>Click here to use this Drive for other customer</a>");
        $('#details_modal_appliance').html(appliance);
        $('#details_modal_current').html(current);
        $('#details_modal_previous').html(previous);
        $('#details_modal_spinner').hide();
        $('#detailsModal').modal();
    });

    $(document).on('click', 'button[name="deleteButton"]', function(e) {

        var id = $(this).attr('id').replace('delete_','');
        var appliance = $('#appliance' + id).val();
        var current = $('#current' + id).val();
        var previous = $('#previous' + id).val();
        var username = $('#username').val();

        bootbox.confirm("Are you sure you want to delete this drive?",
            function(result) {
                if(result == true) {
                    $.post("deleteApplianceAssignment",
                        {
                            appliance : appliance,
                            current : current,
                            previous : previous
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

        $('#change_customer').html("<a href='createApplianceAssignment.jsp?appliance=" + appliance + "&" +
            "current=" + current + "&" +
            "previous=" + previous + "&" +
            "update=true" + "'>Click here to use this Drive for other customer</a>");
        $('#modal_appliance').val(appliance);
        $('#modal_current').val(current);
        $('#modal_previous').val(previous);

        // $('#modal_property').val(property);
        // $('#modal_property').change();

        $('#modal_spinner').hide();
        $('#updateModal').modal();

        e.stopPropagation();
    });

    $(document).on('click', '#modalUpdateButton', function() {
        var appliance = $('#modal_appliance').val();
        var current = $('#modal_current').val();
        var previous = $('#modal_previous').val();
        var username = $('#username').val();

        if(appliance == null || appliance == "") {
            alert("Drive must have Appliance");
            $("#modal_appliance").focus();

            return;
        }

        $('#modal_spinner').show();

        $.post("updateApplianceAssignment",
            {
                appliance : appliance,
                current : current,
                previous : previous
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

function applianceAssignment() {
    var appliance = $('#appliance').val();
    var current = $('#current').val();
    var previous = $('#previous').val();

    $('#page_spinner').show();

    $.post("applianceAssignment",
        {
            appliance : appliance,
            current : current,
            previous : previous
        },
        function(data) {
            $('#appliance_list').empty();

            if(data.totalMatches == 0) {
                var msg = "No drives found for the selection, please try different combination or "
                    + "update=false" + "'>create a drive</a>";

                $('#appliance_list').append(msg);
            }
            else {
                // var value = "<p>Total Appliances: " + data.totalMatches +"</p>";
                var value = "<p>Total Appliances (" + data.totalMatches + ") * 150GB/day: " + data.totalMatches * 150 + " GB per day." + "</p>";
                value += "<table id='drive_table' class='table table-condensed table-hover tablesorter'>";
                value += "<thead>";
                value += "<tr style='background-color:#D8D8D8'>";
                value += "<th>Appliance</th>";
                value += "<th>Current</th>";
                value += "<th>Previous</th>";
                value += "<th>Operations</th>";
                value += "</tr>";
                value += "</thead>";
                value += "<tbody>";

                var i = 1;
                $.each(data.drives, function(k,v) {
                    value += "<tr class='detail' id='tr_"+ i + "'>";

                    if(v.appliance == undefined) {
                        value += "<td>" + "Error: " + v.message + "</td>";
                    }else {
                        value += "<td>" + v.appliance + "</td>";
                        value += "<td>" + v.current + "</td>";
                        value += "<td>" + v.previous + "</td>";

                        value += "<td><button name ='updateButton' class='btn btn-mini' id='update_" + i + "'><i class='icon-edit'></i></button>";

                        if($('#username').val() == "nokada" || $('#username').val() == "nlee")
                            value += "&nbsp;<button name ='deleteButton' class='btn btn-mini btn-danger' id='delete_" + i +"'><i class='icon-trash'></i></button>";

                        value +=
                            "<input type='hidden' id='appliance" + i + "' value='" + v.appliance + "'>" +
                            "<input type='hidden' id='current" + i + "' value='" + v.current +"'>" +
                            "<input type='hidden' id='previous" + i + "' value='" + v.previous + "'>" +
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
    appliance = $('#appliance' + id).val();
    current = $('#current' + id).val();
    previous = $('#previous' + id).val();
}




