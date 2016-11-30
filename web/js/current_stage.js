/**
 * Created by nlee on 9/13/16.
 */
$(document).ready(function() {

    var current_stage = {
        "Waiting for Data" : "Waiting for Data",
        "Pre-processing" : "Pre-processing",
        "Mapping" : "Mapping",
        "Processing" : "Processing",
        "Promotion" : "Promotion"
    };

    $.each(current_stage, function(val, text) {
        $("select[id*=current_stage]").append(
            $('<option></option>').val(val).html(text)
        );
    });
});
