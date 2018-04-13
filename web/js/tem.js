/**
 * Created by nlee on 9/13/16.
 */
$(document).ready(function () {

    var tem = {
        " ": " ",
        "Aimee": "Aimee",
        "Ann": "Ann",
        "Khoj": "Khoj",
        "Joan": "Joan",
        "Ellen": "Ellen",
        "Glenn": "Glenn",
        "Sara": "Sara"

    };

    $.each(tem, function (val, text) {
        $("select[id*=tem]").append(
            $('<option></option>').val(val).html(text)
        );
    });
});
