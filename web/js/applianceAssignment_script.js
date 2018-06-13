$(document).ready(function () {

    $('#appliance').focus();
    $("[id$=date]").datepicker({dateFormat: "yy-mm-dd"});

    $('#search_form').on('submit', applianceAssignment);

    $(document).ready(function () {
        $('#search_form').submit();
    });

    $(document).on('click', '#drive_table a', function (e) {
        e.stopPropagation();
    });

    $("#create_new_appliance").click(function () {
        window.location.href = 'createApplianceAssignment.jsp';
    });

    $(document).on('click', '#drive_table tr', function () {
        var id = $(this).attr('id').replace('tr_', '');
        getValuesById(id);

        $('#details_change_customer').html("<a href='createApplianceAssignment.jsp?appliance=" + appliance + "&" +
            "current=" + current + "&" +
            "previous=" + previous + "&" +
            "update=true" + "'>Click here to use this appliance configuration for another appliance</a>");
        $('#details_modal_appliance').html(appliance);
        $('#details_modal_current').html(current);
        $('#details_modal_previous').html(previous);
        $('#details_modal_updated_by').html(updated_by);
        $('#details_modal_last_updated').html(formatDate(last_updated));
        $('#details_modal_version').html(version);

        $('#details_modal_spinner').hide();
        $('#detailsModal').modal();
    });

    $(document).on('click', 'button[name="deleteButton"]', function (e) {

        var id = $(this).attr('id').replace('delete_', '');
        var appliance = $('#appliance' + id).val();
        var current = $('#current' + id).val();
        var previous = $('#previous' + id).val();
        var username = $('#username').val();
        var version = $('#version' + id).val();

        bootbox.confirm("Are you sure you want to delete this appliance?",
            function (result) {
                if (result == true) {
                    $.post("deleteApplianceAssignment",
                        {
                            appliance: appliance,
                            current: current,
                            previous: previous,
                            deleted_by: username,
                            version: version
                        },
                        function (data) {
                            $('#modal_spinner').hide();

                            if (data.result == 'success') {
                                alert("Appliance deleted.");
                                $('#search_form').submit();
                            }
                            else
                                alert("Error: " + data.result);
                        }, 'json');
                } //if ends here
            });//bootbox ends here

        e.stopPropagation();
    });

    $(document).on('click', "button[name='updateButton']", function (e) {
        var id = $(this).attr('id').replace('update_', '');
        getValuesById(id);

        $('#change_customer').html("<a href='createApplianceAssignment.jsp?appliance=" + appliance + "&" +
            "current=" + current + "&" +
            "previous=" + previous + "&" +
            "update=true" + "'>Click here to use this appliance configuration for another customer</a>");
        $('#modal_appliance').val(appliance);
        $('#modal_current').val(current);
        $('#modal_previous').val(previous);
        $('#modal_version').val(version);

        // $('#modal_property').val(property);
        // $('#modal_property').change();

        $('#modal_spinner').hide();
        $('#updateModal').modal();

        e.stopPropagation();
    });

    $(document).on('click', '#modalUpdateButton', function () {
        var appliance = $('#modal_appliance').val();
        //alert("TESTING MODAL_APPLIANCE" + $('#modal_appliance').val());
        alert("Appliance " + $('#modal_appliance').val() + " updated.")
        var current = $('#modal_current').val();
        var previous = $('#modal_previous').val();
        var version = $('#modal_version').val();
        var username = $('#username').val();

        if (appliance == null || appliance == "") {
            alert("An appliance must have a valid IP address.");
            $("#modal_appliance").focus();

            return;
        }

        $('#modal_spinner').show();

        $.post("updateApplianceAssignment",
            {
                appliance: appliance,
                current: current,
                previous: previous,
                version: version,
                updated_by: username
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
});

function applianceAssignment() {
    var appliance = $('#appliance').val();
    var current = $('#current').val();
    var previous = $('#previous').val();

    $('#page_spinner').show();

    $.post("applianceAssignment",
        {
            appliance: appliance,
            current: current,
            previous: previous
            // updated_by: "",
            // last_updated: "",
            // version: ""
        },
        function (data) {
            $('#appliance_list').empty();

            if (data.totalMatches == 0) {
                var msg = "No drives found for the selection, please try different combination or "
                    + "update=false" + "'>create a drive</a>";

                $('#appliance_list').append(msg);
            }
            else {
                // var value = "<p>Total Appliances: " + data.totalMatches +"</p>";
                var value = "<p>Total appliances (" + data.totalMatches + ") * 150 GB/day = [" + data.totalMatches * 150 + " GB/day]" + "</p>";
                value += "<table id='drive_table' class='table table-condensed table-hover tablesorter'>";
                value += "<thead>";
                value += "<tr style='background-color:#D8D8D8'>";
                value += "<th>Appliance (IP)</th>";
                value += "<th>Current</th>";
                value += "<th>Previous</th>";
                value += "<th>Updated by</th>";
                value += "<th>Last updated</th>";
                value += "<th>Version</th>";
                value += "<th>Operations</th>";
                value += "</tr>";
                value += "</thead>";
                value += "<tbody>";

                var i = 1;
                $.each(data.drives, function (k, v) {
                    value += "<tr class='detail' id='tr_" + i + "'>";

                    if (v.appliance == undefined) {
                        value += "<td>" + "Error: " + v.message + "</td>";
                    } else {
                        value += "<td>" + v.appliance + "</td>";
                        value += "<td>" + v.current + "</td>";
                        value += "<td>" + v.previous + "</td>";
                        value += "<td>" + v.updated_by + "</td>";
                        value += "<td>" + formatDate(v.last_updated) + "</td>";
                        value += "<td>" + v.version + "</td>";

                        value += "<td><button name ='updateButton' class='btn btn-mini' id='update_" + i + "'><i class='icon-edit'></i></button>";

                        if ($('#username').val() == "nokada" || $('#username').val() == "nlee" || $('#username').val() == "mihuang")
                            value += "&nbsp;<button name ='deleteButton' class='btn btn-mini btn-danger' id='delete_" + i + "'><i class='icon-trash'></i></button>";

                        value +=
                            "<input type='hidden' id='appliance" + i + "' value='" + v.appliance + "'>" +
                            "<input type='hidden' id='current" + i + "' value='" + v.current + "'>" +
                            "<input type='hidden' id='previous" + i + "' value='" + v.previous + "'>" +
                            "<input type='hidden' id='updated_by" + i + "' value='" + v.updated_by + "'>" +
                            "<input type='hidden' id='last_updated" + i + "' value='" + formatDate(v.last_updated) + "'>" +
                            "<input type='hidden' id='version" + i + "' value='" + v.version + "'>" +
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
    updated_by = $('#updated_by' + id).val();
    last_updated = $('#last_updated' + id).val();
    version = $('#version' + id).val();
}

//Formatting date from yyyy-MM-dd to MMMM-dd-yyyy
function formatDate(d) {
    //d += " PST"; // Can change timezones at will, EDT, EST, etc.
    const months = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];
    var date = new Date(d);
    date = new Date(date.getTime() + date.getTimezoneOffset() * 60 * 1000);
    var month = months[date.getMonth()];
    var day = date.getDate();
    return (month + " " + day + ", " + date.getFullYear());
    //return (date.getMonth() + 1) + '/' + (date.getDate() + 1) + '/' + date.getFullYear();
}




