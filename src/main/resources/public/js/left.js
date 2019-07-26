var loginUser;
$(function () {
    initUser();

})

function initUser() {
    $.ajax({
        type: "get",
        url: "/serverestimate/userController/getUser",
        data: null,
        async: true,
        success: function (result) {
            user=result;
            // window.parent.longinUser=user;
            $("#usernameSpan").html(user.truename);
        },
        error: function (result) {
            console.log("failed")
        }
    });
}