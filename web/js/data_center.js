/**
 * Created by nlee on 9/13/16.
 */
$(document).ready(function () {

    var dc = {
        " ": " ",
        "FRA": "FRA",
        "MARK": "MARK",
        "SC4": "SC4"
    };

    $.each(dc, function (val, text) {
        $("select[id*=dc]").append(
            $('<option></option>').val(val).html(text)
        );
    });
});
