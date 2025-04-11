const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: $("#detailsForm").serialize(),  // This will send as application/x-www-form-urlencoded
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    }).fail(function (xhr) {
        failNoty(xhr);
    });
}

function enableUser(id, enabled) {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + id + "/enable",
        data: "enabled=" + enabled
    }).done(function () {
        const row = $("#" + id);
        row.toggleClass("disabled", !enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    });
}