const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

$(".datetime-filter, #dateTime").datetimepicker({
    format: 'Y-m-d H:i',
    step: 15,
    lang: 'ru',
    dayOfWeekStart: 1,
    scrollInput: false
});

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,

                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        return data.replace("T", " ");
                    }
                },
                { "data": "description" },
                { "data": "calories" },
                {
                    "render": renderEditBtn,
                    "orderable": false,
                    "defaultContent": ""
                },
                {
                    "render": renderDeleteBtn,
                    "orderable": false,
                    "defaultContent": ""
                }
            ],
            "createdRow": function (row, data) {
                if (data.excess) {
                    $(row).addClass("excess");
                }
            },
            "order": [[0, "desc"]]
        })
    );
});