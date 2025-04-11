const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === 'display') {
                            return date.replace('T', ' ').substr(0, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function renderEditBtn(data, type, row) {
    if (type === 'display') {
        return '<a onclick="updateMealRow(' + row.id + ');">' +
            '<span class="fa fa-pencil"></span></a>';
    }
}

function updateMealRow(id) {
    $.get(ctx.ajaxUrl + id, function (data) {
        if (data.dateTime) {
            data.dateTime = data.dateTime.replace('T', ' ').substring(0, 16);
        }
        $.each(data, function (key, value) {
            form.find("[name='" + key + "']").val(value);
        });
        $('#editRow').modal();
    });
}


function saveMeal() {
    const meal = {
        id: $("#id").val() ? parseInt($("#id").val()) : null,
        dateTime: $("#dateTime").val() + ":00", // Добавляем секунды
        description: $("#description").val(),
        calories: parseInt($("#calories").val())
    };

    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        contentType: "application/json",
        data: JSON.stringify(meal),
    }).done(function () {
        $("#editRow").modal("hide");
        updateFilteredTable();
        successNoty("Сохранено");
    }).fail(function (jqXHR) {
        failNoty(jqXHR);
    });
}

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: $(".filter").serialize()
    }).done(updateTableByData);
}

function resetFilter() {
    $(".filter")[0].reset();
    $.get(ctx.ajaxUrl, updateTableByData);
}