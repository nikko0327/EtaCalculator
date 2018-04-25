$(document).ready(function () {
    //

    $('#sent_date').datepicker({dateFormat: "yy-mm-dd"});
    $('#received_date').datepicker({dateFormat: "yy-mm-dd"}).datepicker('setDate', new Date());
    $('#created_date').datepicker({dateFormat: "yy-mm-dd"}).datepicker('setDate', new Date());

    $('#customer').focus();

    $.urlParam = function (name) {
        return getFromURL(name);
    }


    $("form").on('submit', function () {
        customer = $('#customer').val();
        jira = $('#jira').val();
        dc = $('#dc').val();
        data_size = $('#data_size').val();
        import_engr = $('#import_engr').val();
        tem = $('#tem').val();
        current_stage = $('#current_stage').val();
        created_date = $('#created_date').val();
        notes = $('#notes').val();
        appliance_count = $('#appliance_count').val();


        $('#spinner').show();

        $.post("createCurrentProjects",
            {
                customer: customer,
                jira: jira,
                dc: dc,
                data_size: data_size,
                import_engr: import_engr,
                tem: tem,
                current_stage: current_stage,
                created_date: created_date,
                notes: notes,
                appliance_count: appliance_count

            },
            function (data) {
                $('#spinner').hide();
                $('#createCurrentProjects').hide();
                $('#result').empty();

                if (data.customer == undefined) {
                    result = '<p><h3>Error: Exceeded Appliance Limit</h3>'
                        + '<br>' + data.message
                        + '</p>'
                }

                else {
                    result = "<p><h3>New current project created in system with following details:</h3>"
                        + "<br>Customer: " + data.customer
                        + "<br>Jira: " + data.jira
                        + "<br>DC: " + data.dc
                        + "<br>Data size: " + data.data_size
                        + "<br>Import engineer: " + data.import_engr
                        + "<br>TEM: " + data.tem
                        + "<br>Current stage: " + data.current_stage
                        + "<br>Created date: " + data.created_date
                        + "<br>Notes: " + data.notes
                        + "<br>Appliance count: " + data.appliance_count
                }

                $('#result').append(result);
            }, "json");

        //return false prevents form from reloading/refreshing/going to other page
        return false;
    });
});