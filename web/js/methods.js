$(document).ready(function() {

    //add startsWith function to the String prototype
    if (typeof String.prototype.startsWith != 'function') {
        String.prototype.startsWith = function (str){
            return this.slice(0, str.length) == str;
        };
    }

});

// Parses URL to get parameters
function getFromURL(name) {
    var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results == null) {
        return "";
    }
    else {
        return decodeURI(results[1]) || "";
    }
}
