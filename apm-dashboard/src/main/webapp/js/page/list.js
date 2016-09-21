$(function () {
    $(document).on("click", ".stopNode", function () {
        if (confirm("Pause The Alert Task")) {
            $.post("/alert/update/status", {"status": 1, "id": $(this).attr("alertId")}, function (data) {
                if (data.code == 0) {
                    window.location.reload();
                }
            });
        }
    })

    $(document).on("click", ".playNode", function () {
        if (confirm("Play The Alert Task")) {
            $.post("/alert/update/status", {"status": 0, "id": $(this).attr("alertId")}, function (data) {
                if (data.code == 0) {
                    window.location.reload();
                }
            });
        }
    })
});