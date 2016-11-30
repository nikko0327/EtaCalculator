$(document).ready(function() {
    //
    $('#appliance').focus();

    $.urlParam = function(name) {
        return getFromURL(name);
    }

    $("form").on('submit', function() {
        appliance = $('#appliance').val();
        current = $('#current').val();
        previous = $('#previous').val();


        $('#spinner').show();

        $.post("createApplianceAssignment",
            {
                appliance : appliance,
                current : current,
                previous : previous

            },
            function(data){
                $('#spinner').hide();
                $('#createApplianceAssignment').hide();
                $('#result').empty();

                if(data.appliance == undefined) {
                    result = '<p><h3>Error:</h3>'
                        + '<br>' + data.message
                        + '</p>';
                }

                else {
                    result = "<p><h3>Drive created in system with following details:</h3>"
                        + "<br>Appliance: " + data.appliance
                        + "<br>Current: " + data.current
                        + "<br>Previous: " + data.previous
                }

                $('#result').append(result);
            }, "json");

        //return false prevents form from reloading/refreshing/going to other page
        return false;
    });
});