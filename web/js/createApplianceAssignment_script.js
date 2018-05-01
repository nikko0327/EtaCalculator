$(document).ready(function () {
    //
    $('#appliance').focus();

    $.urlParam = function (name) {
        return getFromURL(name);
    }

    $("form").on('submit', function () {
        appliance = $('#appliance').val();
        current = $('#current').val();
        previous = $('#previous').val();
        username = $('#username').val();
        version = $('#version').val();

        $('#spinner').show();

        $.post("createApplianceAssignment",
            {
                appliance: appliance,
                current: current,
                previous: previous,
                updated_by: username,
                version: version,
                last_updated: ""
            },
            function (data) {
                $('#spinner').hide();
                $('#createApplianceAssignment').hide();
                $('#result').empty();

                if (data.appliance == undefined || data.appliance == null) {
                    result = '<p><h3>Error:</h3>'
                        + '<br>' + data.message
                        + '</p>';
                } else {
                    result = "<p><h3>A new appliance was created in system with following details:</h3>"
                        + "<br>Appliance: " + data.appliance
                        + "<br>Current: " + data.current
                        + "<br>Previous: " + data.previous
                        + "<br>Created by: " + data.updated_by
                        + "<br>Latest update date: " + data.last_updated
                        + "<br>Version: " + data.version
                }

                $('#result').append(result);
            }, "json");

        //return false prevents form from reloading/refreshing/going to other page
        return false;
    });
});