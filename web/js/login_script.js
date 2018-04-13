$(document).ready(function () {

    //Disable back button on login page
    //works on chrome well
    window.history.forward();

    $('#username').focus();

    //this checks if browser is IE
    if ($.browser.msie) {
        $(".container").empty();

        msg = "<div class='masthead'><h3 class='muted'>Drive Tracking Dashboard</h3></div><hr>";
        msg += "<p class='text-error'>This site is compatible with Chrome or Firefox browsers</p>";
        msg += "<br>";
        msg += "<p>Please download Chrome: <a href='https://www.google.com/intl/en/chrome/browser/' target='_blank'>Download</a></p>";
        msg += "<p>Please download Firefox: <a href='http://www.mozilla.org/en-US/firefox/new/' target='_blank'>Download</a></p>";

        $(".container").append(msg);

        return;
    }

    $("form").on('submit', function () {
        username = $('#username').val();
        password = $('#password').val();

        $('#spinner').show();

        $.post("login",
            {
                username: username,
                password: password
            },
            function (data) {

                if (data.result == "success")
                    window.location.href = "currentProjects.jsp";

                else {
                    $('#password').val('').focus();
                    $('#spinner').hide();
                    $('#error-message').html("<b>" + data.result + "</b>").show();
                }
            }, "json");

        //return false prevents form from reloading/refreshing/going to other page
        return false;
    });
});
