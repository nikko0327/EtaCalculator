/**
 * Created by nlee on 9/13/16.
 */
$(document).ready(function() {

    var import_engr = {
        "Kaito" : "Kaito",
        "Nozomi" : "Nozomi"
    };

    $.each(import_engr, function(val, text) {
        $("select[id*=import_engr]").append(
            $('<option></option>').val(val).html(text)
        );
    });
});
